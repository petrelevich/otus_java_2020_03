package ru.otus.demo;

import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;
import org.hibernate.proxy.HibernateProxy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import ru.otus.AbstractHibernateTest;
import ru.otus.core.model.User;

import javax.persistence.Query;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Демо работы с hibernate (без абстракций) должно ")
public class WithoutAbstractionsTest extends AbstractHibernateTest {

    @BeforeEach
    @Override
    public void setUp() {
        super.setUp();
    }

    @DisplayName(" корректно сохранять и загружать пользователя выполяняя заданное кол-во запросов в нужное время")
    @ParameterizedTest(name = "пользователь отключен от контекста (detached) перед загрузкой: {0}")
    @ValueSource(booleans = {false, true})
    void shouldCorrectSaveAndLoadUserWithExpectedQueriesCount(boolean userDetachedBeforeGet) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            User savedUser = buildDefaultUser();

            session.save(savedUser);
            // Не должно быть выполнено ни одной вставки в БД не смотря на то что метод save был вызван
            assertThat(getUserStatistics().getInsertCount()).isEqualTo(0);

            session.getTransaction().commit();
            // Реальная вставка произошла в момент коммита транзакции (А что если GenerationType.IDENTITY?)
            assertThat(getUserStatistics().getInsertCount()).isEqualTo(1);

            // Мы не ожидаем ни одного обращения к БД для загрузки пользователя если он не отсоединен от контекста
            long expectedLoadCount = 0;
            if (userDetachedBeforeGet) {
                session.detach(savedUser);
                // И ожидаем обращения к БД для загрузки если пользователя в контексте нет
                expectedLoadCount = 1;
            }

            User loadedUser = session.get(User.class, savedUser.getId());

