import static org.junit.Assert.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import org.junit.Test;

public class JsonTreeWriterTest {

  @Test
  public void testJsonTreeWriterEmpty() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    JsonElement jsonElement = jsonTreeWriter.get();
    assertNull(jsonElement);
  }

  @Test
  public void testJsonTreeWriterBeginArray() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginArray();
    JsonElement jsonElement = jsonTreeWriter.get();
    JsonArray jsonArray = (JsonArray) jsonElement;
    assertEquals(0, jsonArray.size());
  }

  @Test
  public void testJsonTreeWriterBeginArrayAddValue() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginArray();
    jsonTreeWriter.value("test");
    JsonElement jsonElement = jsonTreeWriter.get();
    JsonArray jsonArray = (JsonArray) jsonElement;
    JsonPrimitive jsonPrimitive = jsonArray.get(0).getAsJsonPrimitive();
    assertEquals("test", jsonPrimitive.getAsString());
  }

  @Test
  public void testJsonTreeWriterBeginArrayAddJsonArray() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginArray();
    jsonTreeWriter.beginArray();
    jsonTreeWriter.value("test");
    jsonTreeWriter.endArray();
    JsonElement jsonElement = jsonTreeWriter.get();
    JsonArray jsonArray = (JsonArray) jsonElement;
    JsonArray jsonArray1 = jsonArray.get(0).getAsJsonArray();
    JsonPrimitive jsonPrimitive = jsonArray1.get(0).getAsJsonPrimitive();
    assertEquals("test", jsonPrimitive.getAsString());
  }

  @Test
  public void testJsonTreeWriterBeginObject() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("test");
    jsonTreeWriter.value("testValue");
    jsonTreeWriter.endObject();
    JsonElement jsonElement = jsonTreeWriter.get();
    JsonObject jsonObject = jsonElement.getAsJsonObject();
    assertEquals("testValue", jsonObject.get("test").getAsString());
  }

  @Test
  public void testJsonTreeWriterBeginObjectAddJsonObject() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("outer");
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("inner");
    jsonTreeWriter.value("innerValue");
    jsonTreeWriter.endObject();
    jsonTreeWriter.endObject();
    JsonElement jsonElement = jsonTreeWriter.get();
    JsonObject jsonObject = jsonElement.getAsJsonObject();
    JsonObject jsonObject1 = jsonObject.getAsJsonObject().get("outer").getAsJsonObject();
    assertEquals("innerValue", jsonObject1.get("inner").getAsString());
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonTreeWriterInvalidStateArrayExpectedObject() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginArray();
    jsonTreeWriter.name("test");
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonTreeWriterInvalidStateObjectExpectedArray() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.beginArray();
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonTreeWriterInvalidStateUnexpectedState() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginArray();
    jsonTreeWriter.beginArray();
    jsonTreeWriter.beginObject();
  }

  @Test
  public void testJsonTreeWriterNullValue() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.nullValue();
    JsonElement jsonElement = jsonTreeWriter.get();
    JsonNull jsonNull = jsonElement.getAsJsonNull();
    assertTrue(jsonNull != null);
  }

  @Test
  public void testJsonTreeWriterValueBoolean() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.value(true);
    JsonElement jsonElement = jsonTreeWriter.get();
    JsonPrimitive jsonPrimitive = jsonElement.getAsJsonPrimitive();
    assertTrue(jsonPrimitive.isBoolean());
    assertTrue(jsonPrimitive.getAsBoolean());
  }

  @Test
  public void testJsonTreeWriterValueBooleanFalse() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.value(false);
    JsonElement jsonElement = jsonTreeWriter.get();
    JsonPrimitive jsonPrimitive = jsonElement.getAsJsonPrimitive();
    assertTrue(jsonPrimitive.isBoolean());
    assertFalse(jsonPrimitive.getAsBoolean());
  }

  @Test
  public void testJsonTreeWriterValueString() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.value("test");
    JsonElement jsonElement = jsonTreeWriter.get();
    JsonPrimitive jsonPrimitive = jsonElement.getAsJsonPrimitive();
    assertEquals("test", jsonPrimitive.getAsString());
  }

  @Test
  public void testJsonTreeWriterValueDouble() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.value(1.0);
    JsonElement jsonElement = jsonTreeWriter.get();
    JsonPrimitive jsonPrimitive = jsonElement.getAsJsonPrimitive();
    assertEquals(1.0, jsonPrimitive.getAsDouble(), 0.0);
  }

  @Test
  public void testJsonTreeWriterValueLong() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.value(1);
    JsonElement jsonElement = jsonTreeWriter.get();
    JsonPrimitive jsonPrimitive = jsonElement.getAsJsonPrimitive();
    assertEquals(1L, jsonPrimitive.getAsLong());
  }

  @Test
  public void testJsonTreeWriterValueNumber() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.value(new Long(1));
    JsonElement jsonElement = jsonTreeWriter.get();
    JsonPrimitive jsonPrimitive = jsonElement.getAsJsonPrimitive();
    assertEquals(1L, jsonPrimitive.getAsLong());
  }
}