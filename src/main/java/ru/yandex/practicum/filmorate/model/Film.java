package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import ru.yandex.practicum.filmorate.annotation.IsAfter;

import javax.validation.constraints.*;

import java.time.LocalDate;
import java.util.HashSet;

@Data
public class Film {
  @Positive
  private Long id;

  private HashSet<Long> likes = new HashSet<>();

  @NotBlank(message = "Название не может быть пустым")
  private String name;

  @Size(max = 200, message = "Максимальная длина описания — 200 символов;")
  private String description;

  @IsAfter(
    message = "дата релиза — не раньше 28 декабря 1895",
    current = "1895-12-28"
  )
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate releaseDate;

  @Positive(message = "Продолжительность фильма должна быть положительной")
  private int duration;
}
