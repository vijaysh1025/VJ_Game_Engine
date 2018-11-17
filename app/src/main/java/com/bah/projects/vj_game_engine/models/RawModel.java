package com.bah.projects.vj_game_engine.models;

import android.opengl.GLES20;
import android.util.Log;

import com.bah.projects.vj_game_engine.shaders.ShaderProgram;

import java.nio.FloatBuffer;
import java.util.ArrayList;

/**
 * Created by 564771 on 7/31/2016.
 */
public class RawModel {

    private int vaoID;
    private int vertexCount;
    private static final String TAG = "RawModel";

    private int mPositionHandle;
    private int mNormalHandle;
    private int mTextureHandle;

    private ArrayList<Integer> vbos = new ArrayList<Integer>();


    public RawModel(int vertexVBO, int normalsVBO, int indexVBO, int textureVBO, int vertexCount)
    {
        vbos.add(vertexVBO);
        vbos.add(normalsVBO);
        vbos.add(textureVBO);
        vbos.add(indexVBO);


        this.vertexCount = vertexCount;
    }

    public void render(ShaderProgram shader)
    {
        mPositionHandle = shader.getAttributeLocationHandler("a_Position");
        mNormalHandle = shader.getAttributeLocationHandler("a_Normal");
        mTextureHandle = shader.getAttributeLocationHandler("a_TexCoordinate");


        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vbos.get(0));
        GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false, 0, 0);
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vbos.get(1));
        GLES20.glVertexAttribPointer(mNormalHandle, 3, GLES20.GL_FLOAT, false, 0, 0);
        GLES20.glEnableVertexAttribArray(mNormalHandle);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vbos.get(2));
        GLES20.glVertexAttribPointer(mTextureHandle, 2, GLES20.GL_FLOAT, false, 0, 0);
        GLES20.glEnableVertexAttribArray(mTextureHandle);

        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, vbos.get(3));
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, vertexCount, GLES20.GL_UNSIGNED_SHORT, 0);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);
    }


    public void deleteVbos()
    {
        int[] buffers = {(int)vbos.get(0),(int)vbos.get(1),(int)vbos.get(2),(int)vbos.get(3)};
        GLES20.glDeleteBuffers(4, buffers, 0);
    }



    public int getVaoID() {
        return vaoID;
    }

    public int getVertexCount() {
        return vertexCount;
    }
}
