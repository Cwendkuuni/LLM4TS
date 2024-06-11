import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class JsonTreeWriterTest {

  @Test
  public void testEmptyWriter() {
    JsonTreeWriter writer = new JsonTreeWriter();
    JsonElement result = writer.get();
    assertNull(result);
  }

  @Test(expected = IllegalStateException.class)
  public void testBeginArrayWithoutEnding() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.close(); // should not be called
  }

  @Test(expected = IllegalStateException.class)
  public void testBeginArrayWithoutEndingTwice() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.beginArray();
  }

  @Test
  public void testBeginArrayEndArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.endArray();
    JsonElement result = writer.get();
    JsonArray array = result.getAsJsonArray();
    assertNotNull(array);
    assertEquals(0, array.size());
  }

  @Test
  public void testBeginArrayAddValueEndArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.value("foo");
    writer.endArray();
    JsonElement result = writer.get();
    JsonArray array = result.getAsJsonArray();
    assertNotNull(array);
    assertEquals(1, array.size());
    JsonPrimitive element = array.get(0).getAsJsonPrimitive();
    assertEquals("foo", element.getAsString());
  }

  @Test
  public void testBeginArrayAddMultipleValuesEndArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.value("foo");
    writer.value(1);
    writer.value(new JsonPrimitive(3.14));
    writer.endArray();
    JsonElement result = writer.get();
    JsonArray array = result.getAsJsonArray();
    assertNotNull(array);
    assertEquals(3, array.size());
    JsonPrimitive foo = array.get(0).getAsJsonPrimitive();
    assertEquals("foo", foo.getAsString());
    JsonPrimitive intValue = array.get(1).getAsJsonPrimitive();
    assertEquals(1, intValue.getAsInt());
    JsonPrimitive doubleValue = array.get(2).getAsJsonPrimitive();
    assertEquals(3.14, doubleValue.getAsDouble(), 0.0);
  }

  @Test
  public void testBeginArrayAddArrayEndArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.beginArray();
    writer.value("bar");
    writer.endArray();
    writer.value("baz");
    writer.endArray();
    JsonElement result = writer.get();
    JsonArray array = result.getAsJsonArray();
    assertNotNull(array);
    assertEquals(1, array.size());
    JsonArray subArray = array.get(0).getAsJsonArray();
    assertNotNull(subArray);
    assertEquals(2, subArray.size());
    JsonPrimitive bar = subArray.get(0).getAsJsonPrimitive();
    assertEquals("bar", bar.getAsString());
    JsonPrimitive baz = subArray.get(1).getAsJsonPrimitive();
    assertEquals("baz", baz.getAsString());
  }

  @Test(expected = IllegalStateException.class)
  public void testBeginObjectWithoutEnding() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.close(); // should not be called
  }

  @Test(expected = IllegalStateException.class)
  public void testBeginObjectWithoutEndingTwice() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.beginObject();
  }

  @Test
  public void testBeginObjectEndObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.endObject();
    JsonElement result = writer.get();
    JsonObject object = result.getAsJsonObject();
    assertNotNull(object);
    assertEquals(0, object.size());
  }

  @Test
  public void testBeginObjectAddNameValuePairEndObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("key");
    writer.value("value");
    writer.endObject();
    JsonElement result = writer.get();
    JsonObject object = result.getAsJsonObject();
    assertNotNull(object);
    assertEquals(1, object.size());
    JsonPrimitive key = object.get("key").getAsJsonPrimitive();
    assertEquals("value", key.getAsString());
  }

  @Test
  public void testBeginObjectAddMultipleNameValuePairsEndObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("key1");
    writer.value("value1");
    writer.name("key2");
    writer.value("value2");
    writer.endObject();
    JsonElement result = writer.get();
    JsonObject object = result.getAsJsonObject();
    assertNotNull(object);
    assertEquals(2, object.size());
    JsonPrimitive key1 = object.get("key1").getAsJsonPrimitive();
    assertEquals("value1", key1.getAsString());
    JsonPrimitive key2 = object.get("key2").getAsJsonPrimitive();
    assertEquals("value2", key2.getAsString());
  }

  @Test
  public void testBeginObjectAddArrayEndObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("foo");
    writer.beginArray();
    writer.value("bar");
    writer.endArray();
    writer.endObject();
    JsonElement result = writer.get();
    JsonObject object = result.getAsJsonObject();
    assertNotNull(object);
    JsonArray array = object.getAsJsonObject("foo").getAsJsonArray();
    assertNotNull(array);
    assertEquals(1, array.size());
    JsonPrimitive bar = array.get(0).getAsJsonPrimitive();
    assertEquals("bar", bar.getAsString());
  }

  @Test
  public void testBeginObjectAddObjectEndObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("foo");
    writer.beginObject();
    writer.name("bar");
    writer.value("baz");
    writer.endObject();
    writer.endObject();
    JsonElement result = writer.get();
    JsonObject object = result.getAsJsonObject();
    assertNotNull(object);
    JsonObject foo = object.getAsJsonObject("foo");
    assertNotNull(foo);
    JsonObject bar = foo.getAsJsonObject("bar");
    assertNotNull(bar);
    assertEquals("baz", bar.get("value").getAsJsonPrimitive().getAsString());
  }

  @Test
  public void testValueString() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value("foo");
    JsonElement result = writer.get();
    JsonPrimitive primitive = result.getAsJsonPrimitive();
    assertEquals("foo", primitive.getAsString());
  }

  @Test
  public void testValueBoolean() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(true);
    JsonElement result = writer.get();
    JsonPrimitive primitive = result.getAsJsonPrimitive();
    assertTrue(primitive.isBoolean());
    assertTrue(primitive.getAsBoolean());
  }

  @Test
  public void testValueNumberInt() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(1);
    JsonElement result = writer.get();
    JsonPrimitive primitive = result.getAsJsonPrimitive();
    assertEquals(1, primitive.getAsInt());
  }

  @Test
  public void testValueNumberLong() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(1L);
    JsonElement result = writer.get();
    JsonPrimitive primitive = result.getAsJsonPrimitive();
    assertEquals(1, primitive.getAsLong());
  }

  @Test
  public void testValueNumberDouble() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(1.0);
    JsonElement result = writer.get();
    JsonPrimitive primitive = result.getAsJsonPrimitive();
    assertEquals(1.0, primitive.getAsDouble(), 0.0);
  }

  @Test
  public void testValueNull() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.nullValue();
    JsonElement result = writer.get();
    JsonNull nullValue = result.getAsJsonNull();
    assertNotNull(nullValue);
  }

  @Test
  public void testFlush() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.value("foo");
    writer.endArray();
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    OutputStreamWriter writer2 = new OutputStreamWriter(outputStream);
    writer.setWriter(writer2);
    writer.flush();
    writer2.flush();
    writer.close();
    String jsonString = outputStream.toString();
    assertEquals("[\n  \"foo\"\n]", jsonString);
  }

  @Test
  public void testClose() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.value("foo");
    writer.endArray();
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    OutputStreamWriter writer2 = new OutputStreamWriter(outputStream);
    writer.setWriter(writer2);
    writer.close();
    String jsonString = outputStream.toString();
    assertEquals("[\n  \"foo\"\n]", jsonString);
    // check that close caused an error when trying to add new elements
    try {
      writer.value("bar");
    } catch (IllegalStateException e) {
      // expected
    }
  }

  @Test(expected = IllegalStateException.class)
  public void testCloseTwice() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.close();
    writer.close();
  }

  @Test
  public void testGetAfterClose() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.close();
    JsonElement result = writer.get();
    assertSame(JsonNull.INSTANCE, result);
  }
}