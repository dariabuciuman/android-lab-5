package com.example.lab5;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText guessEditText;
    TextView result;
    Button checkButton;
    int min = 1;
    int max = 15;
    int random = getRandomNumber(min, max);
    boolean isWinner = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        guessEditText = findViewById(R.id.guessEditText);
        result = findViewById(R.id.resultTextView);
        checkButton = findViewById(R.id.checkButton);
        System.out.println("Chosen number is " + random);
    }

    static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public void makeToast(String string) {
        Toast.makeText(MainActivity.this, string, Toast.LENGTH_SHORT).show();
    }

    public void onClickCheck(View view) {
        int number = Integer.parseInt(guessEditText.getText().toString());
        if (number < random) {
            makeToast("Nope, think of a higher number");
        } else if (number > random) {
            makeToast("Nope, think of a lower number");
        } else {
            makeToast("Nice, you got it!");
            result.setText("You won!\nThe number was " + random);
            isWinner = true;
        }
    }


    public void onClickStartGeneration(View view) {
        makeToast("Thread started! Hurry up!");
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!isWinner) {
                    int numberGuessedByThread = getRandomNumber(min, max);
                    System.out.println("The thread guessed: " + numberGuessedByThread);

                    if (numberGuessedByThread == random) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                result.setText("You lost, we won!\n The number was " + random);
                            }
                        });
                        isWinner = true;
                    } else {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
        if(isWinner) {
            guessEditText.setFocusable(false);
            checkButton.setFocusable(false);
        }
    }

    public void onClickNewGame(View view) {
        isWinner = false;
        result.setText("Game restarted. You haven't guessed yet");
        random = getRandomNumber(min, max);
        guessEditText.setFocusableInTouchMode(true);
        checkButton.setFocusableInTouchMode(true);
        guessEditText.setText("");
        System.out.println("New chosen number is: " + random);
    }
}