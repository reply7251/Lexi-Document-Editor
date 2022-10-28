package me.sa_g6.database;

import me.sa_g6.adapter.AbstractElementAdapter;
import me.sa_g6.adapter.DocumentAdapter;
import me.sa_g6.adapter.ImgEntity;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.List;

public class ODBManager implements IDBManager{
    EntityManagerFactory emf;
    EntityManager em;

    public ODBManager(String file){
        emf = Persistence.createEntityManagerFactory(file);
        em = emf.createEntityManager();
    }

    @Override
    public long save(AbstractElementAdapter elementAdapter) {
        em.persist(elementAdapter);
        em.flush();
        return elementAdapter.getId();
    }

    @Override
    public int saveImage(ImgEntity imgEntity) {
        em.persist(imgEntity);
        em.flush();
        return imgEntity.getId();
    }

    @Override
    public ImgEntity getImage(int id) {
        return em.find(ImgEntity.class, id);
    }


    @Override
    public AbstractElementAdapter get(long id) {
        return em.find(AbstractElementAdapter.class, id);
    }

    @Override
    public DocumentAdapter getHtml(long id) {
        return em.find(DocumentAdapter.class, id);
    }

    @Override
    public List<DocumentAdapter> listDocument() {
        TypedQuery<DocumentAdapter> query = em.createQuery("Select a from DocumentAdapter a",
                DocumentAdapter.class);

        return query.getResultList();
    }

    @Override
    public void saveHtml(DocumentAdapter adapter) {
        em.persist(adapter);
    }

    @Override
    public void removeHtml(long id) {
        removeElement(id);
        em.remove(getHtml(id));
    }

    @Override
    public void removeElement(long id) {
        AbstractElementAdapter adapter = get(id);
        for(long child : adapter.getChildren()){
            removeElement(child);
        }
        em.remove(adapter);
    }


    @Override
    public void begin() {
        em.getTransaction().begin();
    }


    @Override
    public void commit() {
        em.getTransaction().commit();
    }
}
