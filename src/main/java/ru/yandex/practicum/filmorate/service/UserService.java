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
  private final FriendsService friendsService;

  @Autowired
  public UserService(UserRepository userRepository, FriendsService friendsService) {
    this.userRepository = userRepository;
    this.friendsService = friendsService;
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

  public List<User> getUserFriends(Long userId) {
    if (userId < 0) {
      throw new EntityNotFoundException("Не верный id");
    }

    List<Long> ids = friendsService.findFriends(userId);

    return userRepository.findUsers(ids);
  }

  public String addFriend(Long userId, Long friendId) {
    checkIds(userId, friendId);

    return friendsService.addFriend(userId, friendId);
  }

  public String deleteFriend(Long userId, Long friendId) {
    checkIds(userId, friendId);

    return friendsService.delete(userId, friendId);
  }

  public List<User> getCommonFriends(Long userId, Long friendId) {
    checkIds(userId, friendId);

    List<Long> ids = friendsService.findCommon(userId, friendId);

    return userRepository.findUsers(ids);
  }

  private void checkIds(Long id, Long userId) {
    if (id < 0) {
      throw new EntityNotFoundException(String.format("Пользователь с id: %s не найден", id));
    }

    if (userId < 0) {
      throw new EntityNotFoundException(String.format("Пользователь с id: %s не найден", userId));
    }
  }

  private boolean isExistUserName(String name) {
    return name == null || name.isBlank();
  }
}
