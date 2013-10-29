package com.cloud.cam;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.ScaleGestureDetector;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import android.widget.ZoomControls;


public class Preview extends SurfaceView implements SurfaceHolder.Callback {
	private static String TAG = "Preview";
	private SurfaceHolder mHolder;
    private Camera mCamera;
    private MediaRecorder mMediaRecorder;
    private boolean isPreview;
    private boolean isRecording = false;
    private Paint paint = new Paint(); 
    

    public Preview(Context context, Bundle savedInstanceState) {
        super(context);
        mCamera = Camera.open();

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }
    
    public void videoRecorder(){
    	if(!isRecording){
    		if(isPreview){
    			if(mCamera != null){
    				mCamera.stopPreview();
    				mCamera.release();
    				mCamera = null;
    			}
    		}
    		
    		if (mMediaRecorder == null)
				mMediaRecorder = new MediaRecorder();
			else
				mMediaRecorder.reset();
    		
    		mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
    	    mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
    	    mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
    	    mMediaRecorder.setOutputFile("/sdcard/DCIM/test.mp4");
    	    mMediaRecorder.setPreviewDisplay(mHolder.getSurface());
    	    
    	    try {
    	        mMediaRecorder.prepare();
    	        mMediaRecorder.start();
    	        
    	    } catch (IllegalStateException e) {
    	        Log.d(TAG, "IllegalStateException preparing MediaRecorder: " + e.getMessage());
    	        releaseMediaRecorder();
    	        
    	    } catch (IOException e) {
    	        Log.d(TAG, "IOException preparing MediaRecorder: " + e.getMessage());
    	        releaseMediaRecorder();
    	    }
    	    isRecording = true;
    	}else{
    		mMediaRecorder.stop();
    		releaseMediaRecorder();
    		isRecording = false;
    		
    		try{
    			mCamera = Camera.open();
    			mCamera.setPreviewDisplay(mHolder);
                mCamera.startPreview();
                isPreview = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
    	}
    }

    public void releaseMediaRecorder(){
    	if (mMediaRecorder != null) {
            mMediaRecorder.reset();   // clear recorder configuration
            mMediaRecorder.release(); // release the recorder object
            mMediaRecorder = null;
    	}
    }
    
    @Override
	public void onDraw(Canvas canvas) {
    	canvas.save();
    	
    	paint.setColor(Color.WHITE);
    	final float scale = getResources().getDisplayMetrics().density;
    	paint.setTextSize(14 * scale + 0.5f); // convert dps to pixels
    	paint.setTextAlign(Paint.Align.CENTER);
    	canvas.drawText("hello world",canvas.getWidth() / 2, canvas.getHeight() / 3, paint);
    	
    	canvas.restore();
    }
    
    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, now tell the camera where to draw the preview.
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
            isPreview = true;
            this.setWillNotDraw(false);
        } catch (IOException e) {
            Log.d(TAG, "Error setting camera preview: " + e.getMessage());
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // empty. Take care of releasing the Camera preview in your activity.
		if (mCamera != null) {
			if (isPreview) {
				mCamera.stopPreview();
				isPreview = false;
			}
			mCamera.release();
			mCamera = null; // º«µ√ Õ∑≈
		}
		
		mHolder = null;
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.

        if (mHolder.getSurface() == null){
          // preview surface does not exist
          return;
        }

        // stop preview before making changes
        try {
//            mCamera.stopPreview();
        } catch (Exception e){
          // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here

        // start preview with new settings
        try {
//            mCamera.setPreviewDisplay(mHolder);
//            mCamera.startPreview();
//            isPreview = true;
            
        } catch (Exception e){
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }

}
