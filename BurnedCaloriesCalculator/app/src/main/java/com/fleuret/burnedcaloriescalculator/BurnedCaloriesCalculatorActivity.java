package com.fleuret.burnedcaloriescalculator;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import org.w3c.dom.Text;

import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class BurnedCaloriesCalculatorActivity extends AppCompatActivity implements OnEditorActionListener, SeekBar.OnSeekBarChangeListener {

    // Variables

    // widgets
    private EditText weightEditText;
    private EditText nameEditText;
    private SeekBar milesRanSeekbar;
    private Spinner feetSpinner;
    private Spinner inchesSpinner;
    private TextView milesRanTextView;
    private TextView calsBurnedTextView;
    private TextView bmiTextView;

    // Globals
    private String weightString;
    private int heightFeet = 5;
    private int heightInches = 6;
    private String milesRanString;
    float calsBurned;
    float bmi;
    int milesRanProgress;
    // define the SharedPreferences object
    private SharedPreferences savedValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_burned_calories_calculator);

        // Grab references to the widgests
        weightEditText = (EditText) findViewById(R.id.weightEditText);
        nameEditText = (EditText) findViewById(R.id.nameEditText);
        milesRanSeekbar = (SeekBar) findViewById(R.id.milesRanSeekbar);
        feetSpinner = (Spinner) findViewById(R.id.feetSpinner);
        inchesSpinner = (Spinner) findViewById(R.id.inchesSpinner);
        milesRanTextView = (TextView) findViewById(R.id.milesRanTextView);
        calsBurnedTextView = (TextView) findViewById(R.id.calsBurnedTextView);
        bmiTextView = (TextView) findViewById(R.id.bmiTextView);

        ArrayAdapter<CharSequence> feetAdapter = ArrayAdapter.createFromResource(this, R.array.feet_array, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> inchesAdapter = ArrayAdapter.createFromResource(this, R.array.inches_array, android.R.layout.simple_spinner_item);
        feetAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inchesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        feetSpinner.setAdapter(feetAdapter);
        inchesSpinner.setAdapter(inchesAdapter);

        // set action listeners
        SetActionListeners();
        feetSpinner.setSelection(4);
        inchesSpinner.setSelection(6);
        milesRanSeekbar.setProgress(1);

        // get sharedpreferences
        savedValues = getSharedPreferences("SavedValues", MODE_PRIVATE);

    }

    @Override
    protected void onPause() {
        // save the values
        Editor editor = savedValues.edit();
        editor.putString("weightAmountString", weightString);
        editor.putInt("milesRan", milesRanProgress);
        editor.putFloat("calsBurned", calsBurned);
        editor.putFloat("bmi", bmi);
        editor.putString("name", nameEditText.getText().toString());
        editor.putInt("heightFeet", heightFeet);
        editor.putInt("heightInches", heightInches);
        editor.commit();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        weightString = savedValues.getString("weightAmountString", "");
        milesRanProgress = savedValues.getInt("milesRan", 1);
        calsBurned = savedValues.getFloat("calsBurned", 0f);
        bmi = savedValues.getFloat("bmi", 0f);
        nameEditText.setText(savedValues.getString("name", ""));
        heightFeet = savedValues.getInt("heightFeet", 5);
        heightInches = savedValues.getInt("heightInches", 6);
        feetSpinner.setSelection(heightFeet - 1);
        inchesSpinner.setSelection(heightInches);
        milesRanSeekbar.setProgress(milesRanProgress);
        weightEditText.setText(weightString);
    }

    private void SetActionListeners(){
        // setst the action listeners
        weightEditText.setOnEditorActionListener(this);
        milesRanSeekbar.setOnSeekBarChangeListener(this);
        nameEditText.setOnEditorActionListener(this);
        feetSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                heightFeet = position + 1;
                CalculateCaloriesBurned();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        inchesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                heightInches = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_burned_calories_calculator, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
            CalculateCaloriesBurned();
        }
        return false;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        milesRanTextView.setText(progress + "mi");
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        CalculateCaloriesBurned();
    }

    private void CalculateCaloriesBurned(){
        weightString = weightEditText.getText().toString();
        float weight;


        if(weightString.equals(""))
            weight = 0f;
        else
            weight = Float.parseFloat(weightString);

        milesRanProgress = milesRanSeekbar.getProgress();
        calsBurned = 0.75f * weight * milesRanProgress;
        bmi = (weight * 703) / ((12 * heightFeet + heightInches)*(12 * heightFeet + heightInches));

        // diplay the results
        DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setMaximumFractionDigits(2);
        NumberFormat intFormat = NumberFormat.getIntegerInstance();
        calsBurnedTextView.setText(new Float(calsBurned).toString());
        bmiTextView.setText(decimalFormat.format(bmi).toString());


    }

}
