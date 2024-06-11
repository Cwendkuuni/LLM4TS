package com.example.junit4;

import com.google.gson.Gson;
import com.google.gson.JsonAdapter;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeToken;
import com.google.gson.annotations.JsonAdapter;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

 private Gson gson;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 gson = new GsonBuilder().setPrettyPrinting().create();
 }

 @Test
 public void testTypeAdapter() {
 // Test with a JsonAdapter annotated class.
 JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(
 new ConstructorConstructor());
 TypeAdapter<String> stringAdapter = factory.create(gson, new TypeToken<String>() {});
 assertEquals("\"hello world\"", stringAdapter.toJson(new StringAdapter("hello world")));

 // Test with null input.
 assertEquals("null", stringAdapter.toJson(null));
 }

 @Test
 public void testTypeAdapterFactory() {
 // Test with a JsonAdapter annotated TypeAdapterFactory.
 JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(
 new ConstructorConstructor());
 TypeAdapterFactory adapterFactory = new CustomTypeAdapterFactory();
 TypeAdapter<Integer> integerAdapter = factory.create(gson, new TypeToken<Integer>() {});
 TypeAdapter<String> stringAdapter = adapterFactory.create(gson, new TypeToken<String>() {});
 assertEquals("42", integerAdapter.toJson(42));
 assertEquals("\"abc\"", stringAdapter.toJson("abc"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidJsonAdapter() {
 JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(
 new ConstructorConstructor());
 TypeToken<Object> objectType = new TypeToken<Object>() {};
 JsonAdapter annotation = new Object() {}.getClass().getAnnotation(JsonAdapter.class);
 factory.create(gson, objectType);
 }
}

class CustomTypeAdapterFactory implements TypeAdapterFactory {
 @Override
 public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> targetType) {
 return null;
 }
}

class StringAdapter {
 // A simple class annotated with @JsonAdapter.
 @JsonAdapter(StringAdapter.class)
 public static class Adapter {
 // Empty adapter class.
 }
}

class AdapterTest {
 // A test class to verify the @JsonAdapter annotation.
 @JsonAdapter(StringAdapter.Adapter.class)
 public String toString() {
 return "hello world";
 }
}