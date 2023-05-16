package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.repository.LikesRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LikesService {
  private final LikesRepository likesRepository;
  private final MpaService mpaService;

  @Autowired
  public LikesService(LikesRepository likesRepository, MpaService mpaService) {
    this.likesRepository = likesRepository;
    this.mpaService = mpaService;
  }

  public String create(Long filmId, Long userId) {
    checkIds(filmId, userId);

    likesRepository.create(filmId, userId);

    return "Лайк добавлен";
  }

  public String deleteLike(Long filmId, Long userId) {
    checkIds(filmId, userId);

    boolean isDeleted = likesRepository.deleteLike(filmId, userId);

    return isDeleted ? "Лайк удален" : "Нечего удалять";
  }

  public List<Film> getPopular(Optional<Integer> count) {
    List<Film> films = likesRepository.getPopular(count.orElse(10));
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

  private void checkIds(Long filmId, Long userId) {
    if (filmId < 0) {
      throw new EntityNotFoundException(String.format("Фильм с id: %s не найден", filmId));
    }

    if (userId < 0) {
      throw new EntityNotFoundException(String.format("Пользователь с id: %s не найден", userId));
    }
  }
}
