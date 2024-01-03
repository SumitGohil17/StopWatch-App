package com.example.interstopwatch;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the layout for the splash screen
        setContentView(R.layout.splash_screen);

        // Create a new thread to handle the splash screen delay
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    // Pause execution for 2000 milliseconds (2 seconds)
                    sleep(2000);

                    // Create an Intent to launch the MainActivity
                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);

                    // Start the MainActivity
                    startActivity(intent);

                    // Finish the splash screen activity
                    finish();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                super.run();
            }
        };

        // Start the thread
        thread.start();
    }
}
