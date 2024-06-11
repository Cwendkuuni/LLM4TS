package com.google.gson.internal.bind.test;

import static org.junit.Assert.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JsonTreeReaderTest {

  @org.junit.Test
  public void testJsonTreeReaderEmpty() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonParser.parseReader(""));
    assertFalse(reader.hasNext());
    reader.close();
  }

  @org.junit.Test
  public void testJsonTreeReaderNull() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonParser.parseReader("null"));
    assertNull(reader.nextValue());
    reader.close();
  }

  @org.junit.Test
  public void testJsonTreeReaderBoolean() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonParser.parseReader("true"));
    assertTrue(reader.nextBoolean());
    reader.close();
  }

  @org.junit.Test
  public void testJsonTreeReaderNumber() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonParser.parseReader("42"));
    assertEquals(42, reader.nextInt());
    reader.close();
  }

  @org.junit.Test
  public void testJsonTreeReaderString() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonParser.parseReader("\"Hello World\""));
    assertEquals("Hello World", reader.nextString());
    reader.close();
  }

  @org.junit.Test
  public void testJsonTreeReaderArray() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonParser.parseReader("[1, 2, 3]"));
    JsonElement element = reader.nextElement();
    assertInstanceOf(JsonArray.class, element);
    JsonArray jsonArray = (JsonArray) element;
    assertEquals(3, jsonArray.size());
    for (int i = 0; i < jsonArray.size(); i++) {
      assertEquals(i + 1, reader.nextInt());
    }
    reader.close();
  }

  @org.junit.Test
  public void testJsonTreeReaderNestedArray() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonParser.parseReader("[1, [2, 3], 4]]"));
    JsonElement element = reader.nextElement();
    assertInstanceOf(JsonArray.class, element);
    JsonArray jsonArray = (JsonArray) element;
    assertEquals(3, jsonArray.size());
    JsonElement firstElement = jsonArray.get(0);
    assertEquals(1, reader.nextInt());
    JsonElement secondElement = jsonArray.get(1);
    assertInstanceOf(JsonArray.class, secondElement);
    JsonArray nestedJsonArray = (JsonArray) secondElement;
    assertEquals(2, nestedJsonArray.size());
    for (int i = 0; i < nestedJsonArray.size(); i++) {
      assertEquals(i + 2, reader.nextInt());
    }
    assertEquals(4, reader.nextInt());
    reader.close();
  }

  @org.junit.Test
  public void testJsonTreeReaderObject() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonParser.parseReader("{\"name\": \"John\", \"age\": 30}"));
    assertEquals("name", reader.nextName());
    assertEquals("John", reader.nextString());
    assertEquals("age", reader.nextName());
    assertEquals(30, reader.nextInt());
    reader.close();
  }

  @org.junit.Test
  public void testJsonTreeReaderNestedObject() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(
        JsonParser.parseReader("{\"name\": {\"first\": \"John\", \"last\": \"Doe\"}}"));
    assertEquals("name", reader.nextName());
    assertInstanceOf(JsonObject.class, reader.peek());
    reader.beginObject();
    assertEquals("first", reader.nextName());
    assertEquals("John", reader.nextString());
    assertEquals("last", reader.nextName());
    assertEquals("Doe", reader.nextString());
    reader.endObject();
    reader.close();
  }

  @org.junit.Test
  public void testJsonTreeReaderSkipValue() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonParser.parseReader("{ \"name\": null }"));
    reader.skipValue();
    reader.nextName();
    assertEquals("name", reader.nextName());
    reader.nextNull();
    assertEquals(0, reader.pathIndices[0]);
    reader.close();
  }

  @org.junit.Test
  public void testJsonTreeReaderPromoteNameToValue() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonParser.parseReader("{ \"name\": null }"));
    reader.promoteNameToValue();
    assertEquals("null", reader.nextString());
    reader.close();
  }

  @org.junit.Test
  public void testJsonTreeReaderGetPath() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonParser.parseReader("{ \"name\": { \"first\": \"John\", \"last\": \"Doe\" } }"));
    reader.beginObject();
    reader.beginObject();
    assertEquals("first", reader.nextName());
    assertEquals("John", reader.nextString());
    reader.endObject();
    assertEquals("last", reader.nextName());
    assertEquals("[\"name\", \"last\"]", reader.getPath());
    reader.close();
  }
}