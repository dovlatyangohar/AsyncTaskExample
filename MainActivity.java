package com.example.asynctaskexample;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;
    private ImageView imageView;
    private Button startButton;
    private URL ImageUrl;
    private InputStream inputStream;
    private Bitmap bmImg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.image_view);
        startButton = findViewById(R.id.downloadButton);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ExampleAsyncTask(MainActivity.this).execute();
            }
        });

    }


    private class ExampleAsyncTask extends AsyncTask<String, Bitmap, Bitmap> {
        private WeakReference<MainActivity> activityWeakReference;

        ExampleAsyncTask(MainActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            MainActivity activity = activityWeakReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }

            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Please wait..Image is downloading");
            progressDialog.setIndeterminate(false);
            progressDialog.show();

        }

        @Override
        protected Bitmap doInBackground(String... strings) {


            try {
                ImageUrl = new URL("https://api.androidhive.info/json/movies/2.jpg");
                HttpURLConnection conn = (HttpURLConnection)
                        ImageUrl.openConnection();
                conn.setDoInput(true);
                conn.connect();
                inputStream = conn.getInputStream();
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.RGB_565;
                bmImg = BitmapFactory.decodeStream(inputStream, null, options);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bmImg;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (imageView != null) {
                progressDialog.hide();
                imageView.setImageBitmap(bitmap);
            } else {
                progressDialog.show();
            }

        }

    }
}
