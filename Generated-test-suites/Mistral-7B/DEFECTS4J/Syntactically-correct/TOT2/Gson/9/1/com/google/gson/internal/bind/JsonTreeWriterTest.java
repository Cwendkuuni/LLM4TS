import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.Test;

public class JsonTreeWriterTest {

  @Test
  public void testBeginArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    JsonElement jsonElement = writer.get();
    assertNotNull(jsonElement);
    assertEquals(JsonArray.class, jsonElement.getClass());
  }

  @Test
  public void testEndArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.endArray();
    JsonElement jsonElement = writer.get();
    assertNotNull(jsonElement);
    assertEquals(JsonNull.class, jsonElement.getClass());
  }

  @Test
  public void testBeginObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    JsonElement jsonElement = writer.get();
    assertNotNull(jsonElement);
    assertEquals(JsonObject.class, jsonElement.getClass());
  }

  @Test
  public void testEndObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.endObject();
    JsonElement jsonElement = writer.get();
    assertNotNull(jsonElement);
    assertEquals(JsonNull.class, jsonElement.getClass());
  }

  @Test
  public void testName() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("test");
    writer.value("Hello");
    writer.endObject();
    JsonElement jsonElement = writer.get();
    assertNotNull(jsonElement);
    assertEquals(JsonObject.class, jsonElement.getClass());
    JsonObject obj = jsonElement.getAsJsonObject();
    assertEquals("test", obj.name("test").getAsString());
    assertEquals("Hello", obj.getAsJsonPrimitive("test").getAsString());
  }

  @Test
  public void testValueString() {
    JsonTreeWriter writer = new JsonTreeWriter();
    JsonElement jsonElement = writer.value("Hello").get();
    assertNotNull(jsonElement);
    assertEquals(JsonPrimitive.class, jsonElement.getClass());
    assertEquals("Hello", jsonElement.getAsString());
  }

  @Test
  public void testValueBoolean() {
    JsonTreeWriter writer = new JsonTreeWriter();
    JsonElement jsonElement = writer.value(true).get();
    assertNotNull(jsonElement);
    assertEquals(JsonPrimitive.class, jsonElement.getClass());
    assertEquals(true, jsonElement.getAsBoolean());
  }

  @Test
  public void testValueNumber() {
    JsonTreeWriter writer = new JsonTreeWriter();
    JsonElement jsonElement = writer.value(123).get();
    assertNotNull(jsonElement);
    assertEquals(JsonPrimitive.class, jsonElement.getClass());
    assertEquals(123, jsonElement.getAsNumber().doubleValue());
  }

  @Test
  public void testValueNull() {
    JsonTreeWriter writer = new JsonTreeWriter();
    JsonElement jsonElement = writer.nullValue().get();
    assertNotNull(jsonElement);
    assertEquals(JsonNull.class, jsonElement.getClass());
  }

  @Test(expected = IllegalStateException.class)
  public void testValueAfterEndObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.endObject();
    writer.value("Hello");
  }

  @Test(expected = IllegalStateException.class)
  public void testEndArrayWithoutBeginArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.endArray();
  }

  @Test(expected = IllegalStateException.class)
  public void testEndObjectWithoutBeginObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.endObject();
  }

  @Test
  public void testBeginArrayValueString() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.value("Hello");
    writer.endArray();
    JsonElement jsonElement = writer.get();
    assertNotNull(jsonElement);
    assertEquals(JsonArray.class, jsonElement.getClass());
    JsonArray jsonArray = jsonElement.getAsJsonArray();
    assertEquals(1, jsonArray.size());
    assertEquals("Hello", jsonArray.get(0).getAsString());
  }

  @Test
  public void testBeginObjectNameValueString() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("test");
    writer.value("Hello");
    writer.endObject();
    JsonElement jsonElement = writer.get();
    assertNotNull(jsonElement);
    assertEquals(JsonObject.class, jsonElement.getClass());
    JsonObject jsonObject = jsonElement.getAsJsonObject();
    assertEquals("Hello", jsonObject.get("test").getAsString());
  }

  @Test
  public void testBeginObjectNameValueNumber() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("test");
    writer.value(123);
    writer.endObject();
    JsonElement jsonElement = writer.get();
    assertNotNull(jsonElement);
    assertEquals(JsonObject.class, jsonElement.getClass());
    JsonObject jsonObject = jsonElement.getAsJsonObject();
    assertEquals(123, jsonObject.get("test").getAsNumber().doubleValue());
  }
}