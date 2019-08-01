package com.oscarcreator.androidroundsideprogressbar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.oscarcreator.roundsideprogressbar.RoundSideProgressBar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RoundSideProgressBar lProgressBar = findViewById(R.id.lpb);
        Button btn = findViewById(R.id.btn);
        EditText et = findViewById(R.id.et);



        btn.setOnClickListener((v) -> {
            int t =  Integer.valueOf(et.getText().toString());
            lProgressBar.setProgress(t);
        });


    }
}
