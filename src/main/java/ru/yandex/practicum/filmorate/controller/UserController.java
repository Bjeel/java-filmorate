package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@Validated
public class UserController {
  @Autowired
  private UserService userService;

  @GetMapping
  public ResponseEntity<Collection<User>> getUsers() {
    return ResponseEntity.ok(userService.getAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<User> getUserById(
    @PathVariable @Positive Long id
  ) {
    return ResponseEntity.ok(userService.getById(id));
  }

  @PostMapping
  public ResponseEntity<User> addUser(@Valid @RequestBody User user) {
    return ResponseEntity.ok(userService.create(user));
  }

  @PutMapping
  public ResponseEntity<User> updateUser(@Valid @RequestBody User user) {
    return ResponseEntity.ok(userService.update(user));
  }

  @GetMapping("/{id}/friends")
  public ResponseEntity<List<User>> getUserFriends(
    @PathVariable @Positive(message = "id: должно быть больше 0") Long id
  ) {
    return ResponseEntity.ok(userService.getUserFriends(id));
  }

  @PutMapping("/{id}/friends/{friendId}")
  public ResponseEntity<String> addFriend(
    @PathVariable @Positive(message = "id: должно быть больше 0") Long id,
    @PathVariable @Positive(message = "friendId: должно быть больше 0") Long friendId
  ) {
    return ResponseEntity.ok(userService.addFriend(id, friendId));
  }

  @DeleteMapping("/{id}/friends/{friendId}")
  public ResponseEntity<String> deleteFriend(
    @PathVariable @Positive(message = "id: должно быть больше 0") Long id,
    @PathVariable @Positive(message = "friendId: должно быть больше 0") Long friendId
  ) {
    return ResponseEntity.ok(userService.deleteFriend(id, friendId));
  }

  @GetMapping("/{id}/friends/common/{otherId}")
  public ResponseEntity<List<User>> getCommonFriends(
    @PathVariable @Positive(message = "id: должно быть больше 0") Long id,
    @PathVariable @Positive(message = "otherId: должно быть больше 0") Long otherId
  ) {
    return ResponseEntity.ok(userService.getCommonFriends(id, otherId));
  }
}
