package com.sudharsan.kis;


import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WebOperation  {
    public interface IImageListener{

        void onSuccess(List<String> urlList);
        void onError(String error);

    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }

    private ImageLoader imageLoader;
    private static WebOperation webOperation;

    public RequestQueue getQueue() {
        return queue;
    }

    public void setQueue(RequestQueue queue) {
        this.queue = queue;
    }
    private RequestQueue queue;
    private static Context context;
    public  static WebOperation getInstance(Context context){
        WebOperation.context = context;
        return (webOperation==null)?new WebOperation():webOperation;
    }

    private WebOperation()
    {
       queue = Volley.newRequestQueue(context);
        imageLoader =new ImageLoader(queue, new ImageLoader.ImageCache(){
            private final LruCache<String, Bitmap>
                    cache = new LruCache<String, Bitmap>(20);
            @Override
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url,bitmap);
            }
            });

    }
    public  boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return info != null && info.isConnectedOrConnecting();
    }

     public void onImageRequest(String url, final IImageListener listener){
         if(isOnline(context)) {
             JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
                 @Override
                 public void onResponse(JSONObject response) {
                     List<String> urlList = new ArrayList<>();
                     try {
                         JSONArray array = response.getJSONArray("items");
                         for (int i = 0; i < array.length(); i++) {
                             String url = array.getJSONObject(i).
                                     getJSONObject("pagemap").getJSONArray("cse_image").getJSONObject(0).get("src").toString();
                             if (url != null) urlList.add(url);

                         }
                         Log.d("search", array.length() + "");
                         listener.onSuccess(urlList);
                     } catch (JSONException e) {
                         e.printStackTrace();
                         listener.onError("Something went wrong!");
                     }

                 }
             }, new Response.ErrorListener() {
                 @Override
                 public void onErrorResponse(VolleyError error) {
                     listener.onError("Error in network");
                 }
             });
             //adding Request to queue
             queue.add(jsonObjectRequest);
         }else {
             listener.onError("Network is offline");
         }
     }




}
