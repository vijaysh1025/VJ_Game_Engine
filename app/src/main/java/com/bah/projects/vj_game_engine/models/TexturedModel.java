package com.bah.projects.vj_game_engine.models;

import android.opengl.GLES20;

import com.bah.projects.vj_game_engine.shaders.ShaderProgram;
import com.bah.projects.vj_game_engine.textures.ModelTexture;

/**
 * Created by 564771 on 8/10/2016.
 */
public class TexturedModel {
    private RawModel rawModel;
    private ModelTexture texture;
    private int mTextureUniformHandle;

    public TexturedModel(RawModel model, ModelTexture texture){
        this.rawModel = model;
        this.texture = texture;
    }


    public RawModel getRawModel() {
        return rawModel;
    }

    public ModelTexture getTexture() {
        return texture;
    }

    public void bindTexture(ShaderProgram shader){
        mTextureUniformHandle = shader.getUniformLocationHandle("u_Texture");

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture.getID());
        GLES20.glUniform1i(mTextureUniformHandle, 0);
    }

}
