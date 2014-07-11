package com.viniciusdsl.cameraview.library.controller;



import android.content.Context;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.view.SurfaceHolder;

import com.viniciusdsl.cameraview.library.CameraView;
import com.viniciusdsl.cameraview.library.listener.CameraListener;


/**
 * @author Vinicius DSL
 * @version 0.1
 * <p>The controller of the view that display the camera.</p>
 */

public class CameraViewController extends CameraHardwareController implements
		SurfaceHolder.Callback {

    // surfaceHolder that contains the view of the camera
	private SurfaceHolder cameraHolder;

	/**
	 * <p>Simple constructor.</p>
	 */
	@SuppressWarnings("deprecation")
	public CameraViewController(CameraView pCameraView, Context pContext) {

		cameraContext = pContext;
		cameraHolder = pCameraView.getHolder();
		cameraHolder.addCallback(this);
		cameraHolder.setFormat(PixelFormat.TRANSPARENT);
		cameraHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

	}

	/**
	 * <p>Constructor with listener.</p>
	 */
	@SuppressWarnings("deprecation")
	public CameraViewController(CameraView pCameraView, Context pContext,
			CameraListener pCameraListener) {

		cameraContext = pContext;
		cameraHolder = pCameraView.getHolder();
		cameraHolder.addCallback(this);
		cameraHolder.setFormat(PixelFormat.TRANSPARENT);
		cameraHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		cameraListener = pCameraListener;

	}

	/**
     * <p>Setter of the Listener.</p>
     * @param pCameraActionListener is the listener to set on the view controller cameraListener
     */
	public void setListener(CameraListener pCameraActionListener) {

		cameraListener = pCameraActionListener;
	}

	/**
	 * <p>When the surface is changed, we need close and open again the camera.</p>
	 */
	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub

		if (getPreviewStatus()) {
			stopPreview();
		}
		setHolder(cameraHolder);
		configureCameraFromLastId();
		startPreview();

	}

	/**
	 * <p>Here we open the camera, if the device only have front camera (tablets)
     * this will be open.</p>
	 */
	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		// TODO Auto-generated method stub

		int cameraId = Camera.CameraInfo.CAMERA_FACING_BACK;

		if (cameraId < 0) {

			cameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;

		}

		openCameraID(cameraId);

	}

	/**
	 * <p>To destroy the camera when the holder is destroyed.</p>
	 */
	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		// TODO Auto-generated method stub

		destroyCamera();
	}

}
