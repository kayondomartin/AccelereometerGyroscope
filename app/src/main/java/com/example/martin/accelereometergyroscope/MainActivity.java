package com.example.martin.accelereometergyroscope;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometerSensor;

    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 600;
    private static final float ROTATE_THRESHOLD = 0.05f;

    FileOutputStream fileOutputStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometerSensor,sensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this,accelerometerSensor,sensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor sensor = sensorEvent.sensor;
        if(sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            float x = sensorEvent.values[0],
                    y = sensorEvent.values[1],
                    z = sensorEvent.values[2];

            long currentTime = System.currentTimeMillis();

            if((currentTime - lastUpdate) > 100){
                long diffTime = (currentTime - lastUpdate);


                float speed = Math.abs(x+y+z - last_x - last_y - last_z)/diffTime*10000;

                if(speed > SHAKE_THRESHOLD){
                    try {
                        fileOutputStream = openFileOutput("accelerometer.txt",MODE_APPEND);
                        String record = currentTime+"\t"+"(x: "+x+", y: "+y+", z: "+z+")\n";
                        fileOutputStream.write(record.getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }finally {
                        try{
                            fileOutputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(this,"Problem saving record",Toast.LENGTH_SHORT);
                    }
                }

                last_x = x;
                last_y = y;
                last_z = z;
            }
        }

                long currentTime = System.currentTimeMillis();
                float x = sensorEvent.values[0],
                        y = sensorEvent.values[1],
                        z = sensorEvent.values[2];
                try {
                    fileOutputStream = openFileOutput("gyroscope.txt",MODE_APPEND);
                    String record = currentTime+"\t"+"(x: "+x+", y: "+y+", z: "+z+")\n";
                    fileOutputStream.write(record.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    try{
                        fileOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(this,"Problem saving record",Toast.LENGTH_SHORT);
                }



    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }


}
