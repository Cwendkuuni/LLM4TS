import static org.junit.Assert.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class JsonTreeWriterTest {

  @org.junit.Test
  public void testJsonTreeWriter_beginArray() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    JsonElement result = writer.get();
    JsonArray expected = new JsonArray();
    assertEquals(expected, result);
  }

  @org.junit.Test
  public void testJsonTreeWriter_endArray() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.endArray();
    JsonElement result = writer.get();
    JsonNull expected = JsonNull.INSTANCE;
    assertEquals(expected, result);
  }

  @org.junit.Test(expected = IllegalStateException.class)
  public void testJsonTreeWriter_endArray_empty_stack() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    JsonElement result = writer.get();
    writer.endArray();
    throw new IllegalStateException();
  }

  @org.junit.Test
  public void testJsonTreeWriter_beginObject() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    JsonElement result = writer.get();
    JsonObject expected = new JsonObject();
    assertEquals(expected, result);
  }

  @org.junit.Test
  public void testJsonTreeWriter_endObject() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.endObject();
    JsonElement result = writer.get();
    JsonNull expected = JsonNull.INSTANCE;
    assertEquals(expected, result);
  }

  @org.junit.Test(expected = IllegalStateException.class)
  public void testJsonTreeWriter_endObject_empty_stack() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    JsonElement result = writer.get();
    writer.endObject();
    throw new IllegalStateException();
  }

  @org.junit.Test
  public void testJsonTreeWriter_name() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("test");
    writer.endObject();
    JsonElement result = writer.get();
    JsonObject expected = new JsonObject();
    assertEquals(expected.entrySet().iterator().next().getKey(), "test");
    assertEquals(expected, result);
  }

  @org.junit.Test
  public void testJsonTreeWriter_value_string() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("test");
    writer.value("test");
    writer.endObject();
    JsonElement result = writer.get();
    JsonObject expected = new JsonObject();
    expected.addProperty("test", "test");
    assertEquals(expected, result);
  }

  @org.junit.Test
  public void testJsonTreeWriter_value_boolean() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("test");
    writer.value(true);
    writer.endObject();
    JsonElement result = writer.get();
    JsonObject expected = new JsonObject();
    expected.addProperty("test", true);
    assertEquals(expected, result);
  }

  @org.junit.Test
  public void testJsonTreeWriter_value_number() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("test");
    writer.value(1);
    writer.endObject();
    JsonElement result = writer.get();
    JsonObject expected = new JsonObject();
    expected.addProperty("test", 1);
    assertEquals(expected, result);
  }

  @org.junit.Test
  public void testJsonTreeWriter_value_null() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("test");
    writer.nullValue();
    writer.endObject();
    JsonElement result = writer.get();
    JsonNull expected = JsonNull.INSTANCE;
    assertEquals(expected, result);
  }

  @org.junit.Test
  public void testJsonTreeWriter_beginArray_within_object() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("test");
    writer.beginArray();
    writer.endArray();
    writer.endObject();
    JsonElement result = writer.get();
    JsonObject expected = new JsonObject();
    expected.addProperty("test", new JsonArray());
    assertEquals(expected, result);
  }

  @org.junit.Test
  public void testJsonTreeWriter_value_array() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("test");
    writer.beginArray();
    writer.value(1);
    writer.value("test");
    writer.endArray();
    writer.endObject();
    JsonElement result = writer.get();
    JsonObject expected = new JsonObject();
    expected.addProperty("test", new JsonArray().add(1).add("test"));
    assertEquals(expected, result);
  }

  @org.junit.Test(expected = IllegalStateException.class)
  public void testJsonTreeWriter_value_array_after_end_object() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.endObject();
    writer.value(new JsonArray());
    throw new IllegalStateException();
  }

  @org.junit.Test
  public void testJsonTreeWriter_close() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.close();
    JsonElement result = writer.get();
    JsonPrimitive expected = new JsonPrimitive("closed");
    assertEquals(expected, result);
  }

  @org.junit.Test(expected = IOException.class)
  public void testJsonTreeWriter_close_non_empty_stack() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    JsonElement result = writer.get();
    writer.close();
    throw new IOException("Incomplete document");
  }
}