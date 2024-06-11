package com.example.junit4.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.lang.reflect.Type;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

  @Test
  public void testJsonAdapterAnnotationTypeAdapterFactory() {
    // Test with a valid JsonAdapter
    JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(new ConstructorConstructor());
    TypeAdapter<MyClass> adapter = factory.create(new Gson(), new TypeToken<MyClass>() {}.getType());
    assertNotNull(adapter);

    // Test with a null targetType
    assertNull(factory.create(new Gson(), null));

    // Test with a JsonAdapter on a valid type
    @JsonAdapter(MyJsonAdapter.class)
    static class MyClass {
      private int value;
    }

    TypeAdapter<MyClass> myAdapter = factory.create(new Gson(), new TypeToken<MyClass>() {}.getType());
    assertNotNull(myAdapter);

    // Test with a JsonAdapter on an invalid type
    @JsonAdapter(String.class)
    static class InvalidClass {
      private int value;
    }

    TypeAdapter<InvalidClass> invalidAdapter = factory.create(new Gson(), new TypeToken<InvalidClass>() {}.getType());
    assertNull(invalidAdapter);

    // Test with a JsonAdapter on a void type
    @JsonAdapter(void.class)
    static class VoidClass {
    }

    TypeAdapter<VoidClass> voidAdapter = factory.create(new Gson(), new TypeToken<VoidClass>() {}.getType());
    assertNull(voidAdapter);

    // Test with a JsonAdapter on a primitive type
    @JsonAdapter(int.class)
    static class IntClass {
      private int value;
    }

    TypeAdapter<IntClass> intAdapter = factory.create(new Gson(), new TypeToken<IntClass>() {}.getType());
    assertNull(intAdapter);

    // Test with a JsonAdapter on an array type
    @JsonAdapter(int[].class)
    static class IntArrayClass {
      private int[] values;
    }

    TypeAdapter<IntArrayClass> intArrayAdapter = factory.create(new Gson(), new TypeToken<IntArrayClass>() {}.getType());
    assertNull(intArrayAdapter);

    // Test with a JsonAdapter on a List type
    @JsonAdapter(java.util.List.class)
    static class ListClass<T> {
      private java.util.List<T> values;
    }

    TypeAdapter<ListClass<String>> listAdapter = factory.create(new Gson(), new TypeToken<ListClass<String>>() {}.getType());
    assertNull(listAdapter);

    // Test with a JsonAdapter on a Map type
    @JsonAdapter(java.util.Map.class)
    static class MapClass<K, V> {
      private java.util.Map<K, V> values;
    }

    TypeAdapter<MapClass<String, Integer>> mapAdapter = factory.create(new Gson(), new TypeToken<MapClass<String, Integer>>() {}.getType());
    assertNull(mapAdapter);

    // Test with a JsonAdapter on a custom class
    static class CustomClass {
      private String name;
      private int age;

      public CustomClass() {}

      public CustomClass(String name, int age) {
        this.name = name;
        this.age = age;
      }
    }

    @JsonAdapter(CustomClassJsonAdapter.class)
    static class CustomClassWithJsonAdapter {
      private CustomClass customClass;
    }

    TypeAdapter<CustomClassWithJsonAdapter> customAdapter = factory.create(new Gson(), new TypeToken<CustomClassWithJsonAdapter>() {}.getType());
    assertNotNull(customAdapter);

    CustomClassJsonAdapter customJsonAdapter = new CustomClassJsonAdapter();
    assertNotNull(customJsonAdapter);

    // Test reading from a JsonReader
    JsonReader jsonReader = new JsonReader(new StringReader("{\"name\": \"John\", \"age\": 30}"));
    jsonReader.setLenient(true);
    assertNotNull(customAdapter.read(jsonReader));
  }

  static class StringReader extends java.io.StringReader {
    public StringReader(String string) {
      super(string);
    }
  }

  static class CustomClassJsonAdapter extends TypeAdapter<CustomClass> {
    @Override
    public CustomClass read(JsonReader jsonReader) throws IOException {
      String name = jsonReader.nextString();
      int age = jsonReader.nextInt();
      return new CustomClass(name, age);
    }

    @Override
    public void write(JsonWriter jsonWriter, CustomClass value) throws IOException {
      jsonWriter.value(value.name);
      jsonWriter.value(value.age);
    }
  }
}