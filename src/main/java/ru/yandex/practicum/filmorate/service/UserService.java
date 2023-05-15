package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import java.util.*;

@Slf4j
@Service
public class UserService {
  private final UserRepository userRepository;

  @Autowired
  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public User create(User user) {
    if (isExistUserName(user.getName())) {
      user.setName(user.getLogin());
    }

    return userRepository.create(user);
  }

  public User findUserById(Long id) {
    if (id < 0) {
      throw new EntityNotFoundException("Не верный id");
    }

    return userRepository.findUserById(id);
  }

  public Collection<User> getAll() {
    log.info("Получение всех пользователей");

    return userRepository.findAll();
  }

  public User update(User user) {
    if (user.getId() == null || user.getId() == 0) {
      log.info("При обновлении пользователя не передан id");
      return user;
    }

    if (isExistUserName(user.getName())) {
      user.setName(user.getLogin());
    }

    log.info(String.format("Обновление пользователя %s", user.getId()));

    userRepository.update(user);

    return user;
  }

  public String remove(Long userId) {
    if (userId < 0) {
      throw new EntityNotFoundException("Не верный id");
    }

    if (userRepository.delete(userId)) {
      log.info("Пользователь с id = {} удален", userId);

      return "Пользователь удален";
    }

    return null;
  }

  public List<User> getUsersByIds(List<Long> ids) {
    return userRepository.findUsers(ids);
  }

  private boolean isExistUserName(String name) {
    return name == null || name.isBlank();
  }
}
