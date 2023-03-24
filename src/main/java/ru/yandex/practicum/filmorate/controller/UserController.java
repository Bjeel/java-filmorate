package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;

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
    ResponseEntity<String> addUser(@Valid @RequestBody User user) {
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }

        user.setId(counterId);
        increaseCounterId();

        users.put(user.getId(), user);

        return ResponseEntity.ok("Пользователь добавлен");
    }

    @PutMapping
    ResponseEntity<String> updateUser(@Valid @RequestBody User user) {
        if (user.getId() == null || user.getId() == 0) {
            return ResponseEntity.badRequest().body("Невозможно обновить чего нет");
        }

        if (user.getName() == null) {
            user.setName(user.getLogin());
        }

        boolean isExist = users.containsKey(user.getId());

        users.put(user.getId(), user);

        return ResponseEntity.ok(isExist ? "Пользователь обновлен" : "Пользователь добавлен");
    }

    private void increaseCounterId() {
        counterId += 1;
    }
}
