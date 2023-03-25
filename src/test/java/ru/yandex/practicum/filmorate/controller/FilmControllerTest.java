package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = FilmController.class)
class FilmControllerTest {
  @Autowired
  private MockMvc mockMvc;
  private final String defaultFilm = "{\"id\":5,\"name\":\"Film1\",\"description\":\"Description\",\"releaseDate\":\"2023-03-24\",\"duration\":200}";

  @Test
  public void shouldSuccessGetFilms() throws Exception {
    createFilm();

    this.mockMvc
      .perform(
        get("/films")
      )
      .andExpect(status().isOk())
      .andReturn();
  }

  @Test
  public void shouldSuccessUpdateFilm() throws Exception {
    createFilm();

    String updateFilm = "{\"id\":1,\"name\":\"Film1\",\"description\":\"Description\",\"releaseDate\":\"2023-03-24\",\"duration\":200}";

    this.mockMvc
      .perform(
        put("/films")
          .content(updateFilm)
          .contentType("application/json")
      )
      .andExpect(status().isOk())
      .andReturn();
  }

  @Test
  public void shouldFailUpdateFilmWithoutHavingId() throws Exception {
    createFilm();
    String updateFilm = "{\"id\":100,\"name\":\"Film1\",\"description\":\"Description\",\"releaseDate\":\"2023-03-24\",\"duration\":200}";

    this.mockMvc
      .perform(
        put("/films")
          .content(updateFilm)
          .contentType("application/json")
      )
      .andExpect(status().is(500))
      .andReturn();
  }

  @Test
  public void shouldFailUpdateFilm() throws Exception {
    String updateFilm = "{\"name\":\"Film1\",\"description\":\"Description\",\"releaseDate\":\"2023-03-24\",\"duration\":200}";

    this.mockMvc
      .perform(
        put("/films")
          .content(updateFilm)
          .contentType("application/json")
      )
      .andExpect(status().isBadRequest())
      .andReturn();
  }

  @Test
  public void shouldSuccessCreateFilm() throws Exception {
    this.mockMvc.perform(
        post("/films")
          .content(defaultFilm)
          .contentType("application/json")
      )
      .andExpect(status().isOk())
      .andReturn();
  }

  @Test
  public void shouldFailDuration() throws Exception {
    String failDuration = "{\"id\":2,\"name\":\"Film1\",\"description\":\"Description\",\"releaseDate\":\"2023-03-24\",\"duration\":-200}";

    this.mockMvc.perform(
        post("/films")
          .content(failDuration)
          .contentType("application/json")
      )
      .andExpect(status().is(400))
      .andReturn();
  }

  @Test
  public void shouldFailReleaseDate() throws Exception {
    String failReleaseDate = "{\"id\":2,\"name\":\"Film1\",\"description\":\"Description\",\"releaseDate\":\"1880-03-24\",\"duration\":200}";
    this.mockMvc.perform(
        post("/films")
          .content(failReleaseDate)
          .contentType("application/json")
      )
      .andExpect(status().is(400))
      .andReturn();
  }

  @Test
  public void shouldFailDescription() throws Exception {
    String failDescription = "{\"id\":2,\"name\":\"Film1\",\"description\":\"Пятеро друзей ( комик-группа «Шарло»), приезжают в город Бризуль. Здесь они хотят разыскать господина Огюста Куглова, который задолжал им деньги, а именно 20 миллионов. о Куглов, который за время «своего отсутствия», стал кандидатом Коломбани.\",\"releaseDate\":\"2023-03-24\",\"duration\":200}";
    this.mockMvc.perform(
        post("/films")
          .content(failDescription)
          .contentType("application/json")
      )
      .andExpect(status().is(400))
      .andReturn();
  }

  @Test
  public void shouldFailName() throws Exception {
    String failName = "{\"id\":4,\"name\":\"\",\"description\":\"Description\",\"releaseDate\":\"2023-03-24\",\"duration\":200}";
    this.mockMvc.perform(
        post("/films")
          .content(failName)
          .contentType("application/json")
      )
      .andExpect(status().is(400))
      .andReturn();
  }

  private void createFilm() throws Exception {
    this.mockMvc.perform(
      post("/films")
        .content(defaultFilm)
        .contentType("application/json")
    );
  }
}