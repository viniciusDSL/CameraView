package com.viniciusdsl.library.listener;

import android.graphics.Bitmap;

/**
 * @author Vinicius DSL 
 * @version 0.2
 * <p>Interface of the camera listener.</p>
 */

public interface CameraListener {

	/**
	 * <p>Called when the camera take a picture succesfully</p>
	 */
	public void onCameraPictureTaken(String pPath, Bitmap pBitmap);

	/**
	 * <p>Called when the camera try to take a picture but fail</p>
	 */
	public void onCameraPictureFailed(Exception pException);

	/**
	 * <p>Called when the camera try to open but have a error or
     * another app are using the camera</p>
	 */
	public void onCameraOpenFailed(Exception pException);

}
