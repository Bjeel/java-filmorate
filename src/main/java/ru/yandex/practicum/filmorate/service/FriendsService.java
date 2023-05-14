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
    return friendsRepository.addFriend(userId, friendId);
  }

  public List<Long> findCommon(Long userId, Long friendId) {
    return friendsRepository.findCommon(userId, friendId);
  }

  public String delete(Long userId, Long friendId) {
    return friendsRepository.delete(userId, friendId) ? "Дружба разрушена" : "Нечего разрушить";
  }
}
