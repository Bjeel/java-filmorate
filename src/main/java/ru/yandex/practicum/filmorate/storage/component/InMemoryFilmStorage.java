package ru.yandex.practicum.filmorate.storage.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;

import java.util.Collection;
import java.util.HashMap;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
  private final HashMap<Long, Film> films = new HashMap<>();
  private Long counterId = 1L;

  @Override
  public Collection<Film> getAll() {
    log.info("Все фильмы выгружены");
    return films.values();
  }

  @Override
  public Film put(Film film) {
    if (film.getId() == null) {
      film.setId(counterId);
      increaseCounter();
    }

    films.put(film.getId(), film);

    log.info("Фильм добавлен");

    return film;
  }

  @Override
  public Film update(Film film) {
    boolean isExist = films.containsKey(film.getId());

    System.out.println();

    if (!isExist) {
      throw new FilmNotFoundException(String.format("Фильм с {id=%s} не найден", film.getId()));
    }

    films.put(film.getId(), film);
    log.info(String.format("Фильм с {id=%s} обновлен", film.getId()));

    return film;
  }

  @Override
  public Film remove(Film film) {
    films.remove(film.getId());
    log.info((String.format("Фильм с {id=%s} удален", film.getId())));

    return film;
  }

  @Override
  public Film get(Long id) {
    if (films.containsKey(id)) {
      return films.get(id);
    }

    throw new FilmNotFoundException(String.format("Фильм с {id=%s} не найден", id));
  }

  private void increaseCounter() {
    counterId += 1;
  }
}
