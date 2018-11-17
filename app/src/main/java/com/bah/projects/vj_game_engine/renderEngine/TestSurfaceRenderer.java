package com.bah.projects.vj_game_engine.renderEngine;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import com.bah.projects.vj_game_engine.models.RawModel;
import com.bah.projects.vj_game_engine.models.TexturedModel;
import com.bah.projects.vj_game_engine.shaders.ShaderProgram;
import com.bah.projects.vj_game_engine.shaders.StaticShader;
import com.bah.projects.vj_game_engine.textures.ModelTexture;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by 564771 on 7/31/2016.
 */
public class TestSurfaceRenderer implements GLSurfaceView.Renderer{

    private static final String TAG = "SurfaceRenderer";
    Context context;

    private float mAngleX = 0;

    private int mMVPMatrixHandle;
    private int mPositionHandle;
    private int mColorHandle;
    private float[] mMVPMatrix = new float[16];
    private float[] mViewMatrix = new float[16];
    private float[] mProjectionMatrix = new float[16];
    private float[] mModelMatrix = new float[16];
    private float[] vertices;
    private float[] colors;
    private float[] textureCoordinates;
    private short[] indices;

    private static final int BYTES_PER_FLOAT = 4;
    private static final int BYTES_PER_SHORT = 2;

    ShaderProgram shader;


    public TestSurfaceRenderer(Context activityContext)
    {

        context = activityContext;

        float[] v = {
                -1f, 1f, 0f,
                -1f, -1f, 0f,
                1f, -1f, 0f,
                1f, 1f, 0f
        };

        vertices = v;

        float[] c = {
                1.0f, 0.0f, 0.0f, 1.0f,
                0.0f, 0.0f, 1.0f, 1.0f,
                0.0f, 1.0f, 0.0f, 1.0f,
                1.0f, 0.0f, 1.0f, 1.0f
        };

        colors = c;

        float[] tc = {
                0f,0f,
                0f,1f,
                1f,1f,
                1f,0f
        };

        textureCoordinates = tc;

        short[] i = {
                0,1,3,
                3,1,2
        };

        indices = i;

    }

    final int[] vbo = new int[1];
    final int[] cbo = new int[1];
    final int[] ibo = new int[1];

    @Override
    public  void  onSurfaceCreated(GL10 glUnused, EGLConfig config)
    {

        try {
            final FloatBuffer pBuffer = ByteBuffer.allocateDirect(vertices.length * BYTES_PER_FLOAT).order(ByteOrder.nativeOrder()).asFloatBuffer();
            pBuffer.put(vertices).position(0);

            final FloatBuffer cBuffer = ByteBuffer.allocateDirect(colors.length * BYTES_PER_FLOAT).order(ByteOrder.nativeOrder()).asFloatBuffer();
            cBuffer.put(colors).position(0);

            final ShortBuffer iBuffer = ByteBuffer.allocateDirect(indices.length * BYTES_PER_SHORT).order(ByteOrder.nativeOrder()).asShortBuffer();
            iBuffer.put(indices).position(0);

            GLES20.glGenBuffers(1, vbo, 0);
            GLES20.glGenBuffers(1, cbo, 0);
            GLES20.glGenBuffers(1, ibo, 0);

            if (vbo[0] > 0 && ibo[0] > 0) {
                GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vbo[0]);
                GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, pBuffer.capacity() * BYTES_PER_FLOAT, pBuffer, GLES20.GL_STATIC_DRAW);

                GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, cbo[0]);
                GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, cBuffer.capacity() * BYTES_PER_FLOAT, cBuffer, GLES20.GL_STATIC_DRAW);

                GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, ibo[0]);
                GLES20.glBufferData(GLES20.GL_ELEMENT_ARRAY_BUFFER, iBuffer.capacity() * BYTES_PER_SHORT, iBuffer, GLES20.GL_STATIC_DRAW);

                GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
                GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);
            } else {
                Log.e(TAG, "onSurfaceCreated: Failed to Bind Buffer");
            }
        } catch (Throwable t)
        {
            Log.w(TAG, "onSurfaceCreated: ", t);
        }

        GLES20.glClearColor(0.5f, 0.5f, 0.5f, 0.5f);

        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        // Position the eye in front of the origin.
        final float eyeX = 0.0f;
        final float eyeY = 0.0f;
        final float eyeZ = -0.5f;

        // We are looking toward the distance
        final float lookX = 0.0f;
        final float lookY = 0.0f;
        final float lookZ = -5.0f;

        // Set our up vector. This is where our head would be pointing were we
        // holding the camera.
        final float upX = 0.0f;
        final float upY = 1.0f;
        final float upZ = 0.0f;

        Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ);

        shader = new StaticShader(context);

        Log.i(TAG, "onSurfaceCreated: " + shader);
    }

    @Override
    public  void  onSurfaceChanged(GL10 glUnused, int width, int height)
    {
        // Set the OpenGL viewport to the same size as the surface.
        GLES20.glViewport(0, 0, width, height);

        // Create a new perspective projection matrix. The height will stay the
        // same while the width will vary as per aspect ratio.
        final float ratio = (float) width / height;
        final float left = -ratio;
        final float right = ratio;
        final float bottom = -1.0f;
        final float top = 1.0f;
        final float near = 1.0f;
        final float far = 1000.0f;

        Matrix.frustumM(mProjectionMatrix, 0, left, right, bottom, top, near, far);
    }

    @Override
    public  void  onDrawFrame(GL10 glUnused)
    {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        shader.start();

        mMVPMatrixHandle = shader.getUniformLocationHandle("u_MVPMatrix");
        mPositionHandle = shader.getAttributeLocationHandler("a_Position");
        mColorHandle = shader.getAttributeLocationHandler("a_Color");

        //Log.i(TAG, "onDrawFrame: " + mPositionHandle);
       // Log.i(TAG, "onDrawFrame: " + mColorHandle);

        Matrix.setIdentityM(mModelMatrix,0);
        Matrix.translateM(mModelMatrix, 0, 0, 0, -7);

        Matrix.multiplyMM(mMVPMatrix,0,mViewMatrix,0,mModelMatrix,0);
        Matrix.multiplyMM(mMVPMatrix,0,mProjectionMatrix,0,mMVPMatrix,0);

        GLES20.glUniformMatrix4fv(mMVPMatrixHandle,1,false,mMVPMatrix,0);

        render();
    }

    public void render()
    {
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vbo[0]);

        GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false,0,0);
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, cbo[0]);

        GLES20.glVertexAttribPointer(mColorHandle, 4, GLES20.GL_FLOAT, false, 0, 0);
        GLES20.glEnableVertexAttribArray(mColorHandle);

        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, ibo[0]);
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, indices.length, GLES20.GL_UNSIGNED_SHORT, 0);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,0);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);
    }




    public  void prepare(){
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

    }

    public  void render(RawModel model)
    {
    }

    public void setAngleX(float angle) {
        mAngleX = angle;
    }
    public float getAngleX() {
        return mAngleX;
    }


}
