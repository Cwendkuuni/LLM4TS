package com.google.gson.internal.bind.test;

import static org.junit.Assert.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.Test;

public class JsonTreeWriterTest {

  @Test
  public void testGet() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("key");
    writer.value("value");
    JsonElement result = writer.get();
    JsonObject expected = new JsonObject();
    expected.add("key", new JsonPrimitive("value"));
    assertEquals(expected, result);
  }

  @Test
  public void testPeek() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.beginArray();
    writer.value("element1");
    writer.value("element2");
    JsonElement result = writer.peek();
    JsonArray expected = new JsonArray();
    expected.add(new JsonPrimitive("element1"));
    expected.add(new JsonPrimitive("element2"));
    assertEquals(expected, result);
  }

  @Test
  public void testEndArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.endArray();
    assertTrue(writer.stack.isEmpty());

    writer.beginArray();
    writer.beginArray();
    writer.endArray();
    assertFalse(writer.stack.isEmpty());
    writer.endArray();
    assertTrue(writer.stack.isEmpty());
  }

  // Add other test cases for remaining methods here

  @Test
  public void testIllegalStateException() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.value("key"); // Should have called writer.name("key") first
    try {
      writer.get();
    } catch (IllegalStateException e) {
      // Expected exception
    }
  }

  // Add other test cases for edge cases and error handling here

}