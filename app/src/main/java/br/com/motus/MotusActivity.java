package br.com.motus;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MotusActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor linearAccelerationSensor;
    private Sensor gravitySensor;
    private ImageView arrowImageView;
    private static final float FALL_THRESHOLD = 15.0f;  // Valor indicativo de queda
    private float filteredAccX = 0, filteredAccY = 0;
    private static final float SMOOTHING_FACTOR = 0.1f;  // Quanto menor, mais suave

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motus);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        linearAccelerationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        arrowImageView = findViewById(R.id.arrowImageView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (linearAccelerationSensor != null) {
            sensorManager.registerListener(this, linearAccelerationSensor, SensorManager.SENSOR_DELAY_UI);
        }
        if (gravitySensor != null) {
            sensorManager.registerListener(this, gravitySensor, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_GRAVITY) {
            float[] gravityValues = event.values.clone();
        } else if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
            detectMovement(event);
        }
    }

    private void detectMovement(SensorEvent event) {
        float accelerationX = event.values[0];
        float accelerationY = event.values[1];
        float accelerationZ = event.values[2];

        filteredAccX = filteredAccX + SMOOTHING_FACTOR * (accelerationX - filteredAccX);
        filteredAccY = filteredAccY + SMOOTHING_FACTOR * (accelerationY - filteredAccY);

        float totalAcceleration = (float) Math.sqrt(
                accelerationX * accelerationX +
                        accelerationY * accelerationY +
                        accelerationZ * accelerationZ);

        if (totalAcceleration > FALL_THRESHOLD) {
            handleFallDetected();
        } else {
            float angle = (float) Math.toDegrees(Math.atan2(filteredAccY, filteredAccX));
            arrowImageView.setRotation(angle);
        }
    }

    private void handleFallDetected() {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null) {
            vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        }
        Toast.makeText(this, "Queda detectada!", Toast.LENGTH_LONG).show();
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
