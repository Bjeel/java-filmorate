package ru.yandex.practicum.filmorate.repository.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserDao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@Repository
public class UserRepository implements UserDao {
  private final JdbcTemplate jdbcTemplate;

  @Autowired
  public UserRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public User findUserById(Long id) {
    try {
      String sqlQuery = "SELECT * FROM users WHERE id = ?";
      log.info("Получение пользователя с id = {}", id);

      return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToUser, id);
    } catch (EmptyResultDataAccessException e) {
      throw new EntityNotFoundException(String.format("Пользователя с id = %s не найден", id));
    }
  }

  @Override
  public Collection<User> findAll() {
    String sqlQuery = "SELECT * FROM users";
    log.info("Получение всех пользователей");

    return jdbcTemplate.query(sqlQuery, this::mapRowToUser);
  }

  @Override
  public User makeUser(User user) {
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

      String sqlQuery = "UPDATE users SET " +
        "email = ?, login = ?, name = ?, birthday = ? " +
        "where id = ?";

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