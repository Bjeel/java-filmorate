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
    ResponseEntity<Collection<User>> getUsers() {
        return ResponseEntity.ok(users.values());
    }

    @PostMapping
    ResponseEntity<User> addUser(@Valid @RequestBody User user) {
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }

        user.setId(counterId);
        increaseCounterId();

        users.put(user.getId(), user);

        return ResponseEntity.ok(user);
    }

    @PutMapping
    ResponseEntity<User> updateUser(@Valid @RequestBody User user) {
        if (user.getId() == null || user.getId() == 0) {
            return ResponseEntity.badRequest().body(user);
        }

        if (user.getName() == null) {
            user.setName(user.getLogin());
        }

        boolean isExist = users.containsKey(user.getId());

        if (!isExist) {
            log.warn(String.format("Пользователь с {id=%s} не найден", user.getId()));
            return ResponseEntity.status(500).body(user);
        }

        users.put(user.getId(), user);

        return ResponseEntity.ok(user);
    }

    private void increaseCounterId() {
        counterId += 1;
    }
}
