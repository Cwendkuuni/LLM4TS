package com.google.gson.internal.bind.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonTreeReader;

import org.junit.Test;

public class JsonTreeReaderTest {

  @Test
  public void testEmptyJson() throws IOException {
    String jsonString = "{}";
    JsonTreeReader reader = new JsonTreeReader(JsonParser.parseString(jsonString));
    JsonElement element = reader.peek();
    assertSame(JsonToken.BEGIN_OBJECT, element.getAsJson().getType());
    reader.close();
  }

  @Test
  public void testSimpleJsonObject() throws IOException {
    String jsonString = "{\"name\": \"John\", \"age\": 30}";
    JsonTreeReader reader = new JsonTreeReader(JsonParser.parseString(jsonString));

    assertEquals("name", reader.nextName());
    assertEquals("John", reader.nextString());

    assertEquals("age", reader.nextName());
    assertEquals(30, reader.nextInt());

    reader.nextNull(); // should be ignored

    reader.promoteNameToValue();
    assertEquals("age", reader.nextName());
    assertEquals(30, reader.nextInt());

    reader.close();
  }

  @Test
  public void testSimpleJsonArray() throws IOException {
    String jsonString = "[1, 2, 3]";
    JsonTreeReader reader = new JsonTreeReader(JsonParser.parseString(jsonString));

    reader.beginArray();
    assertEquals(1, reader.nextInt());
    assertEquals(2, reader.nextInt());
    assertEquals(3, reader.nextInt());
    reader.endArray();

    reader.nextNull(); // should be ignored

    reader.promoteNameToValue();
    reader.beginArray();
    assertEquals(1, reader.nextInt());
    assertEquals(2, reader.nextInt());
    assertEquals(3, reader.nextInt());
    reader.endArray();

    reader.close();
  }

  @Test
  public void testNestedJsonObject() throws IOException {
    String jsonString = "{\"name\": \"John\", \"address\": {\"street\": \"Main St\", \"city\": \"Anytown\"}}";
    JsonTreeReader reader = new JsonTreeReader(JsonParser.parseString(jsonString));

    assertEquals("name", reader.nextName());
    assertEquals("John", reader.nextString());

    assertEquals("address", reader.nextName());
    reader.beginObject();

    assertEquals("street", reader.nextName());
    assertEquals("Main St", reader.nextString());

    assertEquals("city", reader.nextName());
    assertEquals("Anytown", reader.nextString());

    reader.endObject();

    reader.nextNull(); // should be ignored

    reader.promoteNameToValue();
    reader.beginObject();
    assertEquals("address", reader.nextName());
    reader.beginObject();
    assertEquals("street", reader.nextName());
    assertEquals("Main St", reader.nextString());
    assertEquals("city", reader.nextName());
    assertEquals("Anytown", reader.nextString());
    reader.endObject();
    reader.endObject();

    reader.close();
  }

  @Test
  public void testNestedJsonArray() throws IOException {
    String jsonString = "[1, [2, 3], [4, [5, 6]]]";
    JsonTreeReader reader = new JsonTreeReader(JsonParser.parseString(jsonString));

    reader.beginArray();
    assertEquals(1, reader.nextInt());

    reader.beginArray();
    assertEquals(2, reader.nextInt());
    reader.endArray();

    reader.beginArray();
    assertEquals(4, reader.nextInt());
    reader.beginArray();
    assertEquals(5, reader.nextInt());
    reader.endArray();
    assertEquals(6, reader.nextInt());
    reader.endArray();

    reader.nextNull(); // should be ignored

    reader.promoteNameToValue();
    reader.beginArray();
    assertEquals(1, reader.nextInt());

    reader.beginArray();
    assertEquals(2, reader.nextInt());
    reader.endArray();

    reader.beginArray();
    assertEquals(4, reader.nextInt());
    reader.beginArray();
    assertEquals(5, reader.nextInt());
    reader.endArray();
    assertEquals(6, reader.nextInt());
    reader.endArray();

    reader.close();
  }

  @Test
  public void testJsonNull() throws IOException {
    String jsonString = "{\"name\": null}";
    JsonTreeReader reader = new JsonTreeReader(JsonParser.parseString(jsonString));

    assertEquals("name", reader.nextName());
    assertNull(reader.nextString());

    reader.promoteNameToValue();
    assertNull(reader.nextString());

    reader.close();
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonReaderClosed() throws IOException {
    String jsonString = "{}";
    JsonTreeReader reader = new JsonTreeReader(JsonParser.parseString(jsonString));
    reader.close();
    reader.nextInt();
  }
}