package Repo;

import Domain.Agent;
import Utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

public class AgentRepository implements IRepo<Agent> {

    @Override
    public void create(Agent entity) {

    }

    @Override
    public List<Agent> readAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Agent", Agent.class).list();
        }
    }

    @Override
    public Optional<Agent> readById(int id) {
        return Optional.empty();
    }

    @Override
    public void update(Agent entity) {

    }

    @Override
    public void delete(int id) {

    }

    public void save(Agent agent) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(agent);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }
}
