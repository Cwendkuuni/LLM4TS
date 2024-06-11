import static org.junit.Assert.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.io.StringWriter;

import org.junit.Test;

public class JsonTreeWriterTest {

  @Test
  public void testJsonTreeWriterArray() {
    JsonTreeWriter writer = new JsonTreeWriter();

    writer.beginArray();
    writer.value(1);
    writer.value(2);
    writer.value(3);
    writer.endArray();

    StringWriter stringWriter = new StringWriter();
    writer.setWriter(stringWriter);
    writer.close();

    JsonElement json = writer.get();
    JsonArray jsonArray = json.isJsonArray();
    assertTrue(jsonArray != null);

    assertEquals(3, jsonArray.size());
    assertEquals(1, jsonArray.get(0).getAsInt());
    assertEquals(2, jsonArray.get(1).getAsInt());
    assertEquals(3, jsonArray.get(2).getAsInt());
  }

  @Test
  public void testJsonTreeWriterObject() {
    JsonTreeWriter writer = new JsonTreeWriter();

    writer.beginObject();
    writer.name("name").value("John Doe");
    writer.name("age").value(30);
    writer.endObject();

    StringWriter stringWriter = new StringWriter();
    writer.setWriter(stringWriter);
    writer.close();

    JsonElement json = writer.get();
    JsonObject jsonObject = json.isJsonObject();
    assertTrue(jsonObject != null);

    assertEquals("John Doe", jsonObject.get("name").getAsString());
    assertEquals(30, jsonObject.get("age").getAsInt());
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonTreeWriterInvalidOrder() {
    JsonTreeWriter writer = new JsonTreeWriter();

    writer.value(1);
    writer.beginArray();
    writer.value(2);
    writer.value(3);
    writer.endArray();
    writer.endObject();

    writer.close();
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonTreeWriterNullValue() {
    JsonTreeWriter writer = new JsonTreeWriter();

    writer.beginObject();
    writer.name("name").value(null);
    writer.endObject();

    writer.close();
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonTreeWriterMissingEnd() {
    JsonTreeWriter writer = new JsonTreeWriter();

    writer.beginObject();
    writer.name("name").value("John Doe");

    writer.close();
  }
}