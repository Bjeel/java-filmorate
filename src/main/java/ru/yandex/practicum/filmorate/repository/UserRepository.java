package ru.yandex.practicum.filmorate.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@Repository
public class UserRepository {
  private final JdbcTemplate jdbcTemplate;
  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  @Autowired
  public UserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
    this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
  }

  public User findUserById(Long id) {
    try {
      String sqlQuery = "SELECT * FROM users WHERE id = ?";
      log.info("Получение пользователя с id = {}", id);

      return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToUser, id);
    } catch (EmptyResultDataAccessException e) {
      throw new EntityNotFoundException(String.format("Пользователя с id = %s не найден", id));
    }
  }

  public Collection<User> findAll() {
    String sqlQuery = "SELECT * FROM users";
    log.info("Получение всех пользователей");

    return jdbcTemplate.query(sqlQuery, this::mapRowToUser);
  }

  public List<User> findUsers(List<Long> ids) {
    SqlParameterSource parameters = new MapSqlParameterSource("ids", ids);

    String sqlQuery = "SELECT * FROM users WHERE id IN (:ids)";
    log.info("Получение всех друзей");

    return namedParameterJdbcTemplate.query(sqlQuery, parameters, this::mapRowToUser);
  }

  public User create(User user) {
    String sqlQuery = "INSERT INTO users (email, login, name, birthday) VALUES (?, ?, ?, ?)";

    KeyHolder keyHolder = new GeneratedKeyHolder();

    log.info("Создание пользователя: {}", user.toString());

    jdbcTemplate.update(con -> {
      PreparedStatement stmt = con.prepareStatement(sqlQuery, new String[]{"id"});

      stmt.setString(1, user.getEmail());
      stmt.setString(2, user.getLogin());
      stmt.setString(3, user.getName());
      stmt.setString(4, user.getBirthday().toString());

      return stmt;
    }, keyHolder);

    user.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());

    return findUserById(user.getId());
  }

  public void update(User user) {
    findUserById(user.getId());

    String sqlQuery = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE id = ?";

    log.info("Обновление пользователя с id = {}", user.getId());

    jdbcTemplate.update(
      sqlQuery,
      user.getEmail(),
      user.getLogin(),
      user.getName(),
      user.getBirthday(),
      user.getId()
    );
  }

  public boolean delete(Long id) {
    String sqlQuery = "DELETE FROM users WHERE id = ?";
    log.info("Удаление пользователя с id = {}", id);

    return jdbcTemplate.update(sqlQuery, id) > 0;
  }

  private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
    return User.builder()
      .id(resultSet.getLong("id"))
      .email(resultSet.getString("email"))
      .name(resultSet.getString("name"))
      .login(resultSet.getString("login"))
      .birthday(LocalDate.parse(resultSet.getString("birthday")))
      .build();
  }
}