package com.bah.projects.vj_game_engine.models;

import com.bah.projects.vj_game_engine.textures.ModelTexture;

/**
 * Created by 564771 on 8/10/2016.
 */
public class TexturedModel {
    private RawModel rawModel;
    private ModelTexture texture;

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

}
