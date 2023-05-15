package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.repository.FilmRepository;

import java.util.*;

@Slf4j
@Service
public class FilmService {
  private final FilmRepository filmRepository;
  private final MpaService mpaService;
  private final GenreService genreService;

  @Autowired
  public FilmService(FilmRepository filmRepository, MpaService mpaService, GenreService genreService) {
    this.filmRepository = filmRepository;
    this.mpaService = mpaService;
    this.genreService = genreService;
  }

  public Film getById(Long filmId) {
    if (filmId < 0) {
      log.error("При обновлении фильма не передан id");
      throw new EntityNotFoundException(String.format("Пользователь с id: %s не найден", filmId));
    }

    log.info(String.format("Получение фильма с id %s", filmId));

    Film film = filmRepository.findFilmById(filmId);

    film.setMpa(mpaService.findById(film.getMpa().getId()));
    film.setGenres(genreService.findAllByFilm(filmId));

    return film;
  }

  public Collection<Film> getAll() {
    log.info("Получение всех фильмов");
    Collection<Film> films = filmRepository.findAll();
    List<Rating> ratings = mpaService.findAll();
    HashMap<Long, LinkedHashSet<Genre>> genres = genreService.getAllGenresAndFilms();

    films
      .forEach(film -> {
        Optional<Rating> rating = ratings
          .stream()
          .filter(mpa -> mpa.getId() == film.getMpa().getId())
          .findFirst();

        rating.ifPresent(film::setMpa);
        film.setGenres(genres.getOrDefault(film.getId(), new LinkedHashSet<>()));
      });

    return films;
  }

  public Film put(Film film) {
    Film createdFilm = filmRepository.create(film);

    if (film.getGenres() != null) {
      genreService.addFilm(createdFilm.getId(), film.getGenres());
    }

    createdFilm.setGenres(genreService.findAllByFilm(createdFilm.getId()));

    return createdFilm;
  }

  public Film update(Film film) {
    if (film.getId() == null || film.getId() == 0) {
      log.warn("При обновлении фильма не передан id");
      return film;
    }

    log.info(String.format("Фильм с id %s обновлен", film.getId()));

    Film updatedFilm = filmRepository.update(film);

    if (film.getGenres() != null) {
      genreService.addFilm(updatedFilm.getId(), film.getGenres());
    }

    updatedFilm.setGenres(genreService.findAllByFilm(film.getId()));

    return updatedFilm;
  }

  public String remove(Film film) {
    return filmRepository.delete(film.getId()) ? "Фильм удален" : "Нечего удалять";
  }
}
