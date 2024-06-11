package com.google.gson.internal.bind.test;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;
import org.junit.Before;
import org.junit.Test;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

  private ConstructorConstructor constructorConstructor;
  private Gson gson;
  private TypeAdapterFactory jsonAdapterAnnotationTypeAdapterFactory;

  @Before
  public void setup() {
    constructorConstructor = mock(ConstructorConstructor.class);
    gson = mock(Gson.class);
    jsonAdapterAnnotationTypeAdapterFactory =
        new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
  }

  @Test
  public void testJsonAdapterAnnotationPresent() {
    TypeToken<String> stringTypeToken = new TypeToken<String>() {};
    JsonAdapter annotation = stringTypeToken.getRawType().getAnnotation(JsonAdapter.class);

    when(constructorConstructor.get(TypeToken.get(TypeAdapter.class))).thenReturn(mock(TypeAdapter.class));

    TypeAdapter<String> typeAdapter = jsonAdapterAnnotationTypeAdapterFactory.create(gson, stringTypeToken);

    assertNotNull(typeAdapter);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testJsonAdapterAnnotationMissing() {
    TypeToken<Integer> integerTypeToken = new TypeToken<Integer>() {};

    TypeAdapter<Integer> typeAdapter = jsonAdapterAnnotationTypeAdapterFactory.create(gson, integerTypeToken);
  }

  @Test
  public void testJsonAdapterAnnotationTypeAdapter() {
    TypeToken<MyCustomClass> myCustomClassTypeToken = new TypeToken<MyCustomClass>() {};
    JsonAdapter myCustomClassJsonAdapterAnnotation = myCustomClassTypeToken.getRawType()
        .getAnnotation(JsonAdapter.class);

    when(constructorConstructor.get(TypeToken.get(TypeAdapter.class)))
        .thenReturn(mock(TypeAdapterMyCustomClass.class));

    TypeAdapter<MyCustomClass> typeAdapter = jsonAdapterAnnotationTypeAdapterFactory.create(gson,
        myCustomClassTypeToken);

    assertNotNull(typeAdapter);
  }

  @Test
  public void testJsonAdapterAnnotationTypeAdapterFactory() {
    TypeToken<MyCustomClassList> myCustomClassListTypeToken = new TypeToken<MyCustomClassList>() {};
    JsonAdapter myCustomClassListJsonAdapterAnnotation = myCustomClassListTypeToken.getRawType()
        .getAnnotation(JsonAdapter.class);

    TypeAdapterFactory myCustomClassListTypeAdapterFactory = mock(TypeAdapterFactoryMyCustomClassList.class);

    when(constructorConstructor.get(TypeToken.get(TypeAdapterFactory.class)))
        .thenReturn(myCustomClassListTypeAdapterFactory);
    when(myCustomClassListTypeAdapterFactory.create(gson, myCustomClassTypeToken))
        .thenReturn(mock(TypeAdapterMyCustomClass.class));

    TypeAdapter<MyCustomClassList> typeAdapter = jsonAdapterAnnotationTypeAdapterFactory.create(gson,
        myCustomClassListTypeToken);

    assertNotNull(typeAdapter);
  }
}

class MyCustomClass {
  // Some custom class for testing
}

class MyCustomClassList {
  // Some custom list class for testing
}

class TypeAdapterMyCustomClass implements TypeAdapter<MyCustomClass> {
  // Custom type adapter for MyCustomClass
}

interface TypeAdapterFactoryMyCustomClassList {
  TypeAdapter<MyCustomClass> create(Gson gson, TypeToken<?> fieldType);
}