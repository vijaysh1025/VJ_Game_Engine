package com.bah.projects.vj_game_engine.entities;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.bah.projects.vj_game_engine.models.TexturedModel;
import com.bah.projects.vj_game_engine.shaders.ShaderProgram;

import javax.vecmath.Vector3f;

/**
 * Created by 564771 on 8/24/2016.
 */
public class Entity {
    private TexturedModel model;
    private Vector3f position;
    private float rotX,rotY,rotZ;
    private float scale;

    private float[] mModelMatrix = new float[16];
    private float[] mMVPMatrix = new float[16];



    private float[] mMVMatrix = new float[16];

    public void updateMVPMatrix(float[] ViewMatrix, float[] ProjectionMatrix, ShaderProgram shader){

        int mMVPMatrixHandle = shader.getUniformLocationHandle("u_MVPMatrix");
        int mMVMatrixHandle = shader.getUniformLocationHandle("u_MVMatrix");

        updateMatrix();
        Matrix.multiplyMM(mMVMatrix, 0, ViewMatrix, 0, mModelMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, ProjectionMatrix, 0, mMVMatrix, 0);
        GLES20.glUniformMatrix4fv(mMVMatrixHandle, 1, false, mMVMatrix, 0);
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);

    }



    public float[] getModelMatrix() {
        return mModelMatrix;
    }

    public float[] getMVMatrix() {
        return mMVMatrix;
    }

    public float[] getEyeSpace(float x, float y, float z)
    {
        float[] lightPos = {x,y,z,1};
        float[] lightEyeSpace = new float[4];
        Matrix.multiplyMV(lightEyeSpace,0,mMVMatrix,0,lightPos,0);

        return  lightEyeSpace;
    }


    public Entity(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
        this.model = model;
        this.position = position;
        this.rotX = rotX;
        this.rotY = rotY;
        this.rotZ = rotZ;
        this.scale = scale;
        updateMatrix();
    }

    public void increasePosition(float dx, float dy, float dz){
        this.position.x+=dx;
        this.position.y+=dy;
        this.position.z+=dz;

        updateMatrix();
    }

    public void increaseRotation(float dx, float dy, float dz){
        rotX+=dx;
        rotY+=dy;
        rotZ+=dz;

        updateMatrix();
    }

    public TexturedModel getModel() {
        return model;
    }

    public void setModel(TexturedModel model) {
        this.model = model;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
        updateMatrix();
    }

    public float getRotX() {
        return rotX;
    }

    public void setRotX(float rotX) {
        this.rotX = rotX;
        updateMatrix();
    }

    public float getRotY() {
        return rotY;
    }

    public void setRotY(float rotY) {
        this.rotY = rotY;
        updateMatrix();
    }

    public float getRotZ() {
        return rotZ;
    }

    public void setRotZ(float rotZ) {
        this.rotZ = rotZ;
        updateMatrix();
    }

    public float getScale() {
        return scale;
    }

    public void updateMatrix()
    {
        Matrix.setIdentityM(mModelMatrix,0);
        Matrix.translateM(mModelMatrix,0,position.x,position.y,position.z);
        Matrix.rotateM(mModelMatrix,0,rotX,1f,0f,0f);
        Matrix.rotateM(mModelMatrix,0,rotY,0f,1f,0f);
        Matrix.rotateM(mModelMatrix,0,rotZ,0f,0f,1f);
        Matrix.scaleM(mModelMatrix,0,scale,scale,scale);
    }


    public void setScale(float scale) {
        this.scale = scale;
    }

}
