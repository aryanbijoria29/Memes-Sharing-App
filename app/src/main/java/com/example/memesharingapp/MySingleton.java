package com.example.memesharingapp;
import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MySingleton {

    private static MySingleton INSTANCE;
    private RequestQueue requestQueue;

    private MySingleton(Context context) {
        // Initialize the RequestQueue using the application context to prevent leaks.
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }

    public static synchronized MySingleton getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new MySingleton(context);
        }
        return INSTANCE;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        requestQueue.add(req);
    }
}
