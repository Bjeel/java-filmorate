package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Valid
@Data
@Builder
public class Rating {
  private int id;
  private String name;

  @JsonCreator
  public Rating(
    @JsonProperty("id") int id,
    @JsonProperty("name") String name
  ) {
    this.id = id;
    this.name = name;
  }
}
