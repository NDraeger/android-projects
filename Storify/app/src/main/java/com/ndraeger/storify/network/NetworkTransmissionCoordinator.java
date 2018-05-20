package com.ndraeger.storify.network;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class NetworkTransmissionCoordinator {

    private static NetworkTransmissionCoordinator instance;
    private RequestQueue requestQueue;
    private static Context context;

    private NetworkTransmissionCoordinator(Context context) {
        this.context = context;

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
}
