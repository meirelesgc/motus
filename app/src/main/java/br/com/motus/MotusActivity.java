package br.com.motus;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.os.VibrationEffect;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MotusActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor rotationVectorSensor, accelerometer, gravitySensor;
    private ImageView arrowImageViewY,arrowImageViewX, arrowImageViewA;
    private final float[] rotationMatrix = new float[9];
    private final float[] orientation = new float[3];
    private static final float FALL_THRESHOLD = 15.0f;  // Valor indicativo de queda
    private float filteredAngle;
    private static final float FILTER_FACTOR = 0.2f;
    private static final float DEADZONE = 10f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motus);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        rotationVectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);

        arrowImageViewY = findViewById(R.id.arrowImageViewY);
        arrowImageViewX = findViewById(R.id.arrowImageViewX);
        arrowImageViewA = findViewById(R.id.arrowImageViewAcceleration);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, rotationVectorSensor, SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, gravitySensor, SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values);
            SensorManager.getOrientation(rotationMatrix, orientation);

            float pitch = orientation[1];
            float roll = orientation[2];

            double pitchInDegrees = Math.toDegrees(pitch);
            double rollInDegrees = Math.toDegrees(roll);

            arrowImageViewY.setRotation((float) rollInDegrees);
            arrowImageViewX.setRotation((float) pitchInDegrees);

        } else if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float accelerationX = event.values[0];
            float accelerationY = event.values[1];
            float accelerationZ = event.values[2];

            float totalAcceleration = (float) Math.sqrt(
                    accelerationX * accelerationX + accelerationY * accelerationY + accelerationZ * accelerationZ);

            if (totalAcceleration > FALL_THRESHOLD) {
                handleFallDetected();
            }

            float angle = (float) Math.toDegrees(Math.atan2(accelerationX, accelerationY));
            if (Math.abs(angle - filteredAngle) > DEADZONE) {
                filteredAngle = filteredAngle * (1 - FILTER_FACTOR) + angle * FILTER_FACTOR;
                arrowImageViewA.setRotation(filteredAngle);
            }
        }
    }

    private void handleFallDetected() {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
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