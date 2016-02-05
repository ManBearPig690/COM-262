package com.example.jamesfleuret.ch03_ex1_invoice;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.content.SharedPreferences.Editor;
import android.widget.TextView.OnEditorActionListener;

import java.text.NumberFormat;

public class InvoiceActivity extends AppCompatActivity
implements OnEditorActionListener{

    // widget variables
    private TextView discountAmountTextView;
    private TextView discountPercentTextView;
    private TextView totalTextView;
    private EditText subtotalEditText;

    private SharedPreferences savedValues;

    // instance variables
    private float discountAmount = .1f;
    private String subtotalEditString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);

        // get references to the widgets
        discountAmountTextView = (TextView) findViewById(R.id.discountAmountTextView);
        discountPercentTextView = (TextView) findViewById(R.id.discountPercentTextView);
        totalTextView = (TextView)findViewById(R.id.totalTextView);
        subtotalEditText = (EditText) findViewById(R.id.subtotalEditText);


        // set listeners
        subtotalEditText.setOnEditorActionListener(this);

        savedValues = getSharedPreferences("SavedValues", MODE_PRIVATE);

    }

    private void CalculateAndDisplay()
    {
        subtotalEditString = subtotalEditText.getText().toString();
        float subtotal;

        // parse the subtotal
        if(subtotalEditString.equals(""))
            subtotal = 0;
        else
            subtotal = Float.parseFloat(subtotalEditString);

        float discountPct = 0;

        if(subtotal >= 200)
            discountPct = .2f;
        else if(subtotal >= 100 && subtotal < 200)
            discountPct = .1f;


        float discountAmt = subtotal * discountPct;
        float total = subtotal - discountAmt;

        // display formated results
        NumberFormat currency = NumberFormat.getCurrencyInstance();
        NumberFormat percent = NumberFormat.getPercentInstance();

        discountAmountTextView.setText(currency.format(discountAmt));
        discountPercentTextView.setText(percent.format(discountPct));
        totalTextView.setText(currency.format(total));



    }

    @Override
    protected void onPause() {
        // save the values
        Editor editor = savedValues.edit();
        editor.putString("subtotalString", subtotalEditString);
        editor.commit();

        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();
        subtotalEditString = savedValues.getString("subtotalString", "");
        subtotalEditText.setText(subtotalEditString);

        CalculateAndDisplay();
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
    {
        if(actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_UNSPECIFIED)
            CalculateAndDisplay();

        return false;
    }




}
