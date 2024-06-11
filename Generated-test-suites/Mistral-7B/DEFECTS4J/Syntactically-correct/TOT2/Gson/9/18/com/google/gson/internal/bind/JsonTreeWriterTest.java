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
  public void testJsonTreeWriterBasicUsage() {
    JsonTreeWriter writer = new JsonTreeWriter();

    JsonObject jsonObject = writer.beginObject();
    jsonObject.name("name").value("John Doe");
    jsonObject.name("age").value(30);
    JsonArray jsonArray = jsonObject.name("hobbies").beginArray();
    jsonArray.value("reading");
    jsonArray.value("swimming");
    jsonArray.endArray();
    writer.endObject();

    JsonElement jsonElement = writer.get();

    assertNotNull(jsonElement);
    assertEquals(JsonElement.class, jsonElement.getClass());
    assertEquals(JsonObject.class, jsonElement.getAsJsonObject().getClass());

    JsonObject actualJsonObject = jsonElement.getAsJsonObject();
    assertEquals("name", actualJsonObject.name("name").getAsString());
    assertEquals("John Doe", actualJsonObject.name("name").getAsString());
    assertEquals(30, actualJsonObject.get("age").getAsInt());

    JsonArray actualJsonArray = actualJsonObject.getAsJsonObject().get("hobbies").getAsJsonArray();
    assertEquals(2, actualJsonArray.size());

    assertEquals("reading", actualJsonArray.get(0).getAsString());
    assertEquals("swimming", actualJsonArray.get(1).getAsString());
  }

  @Test
  public void testJsonTreeWriterNullValue() {
    JsonTreeWriter writer = new JsonTreeWriter();

    JsonObject jsonObject = writer.beginObject();
    jsonObject.name("name").value(null);
    writer.endObject();

    JsonElement jsonElement = writer.get();

    assertNotNull(jsonElement);
    assertEquals(JsonElement.class, jsonElement.getClass());
    assertEquals(JsonObject.class, jsonElement.getAsJsonObject().getClass());

    JsonObject actualJsonObject = jsonElement.getAsJsonObject();
    assertNull(actualJsonObject.name("name").getAsString());
  }

  // Add test cases for other methods and edge cases here
}