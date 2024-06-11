import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonReader;
import com.google.gson.JsonToken;

public class JsonTreeReaderTest {

  @Test
  public void testEmptyJsonElement() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.NULL);
    reader.close();
    assertEquals("", reader.getPath());
  }

  @Test
  public void testJsonObjectWithSingleNameValuePair() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(createJsonObject("name", "value"));
    reader.beginObject();
    assertEquals("name", reader.nextName());
    assertEquals("value", reader.nextString());
    reader.endObject();
    assertEquals("name", reader.nextName());
    assertNull(reader.nextName());
    reader.close();
    assertEquals("$", reader.getPath());
  }

  @Test
  public void testJsonObjectWithMultipleNameValuePairs() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(createJsonObject("name1", "value1", "name2", "value2"));
    reader.beginObject();
    assertEquals("name1", reader.nextName());
    assertEquals("value1", reader.nextString());
    assertEquals("name2", reader.nextName());
    assertEquals("value2", reader.nextString());
    reader.endObject();
    assertEquals("name2", reader.nextName());
    assertNull(reader.nextName());
    reader.close();
    assertEquals("$", reader.getPath());
  }

  @Test
  public void testJsonArrayWithSingleElement() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(createJsonArray("element"));
    reader.beginArray();
    JsonElement element = reader.peek();
    assertEquals(JsonToken.BEGIN_ARRAY, reader.peek().getAsInt());
    reader.nextInt(); // skip array size
    reader.beginArray();
    assertEquals("element", reader.nextName());
    assertEquals(JsonToken.STRING, reader.peek().getAsInt());
    assertEquals("element", reader.nextString());
    reader.endArray();
    reader.endArray();
    assertEquals("element", reader.nextName());
    assertNull(reader.nextName());
    reader.close();
    assertEquals("element", reader.getPath());
  }

  @Test
  public void testJsonArrayWithMultipleElements() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(createJsonArray("element1", "element2"));
    reader.beginArray();
    JsonElement element1 = reader.nextString();
    assertEquals("element1", element1.getAsString());
    reader.beginArray();
    JsonElement element2 = reader.nextString();
    assertEquals("element2", element2.getAsString());
    reader.endArray();
    reader.endArray();
    assertEquals("element2", reader.nextName());
    assertNull(reader.nextName());
    reader.close();
    assertEquals("element2", reader.getPath());
  }

  @Test
  public void testJsonObjectAsArrayElement() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(createJsonArray(createJsonObject("name", "value")));
    reader.beginArray();
    reader.nextInt(); // skip array size
    reader.beginArray();
    reader.expect(JsonToken.BEGIN_OBJECT);
    reader.beginObject();
    assertEquals("name", reader.nextName());
    assertEquals("value", reader.nextString());
    reader.endObject();
    reader.endArray();
    reader.endArray();
    assertEquals("value", reader.nextName());
    assertNull(reader.nextName());
    reader.close();
    assertEquals("value", reader.getPath());
  }

  @Test
  public void testJsonNullAsArrayElement() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(createJsonArray(JsonNull.INSTANCE));
    reader.beginArray();
    reader.nextInt(); // skip array size
    reader.beginArray();
    reader.nextNull();
    reader.endArray();
    reader.endArray();
    reader.nextNull();
    reader.close();
    assertEquals("", reader.getPath());
  }

  @Test
  public void testJsonObjectWithEmptyArrayAsValue() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(createJsonObject("name", createJsonArray()));
    reader.beginObject();
    assertEquals("name", reader.nextName());
    reader.nextName(); // skip empty array name
    reader.endArray();
    reader.nextName(); // skip next name
    reader.close();
    assertEquals("name", reader.getPath());
  }

  @Test
  public void testJsonObjectWithNullValue() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(createJsonObject("name", null));
    reader.beginObject();
    assertEquals("name", reader.nextName());
    reader.nextNull();
    reader.endObject();
    reader.nextName(); // skip next name
    reader.close();
    assertEquals("name", reader.getPath());
  }

  @Test
  public void testJsonObjectWithEmptyNameValuePair() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(createJsonObject());
    reader.beginObject();
    reader.nextName(); // skip empty name
    reader.nextNull();
    reader.endObject();
    reader.nextName(); // skip next name
    reader.close();
    assertEquals("", reader.getPath());
  }

  @Test
  public void testJsonObjectWithNestedJsonObject() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(createJsonObject("name", createJsonObject("innerName", "innerValue")));
    reader.beginObject();
    assertEquals("name", reader.nextName());
    reader.beginObject();
    assertEquals("innerName", reader.nextName());
    assertEquals("innerValue", reader.nextString());
    reader.endObject();
    reader.endObject();
    assertEquals("innerName", reader.nextName());
    assertNull(reader.nextName());
    reader.close();
    assertEquals("innerName", reader.getPath());
  }

  @Test
  public void testJsonObjectWithNestedJsonArray() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(createJsonObject("name", createJsonArray(createJsonPrimitive("innerValue"))));
    reader.beginObject();
    assertEquals("name", reader.nextName());
    reader.beginArray();
    reader.nextInt(); // skip array size
    reader.beginArray();
    reader.nextString(); // skip array element name
    reader.nextString(); // skip array element value
    reader.endArray();
    reader.endArray();
    reader.nextName(); // skip next name
    reader.close();
    assertEquals("innerValue", reader.getPath());
  }

  @Test
  public void testJsonObjectWithNestedJsonObjectAndJsonArray() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(createJsonObject("name", createJsonObject("innerName", createJsonArray("innerArrayElement"))));
    reader.beginObject();
    assertEquals("name", reader.nextName());
    reader.beginObject();
    assertEquals("innerName", reader.nextName());
    reader.beginArray();
    reader.nextInt(); // skip array size
    reader.beginArray();
    reader.nextString(); // skip array element name
    reader.nextString(); // skip array element value
    reader.endArray();
    reader.endArray();
    reader.nextName(); // skip next name
    reader.close();
    assertEquals("innerArrayElement", reader.getPath());
  }

  @Test
  public void testJsonTreeReaderPromoteNameToValue() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(createJsonObject("name", "value"));
    reader.promoteNameToValue();
    reader.beginObject();
    assertEquals("value", reader.nextName());
    assertEquals("value", reader.nextString());
    reader.endObject();
    assertEquals("value", reader.nextName());
    assertNull(reader.nextName());
    reader.close();
    assertEquals("value", reader.getPath());
  }

  @Test
  public void testJsonTreeReaderSkipValue() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(createJsonObject("name", "value"));
    reader.skipValue();
    reader.beginObject();
    assertEquals("name", reader.nextName());
    reader.nextNull();
    reader.endObject();
    assertEquals("name", reader.nextName());
    assertNull(reader.nextName());
    reader.close();
    assertEquals("name", reader.getPath());
  }

  @Test
  public void testJsonTreeReaderClose() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(createJsonObject("name", "value"));
    reader.close();
    assertNull(reader.getPath());
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonTreeReaderAfterClose() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(createJsonObject("name", "value"));
    reader.close();
    reader.nextName();
  }

  private JsonElement createJsonArray(JsonElement... elements) {
    JsonArray jsonArray = new JsonArray();
    for (JsonElement element : elements) {
      jsonArray.add(element);
    }
    return jsonArray;
  }

  private JsonElement createJsonObject(String... namesAndValues) {
    JsonObject jsonObject = new JsonObject();
    for (int i = 0; i < namesAndValues.length; i += 2) {
      String name = namesAndValues[i];
      String value = namesAndValues[i + 1];
      jsonObject.addProperty(name, value);
    }
    return jsonObject;
  }

  private JsonElement createJsonPrimitive(String value) {
    return new JsonPrimitive(value);
  }
}