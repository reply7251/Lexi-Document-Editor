package me.sa_g6.adapter;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class DocumentAdapter {
    @Id
    @Column(name = "ELEMENT_ID")
    long id;
    @Column(name = "HTML")
    String html;
    @Column(name = "DOC_STRING")
    String docString;
    @Column(name = "FILE_NAME")
    String name;

    public DocumentAdapter(){
    }

    public DocumentAdapter(long id, String html, String docString, String name){
        this.id = id;
        this.html = html;
        this.docString = docString;
        this.name = name;
    }

    public String getHTML(){
        return html;
    }

    public String getDocString(){
        return docString;
    }

    public String getName(){
        return name;
    }

    public Object[] toTableRow(){
        return new Object[]{id, name};
    }
}
