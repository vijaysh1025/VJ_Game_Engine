package com.bah.projects.vj_game_engine.renderEngine;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

/**
 * Created by 564771 on 7/31/2016.
 */
public class DisplayManager extends GLSurfaceView{
    private VJSurfaceRenderer mRenderer;

    private float mPreviousX0;
    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;

    public DisplayManager(Context context) {
        super(context);

        // Create an OpenGL ES 2.0 context.
        setEGLContextClientVersion(2);


        // Set the Renderer for drawing on the GLSurfaceView
        //LoadModel();
        mRenderer = new VJSurfaceRenderer(context);

        setRenderer(mRenderer);


        // Render the view only when there is a change in the drawing data
        //setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mRenderer.shader.stop();
        super.surfaceDestroyed(holder);

    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.

        int maskedAction = e.getActionMasked();

        float x = e.getX();
        float y = e.getY();

        switch (maskedAction) {

            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_POINTER_DOWN: {

                break;
            }
            case MotionEvent.ACTION_MOVE:




                    float dx = x - mPreviousX0;
                    float dy = y;

                    // reverse direction of rotation above the mid-line
                    if (y > getHeight() / 2) {
                        dx = dx * -1;
                    }

                    // reverse direction of rotation to left of the mid-line
                    if (x < getWidth() / 2) {
                        dy = dy * -1;
                    }

                    mRenderer.setAngleX(mRenderer.getAngleX() - (dx));  // = 180.0f / 320
                    requestRender();



                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        mPreviousX0 = x;

        return true;
    }
}
