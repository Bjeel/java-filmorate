package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.*;

import java.time.LocalDate;
import java.util.HashSet;

@Data
@Valid
public class User {
  private HashSet<Long> friends = new HashSet<>();

  @Positive
  private Long id;

  @Email(message = "Не верный формат электронной почты")
  @NotBlank(message = "Электронная почта не может быть пустой")
  private String email;

  @NotBlank(message = "Логин не может быть пустым")
  @Pattern(
    regexp = "^\\w[a-zA-Z@#0-9.]*$",
    message = "Логин должен содержать буквы и цифры без пробела"
  )
  private String login;

  private String name;

  @PastOrPresent(message = "Дата рождения не может быть в будущем")
  private LocalDate birthday;
}
