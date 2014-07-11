package com.viniciusdsl.cameraview.library.controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.Size;
import android.util.Log;
import android.view.SurfaceHolder;

import com.viniciusdsl.cameraview.library.instrumentation.CameraOptimalSizeHelper;
import com.viniciusdsl.cameraview.library.listener.CameraListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * @author Vinicius DSL
 * @version 0.1
 * <p>The controller of the camera's hardware here have a composite
 * because can't extend the Hardware Camera class.</p>
 */

public class CameraHardwareController {

	protected Camera cameraDevice;
	protected boolean cameraPreviewStatus;
	protected boolean cameraTakingPictureStatus;
	protected Context cameraContext;
	protected CameraListener cameraListener;
	private Size cameraPreviewSize;
	private int cameraWidth;
	private int cameraHeight;
	private int currentCameraId;
    private boolean openCameraError;

	/**
	 * <p>Constructor.</p>
	 */
	CameraHardwareController() {

		cameraPreviewStatus = false;

	}

    public boolean getOpenCameraError(){
        return openCameraError;
    }

	/**
	 * <p>Setter of the Width of the screen.</p>
	 */
	public void setCameraWidth(int width) {
		this.cameraWidth = width;
	}

	/**
	 * <p>Setter of the Height of the screen.</p>
	 */
	public void setCameraHeight(int height) {
		this.cameraHeight = height;
	}

	/**
	 * <p>Setter of the holder where we will display the camera.</p>
     * @param cameraHolder is the surfaceholder that host the camera view
	 */
	protected void setHolder(SurfaceHolder cameraHolder) {
		if (cameraDevice != null) {
			try {

				cameraDevice.setPreviewDisplay(cameraHolder);

			} catch (IOException e) {
				if (cameraListener != null) {
					cameraListener.onCameraOpenFailed(e);
				}
				e.printStackTrace();

			}
		}
	}

	/**
	 * <p>This method start showing the camera in the holder
     *  and return.
     * </p>
	 */
	public void startPreview() {
		if (cameraDevice != null && !cameraPreviewStatus
				&& !cameraTakingPictureStatus) {
            Log.d("ehhh","works :)");
			try {
				Camera.Parameters parameters = cameraDevice.getParameters();
				parameters.setPreviewSize(cameraPreviewSize.width,
						cameraPreviewSize.height);
				cameraDevice.setParameters(parameters);
				cameraDevice.startPreview();
				cameraPreviewStatus = true;

			} catch (Exception e) {
				if (cameraListener != null) {
					cameraListener.onCameraOpenFailed(e);
				}
				e.printStackTrace();

			}
		}
	}

	/**
	 * <p>This method stop showing the camera in the holder.</p>
	 */
	public boolean stopPreview() {
		if (cameraDevice != null && cameraPreviewStatus) {

			try {

				cameraDevice.stopPreview();
				cameraPreviewStatus = false;

			} catch (Exception e) {
				if (cameraListener != null) {
					cameraListener.onCameraOpenFailed(e);
				}
				e.printStackTrace();

			}
		}

		return cameraPreviewStatus;
	}

	/**
	 * <p>This method show the camera by the id.</p>
     * @param cameraId is the id of the camera(front camera,back camera,etc)
	 */
	public void openCameraID(int cameraId) {
        currentCameraId = cameraId;
        openCameraError = false;
        try {
            releaseCameraAndPreview();
            cameraDevice = Camera.open(cameraId);
            cameraPreviewStatus = true;
            configureCameraFromLastId();
        } catch (Exception e) {
            e.printStackTrace();
            openCameraError = true;
            if(cameraListener!=null) {
                cameraListener.onCameraOpenFailed(e);
            }
        }
	}
	
	/**
	 * <p>This method configure the camera size in the SurfaceView from the last
     * ID that was open.</p>
	 */
	protected void configureCameraFromLastId(){
        if(cameraDevice!=null) {
            configureCameraParams();
            int degrees = CameraOptimalSizeHelper
                    .getDisplayOrientation(cameraContext, currentCameraId);
            cameraDevice.setDisplayOrientation(degrees);
        }
	}

	/**
	 * <p>This method destroy the camera and release it.</p>
	 */
	public void destroyCamera() {

		stopPreview();
		releaseCameraAndPreview();
	}

	/**
	 * <p>This method take a picture with the camera and return the path and
     * bitmap on the listener.</p>
     * @param pPath The full path where the image will be saved
	 */
	public void takePicture(final String pPath) {
		cameraTakingPictureStatus = true;
		PictureCallback pictureCallback = new PictureCallback() {

			private String filePath;

			@Override
			public void onPictureTaken(byte[] bytes, Camera camera) {
				FileOutputStream fos;
				try {
					File imagesFolder = new File(pPath);
					filePath = imagesFolder.getAbsolutePath();
					fos = new FileOutputStream(imagesFolder);
					fos.write(bytes);
					fos.close();
				} catch (IOException e) {
                    e.printStackTrace();
					filePath = null;
					if (cameraListener != null) {
						cameraListener.onCameraPictureFailed(e);
                        cameraTakingPictureStatus = false;
						return;
					}
				}

				if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.GINGERBREAD_MR1) {
					// only for gingerbread and newer versions
					stopPreview();
				}
                cameraPreviewStatus = false;
                cameraTakingPictureStatus = false;
				if (filePath != null) {
					Bitmap pictureBitmap = BitmapFactory.decodeByteArray(bytes,
							0, bytes.length);
					if (cameraListener != null) {
						cameraListener.onCameraPictureTaken(filePath,
								pictureBitmap);
						return;
					}
				}
			}
		};
        try {
            cameraDevice.takePicture(null, null, pictureCallback);
        }catch (Exception e){
            e.printStackTrace();
            if(cameraListener!=null) {
                cameraListener.onCameraPictureFailed(e);
            }
        }
	}

	/**
	 * <p>This function return the status of the camera on the holder.</p>
	 */
	public boolean getPreviewStatus() {
		if (cameraTakingPictureStatus) {
			return false;
		}
		return cameraPreviewStatus;
	}

	/**
	 * <p>This method get the width and height of the surfaceview in the screen and
     * calculate the most optimal resolution to show.</p>
	 */
	public void configureCameraParams() {
		List<Size> mSupportedPreviewSizes = cameraDevice.getParameters()
				.getSupportedPreviewSizes();
		if (mSupportedPreviewSizes != null) {
			cameraPreviewSize = CameraOptimalSizeHelper.getOptimalPreviewSize(
                    mSupportedPreviewSizes, cameraWidth, cameraHeight);
		}
	}

    /**
     * <p>This method release the camera.</p>
     */
    private void releaseCameraAndPreview() {
        if (cameraDevice != null) {
            cameraDevice.release();
            cameraDevice = null;
        }
    }



}
