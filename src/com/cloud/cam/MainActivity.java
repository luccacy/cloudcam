package com.cloud.cam;


import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;


class MyDebug {
	static final boolean LOG = true;
}

public class MainActivity extends Activity implements SensorEventListener{
	private final String TAG = "MainActivity";
    private Preview mPreview;
    private boolean isRecording = false;
    private SensorManager mSensorManager = null;
	private Sensor mSensorAccelerometer = null;
    
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().setFormat(PixelFormat.TRANSLUCENT);
		
		setContentView(R.layout.activity_main);
		
		//register sensor
		mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
		if( mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null ) {
				Log.e(TAG, "=======found accelerometer");
			mSensorAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		}
		else {
				Log.e(TAG, "=======no support for accelerometer");
		}
		
        // Create our Preview view and set it as the content of our activity.
        mPreview = new Preview(this, savedInstanceState);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);
	}
	
    @Override
    protected void onResume() {
		if( MyDebug.LOG )
			Log.d(TAG, "onResume");
        super.onResume();
        mSensorManager.registerListener(this, mSensorAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        
    }

    @Override
    protected void onPause() {
		if( MyDebug.LOG )
			Log.d(TAG, "onPause");
        super.onPause();
        mSensorManager.unregisterListener(this);
        
    }
	
	
	public void onClickedRecordVideo(View view){
		this.mPreview.videoRecorder();
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		double x = event.values[0];
		double y = event.values[1];
		double z = event.values[2];
		
		this.mPreview.showToast(null, "x:" + x + "y:" + y + "z:" + z);
	}

}
