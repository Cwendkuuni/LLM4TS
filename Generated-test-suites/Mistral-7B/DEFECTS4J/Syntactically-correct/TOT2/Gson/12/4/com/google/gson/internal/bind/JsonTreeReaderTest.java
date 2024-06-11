package com.google.gson.internal.bind.test;

import static org.junit.Assert.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonTreeReader;

import org.junit.Test;

public class JsonTreeReaderTest {

  @Test
  public void testEmptyJson() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonParser.parseString(""));
    JsonElement element = reader.peek();
    reader.close();
    assertNull(element);
  }

  @Test
  public void testEmptyArray() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonParser.parseString("[]"));
    assertArrayEquals(new Object[]{}, reader.readArray());
    reader.close();
  }

  @Test
  public void testSingleElementArray() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonParser.parseString("[1]"));
    JsonElement element = reader.nextInt();
    assertEquals(1, element.getAsInt());
    reader.close();
  }

  @Test
  public void testSingleElementObject() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonParser.parseString("{\"name\":\"John\"}"));
    String name = reader.nextString();
    assertEquals("John", name);
    reader.close();
  }

  @Test
  public void testComplexJson() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonParser.parseString("{\"name\":\"John\", \"age\":30,\"hobbies\":[\"reading\", \"swimming\"]}"));
    String name = reader.nextString();
    assertEquals("John", name);
    reader.nextInt(); // age
    JsonArray hobbies = reader.readArray();
    assertEquals(2, hobbies.size());
    assertEquals("reading", hobbies.get(0).getAsString());
    assertEquals("swimming", hobbies.get(1).getAsString());
    reader.close();
  }

  @Test(expected = IllegalStateException.class)
  public void testClosedReader() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonParser.parseString("{}"));
    reader.close();
    reader.nextInt();
  }

  @Test(expected = IllegalStateException.class)
  public void testInvalidJson() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonParser.parseString("{name:\"John\"")); // missing '}'
    reader.close();
  }
}