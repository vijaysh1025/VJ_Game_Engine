package com.bah.projects.vj_game_engine.shaders;

import android.content.Context;
import android.util.Log;

import com.bah.projects.vj_game_engine.R;

/**
 * Created by 564771 on 8/10/2016.
 */
public class TextureShader extends ShaderProgram{

    private static final String TAG = "Static Shader";
    private static final int VERTEX_RESOURCE = R.raw.texture_vertex_shader;
    private static final int FRAGMENT_RESOURCE = R.raw.texture_fragment_shader;

    public TextureShader(Context context) {

        super(context, VERTEX_RESOURCE, FRAGMENT_RESOURCE);
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "a_Position");
        super.bindAttribute(1, "a_Color");
        super.bindAttribute(2, "a_TexCoordinate");

        Log.i(TAG, "bindAttributes: ");
    }

    @Override
    protected void getAllUniformLocations()
    {
        
    }

}
