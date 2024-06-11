package com.example.junit4.test.gson.internal.bind;

import static org.junit.Assert.*;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.reflect.TypeToken;

import org.junit.Test;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

  private Gson gson = new Gson();

  @Test
  public void testJsonAdapterAnnotationTypeAdapterFactory_TypeAdapter() {
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(
        new ConstructorConstructor());
    TypeAdapter<String> stringTypeAdapter = factory.create(gson, TypeToken.of(String.class));
    assertNotNull(stringTypeAdapter);

    String input = "example";
    String output = stringTypeAdapter.fromJson(input);
    assertEquals(input, output);
  }

  @Test
  public void testJsonAdapterAnnotationTypeAdapterFactory_TypeAdapterFactory() {
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(
        new ConstructorConstructor());
    TypeAdapterFactory typeAdapterFactory = new JsonAdapterAnnotationTypeAdapterFactory(
        new ConstructorConstructor()) {
          @Override
          public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
            return null;
          }
        };
    TypeAdapter<Number> numberTypeAdapter = factory.create(gson, TypeToken.of(Number.class));
    assertNotNull(numberTypeAdapter);

    Number input = 123;
    Number output = numberTypeAdapter.fromJson("123");
    assertEquals(input, output);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testJsonAdapterAnnotationTypeAdapterFactory_InvalidValue() {
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(
        new ConstructorConstructor());
    JsonAdapterAnnotationTypeAdapterFactory invalidFactory = new JsonAdapterAnnotationTypeAdapterFactory(
        new ConstructorConstructor()) {
          @Override
          public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
            return null;
          }
        };
    TypeAdapter<Object> objectTypeAdapter = factory.create(gson, TypeToken.of(Object.class));
    fail("Should not create TypeAdapter for Object.");

    JsonAdapterAnnotationTypeAdapterFactory invalidFactory2 = new JsonAdapterAnnotationTypeAdapterFactory(
        new ConstructorConstructor()) {
          @Override
          public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
            return null;
          }
        };
    TypeAdapter<String> stringTypeAdapter = invalidFactory2.create(gson, TypeToken.of(String.class));
    fail("Should not create TypeAdapter for String with invalid factory.");

    JsonAdapterAnnotationTypeAdapterFactory invalidFactory3 = new JsonAdapterAnnotationTypeAdapterFactory(
        new ConstructorConstructor()) {
          @Override
          public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
            return null;
          }
        };
    TypeAdapter<JsonAdapterAnnotationTypeAdapterFactory> adapterFactoryTypeAdapter =
        invalidFactory3.create(gson, TypeToken.get(JsonAdapterAnnotationTypeAdapterFactory.class));
    fail("Should not create TypeAdapter for JsonAdapterAnnotationTypeAdapterFactory.");
  }

  @Test
  public void testJsonAdapterAnnotationTypeAdapterFactory_NullSafe() {
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(
        new ConstructorConstructor());
    TypeAdapter<String> stringTypeAdapter = factory.create(gson, TypeToken.of(String.class));
    assertNotNull(stringTypeAdapter);

    String input = null;
    String output = stringTypeAdapter.fromJson(input);
    assertNull(output);

    String nullInput = null;
    String output2 = stringTypeAdapter.toJson(nullInput);
    assertNull(output2);
  }
}