package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private int counterId = 1;
    private final HashMap<Integer, User> users = new HashMap<>();

    @GetMapping
    private ResponseEntity<Collection<User>> getUsers() {
        log.info("Загружены все пользователи");

        return ResponseEntity.ok(users.values());
    }

    @PostMapping
    private ResponseEntity<User> addUser(@Valid @RequestBody User user) {
        if (isExistUserName(user.getName())) {
            user.setName(user.getLogin());
        }

        user.setId(counterId);
        increaseCounterId();

        users.put(user.getId(), user);
        log.info("Пользователь добавлен");

        return ResponseEntity.ok(user);
    }

    @PutMapping
    private ResponseEntity<User> updateUser(@Valid @RequestBody User user) {
        if (user.getId() == null || user.getId() == 0) {
            log.info("При обновлении пользователя не передан id");
            return ResponseEntity.badRequest().body(user);
        }

        if (isExistUserName(user.getName())) {
            user.setName(user.getLogin());
        }

        boolean isExist = users.containsKey(user.getId());

        if (!isExist) {
            log.warn(String.format("Пользователь с {id=%s} не найден", user.getId()));
            return ResponseEntity.status(500).body(user);
        }

        users.put(user.getId(), user);
        log.info(String.format("Пользователь с {id=%s} обновлен", user.getId()));

        return ResponseEntity.ok(user);
    }

    private void increaseCounterId() {
        counterId += 1;
    }

    private boolean isExistUserName(String name) {
        return name == null || name.isBlank();
    }
}
