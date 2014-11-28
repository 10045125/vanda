package com.vanda.vandalibnetwork.utils;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Build.VERSION;

public class ImageUtils {

	public static int getOpengl2MaxTextureSize() {
		int[] maxTextureSize = new int[1];
		maxTextureSize[0] = 2048;
		android.opengl.GLES20.glGetIntegerv(
				android.opengl.GLES20.GL_MAX_TEXTURE_SIZE, maxTextureSize, 0);
		return maxTextureSize[0];
	}

	/**
	 * Get the size in bytes of a bitmap.
	 * 
	 * @param bitmap
	 * @return size in bytes
	 */
	@SuppressLint("NewApi")
	public static int getBitmapSize(Bitmap bitmap) {
		if (VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
			return bitmap.getByteCount();
		}
		// Pre HC-MR1
		return bitmap.getRowBytes() * bitmap.getHeight();
	}

}
