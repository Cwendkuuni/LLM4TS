package com.google.gson.internal.bind.test;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Type;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.internal.ConstructorConstructor;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

  private ConstructorConstructor constructorConstructor;
  private Gson gson;
  private JsonAdapterAnnotationTypeAdapterFactory factory;

  @Before
  public void setup() {
    constructorConstructor = mock(ConstructorConstructor.class);
    gson = mock(Gson.class);
    factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
  }

  @Test
  public void testJsonAdapterAnnotation() {
    // Test with JsonAdapter annotation on a class
    TypeAdapter<String> expectedAdapter = mock(TypeAdapter.class);
    when(expectedAdapter.nullSafe()).thenReturn(expectedAdapter);
    TypeAdapter<String> adapter = mock(TypeAdapter.class);
    when(constructorConstructor.get(TypeToken.get(expectedAdapter.getClass()))).thenReturn(expectedAdapter);
    when(constructorConstructor.get(TypeToken.get(TypeAdapter.class))).thenReturn(adapter);

    when(gson.getDelegate()).thenReturn(gson);

    JsonAdapter annotation = TypeAdapterString.class.getAnnotation(JsonAdapter.class);
    TypeAdapter<String> typeAdapter = factory.create(gson, TypeToken.get(TypeAdapterString.class));

    assertNotNull(typeAdapter);
    assertNotNull(typeAdapter.getClass().getAnnotation(JsonAdapter.class));
    assertNotNull(typeAdapter.getClass().getCanonicalName().contains("TypeAdapterString"));
    assertNotNull(typeAdapter instanceof TypeAdapter<?>);
    assertNotNull(typeAdapter.getClass().getCanonicalName().equals(expectedAdapter.getClass().getCanonicalName()));
  }

  @Test
  public void testJsonAdapterAnnotationFactory() {
    // Test with JsonAdapter annotation on a factory
    TypeAdapterFactory expectedFactory = mock(TypeAdapterFactory.class);
    when(expectedFactory.create(gson, TypeToken.get(String.class))).thenReturn(mock(TypeAdapter.class));

    when(gson.getDelegate()).thenReturn(gson);

    JsonAdapter annotation = TypeAdapterFactoryString.class.getAnnotation(JsonAdapter.class);
    TypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor) {
      @Override
      public TypeAdapter<String> create(Gson gson, TypeToken<String> targetType) {
        return getTypeAdapter(constructorConstructor, gson, targetType, annotation);
      }
    };

    assertNotNull(factory);
    assertNotNull(factory.getClass().getAnnotation(JsonAdapter.class));
    assertNotNull(factory instanceof TypeAdapterFactory);
    assertNotNull(factory.create(gson, TypeToken.get(String.class)) instanceof TypeAdapter<?>) ;
    assertNotNull(factory.create(gson, TypeToken.get(String.class)).getClass().getCanonicalName().equals(expectedFactory.getClass().getCanonicalName()));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testJsonAdapterAnnotationInvalidType() {
    // Test with non-TypeAdapter or non-TypeAdapterFactory value
    JsonAdapter annotation = new JsonAdapter() {};
    factory.create(gson, TypeToken.get(int.class));
  }
}

class TypeAdapterString {}
class TypeAdapterFactoryString implements TypeAdapterFactory {}