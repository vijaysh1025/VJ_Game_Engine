package com.bah.projects.vj_game_engine.renderEngine;

import android.util.Log;

import com.bah.projects.vj_game_engine.models.RawModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import android.os.AsyncTask;

import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;

/**
 * Created by 564771 on 8/25/2016.
 */
public class OBJLoader {

    private static final String TAG = "OBJLoader";



    public RawModel loadObjModel(InputStream is) throws IOException{
        Loader loader = new Loader();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        List<Vector3f> vertices = new ArrayList<Vector3f>();
        List<Vector3f> normals = new ArrayList<Vector3f>();
        List<Vector2f> textureCoords = new ArrayList<Vector2f>();
        List<Short> indices = new ArrayList<Short>();
        float[] verticesArray = null;
        float[] normalsArray = null;
        float[] textureCoordsArray = null;
        short[] indicesArray = null;
        String line = null;

        while (reader.ready())
        {
            line = reader.readLine();
            if (line == null)
                break;

            String[] currentLine = line.split(" ");

            if (line.startsWith("v ")) {
                Vector3f vertex = new Vector3f(Float.parseFloat(currentLine[1]),Float.parseFloat(currentLine[2]),Float.parseFloat(currentLine[3]));
                vertices.add(vertex);
            }
            else
            if (line.startsWith("vn ")) {
                Vector3f vertex = new Vector3f(Float.parseFloat(currentLine[1]),Float.parseFloat(currentLine[2]),Float.parseFloat(currentLine[3]));
                normals.add(vertex);
            }
            else
            if (line.startsWith("vt ")) {
                Vector2f vertex = new Vector2f(Float.parseFloat(currentLine[1]),Float.parseFloat(currentLine[2]));
                textureCoords.add(vertex);
            }
            else
            if (line.startsWith("f ")) {
                textureCoordsArray = new float[vertices.size()*2];
                normalsArray = new float[vertices.size()*3];
                break;
            }
        }

        while (line != null)
        {

            if (!line.startsWith("f ")) {
                line = reader.readLine();
                continue;
            }
            String[] currentLine = line.split(" ");
            String[] vertex1 = currentLine[1].split("/");
            String[] vertex2 = currentLine[2].split("/");
            String[] vertex3 = currentLine[3].split("/");

            processVertex(vertex1, indices, textureCoords, normals, textureCoordsArray, normalsArray);
            processVertex(vertex2, indices, textureCoords, normals, textureCoordsArray, normalsArray);
            processVertex(vertex3, indices, textureCoords, normals, textureCoordsArray, normalsArray);

            //Log.i(TAG, "loadObjModel: " + vertex1.toString());

            line = reader.readLine();
        }

        verticesArray = new float[vertices.size()*3];
        indicesArray = new short[indices.size()];

        int vertexPointer = 0;
        for (Vector3f vertex:vertices){
            verticesArray[vertexPointer++] = vertex.x;
            verticesArray[vertexPointer++] = vertex.y;
            verticesArray[vertexPointer++] = vertex.z;
        }

        for(int i=0; i<indices.size(); i++){
            indicesArray[i] = indices.get(i);
        }

        return loader.loadToVAO(verticesArray,normalsArray,textureCoordsArray,indicesArray);
    }

    private static void processVertex(String[] vertexData, List<Short> indices, List<Vector2f> textures, List<Vector3f> normals, float[] textureArray, float[] normalsArray)
    {
        short currentVertexPointer = (short) (Integer.parseInt(vertexData[0]) - 1);
        indices.add(currentVertexPointer);
        Vector2f currentTex = textures.get(Integer.parseInt(vertexData[1])-1);
        textureArray[currentVertexPointer*2] = currentTex.x;
        textureArray[currentVertexPointer*2+1] = currentTex.y;

        //Log.i(TAG, "processVertex: " + vertexData[2]);
        Vector3f currentNormal = normals.get(Integer.parseInt(vertexData[2])-1);
        normalsArray[currentVertexPointer*3] = currentNormal.x;
        normalsArray[currentVertexPointer*3+1] = currentNormal.y;
        normalsArray[currentVertexPointer*3+2] = currentNormal.z;

    }

    public  RawModel  LoadModel(InputStream is){
        RawModel rawModel = null;

        try {
            rawModel = new LoadModelWorker().execute(is).get();
        }
        catch (java.lang.InterruptedException e)
        {
            System.out.print("Load Model Failed Interrupted Exception");
        }
        catch (java.util.concurrent.ExecutionException e)
        {
            System.out.print("Load Model Failed ExecutionException");
        }

        return rawModel;
    }

    public class LoadModelWorker extends AsyncTask<InputStream, Void, RawModel> {
        protected RawModel doInBackground(InputStream... is) {

            RawModel rawModel = null;
            try {
                rawModel = loadObjModel(is[0]);
            } catch (java.io.IOException e) {
                System.out.print("Model Failed to Load");
            }

            return rawModel;
        }



        protected void onPostExecute(RawModel result) {

        }
    }

}
