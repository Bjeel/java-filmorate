package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/users")
@Validated
public class UserController {
  private final UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping
  public ResponseEntity<Collection<User>> getUsers() {
    return ResponseEntity.ok(userService.getAll());
  }

  @PostMapping
  public ResponseEntity<User> addUser(@Valid @RequestBody User user) {
    return ResponseEntity.ok(userService.create(user));
  }

  @GetMapping("/{id}")
  public ResponseEntity<User> getUserById(@PathVariable Long id) {
    return ResponseEntity.ok(userService.findUserById(id));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteUser(@PathVariable Long id) {
    return ResponseEntity.ok(userService.remove(id));
  }

  @PutMapping
  public ResponseEntity<User> updateUser(@Valid @RequestBody User user) {
    return ResponseEntity.ok(userService.update(user));
  }
}
