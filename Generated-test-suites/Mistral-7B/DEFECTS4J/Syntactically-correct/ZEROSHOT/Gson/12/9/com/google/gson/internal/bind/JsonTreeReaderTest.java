import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonTreeReaderTest {

  private JsonTreeReader reader;
  private static final String JSON_STRING =
      "{\"name\":\"John\",\"age\":30,\"city\":\"New York\",\"hobbies\":[\"reading\",\"swimming\"]}";

  @Before
  public void setUp() {
    reader = new JsonTreeReader(new JsonElement() {
      @Override
      public JsonArray getAsJsonArray() {
        return null;
      }

      @Override
      public JsonObject getAsJsonObject() {
        return new JsonObject() {{
          addProperty("name", "John");
          addProperty("age", 30);
          addProperty("city", "New York");
          add("hobbies", new JsonArray() {{
            add(new JsonPrimitive("reading"));
            add(new JsonPrimitive("swimming"));
          }});
        }};
      }

      @Override
      public JsonPrimitive getAsJsonPrimitive() {
        return null;
      }

      @Override
      public JsonNull getAsJsonNull() {
        return null;
      }

      @Override
      public String getAsString() {
        return JSON_STRING;
      }

      @Override
      public int read(char[] cbuf, int off, int len) {
        throw new UnsupportedOperationException();
      }

      @Override
      public void close() {}
    });
  }

  @Test
  public void testBeginArray() throws IOException {
    reader.beginArray();
    assertTrue(reader.hasNext());
  }

  @Test(expected = IllegalStateException.class)
  public void testBeginArrayAlreadyBegin() throws IOException {
    reader.beginArray();
    reader.beginArray();
  }

  @Test
  public void testEndArray() throws IOException {
    reader.beginArray();
    reader.endArray();
    assertFalse(reader.hasNext());
  }

  @Test(expected = IllegalStateException.class)
  public void testEndArrayNotBegin() throws IOException {
    reader.nextString();
    reader.endArray();
  }

  @Test
  public void testBeginObject() throws IOException {
    reader.beginObject();
    assertTrue(reader.hasNext());
  }

  @Test(expected = IllegalStateException.class)
  public void testBeginObjectAlreadyBegin() throws IOException {
    reader.beginObject();
    reader.beginObject();
  }

  @Test
  public void testEndObject() throws IOException {
    reader.beginObject();
    reader.endObject();
    assertFalse(reader.hasNext());
  }

  @Test(expected = IllegalStateException.class)
  public void testEndObjectNotBegin() throws IOException {
    reader.nextString();
    reader.endObject();
  }

  @Test
  public void testHasNext() throws IOException {
    reader.beginArray();
    reader.beginObject();
    reader.nextName();
    reader.nextBoolean();
    reader.nextNull();
    reader.nextString();
    reader.endObject();
    reader.endArray();

    assertTrue(reader.hasNext());
    reader.nextInt();
    assertFalse(reader.hasNext());
  }

  @Test(expected = IllegalStateException.class)
  public void testHasNextNotBegin() throws IOException {
    reader.nextInt();
    reader.hasNext();
  }

  @Test
  public void testPeek() throws IOException {
    reader.beginArray();
    reader.beginObject();
    reader.nextName();
    reader.nextBoolean();
    reader.nextNull();
    reader.nextString();
    reader.endObject();
    reader.endArray();

    assertEquals(JsonToken.BEGIN_ARRAY, reader.peek().getAsJsonArray().getType());
  }

  @Test
  public void testPopStack() throws IOException {
    reader.beginArray();
    reader.beginObject();
    reader.nextName();
    reader.nextBoolean();
    reader.nextNull();
    reader.nextString();
    reader.endObject();
    reader.endArray();

    reader.popStack();
    reader.popStack();

    assertEquals(JsonToken.END_DOCUMENT, reader.peek().getAsJsonArray().getType());
  }

  @Test
  public void testExpect() throws IOException {
    reader.beginArray();
    reader.beginObject();
    reader.nextName();
    reader.nextBoolean();
    reader.nextNull();
    reader.nextString();
    reader.endObject();
    reader.endArray();

    reader.expect(JsonToken.BEGIN_OBJECT);

    assertTrue(reader.hasNext());
    reader.nextName();
  }

  @Test(expected = IllegalStateException.class)
  public void testExpectNotBegin() throws IOException {
    reader.nextInt();
    reader.expect(JsonToken.BEGIN_OBJECT);
  }

  @Test
  public void testNextName() throws IOException {
    reader.beginArray();
    reader.beginObject();
    reader.nextName();
    assertEquals("name", reader.nextName());
    reader.endObject();
    reader.endArray();
  }

  @Test(expected = IllegalStateException.class)
  public void testNextNameNotBeginObject() throws IOException {
    reader.nextName();
    reader.nextName();
  }

  @Test
  public void testNextString() throws IOException {
    reader.beginArray();
    reader.beginObject();
    reader.nextName();
    reader.nextString();
    reader.endObject();
    reader.endArray();

    assertEquals("John", reader.nextString());
  }

  @Test(expected = IllegalStateException.class)
  public void testNextStringNotBegin() throws IOException {
    reader.nextString();
    reader.nextString();
  }

  @Test
  public void testNextBoolean() throws IOException {
    reader.beginArray();
    reader.beginObject();
    reader.nextName();
    reader.nextBoolean();
    reader.endObject();
    reader.endArray();

    assertEquals(true, reader.nextBoolean());
  }

  @Test(expected = IllegalStateException.class)
  public void testNextBooleanNotBegin() throws IOException {
    reader.nextBoolean();
    reader.nextBoolean();
  }

  @Test
  public void testNextNull() throws IOException {
    reader.beginArray();
    reader.beginObject();
    reader.nextName();
    reader.nextNull();
    reader.endObject();
    reader.endArray();

    assertNull(reader.nextNull());
  }

  @Test(expected = IllegalStateException.class)
  public void testNextNullNotBegin() throws IOException {
    reader.nextNull();
    reader.nextNull();
  }

  @Test
  public void testNextDouble() throws IOException {
    reader.beginArray();
    reader.beginObject();
    reader.nextName();
    reader.nextDouble();
    reader.endObject();
    reader.endArray();

    assertEquals(30.0, reader.nextDouble(), 0.0);
  }

  @Test(expected = IllegalStateException.class)
  public void testNextDoubleNotBegin() throws IOException {
    reader.nextDouble();
    reader.nextDouble();
  }

  @Test
  public void testNextLong() throws IOException {
    reader.beginArray();
    reader.beginObject();
    reader.nextName();
    reader.nextLong();
    reader.endObject();
    reader.endArray();

    assertEquals(30, reader.nextLong());
  }

  @Test(expected = IllegalStateException.class)
  public void testNextLongNotBegin() throws IOException {
    reader.nextLong();
    reader.nextLong();
  }

  @Test
  public void testNextInt() throws IOException {
    reader.beginArray();
    reader.beginObject();
    reader.nextName();
    reader.nextInt();
    reader.endObject();
    reader.endArray();

    assertEquals(30, reader.nextInt());
  }

  @Test(expected = IllegalStateException.class)
  public void testNextIntNotBegin() throws IOException {
    reader.nextInt();
    reader.nextInt();
  }

  @Test
  public void testClose() throws IOException {
    reader.close();
    assertEquals(JsonTreeReader.class.getSimpleName(), reader.toString());
  }

  @Test(expected = IllegalStateException.class)
  public void testCloseAlreadyClosed() throws IOException {
    reader.close();
    reader.close();
  }

  @Test
  public void testSkipValue() throws IOException {
    reader.beginArray();
    reader.beginObject();
    reader.nextName();
    reader.nextString();
    reader.endObject();
    reader.endArray();

    reader.skipValue();
    reader.nextBoolean();
  }

  @Test(expected = IllegalStateException.class)
  public void testSkipValueNotBegin() throws IOException {
    reader.nextBoolean();
    reader.skipValue();
  }

  @Test
  public void testPromoteNameToValue() throws IOException {
    reader.beginArray();
    reader.beginObject();
    reader.nextName();
    reader.promoteNameToValue();
    assertEquals("John", reader.nextString());
    reader.endObject();
    reader.endArray();
  }

  @Test(expected = IllegalStateException.class)
  public void testPromoteNameToValueNotBegin() throws IOException {
    reader.nextString();
    reader.promoteNameToValue();
  }

  @Test
  public void testGetPath() throws IOException {
    reader.beginArray();
    reader.beginObject();
    reader.nextName();
    reader.nextBoolean();
    reader.nextNull();
    reader.nextString();
    reader.endObject();
    reader.endArray();

    assertEquals("$", reader.getPath());
    reader.promoteNameToValue();
    assertEquals("$.hobbies[0]", reader.getPath());
  }
}