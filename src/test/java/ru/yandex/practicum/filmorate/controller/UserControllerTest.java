package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers  = UserController.class)
class UserControllerTest {
  @Autowired
  private MockMvc mockMvc;
  private final String defaultUser = "{\n" +
    "  \"login\": \"dolore\",\n" +
    "  \"name\": \"Nick Name\",\n" +
    "  \"id\": 1,\n" +
    "  \"email\": \"mail@mail.ru\",\n" +
    "  \"birthday\": \"1946-08-20\"\n" +
    "}";

  @Test
  public void shouldSuccessGetUsers() throws Exception {
    createUser();

    this.mockMvc
      .perform(
        get("/users")
      )
      .andExpect(status().isOk())
      .andReturn();
  }

  @Test
  public void shouldSuccessUpdateUser() throws Exception {
    createUser();

    String updateUser = "{\n" +
      "  \"login\": \"doloreUpdate\",\n" +
      "  \"name\": \"est adipisicing\",\n" +
      "  \"id\": 1,\n" +
      "  \"email\": \"mail@yandex.ru\",\n" +
      "  \"birthday\": \"1976-09-20\"\n" +
      "}";

    this.mockMvc
      .perform(
        put("/users")
          .content(updateUser)
          .contentType("application/json")
      )
      .andExpect(status().isOk())
      .andReturn();
  }

  @Test
  public void shouldFailUpdateUserWithoutHavingId() throws Exception {
    String updateUser = "{\n" +
      "  \"login\": \"doloreUpdate\",\n" +
      "  \"name\": \"est adipisicing\",\n" +
      "  \"id\": 100,\n" +
      "  \"email\": \"mail@yandex.ru\",\n" +
      "  \"birthday\": \"1976-09-20\"\n" +
      "}";

    this.mockMvc
      .perform(
        put("/users")
          .content(updateUser)
          .contentType("application/json")
      )
      .andExpect(status().is(500))
      .andReturn();
  }

  @Test
  public void shouldSuccessUpdateUserWithoutName() throws Exception {
    createUser();

    String updateUser = "{\n" +
      "  \"login\": \"doloreUpdate\",\n" +
      "  \"id\": 1,\n" +
      "  \"email\": \"mail@yandex.ru\",\n" +
      "  \"birthday\": \"1976-09-20\"\n" +
      "}";

    this.mockMvc
      .perform(
        put("/users")
          .content(updateUser)
          .contentType("application/json")
      )
      .andExpect(status().isOk())
      .andReturn();
  }

  @Test
  public void shouldFailUpdateUser() throws Exception {
    String updateUser = "{\n" +
      "  \"login\": \"dolore\",\n" +
      "  \"name\": \"Nick Name\",\n" +
      "  \"email\": \"mail@mail.ru\",\n" +
      "  \"birthday\": \"1946-08-20\"\n" +
      "}";

    this.mockMvc
      .perform(
        put("/users")
          .content(updateUser)
          .contentType("application/json")
      )
      .andExpect(status().isBadRequest())
      .andReturn();
  }

  @Test
  public void shouldSuccessCreateUser() throws Exception {
    this.mockMvc.perform(
        post("/users")
          .content(defaultUser)
          .contentType("application/json")
      )
      .andExpect(status().isOk())
      .andReturn();
  }

  @Test
  public void shouldSuccessCreateUserWithoutName() throws Exception {
    String userWithoutName = "{\n" +
      "  \"login\": \"common\",\n" +
      "  \"id\": 1,\n" +
      "  \"email\": \"friend@common.ru\",\n" +
      "  \"birthday\": \"2000-08-20\"\n" +
      "}";

    this.mockMvc.perform(
        post("/users")
          .content(userWithoutName)
          .contentType("application/json")
      )
      .andExpect(status().isOk())
      .andReturn();
  }

  @Test
  public void shouldFailLoginCreateUser() throws Exception {
    String failUser = "{\n" +
      "\"login\": \"dolore ullamco\",\n" +
      "\"email\": \"yandex@mail.ru\",\n" +
      "\"birthday\": \"2446-08-20\"\n" +
      "}";

    this.mockMvc.perform(
        post("/users")
          .content(failUser)
          .contentType("application/json")
      )
      .andExpect(status().isBadRequest())
      .andReturn();
  }

  @Test
  public void shouldFailEmptyLoginCreateUser() throws Exception {
    String failUser = "{\n" +
      "\"login\": \"\",\n" +
      "\"email\": \"yandex@mail.ru\",\n" +
      "\"birthday\": \"2446-08-20\"\n" +
      "}";

    this.mockMvc.perform(
        post("/users")
          .content(failUser)
          .contentType("application/json")
      )
      .andExpect(status().isBadRequest())
      .andReturn();
  }

  @Test
  public void shouldFailEmailCreateUser() throws Exception {
    String failUser = "{" +
      "\"login\": \"doloreUllamco\",\n" +
      "\"name\": \"\",\n" +
      "\"email\": \"mail.ru\",\n" +
      "\"birthday\": \"1980-08-20\"" +
      "}";

    this.mockMvc.perform(
        post("/users")
          .content(failUser)
          .contentType("application/json")
      )
      .andExpect(status().isBadRequest())
      .andReturn();
  }

  @Test
  public void shouldFailBirthdateCreateUser() throws Exception {
    String failUser = "{" +
      "\"login\": \"doloreUllamco\",\n" +
      "\"name\": \"\",\n" +
      "\"email\": \"test@mail.ru\",\n" +
      "\"birthday\": \"2446-08-20\"" +
      "}";

    this.mockMvc.perform(
        post("/users")
          .content(failUser)
          .contentType("application/json")
      )
      .andExpect(status().isBadRequest())
      .andReturn();
  }

  private void createUser() throws Exception {
    this.mockMvc.perform(
      post("/users")
        .content(defaultUser)
        .contentType("application/json")
    );
  }
}