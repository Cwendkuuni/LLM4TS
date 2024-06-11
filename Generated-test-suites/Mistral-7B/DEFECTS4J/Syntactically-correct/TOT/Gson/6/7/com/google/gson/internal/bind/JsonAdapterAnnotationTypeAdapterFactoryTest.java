package com.google.gson.internal.bind.test;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;
import org.junit.Assert;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

  private static final Gson GSON = new Gson();
  private static final ConstructorConstructor CONSTRUCTOR_CONSTRUCTOR = mock(ConstructorConstructor.class);

  @Test
  public void testJsonAdapterTypeAdapter() {
    // Given
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(CONSTRUCTOR_CONSTRUCTOR);
    JsonAdapter jsonAdapterAnnotation = new JsonAdapterAnnotationTypeAdapterFactory.JsonAdapter() {
      @JsonAdapter(MyTypeAdapter.class)
      public static class MyType {}
    };
    MyTypeAdapter myTypeAdapter = mock(MyTypeAdapter.class);
    when(CONSTRUCTOR_CONSTRUCTOR.get(TypeToken.get(MyTypeAdapter.class))).thenReturn(myTypeAdapter);

    // When
    TypeAdapter<MyType> typeAdapter = factory.create(GSON, TypeToken.get(MyType.class));

    // Then
    Assert.assertNotNull(typeAdapter);
    Assert.assertTrue(TypeAdapter.class.isAssignableFrom(typeAdapter.getClass()));
    Assert.assertEquals(MyTypeAdapter.class, typeAdapter.getClass().getComponentType().getComponentType());
  }

  @Test
  public void testJsonAdapterFactoryTypeAdapter() {
    // Given
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(CONSTRUCTOR_CONSTRUCTOR);
    JsonAdapterAnnotationTypeAdapterFactory.JsonAdapterFactory jsonAdapterFactoryAnnotation = new JsonAdapterAnnotationTypeAdapterFactory.JsonAdapterFactory() {
      @JsonAdapter(MyTypeAdapterFactory.class)
      public static class MyType {}
    };

    MyTypeAdapterFactory myTypeAdapterFactory = mock(MyTypeAdapterFactory.class);
    when(CONSTRUCTOR_CONSTRUCTOR.get(TypeToken.get(MyTypeAdapterFactory.class))).thenReturn(myTypeAdapterFactory);

    // When
    TypeAdapterFactory typeAdapterFactory = factory.create(GSON, TypeToken.get(MyType.class));
    TypeAdapter<MyType> typeAdapter = typeAdapterFactory.create(GSON, TypeToken.get(MyType.class));

    // Then
    Assert.assertNotNull(typeAdapter);
    Assert.assertTrue(TypeAdapter.class.isAssignableFrom(typeAdapter.getClass()));
    Assert.assertEquals(MyTypeAdapter.class, typeAdapter.getClass().getComponentType().getComponentType());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testJsonAdapterInvalidValue() {
    // Given
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(CONSTRUCTOR_CONSTRUCTOR);
    JsonAdapterAnnotationTypeAdapterFactory.JsonAdapter jsonAdapterAnnotation = new JsonAdapterAnnotationTypeAdapterFactory.JsonAdapter() {
      @JsonAdapter(String.class)
      public static class MyType {}
    };

    // When and Then
    factory.create(GSON, TypeToken.get(MyType.class));
  }
}