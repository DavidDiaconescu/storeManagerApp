package Repo;

import Domain.Comanda;
import Utils.HibernateUtil;
import jakarta.persistence.TypedQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

public class ComandaRepository implements IRepo<Comanda> {


    @Override
    public void create(Comanda comanda) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(comanda); // NU folosim .save, ca să rămână void
            transaction.commit();     // ID-ul va fi setat automat în obiectul primit
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }




    @Override
    public List<Comanda> readAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Comanda", Comanda.class).list();
        }
    }


    @Override
    public Optional<Comanda> readById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Comanda c = session.createQuery(
                            "SELECT c FROM Comanda c LEFT JOIN FETCH c.produse WHERE c.idComanda = :id", Comanda.class)
                    .setParameter("id", id)
                    .uniqueResult();
            return Optional.ofNullable(c);
        }
    }


    @Override
    public void update(Comanda comandaActualizata) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.merge(comandaActualizata);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int idComanda) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Comanda comanda = session.get(Comanda.class, idComanda);
            if (comanda != null) {
                tx = session.beginTransaction();
                session.remove(comanda);
                tx.commit();
            }
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }
}
