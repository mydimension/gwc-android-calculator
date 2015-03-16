package com.example.william.calculator;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;

public class MainActivity extends Activity {
    private Button operation;
    private Double operand;
    private String bufferDefault;
    private Boolean nextOperand;
    private DecimalFormat df;
    private TextView buffer;
    private Drawable activeOperator;
    private Drawable inactiveOperator;

    public MainActivity() {
        super();
        nextOperand = true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        df = new DecimalFormat(getString(R.string.decimal_format));
        buffer = (TextView) findViewById(R.id.buffer);
        bufferDefault = getResources().getString(R.string.buffer_default);
        activeOperator = getResources().getDrawable(R.drawable.active_operator);
        inactiveOperator = getResources().getDrawable(android.R.drawable.btn_default);

        findViewById(R.id.clear).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onClear(v, true);

                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) return true;

        return super.onOptionsItemSelected(item);
    }

    public void onNumberClick(View v) {
        Button btn = (Button) v;
        Integer value = Integer.parseInt(btn.getText().toString());

        if (nextOperand) {
            setBufferText(value);
            nextOperand = false;
        } else {
            appendBufferText(value);
        }
    }

    public void onPeriodClick(View v) {
        Button btn = (Button) v;

        if (!getBufferText().contains(".")) appendBufferText(btn.getText().toString());

        nextOperand = false;
    }

    public void onOperatorClick(View v) {
        Button btn = (Button) v;

        if (operand == null) {
            operand = getBufferValue();
        } else {
            operand = doOp(operand, getBufferValue(), btn);
            setBufferText(operand);
            operation.setBackground(inactiveOperator);
        }

        btn.setBackground(activeOperator);

        operation = btn;
        nextOperand = true;
    }

    public void onEqualClick(View v) {
        Double result = doOp(operand, getBufferValue(), operation);

        setBufferText(result);

        operation.setBackground(inactiveOperator);
        operation = null;
        operand = null;
        nextOperand = true;
    }

    public void onSignClick(View v) {
        setBufferText(getBufferValue() * -1);
    }

    private void onClear(View v, Boolean longPress) {
        Button btn = (Button) v;

        if (longPress || (btn.getId() == R.id.allclear)) {
            setBufferText(bufferDefault);
            nextOperand = true;
        } else {
            String buf = getBufferText();
            if (buf != null && buf.length() > 0) {
                setBufferText(buf.substring(0, buf.length() - 1));
            }
        }

        if (!nextOperand && getBufferText().equals(bufferDefault)) {
            nextOperand = true;
        }

        if (btn.getId() == R.id.allclear) {
            operand = null;
            if (operation != null) {
                operation.setBackground(inactiveOperator);
                operation = null;
            }
        }
    }

    public void onClear(View v) {
        onClear(v, nextOperand);
    }

    private Double doOp(Double l, Double r, Button op) {
        switch (op.getId()) {
            case R.id.plus:
                return l + r;
            case R.id.minus:
                return l - r;
            case R.id.multiply:
                return l * r;
            case R.id.divide:
                return (r == 0.0) ? 0.0 : l / r;
            default:
                return 0.0;
        }
    }

    private String getBufferText() {
        return buffer.getText().toString();
    }

    private void setBufferText(String s) {
        buffer.setText(s.length() == 0 ? getResources().getString(R.string.buffer_default) : s);
    }

    private void setBufferText(Number d) {
        setBufferText(df.format(d));
    }

    private Double getBufferValue() {
        return Double.valueOf(getBufferText());
    }

    private void appendBufferText(String s) {
        setBufferText(getBufferText() + s);
    }

    private void appendBufferText(Number d) {
        setBufferText(getBufferText() + d.toString());
    }

}
