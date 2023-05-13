package ru.yandex.practicum.filmorate.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
@Slf4j
public class GenreRepository {
  private final JdbcTemplate jdbcTemplate;

  @Autowired
  public GenreRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public List<Genre> findAll() {
    String sqlQuery = "SELECT * FROM genres ORDER BY id";
    log.info("Получение всех рейтингов");

    return jdbcTemplate.query(sqlQuery, this::mapRowToGenre);
  }

  public Genre findById(int genreId) {
    try {
      String sqlQuery = "SELECT * FROM genres WHERE id = ?";
      log.info("Получение жанра с id = {}", genreId);

      return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToGenre, genreId);
    } catch (EmptyResultDataAccessException e) {
      throw new EntityNotFoundException(String.format("Жанр с id = %s не найден", genreId));
    }
  }

  public void addFilm(Long filmId, List<Genre> genres) {
    StringJoiner sqlQuery = new StringJoiner(" ");
    sqlQuery.add(String.format("DELETE FROM film_genres WHERE film_id = %s;", filmId));
    sqlQuery.add("MERGE INTO film_genres (film_id, genre_id) KEY (film_id, genre_id)");
    sqlQuery.add("VALUES");
    genres.forEach(genre -> sqlQuery.add(String.format("(%s, %s),", filmId, genre.getId())));

    log.info(sqlQuery.toString());
    jdbcTemplate.update(
      sqlQuery.toString().substring(0, sqlQuery.length() - 1)
    );
  }

  public void deleteGenres(Long id) {
    String sqlQuery = "DELETE FROM film_genres WHERE film_id = ?";

    jdbcTemplate.update(sqlQuery, id);
  }

  public List<Genre> findAllByFilmId(Long id) {
    String sqlQuery = "SELECT g.id, g.name " +
      "FROM film_genres AS fg " +
      "JOIN genres AS g ON g.id = fg.genre_id " +
      "WHERE fg.film_id = ?";

    return jdbcTemplate.query(sqlQuery, this::mapRowToGenre, id);
  }

  public HashMap<Long, ArrayList<Genre>> getAllGenresAndFilms () {
    String sqlQuery = "SELECT g.id, g.name, fg.film_id " +
      "FROM film_genres AS fg " +
      "JOIN genres AS g ON g.id = fg.genre_id";

    return this.mapToGenresFilms(jdbcTemplate.queryForList(sqlQuery));
  }

  private Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
    return Genre
      .builder()
      .id(resultSet.getInt("id"))
      .name(resultSet.getString("name"))
      .build();
  }

  private HashMap<Long, ArrayList<Genre>> mapToGenresFilms(List<Map<String, Object>> allGenres) {
    HashMap<Long, ArrayList<Genre>> genresFilms = new HashMap<>();

    allGenres.forEach(x -> {
      ArrayList<Genre> genres = new ArrayList<>();

      Long filmId = Long.parseLong(x.get("film_id").toString());

      genres.add(
        Genre
          .builder()
          .id(Integer.parseInt(x.get("id").toString()))
          .name(x.get("name").toString())
          .build()
      );

      genresFilms.put(filmId, genres);
    });

    return genresFilms;
  }
}
