package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.repository.MpaRepository;

import java.util.List;

@Service
public class MpaService {
  private final MpaRepository mpaRepository;

  @Autowired
  public MpaService(MpaRepository mpaRepository) {
    this.mpaRepository = mpaRepository;
  }

  public List<Rating> findAll() {
    return mpaRepository.findAll();
  }

  public Rating findById(int id) {
    if (id < 0) {
      throw new EntityNotFoundException(String.format("Нет рейтинга с id %s", id));
    }

    return mpaRepository.findById(id);
  }
}
