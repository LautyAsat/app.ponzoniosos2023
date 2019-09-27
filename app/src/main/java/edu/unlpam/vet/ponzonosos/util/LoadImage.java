package edu.unlpam.vet.ponzonosos.util;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class LoadImage extends AsyncTask<String, Void, Bitmap> {

    private static final String TAG = "RMD-LoadImage";

    public interface Listener{
        void onImageLoaded(Bitmap bitmap);
        void onError();
    }

    private Listener mListener;

    public LoadImage(Listener listener) {
        mListener = listener;
    }

    @Override
    protected Bitmap doInBackground(String... args) {
        try {
            return BitmapFactory.decodeStream((InputStream)new URL(args[0]).getContent());
        } catch (IOException e) {
            Log.e(TAG, "doInBackground: Error when downloading image bitmap from url " +
                    args[0], e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (bitmap != null) {
            mListener.onImageLoaded(bitmap);
        } else {
            mListener.onError();
        }
    }

}

