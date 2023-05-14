package ru.yandex.practicum.filmorate.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Rating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

@Slf4j
@Repository
public class LikesRepository {
  private final JdbcTemplate jdbcTemplate;

  public LikesRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public void create(Long filmId, Long userId) {
    String sqlQuery = "INSERT INTO likes (film_id, user_id) VALUES(?, ?)";

    try {
      log.info("Лайк фильму с id = {} от пользователя с id = {}", filmId, userId);

      jdbcTemplate.update(sqlQuery, filmId, userId);
    } catch (DuplicateKeyException e) {
      throw new RuntimeException("Данный пользователь уже лайкнул этот фильм");
    }
  }

  public boolean deleteLike(Long filmId, Long userId) {
    String sqlQuery = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";
    log.info("Удаление лайка фильму с id = {} пользователем с id = {}", filmId, userId);

    return jdbcTemplate.update(sqlQuery, filmId, userId) > 0;
  }

  public List<Film> getPopular(int count) {
    StringJoiner sqlQuery = new StringJoiner(" ");

    sqlQuery.add("SELECT f.id, f.name, f.description, f.release_date, f.duration, f.rating_id, COUNT(l.film_id) AS count");
    sqlQuery.add("FROM films AS f");
    sqlQuery.add("LEFT OUTER JOIN likes AS l ON f.id = l.film_id");
    sqlQuery.add("GROUP BY f.id");
    sqlQuery.add("ORDER BY count DESC");
    sqlQuery.add("LIMIT ?");

    log.info("Получение популярных фильмов в количестве {} фильмов", count);

    return jdbcTemplate.query(sqlQuery.toString(), this::mapRowToFilm, count);
  }

  private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
    return Film.builder()
      .id(resultSet.getLong("id"))
      .name(resultSet.getString("name"))
      .description(resultSet.getString("description"))
      .releaseDate(LocalDate.parse(resultSet.getString("release_date")))
      .duration(resultSet.getInt("duration"))
      .mpa(
        Rating
          .builder()
          .id(resultSet.getInt("rating_id"))
          .build()
      )
      .genres(new ArrayList<>())
      .build();
  }
}
