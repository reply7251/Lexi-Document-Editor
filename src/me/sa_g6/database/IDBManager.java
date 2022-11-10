package me.sa_g6.database;

import me.sa_g6.adapter.AbstractElementAdapter;
import me.sa_g6.adapter.DocumentAdapter;
import me.sa_g6.adapter.ImgEntity;

import javax.swing.text.Element;
import java.util.List;

public interface IDBManager {
    long save(AbstractElementAdapter elementAdapter); //儲存element
    int saveImage(ImgEntity imgEntity); //儲存相片
    ImgEntity getImage(int id); //取得相片
    AbstractElementAdapter get(long id); //取得element

    DocumentAdapter getHtml(long id); //取得html

    List<DocumentAdapter> listDocument();

    void saveHtml(DocumentAdapter adapter); //儲存html

    void removeHtml(long id); //刪除html

    void removeElement(long id); //刪除element

    void begin();

    void commit();
}
