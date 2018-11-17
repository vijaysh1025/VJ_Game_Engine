package com.bah.projects.vj_game_engine.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.os.AsyncTask;
import android.util.Log;

public class TextureHelper
{
	private static final String TAG = "TextureHelper";

	public int loadTexture(final Context context, final int resourceId)
	{
		LoadTextureTask.tContext = context;
		final int[] textureHandle = new int[1];
		loadTextureTask = new LoadTextureTask();
		Bitmap bitmap;

		
		GLES20.glGenTextures(1, textureHandle, 0);

		Log.i(TAG, "loadTexture: " + context);
		Log.i(TAG, "loadTexture: " + resourceId);
		Log.i(TAG, "loadTexture: " + textureHandle[0]);
		
		if (textureHandle[0] != 0)
		{
			// No pre-scaling

			Log.i(TAG, "loadTexture: textureHandle Not 0");
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);
			// Read in the resource
			//loadTextureTask.tHandle = textureHandle[0];
			//loadTextureTask.execute(resourceId);
			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inScaled = false;
			bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);
//			try {
//				Thread.currentThread();
//				Thread.sleep(2000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			// Bind to the texture in OpenGL

			GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
			GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
			// Load the bitmap into the bound texture.
			GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
			
			// Recycle the bitmap, since its data has been loaded into OpenGL.
			bitmap.recycle();
		}
		
		if (textureHandle[0] == 0)
		{
			throw new RuntimeException("Error loading texture.");
		}
		
		return textureHandle[0];
	}

	LoadTextureTask loadTextureTask = null;

	private static class LoadTextureTask extends AsyncTask<Integer, Void, Bitmap> {
		protected Bitmap doInBackground(Integer... resourceIds) {
			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inScaled = false;
			Bitmap bitmap = BitmapFactory.decodeResource(tContext.getResources(), resourceIds[0], options);
			return bitmap;
		}



		protected void onPostExecute(Bitmap bitmap) {
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, tHandle);

			// Set filtering
			GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
			GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
			GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
			bitmap.recycle();
		}

		private static Context tContext;
		private int tHandle;
	}
}
