package com.bah.projects.vj_game_engine.renderEngine;

import android.content.Context;
import android.graphics.AvoidXfermode;
import android.opengl.GLES20;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import com.bah.projects.vj_game_engine.R;
import com.bah.projects.vj_game_engine.common.TextureHelper;
import com.bah.projects.vj_game_engine.models.RawModel;
import com.bah.projects.vj_game_engine.models.TexturedModel;
import com.bah.projects.vj_game_engine.shaders.ShaderProgram;
import com.bah.projects.vj_game_engine.shaders.StaticShader;
import com.bah.projects.vj_game_engine.textures.ModelTexture;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by 564771 on 7/31/2016.
 */
public class VJSurfaceRenderer implements GLSurfaceView.Renderer{

    private static final String TAG = "SurfaceRenderer";
    Loader loader;
    RawModel model;
    ShaderProgram shader;
    Context context;
    ModelTexture texture;
    TexturedModel texturedModel;

    private float mAngleX = 0;

    private int mMVPMatrixHandle;
    private float[] mMVPMatrix = new float[16];
    private float[] mViewMatrix = new float[16];
    private float[] mProjectionMatrix = new float[16];
    private float[] mModelMatrix = new float[16];




    public VJSurfaceRenderer(Context activityContext)
    {

        context = activityContext;

    }

    @Override
    public  void  onSurfaceCreated(GL10 glUnused, EGLConfig config)
    {
        GLES20.glClearColor(0.5f, 0.5f, 0.5f, 0.5f);

        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        float[] vertices = {
                -0.5f, 0.5f, 0f,
                -0.5f, -0.5f, 0f,
                0.5f, -0.5f, 0f,
                0.5f, 0.5f, 0f
        };

        float[] colors = {
                1.0f, 0.0f, 0.0f, 1.0f,
                0.0f, 0.0f, 1.0f, 1.0f,
                0.0f, 1.0f, 0.0f, 1.0f,
                1.0f, 0.0f, 1.0f, 1.0f
        };

        float[] textureCoords = {
                0f,0f,
                0f,1f,
                1f,1f,
                1f,0f
        };

        int[] indices = {
                0,1,3,
                3,1,2
        };


        loader = new Loader();

        Log.i(TAG, "VJSurfaceRenderer: Loader Complete");
        model = loader.loadToVAO(vertices, colors, textureCoords, indices);
        //texture = new ModelTexture(TextureHelper.loadTexture(context, R.drawable.ironman_d));
        //texturedModel = new TexturedModel(model,texture);

        Log.i(TAG, "VJSurfaceRenderer: model Complete");



        // Position the eye behind the origin.
        final float eyeX = 0.0f;
        final float eyeY = 0.0f;
        final float eyeZ = 1f;

        // We are looking toward the distance
        final float lookX = 0.0f;
        final float lookY = 0.0f;
        final float lookZ = -5.0f;

        // Set our up vector. This is where our head would be pointing were we holding the camera.
        final float upX = 0.0f;
        final float upY = 1.0f;
        final float upZ = 0.0f;

        // Set the view matrix. This matrix can be said to represent the camera position.
        // NOTE: In OpenGL 1, a ModelView matrix is used, which is a combination of a model and
        // view matrix. In OpenGL 2, we can keep track of these matrices separately if we choose.
        Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ);

        shader = new StaticShader(context);

        Log.i(TAG, "VJSurfaceRenderer: shader Complete");
    }

    @Override
    public  void  onSurfaceChanged(GL10 glUnused, int width, int height)
    {
        GLES20.glViewport(0,0,width,height);

        final  float ratio = (float) width / height;
        final  float left = -ratio;
        final  float right = ratio;
        final  float bottom = -1.0f;
        final  float top = 1.0f;
        final  float near = 1.0f;
        final  float far = 10.0f;

        Matrix.frustumM(mProjectionMatrix, 0, left, right, bottom, top, near, far);
    }

    @Override
    public  void  onDrawFrame(GL10 glUnused)
    {
        prepare();
        shader.start();
        mMVPMatrixHandle = shader.getUniformLocationHandle("u_MVPMatrix");

        //GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        //GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texturedModel.getTexture().getID());
        //GLES20.glUniform1i(texturedModel.getTexture().getID(), 0);

        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, 0f, 0f, -2f);
        Matrix.rotateM(mModelMatrix, 0, mAngleX, 0f, 1.0f, 0f);
        render(model);
    }



    public  void prepare(){
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

    }

    public  void render(RawModel model)
    {




        GLES30.glBindVertexArray(model.getVaoID());
        GLES20.glEnableVertexAttribArray(0);
        GLES20.glEnableVertexAttribArray(1);
        //GLES20.glEnableVertexAttribArray(2);


        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);

        GLES20.glDrawElements(GLES20.GL_TRIANGLES, model.getVertexCount(), GLES20.GL_UNSIGNED_INT, 0);
        GLES20.glDisableVertexAttribArray(0);
        //GLES20.glDisableVertexAttribArray(1);

        GLES30.glBindVertexArray(0);
    }

    public void setAngleX(float angle) {
        mAngleX = angle;
    }
    public float getAngleX() {
        return mAngleX;
    }


}
