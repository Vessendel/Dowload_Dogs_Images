package com.example.wolf.dowloaddogsimages;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends ActionBarActivity {
    Button load_img;

    ImageView img1;
    ImageView img2;
    ImageView img3;
    ImageView img4;
    ImageView img5;

    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        load_img = (Button)findViewById(R.id.load);
        img1 = (ImageView)findViewById(R.id.im1);
        img2 = (ImageView)findViewById(R.id.im2);
        img3 = (ImageView)findViewById(R.id.im3);
        img4 = (ImageView)findViewById(R.id.im4);
        img5 = (ImageView)findViewById(R.id.im5);
        pDialog = new ProgressDialog(MainActivity.this);
        pDialog.setMessage("Loading Image ....");

        load_img.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View v) {


                try {
                    String json = loadJSONFromAsset();
                    JSONObject parsedjson = new JSONObject(json);
                    Iterator<String> iter = parsedjson.keys();
                    ImageView[] IMGS = { img1, img2, img3,img4,img5 };
                    while (iter.hasNext()) {
                        String key = iter.next();
                        try {
                            String value = (String) parsedjson.get(key);
                            LoadImage Loader = new LoadImage();

                            Loader.imgs = IMGS[Integer.parseInt(key)];
                            Loader.execute(value);
                        } catch (JSONException ignored) {

                        }
                    }

                    System.out.println(parsedjson);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }
    public String loadJSONFromAsset() throws Exception {
        String json = null;
        try {

            InputStream is = getAssets().open("json.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }
    private class LoadImage extends AsyncTask<String, String, Bitmap> {
        ImageView imgs;
        @Override

        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.show();

        }
        protected Bitmap doInBackground(String... args) {
            Bitmap bitmap = null;
            try {
                bitmap = BitmapFactory.decodeStream((InputStream)new URL(args[0]).getContent());
                bitmap = Bitmap.createScaledBitmap(bitmap, 200, 200, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap image) {
            if(image != null){
                imgs.setImageBitmap(image);
            }else{
                Toast.makeText(MainActivity.this, "Image Does Not exist or Network Error", Toast.LENGTH_SHORT).show();
            }
            pDialog.dismiss();
        }
    }



}
