package com.google.gson.internal.bind.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.lang.reflect.Type;
import java.util.Arrays;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.bind.JsonAdapterAnnotationTypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

public final class JsonAdapterAnnotationTypeAdapterFactoryTest {

  private ConstructorConstructor constructorConstructor = new ConstructorConstructor();
  private Gson gson = new GsonBuilder().setPrePreProcessor(new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor)).create();

  @Test
  public void testNullType() {
    assertNull(JsonAdapterAnnotationTypeAdapterFactory.create(gson, null));
  }

  @Test
  public void testJsonAdapterAnnotationType() {
    TypeAdapter<String> expected = new StringAdapter(); // Assuming StringAdapter is a valid TypeAdapter implementation
    Type type = TypeToken.get(String.class).getType();
    TypeAdapter<String> actual = JsonAdapterAnnotationTypeAdapterFactory.create(gson, new TypeToken<String>() {}.where(new TypeToken<JsonAdapter>() {}.getType()));
    assertEquals(expected, actual);
  }

  @Test
  public void testJsonAdapterAnnotationTypeFactory() {
    TypeAdapter<Integer[]> expected = new IntegerArrayAdapter(); // Assuming IntegerArrayAdapter is a valid TypeAdapter implementation for Integer[]
    Type type = new TypeToken<Integer[]>() {}.getType();
    TypeAdapter<Integer[]> actual = JsonAdapterAnnotationTypeAdapterFactory.create(gson, new TypeToken<JsonAdapter.Array>() {}.getType());
    assertEquals(expected, actual);
  }

  @Test
  public void testJsonAdapterAnnotationTypeAdapter() {
    TypeAdapter<Person> expected = new PersonTypeAdapter(); // Assuming PersonTypeAdapter is a valid TypeAdapter implementation for Person
    Type personType = new TypeToken<Person>() {}.getType();
    TypeAdapter<Person> actual = JsonAdapterAnnotationTypeAdapterFactory.create(gson, Person.class);
    assertEquals(expected, actual);
  }

  @Test
  public void testJsonAdapterAnnotationTypeAdapterFactory() {
    TypeAdapter<String> expected = new StringAdapter(); // Assuming StringAdapter is a valid TypeAdapter implementation
    TypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    TypeAdapter<String> actual = factory.create(gson, new TypeToken<String>() {}.getType());
    assertEquals(expected, actual);
  }

  @Test
  public void testJsonAdapterAnnotationWrongType() {
    TypeAdapter<Integer> integerAdapter = new IntegerAdapter(); // Assuming IntegerAdapter is a valid TypeAdapter implementation for Integer
    Type type = new TypeToken<String>() {}.getType();
    try {
      JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, type, JsonAdapter.class);
      fail("Expected IllegalArgumentException for wrong type.");
    } catch (IllegalArgumentException e) {
      assertEquals("@JsonAdapter value must be TypeAdapter or TypeAdapterFactory reference.", e.getMessage());
    }
  }

  @Test
  public void testJsonAdapterAnnotationUnknownType() {
    TypeAdapter<String> stringAdapter = new StringAdapter(); // Assuming StringAdapter is a valid TypeAdapter implementation for String
    try {
      JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, new TypeToken<Object>() {}.getType(), JsonAdapter.class);
      fail("Expected IllegalArgumentException for unknown type.");
    } catch (IllegalArgumentException e) {
      assertEquals("Unknown type com.google.gson.reflect.TypeToken@5852d6b6.", e.getMessage());
    }
  }
}

class Person {
  // Person class implementation
}

class StringAdapter implements TypeAdapter<String> {
  // StringAdapter implementation
}

class IntegerArrayAdapter implements TypeAdapter<Integer[]> {
  // IntegerArrayAdapter implementation
}