//package ru.yandex.practicum.filmorate.deserializer;
//
//import com.fasterxml.jackson.core.JsonParser;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.DeserializationContext;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
//import com.fasterxml.jackson.databind.node.IntNode;
//import org.apache.tomcat.jni.Local;
//import ru.yandex.practicum.filmorate.model.Film;
//
//import java.io.IOException;
//import java.time.LocalDate;
//
//public class FilmDeserializer extends StdDeserializer<Film> {
//
//  public ItemDeserializer() {
//    this(null);
//  }
//
//  public ItemDeserializer(Class<?> vc) {
//    super(vc);
//  }
//
//  @Override
//  public Film deserialize(JsonParser jp, DeserializationContext ctxt)
//    throws IOException, JsonProcessingException {
//    JsonNode node = jp.getCodec().readTree(jp);
//    String name = (String) ((IntNode) node.get("name")).asText();
//    String description = node.get("description").asText();
//    LocalDate releaseDate = LocalDate.parse(node.get("releaseDate").asText());
//    int userId = (Integer) ((IntNode) node.get("createdBy")).numberValue();
//
//    return new Film(itemName, new User(userId, null));
//  }
//}
