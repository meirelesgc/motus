package br.com.motus;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class MotusActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor linearAccelerationSensor;
    private ImageView arrowImageView;
    private static final float THRESHOLD = 0.4f;
    private float filteredAccX = 0, filteredAccY = 0;
    private static final float SMOOTHING_FACTOR = 0.1f;  // Quanto menor, mais suave

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motus);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        linearAccelerationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        arrowImageView = findViewById(R.id.arrowImageView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (linearAccelerationSensor != null) {
            sensorManager.registerListener(this, linearAccelerationSensor, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
            float accelerationX = event.values[0];
            float accelerationY = event.values[1];

            if (Math.abs(accelerationX) < THRESHOLD && Math.abs(accelerationY) < THRESHOLD) {
                return;
            }

            filteredAccX = filteredAccX + SMOOTHING_FACTOR * (accelerationX - filteredAccX);
            filteredAccY = filteredAccY + SMOOTHING_FACTOR * (accelerationY - filteredAccY);

            float angle = (float) Math.toDegrees(Math.atan2(filteredAccY, filteredAccX));

            arrowImageView.setRotation(angle);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Não se aplica ao meu problema
    }

    public void goToAdminMotus(View view) {
        Intent intent = new Intent(this, AdminMotusActivity.class);
        startActivity(intent);
    }
}
