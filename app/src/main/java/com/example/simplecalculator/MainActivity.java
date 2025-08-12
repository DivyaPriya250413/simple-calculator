package com.example.simplecalculator;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;



public class MainActivity extends AppCompatActivity {

    TextView display;
    String input = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        display = findViewById(R.id.textView_display);

        int[] buttonIds = {
                R.id.btn_0, R.id.btn_1, R.id.btn_2, R.id.btn_3, R.id.btn_4,
                R.id.btn_5, R.id.btn_6, R.id.btn_7, R.id.btn_8, R.id.btn_9,
                R.id.btn_plus, R.id.btn_minus, R.id.btn_multiply, R.id.btn_divide,
                R.id.btn_dot
        };

        View.OnClickListener listener = v -> {
            Button b = (Button) v;
            input += b.getText().toString();
            display.setText(input);
        };

        for (int id : buttonIds) {
            findViewById(id).setOnClickListener(listener);
        }

        findViewById(R.id.btn_clear).setOnClickListener(v -> {
            input = "";
            display.setText("0");
        });

        findViewById(R.id.btn_delete).setOnClickListener(v -> {
            if (!input.isEmpty()) {
                input = input.substring(0, input.length() - 1);
                display.setText(input.isEmpty() ? "0" : input);
            }
        });

        findViewById(R.id.btn_equal).setOnClickListener(v -> {
            try {
                double result = eval(input);
                display.setText(String.valueOf(result));
                input = String.valueOf(result);
            } catch (Exception e) {
                display.setText("Error");
                input = "";
            }
        });
    }

    // Simple expression evaluator

    private double eval(String expression) {
        try {
            Context rhino = Context.enter();
            rhino.setOptimizationLevel(-1); // Required for Android

            Scriptable scope = rhino.initStandardObjects();
            Object result = rhino.evaluateString(scope,
                    expression.replace("×", "*").replace("÷", "/"),
                    "JavaScript", 1, null);

            return Double.parseDouble(result.toString());
        } catch (Exception e) {
            return 0;
        } finally {
            Context.exit();
        }
    }

}
