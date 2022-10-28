package me.sa_g6.database;

import me.sa_g6.adapter.AbstractElementAdapter;
import me.sa_g6.adapter.DocumentAdapter;
import me.sa_g6.adapter.ImgEntity;

import javax.swing.text.Element;
import java.util.List;

public interface IDBManager {
    long save(AbstractElementAdapter elementAdapter);
    int saveImage(ImgEntity imgEntity);
    ImgEntity getImage(int id);
    AbstractElementAdapter get(long id);

    DocumentAdapter getHtml(long id);

    List<DocumentAdapter> listDocument();

    void saveHtml(DocumentAdapter adapter);

    void removeHtml(long id);

    void removeElement(long id);

    void begin();

    void commit();
}
