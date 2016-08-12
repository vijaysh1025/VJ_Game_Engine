package com.bah.projects.vj_game_engine.shaders;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLES30;
import android.util.Log;

import com.bah.projects.vj_game_engine.R;
import com.bah.projects.vj_game_engine.common.RawResourceReader;

public abstract class ShaderProgram
{
	private static final String TAG = "ShaderHelper";

	private int programID;
	private int vertexShaderID;
	private int fragmentShaderID;

	public ShaderProgram(Context context, int vertexResourceID, int fragmentResourceID)
	{
		final String vertexShaderSource = RawResourceReader.readTextFileFromRawResource(context, vertexResourceID);
		final String fragmentShaderSource = RawResourceReader.readTextFileFromRawResource(context, fragmentResourceID);

		vertexShaderID = compileShader(GLES20.GL_VERTEX_SHADER, vertexShaderSource);
		fragmentShaderID = compileShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderSource);

		programID = createAndLinkProgram(vertexShaderID, fragmentShaderID);
	}

	protected abstract void bindAttributes();

	protected void bindAttribute(int attribute, String variableName)
	{
		GLES20.glBindAttribLocation(programID,attribute,variableName);
	}



	public void start(){
		GLES20.glUseProgram(programID);
	}

	public void stop(){
		GLES20.glUseProgram(0);
	}

	public void cleanUp(){
		stop();
		GLES20.glDetachShader(programID, vertexShaderID);
		GLES20.glDetachShader(programID, fragmentShaderID);
		GLES20.glDeleteShader(vertexShaderID);
		GLES20.glDeleteShader(fragmentShaderID);
		GLES20.glDeleteProgram(programID);
	}

	public int getUniformLocationHandle(String loc)
	{
		int handle = GLES20.glGetUniformLocation(programID,loc);
		return  handle;
	}




	public static int compileShader(final int shaderType, final String shaderSource) 
	{
		int shaderHandle = GLES20.glCreateShader(shaderType);

		if (shaderHandle != 0) 
		{
			// Pass in the shader source.
			GLES20.glShaderSource(shaderHandle, shaderSource);

			// Compile the shader.
			GLES20.glCompileShader(shaderHandle);

			// Get the compilation status.
			final int[] compileStatus = new int[1];
			GLES20.glGetShaderiv(shaderHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

			// If the compilation failed, delete the shader.
			if (compileStatus[0] == 0) 
			{
				Log.e(TAG, "Error compiling shader: " + GLES20.glGetShaderInfoLog(shaderHandle));
				GLES20.glDeleteShader(shaderHandle);
				shaderHandle = 0;
			}
		}

		if (shaderHandle == 0)
		{			
			throw new RuntimeException("Error creating shader.");
		}
		
		return shaderHandle;
	}
	
	/**
	 * Helper function to compile and link a program.
	 * 
	 * @param vertexShaderHandle An OpenGL handle to an already-compiled vertex shader.
	 * @param fragmentShaderHandle An OpenGL handle to an already-compiled fragment shader.
	 * @param attributes Attributes that need to be bound to the program.
	 * @return An OpenGL handle to the program.
	 */
	public int createAndLinkProgram(final int vertexShaderHandle, final int fragmentShaderHandle)
	{
		int programHandle = GLES20.glCreateProgram();
		
		if (programHandle != 0) 
		{
			// Bind the vertex shader to the program.
			GLES20.glAttachShader(programHandle, vertexShaderHandle);			

			// Bind the fragment shader to the program.
			GLES20.glAttachShader(programHandle, fragmentShaderHandle);
			
			// Bind attributes
			bindAttributes();
			//if (attributes != null)
			//{
				//final int size = attributes.length;
				//for (int i = 0; i < size; i++)
				//{
				//	GLES20.glBindAttribLocation(programHandle, i, attributes[i]);
				//}
			//}
			
			// Link the two shaders together into a program.
			GLES20.glLinkProgram(programHandle);

			// Get the link status.
			final int[] linkStatus = new int[1];
			GLES20.glGetProgramiv(programHandle, GLES20.GL_LINK_STATUS, linkStatus, 0);

			// If the link failed, delete the program.
			if (linkStatus[0] == 0) 
			{				
				Log.e(TAG, "Error compiling program: " + GLES20.glGetProgramInfoLog(programHandle));
				GLES20.glDeleteProgram(programHandle);
				programHandle = 0;
			}
		}
		
		if (programHandle == 0)
		{
			throw new RuntimeException("Error creating program.");
		}
		
		return programHandle;
	}
}
