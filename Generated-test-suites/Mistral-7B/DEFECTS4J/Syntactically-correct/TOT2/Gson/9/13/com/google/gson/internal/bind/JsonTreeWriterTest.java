package com.google.gson.internal.bind.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonWriter;

public class JsonTreeWriterTest {

  @Test
  public void testBasicFunctionality() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name").value("John Doe");
    writer.name("age").value(30);
    writer.endObject();

    JsonElement actual = writer.get();
    JsonObject expected = new JsonObject()
        .addProperty("name", "John Doe")
        .addProperty("age", 30);

    assertEquals(expected.toString(), actual.toString());
  }

  @Test
  public void testArrayFunctionality() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.value(1);
    writer.value(2);
    writer.value(3);
    writer.endArray();

    JsonElement actual = writer.get();
    JsonArray expected = new JsonArray().add(1).add(2).add(3);

    assertEquals(expected.toString(), actual.toString());
  }

  @Test(expected = IllegalStateException.class)
  public void testIllegalStateExceptionWhenClosed() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.close();
    writer.value(1);
  }

  @Test(expected = IllegalStateException.class)
  public void testIllegalStateExceptionWhenArrayExpected() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.beginArray();
    writer.value(1);
    writer.endObject();
  }

  @Test(expected = IllegalStateException.class)
  public void testIllegalStateExceptionWhenObjectExpected() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.beginObject();
    writer.name("name").value("John Doe");
    writer.endObject();
    writer.value(1);
  }

  @Test
  public void testSerializeNulls() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter(false);
    writer.beginArray();
    writer.value(null);
    writer.value(new JsonPrimitive(true));
    writer.endArray();

    JsonElement actual = writer.get();
    JsonArray expected = new JsonArray().add(null).add(new JsonPrimitive(true));

    assertEquals(expected.toString(), actual.toString());
  }

  @Test
  public void testCustomJsonWriter() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter() {
      @Override
      public JsonWriter beginObject() throws IOException {
        JsonObject object = new JsonObject();
        object.addProperty("name", this.name);
        put(object);
        return this;
      }
    };
    writer.name("John Doe");

    JsonElement actual = writer.get();
    JsonObject expected = new JsonObject().addProperty("name", "John Doe");

    assertEquals(expected.toString(), actual.toString());
  }

  @Test
  public void testExceptionHandling() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    try {
      writer.close();
      writer.value(1);
    } catch (IllegalStateException e) {
      assertEquals("Incomplete document", e.getMessage());
    }
  }

  @Test
  public void testWriteToOutputStream() throws IOException {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    OutputStreamWriter writer = new OutputStreamWriter(outputStream);
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter(writer);

    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name").value("John Doe");
    jsonTreeWriter.name("age").value(30);
    jsonTreeWriter.endObject();

    jsonTreeWriter.close();

    String jsonString = outputStream.toString();
    JsonElement jsonElement = JsonParser.parseString(jsonString);

    JsonObject expected = new JsonObject()
        .addProperty("name", "John Doe")
        .addProperty("age", 30);

    assertEquals(expected.toString(), jsonElement.toString());
  }

  @Test
  public void testNestedObjectsAndArrays() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.beginArray();
    writer.beginObject();
    writer.name("name").value("John");
    writer.name("age").value(30);
    writer.endObject();
    writer.beginObject();
    writer.name("name").value("Jane");
    writer.name("age").value(20);
    writer.endObject();
    writer.endArray();
    writer.endArray();

    JsonElement actual = writer.get();
    JsonElement expected = new JsonArray().add(new JsonArray().add(new JsonObject().addProperty("name", "John").addProperty("age", 30))
        .add(new JsonObject().addProperty("name", "Jane").addProperty("age", 20)));

    assertEquals(expected.toString(), actual.toString());
  }

  @Test
  public void testMultipleObjectsWithSameName() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name").value("John Doe");
    writer.beginObject();
    writer.name("name").value("Jane Doe");
    writer.endObject();
    writer.endObject();

    JsonElement actual = writer.get();
    JsonObject expected = new JsonObject().add("name", new JsonObject().add("name", "John Doe")
        .add("name", new JsonObject().addProperty("name", "Jane Doe")));

    assertEquals(expected.toString(), actual.toString());
  }

  @Test
  public void testWriteNullValue() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name").value(null);
    writer.endObject();

    JsonElement actual = writer.get();
    JsonObject expected = new JsonObject().addProperty("name", null);

    assertEquals(expected.toString(), actual.toString());
  }

  @Test
  public void testWriteNonJsonElement() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name").value(new Object());
    writer.endObject();

    JsonElement actual = writer.get();
    JsonObject expected = new JsonObject().addProperty("name", "[]");

    assertEquals(expected.toString(), actual.toString());
  }
}