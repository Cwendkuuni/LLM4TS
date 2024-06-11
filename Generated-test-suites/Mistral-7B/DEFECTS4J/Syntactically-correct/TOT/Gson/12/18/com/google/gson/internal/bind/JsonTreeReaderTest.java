package com.google.gson.internal.bind.test;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonTreeReaderTest {

  @Test
  public void testEmptyDocument() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{}"));
    assertFalse(reader.hasNext());
    reader.close();
  }

  @Test
  public void testEmptyArray() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("[]"));
    assertFalse(reader.hasNext());
    reader.close();
  }

  @Test
  public void testEmptyObject() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{}"));
    assertFalse(reader.hasNext());
    reader.close();
  }

  @Test
  public void testString() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{\"name\": \"John\"}"));
    assertEquals("name", reader.nextName());
    assertEquals("John", reader.nextString());
    reader.close();
  }

  @Test
  public void testBoolean() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{\"boolean\": true}"));
    assertEquals("boolean", reader.nextName());
    assertTrue(reader.nextBoolean());
    reader.close();
  }

  @Test
  public void testNumber() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{\"number\": 123.45}"));
    assertEquals("number", reader.nextName());
    assertEquals(123.45, reader.nextDouble(), 0.001);
    reader.close();
  }

  @Test
  public void testNull() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{null}"));
    assertEquals(JsonNull.INSTANCE, reader.nextNull());
    reader.close();
  }

  @Test
  public void testArray() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("[\"John\", 123, true]"));
    assertFalse(reader.hasNext()); // first skip name "array"
    reader.beginArray();
    assertEquals("John", reader.nextString());
    assertEquals(123, reader.nextInt());
    assertTrue(reader.nextBoolean());
    reader.endArray();
    reader.close();
  }

  @Test
  public void testObject() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{" +
        "\"name\": \"John\"," +
        "\"age\": 30," +
        "\"isMarried\": true," +
        "\"children\": [" +
        "{\"name\": \"Mike\", \"age\": 10}," +
        "{\"name\": \"Anna\", \"age\": 5}" +
        "]" +
        "}"));
    reader.promoteNameToValue(); // skip "name" name
    assertEquals(30, reader.nextInt());
    reader.beginObject();
    assertEquals("name", reader.nextName());
    assertEquals("Mike", reader.nextString());
    assertEquals(10, reader.nextInt());
    reader.endObject();
    reader.beginObject();
    assertEquals("name", reader.nextName());
    assertEquals("Anna", reader.nextString());
    assertEquals(5, reader.nextInt());
    reader.endObject();
    reader.close();
  }

  @Test(expected = IllegalStateException.class)
  public void testClosedReader() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{\"name\": \"John\"}"));
    reader.close();
    reader.nextString();
  }

  @Test(expected = IllegalStateException.class)
  public void testReadAfterEndArray() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("[1, 2, 3]"));
    reader.beginArray();
    reader.endArray();
    reader.nextInt();
  }

  @Test(expected = IllegalStateException.class)
  public void testReadAfterEndObject() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{\"name\": \"John\"}"));
    reader.beginObject();
    reader.endObject();
    reader.nextString();
  }
}