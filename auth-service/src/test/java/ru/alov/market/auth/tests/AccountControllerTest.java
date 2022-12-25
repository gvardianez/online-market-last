package ru.alov.market.auth.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import ru.alov.market.api.dto.ChangePasswordRequestDto;
import ru.alov.market.api.dto.JwtRequestDto;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@WebFluxTest
public class AccountControllerTest {

  @Autowired private WebTestClient webTestClient;

  @Autowired private MockMvc mvc;

  @Test
  public void changePasswordTest() throws Exception {

    ObjectMapper objectMapper = new ObjectMapper();

    String validJsonRequest = objectMapper.writeValueAsString(new JwtRequestDto("bob", "100"));

    mvc.perform(
            post("/api/v1/authenticate")
                .content(validJsonRequest)
                .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk());

    ChangePasswordRequestDto changePasswordRequestDto =
        new ChangePasswordRequestDto("100", "200#41Adfds", "200#41Adfds");

    mvc.perform(
            put("/api/v1/account/change-password")
                .header("username", "bob")
                .content(objectMapper.writeValueAsString(changePasswordRequestDto))
                .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk());

    mvc.perform(
            post("/api/v1/authenticate")
                .content(validJsonRequest)
                .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isBadRequest());

    mvc.perform(
            post("/api/v1/authenticate")
                .content(validJsonRequest)
                .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$").isMap())
        .andExpect(jsonPath("$.message", is("Неверный пароль")));

    validJsonRequest = objectMapper.writeValueAsString(new JwtRequestDto("bob", "200#41Adfds"));

    mvc.perform(
            post("/api/v1/authenticate")
                .content(validJsonRequest)
                .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk());
  }
}
