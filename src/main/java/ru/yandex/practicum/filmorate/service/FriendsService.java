package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.FriendsRepository;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class FriendsService {
  private final FriendsRepository friendsRepository;
  private final UserService userService;

  @Autowired
  public FriendsService(FriendsRepository friendsRepository, UserService userService) {
    this.friendsRepository = friendsRepository;
    this.userService = userService;
  }

  public List<User> findFriends(Long userId) {
    if (userId < 0) {
      throw new EntityNotFoundException(String.format("Нет пользователя с id = %s", userId));
    }

    log.info(String.format("Получение друзей пользователя %s", userId));

    List<Long> usersId = friendsRepository.findFriendsById(userId);

    return userService.getUsersByIds(usersId);
  }

  public String addFriend(Long userId, Long friendId) {
    if (Objects.equals(userId, friendId)) {
      throw new RuntimeException("Нельзя добавить себя в друзья");
    }

    checkUsers(userId, friendId);

    return friendsRepository.addFriend(userId, friendId);
  }

  public List<User> findCommon(Long userId, Long friendId) {
    if (Objects.equals(userId, friendId)) {
      throw new RuntimeException("Нельзя получить общих друзей с самим собой");
    }

    checkUsers(userId, friendId);

    List<Long> usersId = friendsRepository.findCommon(userId, friendId);

    return userService.getUsersByIds(usersId);
  }

  public String delete(Long userId, Long friendId) {
    if (Objects.equals(userId, friendId)) {
      throw new RuntimeException("Нельзя удалить себя из друга самому себе!");
    }

    checkUsers(userId, friendId);

    return friendsRepository.delete(userId, friendId) ? "Дружба разрушена" : "Нечего разрушить";
  }

  private void checkUsers(Long userId, Long friendId) {
    if (userId < 0) {
      throw new EntityNotFoundException(String.format("Нет пользователя с id = %s", userId));
    }

    if (friendId < 0) {
      throw new EntityNotFoundException(String.format("Нет пользователя с id = %s", friendId));
    }
  }
}
