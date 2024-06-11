package com.example.junit;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Type;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeToken;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.bind.JsonAdapterAnnotationTypeAdapterFactory;
import com.google.gson.internal.bind.JsonAdapterAnnotationTypeAdapterFactory.JsonAdapter;

/**
 * Tests for {@link JsonAdapterAnnotationTypeAdapterFactory}.
 */
public class JsonAdapterAnnotationTypeAdapterFactoryTest {

  @Test
  public void testJsonAdapterTypeAdapter() {
    Gson gson = new GsonBuilder().create();

    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(
        new ConstructorConstructor());

    Class<String> stringType = String.class;
    JsonAdapter jsonAdapterAnnotation = stringType.getAnnotation(JsonAdapter.class);

    TypeAdapter<String> stringTypeAdapter = factory.create(gson, new TypeToken<String>() {}.getType());

    assertNotNull(stringTypeAdapter);
    assertTrue(TypeAdapter.class.isInstance(stringTypeAdapter));

    String testString = "Test string.";
    String testStringJson = "{\"value\":\"Test string.\"}";

    String deserializedString = gson.fromJson(testStringJson, String.class);
    String serializedString = gson.toJson(testString, String.class);

    String deserializedStringWithAdapter = gson.fromJson(testStringJson, String.class, stringTypeAdapter);
    String serializedStringWithAdapter = gson.toJson(testString, String.class, stringTypeAdapter);

    assertTrue(deserializedString.equals(deserializedStringWithAdapter));
    assertTrue(serializedString.equals(serializedStringWithAdapter));
  }

  @Test
  public void testJsonAdapterTypeAdapterFactory() {
    Gson gson = new GsonBuilder().create();

    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(
        new ConstructorConstructor());

    Class<Integer> integerType = Integer.class;
    JsonAdapter jsonAdapterAnnotation = integerType.getAnnotation(JsonAdapter.class);

    TypeAdapterFactory jsonAdapterFactory = (TypeAdapterFactory) jsonAdapterAnnotation.value();

    TypeAdapter<Integer> integerTypeAdapter = factory.create(gson, new TypeToken<Integer>() {}.getType());

    assertNotNull(integerTypeAdapter);
    assertTrue(TypeAdapter.class.isInstance(integerTypeAdapter));

    Integer testInteger = 123;
    Integer testIntegerJson = 123;

    Integer deserializedInteger = gson.fromJson("123", Integer.class);
    Integer serializedInteger = gson.toJson(testInteger, Integer.class);

    Integer deserializedIntegerWithAdapter = gson.fromJson("{\"value\":123}", Integer.class, integerTypeAdapter);
    Integer serializedIntegerWithAdapter = gson.toJson(testInteger, Integer.class, integerTypeAdapter);

    assertTrue(deserializedInteger.equals(deserializedIntegerWithAdapter));
    assertTrue(serializedInteger.equals(serializedIntegerWithAdapter));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidJsonAdapter() {
    Gson gson = new GsonBuilder().create();

    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(
        new ConstructorConstructor());

    Class<Boolean> booleanType = Boolean.class;
    JsonAdapter jsonAdapterAnnotation = booleanType.getAnnotation(JsonAdapter.class);

    String invalidJsonAdapterValue = "InvalidJsonAdapterValue";

    try {
      factory.create(gson, new TypeToken<Boolean>() {}.getType());
    } catch (Exception e) {
      throw new AssertionError("Expected IllegalArgumentException", e);
    }

    JsonAdapterFactory invalidJsonAdapterFactory = (JsonAdapterFactory) invalidJsonAdapterValue;
  }
}