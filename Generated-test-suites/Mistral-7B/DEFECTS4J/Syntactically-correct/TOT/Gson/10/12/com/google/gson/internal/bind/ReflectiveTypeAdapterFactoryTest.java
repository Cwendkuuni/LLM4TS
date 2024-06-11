package com.example.junit4.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Excluder;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonAdapter;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.google.gson.stream.StreamWriter;

import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.Adapter;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.BoundField;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.TypeAdapterRuntimeTypeWrapper;

public class ReflectiveTypeAdapterFactoryTest {

  public static void main(String[] args) {
    testSerializedName();
    testAlternateName();
    testJsonAdapter();
    testExcluder();
  }

  private static void testSerializedName() {
    class TestClass {
      @SerializedName("name")
      private String name;
    }

    ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
        new ConstructorConstructor(), new FieldNamingPolicy() {
          @Override public String translateName(Field field) {
            return field.getName();
          }
        }, mock(Excluder.class));

    Gson gson = new GsonBuilder()
        .setFieldNamingPolicy(new FieldNamingPolicy() {
          @Override public String translateName(Field field) {
            return field.getName();
          }
        })
        .setAdapter(FactoryProvider.getProvider(ReflectiveTypeAdapterFactory.class).get())
        .create();

    TestClass testObject = new TestClass();
    testObject.name = "John Doe";

    String json = gson.toJson(testObject);
    System.out.println(json);

    JsonParser parser = new JsonParser();
    JsonElement jsonElement = parser.parse(new InputStreamReader(new ByteArrayInputStream(json.getBytes())));
    JsonObject jsonObject = jsonElement.getAsJsonObject();

