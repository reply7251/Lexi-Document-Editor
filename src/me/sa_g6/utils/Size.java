package me.sa_g6.utils;

public class Size<T> {
    T width, height;
    public Size(T w, T h){
        width = w;
        height = h;
    }

    public T getWidth() {
        return width;
    }

    public T getHeight() {
        return height;
    }
}
