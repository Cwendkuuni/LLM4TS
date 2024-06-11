import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonTreeReaderTest {

  @Test
  public void testJsonTreeReaderEmpty() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.NULL);
    assertFalse(reader.hasNext());
    reader.close();
  }

  @Test
  public void testJsonTreeReaderArray() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(new JsonArray());

    reader.beginArray();
    assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());

    reader.nextNull();
    reader.nextNull();

    reader.beginArray();
    assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());

    reader.nextInt();
    assertEquals(JsonToken.NUMBER, reader.peek());
    assertEquals(0, reader.nextInt());

    reader.endArray();
    assertEquals(JsonToken.END_ARRAY, reader.peek());

    reader.endArray();
    assertEquals(JsonToken.END_ARRAY, reader.peek());

    reader.close();
  }

  @Test
  public void testJsonTreeReaderObject() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(new JsonObject());

    reader.beginObject();
    assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());

    reader.nextName();
    assertEquals("", reader.nextString());
    reader.nextName();
    assertEquals("key", reader.nextString());
    reader.nextBoolean();
    reader.nextName();
    reader.nextNull();

    reader.endObject();
    assertEquals(JsonToken.END_OBJECT, reader.peek());
    reader.nextName(); // should throw an exception

    reader.close();
  }

  @Test
  public void testJsonTreeReaderMixed() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(new JsonObject().add("array", new JsonArray().add(1).add(2)));

    reader.beginObject();
    assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());

    reader.nextName();
    assertEquals("array", reader.nextString());
    reader.beginArray();
    assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());

    reader.nextInt();
    assertEquals(1, reader.nextInt());

    reader.nextNull();
    reader.nextInt();
    assertEquals(2, reader.nextInt());

    reader.endArray();
    assertEquals(JsonToken.END_ARRAY, reader.peek());

    reader.endObject();
    assertEquals(JsonToken.END_OBJECT, reader.peek());

    reader.close();
  }

  @Test
  public void testJsonTreeReaderNull() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.NULL);

    reader.nextNull();
    reader.close();
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonTreeReaderClosed() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.NULL);
    reader.close();
    reader.nextInt();
  }
}