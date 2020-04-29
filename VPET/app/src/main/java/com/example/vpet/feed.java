package com.example.vpet;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class feed extends MainActivity {

    public double calories = 0;
    public EditText input_calories;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.feed);

        input_calories = (EditText) findViewById(R.id.editText);

        Button confirm = (Button) findViewById(R.id.confirm_feed);

        //click on the button to finish activity
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feedCalculation();
                finish();
            }
        });

    }

    public void feedCalculation()
    {
        //check if input is empty
        String calories_s = input_calories.getText().toString();
        if(TextUtils.isEmpty(calories_s)) {
            return;
        }

        //convert input to double, making MainActivity able to access calories
        calories = Double.parseDouble(input_calories.getText().toString());
        Intent sendingIntent = new Intent(feed.this, MainActivity.class);
        sendingIntent.putExtra("calorie_t", calories);
        startActivity(sendingIntent);

    }
}
