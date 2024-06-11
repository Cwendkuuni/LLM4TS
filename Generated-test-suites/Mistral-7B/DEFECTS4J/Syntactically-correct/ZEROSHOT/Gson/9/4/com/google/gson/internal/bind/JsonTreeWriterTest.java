import static org.junit.Assert.*;

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
  public void testEmptyJsonTreeWriter() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.close();
    JsonElement jsonElement = jsonTreeWriter.get();
    assertEquals(JsonNull.INSTANCE, jsonElement);
  }

  @Test
  public void testJsonTreeWriterWithArray() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginArray();
    jsonTreeWriter.value("one");
    jsonTreeWriter.value(1);
    jsonTreeWriter.value(new JsonPrimitive(3.14));
    jsonTreeWriter.endArray();
    JsonElement jsonElement = jsonTreeWriter.get();

    JsonArray jsonArray = jsonElement.getAsJsonArray();
    List<JsonElement> jsonArrayList = jsonArray.getAsJsonArray();

    assertEquals(3, jsonArrayList.size());
    assertEquals("one", jsonArrayList.get(0).getAsString());
    assertEquals(1, jsonArrayList.get(1).getAsJsonPrimitive().getAsInt());
    assertEquals(3.14, jsonArrayList.get(2).getAsJsonPrimitive().getAsDouble(), 0.001);
  }

  @Test
  public void testJsonTreeWriterWithObject() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name").value("John Doe");
    jsonTreeWriter.name("age").value(30);
    jsonTreeWriter.endObject();

    JsonElement jsonElement = jsonTreeWriter.get();
    JsonObject jsonObject = jsonElement.getAsJsonObject();

    assertEquals("John Doe", jsonObject.get("name").getAsString());
    assertEquals(30, jsonObject.get("age").getAsJsonPrimitive().getAsInt());
  }

  @Test
  public void testJsonTreeWriterWithNestedObjectsAndArrays() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("user");
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name").value("John Doe");
    jsonTreeWriter.name("age").value(30);
    jsonTreeWriter.name("address");
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("street").value("123 Main St");
    jsonTreeWriter.name("city").value("Anytown");
    jsonTreeWriter.name("state");
    jsonTreeWriter.value("CA");
    jsonTreeWriter.endObject();
    jsonTreeWriter.endObject();
    jsonTreeWriter.beginArray();
    jsonTreeWriter.value("apple");
    jsonTreeWriter.value(new JsonPrimitive(5));
    jsonTreeWriter.endArray();
    jsonTreeWriter.endObject();

    JsonElement jsonElement = jsonTreeWriter.get();
    JsonObject jsonObject = jsonElement.getAsJsonObject();
    JsonElement user = jsonObject.get("user");
    JsonObject userObject = user.getAsJsonObject();
    JsonElement name = userObject.get("name");
    JsonElement address = userObject.get("address");
    JsonObject addressObject = address.getAsJsonObject();
    JsonElement street = addressObject.get("street");
    JsonElement city = addressObject.get("city");
    JsonPrimitive state = addressObject.get("state").getAsJsonPrimitive();
    JsonArray hobbies = jsonObject.get("hobbies").getAsJsonArray();

    assertEquals("John Doe", name.getAsString());
    assertEquals("123 Main St", street.getAsString());
    assertEquals("Anytown", city.getAsString());
    assertEquals("CA", state.getAsString());
    assertEquals(2, hobbies.size());
    assertEquals("apple", hobbies.get(0).getAsString());
    assertEquals(5, hobbies.get(1).getAsJsonPrimitive().getAsInt());
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonTreeWriterWithMismatchedEndObject() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name").value("John Doe");
    jsonTreeWriter.name("age").value(30);
    jsonTreeWriter.endArray(); // should be endObject() instead
    jsonTreeWriter.close();
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonTreeWriterWithMissingEndArrayOrEndObject() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name").value("John Doe");
    jsonTreeWriter.name("age").value(30);
    jsonTreeWriter.close(); // should be endArray() instead
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonTreeWriterWithNestedMismatchedEndArrayOrEndObject() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name").value("John Doe");
    jsonTreeWriter.name("age").value(30);
    jsonTreeWriter.beginObject(); // should be array instead
    jsonTreeWriter.value("apple");
    jsonTreeWriter.endObject(); // should be endArray() instead
    jsonTreeWriter.close();
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonTreeWriterWithUnclosedObject() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name").value("John Doe");
    jsonTreeWriter.name("age").value(30);
  }

  @Test
  public void testJsonTreeWriterToString() throws IOException {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(byteArrayOutputStream);

    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter(outputStreamWriter);
    jsonTreeWriter.beginArray();
    jsonTreeWriter.value("one");
    jsonTreeWriter.value(1);
    jsonTreeWriter.value(new JsonPrimitive(3.14));
    jsonTreeWriter.endArray();
    jsonTreeWriter.close();

    String jsonString = byteArrayOutputStream.toString();
    assertEquals("[\"one\",1,3.14]", jsonString);
  }
}