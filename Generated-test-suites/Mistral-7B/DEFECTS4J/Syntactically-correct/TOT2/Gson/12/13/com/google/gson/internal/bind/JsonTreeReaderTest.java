import org.junit.Test;
import static org.junit.Assert.*;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class JsonTreeReaderTest {
  @Test
  public void testBeginArray() throws IOException {
    String jsonString = "[1, 2, 3]";
    JsonElement jsonElement = JsonParser.parseString(jsonString);
    JsonTreeReader reader = new JsonTreeReader(jsonElement);
    reader.beginArray();
    assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());
    reader.endArray();
    assertEquals(JsonToken.END_ARRAY, reader.peek());
    reader.close();
  }

  @Test(expected = IllegalStateException.class)
  public void testBeginArrayAfterEndArray() throws IOException {
    String jsonString = "[1, 2, 3]";
    JsonElement jsonElement = JsonParser.parseString(jsonString);
    JsonTreeReader reader = new JsonTreeReader(jsonElement);
    reader.endArray();
    reader.beginArray();
  }

  @Test
  public void testBeginObject() throws IOException {
    String jsonString = "{\"name\": \"John\"}";
    JsonElement jsonElement = JsonParser.parseString(jsonString);
    JsonTreeReader reader = new JsonTreeReader(jsonElement);
    reader.beginObject();
    assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
    reader.endObject();
    assertEquals(JsonToken.END_OBJECT, reader.peek());
    reader.close();
  }

  @Test(expected = IllegalStateException.class)
  public void testBeginObjectAfterEndObject() throws IOException {
    String jsonString = "{\"name\": \"John\"}";
    JsonElement jsonElement = JsonParser.parseString(jsonString);
    JsonTreeReader reader = new JsonTreeReader(jsonElement);
    reader.endObject();
    reader.beginObject();
  }

  @Test
  public void testHasNext() throws IOException {
    String jsonString = "[1, 2, 3]";
    JsonElement jsonElement = JsonParser.parseString(jsonString);
    JsonTreeReader reader = new JsonTreeReader(jsonElement);
    reader.beginArray();
    assertTrue(reader.hasNext());
    reader.endArray();
    assertFalse(reader.hasNext());
    reader.close();
  }

  @Test
  public void testPeek() throws IOException {
    String jsonString = "{\"name\": \"John\", \"age\": 25}";
    JsonElement jsonElement = JsonParser.parseString(jsonString);
    JsonTreeReader reader = new JsonTreeReader(jsonElement);
    reader.beginObject();
    reader.nextName();
    assertEquals(JsonToken.NAME, reader.peek());
    reader.nextName();
    reader.endObject();
    reader.close();
  }

  @Test
  public void testNextName() throws IOException {
    String jsonString = "{\"name\": \"John\", \"age\": 25}";
    JsonElement jsonElement = JsonParser.parseString(jsonString);
    JsonTreeReader reader = new JsonTreeReader(jsonElement);
    reader.beginObject();
    assertEquals("name", reader.nextName());
    assertEquals("age", reader.nextName());
    reader.endObject();
    reader.close();
  }

  @Test
  public void testNextString() throws IOException {
    String jsonString = "{\"name\": \"John\"}";
    JsonElement jsonElement = JsonParser.parseString(jsonString);
    JsonTreeReader reader = new JsonTreeReader(jsonElement);
    reader.beginObject();
    reader.nextName();
    reader.nextString();
    assertEquals("John", reader.nextString());
    reader.endObject();
    reader.close();
  }

  @Test
  public void testNextBoolean() throws IOException {
    String jsonString = "{\"is_male\": true}";
    JsonElement jsonElement = JsonParser.parseString(jsonString);
    JsonTreeReader reader = new JsonTreeReader(jsonElement);
    reader.beginObject();
    reader.nextName();
    reader.nextBoolean();
    assertTrue(reader.nextBoolean());
    reader.endObject();
    reader.close();
  }

  @Test
  public void testNextNull() throws IOException {
    String jsonString = "{\"age\": null}";
    JsonElement jsonElement = JsonParser.parseString(jsonString);
    JsonTreeReader reader = new JsonTreeReader(jsonElement);
    reader.beginObject();
    reader.nextName();
    reader.nextNull();
    reader.endObject();
    reader.close();
  }

  @Test
  public void testNextDouble() throws IOException {
    String jsonString = "{\"height\": 1.75}";
    JsonElement jsonElement = JsonParser.parseString(jsonString);
    JsonTreeReader reader = new JsonTreeReader(jsonElement);
    reader.beginObject();
    reader.nextName();
    reader.nextDouble();
    assertEquals(1.75, reader.nextDouble(), 0.01);
    reader.endObject();
    reader.close();
  }

  @Test
  public void testNextLong() throws IOException {
    String jsonString = "{\"age\": 25}";
    JsonElement jsonElement = JsonParser.parseString(jsonString);
    JsonTreeReader reader = new JsonTreeReader(jsonElement);
    reader.beginObject();
    reader.nextName();
    reader.nextLong();
    assertEquals(25, reader.nextLong());
    reader.endObject();
    reader.close();
  }

  @Test
  public void testNextInt() throws IOException {
    String jsonString = "{\"age\": 25}";
    JsonElement jsonElement = JsonParser.parseString(jsonString);
    JsonTreeReader reader = new JsonTreeReader(jsonElement);
    reader.beginObject();
    reader.nextName();
    reader.nextInt();
    assertEquals(25, reader.nextInt());
    reader.endObject();
    reader.close();
  }
}