            // Проверка, что количество загрузок из БД соответствует ожиданиям
            assertThat(getUserStatistics().getLoadCount()).isEqualTo(expectedLoadCount);
            // И что мы достали того же пользователя, что сохраняли
            assertThat(loadedUser).isNotNull().isEqualToComparingFieldByField(savedUser);
        }
    }

    @DisplayName(" корректно сохранять пользователя в одной сессии и загружать в другой выполнив один запрос к БД для загрузки")
    @Test
    void shouldCorrectSaveAndLoadUserWithExpectedQueriesCountInTwoDifferentSessions() {
        User savedUser = buildDefaultUser();
        // Сохранили пользователя в отдельной сессии
        saveUser(savedUser);

        try (Session session = sessionFactory.openSession()) {
            // Загрузка пользователя в отдельной сессии
            User loadedUser = session.get(User.class, savedUser.getId());

            // Проверка, что для получения пользователя было сделано обращение к БД
            // (т.е. пользователь не сохранился в контексте при смене сессии)
            assertThat(getUserStatistics().getLoadCount()).isEqualTo(1);

            // И что мы достали того же пользователя, что сохраняли
            assertThat(loadedUser).isNotNull().isEqualToComparingFieldByField(savedUser);
        }
    }

    @DisplayName(" показывать в каких случаях загруженный с помощью load объект является прокси для сценария: ")
    @ParameterizedTest(name = "{2}")
    @CsvSource({"true, false, пользователь не существует ",
            "false, false, пользователь существует и он persistent",
            "false, true, пользователь существует и он detached"})
    void shouldLoadProxyObjectWithLoadMethod(ArgumentsAccessor arguments) {
        boolean loadedNotExistingUser = arguments.getBoolean(0);
        boolean userDetachedBeforeLoad = arguments.getBoolean(1);

        User savedUser = buildDefaultUser();
        try (Session session = sessionFactory.openSession()) {
            // Сохранили пользователя в рамках текущей сессии
            saveUser(session, savedUser);

            if (userDetachedBeforeLoad) {
                // Отсоединили пользователя от контекста если это нужно по текщему сценарию
                session.detach(savedUser);
            }

            // Если по сценарию нужно загружать пользователя не существующего в БД, выставляем id=-1
            long id = loadedNotExistingUser ? -1L : savedUser.getId();
            User loadedUser = session.load(User.class, id);

            // Метод load должен вернуть пользователя не зависимо от того, существует ли он в БД или нет
            assertThat(loadedUser).isNotNull();

            if (loadedNotExistingUser || userDetachedBeforeLoad) {
                // Если загружен не существующий в БД пользователь или он был отсоединен от конетекста, то загруженный объект д.б. Proxy
                assertThat(loadedUser).isInstanceOf(HibernateProxy.class);

                // Если загружен не существующий в БД пользователь обращение к полю должно привести к ObjectNotFoundException
                if (loadedNotExistingUser) {
                    assertThatThrownBy(loadedUser::getName).isInstanceOf(ObjectNotFoundException.class);
                } else {
                    assertThatCode(loadedUser::getName).doesNotThrowAnyException();
                }
            } else {
                assertThat(loadedUser).isNotInstanceOf(HibernateProxy.class).isInstanceOf(User.class);
            }
        }
    }

    @DisplayName(" показывать что если загрузить, с помощью load, не существующий объект, то с ним можно нормально работать после того, как он был добавлен в БД")
    @Test
    void shouldLoadNotExistingObjectAndWorkWithHimAfterItSaved() {
        User savedUser = buildDefaultUser();
        User loadedUser;
        try (Session session = sessionFactory.openSession()) {
            // На момент загрузки такого юзера в БД нет
            loadedUser = session.load(User.class, 1L);
            // Проверяем, что вернулся прокси
            assertThat(loadedUser).isInstanceOf(HibernateProxy.class);
            // И не произошло обращения к БД
            assertThat(getUserStatistics().getLoadCount()).isEqualTo(0);

            // Сохраняем пользователя в другой сессии
            saveUser(savedUser);
            // Теперь объект есть в БД. Проверяем что с объектом можно нормально работать
            assertThat(loadedUser.getName()).isEqualTo(TEST_USER_NAME);
            // И в момент обращения к свойству произошла загрузка из БД
            assertThat(getUserStatistics().getLoadCount()).isEqualTo(1);
        }
    }

    @DisplayName(" показывать, что загруженный с помощью get объект не является прокси")
    @Test
    void shouldLoadNotAProxyObjectWithGetMethod() {
        try (Session session = sessionFactory.openSession()) {
            User savedUser = buildDefaultUser();
            saveUser(session, savedUser);

            // Заргузка с помощью метода get не существующего в БД пользователя должна приводить к возврату null
            assertThat(session.get(User.class, -1L)).isNull();

            // Метод get для существующего в БД пользователя должен вернуть объект пользователя не являющегося прокси
            assertThat(session.get(User.class, savedUser.getId())).isNotNull()
                    .isEqualToComparingFieldByField(savedUser)
                    .isNotInstanceOf(HibernateProxy.class);
        }
    }

    @DisplayName(" показывать, что несколько обновлений в одной транзакции станут одним запросом к БД")
    @Test
    void shouldExecuteOneUpdateQueryForMultipleUpdateInOneTransaction() {
        try (Session session = sessionFactory.openSession()) {
            User savedUser = buildDefaultUser();
            // Сохранили пользователя в рамках текущей сессии
            saveUser(session, savedUser);

            session.beginTransaction();

            // Изменили имя пользователя
            savedUser.setName(TEST_USER_NEW_NAME);
            session.update(savedUser);

            // Еще раз изменили имя пользователя
            savedUser.setName(TEST_USER_NAME);
            session.update(savedUser);

            // И еще Еще раз изменили имя пользователя
            savedUser.setName(TEST_USER_NEW_NAME2);
            session.update(savedUser);

            session.getTransaction().commit();

            // Проверка, что в итоге был только один запрос к БД на обновление
            assertThat(getUserStatistics().getUpdateCount()).isEqualTo(1);
        }
    }

    @DisplayName(" показывать, что вызов метода save на detached объекте приводит к генерации нового id")
    @Test
    void shouldGenerateNewIdWhenExecuteSaveMethodOnSameEntity() {
        try (Session session = sessionFactory.openSession()) {
            User savedUser = buildDefaultUser();
            // Сохранили пользователя в рамках текущей сессии
            saveUser(session, savedUser);
            // Запомнили его id
            long id = savedUser.getId();

            // Отсоединили пользователя от контекста
            session.detach(savedUser);

            // Еще раз сохранили
            saveUser(session, savedUser);

            // Проверка, что второй раз сохраненный пользователь имеет новый id
            assertThat(id).isNotEqualTo(savedUser.getId());
        }
    }

    @DisplayName(" показывать, что вызов метода saveOrUpdate на detached объекте не приводит к генерации нового id")
    @Test
    void shouldGenerateNewIdWhenExecuteSaveOrUpdateMethodOnSameEntity() {
        try (Session session = sessionFactory.openSession()) {
            User savedUser = buildDefaultUser();
            // Сохранили пользователя в рамках текущей сессии
            saveUser(session, savedUser);
            // Запомнили его id
            long id = savedUser.getId();

            // Отсоединили пользователя от контекста
            session.detach(savedUser);

            // Еще раз сохранили с помощью saveOrUpdate
            session.beginTransaction();
            savedUser.setName(TEST_USER_NEW_NAME);
            session.saveOrUpdate(savedUser);
            session.getTransaction().commit();

            User loadedUser = loadUser(id);

            // Проверка, что второй раз сохраненный пользователь имеет тот же id
            assertThat(loadedUser).isEqualToComparingFieldByField(savedUser);
        }
    }

    @DisplayName(" показывать, что вызов метода update на transient объекте приводит к исключению")
    @Test
    void shouldThrowExceptionWhenCommitTransactionAfterUpdateTransientEntity() {
        try (Session session = sessionFactory.openSession()) {
            // Создали нового пользователя, но не сохранили его
            User savedUser = buildDefaultUser();

            // Вызвали для данного пользователя update
            session.beginTransaction();
            session.update(savedUser);
            // Проверка, что id у него не появился
            assertThat(savedUser.getId()).isEqualTo(0);
            // Проверка, что коммит транзакции приведет к исключению
            assertThatThrownBy(session.getTransaction()::commit).isInstanceOf(Exception.class);
        }
    }

    @DisplayName(" показывать, что изменение persistent объекта внутри транзакции приводит к его изменению в БД")
    @Test
    void shouldUpdatePersistentEntityInDBWhenChangedFieldsInTransaction() {
        User savedUser = buildDefaultUser();
        try (Session session = sessionFactory.openSession()) {
            // Открыли транзакцию
            session.beginTransaction();

            // Сохранили пользователя
            session.save(savedUser);

            // Убедились, что его имя соответствует ожидаемому
            assertThat(savedUser.getName()).isEqualTo(TEST_USER_NAME);
            // Сменили имя на новое
            savedUser.setName(TEST_USER_NEW_NAME);

            // Завершили транзакцию
            session.getTransaction().commit();
            // И сессию
        }
        // Заргрузили пользователя в новой сессии
        User loadedUser = loadUser(savedUser.getId());
        // Проверка, что имя загруженного пользвателя соответствует тому, что дали после сохранения
        assertThat(loadedUser.getName()).isEqualTo(TEST_USER_NEW_NAME);
    }

    @DisplayName(" показывать, что удаленный через HQL persistent объект остется в сессии, но удаляется в БД")
    @Test
    void shouldNotDetachPersistentEntityWhenRemoveWithHQLQuery() {
        User savedUser = buildDefaultUser();
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            // Сохранили пользователя в рамках текущей сессии. Теперь у него state = persistent
            session.save(savedUser);

            // Удалили пользователя с помощью запроса
            Query query = session.createQuery("delete from User u where u.id = ?1");
            query.setParameter(1, savedUser.getId());
            query.executeUpdate();

            // Заргрузили пользователя
            User loadedUser = session.get(User.class, savedUser.getId());
            // Проверка, что загруженный пользователь не null и равен сохраненному ранее
            assertThat(loadedUser).isNotNull().isEqualToComparingFieldByField(savedUser);

            // Отсоединили пользователя от контекста
            session.detach(savedUser);

            // Заргрузили пользователя еще раз
            loadedUser = session.get(User.class, savedUser.getId());

            // Проверка, что загруженный пользователь null
            assertThat(loadedUser).isNull();

            session.getTransaction().commit();
        }
    }
}
