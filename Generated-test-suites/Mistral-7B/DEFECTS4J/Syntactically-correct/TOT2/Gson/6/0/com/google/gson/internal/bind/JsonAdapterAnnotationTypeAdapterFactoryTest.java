package com.google.gson.internal.bind.test;

import static org.junit.Assert.*;
import org.junit.Test;

import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;
import org.mockito.Mockito;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

  @Test
  public void testCreate_ValidTypeAdapterAnnotation_ReturnsTypeAdapterInstance() {
    // Arrange
    Gson gson = Mockito.mock(Gson.class);
    TypeToken<String> targetType = new TypeToken<String>() {};
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(Mockito.mock(ConstructorConstructor.class));
    JsonAdapter jsonAdapterAnnotation = Mockito.mock(JsonAdapter.class);

    when(targetType.getRawType().getAnnotation(JsonAdapter.class)).thenReturn(jsonAdapterAnnotation);
    Class<TypeAdapter<String>> typeAdapterClass = Mockito.mock(Class.class);
    when(typeAdapterClass.newInstance()).thenReturn(Mockito.mock(TypeAdapter.class));
    when(jsonAdapterAnnotation.value()).thenReturn(typeAdapterClass);

    // Act
    TypeAdapter<String> typeAdapter = factory.create(gson, targetType);

    // Assert
    assertNotNull(typeAdapter);
  }

  @Test
  public void testGetTypeAdapter_TypeAdapterClass_ReturnsInstanceOfTypeAdapter() {
    // Arrange
    ConstructorConstructor constructorConstructor = Mockito.mock(ConstructorConstructor.class);
    Gson gson = Mockito.mock(Gson.class);
    TypeToken<String> fieldType = new TypeToken<String>() {};
    JsonAdapter jsonAdapterAnnotation = Mockito.mock(JsonAdapter.class);
    Class<TypeAdapter<String>> typeAdapterClass = Mockito.mock(Class.class);

    when(constructorConstructor.get(TypeToken.get(typeAdapterClass))).thenReturn(Mockito.mock(TypeAdapter.class));
    when(jsonAdapterAnnotation.value()).thenReturn(typeAdapterClass);

    // Act
    TypeAdapter<String> typeAdapter = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, jsonAdapterAnnotation);

    // Assert
    assertEquals(typeAdapterClass.newInstance(), typeAdapter);
  }

  @Test
  public void testCreate_ValidTypeAdapterAnnotation_ReturnsTypeAdapterInstance_TypeAdapterFactory() {
    // Arrange
    ConstructorConstructor constructorConstructor = Mockito.mock(ConstructorConstructor.class);
    Gson gson = Mockito.mock(Gson.class);
    TypeToken<Integer> targetType = new TypeToken<Integer>() {};
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    JsonAdapter jsonAdapterAnnotation = Mockito.mock(JsonAdapter.class);

    when(targetType.getRawType().getAnnotation(JsonAdapter.class)).thenReturn(jsonAdapterAnnotation);
    Class<TypeAdapter<Integer>> typeAdapterClass = Mockito.mock(Class.class);
    when(typeAdapterClass.newInstance()).thenReturn(Mockito.mock(TypeAdapter.class));
    when(jsonAdapterAnnotation.value()).thenReturn(typeAdapterClass);

    // Act
    TypeAdapter<Integer> typeAdapter = factory.create(gson, targetType);

    // Assert
    assertNotNull(typeAdapter);
  }

  @Test
  public void testGetTypeAdapter_TypeAdapterFactoryClass_ReturnsInstanceOfTypeAdapterFactory() {
    // Arrange
    ConstructorConstructor constructorConstructor = Mockito.mock(ConstructorConstructor.class);
    Gson gson = Mockito.mock(Gson.class);
    TypeToken<Number> fieldType = new TypeToken<Number>() {};
    JsonAdapter jsonAdapterAnnotation = Mockito.mock(JsonAdapter.class);
    Class<TypeAdapterFactory> typeAdapterFactoryClass = Mockito.mock(Class.class);

    when(typeAdapterFactoryClass.newInstance()).thenReturn(Mockito.mock(TypeAdapterFactory.class));
    when(jsonAdapterAnnotation.value()).thenReturn(typeAdapterFactoryClass);

    // Act
    TypeAdapterFactory typeAdapterFactory = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, jsonAdapterAnnotation);

    // Assert
    assertEquals(typeAdapterFactoryClass.newInstance(), typeAdapterFactory);
  }
}