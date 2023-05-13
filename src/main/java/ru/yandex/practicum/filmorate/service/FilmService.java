package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
  private final FilmRepository filmRepository;
  private final MpaService mpaService;

  @Autowired
  public FilmService(FilmRepository filmRepository, MpaService mpaService) {
    this.filmRepository = filmRepository;
    this.mpaService = mpaService;

  }

  public Film getById(Long id) {
    if (id < 0) {
      log.error("При обновлении фильма не передан id");
      throw new EntityNotFoundException(String.format("Пользователь с id: %s не найден", id));
    }

    log.info(String.format("Получение фильма с id %s", id));

    Film film = filmRepository.findFilmById(id);

    film.setMpa(mpaService.findById(film.getMpa().getId()));

    return film;
  }

  public Collection<Film> getAll() {
    log.info("Получение всех фильмов");
    Collection<Film> films = filmRepository.findAll();
    List<Rating> ratings = mpaService.findAll();

    return films
      .stream()
      .peek(film -> {
        Optional<Rating> rating = ratings
          .stream()
          .filter(mpa -> mpa.getId() == film.getMpa().getId())
          .findFirst();


        rating.ifPresent(film::setMpa);
      })
      .collect(Collectors.toList());
  }

  public Film put(Film film) {
    log.info(String.format("Фильм с id %s добавлен", film.getId()));

    return filmRepository.create(film);
  }

  public Film update(Film film) {
    if (film.getId() == null || film.getId() == 0) {
      log.warn("При обновлении фильма не передан id");
      return film;
    }

    log.info(String.format("Фильм с id %s обновлен", film.getId()));

    return filmRepository.update(film);
  }

  public String remove(Film film) {
    return filmRepository.delete(film.getId()) ? "Фильм удален" : "Нечего удалять";
  }
}
