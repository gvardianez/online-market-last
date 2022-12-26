package ru.alov.market.auth.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.alov.market.api.dto.JwtRequestDto;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class AuthControllerTest {

  public static ObjectMapper objectMapper;
  public static String validJsonRequest;
  public static String invalidNameJsonRequest;
  public static String invalidPasswordJsonRequest;
  public static String emptyJsonRequest;

  @Autowired private MockMvc mvc;

  @BeforeAll
  public static void init() throws Exception {
    objectMapper = new ObjectMapper();
    validJsonRequest = objectMapper.writeValueAsString(new JwtRequestDto("bob", "100"));
    invalidNameJsonRequest = objectMapper.writeValueAsString(new JwtRequestDto("bob1212", "100"));
    invalidPasswordJsonRequest = objectMapper.writeValueAsString(new JwtRequestDto("bob", "10056"));
    emptyJsonRequest = objectMapper.writeValueAsString(new JwtRequestDto("   ", "   "));
  }

  @Test
  public void createAuthTokenTest() throws Exception {
    mvc.perform(
            post("/api/v1/authenticate")
                .content(validJsonRequest)
                .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk());

    mvc.perform(
            post("/api/v1/authenticate")
                .content(invalidNameJsonRequest)
                .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$").isMap())
        .andExpect(jsonPath("$.code", is("AUTH_SERVICE_RESOURCE_NOT_FOUND")));

    mvc.perform(
            post("/api/v1/authenticate")
                .content(invalidPasswordJsonRequest)
                .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$").isMap())
        .andExpect(jsonPath("$.code", is("AUTH_SERVICE_BAD_CREDENTIALS")));

    mvc.perform(
            post("/api/v1/authenticate")
                .content(emptyJsonRequest)
                .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$").isMap())
        .andExpect(jsonPath("$.code", is("AUTH_SERVICE_FIELD_VALIDATION")));
  }

  @Test
  public void refreshTokensTest() throws Exception {
    MvcResult mvcResult =
        mvc.perform(
                post("/api/v1/authenticate")
                    .content(validJsonRequest)
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn();

    String refreshToken = mvcResult.getResponse().getContentAsString();

    refreshToken =
        refreshToken.substring(refreshToken.lastIndexOf(":") + 2, refreshToken.length() - 2);

    mvc.perform(
            post("/api/v1/authenticate/refresh-tokens")
                .content(objectMapper.writeValueAsString(refreshToken))
                .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk());

    mvc.perform(
            post("/api/v1/authenticate/refresh-tokens")
                .content(objectMapper.writeValueAsString("sadihfsidufhusdi"))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is5xxServerError());
  }
}
