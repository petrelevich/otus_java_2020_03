package ru.otus.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.AbstractHibernateTest;
import ru.otus.core.dao.UserDao;
import ru.otus.core.model.User;
import ru.otus.core.service.DBServiceUser;
import ru.otus.hibernate.dao.UserDaoHibernate;
import ru.otus.core.service.DbServiceUserImpl;
import ru.otus.hibernate.sessionmanager.SessionManagerHibernate;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Демо работы с hibernate (с абстракциями) должно ")
public class WithAbstractionsTest extends AbstractHibernateTest {

    private DBServiceUser dbServiceUser;

    @BeforeEach
    @Override
    public void setUp() {
        super.setUp();
        SessionManagerHibernate sessionManager = new SessionManagerHibernate(sessionFactory);
        UserDao userDao = new UserDaoHibernate(sessionManager);
        dbServiceUser = new DbServiceUserImpl(userDao);
    }

    @Test
    @DisplayName(" корректно сохранять пользователя")
    void shouldCorrectSaveUser() {
        User savedUser = buildDefaultUser();
        long id = dbServiceUser.saveUser(savedUser);
        User loadedUser = loadUser(id);

        assertThat(loadedUser).isNotNull().isEqualToComparingFieldByField(savedUser);

        System.out.println(savedUser);
        System.out.println(loadedUser);
    }

    @Test
    @DisplayName(" корректно загружать пользователя")
    void shouldLoadCorrectUser() {
        User savedUser = buildDefaultUser();
        saveUser(savedUser);

        Optional<User> mayBeUser = dbServiceUser.getUser(savedUser.getId());

        assertThat(mayBeUser).isPresent().get().isEqualToComparingFieldByField(savedUser);

        System.out.println(savedUser);
        mayBeUser.ifPresent(System.out::println);
    }

    @Test
    @DisplayName(" корректно изменять ранее сохраненного пользователя")
    void shouldCorrectUpdateSavedUser() {
        User savedUser = buildDefaultUser();
        saveUser(savedUser);

        User savedUser2 = new User(savedUser.getId(), TEST_USER_NEW_NAME);
        long id = dbServiceUser.saveUser(savedUser2);
        User loadedUser = loadUser(id);

        assertThat(loadedUser).isNotNull().isEqualToComparingFieldByField(savedUser2);

        System.out.println(savedUser);
        System.out.println(savedUser2);
        System.out.println(loadedUser);
    }

}
