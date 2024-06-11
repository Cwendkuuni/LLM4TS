import static org.junit.Assert.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.Test;

public class JsonTreeWriterTest {

  @Test
  public void testEmptyWriter() {
    JsonTreeWriter writer = new JsonTreeWriter();
    JsonElement result = writer.get();
    assertEquals(JsonNull.INSTANCE, result);
  }

  @Test
  public void testSimpleWriter() {
    JsonTreeWriter writer = new JsonTreeWriter();

    writer.beginObject();
    writer.name("name");
    writer.value("John Doe");
    writer.name("age");
    writer.value(30);
    writer.endObject();

    JsonElement result = writer.get();
    JsonObject expected = new JsonObject();
    expected.addProperty("name", "John Doe");
    expected.addProperty("age", 30);

    assertEquals(expected, result);
  }

  @Test
  public void testArrayWriter() {
    JsonTreeWriter writer = new JsonTreeWriter();

    writer.beginArray();
    writer.value(1);
    writer.value("two");
    writer.value(3.5);
    writer.endArray();

    JsonElement result = writer.get();
    JsonArray expected = new JsonArray();
    expected.add(new JsonPrimitive(1));
    expected.add(new JsonPrimitive("two"));
    expected.add(new JsonPrimitive(3.5));

    assertEquals(expected, result);
  }

  @Test(expected = IllegalStateException.class)
  public void testMissingBeginObject() {
    JsonTreeWriter writer = new JsonTreeWriter();

    writer.name("name");
    writer.value("John Doe");
    writer.endObject(); // missing beginObject()
  }

  @Test(expected = IllegalStateException.class)
  public void testMissingEndObject() {
    JsonTreeWriter writer = new JsonTreeWriter();

    writer.beginObject();
    writer.name("name");
    writer.value("John Doe");
  }

  @Test(expected = IllegalStateException.class)
  public void testMissingBeginArray() {
    JsonTreeWriter writer = new JsonTreeWriter();

    writer.value(1);
    writer.value("two");
    writer.value(3.5);
  }

  @Test(expected = IllegalStateException.class)
  public void testMissingEndArray() {
    JsonTreeWriter writer = new JsonTreeWriter();

    writer.beginArray();
    writer.value(1);
    writer.value("two");
  }

  @Test(expected = IllegalStateException.class)
  public void testMissingName() {
    JsonTreeWriter writer = new JsonTreeWriter();

    writer.beginObject();
    writer.beginArray();
    writer.value(1);
    writer.value("two");
    writer.endArray();
    writer.endObject();
  }

  @Test
  public void testNestedObjects() {
    JsonTreeWriter writer = new JsonTreeWriter();

    writer.beginObject();
    writer.name("person");
    writer.beginObject();
      writer.name("name");
      writer.value("John Doe");
      writer.name("age");
      writer.value(30);
    writer.endObject();
    writer.endObject();

    JsonElement result = writer.get();
    JsonObject expected = new JsonObject();
    JsonObject person = new JsonObject();
    person.addProperty("name", "John Doe");
    person.addProperty("age", 30);
    expected.add("person", person);

    assertEquals(expected, result);
  }

  @Test
  public void testNestedArrays() {
    JsonTreeWriter writer = new JsonTreeWriter();

    writer.beginObject();
    writer.name("person");
    writer.beginArray();
      writer.beginArray();
        writer.value(1);
        writer.value("two");
      writer.endArray();
      writer.value("John Doe");
    writer.endArray();
    writer.endObject();

    JsonElement result = writer.get();
    JsonObject expected = new JsonObject();
    JsonArray person = new JsonArray();
    JsonArray array = new JsonArray();
    array.add(new JsonPrimitive(1));
    array.add(new JsonPrimitive("two"));
    person.add(array);
    person.add(new JsonPrimitive("John Doe"));
    expected.add("person", person);

    assertEquals(expected, result);
  }
}