package ru.yandex.practicum.filmorate.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Rating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@Slf4j
public class MpaRepository {
  private final JdbcTemplate jdbcTemplate;

  @Autowired
  public MpaRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public List<Rating> findAll() {
    String sqlQuery = "SELECT * FROM ratings";
    log.info("Получение всех рейтингов");

    return jdbcTemplate.query(sqlQuery, this::mapRowToRating);
  }

  public Rating findById(int mpaId) {
    try {
      String sqlQuery = "SELECT * FROM ratings WHERE id = ?";
      log.info("Получение рейтинга с id = {}", mpaId);

      return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToRating, mpaId);
    } catch (EmptyResultDataAccessException e) {
      throw new EntityNotFoundException(String.format("Пользователя с id = %s не найден", mpaId));
    }
  }

  private Rating mapRowToRating(ResultSet resultSet, int rowNum) throws SQLException {
    return Rating
      .builder()
      .id(resultSet.getInt("id"))
      .name(resultSet.getString("name"))
      .build();
  }
}
