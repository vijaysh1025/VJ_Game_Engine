package com.bah.projects.vj_game_engine;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ToggleButton;

import com.bah.projects.vj_game_engine.renderEngine.DisplayManager;

public class MainActivity extends Activity {

    /** Hold a reference to our GLSurfaceView */
    private GLSurfaceView mGLSurfaceView;
    private FrameLayout frame;
    private Button backButton;
    private Button nextButton;
    private ToggleButton toggleButton;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGLSurfaceView = new DisplayManager(this.getApplicationContext());

        frame = (FrameLayout)findViewById(R.id.frameLayout);
        frame.addView(mGLSurfaceView);

        backButton = (Button)findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DisplayManager)mGLSurfaceView).getRenderer().ShowPreviousHero();

            }
        });

        nextButton = (Button)findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DisplayManager)mGLSurfaceView).getRenderer().ShowNextHero();

            }
        });

        toggleButton = (ToggleButton)findViewById(R.id.lightToggle);
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ((DisplayManager)mGLSurfaceView).getRenderer().ToggleLight(toggleButton.isChecked());
            }
        });




        //setContentView(mGLSurfaceView);
    }

    @Override
    protected void onResume()
    {
        // The activity must call the GL surface view's onResume() on activity onResume().
        super.onResume();
        mGLSurfaceView.onResume();
    }

    @Override
    protected void onPause()
    {
        // The activity must call the GL surface view's onPause() on activity onPause().
        super.onPause();
        mGLSurfaceView.onPause();
    }
}
