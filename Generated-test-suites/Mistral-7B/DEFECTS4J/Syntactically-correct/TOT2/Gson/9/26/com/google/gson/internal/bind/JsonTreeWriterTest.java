import static org.junit.Assert.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.Test;

public class JsonTreeWriterTest {
  @Test
  public void testJsonTreeWriter_basicUsage() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name").value("John Doe");
    writer.name("age").value(30);
    writer.endObject();

    JsonElement jsonElement = writer.get();
    JsonObject jsonObject = jsonElement.getAsJsonObject();

    assertEquals("name", jsonObject.name("name").getAsString());
    assertEquals(30, jsonObject.get("age").getAsInt());
  }

  @Test
  public void testJsonTreeWriter_arrayUsage() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.value("apple");
    writer.value("banana");
    writer.endArray();

    JsonElement jsonElement = writer.get();
    JsonArray jsonArray = jsonElement.getAsJsonArray();

    assertEquals(2, jsonArray.size());
    assertEquals("apple", jsonArray.get(0).getAsString());
    assertEquals("banana", jsonArray.get(1).getAsString());
  }
}