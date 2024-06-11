import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class JsonTreeWriterTest {

  @Test
  public void testJsonTreeWriter_Empty() throws IOException {
    // Given
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();

    // When
    JsonElement jsonElement = jsonTreeWriter.get();

    // Then
    assertNotNull(jsonElement);
    assertEquals(JsonNull.INSTANCE, jsonElement);
  }

  @Test
  public void testJsonTreeWriter_BeginArray() throws IOException {
    // Given
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();

    // When
    jsonTreeWriter.beginArray();
    jsonTreeWriter.endArray();
    JsonElement jsonElement = jsonTreeWriter.get();

    // Then
    assertNotNull(jsonElement);
    assertTrue(jsonElement instanceof JsonArray);
    assertEquals(0, ((JsonArray) jsonElement).size());
  }

  @Test
  public void testJsonTreeWriter_BeginObject() throws IOException {
    // Given
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();

    // When
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name").value("value");
    jsonTreeWriter.endObject();
    JsonElement jsonElement = jsonTreeWriter.get();

    // Then
    assertNotNull(jsonElement);
    assertTrue(jsonElement instanceof JsonObject);
    JsonObject jsonObject = (JsonObject) jsonElement;
    assertEquals("name", jsonObject.keySet().iterator().next());
    assertEquals("value", jsonObject.get("name").getAsString());
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonTreeWriter_InvalidState_BeginObjectWithoutBeginArrayOrName() throws IOException {
    // Given
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.endObject();

    // When
    jsonTreeWriter.get();

    // Then: Throws an IllegalStateException
  }

  @Test
  public void testJsonTreeWriter_Value_String() throws IOException {
    // Given
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();

    // When
    jsonTreeWriter.value("value");
    JsonElement jsonElement = jsonTreeWriter.get();

    // Then
    assertNotNull(jsonElement);
    assertTrue(jsonElement instanceof JsonPrimitive);
    assertEquals("value", jsonElement.getAsString());
  }

  @Test
  public void testJsonTreeWriter_Value_Boolean() throws IOException {
    // Given
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();

    // When
    jsonTreeWriter.value(true);
    JsonElement jsonElement = jsonTreeWriter.get();

    // Then
    assertNotNull(jsonElement);
    assertTrue(jsonElement instanceof JsonPrimitive);
    assertTrue(jsonElement.getAsBoolean());
  }

  @Test
  public void testJsonTreeWriter_Value_Number() throws IOException {
    // Given
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();

    // When
    jsonTreeWriter.value(123);
    JsonElement jsonElement = jsonTreeWriter.get();

    // Then
    assertNotNull(jsonElement);
    assertTrue(jsonElement instanceof JsonPrimitive);
    assertEquals(123, jsonElement.getAsInt());
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonTreeWriter_InvalidState_BeginObjectWithoutEndObject() throws IOException {
    // Given
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name").value("value");

    // When
    JsonElement jsonElement = jsonTreeWriter.get();

    // Then: Throws an IllegalStateException
  }

  @Test
  public void testJsonTreeWriter_Close() throws IOException {
    // Given
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name").value("value");
    jsonTreeWriter.endObject();
    jsonTreeWriter.close();
    JsonElement jsonElement = jsonTreeWriter.get();

    // Then
    assertNotNull(jsonElement);
    assertEquals(JsonTreeWriter.SENTINEL_CLOSED, jsonElement);
  }
}