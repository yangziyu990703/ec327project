package com.example.vpet;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //initialize variables.
    private static final int READ_BLOCK_SIZE = 100 ;
    private ImageButton pet;
    private ProgressBar pgsBar;
    private double i = 0;
    private Handler hdlr = new Handler();
    public double should;
    public static double state;
    public static double cumulative = 0;
    public static double weight_d;
    public static double height_d;
    public static double bmi;
    //Lines for virtual pet.
    String[] conver = new String[]{"Hi!", "Wow!", "Hello!", "Ok!"};
    String[] conver2 = new String[]{"feed me", "WOW", "Hello", };

    @SuppressLint({"WrongViewCast", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //initialize UIs in activity_main.xml by using findViewById.
        pet = (ImageButton) findViewById(R.id.pet);
        ImageButton pet2 = (ImageButton) findViewById(R.id.pet2);
        Button feed = (Button) findViewById(R.id.Feed);
        pgsBar = (ProgressBar) findViewById(R.id.progressBar);
        TextView Wt = (TextView) findViewById(R.id.weight);
        TextView BMI = (TextView) findViewById(R.id.bmi);
        TextView Ht = (TextView) findViewById(R.id.height);
        TextView hunger = (TextView) findViewById(R.id.hunger);
        TextView txt1 = (TextView) findViewById(R.id.text1);
        TextView txt2 = (TextView) findViewById(R.id.text2);
        TextView txt3 = (TextView) findViewById(R.id.text3);
        //receive data from feed.java and EditValues.java by using intent.
        Intent datashare = getIntent();
        weight_d = datashare.getDoubleExtra("weight_d", weight_d);
        height_d = datashare.getDoubleExtra("height_d", height_d);
        bmi = datashare.getDoubleExtra("bmi", bmi);
        double calories = datashare.getDoubleExtra("calorie_t", 0);
        final double should = datashare.getDoubleExtra("should",0);
        state = datashare.getDoubleExtra("state",0);


        try {
            FileInputStream fileIn=openFileInput("weight.txt");
            InputStreamReader InputRead= new InputStreamReader(fileIn);
            char[] inputBuffer= new char[READ_BLOCK_SIZE];
            int charRead;
            charRead=InputRead.read(inputBuffer);
            weight_d = Double.parseDouble(String.copyValueOf(inputBuffer,0,charRead));
            InputRead.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            FileInputStream fileIn=openFileInput("height.txt");
            InputStreamReader InputRead= new InputStreamReader(fileIn);
            char[] inputBuffer= new char[READ_BLOCK_SIZE];
            int charRead;
            charRead=InputRead.read(inputBuffer);
            height_d = Double.parseDouble(String.copyValueOf(inputBuffer,0,charRead));
            InputRead.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            FileInputStream fileIn=openFileInput("bmi.txt");
            InputStreamReader InputRead= new InputStreamReader(fileIn);
            char[] inputBuffer= new char[READ_BLOCK_SIZE];
            int charRead;
            charRead=InputRead.read(inputBuffer);
            bmi = Double.parseDouble(String.copyValueOf(inputBuffer,0,charRead));
            InputRead.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


        //Use setText to show variables on the main layout.
        Wt.setText(String.format("Weight ：%s", weight_d));
        Ht.setText(String.format("Height ：%s", height_d));
        BMI.setText(String.format("BMI : %s", bmi));

       

        //Set the maximum value of the progress bar to the calculated value.
        pgsBar.setMax((int)Math.round(weight_d*30));
        cumulative = cumulative + calories;
        int value= pgsBar.getProgress();
        //Add daily intakes to the stomach bar.
        pgsBar.setProgress((int) Math.round(cumulative));
        if (cumulative>weight_d*30){
            pet.setVisibility(View.INVISIBLE);
            pet2.setVisibility(View.VISIBLE);
        }
        else{
            pet.setVisibility(View.VISIBLE);
            pet2.setVisibility(View.INVISIBLE);
        }
    }

//Functions for buttons.
    @Override
    public void onClick(View v) {
        Intent datashare = getIntent();
        final double should = datashare.getDoubleExtra("should",0);
        TextView txt1 = (TextView) findViewById(R.id.text1);
        TextView txt2 = (TextView) findViewById(R.id.text2);
        TextView txt3 = (TextView) findViewById(R.id.text3);
        //A reset button that reset the progress bar.
        if (v.getId() == R.id.reset) {
            cumulative =0;
            pgsBar.setProgress(0);
        }
        //An edit button that bring user to edit_value.xml.
        if (v.getId() == R.id.edit) {
            Intent open1 = new Intent(MainActivity.this, EditValues.class);
            startActivity(open1);
        }
        //An feed button that bring user to feed.xml.
        if (v.getId() == R.id.Feed) {
            Intent open2 = new Intent(MainActivity.this, feed.class);
            startActivity(open2);
        }
        //An intro button that bring user to intro.xml.
        if (v.getId() == R.id.intro) {
            Intent open3 = new Intent(MainActivity.this, intro.class);
            startActivity(open3);
        }
        //Set the virtual pet as a button.
        if (v.getId() == (R.id.pet)||v.getId() == (R.id.pet2)) {
            Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
            pet.startAnimation(shake);
            if (i <= 0.8*weight_d*30) {
                Random mRand = new Random();
                //Show random conversation at random position.
                int x = mRand.nextInt(3);
                if (x == 0) {
                    txt1.setVisibility(View.VISIBLE);
                    Random r = new Random();
                    int index = r.nextInt(conver.length);
                    txt1.setText(conver[index]);
                    txt2.setVisibility(View.INVISIBLE);
                    txt3.setVisibility(View.INVISIBLE);
                } else if (x == 1) {
                    txt2.setVisibility(View.VISIBLE);
                    Random r = new Random();
                    int index = r.nextInt(conver.length);
                    txt2.setText(conver[index]);
                    txt1.setVisibility(View.INVISIBLE);
                    txt3.setVisibility(View.INVISIBLE);
                } else if (x == 2) {
                    txt3.setVisibility(View.VISIBLE);
                    Random r = new Random();
                    int index = r.nextInt(conver.length);
                    txt3.setText(conver[index]);
                    txt1.setVisibility(View.INVISIBLE);
                    txt2.setVisibility(View.INVISIBLE);
                }
            }
            //if the pet is hungry, show different conversation.
            else if (i >= 0.8*weight_d*30) {
                Random mRand = new Random();
                int x = mRand.nextInt(3);
                if (x == 0) {
                    txt1.setVisibility(View.VISIBLE);
                    Random r = new Random();
                    int index = r.nextInt(conver2.length);
                    txt1.setText(conver2[index]);
                    txt2.setVisibility(View.INVISIBLE);
                    txt3.setVisibility(View.INVISIBLE);
                } else if (x == 1) {
                    txt2.setVisibility(View.VISIBLE);
                    Random r = new Random();
                    int index = r.nextInt(conver2.length);
                    txt2.setText(conver2[index]);
                    txt1.setVisibility(View.INVISIBLE);
                    txt3.setVisibility(View.INVISIBLE);
                } else if (x == 2) {
                    txt3.setVisibility(View.VISIBLE);
                    Random r = new Random();
                    int index = r.nextInt(conver2.length);
                    txt3.setText(conver2[index]);
                    txt1.setVisibility(View.INVISIBLE);
                    txt2.setVisibility(View.INVISIBLE);
                }
            }
            //If user click the background, all the conversation become invisible.
            if (v.getId() == R.id.container) {
                txt1.setVisibility(View.INVISIBLE);
                txt2.setVisibility(View.INVISIBLE);
                txt3.setVisibility(View.INVISIBLE);
            }
        }
    }
}

