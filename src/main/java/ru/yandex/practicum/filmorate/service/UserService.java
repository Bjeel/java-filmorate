package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {
  @Autowired
  private UserStorage userStorage;

  public Collection<User> getAll() {
    return userStorage.getAll();
  }

  public User create(User user) {
    if (isExistUserName(user.getName())) {
      user.setName(user.getLogin());
    }

    return userStorage.create(user);
  }

  public User update(User user) {
    if (user.getId() == null || user.getId() == 0) {
      log.info("При обновлении пользователя не передан id");
      return user;
    }

    if (isExistUserName(user.getName())) {
      user.setName(user.getLogin());
    }

    return userStorage.update(user);
  }

  private boolean isExistUserName(String name) {
    return name == null || name.isBlank();
  }

  public User getById(Long id) {
    return userStorage.get(id);
  }

  public List<User> getUserFriends(Long userId) {
    return userStorage
      .get(userId)
      .getFriends()
      .stream()
      .map(id -> userStorage.get(id))
      .collect(Collectors.toList());
  }

  public String addFriend(Long id, Long friendId) {
    User user = userStorage.get(id);
    user.getFriends().add(friendId);

    user = userStorage.get(friendId);
    user.getFriends().add(id);

    return String.format("Пользователи с айди %s и %s добавлен в друзья", id, friendId);
  }

  public String deleteFriend(Long id, Long friendId) {
    User user = userStorage.get(id);
    HashSet<Long> friends = new HashSet<>(user.getFriends());
    friends.remove(friendId);

    user.setFriends(friends);
    userStorage.update(user);

    return String.format("Пользователь с {id=%s} удален из друзей", friendId);
  }

  public List<User> getCommonFriends(Long firstUserId, Long secondUserId) {
    User firstUser = userStorage.get(firstUserId);
    User secondUser = userStorage.get(secondUserId);

    HashSet<Long> intersection = new HashSet<>(firstUser.getFriends());

    intersection.retainAll(secondUser.getFriends());

    return intersection.stream()
      .map(id -> userStorage.get(id))
      .collect(Collectors.toList());
  }
}
