package com.example.cache.iamgecache;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

/**
 * Created by Administrator on 2018/4/18 0018.
 */

public class BitmapCache implements ImageLoader.ImageCache {
    public LruCache<String,Bitmap> lruCache;
    public int maxSize = 10*1024*1024;


    public BitmapCache(){
        lruCache = new LruCache<String,Bitmap>(maxSize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getHeight()*value.getWidth();
            }
        };


    }

    @Override
    public Bitmap getBitmap(String url) {
        return lruCache.get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        lruCache.put(url, bitmap);
    }
}
