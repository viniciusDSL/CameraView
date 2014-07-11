package com.viniciusdsl.cameraview.library.instrumentation;

import android.content.Context;
import android.hardware.Camera;
import android.view.Surface;
import android.view.WindowManager;

import java.util.List;

/**
 * @author Vinicius DSL
 * @version 0.1
 * <p>This class help with some calculus to get the optimal resolution.</p>
 */

public class CameraOptimalSizeHelper {

	/**
	 * <p>This function calculate the optimal size to display in the surfaceview.</p>
     * @param sizes    contains a list of supported preview sizes of the camera
     * @param w        camera width
     * @param h        camera height
	 */
	public static Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes,
			int w, int h) {
		final double ASPECT_TOLERANCE = 0.1;
		double targetRatio = (double) h / w;

		if (sizes == null)
			return null;

		Camera.Size optimalSize = null;
		double minDiff = Double.MAX_VALUE;

		int targetHeight = h;

		for (Camera.Size size : sizes) {
			double ratio = (double) size.width / size.height;
			if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
				continue;
			if (Math.abs(size.height - targetHeight) < minDiff) {
				optimalSize = size;
				minDiff = Math.abs(size.height - targetHeight);
			}
		}

		if (optimalSize == null) {
			minDiff = Double.MAX_VALUE;
			for (Camera.Size size : sizes) {
				if (Math.abs(size.height - targetHeight) < minDiff) {
					optimalSize = size;
					minDiff = Math.abs(size.height - targetHeight);
				}
			}
		}
		return optimalSize;
	}

	/**
	 * <p>This function return the default orientation of the device, all devices don't have
	 * default orientation in the same way, so we need rotate 0, 90, 180 or 270
	 * degrees to show correctly.</p>
     * @param context   Application context
     * @param cameraId  The id of the current open camera
	 */
	public static int getDisplayOrientation(Context context,int cameraId) {
		Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Camera.getCameraInfo(cameraId, cameraInfo);
		int rotation = windowManager.getDefaultDisplay().getRotation();
		int degrees = 0;

		switch (rotation) {
		case Surface.ROTATION_0:
			degrees = 0;
			break;
		case Surface.ROTATION_90:
			degrees = 90;
			break;
		case Surface.ROTATION_180:
			degrees = 180;
			break;
		case Surface.ROTATION_270:
			degrees = 270;
			break;
		}

		int result;
	     if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
	         result = (cameraInfo.orientation + degrees) % 360;
	         result = (360 - result) % 360;  // compensate the mirror
	     } else {  // back-facing
	         result = (cameraInfo.orientation - degrees + 360) % 360;
	     }
		return result;
	}
	
	

}
