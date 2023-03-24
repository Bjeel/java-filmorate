package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import ru.yandex.practicum.filmorate.annotation.IsAfter;

import javax.validation.constraints.*;

import java.time.LocalDate;

@Data
public class Film {
  @Positive
  private Integer id;

  @NotBlank(message = "Название не может быть пустым")
  @NotNull(message = "Название не может быть null")
  final private String name;

  @Size(max = 200, message = "Максимальная длина описания — 200 символов;")
  @NotBlank
  final private String description;

  @IsAfter(
    message = "дата релиза — не раньше 28 декабря 1895",
    current = "1895-12-28"
  )
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  final private LocalDate releaseDate;

  @Positive(message = "Продолжительность фильма должна быть положительной")
  final private int duration;
}
