package com.oscarcreator.androidroundsideprogressbar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.SeekBar;

import com.oscarcreator.roundsideprogressbar.DividedRoundSideProgressBar;
import com.oscarcreator.roundsideprogressbar.RoundSideProgressBar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RoundSideProgressBar rspb = findViewById(R.id.lpb1);
        DividedRoundSideProgressBar drspb = findViewById(R.id.drspb1);

        SeekBar sb = findViewById(R.id.sb);

        Button btn_10 = findViewById(R.id.btn_10);
        btn_10.setOnClickListener((v) -> {
            rspb.setProgress(10, true);
            drspb.setProgress(10, true);
            sb.setProgress(10);
        });
        Button btn_50 = findViewById(R.id.btn_50);
        btn_50.setOnClickListener((v) -> {
            rspb.setProgress(50, true);
            drspb.setProgress(50, true);
            sb.setProgress(50);
        });
        Button btn_90 = findViewById(R.id.btn_90);
        btn_90.setOnClickListener((v) -> {
            rspb.setProgress(90, true);
            drspb.setProgress(90, true);
            sb.setProgress(90);
        });



        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                rspb.setProgress(progress);
                drspb.setProgress(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }
}
