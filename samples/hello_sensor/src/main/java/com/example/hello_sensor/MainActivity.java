package com.example.hello_sensor;

import android.annotation.SuppressLint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    @Nullable private Sensor ambientTempSensor;

    private ImageView ivCharacter;
    private TextView tvTemp;
    private TextView tvMode;
    private View demoContainer;
    private SeekBar seekDemo;

    private static final float MIN_C = -40f;
    private static final float MAX_C = 85f;
    private static final float DEFAULT_TEMP_C = 20f;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ivCharacter = findViewById(R.id.ivCharacter);
        tvTemp = findViewById(R.id.tvTemp);
        tvMode = findViewById(R.id.tvMode);
        demoContainer = findViewById(R.id.demoContainer);
        seekDemo = findViewById(R.id.seekDemoTemp);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        if (sensorManager != null) {
            ambientTempSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        }

        if (ambientTempSensor != null) {
            tvMode.setText(R.string.live_sensor);
            demoContainer.setVisibility(View.GONE);
            // Initialize UI immediately; sensor event may not fire right away (emulators)
            applyTemp(DEFAULT_TEMP_C);
        } else {
            // Demo mode fallback (common on phones)
            tvMode.setText(R.string.demo_mode_no_ambient_temp_sensor);
            demoContainer.setVisibility(View.VISIBLE);
            seekDemo.setProgress(300);
            seekDemo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    float tempC = -10f + (progress / 600f) * 60f;
                    applyTemp(tempC);
                }
                @Override public void onStartTrackingTouch(SeekBar seekBar) {}
                @Override public void onStopTrackingTouch(SeekBar seekBar) {}
            });
            applyTemp(20f);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ambientTempSensor != null) {
            sensorManager.registerListener(
                    this,
                    ambientTempSensor,
                    SensorManager.SENSOR_DELAY_UI
            );
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (ambientTempSensor != null) {
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
            float tempC = event.values[0];
            applyTemp(tempC);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) { /* no-op */ }

    @SuppressLint("DefaultLocale")
    private void applyTemp(float tempC) {
        if(!isValidTemp(tempC)) {
            return;
        }
        float c = Math.round(tempC * 10f) / 10f;
        float f = Math.round((c * 9f / 5f + 32f) * 10f) / 10f;

        tvTemp.setText(String.format(Locale.US, "%.1f \u00B0C (%.1f \u00B0F)", c, f));
        @DrawableRes int icon = TemperatureIconMapper.iconFor(tempC);
        ivCharacter.setImageResource(icon);
    }

    private boolean isValidTemp(float v) {
        return !Float.isNaN(v) && !Float.isInfinite(v) && v >= MIN_C && v <= MAX_C;
    }

}
