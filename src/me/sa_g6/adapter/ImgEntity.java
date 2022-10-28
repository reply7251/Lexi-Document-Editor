package me.sa_g6.adapter;

import javax.imageio.ImageIO;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.awt.*;
import java.awt.image.RenderedImage;
import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;
import java.util.List;

@Entity
public class ImgEntity{

    @Id @GeneratedValue
    private int id;
    byte[] images;
    private String url;
    public void setImages(byte[] images) {
        this.images = images;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public byte[] getImages() {
        return images;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ImgEntity(){
    }
    public ImgEntity(URL url, Image image) throws IOException {
        ByteArrayOutputStream outStreamObj = new ByteArrayOutputStream();
        ImageIO.write((RenderedImage) image, "png", outStreamObj);
        byte [] byteArray = outStreamObj.toByteArray();
        this.images = byteArray;
        this.url = url.toString();
    }
}

