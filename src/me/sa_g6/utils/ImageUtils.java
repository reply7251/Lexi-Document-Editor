package me.sa_g6.utils;

import javax.swing.text.Document;
import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Dictionary;
import java.util.Hashtable;

public class ImageUtils {
    static Dictionary<URL, Image> imageCache;
    static int counter = 0;
    public static void setCache(Document document){
        document.putProperty("imageCache", imageCache);
    }

    public static URL putImage(Image image){
        try {
            URL url = new URL("file","", String.valueOf(counter++));
            imageCache.put(url, image);
            return url;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    static {
        imageCache = new Hashtable<>();
    }
}
