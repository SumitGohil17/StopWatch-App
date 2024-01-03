package com.example.interstopwatch;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    // UI elements
    TextView textView;
    FloatingActionButton reset, start, stop, lap;
    ListView listView;

    // Variables for stopwatch functionality
    int seconds, minutes, milliSeconds;
    long millisecondTime, startTime, timeBuff, updateTime = 0L;
    Handler handler;

    // List to store lap times
    ArrayList<String> arrayList = new ArrayList<>();

    // Runnable to update the stopwatch display
    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            millisecondTime = SystemClock.uptimeMillis() - startTime;
            updateTime = timeBuff + millisecondTime;
            seconds = (int) (updateTime / 1000);
            minutes = seconds / 60;
            seconds = seconds % 60;
            milliSeconds = (int) (updateTime % 100);

            // Update the TextView with the formatted time
            textView.setText(MessageFormat.format("{0}:{1}:{2}", minutes, String.format(Locale.getDefault(), "%02d", seconds), String.format(Locale.getDefault(), "%02d", milliSeconds)));

            // Schedule the next update
            handler.postDelayed(this, 0);
        }
    };

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI elements
        textView = findViewById(R.id.chrono);
        reset = findViewById(R.id.reset);
        start = findViewById(R.id.start);
        stop = findViewById(R.id.stop);
        lap = findViewById(R.id.lap);
        listView = findViewById(R.id.listView);

        // Set initial visibility of buttons
        reset.setVisibility(View.INVISIBLE);
        stop.setVisibility(View.INVISIBLE);
        lap.setVisibility(View.INVISIBLE);

        // Initialize ListView for lap times
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, arrayList);
        listView.setAdapter(arrayAdapter);

        // Lap button click listener
        lap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (handler.postDelayed(runnable, 0)) {
                    // Add the current time to the lap list
                    arrayList.add(textView.getText().toString());
                    arrayAdapter.notifyDataSetChanged();
                    Toast.makeText(MainActivity.this, "LAP", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Initialize Handler
        handler = new Handler(Looper.getMainLooper());

        // Start button click listener
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTime = SystemClock.uptimeMillis();
                handler.postDelayed(runnable, 0);
                lap.setVisibility(View.VISIBLE);
                reset.setVisibility(View.INVISIBLE);
                stop.setVisibility(View.VISIBLE);
                start.setVisibility(View.INVISIBLE);

                Toast.makeText(MainActivity.this, "START", Toast.LENGTH_SHORT).show();
            }
        });

        // Stop button click listener
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeBuff += millisecondTime;
                handler.removeCallbacks(runnable);
                lap.setVisibility(View.INVISIBLE);
                reset.setVisibility(View.VISIBLE);
                stop.setVisibility(View.INVISIBLE);
                start.setVisibility(View.VISIBLE);

                Toast.makeText(MainActivity.this, "STOP", Toast.LENGTH_SHORT).show();
            }
        });

        // Reset button click listener
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Reset all variables and clear lap times
                millisecondTime = 0L;
                startTime = 0L;
                timeBuff = 0L;
                updateTime = 0L;
                seconds = 0;
                minutes = 0;
                milliSeconds = 0;
                arrayAdapter.clear();
                arrayAdapter.notifyDataSetChanged();
                textView.setText("00:00:00");
                reset.setVisibility(View.INVISIBLE);

                Toast.makeText(MainActivity.this, "CLEAR", Toast.LENGTH_SHORT).show();
            }
        });

        // Set initial time display
        textView.setText("00:00:00");
    }
}

