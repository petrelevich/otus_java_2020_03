package ru.otus.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.otus.WebAppInitializer;
import ru.otus.WebConfig;
import ru.otus.domain.User;
import ru.otus.services.UsersService;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = WebConfig.class)
@WebAppConfiguration
@ExtendWith(MockitoExtension.class)
class UserRestControllerWithContextTest {

    private MockMvc mvc;

    @Mock
    private UsersService usersService;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void getUserById() throws Exception {
        Gson gson = new GsonBuilder().create();
        User expectedUser = new User(1, "Крис Гир");
        mvc.perform(get("/api/user/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().string(gson.toJson(expectedUser)));
    }
}