import org.junit.Test;
import static org.junit.Assert.*;

public class JsonTreeReaderTest {
  @Test
  public void testBeginArray() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonArray());
    reader.beginArray();
    reader.endArray();
    assertEquals(JsonToken.END_ARRAY, reader.peek());
  }

  @Test
  public void testBeginObject() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonObject());
    reader.beginObject();
    reader.endObject();
    assertEquals(JsonToken.END_OBJECT, reader.peek());
  }

  @Test
  public void testHasNext() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonArray());
    reader.beginArray();
    reader.endArray();
    reader.beginArray();
    reader.endArray();
    reader.beginObject();
    reader.endObject();
    reader.beginArray();
    reader.endArray();
    assertFalse(reader.hasNext());
  }

  @Test(expected = AssertionError.class)
  public void testEmptyReader() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonNull());
    reader.beginArray();
  }

  @Test(expected = AssertionError.class)
  public void testClosedReader() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonNull());
    reader.close();
    reader.beginArray();
  }
}