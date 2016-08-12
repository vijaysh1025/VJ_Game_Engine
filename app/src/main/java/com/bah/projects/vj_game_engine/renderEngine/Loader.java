package com.bah.projects.vj_game_engine.renderEngine;

import android.opengl.GLES20;
import android.opengl.GLES30;

import com.bah.projects.vj_game_engine.models.RawModel;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

/**
 * Created by 564771 on 7/31/2016.
 */
public class Loader {

    private final int mBytesPerFloat = 4;
    private final int mBytesPerInt = 4;
    private final int mPositionDataSize = 3;

    private ArrayList<Integer> vaos = new ArrayList<Integer>();
    private ArrayList<Integer> vbos = new ArrayList<Integer>();

    public RawModel loadToVAO(float[] positions, float[] colors, float[] textureCoords, int[] indices){
        int vaoID = createVAO();
        bindIndicesBuffer(indices);
        storeDataInAttributeList(0, positions,3);
        storeDataInAttributeList(1, colors,4);
        //storeDataInAttributeList(1, textureCoords,2);
        unbindVAO();
        return new RawModel(vaoID, indices.length);
    }

    public  void cleanUp(){
        for(int vao:vaos){
            int[] temp = new int[1];
            temp[0] = vao;
            GLES30.glDeleteVertexArrays(1,temp,0);
        }

        for(int vbo:vbos){
            int[] temp = new int[1];
            temp[0] = vbo;
            GLES30.glDeleteVertexArrays(1,temp,0);
        }
    }


    private int createVAO(){
        int[] vaoID = new int[1];
        GLES30.glGenVertexArrays(1,vaoID,0);
        vaos.add(vaoID[0]);
        GLES30.glBindVertexArray(vaoID[0]);

        return vaoID[0];
    }

    private void storeDataInAttributeList(int attributeNumber, float[] data, int size) {
        int[] vboID = new int[1];
        GLES20.glGenBuffers(1, vboID, 0);
        vbos.add((vboID[0]));
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vboID[0]);
        FloatBuffer buffer = storeDataInFloatBuffer(data);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, data.length * mBytesPerFloat, buffer, GLES20.GL_STATIC_DRAW);
        GLES20.glVertexAttribPointer(attributeNumber,size,GLES20.GL_FLOAT,false,0,0);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
    }

    private  void unbindVAO(){
        GLES30.glBindVertexArray(0);
    }

    private  void bindIndicesBuffer(int[] indices){
        int[] vboID = new int[1];
        GLES20.glGenBuffers(1, vboID, 0);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, vboID[0]);
        IntBuffer buffer =  storeDataInIntBuffer(indices);
        GLES20.glBufferData(GLES20.GL_ELEMENT_ARRAY_BUFFER, indices.length * mBytesPerFloat, buffer, GLES20.GL_STATIC_DRAW);
    }

    private IntBuffer storeDataInIntBuffer(int[] data)
    {
        IntBuffer buffer = ByteBuffer.allocateDirect(data.length*mBytesPerInt).order(ByteOrder.nativeOrder()).asIntBuffer();
        buffer.put(data).position(0);
        return  buffer;
    }


    private FloatBuffer storeDataInFloatBuffer(float[] data){
        FloatBuffer buffer = ByteBuffer.allocateDirect(data.length*mBytesPerFloat).order(ByteOrder.nativeOrder()).asFloatBuffer();
        buffer.put(data).position(0);
        return  buffer;
    }
}
