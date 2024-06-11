package com.example.junit.test.adapter;

import static org.junit.Assert.*;

import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

  @SuppressWarnings("unchecked")
  static class StringAdapter implements TypeAdapter<String> {}

  @SuppressWarnings("unchecked")
  static class MapListAdapter implements TypeAdapter<Map<String, List<Integer>>> {}

  private static final class JsonAdapterAnnotation {
    @JsonAdapter(StringAdapter.class)
    public static class StringAdapterAnnotation {}

    @JsonAdapter(MapListAdapter.class)
    public static class MapListAdapterAnnotation {}
  }

  @org.junit.Test
  public void testCreate() {
    // Expert 1 test case here
  }

  @org.junit.Test(expected = NullPointerException.class)
  public void testCreateWithNullTargetType() {
    // Expert 1 edge case test here
  }

  @org.junit.Test
  public void testCreateWithNoJsonAdapterAnnotation() {
    // Expert 1 edge case test here
  }

  @org.junit.Test
  public void testGetTypeAdapter() {
    // Expert 1 test case here
  }

  @org.junit.Test(expected = IllegalArgumentException.class)
  public void testGetTypeAdapterWithInvalidValue() {
    // Expert 1 edge case test here
  }

  @org.junit.Test
  public void testCreateWithEmptyConstructor() {
    // Expert 2 edge case test here
  }

  @org.junit.Test
  public void testCreateWithNestedTypeToken() {
    // Expert 2 test case here
  }

  @org.junit.Test
  public void testCreateWithNoRawTypeTargetType() {
    // Expert 3 edge case test here
  }

  @org.junit.Test
  public void testGetTypeAdapterWithNullJsonAdapterAnnotation() {
    // Expert 2 edge case test here
  }

  @org.junit.Test(expected = NullPointerException.class)
  public void testGetTypeAdapterWithNullGson() {
    // Expert 3 edge case test here
  }

  @org.junit.Test(expected = IllegalArgumentException.class)
  public void testGetTypeAdapterWithNullTargetType() {
    // Expert 3 edge case test here
  }

  @org.junit.Test(expected = IllegalArgumentException.class)
  public void testGetTypeAdapterWithNoJsonAdapterAnnotationOnType() {
    // Expected edge case test for methods with no JsonAdapter annotation
  }

  @org.junit.Test
  public void testCreateWithNullConstructorConstructor() {
    // Expected edge case test for constructorConstructor being null
  }
}