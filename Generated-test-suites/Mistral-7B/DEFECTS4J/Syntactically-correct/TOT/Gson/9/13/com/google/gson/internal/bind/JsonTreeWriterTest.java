import static org.junit.Assert.*;
import static com.google.gson.stream.CoercingJsonWriter.*;

import com.google.gson.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonWriter;

import org.junit.Test;

public class JsonTreeWriterTest {

  @Test
  public void testEmptyWriter() {
    JsonTreeWriter writer = new JsonTreeWriter();
    JsonElement result = writer.get();
    assertEquals(JsonNull.INSTANCE, result);
  }

  @Test
  public void testSimpleWriter() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name").value("John Doe");
    writer.name("age").value(30);
    writer.name("city").value("New York");
    writer.endObject();
    JsonElement result = writer.get();

    JsonObject expected = new JsonObject();
    expected.addProperty("name", "John Doe");
    expected.addProperty("age", 30);
    expected.addProperty("city", "New York");

    assertEquals(expected, result);
  }

  @Test
  public void testArrayWriter() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.value(1);
    writer.value(2);
    writer.value(3);
    writer.endArray();
    JsonElement result = writer.get();

    JsonArray expected = new JsonArray();
    expected.add(new JsonPrimitive(1));
    expected.add(new JsonPrimitive(2));
    expected.add(new JsonPrimitive(3));

    assertEquals(expected, result);
  }

  @Test(expected = IllegalStateException.class)
  public void testNestedObjectsAndArrays() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.beginObject();
    writer.name("name").value("John Doe");
    writer.name("age").value(30);
    writer.endObject();
    writer.beginArray();
    writer.beginObject();
    writer.name("name").value("Jane Doe");
    writer.name("age").value(25);
    writer.endObject();
    writer.endArray();
    writer.endArray();
    writer.get();
  }

  @Test(expected = IllegalStateException.class)
  public void testMissingEndObject() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name").value("John Doe");
    writer.name("age").value(30);
    writer.endArray(); // should be endObject()
    writer.get();
  }

  @Test(expected = IllegalStateException.class)
  public void testMissingEndArray() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.value(1);
    writer.beginObject();
    writer.name("name").value("John Doe");
    writer.name("age").value(30);
    writer.endObject(); // should be endArray()
    writer.get();
  }

  @Test(expected = IllegalStateException.class)
  public void testMissingName() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.value(new JsonPrimitive("John Doe")); // should be name()
    writer.value(30);
    writer.endObject();
    writer.get();
  }

  @Test(expected = IllegalStateException.class)
  public void testNameTwice() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name").value("John Doe");
    writer.name("name").value(30); // should be another property name
    writer.endObject();
    writer.get();
  }

  @Test(expected = IllegalStateException.class)
  public void testMissingValue() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name");
    writer.endObject();
    writer.get();
  }

  @Test
  public void testLenientMode() throws IOException {
    Gson gson = new GsonBuilder().setLenient().create();
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.setLenient(true);
    writer.beginObject();
    writer.name("name").value("John Doe");
    writer.name("age"); // should be allowed in lenient mode
    writer.endObject();
    JsonElement result = writer.get();

    JsonObject expected = new JsonObject();
    expected.addProperty("name", "John Doe");
    // expected.addProperty("age", null); // should not be added in lenient mode

    assertEquals(expected, result);
  }

  @Test(expected = IOException.class)
  public void testClosedWriter() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.close();
    writer.value(new JsonPrimitive("John Doe"));
  }

  @Test(expected = AssertionError.class)
  public void testUnwritableWriter() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.setWriter(UNWRITABLE_WRITER);
    writer.beginObject();
    writer.name("name").value("John Doe");
    writer.name("age").value(30);
    writer.endObject();
  }
}