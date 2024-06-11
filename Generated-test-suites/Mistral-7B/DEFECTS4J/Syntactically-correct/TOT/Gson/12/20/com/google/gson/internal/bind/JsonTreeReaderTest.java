package com.google.gson.internal.bind.test;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class JsonTreeReaderTest {

  @Test
  public void testEmptyJson() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{}"));
    assertFalse(reader.hasNext());
    reader.close();
  }

  @Test
  public void testSingleObject() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{'name': 'John'}"));
    assertEquals("name", reader.nextName());
    assertEquals("John", reader.nextString());
    reader.nextNull();
    reader.close();
  }

  @Test
  public void testSingleArray() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("[1, 2, 3]"));;
    reader.beginArray();
    assertEquals(1, reader.nextInt());
    reader.nextNull(); // skip first null
    assertEquals(2, reader.nextInt());
    reader.nextNull(); // skip second null
    assertEquals(3, reader.nextInt());
    reader.endArray();
    reader.close();
  }

  @Test
  public void testNestedObjectsAndArrays() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{" +
        "  'obj1': {" +
        "      'name': 'John'," +
        "      'age': 30," +
        "      'array': [1, 2, 3]" +
        "  }," +
        "  'obj2': [" +
        "      {'name': 'Alice', 'age': 25}" +
        "  ]" +
        "}"));

    reader.beginObject();
    assertEquals("obj1", reader.nextName());
    reader.beginObject();
    assertEquals("name", reader.nextName());
    assertEquals("John", reader.nextString());
    reader.nextNull();
    assertEquals("age", reader.nextName());
    assertEquals(30, reader.nextInt());
    reader.beginArray();
    assertEquals("array", reader.nextName());
    assertEquals(1, reader.nextInt());
    reader.nextNull();
    assertEquals(2, reader.nextInt());
    reader.nextNull();
    assertEquals(3, reader.nextInt());
    reader.endArray();
    reader.endObject(); // end obj1
    reader.beginArray(); // begin obj2
    reader.beginObject();
    assertEquals("name", reader.nextName());
    assertEquals("Alice", reader.nextString());
    reader.nextNull();
    assertEquals("age", reader.nextName());
    assertEquals(25, reader.nextInt());
    reader.endObject(); // end obj2
    reader.endArray();
    reader.close();
  }

  @Test(expected = IllegalStateException.class)
  public void testClosedReader() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{}"));
    reader.close();
    reader.nextInt(); // this should throw an exception
  }

  @Test
  public void testReadPrimitiveTypes() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{" +
        "  'bool': true," +
        "  'int': 42," +
        "  'long': 42L," +
        "  'float': 1.23," +
        "  'double': 1.234" +
        "}"));

    reader.beginObject();
    assertTrue(reader.nextBoolean());
    reader.nextNull();
    assertEquals(42, reader.nextInt());
    reader.nextNull();
    assertEquals(42L, reader.nextLong());
    reader.nextNull();
    assertEquals(1.23f, reader.nextFloat(), 0.01f);
    reader.nextNull();
    assertEquals(1.234, reader.nextDouble(), 0.001);
    reader.endObject();
    reader.close();
  }

  @Test
  public void testReadNull() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{'null': null}"));
    reader.beginObject();
    assertNull(reader.nextString());
    reader.nextNull();
    reader.nextNull(); // second null is the value
    reader.endObject();
    reader.close();
  }

  @Test
  public void testReadEmptyArray() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("[]"));
    reader.beginArray();
    reader.endArray();
    reader.close();
  }

  @Test
  public void testReadEmptyObject() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{}"));
    reader.beginObject();
    reader.endObject();
    reader.close();
  }

  @Test
  public void testSkipValue() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{'name': 'John'}"));
    reader.nextName();
    reader.nextString();
    reader.skipValue();
    reader.nextNull();
    reader.close();
  }
}