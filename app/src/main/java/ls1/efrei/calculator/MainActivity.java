package ls1.efrei.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    List<Integer> digits = new ArrayList<Integer>();
    List<Integer> operators = new ArrayList<Integer>();
    WebView webView;
    TextView operation;
    TextView result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webView = new WebView(this);
        webView.getSettings().setJavaScriptEnabled(true);
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

    public void myClickHandler(View view) {
        try{
        String input = ((Button)view).getText().toString();
        if(digits.contains(view.getId()) || operators.contains(view.getId())){
            operation.setText(operation.getText().toString().concat(String.valueOf(input)));
        }
        if(view.getId() == R.id._equals){
                    webView.evaluateJavascript(operation.getText().toString(), new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String s) {
                           try {
                               double output = Double.parseDouble(s);
                               DecimalFormat df = new DecimalFormat("#.##########"); // set the format to two decimal places
                               String formattedValue = df.format(output); // format the value as a string
                               double roundedValue = Double.parseDouble(formattedValue);
                               result.setText(String.valueOf(roundedValue));
                               operation.setText("");
                           }
                           catch (NumberFormatException e) {
                               Toast.makeText(getApplicationContext(), "Arithmetic Error", Toast.LENGTH_LONG).show();
                               operation.setText("");
                           }
                        }
                    });
                }
        }
        catch (Exception e){
            Toast.makeText(this, "Arithmetic Error", Toast.LENGTH_LONG).show();
        }
    }
}