package com.example.vpet;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class EditValues extends AppCompatActivity {

    public double bmi = 0;
    public int state = 0;
    public double should = 0;
    public EditText weight;
    public EditText height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.edit_values);

        //get input
        weight = (EditText) findViewById(R.id.editText);
        height = (EditText) findViewById(R.id.editText2);

        Button finished = (Button) findViewById(R.id.edit_return);

        //click button to finish activity
        finished.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculationEdit();
                finish();
            }
        });
    }

    public void calculationEdit() {

        //check if input is empty
        String weight_s = weight.getText().toString();
        String height_s = height.getText().toString();

        if(TextUtils.isEmpty(weight_s) || TextUtils.isEmpty(height_s)) {
            return;
        }

        else {

            //converting input to double, calculating bmi and state
            double weight_d = Double.parseDouble(weight.getText().toString());
            double height_d = Double.parseDouble(height.getText().toString());

            bmi = weight_d / (height_d * height_d);

            if (bmi < 18.5)
                state = 1; // underweight
            else if (bmi >= 18.5 && bmi <= 24.9)
                state = 2; // normal
            else if (bmi >= 25 && bmi <= 29.9)
                state = 3; // overweight
            else if (bmi >= 30 && bmi <= 34.9)
                state = 4; // obese
            else if (bmi >= 35)
                state = 5; // extremely obese

            should = weight_d * 28;

            //making MainActivity able to access data here
            Intent sendingIntent = new Intent(EditValues.this, MainActivity.class);
            sendingIntent.putExtra("weight_d", weight_d);
            sendingIntent.putExtra("height_d", height_d);
            sendingIntent.putExtra("state", state);
            sendingIntent.putExtra("bmi", bmi);
            startActivity(sendingIntent);

            try {
                FileOutputStream fileout = openFileOutput("weight.txt", MODE_PRIVATE);
                OutputStreamWriter outputWriter = new OutputStreamWriter(fileout);
                outputWriter.write(String.valueOf(weight_d));
                outputWriter.close();
                Toast.makeText(getBaseContext(), weight_d + "successfully saved",
                        Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                FileOutputStream fileout = openFileOutput("height.txt", MODE_PRIVATE);
                OutputStreamWriter outputWriter = new OutputStreamWriter(fileout);
                outputWriter.write(String.valueOf(height_d));
                outputWriter.close();
                Toast.makeText(getBaseContext(), height_d + "successfully saved",
                        Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                FileOutputStream fileout = openFileOutput("bmi.txt", MODE_PRIVATE);
                OutputStreamWriter outputWriter = new OutputStreamWriter(fileout);
                outputWriter.write(String.valueOf(bmi));
                outputWriter.close();
                Toast.makeText(getBaseContext(), bmi + "successfully saved",
                        Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}


