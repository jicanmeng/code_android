package com.example.accsensordata;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mAccSensor;

    TextView title;
    TextView tv_x_value, tv_y_value, tv_z_value;
    TextView tv_sensor_accuracy;
    TextView tv_sensor_rate;
    long timestamp_this_time;
    long timestamp_last_time;
    RelativeLayout layout;
    int sensor_accuracy = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        //get layout
        layout = (RelativeLayout)findViewById(R.id.relative);

        //get textviews
        title = (TextView)findViewById(R.id.name);
        tv_x_value =(TextView)findViewById(R.id.xval);
        tv_y_value = (TextView)findViewById(R.id.yval);
        tv_z_value = (TextView)findViewById(R.id.zval);
        tv_sensor_accuracy =(TextView)findViewById(R.id.accuracy);
        tv_sensor_rate = (TextView)findViewById(R.id.rate);
        
        title.setText(R.string.app_name);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy)
    {
        // Do something here if sensor accuracy changes.
        tv_sensor_accuracy.setText("accuracy: " + accuracy);
        sensor_accuracy = accuracy;
    }

    @Override
    public final void onSensorChanged(SensorEvent event) 
    {
        // Many sensors return 3 values, one for each axis.
        float x =  event.values[0];
        float y =  event.values[1];
        float z =  event.values[2];

	    int rate = 0;
		// unit of timestamp is nanosecond
	    timestamp_this_time = event.timestamp;
	    if(timestamp_last_time != 0) {
		    rate = (int)(1000000000L/(timestamp_this_time - timestamp_last_time));
		    tv_sensor_rate.setText("reporting rate: " + rate + "HZ");
	    }
	    timestamp_last_time = timestamp_this_time;

        //display values using TextView
        tv_x_value.setText("X axis" + "\t\t" + x);
        tv_y_value.setText("Y axis" + "\t\t" + y);
        tv_z_value.setText("Z axis" + "\t\t" + z);

        Log.e("demo", "acc data is " + x + ", " + y + ", " + z);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        timestamp_this_time = 0;
        timestamp_last_time = 0;
        mSensorManager.registerListener(this, mAccSensor, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();
        timestamp_this_time = 0;
        timestamp_last_time = 0;
        mSensorManager.unregisterListener(this);
    }
}
