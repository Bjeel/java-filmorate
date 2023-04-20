package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
  @Autowired
  private FilmStorage filmStorage;

  public Collection<Film> getAll() {
    return filmStorage.getAll();
  }

  public Film put(Film film) {
    return filmStorage.put(film);
  }

  public Film update(Film film) {
    if (film.getId() == null || film.getId() == 0) {
      log.warn("При обновлении фильма не передан id");
      return film;
    }

    return filmStorage.update(film);
  }

  public Film getById(Long id) {
    return filmStorage.get(id);
  }

  public List<Film> getPopular(Optional<Integer> count) {
    int size = count.orElse(10);
    List<Film> sortedFilms = filmStorage
      .getAll()
      .stream()
      .sorted((first, second) -> Integer.compare(second.getLikes().size(), first.getLikes().size()))
      .collect(Collectors.toList());

    return sortedFilms.subList(0, Math.min(sortedFilms.size(), size));
  }

  public Film addLike(Long id, Long userId) {
    Film film = filmStorage.get(id);
    film.getLikes().add(userId);

    return filmStorage.update(film);
  }

  public Film deleteLike(Long id, Long userId) {
    Film film = filmStorage.get(id);
    film.getLikes().remove(userId);

    return filmStorage.update(film);
  }
}
