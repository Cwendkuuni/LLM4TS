package com.google.gson.internal.bind.test;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Type;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

  private ConstructorConstructor constructorConstructor;
  private Gson gson;
  private TypeAdapterFactory typeAdapterFactory;

  @Before
  public void setup() {
    constructorConstructor = new ConstructorConstructor();
    gson = new GsonBuilder().create();
    typeAdapterFactory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
  }

  @Test
  public void testJsonAdapter() {
    // Given
    JsonAdapter annotation = new JsonAdapterAnnotationTypeAdapterFactory.JsonAdapter() {
      Class<?> type = String.class;
      Class<?> value = StringSerializer.class;
    };

    Type fieldType = new TypeToken<String>() {}.getType();

    // When
    TypeAdapter<String> typeAdapter = typeAdapterFactory.create(gson, new TypeToken<String>() {}.getType());

    // Then
    assertNotNull(typeAdapter);
    TypeAdapter<?> expectedTypeAdapter = mock(StringSerializer.class);
    when(expectedTypeAdapter.nullSafe()).thenReturn(expectedTypeAdapter);
    assertSame(expectedTypeAdapter, typeAdapter);
  }

  @Test
  public void testJsonAdapterFactory() {
    // Given
    JsonAdapterFactory annotation = new JsonAdapterAnnotationTypeAdapterFactory.JsonAdapterFactory() {
      Class<?> type = Number.class;
      Class<?> value = NumberSerializer.class;
    };

    Type fieldType = new TypeToken<Number>() {}.getType();

    // When
    TypeAdapter<Number> typeAdapter = typeAdapterFactory.create(gson, fieldType);

    // Then
    assertNotNull(typeAdapter);
    TypeAdapterFactory expectedTypeAdapterFactory = mock(NumberSerializer.class);
    when(expectedTypeAdapterFactory.create(gson, fieldType)).thenReturn(new NumberSerializer());
    verify(expectedTypeAdapterFactory).create(gson, fieldType);
    assertEquals(expectedTypeAdapterFactory, typeAdapter.getDelegate());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testJsonAdapterInvalidValue() {
    // Given
    JsonAdapter annotation = new JsonAdapterAnnotationTypeAdapterFactory.JsonAdapter() {
      Class<?> type = String.class;
      Class<?> value = Object.class;
    };

    Type fieldType = new TypeToken<String>() {}.getType();

    // When
    typeAdapterFactory.create(gson, fieldType);

    // Then
    fail("Should have thrown an IllegalArgumentException");
  }

  private static class StringSerializer implements TypeAdapter<String> {}
  private static class NumberSerializer implements TypeAdapterFactory {}
}