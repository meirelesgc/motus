package com.motus.analytics.view;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.motus.analytics.R;

import java.util.Locale;

public class MotusActivity extends AppCompatActivity implements SensorEventListener {

    // Sensor
    public SensorManager sensorManager;
    public Sensor accelerometerSensor, gravitySensor, gyroscopeSensor, linearAccelerationSensor, rotationVectorSensor, stepCounterSensor;
    public final float[][] sensorData = new float[6][3];

    // Count Steps
    public float[] orientation = {0, 0, 0};
    public boolean step = false;
    private float steps = 0;
    public long lastTimestamp = 0;

    // Interface
    public TextView accelerometerText, gravityText, gyroscopeText, linearAccelerationText, rotationVectorText, stepCounterText;
    public Button orientationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_motus);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        linearAccelerationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        rotationVectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        orientationButton = findViewById(R.id.button);

        accelerometerText = findViewById(R.id.accelerometerText);
        gravityText = findViewById(R.id.gravityText);
        gyroscopeText = findViewById(R.id.gyroscopeText);
        linearAccelerationText = findViewById(R.id.linearAccelerationText);
        rotationVectorText = findViewById(R.id.rotationVectorText);
        stepCounterText = findViewById(R.id.stepCounterText);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, gravitySensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, gyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, linearAccelerationSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, rotationVectorSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        int sensorIndex = -1;
        float threshold = 0.3f;

        long deltaTime = 0;

        long currentTimestamp = System.currentTimeMillis();
        if (lastTimestamp != 0) {
            deltaTime = currentTimestamp - lastTimestamp;
        }
        lastTimestamp = currentTimestamp;

        switch (event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                accelerometerText.setText(getString(R.string.accelerometer_text, event.values[0], event.values[1], event.values[2]));
                sensorIndex = 0;
                break;
            case Sensor.TYPE_GRAVITY:
                gravityText.setText(getString(R.string.gravity_text, event.values[0], event.values[1], event.values[2]));
                stepCounterText.setText(getString(R.string.step_counter_text, (int) steps));
                countSteps();
                sensorIndex = 1;
                break;
            case Sensor.TYPE_GYROSCOPE:
                gyroscopeText.setText(getString(R.string.gyroscope_text, event.values[0], event.values[1], event.values[2]));
                sensorIndex = 2;
                break;
            case Sensor.TYPE_LINEAR_ACCELERATION:
                linearAccelerationText.setText(getString(R.string.linear_acceleration_text, event.values[0], event.values[1], event.values[2]));
                sensorIndex = 3;
                break;
            case Sensor.TYPE_ROTATION_VECTOR:
                rotationVectorText.setText(getString(R.string.rotation_vector_text, event.values[0], event.values[1], event.values[2]));
                sensorIndex = 4;
                break;
        }

        if (sensorIndex != -1) {
            int length = Math.min(event.values.length, sensorData[sensorIndex].length);
            System.arraycopy(event.values, 0, sensorData[sensorIndex], 0, length);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void countSteps() {
        if (Math.abs(sensorData[1][0]) > 3.5 && !step) {
            step = true;
        } else if (Math.abs(sensorData[1][0]) <= 3.5 && step) {
            step = false;
            steps++;
        }
    }

    public void setOrientation(View view) {
        System.arraycopy(sensorData[1], 0, orientation, 0, sensorData[1].length);

        String orientationText = String.format(Locale.US, "X: %.2f\nY: %.2f\nZ: %.2f",
                orientation[0], orientation[1], orientation[2]);
        orientationButton.setText(orientationText);
    }

}
