package com.bah.projects.vj_game_engine.renderEngine;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.EntityIterator;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.AsyncTask;
import android.util.Log;

import com.bah.projects.vj_game_engine.R;
import com.bah.projects.vj_game_engine.common.TextureHelper;
import com.bah.projects.vj_game_engine.entities.Entity;
import com.bah.projects.vj_game_engine.lights.PointLight;
import com.bah.projects.vj_game_engine.models.RawModel;
import com.bah.projects.vj_game_engine.models.TexturedModel;
import com.bah.projects.vj_game_engine.shaders.ShaderProgram;
import com.bah.projects.vj_game_engine.shaders.StaticShader;
import com.bah.projects.vj_game_engine.textures.ModelTexture;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import javax.vecmath.Vector3f;

/**
 * Created by 564771 on 7/31/2016.
 */
public class VJSurfaceRenderer implements GLSurfaceView.Renderer{

    private static final String TAG = "SurfaceRenderer";
    Loader loader;
    RawModel model;
    ShaderProgram shader;
    Context context;
    ModelTexture texture;
    TexturedModel texturedModel;
    Entity currentEntity;
    private  int entityIndex =0;

    List<EntityParams> heroEntities;

    private class EntityParams{
        public int getObjResource() {
            return objResource;
        }

        public int getTextureResource() {
            return textureResource;
        }

        public Loader getLoader() {
            return loader;
        }

        public float getDistance() {
            return distance;
        }

        int objResource;
        int textureResource;
        Loader loader;
        float distance;

        public EntityParams(int objResource, int textureResource, Loader loader, float distance) {
            this.objResource = objResource;
            this.textureResource = textureResource;
            this.loader = loader;
            this.distance = distance;
        }
    }

    private float mAngleX = 0;

    private float[] mViewMatrix = new float[16];
    private float[] mProjectionMatrix = new float[16];
    boolean isLightOn = false;

    private Entity createEntity(EntityParams params)
    {
        return createEntity(params.getObjResource(), params.getTextureResource(), params.getLoader(), params.getDistance());
    }

    private Entity createEntity(int ObjResource, int textureResource, Loader loader, float distance)
    {
        RawModel model;
        ModelTexture texture;
        TexturedModel texturedModel;
        Entity entity;

        try{
            model = new OBJLoader().loadObjModel(context.getResources().openRawResource(ObjResource));
            //model = OBJLoader.loadObjModel(context.getResources().openRawResource(ObjResource),loader);
            texture = new ModelTexture(new TextureHelper().loadTexture(context, textureResource));
            texturedModel = new TexturedModel(model, texture);
            entity = new Entity(texturedModel,new Vector3f(0,0,distance),0,0,0,1);
            return  entity;
        } catch (java.io.IOException e) {
            System.out.print("Model Failed to Load");
        }

        return null;

    }



    public void ShowNextHero()
    {
        entityIndex++;

        if(entityIndex>=heroEntities.size())
            entityIndex = 0;

        Entity entity = currentEntity;
        entity.getModel().getRawModel().deleteVbos();
        //entity = createEntity(heroEntities.get(entityIndex));
    }

    public void ShowPreviousHero()
    {
        entityIndex--;
        if(entityIndex<0)
            entityIndex = heroEntities.size()-1;
        Entity entity = currentEntity;
        entity = createEntity(heroEntities.get(entityIndex));
    }


    public VJSurfaceRenderer(Context activityContext)
    {

        context = activityContext;

    }

    @Override
    public  void  onSurfaceCreated(GL10 glUnused, EGLConfig config)
    {
        GLES20.glClearColor(0.5f, 0.5f, 0.5f, 0.5f);

        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        loader = new Loader();
        heroEntities = new ArrayList<EntityParams>();

        //new LoadEntity().execute(new EntityParams(R.raw.ironman, R.drawable.ironman_d, loader, -3.5f));
        //new LoadEntity().execute(new EntityParams(R.raw.spiderman, R.drawable.spiderman_d, loader, -3.5f));
        //new LoadEntity().execute(new EntityParams(R.raw.venom, R.drawable.venom_d, loader, -3.5f));
        //new LoadEntity().execute(new EntityParams(R.raw.ironman_hb, R.drawable.ironman_hb_d, loader, -5f));
        //heroEntities.add(createEntity(R.raw.ironman,R.drawable.ironman_d, loader,-3.5f));
        //heroEntities.add(createEntity(R.raw.spiderman,R.drawable.spiderman_d, loader,-3.5f));
        //heroEntities.add(createEntity(R.raw.venom,R.drawable.venom_d, loader,-3.5f));
        //heroEntities.add(createEntity(R.raw.ironman_hb,R.drawable.ironman_hb_d, loader,-5f));

        heroEntities.add(new EntityParams(R.raw.ironman, R.drawable.ironman_d, loader, -3.5f));
        heroEntities.add(new EntityParams(R.raw.spiderman, R.drawable.spiderman_d, loader, -3.5f));
        heroEntities.add(new EntityParams(R.raw.venom, R.drawable.venom_d, loader, -3.5f));
        heroEntities.add(new EntityParams(R.raw.ironman_hb, R.drawable.ironman_hb_d, loader, -5f));

        currentEntity = createEntity(heroEntities.get(entityIndex));

        Log.i(TAG, "VJSurfaceRenderer: model Complete");



        // Position the eye behind the origin.
        final float eyeX = 0.0f;
        final float eyeY = 2.0f;
        final float eyeZ = -0.5f;

        // We are looking toward the distance
        final float lookX = 0.0f;
        final float lookY = 2f;
        final float lookZ = -5.0f;

        // Set our up vector. This is where our head would be pointing were we holding the camera.
        final float upX = 0.0f;
        final float upY = 1.0f;
        final float upZ = 0.0f;

        // Set the view matrix. This matrix can be said to represent the camera position.
        // NOTE: In OpenGL 1, a ModelView matrix is used, which is a combination of a model and
        // view matrix. In OpenGL 2, we can keep track of these matrices separately if we choose.
        Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ);

        shader = new StaticShader(context);

        Log.i(TAG, "VJSurfaceRenderer: shader Complete");
    }

    @Override
    public  void  onSurfaceChanged(GL10 glUnused, int width, int height)
    {
        GLES20.glViewport(0,0,width,height);

        final  float ratio = (float) width / height;
        final  float left = -ratio;
        final  float right = ratio;
        final  float bottom = -1.0f;
        final  float top = 1.0f;
        final  float near = 1.0f;
        final  float far = 100.0f;

        Matrix.frustumM(mProjectionMatrix, 0, left, right, bottom, top, near, far);
    }

    @Override
    public  void  onDrawFrame(GL10 glUnused)
    {
        prepare();
        shader.start();
        if(currentEntity != null) {
            currentEntity.getModel().bindTexture(shader);
            currentEntity.updateMVPMatrix(mViewMatrix, mProjectionMatrix, shader);
            if (isLightOn)
                PointLight.Add(0, 2.5f, -3, shader, mViewMatrix);
            else
                PointLight.Add(0, 100, -3, shader, mViewMatrix);
            currentEntity.setRotY(mAngleX);

            currentEntity.getModel().getRawModel().render(shader);
        }
    }



    public  void prepare(){
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

    }

    public void ToggleLight(boolean val)
    {
        isLightOn = val;
    }


    public void render(Entity entity, StaticShader shader){
        TexturedModel model = entity.getModel();
        RawModel rawModel = model.getRawModel();

    }


    public void setAngleX(float angle) {
        mAngleX = angle;
    }
    public float getAngleX() {
        return mAngleX;
    }


}
