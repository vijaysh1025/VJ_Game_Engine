package com.bah.projects.vj_game_engine.shaders;

import android.content.Context;

import com.bah.projects.vj_game_engine.R;

/**
 * Created by 564771 on 8/10/2016.
 */
public class StaticShader extends ShaderProgram{

    private static final int VERTEX_RESOURCE = R.raw.color_vertex_shader;
    private static final int FRAGMENT_RESOURCE = R.raw.color_fragment_shader;

    public StaticShader(Context context) {

        super(context, VERTEX_RESOURCE, FRAGMENT_RESOURCE);
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "a_Position");
        super.bindAttribute(1, "a_Color");
        //super.bindAttribute(1, "a_TexCoordinate");
    }
}
