package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.Collection;

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
  public ResponseEntity<String> removeFilm(@RequestBody Film film) {
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
}
