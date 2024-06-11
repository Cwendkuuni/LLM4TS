import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonTreeWriterTest {
  @Test
  public void testBeginArray() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    JsonArray array = writer.get();
    assertNotNull(array);
    assertTrue(array.isJsonArray());
    writer.close();
  }

  @Test
  public void testEndArray() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.endArray();
    JsonArray array = writer.get();
    assertNotNull(array);
    assertTrue(array.isJsonArray());
    assertEquals(0, array.size());
    writer.close();
  }

  @Test
  public void testBeginObject() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    JsonObject object = writer.get();
    assertNotNull(object);
    assertTrue(object.isJsonObject());
    writer.close();
  }

  @Test
  public void testEndObject() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name");
    writer.value("value");
    writer.endObject();
    JsonObject object = writer.get();
    assertNotNull(object);
    assertTrue(object.isJsonObject());
    assertEquals("value", object.get("name").getAsString());
    writer.close();
  }

  @Test
  public void testName() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name");
    writer.value("value");
    writer.endObject();
    JsonObject object = writer.get();
    assertNotNull(object);
    assertTrue(object.isJsonObject());
    assertEquals("value", object.get("name").getAsString());
    writer.close();
  }

  @Test(expected = IllegalStateException.class)
  public void testNameAfterEndObject() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.endObject();
    writer.name("name");
  }

  @Test(expected = IllegalStateException.class)
  public void testValueAfterEndObject() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.endObject();
    writer.value("value");
  }

  @Test
  public void testValueString() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name");
    writer.value("value");
    writer.endObject();
    JsonObject object = writer.get();
    assertNotNull(object);
    assertTrue(object.isJsonObject());
    JsonPrimitive value = object.get("name").getAsJsonPrimitive();
    assertEquals("value", value.getAsString());
    writer.close();
  }

  @Test
  public void testValueBoolean() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name");
    writer.value(true);
    writer.endObject();
    JsonObject object = writer.get();
    assertNotNull(object);
    assertTrue(object.isJsonObject());
    JsonPrimitive value = object.get("name").getAsJsonPrimitive();
    assertTrue(value.isBoolean());
    assertTrue(value.getAsBoolean());
    writer.close();
  }

  @Test
  public void testValueNull() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name");
    writer.nullValue();
    writer.endObject();
    JsonObject object = writer.get();
    assertNotNull(object);
    assertTrue(object.isJsonObject());
    JsonPrimitive value = object.get("name");
    assertNull(value);
    writer.close();
  }

  @Test
  public void testValueNumber() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name");
    writer.value(123);
    writer.endObject();
    JsonObject object = writer.get();
    assertNotNull(object);
    assertTrue(object.isJsonObject());
    JsonPrimitive value = object.get("name").getAsJsonPrimitive();
    assertEquals(123, value.getAsInt());
    writer.close();
  }

  @Test
  public void testValueArray() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.value("element1");
    writer.value("element2");
    writer.endArray();
    JsonArray array = writer.get();
    assertNotNull(array);
    assertEquals(2, array.size());
    JsonPrimitive element1 = array.get(0).getAsJsonPrimitive();
    assertEquals("element1", element1.getAsString());
    JsonPrimitive element2 = array.get(1).getAsJsonPrimitive();
    assertEquals("element2", element2.getAsString());
    writer.close();
  }
}