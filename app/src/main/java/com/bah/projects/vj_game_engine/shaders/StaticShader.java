package com.bah.projects.vj_game_engine.shaders;

import android.content.Context;
import android.util.Log;

import com.bah.projects.vj_game_engine.R;

import javax.vecmath.Matrix4f;

/**
 * Created by 564771 on 8/10/2016.
 */
public class StaticShader extends ShaderProgram{

    private static final String TAG = "Static Shader";
    private static final int VERTEX_RESOURCE = R.raw.per_pixel_vertex_shader;
    private static final int FRAGMENT_RESOURCE = R.raw.per_pixel_fragment_shader;

    private int mMVPMatrixHandle;

    public StaticShader(Context context) {

        super(context, VERTEX_RESOURCE, FRAGMENT_RESOURCE);
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "a_Position");
        super.bindAttribute(1, "a_Normal");
        super.bindAttribute(2, "a_TexCoordinate");

        Log.i(TAG, "bindAttributes: ");
    }

    @Override
    protected void getAllUniformLocations()
    {
        mMVPMatrixHandle = super.getUniformLocationHandle("u_MVPMatrix");
    }

    public void loadTransformationMatrix(Matrix4f matrix)
    {
        super.loadMatrix(mMVPMatrixHandle, matrix);
    }




}
