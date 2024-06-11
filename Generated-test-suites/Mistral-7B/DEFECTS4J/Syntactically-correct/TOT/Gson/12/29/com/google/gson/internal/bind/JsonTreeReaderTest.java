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
  public void testEmptyJson() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.NULL);
    reader.close();
    JsonObject expected = new JsonObject();
    JsonReader expectedReader = new JsonReader(new StringReader(""));
    testReader(reader, expectedReader, expected);
  }

  @Test
  public void testSingleString() throws Exception {
    String json = "\"abc\"";
    JsonTreeReader reader = new JsonTreeReader(new JsonPrimitive(json));
    JsonObject expected = new JsonObject();
    JsonElement stringElement = new JsonPrimitive(json);
    expected.add("", stringElement);
    JsonReader expectedReader = new JsonReader(new StringReader(json));
    testReader(reader, expectedReader, expected);
  }

  @Test
  public void testSingleNumber() throws Exception {
    String json = "42";
    JsonTreeReader reader = new JsonTreeReader(new JsonPrimitive(json));
    JsonObject expected = new JsonObject();
    expected.addProperty("", 42);
    JsonReader expectedReader = new JsonReader(new StringReader(json));
    testReader(reader, expectedReader, expected);
  }

  @Test
  public void testSingleBoolean() throws Exception {
    String json = "true";
    JsonTreeReader reader = new JsonTreeReader(new JsonPrimitive(json));
    JsonObject expected = new JsonObject();
    expected.addProperty("", true);
    JsonReader expectedReader = new JsonReader(new StringReader(json));
    testReader(reader, expectedReader, expected);
  }

  @Test
  public void testSingleNull() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(JsonNull.INSTANCE);
    JsonObject expected = new JsonObject();
    JsonReader expectedReader = new JsonReader();
    testReader(reader, expectedReader, expected);
  }

  @Test
  public void testSingleBooleanValueInObject() throws Exception {
    String json = "{\"bool\":true}";
    JsonTreeReader reader = new JsonTreeReader(new JsonPrimitive(json));
    JsonObject expected = new JsonObject();
    expected.addProperty("bool", true);
    testReader(reader, expectedReader(json), expected);
  }

  @Test
  public void testSingleArray() throws Exception {
    String json = "[42]";
    JsonTreeReader reader = new JsonTreeReader(new JsonPrimitive(json));
    JsonObject expected = new JsonObject();
    JsonArray array = new JsonArray();
    array.add(42);
    expected.add("", array);
    JsonReader expectedReader = new JsonReader(new StringReader(json));
    testReader(reader, expectedReader, expected);
  }

  @Test
  public void testArrayWithMultipleElements() throws Exception {
    String json = "[1, 2, 3]";
    JsonTreeReader reader = new JsonTreeReader(new JsonPrimitive(json));
    JsonObject expected = new JsonObject();
    JsonArray array = new JsonArray();
    array.add(1);
    array.add(2);
    array.add(3);
    expected.add("", array);
    JsonReader expectedReader = new JsonReader(new StringReader(json));
    testReader(reader, expectedReader, expected);
  }

  @Test
  public void testNestedObjects() throws Exception {
    String json = "{\"key\":{\"nestedKey\":\"value\"}}";
    JsonTreeReader reader = new JsonTreeReader(new JsonPrimitive(json));
    JsonObject expected = new JsonObject();
    expected.addProperty("key", new JsonObject());
    JsonObject nested = new JsonObject();
    nested.addProperty("nestedKey", "value");
    expected.getAsJsonObject("key").add("nestedKey", nested);
    JsonReader expectedReader = new JsonReader(new StringReader(json));
    testReader(reader, expectedReader, expected);
  }

  @Test
  public void testNestedArrays() throws Exception {
    String json = "[1, [2, 3], 4]]";
    JsonTreeReader reader = new JsonTreeReader(new JsonPrimitive(json));
    JsonObject expected = new JsonObject();
    JsonArray outer = new JsonArray();
    JsonArray inner = new JsonArray();
    inner.add(2);
    inner.add(3);
    outer.add(1);
    outer.add(inner);
    outer.add(4);
    expected.add("", outer);
    JsonReader expectedReader = new JsonReader(new StringReader(json));
    testReader(reader, expectedReader, expected);
  }

  @Test(expected = IllegalStateException.class)
  public void testClosedReader() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(JsonNull.INSTANCE);
    reader.close();
    reader.nextInt();
  }

  @Test(expected = IllegalStateException.class)
  public void testReadingBeyondEndOfArray() throws Exception {
    String json = "[1, 2]";
    JsonTreeReader reader = new JsonTreeReader(new JsonPrimitive(json));
    reader.beginArray();
    reader.nextInt();
    reader.nextInt();
    reader.nextInt();
  }

  @Test(expected = IllegalStateException.class)
  public void testReadingBeyondEndOfObject() throws Exception {
    String json = "{\"key\":\"value\"}";
    JsonTreeReader reader = new JsonTreeReader(new JsonPrimitive(json));
    reader.beginObject();
    reader.nextName();
    reader.nextName();
  }

  private void testReader(JsonTreeReader actual, JsonReader expected, JsonObject expectedData)
      throws IOException {
    actual.beginObject();
    String path = "";
    while (actual.hasNext()) {
      JsonToken token = actual.peek();
      path += token.isName() ? "." + actual.nextName() : "[" + actual.nextInt() + "]";

      if (token.isBeginArray()) {
        JsonArray array = actual.beginArray();
        JsonElement element = expected.peek();
        JsonArray expectedArray = null;
        if (element.isJsonArray()) {
          expectedArray = element.getAsJsonArray();
        }
        assertEquals(expectedArray.size(), array.size());
        for (int i = 0; i < array.size(); i++) {
          testReader(actual, expected, expectedData.getAsJsonArray().get(i), array.get(i));
        }
        actual.endArray();
      } else if (token.isBeginObject()) {
        JsonObject object = actual.beginObject();
        JsonElement element = expected.peek();
        JsonObject expectedObject = null;
        if (element.isJsonObject()) {
          expectedObject = element.getAsJsonObject();
        }
        assertEquals(expectedObject.size(), object.size());
        for (Map.Entry<String, JsonElement> entry : expectedObject.entrySet()) {
          String key = entry.getKey();
          JsonElement value = entry.getValue();
          String valueKey = key.equals("") ? "" : "." + key;
          testReader(actual, expected, value, object.get(key));
        }
        actual.endObject();
      } else if (token.isNumber()) {
        double actualNumber = actual.nextDouble();
        double expectedNumber = expected.nextDouble();
        assertEquals(expectedNumber, actualNumber, 0.00001);
      } else if (token.isBoolean()) {
        boolean actualBoolean = actual.nextBoolean();
        boolean expectedBoolean = expected.nextBoolean();
        assertEquals(expectedBoolean, actualBoolean);
      } else if (token.isString()) {
        String actualString = actual.nextString();
        String expectedString = expected.nextString();
        assertEquals(expectedString, actualString);
      } else if (token.isNull()) {
        assertNull(actual.nextNull());
        assertNull(expected.nextNull());
      }
    }
    actual.endObject();
    expected.close();
  }

  private JsonReader expectedReader(String json) {
    return new JsonReader(new StringReader(json));
  }
}