import static org.junit.Assert.*;
import static com.google.gson.internal.bind.JsonTreeWriter.*;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.junit.Test;

public class JsonTreeWriterTest {

  @Test
  public void testWriterBasicUsage() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name").value("John Doe");
    writer.name("age").value(30);
    writer.name("city").value("New York");
    writer.endObject();

    JsonElement json = writer.get();
    JsonObject jsonObject = json.getAsJsonObject();

    assertEquals("name", jsonObject.entrySet().iterator().next().getKey());
    assertEquals("John Doe", jsonObject.get("name").getAsString());
    assertEquals("age", jsonObject.entrySet().iterator().next().next().getKey());
    assertEquals(30, jsonObject.get("age").getAsInt());
    assertEquals("city", jsonObject.entrySet().iterator().next().next().next().getKey());
    assertEquals("New York", jsonObject.get("city").getAsString());
  }

  @Test
  public void testWriterArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.value("apple");
    writer.value("banana");
    writer.value("orange");
    writer.endArray();

    JsonElement json = writer.get();
    JsonArray jsonArray = json.getAsJsonArray();

    assertEquals(3, jsonArray.size());
    assertEquals("apple", jsonArray.get(0).getAsString());
    assertEquals("banana", jsonArray.get(1).getAsString());
    assertEquals("orange", jsonArray.get(2).getAsString());
  }

  @Test
  public void testWriterNullValues() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name").value(null);
    writer.name("age").value(null);
    writer.endObject();

    JsonElement json = writer.get();
    JsonObject jsonObject = json.getAsJsonObject();

    assertNull(jsonObject.get("name"));
    assertNull(jsonObject.get("age"));
  }

  @Test(expected = IllegalStateException.class)
  public void testWriterInvalidUsage() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name").value("John Doe");
    writer.name("age").value("thirty");
    writer.endObject();

    writer.get();
  }

  @Test
  public void testWriterLenient() {
    JsonTreeWriter writer = new JsonTreeWriter(true);
    writer.beginObject();
    writer.name("name").value("John Doe");
    writer.name("age").value("thirty");
    writer.endObject();

    JsonElement json = writer.get();
    JsonObject jsonObject = json.getAsJsonObject();

    assertEquals("name", jsonObject.entrySet().iterator().next().getKey());
    assertEquals("John Doe", jsonObject.get("name").getAsString());
    assertEquals("age", jsonObject.entrySet().iterator().next().next().getKey());
    assertEquals("thirty", jsonObject.get("age").getAsString());
  }

  @Test
  public void testWriterSerializeNulls() {
    JsonTreeWriter writer = new JsonTreeWriter(false);
    writer.beginObject();
    writer.name("name").value(null);
    writer.name("age").value("thirty");
    writer.endObject();

    JsonElement json = writer.get();
    JsonObject jsonObject = json.getAsJsonObject();

    assertNull(jsonObject.get("name"));
    assertEquals("age", jsonObject.entrySet().iterator().next().getKey());
    assertFalse(jsonObject.get("age").isJsonNull());
    assertFalse(jsonObject.get("age").isJsonPrimitive());
  }

  @Test
  public void testWriterWithErrors() {
    JsonTreeWriter writer = new JsonTreeWriter();

    try {
      writer.beginArray();
      writer.value("apple");
      writer.endObject(); // should throw an error
    } catch (IllegalStateException e) {
      // expected
    }

    try {
      writer.beginObject();
      writer.endArray(); // should throw an error
    } catch (IllegalStateException e) {
      // expected
    }

    try {
      writer.beginObject();
      writer.name("name").value("John Doe");
      writer.name("age").value("thirty");
      writer.name("city").value(42); // should throw an error
    } catch (ClassCastException e) {
      // expected
    }

    try {
      writer.beginObject();
      writer.name("name").value("John Doe");
      writer.name("age").value(new Object()); // should throw an error
    } catch (ClassCastException e) {
      // expected
    }
  }
}