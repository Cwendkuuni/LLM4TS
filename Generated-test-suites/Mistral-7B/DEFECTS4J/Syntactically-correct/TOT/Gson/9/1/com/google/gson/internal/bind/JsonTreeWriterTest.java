import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class JsonTreeWriterTest {

  @Test
  public void testEmptyWriter() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    JsonElement json = writer.get();
    assertEquals(JsonNull.INSTANCE, json);
  }

  @Test
  public void testArrayWriter() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.value("element1");
    writer.value(123);
    writer.value(new JsonPrimitive(true));
    writer.endArray();

    JsonElement json = writer.get();
    JsonArray array = (JsonArray) json;
    List<JsonElement> elements = array.getAsJsonArray();
    assertEquals(3, elements.size());
    assertEquals("element1", elements.get(0).getAsString());
    assertEquals(123, elements.get(1).getAsInt());
    assertEquals(true, elements.get(2).getAsBoolean());
  }

  @Test
  public void testObjectWriter() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("key1").value("value1");
    writer.name("key2").beginArray();
    writer.value("element1");
    writer.value(123);
    writer.endArray();
    writer.name("key3").value(new JsonPrimitive(true));
    writer.endObject();

    JsonElement json = writer.get();
    JsonObject object = (JsonObject) json;
    assertEquals("value1", object.get("key1").getAsString());
    assertEquals(2, object.size());

    JsonElement element1 = object.get("key2");
    assertInstanceOf(JsonArray.class, element1);
    JsonArray array = (JsonArray) element1;
    List<JsonElement> elements = array.getAsJsonArray();
    assertEquals(2, elements.size());
    assertEquals("element1", elements.get(0).getAsString());
    assertEquals(123, elements.get(1).getAsInt());

    JsonElement key3 = object.get("key3");
    assertEquals(true, key3.getAsBoolean());
  }

  @Test
  public void testErrorScenarios() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();

    try {
      writer.beginArray();
      writer.endObject();
      throw new AssertionError();
    } catch (IllegalStateException e) {
      // expected
    }

    try {
      writer.beginObject();
      writer.name("key1");
      writer.beginArray();
      writer.value(123);
      writer.value("element1");
      writer.endArray();
      writer.endObject();
      writer.value("extraValue");
      throw new AssertionError();
    } catch (IllegalStateException e) {
      // expected
    }

    try {
      writer.value("value");
      writer.beginArray();
      throw new AssertionError();
    } catch (IllegalStateException e) {
      // expected
    }

    try {
      writer.beginObject();
      writer.name("key1");
      writer.value("value");
      writer.name("key2");
      throw new AssertionError();
    } catch (IllegalStateException e) {
      // expected
    }

    try {
      writer.value(new JsonPrimitive(123));
      writer.beginArray();
      writer.value(new JsonPrimitive(456));
      writer.endArray();
      writer.name("key");
      throw new AssertionError();
    } catch (IllegalStateException e) {
      // expected
    }

    try {
      writer.beginArray();
      writer.beginObject();
      throw new AssertionError();
    } catch (IllegalStateException e) {
      // expected
    }

    try {
      writer.beginObject();
      writer.endArray();
      throw new AssertionError();
    } catch (IllegalStateException e) {
      // expected
    }

    try {
      writer.beginObject();
      writer.name("key");
      writer.name("key");
      throw new AssertionError();
    } catch (IllegalStateException e) {
      // expected
    }

    try {
      writer.beginObject();
      writer.name("key");
      writer.beginObject();
      writer.name("key");
      writer.endObject();
      writer.endObject();
      writer.endObject();
      throw new AssertionError();
    } catch (IllegalStateException e) {
      // expected
    }

    try {
      writer.beginObject();
      writer.name("key");
      writer.beginObject();
      writer.name("key");
      writer.name("key");
      writer.endObject();
      writer.endObject();
      throw new AssertionError();
    } catch (IllegalStateException e) {
      // expected
    }
  }

  @Test
  public void testWriterWithCustomCloseHandler() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter() {
      @Override
      public void close() throws IOException {
        super.close();
        System.out.println("Writer closed.");
      }
    };

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    OutputStreamWriter writerStream = new OutputStreamWriter(outputStream);
    writer.setWriter(writerStream);
    writer.beginObject();
    writer.name("key1").value(123);
    writer.endObject();

    String jsonString = outputStream.toString();
    JsonElement json = new JsonParser().parse(jsonString);

    JsonObject object = (JsonObject) json;
    assertEquals(1, object.size());
    assertEquals(123, object.get("key1").getAsInt());

    assertNull(writer.get());
    writerStream.close();
    writer.get(); // should throw an exception
  }
}