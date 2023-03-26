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
    private ResponseEntity<Collection<Film>> getFilms() {
        log.info("Загружены все фильмы");

        return ResponseEntity.ok(films.values());
    }

    @PostMapping
    private ResponseEntity<Film> addFilm(@Valid @RequestBody Film film) {
        film.setId(counterId);
        increaseCounterId();

        films.put(film.getId(), film);

        log.info("Фильм добавлен");

        return ResponseEntity.ok(film);
    }

    @PutMapping
    private ResponseEntity<Film> updateFilm(@Valid @RequestBody Film film) {
        if (film.getId() == null || film.getId() == 0) {
            log.warn("При обновлении фильма не передан id");
            return ResponseEntity.badRequest().body(film);
        }

        boolean isExist = films.containsKey(film.getId());

        System.out.println();

        if (!isExist) {
            log.warn(String.format("Фильм с {id=%s} не найден", film.getId()));
            return ResponseEntity.status(500).body(film);
        }

        films.put(film.getId(), film);
        log.info(String.format("Фильм с {id=%s} обновлен", film.getId()));

        return ResponseEntity.ok(film);
    }

    private void increaseCounterId() {
        counterId += 1;
    }
}
