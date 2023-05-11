package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.repository.UserDao;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Slf4j
@Service
public class UserService {
  private final UserDao userDao;

  @Autowired
  public UserService(UserDao userDao) {
    this.userDao = userDao;
  }

  public Collection<User> getAll() {
    log.info("Получение всех пользователей");

    return userDao.findAll();
  }

  public User create(User user) {
    if (isExistUserName(user.getName())) {
      user.setName(user.getLogin());
    }

    return userDao.makeUser(user);
  }

  public User findUserById(Long id) {
    if (id < 0) {
      throw new EntityNotFoundException("Не верный id");
    }

    return userDao.findUserById(id);
  }

  public User update(User user) {
    System.out.println(user);
    if (user.getId() == null || user.getId() == 0) {
      log.info("При обновлении пользователя не передан id");
      return user;
    }

    if (isExistUserName(user.getName())) {
      user.setName(user.getLogin());
    }

    log.info(String.format("Обновление пользователя %s", user.getId()));

    userDao.update(user);

    return user;
  }

//  public User remove(User user) {
//    return userStorage.remove(user);
//  }
//
//  public List<User> getUserFriends(Long userId) {
//    log.info(String.format("Получение друзей пользователя %s", userId));
//
//    return userStorage.get(userId).getFriends().stream()
//      .map(id -> userStorage.get(id)).collect(Collectors.toList());
//  }
//
//  public String addFriend(Long userId, Long friendId) {
//    checkIds(userId, friendId);
//
//    User user = userStorage.get(userId);
//    user.getFriends().add(friendId);
//
//    user = userStorage.get(friendId);
//    user.getFriends().add(userId);
//
//    String result = String.format("Пользователи с айди %s и %s добавлен в друзья", userId, friendId);
//    log.info(result);
//
//    return result;
//  }
//
//  public String deleteFriend(Long userId, Long friendId) {
//    checkIds(userId, friendId);
//
//    User user = userStorage.get(userId);
//    HashSet<Long> friends = new HashSet<>(user.getFriends());
//    friends.remove(friendId);
//
//    user.setFriends(friends);
//    userStorage.update(user);
//
//    String result = String.format("Пользователь с {id=%s} удален из друзей", friendId);
//    log.info(result);
//
//    return result;
//  }
//
//  public List<User> getCommonFriends(Long userId, Long friendId) {
//    checkIds(userId, friendId);
//
//    User firstUser = userStorage.get(userId);
//    User secondUser = userStorage.get(friendId);
//
//    HashSet<Long> intersection = new HashSet<>(firstUser.getFriends());
//
//    intersection.retainAll(secondUser.getFriends());
//    log.info(String.format("Получение общих друзей %s и %s ", userId, userId));
//
//    return intersection.stream().map(id -> userStorage.get(id)).collect(Collectors.toList());
//  }
//
//  private void checkIds(Long id, Long userId) {
//    if (id < 0) {
//      throw new EntityNotFoundException(String.format("Пользователь с id: %s не найден", id));
//    }
//
//    if (userId < 0) {
//      throw new EntityNotFoundException(String.format("Пользователь с id: %s не найден", userId));
//    }
//  }

  private boolean isExistUserName(String name) {
    return name == null || name.isBlank();
  }
}
