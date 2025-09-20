package Repo;

import Domain.Produs;
import Utils.HibernateUtil;
import jakarta.persistence.TypedQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

public class ProdusRepository implements IRepo<Produs> {

    @Override
    public void create(Produs produs) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(produs);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public List<Produs> readAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            TypedQuery<Produs> query = session.createQuery("from Produs", Produs.class);
            return query.getResultList();
        }
    }

    @Override
    public Optional<Produs> readById(int idProdus) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Produs produs = session.get(Produs.class, idProdus);
            return Optional.ofNullable(produs);
        }
    }

    @Override
    public void update(Produs produsActualizat) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.merge(produsActualizat);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int idProdus) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Produs produs = session.get(Produs.class, idProdus);
            if (produs != null) {
                tx = session.beginTransaction();
                session.remove(produs);
                tx.commit();
            }
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }
}
