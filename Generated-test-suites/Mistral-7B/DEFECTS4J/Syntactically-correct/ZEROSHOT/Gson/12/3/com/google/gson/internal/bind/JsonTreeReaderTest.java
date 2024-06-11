import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.Test;

public class JsonTreeReaderTest {

  @Test
  public void testJsonTreeReader_emptyJsonElement() throws IOException {
    JsonElement emptyJsonElement = JsonElement.NULL;
    JsonTreeReader jsonTreeReader = new JsonTreeReader(emptyJsonElement);
    jsonTreeReader.close();

    assertFalse(jsonTreeReader.hasNext());
  }

  @Test
  public void testJsonTreeReader_jsonObject() throws IOException {
    String jsonString = "{\"name\":\"John\",\"age\":30,\"city\":\"New York\"}";
    JsonElement jsonElement = new JsonParser().parse(jsonString);
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);

    assertEquals("name", jsonTreeReader.nextName());
    assertEquals("John", jsonTreeReader.nextString());
    assertFalse(jsonTreeReader.hasNext());

    jsonTreeReader.close();
  }

  @Test
  public void testJsonTreeReader_jsonArray() throws IOException {
    String jsonString = "[1, 2, 3, 4]";
    JsonElement jsonElement = new JsonParser().parse(jsonString);
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);

    assertEquals(JsonToken.BEGIN_ARRAY, jsonTreeReader.peek().getAsInt());
    jsonTreeReader.beginArray();

    assertEquals(JsonToken.NUMBER, jsonTreeReader.peek().getAsInt());
    assertEquals(1.0, jsonTreeReader.nextDouble());

    assertEquals(JsonToken.COMMMA, jsonTreeReader.peek().getAsInt());
    jsonTreeReader.nextString(); // consume comma

    assertEquals(JsonToken.NUMBER, jsonTreeReader.peek().getAsInt());
    assertEquals(2.0, jsonTreeReader.nextDouble());

    assertEquals(JsonToken.COMMMA, jsonTreeReader.peek().getAsInt());
    jsonTreeReader.nextString(); // consume comma

    assertEquals(JsonToken.NUMBER, jsonTreeReader.peek().getAsInt());
    assertEquals(3.0, jsonTreeReader.nextDouble());

    assertEquals(JsonToken.COMMMA, jsonTreeReader.peek().getAsInt());
    jsonTreeReader.nextString(); // consume comma

    assertEquals(JsonToken.NUMBER, jsonTreeReader.peek().getAsInt());
    assertEquals(4.0, jsonTreeReader.nextDouble());

    assertEquals(JsonToken.END_ARRAY, jsonTreeReader.peek().getAsInt());
    jsonTreeReader.endArray();

    assertEquals(JsonToken.END_DOCUMENT, jsonTreeReader.peek().getAsInt());
    jsonTreeReader.close();
  }

  @Test
  public void testJsonTreeReader_jsonObjectWithArray() throws IOException {
    String jsonString = "{\"name\":\"John\",\"ages\":[30, 35, 40],\"city\":\"New York\"}";
    JsonElement jsonElement = new JsonParser().parse(jsonString);
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);

    assertEquals("name", jsonTreeReader.nextName());
    assertEquals("John", jsonTreeReader.nextString());

    assertEquals(JsonToken.NAME, jsonTreeReader.peek().getAsString());
    assertEquals("ages", jsonTreeReader.nextName());

    assertEquals(JsonToken.BEGIN_ARRAY, jsonTreeReader.peek().getAsInt());
    jsonTreeReader.beginArray();

    assertEquals(JsonToken.NUMBER, jsonTreeReader.peek().getAsInt());
    assertEquals(30.0, jsonTreeReader.nextDouble());

    assertEquals(JsonToken.COMMMA, jsonTreeReader.peek().getAsInt());
    jsonTreeReader.nextString(); // consume comma

    assertEquals(JsonToken.NUMBER, jsonTreeReader.peek().getAsInt());
    assertEquals(35.0, jsonTreeReader.nextDouble());

    assertEquals(JsonToken.COMMMA, jsonTreeReader.peek().getAsInt());
    jsonTreeReader.nextString(); // consume comma

    assertEquals(JsonToken.NUMBER, jsonTreeReader.peek().getAsInt());
    assertEquals(40.0, jsonTreeReader.nextDouble());

    assertEquals(JsonToken.END_ARRAY, jsonTreeReader.peek().getAsInt());
    jsonTreeReader.endArray();

    assertEquals(JsonToken.NAME, jsonTreeReader.peek().getAsString());
    assertEquals("city", jsonTreeReader.nextName());

    assertEquals("New York", jsonTreeReader.nextString());

    assertEquals(JsonToken.END_OBJECT, jsonTreeReader.peek().getAsInt());
    jsonTreeReader.endObject();

    assertEquals(JsonToken.END_DOCUMENT, jsonTreeReader.peek().getAsInt());
    jsonTreeReader.close();
  }

  @Test
  public void testJsonTreeReader_jsonObjectWithNullValue() throws IOException {
    String jsonString = "{\"name\":\"John\",\"age\":null,\"city\":\"New York\"}";
    JsonElement jsonElement = new JsonParser().parse(jsonString);
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);

    assertEquals("name", jsonTreeReader.nextName());
    assertEquals("John", jsonTreeReader.nextString());

    assertEquals(JsonToken.NAME, jsonTreeReader.peek().getAsString());
    assertEquals("age", jsonTreeReader.nextName());

    assertNull(jsonTreeReader.nextNull());

    assertEquals(JsonToken.NAME, jsonTreeReader.peek().getAsString());
    assertEquals("city", jsonTreeReader.nextName());

    assertEquals("New York", jsonTreeReader.nextString());

    assertEquals(JsonToken.END_OBJECT, jsonTreeReader.peek().getAsInt());
    jsonTreeReader.endObject();

    assertEquals(JsonToken.END_DOCUMENT, jsonTreeReader.peek().getAsInt());
    jsonTreeReader.close();
  }

  @Test
  public void testJsonTreeReader_jsonObjectWithBooleanValue() throws IOException {
    String jsonString = "{\"name\":\"John\",\"isMarried\":true,\"city\":\"New York\"}";
    JsonElement jsonElement = new JsonParser().parse(jsonString);
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);

    assertEquals("name", jsonTreeReader.nextName());
    assertEquals("John", jsonTreeReader.nextString());

    assertEquals(JsonToken.NAME, jsonTreeReader.peek().getAsString());
    assertEquals("isMarried", jsonTreeReader.nextName());

    assertTrue(jsonTreeReader.nextBoolean());

    assertEquals(JsonToken.NAME, jsonTreeReader.peek().getAsString());
    assertEquals("city", jsonTreeReader.nextName());

    assertEquals("New York", jsonTreeReader.nextString());

    assertEquals(JsonToken.END_OBJECT, jsonTreeReader.peek().getAsInt());
    jsonTreeReader.endObject();

    assertEquals(JsonToken.END_DOCUMENT, jsonTreeReader.peek().getAsInt());
    jsonTreeReader.close();
  }

  @Test
  public void testJsonTreeReader_jsonObjectWithStringValue() throws IOException {
    String jsonString = "{\"name\":\"John\",\"favoriteColor\":\"red\",\"city\":\"New York\"}";
    JsonElement jsonElement = new JsonParser().parse(jsonString);
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);

    assertEquals("name", jsonTreeReader.nextName());
    assertEquals("John", jsonTreeReader.nextString());

    assertEquals(JsonToken.NAME, jsonTreeReader.peek().getAsString());
    assertEquals("favoriteColor", jsonTreeReader.nextName());

    assertEquals("red", jsonTreeReader.nextString());

    assertEquals(JsonToken.NAME, jsonTreeReader.peek().getAsString());
    assertEquals("city", jsonTreeReader.nextName());

    assertEquals("New York", jsonTreeReader.nextString());

    assertEquals(JsonToken.END_OBJECT, jsonTreeReader.peek().getAsInt());
    jsonTreeReader.endObject();

    assertEquals(JsonToken.END_DOCUMENT, jsonTreeReader.peek().getAsInt());
    jsonTreeReader.close();
  }

  @Test
  public void testJsonTreeReader_jsonObjectWithNumberValue() throws IOException {
    String jsonString = "{\"name\":\"John\",\"height\":1.8,\"weight\":70.0,\"city\":\"New York\"}";
    JsonElement jsonElement = new JsonParser().parse(jsonString);
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);

    assertEquals("name", jsonTreeReader.nextName());
    assertEquals("John", jsonTreeReader.nextString());

    assertEquals(JsonToken.NAME, jsonTreeReader.peek().getAsString());
    assertEquals("height", jsonTreeReader.nextName());

    assertEquals(1.8, jsonTreeReader.nextDouble(), 0.0);

    assertEquals(JsonToken.NAME, jsonTreeReader.peek().getAsString());
    assertEquals("weight", jsonTreeReader.nextName());

    assertEquals(70.0, jsonTreeReader.nextDouble(), 0.0);

    assertEquals(JsonToken.NAME, jsonTreeReader.peek().getAsString());
    assertEquals("city", jsonTreeReader.nextName());

    assertEquals("New York", jsonTreeReader.nextString());

    assertEquals(JsonToken.END_OBJECT, jsonTreeReader.peek().getAsInt());
    jsonTreeReader.endObject();

    assertEquals(JsonToken.END_DOCUMENT, jsonTreeReader.peek().getAsInt());
    jsonTreeReader.close();
  }

  private JsonParser newJsonParser() {
    return new JsonParser();
  }
}