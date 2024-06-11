import static org.junit.Assert.*;
import static com.google.gson.internal.bind.JsonTreeWriter.*;

import com.google.gson.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.Test;

public class JsonTreeWriterTest {

  @Test
  public void testEmptyWriter() {
    JsonTreeWriter writer = new JsonTreeWriter();
    assertNull(writer.get());
  }

  @Test
  public void testArrayWriter() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.value("hello");
    writer.value(1);
    writer.value(true);
    JsonElement result = writer.get();
    JsonArray array = result.getAsJsonArray();
    assertEquals(3, array.size());
    assertEquals("hello", array.get(0).getAsString());
    assertEquals(1, array.get(1).getAsInt());
    assertEquals(true, array.get(2).getAsBoolean());
    writer.close();
  }

  @Test
  public void testObjectWriter() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name");
    writer.value("John Doe");
    writer.name("age");
    writer.value(30);
    writer.name("isStudent");
    writer.value(false);
    JsonObject result = writer.get().getAsJsonObject();
    assertEquals("John Doe", result.get("name").getAsString());
    assertEquals(30, result.get("age").getAsInt());
    assertFalse(result.get("isStudent").getAsBoolean());
    writer.close();
  }

  @Test(expected = IllegalStateException.class)
  public void testMultipleObjects() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name");
    writer.value("John Doe");
    writer.endObject(); // should throw an error here
    writer.name("age");
    writer.value(30);
    writer.close();
  }

  @Test(expected = IllegalStateException.class)
  public void testMissingName() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.value(30);
    writer.close();
  }

  @Test(expected = IllegalStateException.class)
  public void testMissingValue() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name");
    writer.close();
  }
}