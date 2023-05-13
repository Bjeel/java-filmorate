package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
  private final FilmRepository filmRepository;
  private final FilmStorage filmStorage;

  @Autowired
  public FilmService(FilmStorage filmStorage, FilmRepository filmRepository) {
    this.filmStorage = filmStorage;
    this.filmRepository = filmRepository;
  }

  public Film getById(Long id) {
    if (id < 0) {
      log.error("При обновлении фильма не передан id");
      throw new EntityNotFoundException(String.format("Пользователь с id: %s не найден", id));
    }

    log.info(String.format("Получение фильма с id %s", id));

    return filmRepository.findFilmById(id);
  }

  public Collection<Film> getAll() {
    log.info("Получение всех фильмов");

    return filmRepository.findAll();
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

  public Film remove(Film film) {
    return filmStorage.remove(film);
  }

  public List<Film> getPopular(Optional<Integer> count) {
    int size = count.orElse(10);
    List<Film> sortedFilms = filmStorage.getAll().stream()
      .sorted((first, second) -> Integer.compare(second.getLikes().size(), first.getLikes().size()))
      .collect(Collectors.toList());
    log.info("Получение популярных фильмов");

    return sortedFilms.subList(0, Math.min(sortedFilms.size(), size));
  }

  public Film addLike(Long id, Long userId) {
    checkIds(id, userId);

    Film film = filmStorage.get(id);
    film.getLikes().add(userId);
    log.info(String.format("Лайк %s фильму %s добавлен", id, userId));

    return filmStorage.update(film);
  }

  public Film deleteLike(Long id, Long userId) {
    checkIds(id, userId);

    Film film = filmStorage.get(id);
    film.getLikes().remove(userId);
    log.info(String.format("Лайк %s у фильма %s удален", id, userId));

    return filmStorage.update(film);
  }

  private void checkIds(Long id, Long userId) {
    if (id < 0) {
      throw new EntityNotFoundException(String.format("Фильм с id: %s не найден", id));
    }

    if (userId < 0) {
      throw new EntityNotFoundException(String.format("Пользователь с id: %s не найден", userId));
    }
  }
}
