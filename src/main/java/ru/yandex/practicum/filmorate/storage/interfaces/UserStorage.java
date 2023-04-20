package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

public interface UserStorage {
  public User get(Long id);

  public Collection<User> getAll();

  public User create(User user);

  public User update(User user);

  public User remove(User user);
}
