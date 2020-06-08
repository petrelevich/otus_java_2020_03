package ru.otus.dbservice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.core.dao.UserDao;
import ru.otus.core.model.User;
import ru.otus.core.service.DbServiceException;
import ru.otus.core.service.DbServiceUserImpl;
import ru.otus.core.sessionmanager.SessionManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


@DisplayName("Сервис для работы с пользователями в рамках БД должен ")
@ExtendWith(MockitoExtension.class)
class DbServiceUserImplTest {

    private static final long USER_ID = 1L;

    @Mock
    private SessionManager sessionManager;

    @Mock
    private UserDao userDao;

    private DbServiceUserImpl dbServiceUser;

    private InOrder inOrder;

    @BeforeEach
    void setUp() {
        given(userDao.getSessionManager()).willReturn(sessionManager);
        inOrder = inOrder(userDao, sessionManager);
        dbServiceUser = new DbServiceUserImpl(userDao);
    }

    @Test
    @DisplayName(" корректно сохранять пользователя")
    void shouldCorrectSaveUser() {
        var vasya = new User();
        doAnswer(invocation ->{
            vasya.setId(USER_ID);
            return null;
        }).when(userDao).insertOrUpdate(vasya);

        long id = dbServiceUser.saveUser(vasya);
        assertThat(id).isEqualTo(USER_ID);
    }

    @Test
    @DisplayName(" при сохранении пользователя, открывать и коммитить транзакцию в нужном порядке")
    void shouldCorrectSaveUserAndOpenAndCommitTranInExpectedOrder() {
        dbServiceUser.saveUser(new User());

        inOrder.verify(userDao, times(1)).getSessionManager();
        inOrder.verify(sessionManager, times(1)).beginSession();
        inOrder.verify(sessionManager, times(1)).commitSession();
        inOrder.verify(sessionManager, never()).rollbackSession();
    }

    @Test
    @DisplayName(" при сохранении пользователя, открывать и откатывать транзакцию в нужном порядке")
    void shouldOpenAndRollbackTranWhenExceptionInExpectedOrder() {
        doThrow(IllegalArgumentException.class).when(userDao).insertOrUpdate(any());

        assertThatThrownBy(() -> dbServiceUser.saveUser(null))
                .isInstanceOf(DbServiceException.class)
                .hasCauseInstanceOf(IllegalArgumentException.class);

        inOrder.verify(userDao, times(1)).getSessionManager();
        inOrder.verify(sessionManager, times(1)).beginSession();
        inOrder.verify(sessionManager, times(1)).rollbackSession();
        inOrder.verify(sessionManager, never()).commitSession();
    }

    @Test
    @DisplayName(" корректно загружать пользователя по заданному id")
    void shouldLoadCorrectUserById() {
        User expectedUser = new User(USER_ID, "Вася");
        given(userDao.findById(USER_ID)).willReturn(Optional.of(expectedUser));
        Optional<User> mayBeUser = dbServiceUser.getUser(USER_ID);
        assertThat(mayBeUser).isPresent().get().isEqualToComparingFieldByField(expectedUser);

    }
}
