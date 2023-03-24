package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.*;

import java.time.LocalDate;

@Data
public class User {
    @Positive
    private Integer id;

    @Email(message = "Не верный формат электронной почты")
    @NotBlank(message = "Электронная почта не может быть пустой")
    private final String email;

    @NotBlank(message = "Логин не может быть пустым")
    @Pattern(
      regexp = "^\\w[a-zA-Z@#0-9.]*$",
      message = "Логин должен содержать буквы и цифры без пробела"
    )
    private final String login;

    private String name;

    @PastOrPresent(message = "Дата рождения не может быть в будущем")
    private final LocalDate birthday;
}
