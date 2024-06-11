package com.google.gson.internal.bind;

import static org.junit.Assert.*;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.Exclude;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public class ReflectiveTypeAdapterFactoryTest {

  @SuppressWarnings("unchecked")
  private static <T> void testSerializationDeserialization(Class<T> type) throws Exception {
    Gson gson = new Gson();
    TypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
        new ConstructorConstructor(),
        new FieldNamingStrategy() {
          @Override public String translateName(Field f) {
            return f.getName();
          }
        },
        new Excluder() {
          @Override public boolean excludeClass(Class<?> clazz, boolean serialize) {
            return false;
          }

          @Override public boolean excludeField(Field f, boolean serialize) {
            return Modifier.isStatic(f.getModifiers()) || f.isAnnotationPresent(Exclude.class);
          }
        });
    TypeAdapter<T> adapter = factory.create(gson, new TypeToken<T>() {});

    T object = adapter.fromJson(new JsonParser().parse("{}").getAsJsonObject().getAsJsonObject(), null);
    String json = gson.toJson(object);
    T roundTrip = adapter.fromJson(new JsonElement(new StringReader(json)), null);

    assertEquals(object, roundTrip);
  }

  @Test
  public void testReflectiveTypeAdapterFactory_SimpleClass() throws Exception {
    testSerializationDeserialization(SimpleClass.class);
  }

  static class SimpleClass {
    private int intField;
    private String stringField;

    public SimpleClass(int intField, String stringField) {
      this.intField = intField;
      this.stringField = stringField;
    }
  }

  @Test
  public void testReflectiveTypeAdapterFactory_NestedClasses() throws Exception {
    testSerializationDeserialization(NestedClasses.OuterClass.class);
  }

  static class NestedClasses {
    static class OuterClass {
      private InnerClass inner;

      public OuterClass(InnerClass inner) {
        this.inner = inner;
      }

      public InnerClass getInner() {
        return inner;
      }
    }

    static class InnerClass {
      private int intField;

      public InnerClass(int intField) {
        this.intField = intField;
      }
    }
  }

  @Test
  public void testReflectiveTypeAdapterFactory_Collections() throws Exception {
    testSerializationDeserialization(CollectionsClass.class);
  }

  static class CollectionsClass {
    private Map<String, Object> map;
    private List<Object> list;

    public CollectionsClass(Map<String, Object> map, List<Object> list) {
      this.map = map;
      this.list = list;
    }

    public Map<String, Object> getMap() {
      return map;
    }

    public List<Object> getList() {
      return list;
    }
  }

  @Test
  public void testReflectiveTypeAdapterFactory_JsonAdapter() throws Exception {
    testSerializationDeserialization(JsonAdapterClass.class);
  }

  static class JsonAdapterClass {
    private JsonAdapter jsonAdapter;

    public JsonAdapterClass(JsonAdapter jsonAdapter) {
      this.jsonAdapter = jsonAdapter;
    }

    public JsonAdapter getJsonAdapter() {
      return jsonAdapter;
    }
  }

  static class JsonAdapter {
    private String name;

    public JsonAdapter(String name) {
      this.name = name;
    }

    public String getName() {
      return name;
    }
  }
}