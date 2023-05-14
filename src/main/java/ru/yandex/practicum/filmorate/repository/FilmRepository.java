package ru.yandex.practicum.filmorate.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Rating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

@Repository
@Slf4j
public class FilmRepository {
  private final JdbcTemplate jdbcTemplate;
  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  @Autowired
  public FilmRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
    this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
  }

  public Film create(Film film) {
    MapSqlParameterSource parameters = new MapSqlParameterSource("name", film.getName());
    parameters.addValue("description", film.getDescription());
    parameters.addValue("release_date", film.getReleaseDate().toString());
    parameters.addValue("duration", film.getDuration());
    parameters.addValue("mpa", film.getMpa().getId());

    String sqlQuery = "INSERT INTO films (name, description, release_date, duration, rating_id) VALUES (:name, :description, :release_date, :duration, :mpa)";

    KeyHolder keyHolder = new GeneratedKeyHolder();

    namedParameterJdbcTemplate.update(sqlQuery, parameters, keyHolder);

    log.info("Фильм успешно создан - {}", film);

    return findFilmById(Objects.requireNonNull(keyHolder.getKey()).longValue());
  }

  public Film findFilmById(Long filmId) {
    String sqlQuery = "SELECT * FROM films WHERE id = ?";
    log.info("Получение фильма с id = {}", filmId);

    try {
      return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilm, filmId);
    } catch (EmptyResultDataAccessException e) {
      throw new EntityNotFoundException(String.format("Фильм с id = %s не найден", filmId));
    }
  }

  public Collection<Film> findAll() {
    String sqlQuery = "SELECT * FROM films";

    log.info("Получение всех фильмов");

    return jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
  }

  public Film update(Film film) {
    String sqlQuery = "UPDATE films SET name = ?, description = ?, release_date = ?, duration = ?, rating_id = ? WHERE id = ?";

    log.info("Обновление фильма с id = {}", film.getId());

    jdbcTemplate.update(
      sqlQuery,
      film.getName(),
      film.getDescription(),
      film.getReleaseDate().toString(),
      film.getDuration(),
      film.getMpa().getId(),
      film.getId()
    );

    return findFilmById(film.getId());
  }

  public boolean delete(Long filmId) {
    String sqlQuery = "DELETE FROM films WHERE id = ?";
    log.info("Удаление фильма с id = {}", filmId);

    return jdbcTemplate.update(sqlQuery, filmId) > 0;
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
