package org.example;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DecimalFormat;
import java.util.Stack;


class MyCalculusRunnable implements Runnable {
    private Socket sock;

    public MyCalculusRunnable(Socket s) {
        sock = s;
    }

    @Override
    public void run() {

        try {
            DataInputStream dis = new DataInputStream(sock.getInputStream());
            DataOutputStream dos = new DataOutputStream(sock.getOutputStream());

            // read the operand
            String op = dis.readUTF();
            System.out.println("Received message: " + op);


            Double res = CalculusServer.doOp(op);

            // send back result
            dos.writeDouble(res);

            dis.close();
            dos.close();
            sock.close();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}

public class CalculusServer {

    private static final String OPERATORS = "+-*/";

    public static double doOp(String expression) throws Exception
    {
        // Remove all whitespace from the expression
        expression = expression.replaceAll("\\s", "");
        // Initialize stacks for operands and operators
        Stack<Double> operands = new Stack<>();
        Stack<Character> operators = new Stack<>();

        // Loop through each character in the expression
        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);

            // If the character is an operator, push it onto the operators stack
            if (OPERATORS.indexOf(c) != -1) {
                operators.push(c);
            }
            // If the character is a digit, extract the full number and push it onto the operands stack
            else if (Character.isDigit(c)) {
                StringBuilder sb = new StringBuilder();
                sb.append(c);

                // Keep appending digits until we reach the end of the number
                while (i < expression.length() - 1 && Character.isDigit(expression.charAt(i + 1))) {
                    sb.append(expression.charAt(i + 1));
                    i++;
                }

                // Push the full number onto the operands stack
                operands.push(Double.parseDouble(sb.toString()));
            }

            // If the operators stack has at least two operators, and the top operator has higher
            // precedence than the second-to-top operator, perform the operation
            while (operators.size() >= 2 && hasHigherPrecedence(operators.peek(), operators.get(operators.size() - 2))) {
                char op = operators.pop();
                double op2 = operands.pop();
                double op1 = operands.pop();

                double result = performOperation(op1, op2, op);
                operands.push(result);
            }
        }

        // Perform any remaining operations
        while (operators.size() > 0) {
            char op = operators.pop();
            double op2 = operands.pop();
            double op1 = operands.pop();

            double result = performOperation(op1, op2, op);
            operands.push(result);
        }

        // The final result is the top operand on the operands stack
        DecimalFormat df = new DecimalFormat("#.##########"); // set the format to two decimal places
        String result = df.format(Double.parseDouble(String.valueOf(operands.pop()))); // format the value as a string
        return Double.parseDouble(result);

    }

    private static boolean hasHigherPrecedence(char op1, char op2) {
        if ((op1 == '*' || op1 == '/') && (op2 == '+' || op2 == '-')) {
            return true;
        } else {
            return false;
        }
    }

    private static double performOperation(double op1, double op2, char op) {
        switch (op) {
            case '+':
                return op1 + op2;
            case '-':
                return op1 - op2;
            case '*':
                return op1 * op2;
            case '/':
                return op1 / op2;
            default:
                return 0;
        }
    }



    public static void main(String[] args) throws Exception {
        // Example of a distant calculator
        ServerSocket ssock = new ServerSocket(9876);
        System.out.println("server hello");
        while (true) { // infinite loop
            Socket comm = ssock.accept();
            System.out.println("connection established");

            new Thread(new MyCalculusRunnable(comm)).start();

        }

    }

}