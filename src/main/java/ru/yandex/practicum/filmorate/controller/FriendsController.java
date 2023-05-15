package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FriendsService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users/{id}")
@Validated
public class FriendsController {
  private final FriendsService friendsService;

  @Autowired
  public FriendsController(FriendsService friendsService) {
    this.friendsService = friendsService;
  }

  @GetMapping("/friends")
  public ResponseEntity<List<User>> getUserFriends(@PathVariable Long id) {
    return ResponseEntity.ok(friendsService.findFriends(id));
  }

  @PutMapping("/friends/{friendId}")
  public ResponseEntity<String> addFriend(@PathVariable Long id, @PathVariable Long friendId) {
    return ResponseEntity.ok(friendsService.addFriend(id, friendId));
  }

  @DeleteMapping("/friends/{friendId}")
  public ResponseEntity<String> deleteFriend(@PathVariable Long id, @PathVariable Long friendId) {
    return ResponseEntity.ok(friendsService.delete(id, friendId));
  }

  @GetMapping("/friends/common/{otherId}")
  public ResponseEntity<List<User>> getCommonFriends(@PathVariable Long id, @PathVariable Long otherId) {
    return ResponseEntity.ok(friendsService.findCommon(id, otherId));
  }
}
