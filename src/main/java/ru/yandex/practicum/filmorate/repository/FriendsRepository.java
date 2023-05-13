package ru.yandex.practicum.filmorate.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
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
      String sqlQuery = "SELECT friend_id FROM friends WHERE target_user_id = ? AND is_friends = true";

      return jdbcTemplate.queryForList(sqlQuery, Long.class, userId);
    } catch (EmptyResultDataAccessException e) {
      throw new EntityNotFoundException(String.format("Пользователя с id = %s не найден", userId));
    }
  }

  public Boolean findFriendsStatus(Long userId, Long friendId) {
    try {
      String sqlQuery = "SELECT is_friends FROM friends WHERE target_user_id = ? AND friend_id = ?";

      return jdbcTemplate.queryForObject(sqlQuery, Boolean.class, userId, friendId);
    } catch (EmptyResultDataAccessException e) {
      throw new EntityNotFoundException(String.format("Заявка в друзья между пользователем %s и %s отсутствует", userId, friendId));
    }
  }

  public String addFriend(Long userId, Long friendId) {
    return "";

//    try {
//      MapSqlParameterSource parameters = new MapSqlParameterSource("userId", userId);
//      parameters.addValue("friendId", friendId);
//
//      String sqlQuery = "INSERT INTO friends (target_user_id, friend_id, is_friends) VALUES (?, ?, ?)";;
//
//      namedParameterJdbcTemplate.execute(sqlQuery, parameters, x -> x);
//
//      return jdbcTemplate.queryForList(sqlQuery, Long.class, userId);
//    } catch (EmptyResultDataAccessException e) {
//      throw new EntityNotFoundException(String.format("Пользователя с id = %s не найден", userId));
//    }
  }

//  public User findCommonFriends(Long userId, Long friendId) {
//    try {
//      String sqlQuery = "SELECT * FROM "friends" WHERE target_user_id = ? AND friend_id = ?";
//
//      return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToUser, userId, friendId);
//    } catch (EmptyResultDataAccessException e) {
//      throw new EntityNotFoundException(String.format("Пользователя с id = %s не найден", userId));
//    }
//  }
//
//  public User findAllFriends(Long userId) {
//    try {
//      String sqlQuery = "SELECT * FROM "friends" WHERE target_user_id = ? AND friend_id = ?";
//
//      return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToUser, userId, friendId);
//    } catch (EmptyResultDataAccessException e) {
//      throw new EntityNotFoundException(String.format("Пользователя с id = %s не найден", userId));
//    }
//  }
}

