package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/mpa")
@Validated
public class MpaController {
  MpaService mpaService;

  @Autowired
  public MpaController(MpaService mpaService) {
    this.mpaService = mpaService;
  }

  @GetMapping
  public ResponseEntity<Collection<Rating>> findAll() {
    return ResponseEntity.ok(mpaService.findAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Rating> findRatingById(@PathVariable int id) {
    return ResponseEntity.ok(mpaService.findById(id));
  }
}
