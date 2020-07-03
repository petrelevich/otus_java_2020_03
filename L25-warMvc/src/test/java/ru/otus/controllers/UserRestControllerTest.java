package ru.otus.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.otus.domain.User;
import ru.otus.services.UsersService;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserRestControllerTest {

    private MockMvc mvc;

    @Mock
    private UsersService usersService;

    @BeforeEach
    public void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(new UserRestController(usersService)).build();
    }

    @Test
    void getUserById() throws Exception {
        given(usersService.findById(1L)).willReturn(new User(1, "Vasya"));
        mvc.perform(get("/api/user/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"id\":1,\"name\":\"Vasya\"}"))
                .andReturn();
    }
}