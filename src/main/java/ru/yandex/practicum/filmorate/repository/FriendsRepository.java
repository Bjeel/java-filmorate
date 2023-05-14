package ru.yandex.practicum.filmorate.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;

import java.util.List;

@Slf4j
@Repository
public class FriendsRepository {
  private final JdbcTemplate jdbcTemplate;
  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  @Autowired
  public FriendsRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
    this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
  }

  public List<Long> findFriendsById(Long userId) {
    try {
      String sqlQuery = "SELECT friend_second FROM friends WHERE friend_first = ? AND relative_type != '0'";

      return jdbcTemplate.queryForList(sqlQuery, Long.class, userId);
    } catch (EmptyResultDataAccessException e) {
      throw new EntityNotFoundException(String.format("Пользователя с id = %s не найден", userId));
    }
  }

  public String addFriend(Long userId, Long friendId) {
    String sqlQuery = "INSERT INTO friends VALUES (?, ?, '1')";

    return jdbcTemplate.update(sqlQuery, userId, friendId) > 0 ? "Запрос на дружбу отправлен" : "";
  }

  public boolean delete(Long userId, Long friendId) {
      MapSqlParameterSource parameters = new MapSqlParameterSource("userId", userId);
      parameters.addValue("friendId", friendId);

    String sqlQuery =
      "UPDATE friends SET relative_type = '0' WHERE friend_first = :userId AND friend_second = :friendId;" +
      "UPDATE friends SET relative_type = '0' WHERE friend_first = :friendId AND friend_second = :userId;";

    return namedParameterJdbcTemplate.update(sqlQuery, parameters) > 0;
  }

  public List<Long> findCommon(Long userId, Long friendId) {
    String sqlQuery =
      "SELECT friend_second FROM friends " +
      "WHERE friend_first = ? AND friend_second  IN (" +
        "SELECT friend_second FROM friends WHERE friend_first = ?"+
      ")";

    return jdbcTemplate.queryForList(sqlQuery, Long.class, userId, friendId);
  }
}

