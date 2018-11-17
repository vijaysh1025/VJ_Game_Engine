package com.bah.projects.vj_game_engine.renderEngine;

import android.opengl.GLES20;
import android.opengl.GLES30;
import android.util.Log;

import com.bah.projects.vj_game_engine.models.RawModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by 564771 on 7/31/2016.
 */
public class Loader {

    private final int mBytesPerFloat = 4;
    private final int mBytesPerInt = 2;
    private final int mPositionDataSize = 3;

    private static final String TAG = "LOADER";

    private ArrayList<Integer> vaos = new ArrayList<Integer>();
    private ArrayList<Integer> vbos = new ArrayList<Integer>();



    public RawModel loadToVAO(float[] positions, float[] normals, float[] textureCoords, short[] indices){
        //int vaoID = createVAO();

        int vertexVBO = storeDataInAttributeList(positions);
        int normalsVBO = storeDataInAttributeList(normals);
        int textureVBO = storeDataInAttributeList(textureCoords);
        int indexVBO = bindIndicesBuffer(indices);


        //storeDataInAttributeList(1, textureCoords,2);
        //unbindVAO();
        return new RawModel(vertexVBO,normalsVBO,indexVBO,textureVBO, indices.length);
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

    private int storeDataInAttributeList(float[] data) {
        int[] vboID = new int[1];
        try {

            GLES20.glGenBuffers(1, vboID, 0);
            if(vboID[0]>0) {
                vbos.add((vboID[0]));
                FloatBuffer buffer = storeDataInFloatBuffer(data);
                GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vboID[0]);

                //Log.i(TAG, "vbo Attrib: buffer " + buffer);
                GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, buffer.capacity() * mBytesPerFloat, buffer, GLES20.GL_STATIC_DRAW);
                //GLES20.glVertexAttribPointer(attributeNumber,size,GLES20.GL_FLOAT,false,0,0);
                GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
            }
        }catch (Throwable t) {
            Log.i(TAG, "storeDataInAttributeList: Error loading vbo" + t);
        }
        return vboID[0];
    }

    private  void unbindVAO(){
        GLES30.glBindVertexArray(0);
    }

    private  int bindIndicesBuffer(short[] indices){
        int[] vboID = new int[1];
        try {
            GLES20.glGenBuffers(1, vboID, 0);
            if(vboID[0]>0) {
                ShortBuffer buffer = storeDataInIntBuffer(indices);
                GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, vboID[0]);

                //Log.i(TAG, "bindIndices: buffer " + buffer);
                GLES20.glBufferData(GLES20.GL_ELEMENT_ARRAY_BUFFER, buffer.capacity() * mBytesPerInt, buffer, GLES20.GL_STATIC_DRAW);
                //GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);
            }
        }
        catch (Throwable t){
            Log.i(TAG, "storeDataInAttributeList: Error loading vbo" + t);
        }
        return vboID[0];
    }

    private ShortBuffer storeDataInIntBuffer(short[] data)
    {
        //Log.i(TAG, "storeDataInIntBuffer: "+data.length);
        ShortBuffer buffer = ByteBuffer.allocateDirect(data.length*mBytesPerInt).order(ByteOrder.nativeOrder()).asShortBuffer();
        buffer.put(data).position(0);
        return  buffer;
    }


    private FloatBuffer storeDataInFloatBuffer(float[] data){
        Log.i(TAG, "storeDataInFloatBuffer: "+data.length);
        FloatBuffer buffer = ByteBuffer.allocateDirect(data.length*mBytesPerFloat).order(ByteOrder.nativeOrder()).asFloatBuffer();
        buffer.put(data).position(0);
        return  buffer;
    }


}
