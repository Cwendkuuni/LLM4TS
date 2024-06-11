import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class JsonTreeReaderTest {

  @Test
  public void testEmptyJson() throws IOException {
    JsonElement jsonElement = JsonParser.parse("{}");
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);
    jsonTreeReader.beginObject();
    jsonTreeReader.endObject();
    jsonTreeReader.close();
  }

  @Test
  public void testBasicJsonObject() throws IOException {
    JsonElement jsonElement = JsonParser.parse("{"
        + "\"name\":\"John\","
        + "\"age\":30,"
        + "\"isMarried\":true,"
        + "\"hobbies\":["
        + "\"reading\","
        + "\"writing\""
        + "]"
        + "}");
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);

    jsonTreeReader.beginObject();
    assertEquals("name", jsonTreeReader.nextName());
    assertEquals("John", jsonTreeReader.nextString());
    jsonTreeReader.beginObject();
    assertEquals("age", jsonTreeReader.nextName());
    assertEquals(30, jsonTreeReader.nextInt());
    jsonTreeReader.endObject();
    jsonTreeReader.beginArray();
    assertEquals("hobbies", jsonTreeReader.nextName());
    jsonTreeReader.beginArray();
    assertEquals("reading", jsonTreeReader.nextString());
    jsonTreeReader.endArray();
    jsonTreeReader.beginArray();
    assertEquals("writing", jsonTreeReader.nextString());
    jsonTreeReader.endArray();
    jsonTreeReader.endArray();
    jsonTreeReader.endObject();
    jsonTreeReader.close();
  }

  @Test
  public void testBasicJsonArray() throws IOException {
    JsonElement jsonElement = JsonParser.parse("[1, \"two\", 3, 4.0, false, null]");
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);

    jsonTreeReader.beginArray();
    jsonTreeReader.beginArray();
    assertEquals(1, jsonTreeReader.nextInt());
    jsonTreeReader.endArray();

    jsonTreeReader.beginArray();
    assertEquals("two", jsonTreeReader.nextString());
    jsonTreeReader.endArray();

    jsonTreeReader.beginArray();
    assertEquals(3, jsonTreeReader.nextInt());
    jsonTreeReader.endArray();

    jsonTreeReader.beginArray();
    assertEquals(4.0, jsonTreeReader.nextDouble(), 0.0);
    jsonTreeReader.endArray();

    jsonTreeReader.beginBoolean();
    assertTrue(jsonTreeReader.nextBoolean());
    jsonTreeReader.endBoolean();

    jsonTreeReader.nextNull();

    jsonTreeReader.endArray();
    jsonTreeReader.close();
  }

  @Test
  public void testTypeMismatch() throws IOException {
    JsonElement jsonElement = new JsonParser().parse("{ \"value\": 3 }");
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);
    jsonTreeReader.beginObject();
    jsonTreeReader.beginObject(); // expected JsonObject but got JsonArray
    try {
      jsonTreeReader.nextName();
      fail();
    } catch (IllegalStateException e) {
      // expected
    }
    jsonTreeReader.close();
  }

  @Test
  public void testNestedObjects() throws IOException {
    JsonElement jsonElement = JsonParser.parse("{"
        + "\"person\":{"
        + "\"name\":\"John\","
        + "\"age\":30,"
        + "\"address\":{"
        + "\"street\":\"1st street\","
        + "\"city\":\"New York\""
        + "}"
        + "}"
        + "}");
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);

    jsonTreeReader.beginObject();
    jsonTreeReader.beginObject();
    assertEquals("person", jsonTreeReader.nextName());
    jsonTreeReader.beginObject();
    assertEquals("name", jsonTreeReader.nextName());
    assertEquals("John", jsonTreeReader.nextString());
    jsonTreeReader.beginObject();
    assertEquals("address", jsonTreeReader.nextName());
    jsonTreeReader.beginObject();
    assertEquals("street", jsonTreeReader.nextName());
    assertEquals("1st street", jsonTreeReader.nextString());
    jsonTreeReader.endObject();
    jsonTreeReader.beginObject();
    assertEquals("city", jsonTreeReader.nextName());
    assertEquals("New York", jsonTreeReader.nextString());
    jsonTreeReader.endObject();
    jsonTreeReader.endObject();
    jsonTreeReader.endObject();
    jsonTreeReader.close();
  }

  @Test
  public void testEmptyArrays() throws IOException {
    JsonElement jsonElement = JsonParser.parse("{}");
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);
    jsonTreeReader.beginObject();
    jsonTreeReader.beginArray();
    jsonTreeReader.endArray();
    jsonTreeReader.endObject();
    jsonTreeReader.close();
  }

  @Test
  public void testNestedArrays() throws IOException {
    JsonElement jsonElement = JsonParser.parse("{"
        + "\"array1\":["
        + "1,"
        + "\"two\","
        + "[2, \"three\", 4.0, true, null]"
        + "]"
        + "}");
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);

    jsonTreeReader.beginObject();
    jsonTreeReader.beginArray();
    jsonTreeReader.beginArray();
    assertEquals(1, jsonTreeReader.nextInt());
    jsonTreeReader.endArray();

    jsonTreeReader.beginArray();
    assertEquals("two", jsonTreeReader.nextString());
    jsonTreeReader.endArray();

    jsonTreeReader.beginArray();
    jsonTreeReader.beginArray();
    jsonTreeReader.beginArray();
    assertEquals(2, jsonTreeReader.nextInt());
    jsonTreeReader.beginArray();
    assertEquals("three", jsonTreeReader.nextString());
    jsonTreeReader.nextNull();
    jsonTreeReader.beginBoolean();
    assertTrue(jsonTreeReader.nextBoolean());
    jsonTreeReader.endBoolean();
    jsonTreeReader.endArray();
    jsonTreeReader.endArray();
    jsonTreeReader.endArray();
    jsonTreeReader.endObject();
    jsonTreeReader.close();
  }

  @Test(expected = JsonSyntaxException.class)
  public void testUnexpectedEndOfJson() throws IOException {
    JsonElement jsonElement = new JsonParser().parse("{ \"name\":\"John\" }");
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);
    jsonTreeReader.beginObject();
    jsonTreeReader.beginObject(); // expected JsonObject but got JsonNull
    jsonTreeReader.endObject();
    jsonTreeReader.close();
  }

  @Test
  public void testClosedJsonTreeReader() throws IOException {
    JsonElement jsonElement = new JsonParser().parse("{ \"name\":\"John\" }");
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);
    jsonTreeReader.close();
    try {
      jsonTreeReader.peek();
      fail();
    } catch (IllegalStateException e) {
      // expected
    }
  }
}