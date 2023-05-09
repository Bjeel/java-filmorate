package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
  public Collection<Film> getAll();

  public Film put(Film film);

  public Film update(Film film);

  public Film remove(Film film);

  Film get(Long id);
}
