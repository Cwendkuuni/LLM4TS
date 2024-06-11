import static org.junit.Assert.*;
import static com.google.gson.internal.bind.JsonTreeWriter.*;
import com.google.gson.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Tests for {@link JsonTreeWriter}.
 */
public class JsonTreeWriterTest {

  @org.junit.Test
  public void testEmptyWriter() {
    try (JsonTreeWriter writer = new JsonTreeWriter()) {
      JsonElement result = writer.get();
      assertEquals(JsonNull.INSTANCE, result);
    }
  }

  @org.junit.Test
  public void testArrayWriter() {
    try (JsonTreeWriter writer = new JsonTreeWriter()) {
      writer.beginArray();
      writer.value(1);
      writer.value(2);
      writer.value(3);
      writer.endArray();

      JsonElement result = writer.get();
      JsonArray array = (JsonArray) result;
      assertEquals(3, array.size());
      List<JsonElement> elements = new ArrayList<>(Arrays.asList(array.get(0), array.get(1), array.get(2)));
      assertEquals(1, elements.get(0).getAsInt());
      assertEquals(2, elements.get(1).getAsInt());
      assertEquals(3, elements.get(2).getAsInt());
    }
  }

  @org.junit.Test
  public void testObjectWriter() {
    try (JsonTreeWriter writer = new JsonTreeWriter()) {
      writer.beginObject();
      writer.name("a").value(1);
      writer.name("b").value(new JsonPrimitive(2.5));
      writer.name("c").value(new JsonPrimitive(true));
      writer.name("d").value(new JsonPrimitive("hello"));
      writer.endObject();

      JsonElement result = writer.get();
      JsonObject object = (JsonObject) result;
      assertEquals(4, object.size());

      List<Map.Entry<String, JsonElement>> entries = new ArrayList<>(object.entrySet());
      assertEquals("a", entries.get(0).getKey());
      JsonPrimitive a = (JsonPrimitive) entries.get(0).getValue();
      assertEquals(1, a.getAsInt());

      assertEquals("b", entries.get(1).getKey());
      JsonPrimitive b = (JsonPrimitive) entries.get(1).getValue();
      assertEquals(2.5, b.getAsDouble(), 0.001);

      assertEquals("c", entries.get(2).getKey());
      JsonPrimitive c = (JsonPrimitive) entries.get(2).getValue();
      assertTrue(c.isJsonPrimitive());
      assertTrue(c.isBoolean());
      assertTrue(c.getAsBoolean());

      assertEquals("d", entries.get(3).getKey());
      JsonPrimitive d = (JsonPrimitive) entries.get(3).getValue();
      assertEquals("hello", d.getAsString());
    }
  }

  @org.junit.Test
  public void testObjectWriterWithName() {
    try (JsonTreeWriter writer = new JsonTreeWriter()) {
      writer.beginObject();
      writer.name("a").value(1);
      writer.name("b").beginObject();
      writer.name("c").value(2);
      writer.name("d").value(new JsonPrimitive(3.5));
      writer.endObject();
      writer.endObject();

      JsonElement result = writer.get();
      JsonObject object = (JsonObject) result;
      assertEquals(1, object.size());

      JsonElement element = object.get("a");
      JsonObject nestedObject = (JsonObject) element;
      assertEquals(2, nestedObject.size());

      List<Map.Entry<String, JsonElement>> nestedEntries = new ArrayList<>(nestedObject.entrySet());
      assertEquals("c", nestedEntries.get(0).getKey());
      JsonPrimitive c = (JsonPrimitive) nestedEntries.get(0).getValue();
      assertEquals(2, c.getAsInt());

      assertEquals("d", nestedEntries.get(1).getKey());
      JsonPrimitive d = (JsonPrimitive) nestedEntries.get(1).getValue();
      assertEquals(3.5, d.getAsDouble(), 0.001);
    }
  }

  @org.junit.Test
  public void testErrorHandling() {
    try (JsonTreeWriter writer = new JsonTreeWriter()) {
      writer.beginArray();
      writer.value(1);
      writer.endObject();
      writer.close();
    } catch (IOException e) {
      // expected
    }
  }
}