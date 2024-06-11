
package com.example.junit4testcases;

import static org.junit.Assert.*;
import com.google.gson.*;
import com.google.gson.internal.*;
import org.junit.Test;

public class JsonTreeWriterTest {

  @Test
  public void testJsonTreeWriterConstructor() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
  }

  @Test
  public void testGet() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    JsonElement jsonElement = jsonTreeWriter.get();
    assertEquals(JsonNull.INSTANCE, jsonElement);
  }

  @Test
  public void testJsonTreeWriterEmptyArray() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginArray();
    jsonTreeWriter.endArray();
    JsonElement jsonElement = jsonTreeWriter.get();
    JsonArray jsonArray = (JsonArray) jsonElement;
    assertEquals(0, jsonArray.size());
  }

  @Test
  public void testJsonTreeWriterSingleStringValue() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginArray();
    jsonTreeWriter.value("hello");
    jsonTreeWriter.endArray();
    JsonElement jsonElement = jsonTreeWriter.get();
    JsonArray jsonArray = (JsonArray) jsonElement;
    assertEquals(1, jsonArray.size());
    JsonPrimitive jsonPrimitive = (JsonPrimitive) jsonArray.get(0);
    assertEquals("hello", jsonPrimitive.getAsString());
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonTreeWriterBeginArrayAfterEndArray() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginArray();
    jsonTreeWriter.endArray();
    jsonTreeWriter.beginArray();
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonTreeWriterBeginObjectAfterEndObject() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.endObject();
    jsonTreeWriter.beginObject();
  }

  @Test
  public void testJsonTreeWriterEmptyObject() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.endObject();
    JsonElement jsonElement = jsonTreeWriter.get();
    JsonObject jsonObject = (JsonObject) jsonElement;
    assertTrue(jsonObject.entrySet().isEmpty());
  }

  @Test
  public void testJsonTreeWriterSingleKeyValuePair() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("key");
    jsonTreeWriter.value("value");
    jsonTreeWriter.endObject();
    JsonElement jsonElement = jsonTreeWriter.get();
    JsonObject jsonObject = (JsonObject) jsonElement;
    JsonPrimitive jsonPrimitive = jsonObject.get("key");
    assertEquals("value", jsonPrimitive.getAsString());
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonTreeWriterNameAfterEndObject() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.endObject();
    jsonTreeWriter.name("key");
  }

  @Test
  public void testJsonTreeWriterClose() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    JsonElement jsonElement = jsonTreeWriter.get();
    assertEquals(JsonNull.INSTANCE, jsonElement);
    jsonTreeWriter.close();
    try {
      jsonTreeWriter.get();
      fail();
    } catch (IOException e) {
      // Expected
    }
  }
}