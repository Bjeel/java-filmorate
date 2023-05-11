package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmDao {

  Optional<Film> findFilmById(Long id);

  public Collection<Film> findAll();

  public Film create(Film film);

  public Film update(Film film);

  public Film remove(Film film);
}
