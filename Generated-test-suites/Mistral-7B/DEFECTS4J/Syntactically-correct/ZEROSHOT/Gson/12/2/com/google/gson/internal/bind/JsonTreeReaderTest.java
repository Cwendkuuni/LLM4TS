import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import org.junit.Before;
import org.junit.Test;

public class JsonTreeReaderTest {

  private JsonElement element;
  private JsonTreeReader reader;

  @Before
  public void setUp() {
    JsonObject jsonObject = new JsonObject();
    JsonArray jsonArray = new JsonArray();
    jsonObject.add("name1", new JsonPrimitive("value1"));
    jsonObject.add("name2", jsonArray);
    jsonArray.add(new JsonPrimitive(1));
    jsonArray.add(new JsonPrimitive(2));
    element = jsonObject;
    reader = new JsonTreeReader(element);
  }

  @Test
  public void testBeginArray() throws IOException {
    reader.beginArray();
    assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());
  }

  @Test(expected = IllegalStateException.class)
  public void testBeginArrayAfterEndArray() throws IOException {
    reader.endArray();
    reader.beginArray();
  }

  @Test
  public void testEndArray() throws IOException {
    reader.beginArray();
    reader.endArray();
    assertEquals(JsonToken.END_ARRAY, reader.peek());
  }

  @Test(expected = IllegalStateException.class)
  public void testEndArrayAfterEndDocument() throws IOException {
    reader.close();
    reader.endArray();
  }

  @Test
  public void testHasNext() throws IOException {
    reader.beginArray();
    reader.beginArray();
    assertTrue(reader.hasNext());
    reader.endArray();
    assertFalse(reader.hasNext());
  }

  @Test
  public void testPeek() throws IOException {
    reader.beginArray();
    reader.beginArray();
    assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());
    reader.endArray();
    assertEquals(JsonToken.END_ARRAY, reader.peek());
  }

  @Test
  public void testPopStack() throws IOException {
    reader.beginObject();
    reader.nextName();
    reader.nextName();
    reader.endObject();
    reader.endArray();
    reader.endArray();
    reader.popStack();
    reader.popStack();
    reader.popStack();
    reader.popStack();
    reader.popStack();
    assertEquals(JsonToken.END_DOCUMENT, reader.peek());
  }

  @Test
  public void testExpect() throws IOException {
    reader.expect(JsonToken.BEGIN_ARRAY);
    reader.beginArray();
    reader.expect(JsonToken.NAME);
    reader.nextName();
    reader.expect(JsonToken.END_ARRAY);
    reader.endArray();
  }

  @Test(expected = IllegalStateException.class)
  public void testExpectUnexpectedObject() throws IOException {
    reader.expect(JsonToken.BEGIN_ARRAY);
    reader.beginObject();
  }

  @Test(expected = IllegalStateException.class)
  public void testExpectUnexpectedArray() throws IOException {
    reader.expect(JsonToken.BEGIN_ARRAY);
    reader.endObject();
  }

  @Test
  public void testExpectUnexpectedNull() throws IOException {
    reader.expect(JsonToken.NULL);
    reader.nextNull();
  }

  @Test(expected = IllegalStateException.class)
  public void testExpectUnexpectedString() throws IOException {
    reader.expect(JsonToken.STRING);
    reader.nextString();
  }

  @Test
  public void testExpectUnexpectedBoolean() throws IOException {
    reader.expect(JsonToken.BOOLEAN);
    reader.nextBoolean();
  }

  @Test
  public void testExpectUnexpectedNumber() throws IOException {
    reader.expect(JsonToken.NUMBER);
    reader.nextInt();
  }

  @Test
  public void testBeginObject() throws IOException {
    reader.beginObject();
    assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
  }

  @Test(expected = IllegalStateException.class)
  public void testBeginObjectAfterEndObject() throws IOException {
    reader.endObject();
    reader.beginObject();
  }

  @Test
  public void testEndObject() throws IOException {
    reader.beginObject();
    reader.endObject();
    assertEquals(JsonToken.END_OBJECT, reader.peek());
  }

  @Test(expected = IllegalStateException.class)
  public void testEndObjectAfterEndDocument() throws IOException {
    reader.close();
    reader.endObject();
  }

  @Test
  public void testNextName() throws IOException {
    reader.beginObject();
    reader.nextName();
    assertEquals("name1", reader.nextString());
  }

  @Test(expected = IllegalStateException.class)
  public void testNextNameAfterEndObject() throws IOException {
    reader.endObject();
    reader.nextName();
  }

  @Test
  public void testNextNameAfterEndDocument() throws IOException {
    reader.close();
    reader.nextName();
  }

  @Test
  public void testNextString() throws IOException {
    reader.beginObject();
    reader.nextName();
    assertEquals("value1", reader.nextString());
  }

  @Test(expected = IllegalStateException.class)
  public void testNextStringAfterEndDocument() throws IOException {
    reader.close();
    reader.nextString();
  }

  @Test
  public void testNextBoolean() throws IOException {
    reader.beginObject();
    reader.nextName();
    reader.nextName();
    reader.nextBoolean();
    assertEquals(true, reader.nextBoolean());
  }

  @Test(expected = IllegalStateException.class)
  public void testNextBooleanAfterEndDocument() throws IOException {
    reader.close();
    reader.nextBoolean();
  }

  @Test
  public void testNextNull() throws IOException {
    reader.beginObject();
    reader.nextName();
    reader.nextNull();
    assertNull(reader.nextString());
  }

  @Test(expected = IllegalStateException.class)
  public void testNextNullAfterEndDocument() throws IOException {
    reader.close();
    reader.nextNull();
  }

  @Test
  public void testNextDouble() throws IOException {
    reader.beginObject();
    reader.nextName();
    reader.nextName();
    reader.nextDouble();
    assertEquals(1.0, reader.nextDouble(), 0.0);
  }

  @Test(expected = IllegalStateException.class)
  public void testNextDoubleAfterEndDocument() throws IOException {
    reader.close();
    reader.nextDouble();
  }

  @Test
  public void testNextLong() throws IOException {
    reader.beginObject();
    reader.nextName();
    reader.nextName();
    reader.nextLong();
    assertEquals(1, reader.nextInt());
  }

  @Test(expected = IllegalStateException.class)
  public void testNextLongAfterEndDocument() throws IOException {
    reader.close();
    reader.nextLong();
  }

  @Test
  public void testNextInt() throws IOException {
    reader.beginObject();
    reader.nextName();
    reader.nextName();
    reader.nextInt();
    assertEquals(1, reader.nextInt());
  }

  @Test
  public void testNextIntAfterEndArray() throws IOException {
    reader.beginArray();
    reader.endArray();
    reader.nextInt();
  }

  @Test(expected = IllegalStateException.class)
  public void testNextIntAfterEndDocument() throws IOException {
    reader.close();
    reader.nextInt();
  }

  @Test
  public void testClose() throws IOException {
    reader.close();
    assertEquals(JsonToken.END_DOCUMENT, reader.peek());
  }

  @Test(expected = IllegalStateException.class)
  public void testCloseAlreadyClosed() throws IOException {
    reader.close();
    reader.close();
  }

  @Test
  public void testSkipValue() throws IOException {
    reader.nextName();
    reader.nextName();
    reader.nextName();
    reader.skipValue();
    reader.nextInt();
  }

  @Test(expected = IllegalStateException.class)
  public void testSkipValueAfterEndDocument() throws IOException {
    reader.close();
    reader.skipValue();
  }

  @Test
  public void testPushNameToValue() throws IOException {
    reader.nextName();
    reader.promoteNameToValue();
    reader.nextString();
    assertEquals("name1", reader.nextString());
  }

  @Test
  public void testPath() throws IOException {
    reader.beginArray();
    reader.beginArray();
    reader.nextName();
    reader.nextName();
    reader.nextBoolean();
    reader.nextInt();
    reader.endArray();
    reader.endArray();
    reader.close();

    String expectedPath = "$[0].[name1].[]";
    assertEquals(expectedPath, reader.getPath());
  }
}