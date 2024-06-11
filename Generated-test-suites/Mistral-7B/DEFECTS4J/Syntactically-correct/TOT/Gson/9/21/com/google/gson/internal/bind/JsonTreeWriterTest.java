import static org.junit.Assert.*;
import static com.google.gson.stream.CoercingJsonWriter.*;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonWriter;
import org.junit.Test;

public class JsonTreeWriterTest {

  @Test
  public void testEmptyJsonTreeWriter() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.endObject();
    JsonElement jsonElement = jsonTreeWriter.get();

    JsonElement expectedJsonElement = new JsonPrimitive("{}");
    assertEquals(expectedJsonElement.getAsString(), jsonElement.getAsString());
  }

  @Test
  public void testSimpleJsonTreeWriter() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name").value("John");
    jsonTreeWriter.name("age").value(30);
    jsonTreeWriter.endObject();
    JsonElement jsonElement = jsonTreeWriter.get();

    JsonElement expectedJsonElement = new JsonObject()
        .addProperty("name", "John")
        .addProperty("age", 30);
    assertEquals(expectedJsonElement.getAsString(), jsonElement.getAsString());
  }

  @Test
  public void testArrayJsonTreeWriter() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginArray();
    jsonTreeWriter.value(1);
    jsonTreeWriter.value(2);
    jsonTreeWriter.endArray();
    JsonElement jsonElement = jsonTreeWriter.get();

    JsonElement expectedJsonElement = new JsonArray().add(new JsonPrimitive(1))
        .add(new JsonPrimitive(2));
    assertEquals(expectedJsonElement.getAsString(), jsonElement.getAsString());
  }

  @Test(expected = IllegalStateException.class)
  public void testMixedJsonTreeWriter() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginArray();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name").value("John");
    jsonTreeWriter.endObject();
    jsonTreeWriter.value(2);
    jsonTreeWriter.endArray();
    jsonTreeWriter.get();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNaNJsonTreeWriter() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name").value(Double.NaN);
    jsonTreeWriter.endObject();
    jsonTreeWriter.get();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInfinityJsonTreeWriter() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name").value(Double.POSITIVE_INFINITY);
    jsonTreeWriter.endObject();
    jsonTreeWriter.get();
  }

  @Test
  public void testStringCoercionJsonTreeWriter() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name").value("\"John\"");
    jsonTreeWriter.endObject();
    JsonElement jsonElement = jsonTreeWriter.get();

    JsonElement expectedJsonElement = new JsonObject()
        .addProperty("name", "John");
    assertEquals(expectedJsonElement.getAsString(), jsonElement.getAsString());
  }

  @Test
  public void testLenientJsonTreeWriter() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter(new GsonBuilder().setLenient().create());
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name").value("\"John\"");
    jsonTreeWriter.name("age").value(Double.NaN);
    jsonTreeWriter.endObject();
    JsonElement jsonElement = jsonTreeWriter.get();

    JsonElement expectedJsonElement = new JsonObject()
        .addProperty("name", "John")
        .addProperty("age", JsonNull.INSTANCE);
    assertEquals(expectedJsonElement.getAsString(), jsonElement.getAsString());
  }

  @Test
  public void testStringCoercionWithCustomSerializerJsonTreeWriter() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.setLenient(false);
    jsonTreeWriter.serializeNulls();

    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name").value("null");
    jsonTreeWriter.endObject();
    JsonElement jsonElement = jsonTreeWriter.get();

    JsonElement expectedJsonElement = new JsonObject()
        .addProperty("name", null);
    assertEquals(expectedJsonElement.getAsString(), jsonElement.getAsString());
  }

  @Test
  public void testNullValueJsonTreeWriter() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject()
        .name("name").value(null)
        .endObject();
    JsonElement jsonElement = jsonTreeWriter.get();

    JsonElement expectedJsonElement = new JsonObject().addProperty("name", JsonNull.INSTANCE);
    assertEquals(expectedJsonElement.getAsString(), jsonElement.getAsString());
  }

  @Test
  public void testEmptyArrayJsonTreeWriter() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginArray().endArray();
    JsonElement jsonElement = jsonTreeWriter.get();

    JsonElement expectedJsonElement = new JsonArray();
    assertEquals(expectedJsonElement.getAsString(), jsonElement.getAsString());
  }
}