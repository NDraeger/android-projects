package com.ndraeger.storify.network;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class NetworkTransmissionCoordinator {

    private static NetworkTransmissionCoordinator instance;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;
    private static Context context;

    private NetworkTransmissionCoordinator(Context context) {
        this.context = context;
        requestQueue = getRequestQueue();

        imageLoader = new ImageLoader(requestQueue,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap>
                            cache = new LruCache<String, Bitmap>(20);

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                });
    }

    public static synchronized NetworkTransmissionCoordinator getInstance(Context context) {
        if(instance == null) {
            instance = new NetworkTransmissionCoordinator(context);
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if(requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> request) {
        getRequestQueue().add(request);
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }
}
