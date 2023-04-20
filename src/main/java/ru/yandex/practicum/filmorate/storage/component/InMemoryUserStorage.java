package ru.yandex.practicum.filmorate.storage.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.util.*;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
  private final HashMap<Long, User> users = new HashMap<>();
  private Long counterId = 1L;

  @Override
  public User get(Long id) {
    if (users.containsKey(id)) {
      return users.get(id);
    }

    throw new UserNotFoundException(String.format("Пользователь с {id=%s} не найден", id));
  }

  @Override
  public Collection<User> getAll() {
    return users.values();
  }

  @Override
  public User create(User user) {
    if (user.getId() == null) {
      user.setId(counterId);
      increaseCounter();
    }

    users.put(user.getId(), user);

    log.info("Пользователь добавлен");
    return user;
  }

  @Override
  public User update(User user) {
    boolean isExist = users.containsKey(user.getId());

    if (!isExist) {
      throw new UserNotFoundException(String.format("Пользователь с {id=%s} не найден", user.getId()));
    }

    users.put(user.getId(), user);
    log.info(String.format("Пользователь с {id=%s} обновлен", user.getId()));

    return user;
  }

  @Override
  public User remove(User user) {
    users.remove(user.getId());
    log.info((String.format("Пользователь с {id=%s} удален", user.getId())));

    return user;
  }

  private void increaseCounter() {
    counterId += 1;
  }
}
