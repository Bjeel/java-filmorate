package ru.yandex.practicum.filmorate.repository;


import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserDao {

  public User makeUser(User user);

  public User findUserById(Long id);

  public Collection<User> findAll();

  public void update(User user);
//
//  public User remove(User user);
//
//  public Collection<User> findFriends(Long userId, Long friendId);
}