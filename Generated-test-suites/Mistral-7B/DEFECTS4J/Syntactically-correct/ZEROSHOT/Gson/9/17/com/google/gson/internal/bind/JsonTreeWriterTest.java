import static org.junit.Assert.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class JsonTreeWriterTest {

  @Test
  public void testEmptyJsonTree() {
    JsonTreeWriter writer = new JsonTreeWriter();
    JsonElement jsonTree = writer.get();

    assertEquals(JsonNull.INSTANCE, jsonTree);
  }

  @Test
  public void testJsonTreeWithSingleArray() {
    JsonTreeWriter writer = new JsonTreeWriter();

    writer.beginArray();
    writer.value(1);
    writer.value(2);
    writer.endArray();

    JsonElement jsonTree = writer.get();
    JsonArray jsonArray = (JsonArray) jsonTree;

    assertEquals(2, jsonArray.size());
    assertEquals(1, jsonArray.get(0).getAsInt());
    assertEquals(2, jsonArray.get(1).getAsInt());
  }

  @Test
  public void testJsonTreeWithSingleObject() {
    JsonTreeWriter writer = new JsonTreeWriter();

    writer.beginObject();
    writer.name("key1").value("value1");
    writer.endObject();

    JsonElement jsonTree = writer.get();
    JsonObject jsonObject = (JsonObject) jsonTree;

    assertEquals(1, jsonObject.size());
    assertEquals("value1", jsonObject.get("key1").getAsString());
  }

  @Test
  public void testNestedJsonTree() {
    JsonTreeWriter writer = new JsonTreeWriter();

    writer.beginObject();
    writer.name("key1").beginArray();
    writer.value(1);
    writer.value(2);
    writer.endArray();
    writer.name("key2").beginObject();
    writer.name("key3").value("value3");
    writer.endObject();
    writer.endObject();

    JsonElement jsonTree = writer.get();
    JsonObject jsonObject = (JsonObject) jsonTree;

    JsonArray jsonArray = jsonObject.getAsJsonArray("key1");

    assertEquals(2, jsonArray.size());
    assertEquals(1, jsonArray.get(0).getAsInt());
    assertEquals(2, jsonArray.get(1).getAsInt());

    JsonObject innerJsonObject = jsonObject.getAsJsonObject("key2");

    assertEquals(1, innerJsonObject.size());
    assertEquals("value3", innerJsonObject.get("key3").getAsString());
  }

  @Test
  public void testJsonTreeWithNullValues() {
    JsonTreeWriter writer = new JsonTreeWriter();

    writer.beginObject();
    writer.name("key1").value(null);
    writer.name("key2").value(null);
    writer.endObject();

    JsonElement jsonTree = writer.get();
    JsonObject jsonObject = (JsonObject) jsonTree;

    assertNotNull(jsonObject.get("key1"));
    assertNull(jsonObject.get("key1").isJsonNull());

    assertNotNull(jsonObject.get("key2"));
    assertNull(jsonObject.get("key2").isJsonNull());
  }

  @Test
  public void testJsonTreeWithLenientSerialization() {
    JsonTreeWriter writer = new JsonTreeWriter(true);

    writer.beginObject();
    writer.name("key1").value(Double.NaN);
    writer.name("key2").value(Double.POSITIVE_INFINITY);
    writer.endObject();

    JsonElement jsonTree = writer.get();
    JsonObject jsonObject = (JsonObject) jsonTree;

    JsonElement nanElement = jsonObject.get("key1");
    assertTrue(nanElement.isJsonNumber());
    assertEquals(Double.NaN, nanElement.getAsDouble());

    JsonElement infiniteElement = jsonObject.get("key2");
    assertTrue(infiniteElement.isJsonNumber());
    assertTrue(infiniteElement.getAsDouble() > 0);
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonTreeWithMismatchedEndObject() {
    JsonTreeWriter writer = new JsonTreeWriter();

    writer.beginObject();
    writer.name("key1").beginArray();
    writer.value(1);
    writer.value(2);
    writer.endArray();
    writer.name("key2").beginObject();
    writer.name("key3").value("value3");
    writer.endArray(); // <-- should be endObject()

    writer.endObject();
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonTreeWithMismatchedEndArray() {
    JsonTreeWriter writer = new JsonTreeWriter();

    writer.beginObject();
    writer.name("key1").beginArray();
    writer.value(1);
    writer.value(2);
    writer.endObject(); // <-- should be endArray()

    writer.endArray();
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonTreeWithMismatchedBeginObjectAfterArray() {
    JsonTreeWriter writer = new JsonTreeWriter();

    writer.beginArray();
    writer.value(1);
    writer.beginObject();

    writer.name("key1").value("value1");
    writer.name("key2").value("value2");

    writer.endObject(); // <-- should be endArray() before endObject()

    writer.endArray();
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonTreeWithMismatchedBeginArrayAfterObject() {
    JsonTreeWriter writer = new JsonTreeWriter();

    writer.beginObject();
    writer.name("key1").value("value1");
    writer.name("key2").value("value2");

    writer.beginArray();

    writer.endObject(); // <-- should be endArray() before endObject()

    writer.endArray();
  }

  @Test
  public void testJsonTreeWriterWithWrittenToByteArrayOutputStream() throws IOException {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    OutputStreamWriter writer = new OutputStreamWriter(outputStream);

    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter(writer);

    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("key1").value("value1");
    jsonTreeWriter.name("key2").beginArray();
    jsonTreeWriter.value(1);
    jsonTreeWriter.value(2);
    jsonTreeWriter.endArray();
    jsonTreeWriter.endObject();

    jsonTreeWriter.close();

    String jsonString = outputStream.toString();

    JsonParser jsonParser = new JsonParser();
    JsonElement jsonTree = jsonParser.parse(jsonString);

    JsonObject jsonObject = jsonTree.getAsJsonObject();

    assertEquals(1, jsonObject.size());
    JsonElement key1Element = jsonObject.get("key1");
    assertEquals("value1", key1Element.getAsString());

    JsonArray jsonArray = jsonObject.getAsJsonArray("key2");
    assertEquals(2, jsonArray.size());
    assertEquals(1, jsonArray.get(0).getAsInt());
    assertEquals(2, jsonArray.get(1).getAsInt());
  }
}