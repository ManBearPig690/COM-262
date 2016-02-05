// Initial comment test
package com.fleuret.Ch05_TipCalculator;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.widget.TextView.OnEditorActionListener;

import com.fleuret.tipcalculator.R;

import java.text.NumberFormat;

// test from laptop
public class TipCalculatorActivity extends AppCompatActivity{

    //  Define Variable for the widgets
    private TextView pctTextView;
    private EditText billAmountEditText;
    private TextView tipTextView;
    private TextView totalTextView;
    private SeekBar tipSeekBar;
    private Button tipApplyButton;
    private Button resetButton;
    private RadioGroup roundingRadioGroup;
    private Spinner splitSpinner;
    private TextView perPersonTextView;

    // define the SharedPreferences object
    private SharedPreferences savedValues; // used for saving data

    // define instance variables that should be saved
    private float tipPercent = .15f;
    private String billAmountEditString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tip_calculator);

        // get reference to the widgets
        billAmountEditText = (EditText) findViewById(R.id.billAmountEditText);
        pctTextView = (TextView) findViewById(R.id.pctTextView);
        tipTextView = (TextView) findViewById(R.id.tipTextView);
        totalTextView = (TextView) findViewById(R.id.totalTextView);
        resetButton = (Button) findViewById(R.id.resetButton);
        roundingRadioGroup = (RadioGroup) findViewById(R.id.roundingRadioGroup);
        splitSpinner = (Spinner) findViewById(R.id.splitSpinner);
        perPersonTextView = (TextView)findViewById(R.id.perPersonTextView);
        tipSeekBar = (SeekBar) findViewById(R.id.tipSeekBar);
        tipApplyButton = (Button) findViewById(R.id.tipApplyButton);


        setActionListeners();

        savedValues = getSharedPreferences("SavedValues", MODE_PRIVATE); // mode_private means only this app has access to the file

    }



    @Override
    protected void onPause() {

        // save the instance variables
        Editor editor = savedValues.edit(); // based on key value pairs
        editor.putString("billAmountString", billAmountEditString);
        editor.putFloat("tipPercent", tipPercent);
        editor.commit();

        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // get the instance variables
        billAmountEditString = savedValues.getString("billAmountString", ""); // second param is the default value if there is noting in the editor
        tipPercent = savedValues.getFloat("tipPercent", .15f);

        //set the bbill amount on its widget
        billAmountEditText.setText(billAmountEditString);

        // call calculate and display method
        CalculateAndDisplay();
    }


    public void CalculateAndDisplay() {

        // get bill amount from UI/Variable
        billAmountEditString = billAmountEditText.getText().toString();
        float billAmount; // for holding final billamount
        float splitAmount = 0;
        int checkedRounding = roundingRadioGroup.getCheckedRadioButtonId();

        float tipAmount = 0; //= billAmount * tipPercent;
        float totalAmount = 0; //= billAmount + tipAmount;

        // parse bill amout into float
        if(billAmountEditString.equals(""))
            billAmount = 0;
        else{
            billAmount = Float.parseFloat(billAmountEditString);
            if(checkedRounding == R.id.tipRadioButton){
                tipAmount = StrictMath.round(billAmount * tipPercent);
                totalAmount = billAmount + tipAmount;
                tipPercent = tipAmount/billAmount;
            }
            else if(checkedRounding == R.id.totalRadioButton){
                float tipNotRounded = billAmount * tipPercent;
                totalAmount = StrictMath.round(billAmount + tipNotRounded);
                tipAmount = totalAmount - billAmount;
                tipPercent = tipAmount / billAmount;
            }
            else{
                tipAmount = billAmount * tipPercent;
                totalAmount = billAmount + tipAmount;
            }

            switch(splitSpinner.getSelectedItem().toString())
            {
                case "No":
                    splitAmount = billAmount;
                    break;
                case "2 Ways":
                    splitAmount = billAmount / 2;
                    break;
                case "3 Ways":
                    splitAmount = billAmount / 3;
                    break;
                case "4 Ways":
                    splitAmount = billAmount / 4;
                    break;
                default:
                    splitAmount = billAmount;
                    break;
            }
        }


        // calculate tip and total




        // display other results with formatting
        NumberFormat currency = NumberFormat.getCurrencyInstance(); // used to format to currecny
        NumberFormat percent = NumberFormat.getPercentInstance(); // used to format to %

        // set the text with the proper formatting
        tipTextView.setText(currency.format(tipAmount));
        totalTextView.setText(currency.format(totalAmount));
        pctTextView.setText(percent.format(tipPercent));
        perPersonTextView.setText(currency.format(splitAmount));

    }

    // sets anonymous listeners for the clicks and edits etc.
    private void setActionListeners(){

        // edit listener
        billAmountEditText.setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
                    CalculateAndDisplay();
                }
                return false;
            }
        });


        resetButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                customOnClickListener(v);
            }
        });

        // spinner listener
        splitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                CalculateAndDisplay();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });

        // listener for spinner is ther implementation like others?
        roundingRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                CalculateAndDisplay();

            }
        });
        tipApplyButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                customOnClickListener(v);
            }
        });
        tipSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                perPersonTextView.setText(progress + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                tipPercent = (float) progress/100;
                if(tipPercent < .15f) {
                    tipPercent = .15f;
                    seekBar.setProgress(15);
                }
                NumberFormat percent = NumberFormat.getPercentInstance(); // used to format to %
                pctTextView.setText(percent.format(tipPercent));
                //CalculateAndDisplay(); // the specs did not say to recalculate on change only 'Apply' click
            }
        });
    }

    public void customOnClickListener(View v) {
        switch(v.getId())
        {
            case R.id.tipApplyButton:
                tipPercent = (float)tipSeekBar.getProgress() / 100;
                CalculateAndDisplay();
                break;
            case R.id.resetButton:
                billAmountEditText.setText("");
                tipPercent = .15f;
                roundingRadioGroup.check(R.id.noneRadioButton);
                splitSpinner.setSelection(0);
                CalculateAndDisplay();
                break;
            default:
                //CalculateAndDisplay();
                break;

        }

    }
}
