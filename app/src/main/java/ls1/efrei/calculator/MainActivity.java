package ls1.efrei.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    List<Integer> digits = new ArrayList<Integer>();
    List<Integer> operators = new ArrayList<Integer>();
    private Handler handler;
    WebView webView;
    TextView operation;
    TextView result;
    Button myButton;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webView = new WebView(this);
        webView.getSettings().setJavaScriptEnabled(true);
        handler = new Handler();
        myButton = new Button(this);
        myButton.setId(2327);
        myButton.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 0.2f));
        myButton.setText("=");
        myButton.setTextSize(30);
        myButton.setBackgroundColor(getResources().getColor(R.color.equals));
        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myClickHandler(v);
            }
        });
        LinearLayout centerLinearLayout = findViewById(R.id.center_layout);
        centerLinearLayout.addView(myButton);
        operation = findViewById(R.id.operation);
        result = findViewById(R.id.result);
        digits.add(R.id._0);
        digits.add(R.id._1);
        digits.add(R.id._2);
        digits.add(R.id._3);
        digits.add(R.id._4);
        digits.add(R.id._5);
        digits.add(R.id._6);
        digits.add(R.id._7);
        digits.add(R.id._8);
        digits.add(R.id._9);

        operators.add(R.id._plus);
        operators.add(R.id._minus);
        operators.add(R.id._divide);
        operators.add(R.id._multiply);
    }



    @SuppressLint("ResourceType")
    public void myClickHandler(View view) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                    try{
                        String input = ((Button)view).getText().toString();
                        if(digits.contains(view.getId()) || operators.contains(view.getId())){
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    operation.setText(operation.getText().toString().concat(String.valueOf(input)));
                                }
                            });

                        }
                        if(view.getId() == 2327){
                            Socket conn = new Socket("10.0.2.2", 9876);

                            DataInputStream dis = new DataInputStream(conn.getInputStream());
                            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                            dos.writeUTF(operation.getText().toString());
                            double res = dis.readDouble();
                            dos.close();
                            dis.close();
                            conn.close();
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    result.setText(String.valueOf(res));
                                }
                            });

                        }
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }

            }
        };
        new Thread(runnable).start();
        //runOnUiThread(runnable);

    }
}