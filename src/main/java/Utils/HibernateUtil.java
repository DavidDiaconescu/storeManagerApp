package Utils;

import Domain.Agent;
import Domain.Comanda;
import Domain.Produs;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            Configuration configuration = new Configuration().configure();

            // Setează calea absolută către baza de date
            configuration.setProperty("hibernate.connection.url",
                    "jdbc:sqlite:/Users/daviddiaconescu/Documents/facultate/AgentVanzari/vanzari.db");

            // Forțează update, ca să nu recreeze schema
            configuration.setProperty("hibernate.hbm2ddl.auto", "update");

            // Adaugă explicit clasele entităților
            configuration.addAnnotatedClass(Agent.class);
            configuration.addAnnotatedClass(Produs.class);
            configuration.addAnnotatedClass(Comanda.class);

            return configuration.buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed: " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        getSessionFactory().close();
    }
}
