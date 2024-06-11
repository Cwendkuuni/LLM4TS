import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonTreeReaderTest {
  private JsonTreeReader reader;
  private JsonElement jsonElement;

  @Before
  public void setup() {
    jsonElement = new JsonObject();
    reader = new JsonTreeReader(jsonElement);
  }

  @Test
  public void testBeginArray() throws IOException {
    reader.beginArray();
    JsonArray array = (JsonArray) reader.peekStack();
    reader.endArray();
    assertFalse(reader.hasNext());
  }

  @Test
  public void testEndArray() throws IOException {
    reader.beginArray();
    reader.endArray();
    reader.beginArray();
    JsonArray array = (JsonArray) reader.peekStack();
    reader.endArray();
    assertFalse(reader.hasNext());
  }

  @Test
  public void testBeginObject() throws IOException {
    reader.beginObject();
    JsonObject object = (JsonObject) reader.peekStack();
    reader.endObject();
    assertFalse(reader.hasNext());
  }

  @Test
  public void testEndObject() throws IOException {
    reader.beginObject();
    reader.endObject();
    reader.beginObject();
    JsonObject object = (JsonObject) reader.peekStack();
    reader.endObject();
    assertFalse(reader.hasNext());
  }

  @Test(expected = IllegalStateException.class)
  public void testHasNextWhenClosed() throws IOException {
    reader.close();
    reader.hasNext();
  }

  @Test
  public void testPeek() throws IOException {
    reader.beginArray();
    reader.nextNull();
    reader.nextBoolean();
    reader.nextString("test");
    reader.nextInt(42);
    reader.nextDouble(3.14);
    JsonToken actualToken = reader.peek();
    assertEquals(JsonToken.END_ARRAY, actualToken);
  }

  @Test
  public void testNext() throws IOException {
    reader.beginArray();
    reader.nextNull();
    reader.nextBoolean(false);
    assertEquals("test", reader.nextString());
    reader.nextInt(42);
    reader.nextDouble(3.14);
    JsonToken actualToken = reader.peek();
    assertEquals(JsonToken.END_ARRAY, actualToken);
  }

  @Test
  public void testClose() throws IOException {
    reader.beginArray();
    reader.nextNull();
    reader.nextBoolean(false);
    reader.nextString("test");
    reader.nextInt(42);
    reader.nextDouble(3.14);
    reader.endArray();
    reader.close();
    assertFalse(reader.hasNext());
  }
}