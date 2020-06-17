package ru.otus.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.hibernate.model.Person;
import ru.otus.hibernate.model.Phone;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * @author sergey
 * created on 08.10.18.
 */

public class HiberDemo {
    private static final Logger logger = LoggerFactory.getLogger(HiberDemo.class);

    private static final String URL = "jdbc:h2:mem:testDB;DB_CLOSE_DELAY=-1";
    private final SessionFactory sessionFactory;

    public static void main(String[] args) {
        HiberDemo demo = new HiberDemo();

//        demo.entityExample();
//        demo.leakageExample();
//        demo.fetchExample();
//        demo.JPQLexample();
//        demo.deleteFrom();
//        demo.nativeExample();
    }

    private HiberDemo() {
        Configuration configuration = new Configuration()
                .setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect")
                .setProperty("hibernate.connection.driver_class", "org.h2.Driver")
                .setProperty("hibernate.connection.url", URL)
                .setProperty("hibernate.show_sql", "true")
                .setProperty("hibernate.hbm2ddl.auto", "create")
                .setProperty("hibernate.generate_statistics", "true");

        StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties()).build();

        Metadata metadata = new MetadataSources(serviceRegistry)
                .addAnnotatedClass(Person.class)
                .addAnnotatedClass(Phone.class)
                .getMetadataBuilder()
                .build();

        sessionFactory = metadata.getSessionFactoryBuilder().build();
    }

    private void entityExample() {
        try (Session session = sessionFactory.openSession()) {
            Person person = new Person();
            person.setName("Ivan");
            person.setNickName("Durak");
            person.setAddress("derv str");
            session.persist(person);
            logger.info("persisted person:{}", person);

            Person selected = session.load(Person.class, person.getId());
            logger.info("selected: {}", selected);
            logger.info(">>> updating >>>");

            Transaction transaction = session.getTransaction();
            transaction.begin();
            person.setAddress("moved street");
            transaction.commit();

            Person updated = session.load(Person.class, person.getId());
            logger.info("updated: {}", updated);

            session.detach(updated);

            logger.info(">>> updating detached>>>");

            Transaction transactionDetached = session.getTransaction();
            transactionDetached.begin();
            updated.setAddress("moved street NOT CHANGED");
            transactionDetached.commit();

            Person notUpdated = session.load(Person.class, person.getId());
            logger.info("notUpdated: {}", notUpdated);
        }
    }

    private void leakageExample() {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.getTransaction();
            transaction.begin();

            Person person = new Person();
            person.setName("Ivan");
            person.setNickName("Durak");
            person.setAddress("derv str");
            session.persist(person);
            logger.info("person:{}", person);

            transaction.commit();

            //session.detach(person);
            deepInIn(person);

            Person selected = session.load(Person.class, person.getId());
            logger.info("selected: {}", selected);
        }
    }

    //Далекая часть программы
    private void deepInIn(Person person) {
        Person jon = person;
        jon.setName("jon");
        logger.info("jon: {}", jon);
    }

    private void fetchExample() {
        long personId;
        try (Session session = sessionFactory.openSession()) {
            personId = createPerson(session);
        }
        Person selectedPerson;
        try (Session session = sessionFactory.openSession()) {
            Phone selectedPhone = session.load(Phone.class, 3L);
            Phone selectedPhone2 = session.load(Phone.class, 3L);
            logger.info(">>>>>>>>>>>>>>>>>>>>>>>   selectedPhone: {}", selectedPhone);

            selectedPerson = session.load(Person.class, personId);
            //} // сессия закрылась раньше, чеем мы воспользовались объектом.

            logger.info(">>>>>>>>>>>>>>>>>>>>>>>  selected person: {}", selectedPerson.getName());
            logger.info("phones:{}", selectedPerson.getPhones());
        }
    }


    private long createPerson(Session session) {
        Transaction transaction = session.getTransaction();
        transaction.begin();

        Person person = new Person();
        person.setName("Ivan");
        person.setNickName("Durak");
        person.setAddress("derv str");

        List<Phone> listPhone = new ArrayList<>();
        for (int idx = 0; idx < 5; idx++) {
            listPhone.add(new Phone("+" + idx, person));
        }
        person.setPhones(listPhone);
        logger.info(">>>>>>>>>>>>>>>>>>>>>>>   persist...");
        session.save(person);
        logger.info(">>>>>>>>>>>>>>>>>>>>>>>   commit...");

        // должны выполниться 1 insert для person и 5 для phone, update быть не должно
        transaction.commit();
        return person.getId();
    }

    private void JPQLexample() {
        try (Session session = sessionFactory.openSession()) {
            createPerson(session);
        }

        EntityManager entityManager = sessionFactory.createEntityManager();

        logger.info("select phone list:");

        List<Phone> selectedPhones = entityManager.createQuery(
                "select p from Phone p where p.id > :paramId", Phone.class)
                .setParameter("paramId", 2L)
                .getResultList();

        logger.info("selectedPhones:{}", selectedPhones);


        Person person = entityManager
                .createNamedQuery("get_person_by_id", Person.class)
                .setParameter("id", 1L)
                .getSingleResult();

        logger.info("selected person:{}", person.getNickName());


        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Person> criteria = builder.createQuery(Person.class);
        Root<Person> root = criteria.from(Person.class);
        criteria.select(root);
        criteria.where(builder.equal(root.get("id"), 1L));

        Person personCriteria = entityManager.createQuery(criteria).getSingleResult();
        logger.info("selected personCriteria:{}", personCriteria.getNickName());
        logger.info("selected personCriteria, Phones:{}", personCriteria.getPhones());
    }

    //https://www.baeldung.com/delete-with-hibernate
    //Deletion Using a JPQL Statement
    private void deleteFrom() {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.getTransaction();
            transaction.begin();
            Person person = new Person();
            person.setName("Ivan");
            person.setNickName("Durak");
            person.setAddress("derv str");

            session.save(person);
            transaction.commit();
        }

        Long personId = 1L;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            Person loadedPerson = session.get(Person.class, personId); //загружаем в конекст, тут это важно
            logger.info("loadedPerson:{}", loadedPerson);

            Query query = session.createQuery("delete from Person u where u.id = ?1");
            query.setParameter(1, personId);
            query.executeUpdate();

            Person deletedPerson = session.get(Person.class, personId);
            logger.info("deletedPerson:{}", deletedPerson);

            session.getTransaction().commit();

            Person reLoadedPerson = session.get(Person.class, personId);
            logger.info("reLoadedPerson:{}", reLoadedPerson);
        }
    }

    private void nativeExample() {
        try (Session session = sessionFactory.openSession()) {
            createPerson(session);
        }

        try (Session session = sessionFactory.openSession()) {
            String name = session.doReturningWork(connection -> {
                try (PreparedStatement ps = connection.prepareStatement("select name from tPerson where id = ?")) {
                    ps.setLong(1, 1L);
                    try (ResultSet rs = ps.executeQuery()) {
                        rs.next();
                        return rs.getString("name");
                    }
                }
            });
            logger.info("sqL name: {}", name);
        }
    }
}

