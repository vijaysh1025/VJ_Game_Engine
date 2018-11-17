package com.bah.projects.vj_game_engine.lights;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.bah.projects.vj_game_engine.shaders.ShaderProgram;

import javax.vecmath.Vector3f;

/**
 * Created by 564771 on 8/26/2016.
 */
public class PointLight {




    public static void Add(float x, float y, float z, ShaderProgram shader, float[] ViewMatrix)
    {
        int mLightHandle = shader.getUniformLocationHandle("u_LightPos");
        float[] LightModelMatrix = new float[16];
        float[] MVMatrix = new float[16];
        float[] lightPos = {0,0,0,1};
        float[] lightWorldSpace = new float[4];
        float[] lightEyeSpace = new float[4];

        Matrix.setIdentityM(LightModelMatrix, 0);
        Matrix.translateM(LightModelMatrix, 0,0,y, -2.5f);

        Matrix.multiplyMV(lightWorldSpace, 0, LightModelMatrix, 0, lightPos, 0);
        Matrix.multiplyMV(lightEyeSpace, 0, ViewMatrix, 0, lightWorldSpace, 0);

        GLES20.glUniform3f(mLightHandle,lightEyeSpace[0],lightEyeSpace[1],lightEyeSpace[2]);
    }

}
