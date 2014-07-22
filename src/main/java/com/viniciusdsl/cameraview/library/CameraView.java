package com.viniciusdsl.library;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.SurfaceView;

import com.viniciusdsl.library.controller.CameraViewController;
import com.viniciusdsl.library.listener.CameraListener;

/**
 * @author Vinicius DSL
 * @version 0.2
 * <p>The widget that can use directly to the layout.</p>
 */

public class CameraView extends SurfaceView {

	private CameraViewController cameraViewController;

    /**
     * <p>Get the measure of the camera view and send the width and height to get a optimal
     * view on the surfaceView.</p>
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        final int width = resolveSize(getSuggestedMinimumWidth(),
                widthMeasureSpec);
        final int height = resolveSize(getSuggestedMinimumHeight(),
                heightMeasureSpec);
        setMeasuredDimension(width, height);
        cameraViewController.setCameraWidth(width);
        cameraViewController.setCameraHeight(height);
    }

	/**
     * <p>Widget constructor.</p>
     * <p>This is called when is declared on the layout any activity or fragment.</p>
     * @param context The application context
     * @param attrs   The attributes declared on the layout
	 */
	public CameraView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setBackgroundColor(Color.TRANSPARENT);
		cameraViewController = new CameraViewController(this, context);
	}

	/**
     * <p>Simple constructor</p>
     * @param context The application context
	 */
	public CameraView(Context context) {
		super(context);
		setBackgroundColor(Color.TRANSPARENT);
		cameraViewController = new CameraViewController(this, context);

	}

	/**
     * <p>Constructor with listener.</p>
     * @param context         The application context
     * @param pCameraListener The listener to set on the view controller
	 */
	public CameraView(Context context, CameraListener pCameraListener) {
		super(context);
		setBackgroundColor(Color.TRANSPARENT);
		cameraViewController = new CameraViewController(this, context);
		cameraViewController.setListener(pCameraListener);

	}

	/**
     * <p>Setter of the listener.</p>
     * @param pCameraListener The listener to set on the view controller
	 */
	public void setListener(CameraListener pCameraListener) {

		cameraViewController.setListener(pCameraListener);

	}

	/**
     * <p>This method take a picture.</p>
     * @param pPath The full path where the image will be saved
	 */
	public void takePicture(String pPath) {
		cameraViewController.takePicture(pPath);
	}

	/**
     * <p>This method stop the camera on the holder.</p>
	 */
	public void stopView() {
		cameraViewController.stopPreview();
	}

	/**
     * <p>This method show the camera on the holder.</p>
	 */
	public void startView() {
		cameraViewController.startPreview();
	}


    /**
     * <p>This function return the camera status if this is showing on the holder.</p>
     */
	public boolean isCameraStart() {
		return cameraViewController.getPreviewStatus();
	}

    /**
     * <p>This function return a flag that indicate if an error happened
     * when try to open the camera.</p>
     */
    public boolean getCameraOpenError(){
        return cameraViewController.getOpenCameraError();
    }

    public void resetCamera(){
        cameraViewController.resetCamera();
    }

    public void releaseCamera(){
        cameraViewController.releaseCameraAndPreview();
    }
}