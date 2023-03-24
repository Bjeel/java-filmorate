package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private int counterId = 1;
    private final HashMap<Integer, Film> films = new HashMap<>();

    @GetMapping
    ResponseEntity<Collection<Film>> getFilms() {
        log.info("Загружены все фильмы");

        return ResponseEntity.ok(films.values());
    }

    @PostMapping
    ResponseEntity<String> addFilm(@Valid @RequestBody Film film) {
        film.setId(counterId);
        increaseCounterId();

        films.put(film.getId(), film);

        log.info("Фильм добавлен");
        return ResponseEntity.ok("Фильм добавлен");
    }

    @PutMapping
    ResponseEntity<String> updateFilm(@Valid @RequestBody Film film) {
        if (film.getId() == null || film.getId() == 0) {
            log.info("При обновлении фильма не передан id");
            return ResponseEntity.badRequest().body("Невозможно обновить чего нет");
        }

        boolean isExist = films.containsKey(film.getId());

        films.put(film.getId(), film);
        log.info(String.format("Фильм с {id=%s} %s", film.getId(), isExist ? "обновлен" : "добавлен"));

        return ResponseEntity.ok(isExist ? "Фильм обновлен" : "Фильм добавлен");
    }

    private void increaseCounterId() {
        counterId += 1;
    }
}
