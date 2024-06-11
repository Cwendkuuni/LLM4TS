import org.junit.Test;
import static org.junit.Assert.*;

public class JsonTreeReaderTest {

  @Test
  public void testEmptyJson() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{}"));
    assertEquals(JsonToken.END_DOCUMENT, reader.peek());
    reader.close();
  }

  @Test
  public void testSimpleJsonObject() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{\"name\": \"John\", \"age\": 30}"));
    assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
    reader.beginObject();
    assertEquals("name", reader.nextName());
    assertEquals("John", reader.nextString());
    reader.nextName(); // skip "age"
    reader.nextInt(); // read "30" as int
    reader.endObject();
    reader.nextNull(); // read empty value
    reader.endObject();
    reader.close();
  }

  @Test
  public void testSimpleJsonArray() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("[1, 2, 3]"));
    assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());
    reader.beginArray();
    reader.nextInt(); // read "1" as int
    reader.nextInt(); // read "2" as int
    reader.nextInt(); // read "3" as int
    reader.endArray();
    reader.nextNull(); // read empty value
    reader.endObject();
    reader.close();
  }

  @Test
  public void testJsonWithBoolean() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{\"name\": true, \"age\": 30}"));
    assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
    reader.beginObject();
    reader.nextName(); // skip "name"
    reader.nextBoolean(); // read "true" as boolean
    reader.nextName(); // skip "age"
    reader.nextInt(); // read "30" as int
    reader.endObject();
    reader.nextNull(); // read empty value
    reader.endObject();
    reader.close();
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonWithMissingValue() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{\"name\":}"));
    reader.nextName(); // try to read missing value
  }

  @Test
  public void testJsonWithEmptyArray() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("[]"));
    assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());
    reader.beginArray();
    reader.endArray();
    reader.nextNull(); // read empty value
    reader.endObject();
    reader.close();
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonWithUnexpectedJsonToken() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{42}"));
    reader.nextName(); // try to read name
  }

  @Test
  public void testJsonWithNullValue() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{\"name\": null}"));
    assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
    reader.beginObject();
    reader.nextName(); // skip "name"
    reader.nextNull(); // read null value
    reader.endObject();
    reader.nextNull(); // read empty value
    reader.endObject();
    reader.close();
  }

  @Test
  public void testJsonWithEmptyObject() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{}"));
    assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
    reader.beginObject();
    reader.endObject();
    reader.nextNull(); // read empty value
    reader.endObject();
    reader.close();
  }

  @Test(expected = AssertionError.class)
  public void testJsonReaderClosed() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{}"));
    reader.close();
    reader.nextInt(); // try to read after closing
  }
}