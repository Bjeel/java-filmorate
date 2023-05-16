package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.GenreRepository;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;

@Service
public class GenreService {
  private final GenreRepository genreRepository;

  @Autowired
  public GenreService(GenreRepository genreRepository) {
    this.genreRepository = genreRepository;
  }

  public List<Genre> findAll() {
    return genreRepository.findAll();
  }

  public Genre findById(int id) {
    if (id < 0) {
      throw new EntityNotFoundException(String.format("Нет рейтинга с id %s", id));
    }

    return genreRepository.findById(id);
  }

  public void addFilm(Long id, LinkedHashSet<Genre> genres) {
    if (genres.size() != 0) {
      genreRepository.addFilm(id, genres);
    } else {
      genreRepository.deleteGenres(id);
    }
  }

  public LinkedHashSet<Genre> findAllByFilm(Long id) {
    if (id < 0) {
      throw new EntityNotFoundException(String.format("Нет рейтинга с id %s", id));
    }

    return genreRepository.findAllByFilmId(id);
  }

  public HashMap<Long, LinkedHashSet<Genre>> getAllGenresAndFilms() {
    return genreRepository.getAllGenresAndFilms();
  }
}
