import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

public class JsonTreeReaderTest {

  @Test
  public void testEmptyJsonTree() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonParser.parseReader(""));
    assertFalse(reader.hasNext());
  }

  @Test
  public void testSimpleJsonObject() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonParser.parseReader("{" +
        "\"name\": \"John\"," +
        "\"age\": 27," +
        "\"hobbies\": [" +
            "\"reading\"," +
            "\"hiking\"" +
        "]," +
        "\"isMarried\": true" +
        "}"));

    JsonToken token;
    assertEquals(JsonToken.BEGIN_OBJECT, token = reader.peek());

    reader.beginObject();
    assertEquals("name", reader.nextName());
    assertEquals("John", reader.nextString());

    assertEquals(JsonToken.NAME, token = reader.peek());
    assertEquals("age", reader.nextName());
    assertEquals(27, reader.nextInt());

    assertEquals(JsonToken.NAME, token = reader.peek());
    assertEquals("hobbies", reader.nextName());
    reader.beginArray();
    assertEquals("reading", reader.nextString());
    assertEquals("hiking", reader.nextString());
    reader.endArray();

    assertEquals(JsonToken.NAME, token = reader.peek());
    assertEquals("isMarried", reader.nextName());
    assertEquals(true, reader.nextBoolean());

    reader.endObject();
    assertFalse(reader.hasNext());
  }

  @Test
  public void testSimpleJsonArray() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonParser.parseReader("[1, 2, 3]"));

    JsonToken token;
    assertEquals(JsonToken.BEGIN_ARRAY, token = reader.peek());

    reader.beginArray();
    assertEquals(1, reader.nextInt());
    assertEquals(2, reader.nextInt());
    assertEquals(3, reader.nextInt());
    reader.endArray();

    reader.endArray();
    assertFalse(reader.hasNext());
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonTreeReaderClosed() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonParser.parseReader("{}"));
    reader.close();
    reader.nextInt();
  }

  @Test(expected = AssertionError.class)
  public void testJsonTreeReaderEndDocument() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonParser.parseReader(""));
    reader.nextInt();
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonTreeReaderEmptyArray() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonParser.parseReader("[]"));
    reader.beginArray();
    reader.endArray();
    reader.nextInt();
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonTreeReaderEmptyObject() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonParser.parseReader("{}"));
    reader.beginObject();
    reader.endObject();
    reader.nextInt();
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonTreeReaderNullValue() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonParser.parseReader("{\"name\": null}"));
    reader.beginObject();
    reader.nextName();
    reader.nextNull();
    reader.endObject();
    reader.nextInt();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testJsonTreeReaderInvalidJson() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonReader(new ByteArrayInputStream("{".getBytes())));
    reader.nextInt();
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonTreeReaderUnexpectedToken() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonParser.parseReader("{\"name\": 1}"));
    reader.beginObject();
    reader.nextName();
    reader.nextInt(); // expected String
    reader.nextInt(); // expected end object
  }

  @Test
  public void testJsonTreeReaderComplexJson() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonParser.parseReader("{\"name\": \"John\"," +
        "\"age\": 27," +
        "\"isMarried\": true," +
        "\"address\": {" +
            "\"street\": \"Main St\"," +
            "\"city\": \"Anytown\"," +
            "\"state\": \"CA\"," +
            "\"zip\": 12345" +
        "}," +
        "\"phoneNumbers\": [" +
            "{\"type\": \"home\", \"number\": \"555-1234\"}," +
            "{\"type\": \"work\", \"number\": \"555-5678\"}" +
        "]," +
        "\"children\": [" +
            "{\"name\": \"John Jr.\"," +
            "\"age\": 10," +
            "\"hobbies\": [" +
                "\"reading\"" +
            "]}" +
            "]," +
        "\"pets\": [" +
            "{\"name\": \"Fluffy\"," +
            "\"type\": \"cat\"" +
            "}" +
            "]," +
        "\"car\": {" +
            "\"make\": \"Toyota\"," +
            "\"model\": \"Corolla\"," +
            "\"color\": \"red\"," +
            "\"year\": 2005" +
        "}" +
        "}"));

    JsonToken token;
    assertEquals(JsonToken.BEGIN_OBJECT, token = reader.peek());

    reader.beginObject();
    assertEquals("name", reader.nextName());
    assertEquals("John", reader.nextString());

    assertEquals(JsonToken.NAME, token = reader.peek());
    assertEquals("age", reader.nextName());
    assertEquals(27, reader.nextInt());

    assertEquals(JsonToken.NAME, token = reader.peek());
    assertEquals("isMarried", reader.nextName());
    assertEquals(true, reader.nextBoolean());

    assertEquals(JsonToken.NAME, token = reader.peek());
    assertEquals("address", reader.nextName());
    reader.beginObject();

    assertEquals("street", reader.nextName());
    assertEquals("Main St", reader.nextString());

    assertEquals("city", reader.nextName());
    assertEquals("Anytown", reader.nextString());

    assertEquals("state", reader.nextName());
    assertEquals("CA", reader.nextString());

    assertEquals("zip", reader.nextName());
    assertEquals(12345, reader.nextInt());

    reader.endObject();

    assertEquals(JsonToken.NAME, token = reader.peek());
    assertEquals("phoneNumbers", reader.nextName());
    reader.beginArray();

    reader.beginObject();
    assertEquals("type", reader.nextName());
    assertEquals("home", reader.nextString());
    assertEquals("number", reader.nextName());
    assertEquals("555-1234", reader.nextString());
    reader.endObject();

    reader.beginObject();
    assertEquals("type", reader.nextName());
    assertEquals("work", reader.nextString());
    assertEquals("number", reader.nextName());
    assertEquals("555-5678", reader.nextString());
    reader.endObject();

    reader.endArray();

    assertEquals(JsonToken.NAME, token = reader.peek());
    assertEquals("children", reader.nextName());
    reader.beginArray();

    reader.beginObject();
    assertEquals("name", reader.nextName());
    assertEquals("John Jr.", reader.nextString());
    assertEquals("age", reader.nextName());
    assertEquals(10, reader.nextInt());
    reader.beginArray();
    assertEquals("hobbies", reader.nextName());
    reader.beginArray();
    assertEquals("reading", reader.nextString());
    reader.endArray();
    reader.endObject();

    reader.endArray();

    assertEquals(JsonToken.NAME, token = reader.peek());
    assertEquals("pets", reader.nextName());
    reader.beginArray();

    reader.beginObject();
    assertEquals("name", reader.nextName());
    assertEquals("Fluffy", reader.nextString());
    assertEquals("type", reader.nextName());
    assertEquals("cat", reader.nextString());
    reader.endObject();

    reader.endArray();

    assertEquals(JsonToken.NAME, token = reader.peek());
    assertEquals("car", reader.nextName());
    reader.beginObject();

    assertEquals("make", reader.nextName());
    assertEquals("Toyota", reader.nextString());

    assertEquals("model", reader.nextName());
    assertEquals("Corolla", reader.nextString());

    assertEquals("color", reader.nextName());
    assertEquals("color", reader.nextString());

    assertEquals("year", reader.nextName());
    assertEquals(2005, reader.nextInt());

    reader.endObject();

    reader.endObject();
    assertFalse(reader.hasNext());
  }
}