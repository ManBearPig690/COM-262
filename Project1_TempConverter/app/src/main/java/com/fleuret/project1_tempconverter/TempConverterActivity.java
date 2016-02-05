/*
*   Author:         James Fleuret
*
*   Program:        Project 1 Temp Converter
*
*   Date:           08/18/2015
*
*   Desc:           This App will take input from the user and convert it from
 *                  degrees fahrenheit to degrees celsius.
 *                  It also uses whole numbers for user & display simplicity
* */
package com.fleuret.project1_tempconverter;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import java.text.NumberFormat;

public class TempConverterActivity extends AppCompatActivity
implements OnEditorActionListener{
    // Variables for the widgets
    private TextView celsiusResultTextView;
    private EditText fahrenheitEditText;

    // vaiable to hold saved data
    private SharedPreferences savedValues;

    // string for the fahrenheit text
    private String fahrenheitEditString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp_converter);

        // initialize the widget variables
        celsiusResultTextView = (TextView) findViewById(R.id.celciusResultLabel);
        fahrenheitEditText = (EditText) findViewById(R.id.farenheitEditText);

        // set listener
        fahrenheitEditText.setOnEditorActionListener(this);

        // create saved values object
        savedValues = getSharedPreferences("SavedValues", MODE_PRIVATE);

    }

    // overrides the onPause() grab and save values
    @Override
    protected void onPause() {
        // save the instance variables
        Editor editor = savedValues.edit();
        editor.putString("fahrenheitEditString", fahrenheitEditString); // pushes the edit text to th savedValues shared preference.
        editor.commit();

        super.onPause();
    }

    // overrides the onResume() to set the widget text to the saved vaulues
    @Override
    protected void onResume() {
        super.onResume();
        // retrieve the instance variables
        fahrenheitEditString = savedValues.getString("fahrenheitEditString", "");

        // set the fahrenheit widget text
        fahrenheitEditText.setText(fahrenheitEditString);

        // call calculate and dispaly method
        CalculateAndDisplay();
    }

    // listes for the user to be done entering the text
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        // checks for the done click and will calculate the values
        if(actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_UNSPECIFIED)        {
            CalculateAndDisplay();
        }
        return false;
    }

    // calculates / converts and displays the results.
    private void CalculateAndDisplay(){
        // get the text from
        fahrenheitEditString = fahrenheitEditText.getText().toString();
        // variables for the conversion
        float celsiusResult; // the conversion result to be used in the display
        float conversion = 5f/9f; // holds result for the (5/9) portion of the equation needed 5f and 9f because it would result in 0 if not there
        float fahrenheit; // the fahrenheit value converted from the edit string to float
        //checks if the edit text is empty
        if (fahrenheitEditString.equals("")) {
            // if empty result it 0
            celsiusResult = 0f;
        } else {
            // if has value..do the conversion
            fahrenheit = Float.parseFloat(fahrenheitEditString);
            celsiusResult = (fahrenheit - 32) * conversion;
        }

        int result = Math.round(celsiusResult); // convert the result to int
        celsiusResultTextView.setText(Integer.toString(result)); // convert to string for result
    }


}
