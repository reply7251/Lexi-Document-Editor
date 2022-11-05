package me.sa_g6.utils;

import javax.swing.text.Document;
import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;

public class ImageCache {
    Dictionary<URL, Image> cache = new Hashtable<>();

    static HashMap<Document, ImageCache> imageCaches = new HashMap<>();

    int counter = 0;


    ImageCache(){

    }
    public Dictionary<URL, Image> getCache(){
        return cache;
    }

    public static ImageCache getImageCache(Document document){

        if(!imageCaches.containsKey(document)){
            ImageCache imageCache = new ImageCache();
            document.putProperty("imageCache", imageCache.getCache());
            imageCaches.put(document,imageCache);
        }
        return imageCaches.get(document);
    }

    public static URL putImage(Document document, Image image){
        try {
            ImageCache cache = getImageCache(document);
            URL url = new URL("file","", String.valueOf(cache.counter++));
            cache.cache.put(url,image);
            return url;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
