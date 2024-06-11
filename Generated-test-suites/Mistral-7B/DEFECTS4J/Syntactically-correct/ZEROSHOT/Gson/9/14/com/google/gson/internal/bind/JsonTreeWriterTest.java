import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

public class JsonTreeWriterTest {

  @Test
  public void testEmptyWriter() {
    JsonTreeWriter writer = new JsonTreeWriter();
    JsonElement element = writer.get();
    assertEquals(JsonNull.INSTANCE, element);
  }

  @Test
  public void testBeginArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    JsonElement element = writer.get();
    assertTrue(element instanceof JsonArray);
    JsonArray jsonArray = (JsonArray) element;
    assertEquals(0, jsonArray.size());

    writer.value("test");;
    element = writer.get();
    jsonArray = (JsonArray) element;
    assertEquals(1, jsonArray.size());
    assertEquals("test", jsonArray.get(0).getAsString());
  }

  @Test
  public void testBeginObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name");
    writer.value("John");
    writer.name("age");
    writer.value(30);
    JsonElement element = writer.get();
    assertTrue(element instanceof JsonObject);
    JsonObject jsonObject = (JsonObject) element;

    assertEquals("John", jsonObject.get("name").getAsString());
    assertEquals(30, jsonObject.get("age").getAsInt());

    List<Map.Entry<String, JsonElement>> entries = jsonObject.entrySet();
    assertEquals(2, entries.size());
  }

  @Test(expected = IllegalStateException.class)
  public void testBeginArrayAfterBeginObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.beginArray();
  }

  @Test(expected = IllegalStateException.class)
  public void testBeginObjectAfterBeginArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.beginObject();
  }

  @Test
  public void testEndArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.value("test1");;
    writer.endArray();
    JsonElement element = writer.get();
    assertTrue(element instanceof JsonArray);
    JsonArray jsonArray = (JsonArray) element;
    assertEquals(1, jsonArray.size());
    assertEquals("test1", jsonArray.get(0).getAsString());
  }

  @Test(expected = IllegalStateException.class)
  public void testEndArrayWithoutBeginArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.endArray();
  }

  @Test
  public void testEndObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name");
    writer.value("John");
    writer.name("age");
    writer.value(30);
    writer.endObject();
    JsonElement element = writer.get();
    assertTrue(element instanceof JsonObject);
    JsonObject jsonObject = (JsonObject) element;

    assertEquals("John", jsonObject.get("name").getAsString());
    assertEquals(30, jsonObject.get("age").getAsInt());
  }

  @Test(expected = IllegalStateException.class)
  public void testEndObjectWithoutBeginObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.endObject();
  }

  @Test
  public void testName() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name");
    writer.value("John");
    writer.name("age");
    writer.value(30);
    writer.name("address");
    writer.beginObject();
    writer.name("street");
    writer.value("Test Street");
    writer.name("city");
    writer.value("Test City");
    writer.endObject();
    writer.endObject();
    JsonElement element = writer.get();

    JsonObject root = (JsonObject) element;
    JsonObject address = root.getAsJsonObject("address");
    assertEquals("Test Street", address.get("street").getAsString());
    assertEquals("Test City", address.get("city").getAsString());
  }

  @Test(expected = IllegalStateException.class)
  public void testNameAfterEndObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name");
    writer.value("John");
    writer.name("age");
    writer.value(30);
    writer.endObject();
    writer.name("test");
  }

  @Test
  public void testValueString() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name");
    writer.value("John");
    writer.name("age");
    writer.value(30);
    writer.endObject();
    JsonElement element = writer.get();

    JsonObject jsonObject = (JsonObject) element;
    assertEquals("John", jsonObject.get("name").getAsString());
    assertEquals(30, jsonObject.get("age").getAsInt());
  }

  @Test
  public void testValueBoolean() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name");
    writer.value(true);
    writer.name("age");
    writer.value(30);
    writer.endObject();
    JsonElement element = writer.get();

    JsonObject jsonObject = (JsonObject) element;
    assertTrue(jsonObject.get("name").isJsonPrimitive());
    assertTrue(jsonObject.get("name").getAsBoolean());
  }

  @Test
  public void testValueDouble() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name");
    writer.value(3.14);
    writer.name("age");
    writer.value(30);
    writer.endObject();
    JsonElement element = writer.get();

    JsonObject jsonObject = (JsonObject) element;
    assertEquals(3.14, jsonObject.get("name").getAsDouble(), 0.0001);
  }

  @Test
  public void testValueLong() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name");
    writer.value(123L);
    writer.name("age");
    writer.value(30);
    writer.endObject();
    JsonElement element = writer.get();

    JsonObject jsonObject = (JsonObject) element;
    assertEquals(123L, jsonObject.get("name").getAsLong());
  }

  @Test
  public void testValueNumber() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name");
    writer.value(123.45);
    writer.name("age");
    writer.value(30);
    writer.endObject();
    JsonElement element = writer.get();

    JsonObject jsonObject = (JsonObject) element;
    JsonPrimitive jsonPrimitive = jsonObject.get("name").getAsJsonPrimitive();
    assertEquals(123.45, jsonPrimitive.getAsNumber().doubleValue(), 0.0001);
  }

  @Test
  public void testValueNull() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name");
    writer.nullValue();
    writer.name("age");
    writer.value(30);
    writer.endObject();
    JsonElement element = writer.get();

    JsonObject jsonObject = (JsonObject) element;
    assertNull(jsonObject.get("name"));
  }

  @Test
  public void testFlush() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name");
    writer.value("John");
    writer.name("age");
    writer.value(30);
    writer.endObject();
    writer.flush();
    JsonElement element = writer.get();

    JsonObject jsonObject = (JsonObject) element;
    assertEquals("John", jsonObject.get("name").getAsString());
    assertEquals(30, jsonObject.get("age").getAsInt());
  }

  @Test
  public void testClose() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name");
    writer.value("John");
    writer.name("age");
    writer.value(30);
    writer.endObject();
    writer.close();
    JsonElement element = writer.get();

    JsonObject jsonObject = (JsonObject) element;
    assertEquals("John", jsonObject.get("name").getAsString());
    assertEquals(30, jsonObject.get("age").getAsInt());

    assertEquals(1, writer.stack.size());
    JsonPrimitive closedSentinal = (JsonPrimitive) writer.stack.get(0);
    assertEquals("closed", closedSentinal.getAsString());
  }

  @Test
  public void testValueList() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("list");
    writer.beginArray();
    writer.value("test1");
    writer.value("test2");
    writer.endArray();
    writer.endObject();
    JsonElement element = writer.get();

    JsonObject jsonObject = (JsonObject) element;
    JsonArray jsonArray = jsonObject.get("list").getAsJsonArray();
    assertEquals(2, jsonArray.size());
    assertEquals("test1", jsonArray.get(0).getAsString());
    assertEquals("test2", jsonArray.get(1).getAsString());
  }

  @Test
  public void testNestedObjects() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name");
    writer.value("John");
    writer.name("age");
    writer.value(30);
    writer.name("address");
    writer.beginObject();
    writer.name("street");
    writer.value("Test Street");
    writer.name("city");
    writer.value("Test City");
    writer.endObject();
    writer.endObject();
    JsonElement element = writer.get();

    JsonObject jsonObject = (JsonObject) element;
    assertEquals("John", jsonObject.get("name").getAsString());
    assertEquals(30, jsonObject.get("age").getAsInt());
    JsonObject address = jsonObject.get("address").getAsJsonObject();
    assertEquals("Test Street", address.get("street").getAsString());
    assertEquals("Test City", address.get("city").getAsString());
  }
}