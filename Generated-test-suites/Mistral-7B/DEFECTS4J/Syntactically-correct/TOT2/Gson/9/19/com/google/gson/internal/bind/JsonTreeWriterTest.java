import static org.junit.Assert.*;

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
    JsonElement result = writer.get();
    assertTrue(result instanceof JsonArray);
    assertEquals(0, ((JsonArray) result).size());
  }

  @Test
  public void testEndArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.beginArray();
    writer.value("element1");
    writer.value("element2");
    writer.endArray();
    writer.endArray();
    JsonElement result = writer.get();
    JsonArray jsonArray = (JsonArray) result;
    JsonArray innerJsonArray = (JsonArray) jsonArray.get(0);
    assertEquals(2, innerJsonArray.size());
    assertEquals("element1", innerJsonArray.get(0).getAsString());
    assertEquals("element2", innerJsonArray.get(1).getAsString());
    assertEquals(1, jsonArray.size());
  }

  @Test
  public void testBeginObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("key1").value("value1");
    writer.name("key2").value("value2");
    writer.endObject();
    JsonElement result = writer.get();
    JsonObject jsonObject = (JsonObject) result;
    assertEquals(2, jsonObject.size());
    assertEquals("value1", jsonObject.get("key1").getAsString());
    assertEquals("value2", jsonObject.get("key2").getAsString());
  }

  @Test
  public void testEndObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("key1").value("value1");
    writer.name("key2").value("value2");
    writer.endObject();
    JsonElement result = writer.get();
    assertEquals(1, result.getAsJsonObject().size());
    assertTrue(result.isJsonObject());
  }

  @Test(expected = IllegalStateException.class)
  public void testEndObjectBeforeBeginObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.endObject();
    writer.get();
  }

  @Test(expected = IllegalStateException.class)
  public void testValueAfterEndObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.endObject();
    writer.value("test");
  }

  @Test(expected = IllegalStateException.class)
  public void testNameAfterEndObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.endObject();
    writer.name("test");
  }
}