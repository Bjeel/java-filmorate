package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/films")
@Validated
public class FilmController {
  private final FilmService filmService;

  @Autowired
  public FilmController(FilmService filmService) {
    this.filmService = filmService;
  }

  @GetMapping
  public ResponseEntity<Collection<Film>> getFilms() {
    return ResponseEntity.ok(filmService.getAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Film> getFilmById(@PathVariable Long id) {
    return ResponseEntity.ok(filmService.getById(id));
  }

  @DeleteMapping()
  public ResponseEntity<Film> removeFilm(@RequestBody Film film) {
    return ResponseEntity.ok(filmService.remove(film));
  }

  @PostMapping
  public ResponseEntity<Film> addFilm(@Valid @RequestBody Film film) {
    return ResponseEntity.ok(filmService.put(film));
  }

  @PutMapping
  public ResponseEntity<Film> updateFilm(@Valid @RequestBody Film film) {
    return ResponseEntity.ok(filmService.update(film));
  }

  @PutMapping("/{id}/like/{userId}")
  public ResponseEntity<Film> addLike(@PathVariable Long id, @PathVariable Long userId) {
    return ResponseEntity.ok(filmService.addLike(id, userId));
  }

  @DeleteMapping("/{id}/like/{userId}")
  public ResponseEntity<Film> deleteLike(@PathVariable Long id, @PathVariable Long userId) {
    return ResponseEntity.ok(filmService.deleteLike(id, userId));
  }

  @GetMapping("/popular")
  public ResponseEntity<List<Film>> getPopular(@RequestParam Optional<Integer> count) {
    return ResponseEntity.ok(filmService.getPopular(count));
  }
}
