import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

public class JsonTreeReaderTest {

  private JsonTreeReader reader;
  private JsonElement jsonElement;

  @Before
  public void setup() {
    jsonElement = JsonParser.parseString("{}");
    reader = new JsonTreeReader(jsonElement);
  }

  @Test
  public void testBeginArray() throws IOException {
    reader.beginArray();
    assertTrue(reader.hasNext());
    reader.endArray();
    assertFalse(reader.hasNext());
  }

  @Test
  public void testBeginObject() throws IOException {
    reader.beginObject();
    assertTrue(reader.hasNext());
    reader.endObject();
    assertFalse(reader.hasNext());
  }

  @Test
  public void testEndArray() throws IOException {
    reader.beginArray();
    reader.beginArray();
    reader.endArray();
    reader.endArray();
    assertFalse(reader.hasNext());
  }

  @Test
  public void testEndObject() throws IOException {
    reader.beginObject();
    reader.beginObject();
    reader.endObject();
    reader.endObject();
    assertFalse(reader.hasNext());
  }

  @Test(expected = IllegalStateException.class)
  public void testEndWhenNotStarted() throws IOException {
    reader.endArray();
  }

  @Test
  public void testHasNext() throws IOException {
    reader.beginObject();
    reader.beginArray();
    reader.beginArray();
    reader.beginObject();
    reader.beginArray();
    reader.endArray();
    reader.endObject();
    reader.endArray();
    reader.endObject();
    reader.endArray();

    for (int i = 0; i < 10; i++) {
      assertTrue(reader.hasNext());
    }

    assertFalse(reader.hasNext());
  }

  @Test
  public void testNextArray() throws IOException {
    reader.beginArray();
    JsonArray array = reader.nextArray();
    reader.endArray();
    assertEquals(JsonArray.class, array.getClass());
  }

  @Test
  public void testNextBoolean() throws IOException {
    reader.beginObject();
    reader.name("bool");
    reader.nextBoolean();
    reader.endObject();
    assertTrue(reader.hasNext());
  }

  @Test
  public void testNextDouble() throws IOException {
    reader.beginObject();
    reader.name("number");
    reader.nextDouble();
    reader.endObject();
    assertTrue(reader.hasNext());
  }

  @Test
  public void testNextInt() throws IOException {
    reader.beginObject();
    reader.name("number");
    reader.nextInt();
    reader.endObject();
    assertTrue(reader.hasNext());
  }

  @Test
  public void testNextJsonObject() throws IOException {
    reader.beginObject();
    JsonObject object = reader.nextObject();
    reader.endObject();
    assertEquals(JsonObject.class, object.getClass());
  }

  @Test
  public void testNextJsonPrimitive() throws IOException {
    reader.beginObject();
    reader.name("string");
    reader.nextString();
    reader.name("bool");
    reader.nextBoolean();
    reader.name("number");
    reader.nextDouble();
    reader.name("int");
    reader.nextInt();
    reader.endObject();

    for (int i = 0; i < 5; i++) {
      assertTrue(reader.hasNext());
    }

    JsonElement element = reader.nextPrimitive();
    assertNull(element);
    assertFalse(reader.hasNext());
  }

  @Test
  public void testNextNull() throws IOException {
    reader.beginObject();
    reader.name("null");
    reader.nextNull();
    reader.endObject();
    assertTrue(reader.hasNext());
  }

  @Test
  public void testNextPrimitive() throws IOException {
    reader.beginObject();
    reader.name("string");
    reader.nextString();
    reader.name("bool");
    reader.nextBoolean();
    reader.name("number");
    reader.nextDouble();
    reader.name("int");
    reader.nextInt();
    reader.name("null");
    reader.nextNull();
    reader.endObject();

    for (int i = 0; i < 6; i++) {
      assertTrue(reader.hasNext());
    }

    JsonElement element = reader.nextPrimitive();
    assertNull(element);
    assertFalse(reader.hasNext());
  }

  @Test
  public void testNextString() throws IOException {
    reader.beginObject();
    reader.name("string");
    reader.nextString();
    reader.endObject();
    assertTrue(reader.hasNext());
  }

  @Test
  public void testPeek() throws IOException {
    reader.beginObject();
    reader.name("bool");
    reader.nextBoolean();
    reader.name("number");
    reader.nextDouble();
    reader.name("int");
    reader.nextInt();
    reader.name("string");
    reader.nextString();
    reader.name("null");
    reader.nextNull();
    reader.endObject();

    JsonToken token = reader.peek();
    assertTrue(token == JsonToken.NAME || token == JsonToken.BEGIN_ARRAY || token == JsonToken.BEGIN_OBJECT);
  }

  @Test
  public void testPushPop() throws IOException {
    reader.beginObject();
    reader.name("object");
    reader.push(reader.peekStack());
    reader.name("array");
    reader.nextArray();
    reader.nextInt();
    reader.pop();
    reader.endObject();
    reader.endArray();

    Object[] stack = reader.peekStack();
    assertSame(JsonObject.class, stack[stack.length - 1].getClass());
    reader.close();
  }

  @Test
  public void testSkipValue() throws IOException {
    reader.beginObject();
    reader.name("string");
    reader.nextString();
    reader.name("int");
    reader.nextInt();
    reader.skipValue();
    reader.name("bool");
    reader.nextBoolean();
    reader.endObject();

    assertTrue(reader.hasNext());
  }

  @Test
  public void testToString() throws IOException {
    reader.beginObject();
    reader.beginArray();
    reader.name("array");
    reader.nextString();
    reader.endArray();
    reader.name("object");
    reader.nextInt();
    reader.endObject();
    reader.endObject();

    assertEquals("$object.object", reader.getPath());
  }

  @Test
  public void testUnexpectedToken() throws IOException {
    reader.beginObject();
    reader.nextInt();
    reader.expect(JsonToken.NAME);
    reader.nextString();
    reader.endObject();
  }

  @Test
  public void testUnexpectedTokenAtEnd() throws IOException {
    reader.beginObject();
    reader.endObject();
    reader.expect(JsonToken.NAME);
  }

  @Test
  public void testUnexpectedTokenDuringStreaming() throws IOException {
    reader.beginArray();
    reader.beginArray();
    reader.expect(JsonToken.NAME);
    reader.nextInt();
    reader.endArray();
    reader.endArray();
  }

  @Test
  public void testValidateJson() throws IOException {
    ByteArrayInputStream inputStream = new ByteArrayInputStream("{\"bool\":true,\"int\":1,\"number\":3.14,\"string\":\"abc\",\"array\":[1, \"def\", null],\"object\":{\"name\":\"John\"}}".getBytes());
    reader = new JsonTreeReader(new InputStreamReader(inputStream));

    reader.nextBoolean();
    reader.nextInt();
    reader.nextDouble();
    reader.nextString();
    reader.beginArray();
    reader.nextInt();
    reader.nextString();
    reader.nextNull();
    reader.endArray();
    reader.nextObject();
    reader.nextString();
    reader.endObject();

    reader.close();
  }
}