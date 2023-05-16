package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.LikesService;

import java.util.List;
import java.util.Optional;


@Slf4j
@RestController
@RequestMapping("/films")
@Validated
public class LikesController {
  private final LikesService likesService;

  @Autowired
  public LikesController(LikesService likesService) {
    this.likesService = likesService;
  }

  @PutMapping("/{id}/like/{userId}")
  public ResponseEntity<String> addLike(@PathVariable Long id, @PathVariable Long userId) {
    return ResponseEntity.ok(likesService.create(id, userId));
  }

  @DeleteMapping("/{id}/like/{userId}")
  public ResponseEntity<String> deleteLike(@PathVariable Long id, @PathVariable Long userId) {
    return ResponseEntity.ok(likesService.deleteLike(id, userId));
  }


  @GetMapping("/popular")
  public ResponseEntity<List<Film>> getPopular(@RequestParam Optional<Integer> count) {
    return ResponseEntity.ok(likesService.getPopular(count));
  }
}
