package ru.alov.market.auth.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.alov.market.api.dto.ListDto;
import ru.alov.market.api.dto.RegisterUserDto;
import ru.alov.market.auth.entities.User;
import ru.alov.market.auth.services.UserService;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class RegistrationControllerTest {

  @Autowired private MockMvc mvc;

  @Autowired private UserService userService;

  @Test
  public void createNewUserTest() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();

    ListDto<Long> longListDto = new ListDto<>(List.of(1L, 2L));

    String validJsonRequest =
        objectMapper.writeValueAsString(
            new RegisterUserDto("vasya", "234DF#asdfr3", "234DF#asdfr3", "tasdu@ya.ru"));

    mvc.perform(
            post("/api/v1/registration")
                .content(validJsonRequest)
                .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk());

    Optional<User> userProfileDto = userService.findByUsername("vasya");

    Assertions.assertEquals("vasya", userProfileDto.get().getUsername());
    Assertions.assertEquals("tasdu@ya.ru", userProfileDto.get().getEmail());
  }

  @Test
  public void confirmEmailTest() throws Exception {

    mvc.perform(
            get("/api/v1/registration/confirm-email")
                .queryParam("username", "bob")
                .queryParam("email", "sdasd@asda.ru")
                .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$").isMap())
        .andExpect(
            jsonPath(
                "$.message", is("Email пользователя не совпадает с указанным при регистрации")));

    mvc.perform(
            get("/api/v1/registration/confirm-email")
                .queryParam("username", "bob")
                .queryParam("email", "bob_johnson@gmail.com")
                .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk());
  }
}
