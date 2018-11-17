package com.bah.projects.vj_game_engine.jello;


import java.util.ArrayList;

import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3d;

/**
 * Created by 564771 on 8/22/2016.
 */
public class Jello {

    private static final int CUBESIZE = 3;
    private static final float REST_DISTANCE = 0.5f;



    public float[] positions = new float[(CUBESIZE*CUBESIZE*CUBESIZE-(CUBESIZE-2)*(CUBESIZE-2)*(CUBESIZE-2))*3];
    public float[] indices = new float[(CUBESIZE-1)*(CUBESIZE-1)*2*6];

    Point3f[][][] cubePoints = new Point3f[CUBESIZE][CUBESIZE][CUBESIZE];

    public Jello()
    {
        for (int k = 0; k<CUBESIZE; k++)
        {
            for (int j = 0; j<CUBESIZE; j++)
            {
                for (int i = 0; i<CUBESIZE; i++)
                {
                    cubePoints[i][j][k] = new Point3f((i-CUBESIZE/2)*REST_DISTANCE, (j-CUBESIZE/2)*REST_DISTANCE, (k-CUBESIZE/2)*REST_DISTANCE);
                }
            }
        }
    }

    public void UpdatePositionArray()
    {
        int n = 0;

        for (int k = 0; k<CUBESIZE; k++)
        {
            for (int j = 0; j<CUBESIZE; j++)
            {
                for (int i = 0; i<CUBESIZE; i++)
                {
                    if(i==0 || i==CUBESIZE-1 || j==0 || j==CUBESIZE-1 || k==0 || k==CUBESIZE-1)
                    {
                        positions[n++] = (float)cubePoints[i][j][k].x;
                        positions[n++] = (float)cubePoints[i][j][k].y;
                        positions[n++] = (float)cubePoints[i][j][k].z;
                    }
                }
            }
        }

    }

    public float[] UpdateIndicesArray()
    {



        return  positions;
    }


}
