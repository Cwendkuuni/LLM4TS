package com.example.json;

import static org.junit.Assert.*;

import org.junit.Test;

public class JsonTreeWriterTest {
  @Test
  public void testJsonTreeWriterConstruction() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    assertNull(jsonTreeWriter.get());
  }

  @Test
  public void testBeginArray() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginArray();
    jsonTreeWriter.endArray();
    JsonElement result = jsonTreeWriter.get();
    assertTrue(result.isJsonArray());
  }

  @Test
  public void testBeginObject() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name");
    jsonTreeWriter.value("John Doe");
    jsonTreeWriter.endObject();
    JsonElement result = jsonTreeWriter.get();
    assertTrue(result.isJsonObject());
    assertEquals("name", result.getAsJsonObject().entrySet().iterator().next().getKey());
    assertEquals("John Doe", result.getAsJsonObject().entrySet().iterator().next().getValue().getAsString());
  }

  @Test
  public void testInvalidJsonValues() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.value(null);
    // Add tests for empty strings, NaN, Infinity, etc. here
    JsonElement result = jsonTreeWriter.get();
    // Assert that the get() method throws an exception or returns the correct JsonNull, JsonPrimitive, etc.
  }

  @Test
  public void testIllegalStateExceptions() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.value("value"); // No name provided before value
    jsonTreeWriter.name("name");
    jsonTreeWriter.value("John Doe"); // No object on stack
    JsonElement result = jsonTreeWriter.get();
    assertFalse(result.isJsonArray());
    assertFalse(result.isJsonObject());
    // Assert that exceptions are thrown as expected
  }

  @Test
  public void testClose() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name");
    jsonTreeWriter.value("John Doe");
    jsonTreeWriter.endObject();
    jsonTreeWriter.close();
    JsonElement result = jsonTreeWriter.get();
    assertEquals(JsonTreeWriter.SENTINEL_CLOSED, result);
  }
}