package com.google.gson.internal.bind.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonReader;
import com.google.gson.JsonToken;

import org.junit.Test;

public class JsonTreeReaderTest {

  @Test
  public void testEmpty() throws IOException {
    JsonReader reader = new JsonTreeReader(JsonFactory.createParser(""));
    assertFalse(reader.hasNext());
    reader.close();
  }

  @Test
  public void testNullElement() throws IOException {
    JsonReader reader = new JsonTreeReader(JsonFactory.createParser("null"));
    assertNull(reader.nextName());
    reader.close();
  }

  @Test
  public void testEmptyArray() throws IOException {
    JsonReader reader = new JsonTreeReader(JsonFactory.createParser("[]"));
    assertTrue(reader.hasNext());
    reader.beginArray();
    assertFalse(reader.hasNext());
    reader.endArray();
    reader.close();
  }

  @Test
  public void testSingleElementArray() throws IOException {
    JsonReader reader = new JsonTreeReader(JsonFactory.createParser("[\'abc\']"));
    assertTrue(reader.hasNext());
    reader.beginArray();
    reader.beginArray();
    String name = reader.nextName();
    reader.endArray();
    reader.endArray();
    assertEquals("abc", name);
    reader.close();
  }

  @Test
  public void testMultipleElementArray() throws IOException {
    JsonReader reader = new JsonTreeReader(JsonFactory.createParser("[\'abc\', 123, true]"));
    assertTrue(reader.hasNext());
    reader.beginArray();
    reader.beginArray();
    String name1 = reader.nextName();
    reader.nextString();
    reader.nextBoolean();
    reader.nextInt();
    reader.endArray();
    reader.endArray();
    reader.close();
    assertEquals("abc", name1);
  }

  @Test
  public void testEmptyObject() throws IOException {
    JsonReader reader = new JsonTreeReader(JsonFactory.createParser("{}"));
    assertTrue(reader.hasNext());
    reader.beginObject();
    assertFalse(reader.hasNext());
    reader.endObject();
    reader.close();
  }

  @Test
  public void testSingleElementObject() throws IOException {
    JsonReader reader = new JsonTreeReader(JsonFactory.createParser("{\"name\": \"abc\"}"));
    assertTrue(reader.hasNext());
    reader.beginObject();
    reader.beginObject();
    String name = reader.nextName();
    reader.nextString();
    reader.endObject();
    reader.endObject();
    assertEquals("name", name);
    reader.close();
  }

  @Test
  public void testNestedObject() throws IOException {
    JsonReader reader = new JsonTreeReader(JsonFactory.createParser("{\"outer\": {\"inner\": 123}}"));
    assertTrue(reader.hasNext());
    reader.beginObject();
    String outerName = reader.nextName();
    reader.beginObject();
    reader.beginObject();
    reader.nextName();
    reader.nextInt();
    reader.endObject();
    reader.endObject();
    reader.endObject();
    assertEquals("outer", outerName);
    reader.close();
  }

  @Test
  public void testNestedArray() throws IOException {
    JsonReader reader = new JsonTreeReader(JsonFactory.createParser("{[1, 2], [3, 4]}"));
    assertTrue(reader.hasNext());
    reader.beginObject();
    reader.beginArray();
    reader.beginArray();
    reader.nextInt();
    reader.nextInt();
    reader.endArray();
    reader.endArray();
    reader.beginArray();
    reader.beginArray();
    reader.nextInt();
    reader.nextInt();
    reader.endArray();
    reader.endArray();
    reader.close();
  }

  @Test
  public void testVariousTypes() throws IOException {
    JsonReader reader = new JsonTreeReader(JsonFactory.createParser("{" +
        "\"string\": \"abc\"," +
        "\"number\": 123.0," +
        "\"boolean\": true," +
        "\"null\": null" +
        "}"));
    assertTrue(reader.hasNext());
    reader.beginObject();
    reader.beginObject();
    reader.nextName();
    String string = reader.nextString();
    reader.nextName();
    reader.nextDouble();
    reader.nextName();
    reader.nextBoolean();
    reader.nextNull();
    reader.endObject();
    reader.endObject();
    assertEquals("string", reader.currentName());
    assertEquals("abc", string);
    reader.nextBoolean();
    assertTrue(reader.hasNext());
    reader.endObject();
    reader.close();
  }

  @Test(expected = AssertionError.class)
  public void testClosedReader() throws IOException {
    JsonReader reader = new JsonTreeReader(JsonFactory.createParser("{}"));
    reader.close();
    reader.nextString();
  }

  @Test(expected = IllegalStateException.class)
  public void testInvalidJson() throws IOException {
    JsonReader reader = new JsonTreeReader(JsonFactory.createParser("{abc: \"def\""));
    reader.nextName();
  }
}