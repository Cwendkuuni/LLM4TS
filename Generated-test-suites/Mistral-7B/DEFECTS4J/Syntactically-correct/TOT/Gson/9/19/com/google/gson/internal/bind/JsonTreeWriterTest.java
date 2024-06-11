import static org.junit.Assert.*;
import static com.google.gson.stream.CoercingJsonWriter.*;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.Test;

public class JsonTreeWriterTest {

  @Test
  public void testJsonTreeWriterWithBasicTypes() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();

    writer.beginObject().name("name").value("John Doe");
    writer.name("age").value(30);
    writer.name("isStudent").value(false);
    writer.endObject();

    JsonElement jsonElement = writer.get();
    JsonObject jsonObject = jsonElement.getAsJsonObject();

    assertEquals("John Doe", jsonObject.get("name").getAsString());
    assertEquals(30, jsonObject.get("age").getAsInt());
    assertFalse(jsonObject.has("isStudent"));
  }

  @Test
  public void testJsonTreeWriterWithArrays() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();

    writer.beginObject().name("array");
    writer.beginArray();
    writer.value(1);
    writer.value(2);
    writer.value(3.5);
    writer.endArray();
    writer.endObject();

    JsonElement jsonElement = writer.get();
    JsonObject jsonObject = jsonElement.getAsJsonObject();
    JsonArray jsonArray = jsonObject.get("array").getAsJsonArray();

    assertEquals(3, jsonArray.size());
    assertEquals(1, jsonArray.get(0).getAsInt());
    assertEquals(2, jsonArray.get(1).getAsInt());
    assertNotNull(jsonArray.get(2).isJsonNumber());
  }

  @Test
  public void testJsonTreeWriterWithNullValues() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();

    writer.beginObject().name("name").value(null);
    writer.name("age").value(null);
    writer.endObject();

    JsonElement jsonElement = writer.get();
    JsonObject jsonObject = jsonElement.getAsJsonObject();

    assertNull(jsonObject.get("name"));
    assertNull(jsonObject.get("age"));
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonTreeWriterWithInvalidOrdering() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();

    writer.beginArray().value("John");
    writer.endArray().name("name").value("Doe");

    writer.get();
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonTreeWriterWithMissingEndArray() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();

    writer.beginArray();
    writer.value("John");

    writer.get();
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonTreeWriterWithMissingEndObject() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();

    writer.beginObject().name("name");

    writer.get();
  }

  @Test
  public void testJsonTreeWriterWithCustomWriter() throws IOException {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    JsonTreeWriter writer = new JsonTreeWriter();

    writer.beginObject().name("name").value("John Doe");
    writer.name("age").value(30);
    writer.name("isStudent").value(false);
    writer.endObject();

    String jsonString = gson.toJson(writer.get());

    JsonElement jsonElement = JsonParser.parseString(jsonString).getAsJson();

    assertEquals("John Doe", jsonElement.getAsJsonObject().get("name").getAsString());
    assertEquals(30, jsonElement.getAsJsonObject().get("age").getAsInt());
    assertFalse(jsonElement.getAsJsonObject().has("isStudent"));
  }

  @Test
  public void testJsonTreeWriterWithLenientMode() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter(true);

    writer.beginObject().name("name").value("John Doe");
    writer.name("age").value("30");
    writer.name("isStudent").value(false);
    writer.endObject();

    JsonElement jsonElement = writer.get();
    JsonObject jsonObject = jsonElement.getAsJsonObject();

    assertEquals("John Doe", jsonObject.get("name").getAsString());
    assertEquals(30, jsonObject.get("age").getAsInt());
    assertFalse(jsonObject.has("isStudent"));
  }

  @Test
  public void testJsonTreeWriterWithCustomWriterAndLenientMode() throws IOException {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    JsonTreeWriter writer = new JsonTreeWriter(true);

    writer.beginObject().name("name").value("John Doe");
    writer.name("age").value("30");
    writer.name("isStudent").value(false);
    writer.endObject();

    String jsonString = gson.toJson(writer.get());

    JsonElement jsonElement = JsonParser.parseString(jsonString).getAsJson();

    assertEquals("John Doe", jsonElement.getAsJsonObject().get("name").getAsString());
    assertEquals(30, jsonElement.getAsJsonObject().get("age").getAsInt());
    assertFalse(jsonElement.getAsJsonObject().has("isStudent"));
  }
}