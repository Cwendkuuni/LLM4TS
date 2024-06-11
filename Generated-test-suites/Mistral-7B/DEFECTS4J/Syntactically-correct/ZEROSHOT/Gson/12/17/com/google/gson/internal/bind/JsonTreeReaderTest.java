import static org.junit.Assert.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.io.IOException;
import java.util.Arrays;

public class JsonTreeReaderTest {

  @ org.junit.Test
  public void testEmptyJsonElement() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonNull.INSTANCE);
    assertFalse(reader.hasNext());
    reader.close();
  }

  @ org.junit.Test
  public void testBeginArray() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonArray());
    reader.beginArray();
    assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());
    reader.endArray();
    assertEquals(JsonToken.END_ARRAY, reader.peek());
    reader.close();
  }

  @ org.junit.Test
  public void testBeginArrayWithEmptyIterable() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonArray());
    reader.beginArray();
    reader.endArray();
    reader.beginArray();
    assertFalse(reader.hasNext());
    reader.close();
  }

  @ org.junit.Test
  public void testBeginArrayWithNonEmptyIterable() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonArray() {
      {
        add(new JsonPrimitive("element1"));
        add(new JsonPrimitive("element2"));
      }
    });
    reader.beginArray();
    assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());
    assertEquals("element1", reader.nextString());
    assertEquals(JsonToken.STRING, reader.peek());
    assertEquals("element2", reader.nextString());
    assertEquals(JsonToken.END_ARRAY, reader.peek());
    reader.endArray();
    assertEquals(JsonToken.END_ARRAY, reader.peek());
    reader.close();
  }

  @ org.junit.Test
  public void testBeginObject() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonObject() {
      {
        addProperty("name1", new JsonPrimitive("value1"));
        addProperty("name2", new JsonPrimitive("value2"));
      }
    });
    reader.beginObject();
    assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
    assertEquals("name1", reader.nextName());
    assertEquals("value1", reader.nextString());
    assertEquals(JsonToken.NAME, reader.peek());
    assertEquals("name2", reader.nextName());
    assertEquals("value2", reader.nextString());
    assertEquals(JsonToken.END_OBJECT, reader.peek());
    reader.endObject();
    assertEquals(JsonToken.END_OBJECT, reader.peek());
    reader.close();
  }

  @ org.junit.Test
  public void testBeginObjectWithEmptyEntrySet() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonObject());
    reader.beginObject();
    reader.endObject();
    reader.beginObject();
    assertFalse(reader.hasNext());
    reader.close();
  }

  @ org.junit.Test
  public void testBeginObjectWithNonEmptyEntrySet() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonObject() {
      {
        addProperty("name1", new JsonPrimitive("value1"));
        addProperty("name2", new JsonPrimitive("value2"));
      }
    });
    reader.beginObject();
    reader.nextName();
    reader.nextString(); // consume name1
    reader.nextName();
    reader.nextString(); // consume name2
    reader.nextName(); // expect name3 to fail
    reader.close();
  }

  @ org.junit.Test
  public void testEndArray() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonArray() {
      {
        add(new JsonPrimitive("element1"));
        add(new JsonPrimitive("element2"));
      }
    });
    reader.beginArray();
    reader.nextString();
    reader.nextString();
    reader.endArray();
    assertFalse(reader.hasNext());
    reader.close();
  }

  @ org.junit.Test
  public void testEndArrayWithEmptyIterable() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonArray());
    reader.beginArray();
    reader.endArray();
    reader.endArray();
    assertFalse(reader.hasNext());
    reader.close();
  }

  @ org.junit.Test
  public void testEndObject() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonObject() {
      {
        addProperty("name1", new JsonPrimitive("value1"));
        addProperty("name2", new JsonPrimitive("value2"));
      }
    });
    reader.beginObject();
    reader.nextName();
    reader.nextString(); // consume name1
    reader.nextName();
    reader.nextString(); // consume name2
    reader.endObject();
    assertFalse(reader.hasNext());
    reader.close();
  }

  @ org.junit.Test
  public void testHasNext() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonArray() {
      {
        add(new JsonPrimitive("element1"));
        add(new JsonPrimitive("element2"));
      }
    });
    reader.beginArray();
    assertTrue(reader.hasNext());
    reader.nextString();
    assertTrue(reader.hasNext());
    reader.nextString();
    assertFalse(reader.hasNext());
    reader.endArray();
    reader.endArray();
    reader.close();
  }

  @ org.junit.Test
  public void testPeek() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonObject() {
      {
        addProperty("name1", new JsonPrimitive("value1"));
        addProperty("name2", new JsonPrimitive("value2"));
      }
    });
    reader.beginObject();
    reader.nextName();
    reader.nextString(); // consume name1
    assertEquals("name2", reader.nextName().toString());
    assertEquals("value2", reader.nextString());
    assertEquals(JsonToken.END_OBJECT, reader.peek().getValue());
    reader.endObject();
    assertFalse(reader.hasNext());
    reader.close();
  }

  @ org.junit.Test
  public void testPeekStack() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonArray() {
      {
        add(new JsonPrimitive("element1"));
        add(new JsonPrimitive("element2"));
      }
    });
    reader.beginArray();
    assertEquals("element1", reader.nextString());
    assertEquals(JsonToken.BEGIN_ARRAY, reader.peek().getValue());
    reader.endArray();
    assertEquals(JsonToken.END_ARRAY, reader.peek().getValue());
    reader.close();
  }

  @ org.junit.Test
  public void testPopStack() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonArray() {
      {
        add(new JsonPrimitive("element1"));
        add(new JsonPrimitive("element2"));
      }
    });
    reader.beginArray();
    reader.nextString();
    reader.nextString();
    reader.endArray();
    reader.popStack();
    assertEquals(JsonToken.BEGIN_ARRAY, reader.peek().getValue());
    assertEquals("element1", reader.nextString());
    reader.endArray();
    reader.close();
  }

  @ org.junit.Test
  public void testSkipValue() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonObject() {
      {
        addProperty("name1", new JsonPrimitive("value1"));
        addProperty("name2", new JsonPrimitive("value2"));
      }
    });
    reader.beginObject();
    reader.nextName();
    reader.nextString(); // consume name1
    reader.skipValue();
    reader.nextName();
    reader.nextString(); // consume name2
    reader.nextName(); // expect name3 to fail
    reader.close();
  }

  @ org.junit.Test
  public void testToString() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonObject() {
      {
        addProperty("name1", new JsonPrimitive("value1"));
        addProperty("name2", new JsonPrimitive("value2"));
      }
    });
    String result = reader.toString();
    assertEquals("JsonTreeReader", result);
    reader.beginObject();
    reader.nextName();
    reader.nextString(); // consume name1
    reader.nextName();
    reader.nextString(); // consume name2
    reader.close();
    reader.close();
  }
}