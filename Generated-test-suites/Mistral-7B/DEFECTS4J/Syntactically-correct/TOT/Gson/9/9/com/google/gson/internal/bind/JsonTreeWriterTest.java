import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class JsonTreeWriterTest {

  @Test
  public void testWritingEmptyJson() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.endObject();
    JsonElement result = writer.get();
    assertNotNull(result);
    assertEquals(JsonNull.INSTANCE, result);
  }

  @Test
  public void testWritingSingleValueJson() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("value");
    writer.value("test");
    writer.endObject();
    JsonElement result = writer.get();
    assertNotNull(result);
    JsonObject object = result.getAsJsonObject();
    assertNotNull(object);
    assertEquals("test", object.get("value").getAsString());
  }

  @Test
  public void testWritingArrayJson() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("array");
    writer.beginArray();
    writer.value("test1");
    writer.value("test2");
    writer.endArray();
    writer.endObject();
    JsonElement result = writer.get();
    assertNotNull(result);
    JsonObject object = result.getAsJsonObject();
    assertNotNull(object);
    JsonArray array = object.getAsJsonArray("array");
    assertNotNull(array);
    assertEquals(2, array.size());
    assertEquals("test1", array.get(0).getAsString());
    assertEquals("test2", array.get(1).getAsString());
  }

  @Test
  public void testWritingComplexJson() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name");
    writer.value("John Doe");
    writer.name("age");
    writer.value(30);
    writer.name("hobbies");
    writer.beginArray();
    writer.value("reading");
    writer.value("gym");
    writer.value("coding");
    writer.endArray();
    writer.name("address");
    writer.beginObject();
    writer.name("street");
    writer.value("123 Main St");
    writer.name("city");
    writer.value("Anytown");
    writer.name("state");
    writer.value("CA");
    writer.name("zip");
    writer.value(12345);
    writer.endObject();
    writer.endObject();
    JsonElement result = writer.get();
    assertNotNull(result);
    JsonObject object = result.getAsJsonObject();
    assertNotNull(object);
    assertEquals("John Doe", object.get("name").getAsString());
    assertEquals(30, object.get("age").getAsInt());
    JsonArray hobbies = object.getAsJsonObject().get("hobbies").getAsJsonArray();
    assertEquals(3, hobbies.size());
    assertEquals("reading", hobbies.get(0).getAsString());
    assertEquals("gym", hobbies.get(1).getAsString());
    assertEquals("coding", hobbies.get(2).getAsString());
    JsonObject address = object.getAsJsonObject().getAsJsonObject("address");
    assertNotNull(address);
    assertEquals("123 Main St", address.get("street").getAsString());
    assertEquals("Anytown", address.get("city").getAsString());
    assertEquals("CA", address.get("state").getAsString());
    assertEquals(12345, address.get("zip").getAsInt());
  }

  @Test
  public void testWritingNullJson() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name");
    writer.value(null);
    writer.endObject();
    JsonElement result = writer.get();
    assertNotNull(result);
    JsonObject object = result.getAsJsonObject();
    assertNotNull(object);
    assertNull(object.get("name"));
  }
}