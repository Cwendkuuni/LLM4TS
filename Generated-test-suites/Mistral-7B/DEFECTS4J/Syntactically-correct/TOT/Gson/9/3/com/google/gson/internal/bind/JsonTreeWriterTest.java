import static org.junit.Assert.*;
import static com.google.gson.internal.bind.JsonTreeWriter.*;
import com.google.gson.*;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JsonTreeWriterTest {

  @org.junit.Test
  public void testEmptyJsonWriter() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    StringWriter stringWriter = new StringWriter();
    writer.setWriter(stringWriter);
    writer.beginObject();
    writer.endObject();
    JsonElement jsonElement = writer.get();
    assertEquals("{}", stringWriter.toString());
    assertEquals(JsonNull.INSTANCE, jsonElement);
  }

  @org.junit.Test
  public void testJsonWriterWithArray() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    StringWriter stringWriter = new StringWriter();
    writer.setWriter(stringWriter);
    writer.beginArray();
    writer.value("element1");
    writer.value(1);
    writer.value(true);
    writer.endArray();
    JsonElement jsonElement = writer.get();
    assertEquals("[\"element1\",1,true]", stringWriter.toString());
    assertEquals(new JsonArray() {{
      add(new JsonPrimitive("element1"));
      add(new JsonPrimitive(1));
      add(new JsonPrimitive(true));
    }}, jsonElement);
  }

  @org.junit.Test
  public void testJsonWriterWithObject() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    StringWriter stringWriter = new StringWriter();
    writer.setWriter(stringWriter);
    writer.beginObject();
    writer.name("name").value("value");
    writer.name("anotherName").value(123);
    writer.endObject();
    JsonElement jsonElement = writer.get();
    assertEquals("{\"name\":\"value\",\"anotherName\":123}", stringWriter.toString());
    assertEquals(new JsonObject() {{
      addProperty("name", new JsonPrimitive("value"));
      addProperty("anotherName", new JsonPrimitive(123));
    }}, jsonElement);
  }

  @org.junit.Test
  public void testJsonWriterWithNestedObjectsAndArrays() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    StringWriter stringWriter = new StringWriter();
    writer.setWriter(stringWriter);
    writer.beginObject();
    writer.name("outerObj");
    writer.beginObject();
    writer.name("name1").value("value1");
    writer.name("name2");
    writer.beginArray();
    writer.value(1);
    writer.value("element");
    writer.endArray();
    writer.endObject();
    writer.endObject();
    JsonElement jsonElement = writer.get();
    assertEquals(
        "{\"outerObj\":{\"name1\":\"value1\",\"name2\":[1,\"element\"]}}",
        stringWriter.toString());
    assertEquals(
        new JsonObject() {{
          addProperty("outerObj", new JsonObject() {{
            addProperty("name1", new JsonPrimitive("value1"));
            addProperty("name2", new JsonArray() {{
              add(new JsonPrimitive(1));
              add(new JsonPrimitive("element"));
            }});
          }});
        }},
        jsonElement);
  }

  @org.junit.Test
  public void testJsonWriterWithInvalidOperations() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    StringWriter stringWriter = new StringWriter();
    writer.setWriter(stringWriter);

    // Nested beginObject without endObject
    writer.beginObject();
    writer.beginObject();
    writer.close(); // should throw an exception

    // BeginObject without Name
    writer.beginObject();
    writer.beginArray();
    writer.endObject(); // should throw an exception

    // BeginArray without BeginArray
    writer.beginObject();
    writer.value("test"); // should throw an exception

    // EndArray without BeginArray
    writer.beginObject();
    writer.beginArray();
    writer.endArray();
    writer.endArray(); // should throw an exception

    // EndObject without BeginObject
    writer.beginObject();
    writer.endObject();
    writer.name("test"); // should throw an exception

    // Multiple top-level JsonElements
    writer.beginObject();
    writer.name("test1").value(1);
    writer.beginObject();
    writer.name("test2").value(2);
    writer.endObject();
    writer.endObject();
    writer.beginObject(); // should throw an exception

    writer.close();

    // Check that the writer closes properly
    List<JsonElement> elements = new ArrayList<>();
    writer.setWriter(new StringWriter(elements::write));
    writer.beginObject();
    writer.name("test").value("test");
    writer.endObject();
    writer.close();
    assertEquals(1, elements.size());
  }
}