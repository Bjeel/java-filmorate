package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.repository.FriendsRepository;

import java.util.List;

@Slf4j
@Service
public class FriendsService {
  private final FriendsRepository friendsRepository;

  @Autowired
  public FriendsService(FriendsRepository friendsRepository) {
    this.friendsRepository = friendsRepository;
  }

  public List<Long> findFriends(Long userId) {
    log.info(String.format("Получение друзей пользователя %s", userId));

    return friendsRepository.findFriendsById(userId);
  }

  public String addFriend(Long userId, Long friendId) {
    boolean hasUserRelative = friendsRepository.findFriendsStatus(userId, friendId);
    boolean hasFriendRelative = friendsRepository.findFriendsStatus(friendId, userId);

    if (hasUserRelative && hasFriendRelative) {
      return "Пользователи уже друзья";
    }

    if (!hasFriendRelative && !hasUserRelative) {
      return friendsRepository.addFriend(userId, friendId);
    }

    return "";

//    String result = String.format("Пользователи с айди %s и %s добавлен в друзья", userId, friendId);
//    log.info(result);
//
//    return friendsRepository.addFriend(userId, friendId);
  }
}
