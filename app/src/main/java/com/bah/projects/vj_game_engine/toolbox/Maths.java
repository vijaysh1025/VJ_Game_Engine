package com.bah.projects.vj_game_engine.toolbox;

import javax.vecmath.AxisAngle4f;
import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

/**
 * Created by 564771 on 8/23/2016.
 */
public class Maths {
    public  static Matrix4f createTransformationMatrix(Vector3f translation, float rx, float ry, float rz, float scale)
    {
        Matrix4f matrix = new Matrix4f();
        matrix.setIdentity();
        matrix.setTranslation(translation);
        matrix.setRotation(new AxisAngle4f((float)Math.toRadians(rx),1,0,0));
        matrix.setRotation(new AxisAngle4f((float)Math.toRadians(ry),0,1,0));
        matrix.setRotation(new AxisAngle4f((float)Math.toRadians(rz),0,0,1));
        matrix.setScale(scale);

        return matrix;

    }

}
