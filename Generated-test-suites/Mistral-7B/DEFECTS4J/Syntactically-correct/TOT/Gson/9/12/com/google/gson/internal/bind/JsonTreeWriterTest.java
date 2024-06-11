import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonTreeWriterTest {

  @Test
  public void testEmptyWriter() {
    JsonTreeWriter writer = new JsonTreeWriter();
    JsonElement json = writer.get();
    assertTrue(json.isJsonNull());
  }

  @Test
  public void testSingleValueWriter() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value("test");
    JsonElement json = writer.get();
    JsonPrimitive primitive = json.getAsJsonPrimitive();
    assertEquals("test", primitive.getAsString());
  }

  @Test
  public void testArrayWriter() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.value("test1");
    writer.value("test2");
    writer.endArray();
    JsonElement json = writer.get();
    JsonArray array = json.getAsJsonArray();
    assertEquals(2, array.size());
    assertEquals("test1", array.get(0).getAsString());
    assertEquals("test2", array.get(1).getAsString());
  }

  @Test
  public void testNestedObjectsWriter() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name");
    writer.value("John");
    writer.beginObject();
    writer.name("age");
    writer.value(30);
    writer.endObject();
    writer.endObject();
    JsonElement json = writer.get();
    JsonObject jsonObject = json.getAsJsonObject();
    assertEquals("John", jsonObject.get("name").getAsString());
    JsonObject innerObject = jsonObject.getAsJsonObject("name");
    assertEquals(1, innerObject.size());
    JsonObject ageObject = innerObject.getAsJsonObject();
    assertEquals(1, ageObject.size());
    assertEquals(30, ageObject.get("age").getAsInt());
  }

  @Test(expected = IllegalStateException.class)
  public void testMissingEndObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name");
    writer.value("John");
    writer.endArray(); // should be endObject
  }

  @Test(expected = IllegalStateException.class)
  public void testMissingEndArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.value("test1");
    writer.endObject(); // should be endArray
  }
}