    TestClass deserializedObject = gson.fromJson(jsonObject, TestClass.class);
    assertEquals("John Doe", deserializedObject.name);
  }

  private static void testAlternateName() {
    class TestClass {
      @SerializedName({"name", "username"})
      private String name;
    }

    ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
        new ConstructorConstructor(), new FieldNamingPolicy() {
          @Override public String translateName(Field field) {
            return field.getName();
          }
        }, mock(Excluder.class));

    Gson gson = new GsonBuilder()
        .setFieldNamingPolicy(new FieldNamingPolicy() {
          @Override public String translateName(Field field) {
            return field.getName();
          }
        })
        .setAdapter(FactoryProvider.getProvider(ReflectiveTypeAdapterFactory.class).get())
        .create();

    TestClass testObject = new TestClass();
    testObject.name = "John Doe";

    String json = gson.toJson(testObject);
    System.out.println(json);

    JsonParser parser = new JsonParser();
    JsonElement jsonElement = parser.parse(new InputStreamReader(new ByteArrayInputStream(json.getBytes())));
    JsonObject jsonObject = jsonElement.getAsJsonObject();

    TestClass deserializedObject = gson.fromJson(jsonObject, TestClass.class);
    assertEquals("John Doe", deserializedObject.name);
  }

  private static void testJsonAdapter() {
    class TestClass {
      private String name;
      @JsonAdapter(MyJsonAdapter.class)
      private MyCustomType customType;

      public String getName() {
        return name;
      }

      public void setName(String name) {
        this.name = name;
      }

      public MyCustomType getCustomType() {
        return customType;
      }

      public void setCustomType(MyCustomType customType) {
        this.customType = customType;
      }
    }

    class MyCustomType {
      private int value;

      public int getValue() {
        return value;
      }

      public void setValue(int value) {
        this.value = value;
      }
    }

    class MyJsonAdapter extends JsonAdapter<MyCustomType> {
      @Override public MyCustomType read(JsonReader reader)
          throws IOException {
        MyCustomType object = new MyCustomType();
        object.setValue(reader.nextInt());
        return object;
      }

      @Override public void write(JsonWriter writer, MyCustomType value)
          throws IOException {
        writer.value(value.getValue());
      }
    }

    ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
        new ConstructorConstructor(), new FieldNamingPolicy() {
          @Override public String translateName(Field field) {
            return field.getName();
          }
        }, mock(Excluder.class));

    Gson gson = new GsonBuilder()
        .setFieldNamingPolicy(new FieldNamingPolicy() {
          @Override public String translateName(Field field) {
            return field.getName();
          }
        })
        .setAdapter(FactoryProvider.getProvider(ReflectiveTypeAdapterFactory.class).get())
        .create();

    TestClass testObject = new TestClass();
    testObject.name = "John Doe";
    testObject.customType = new MyCustomType();
    testObject.customType.setValue(123);

    String json = gson.toJson(testObject);
    System.out.println(json);

    JsonParser parser = new JsonParser();
    JsonElement jsonElement = parser.parse(new InputStreamReader(new ByteArrayInputStream(json.getBytes())));
    JsonObject jsonObject = jsonElement.getAsJsonObject();

    TestClass deserializedObject = gson.fromJson(jsonObject, TestClass.class);
    assertEquals("John Doe", deserializedObject.name);
    assertEquals(123, deserializedObject.customType.getValue());
  }

  private static void testExcluder() {
    class TestClass {
      @Exclude private int excludedField;
      private String includedField;

      public int getExcludedField() {
        return excludedField;
      }

      public void setExcludedField(int excludedField) {
        this.excludedField = excludedField;
      }

      public String getIncludedField() {
        return includedField;
      }

      public void setIncludedField(String includedField) {
        this.includedField = includedField;
      }
    }

    ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
        new ConstructorConstructor(), new FieldNamingPolicy() {
          @Override public String translateName(Field field) {
            return field.getName();
          }
        }, mock(Excluder.class));

    Gson gson = new GsonBuilder()
        .setFieldNamingPolicy(new FieldNamingPolicy() {
          @Override public String translateName(Field field) {
            return field.getName();
          }
        })
        .setAdapter(FactoryProvider.getProvider(ReflectiveTypeAdapterFactory.class).get())
        .create();

    TestClass testObject = new TestClass();
    testObject.excludedField = 123;
    testObject.includedField = "John Doe";

    String json = gson.toJson(testObject);
    System.out.println(json);

    JsonParser parser = new JsonParser();
    JsonElement jsonElement = parser.parse(new InputStreamReader(new ByteArrayInputStream(json.getBytes())));
    JsonObject jsonObject = jsonElement.getAsJsonObject();

    TestClass deserializedObject = gson.fromJson(jsonObject, TestClass.class);
    assertEquals("John Doe", deserializedObject.getIncludedField());
    assertEquals(0, deserializedObject.getExcludedField());
  }

  private static class FactoryProvider {
    public static <T> TypeAdapterFactory getProvider(Class<T> clazz) {
      return (TypeAdapterFactory) clazz.cast(ReflectiveTypeAdapterFactory.class);
    }
  }

  private static void assertEquals(Object expected, Object actual) {
    if (expected == null && actual == null) {
      return;
    }
    if (expected == null || actual == null || !expected.getClass().isInstance(actual)) {
      throw new AssertionError("Expected: " + expected + ", actual: " + actual);
    }
    assertEquals(expected.toString(), actual.toString());
  }

  @SuppressWarnings("unchecked")
  private static void assertEquals(String expectedJson, String actualJson) {
    Gson gson = new Gson();
    Map<String, Object> expectedMap = gson.fromJson(expectedJson, Map.class);
    Map<String, Object> actualMap = gson.fromJson(actualJson, Map.class);

    assertEquals(expectedMap.size(), actualMap.size());
    for (Map.Entry<String, Object> entry : expectedMap.entrySet()) {
      String key = entry.getKey();
      Object expectedValue = entry.getValue();
      Object actualValue = actualMap.get(key);
      assertEquals(key, expectedValue.getClass(), actualValue.getClass(), expectedValue, actualValue);
    }
  }
}