import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonTreeReaderTest {

 @Test
 public void testEmptyJsonTree() throws Exception {
 JsonTreeReader jsonTreeReader = new JsonTreeReader(JsonElement.parse("{}"));
 assertEquals(JsonToken.END_DOCUMENT, jsonTreeReader.peek());
 jsonTreeReader.close();
 }

 @Test
 public void testJsonArray() throws Exception {
 JsonTreeReader jsonTreeReader = new JsonTreeReader(JsonElement.parse("[]"));
 assertEquals(JsonToken.END_ARRAY, jsonTreeReader.peek());
 jsonTreeReader.close();

 jsonTreeReader = new JsonTreeReader(JsonElement.parse("[1, 2, 3]"));
 assertEquals(JsonToken.BEGIN_ARRAY, jsonTreeReader.peek());
 jsonTreeReader.beginArray();
 assertEquals(JsonToken.NULL, jsonTreeReader.nextNull());
 assertEquals(JsonToken.BEGIN_ARRAY, jsonTreeReader.peek());
 jsonTreeReader.beginArray();
 assertEquals(JsonToken.NUMBER, jsonTreeReader.peek());
 assertEquals(1.0, jsonTreeReader.nextDouble(), 0.001);
 jsonTreeReader.endArray();
 assertEquals(JsonToken.NUMBER, jsonTreeReader.peek());
 assertEquals(2.0, jsonTreeReader.nextDouble(), 0.001);
 jsonTreeReader.endArray();
 assertEquals(JsonToken.NUMBER, jsonTreeReader.peek());
 assertEquals(3.0, jsonTreeReader.nextDouble(), 0.001);
 jsonTreeReader.endArray();
 assertEquals(JsonToken.END_ARRAY, jsonTreeReader.peek());
 jsonTreeReader.close();
 }

 @Test
 public void testJsonObject() throws Exception {
 JsonTreeReader jsonTreeReader = new JsonTreeReader(JsonElement.parse("{}"));
 assertEquals(JsonToken.END_OBJECT, jsonTreeReader.peek());
 jsonTreeReader.close();

 jsonTreeReader = new JsonTreeReader(JsonElement.parse("{\"name\": \"John\", \"age\": 30, \"city\": \"New York\"}"));
 assertEquals(JsonToken.BEGIN_OBJECT, jsonTreeReader.peek());
 jsonTreeReader.beginObject();
 assertEquals(JsonToken.NAME, jsonTreeReader.peek());
 assertEquals("name", jsonTreeReader.nextName());
 assertEquals(JsonToken.STRING, jsonTreeReader.peek());
 assertEquals("John", jsonTreeReader.nextString());
 jsonTreeReader.endObject();
 assertEquals(JsonToken.NAME, jsonTreereader.peek());
 assertEquals("age", jsonTreeReader.nextName());
 assertEquals(JsonToken.NUMBER, jsonTreeReader.peek());
 assertEquals(30, jsonTreeReader.nextInt());
 jsonTreeReader.endObject();
 assertEquals(JsonToken.NAME, jsonTreeReader.peek());
 assertEquals("city", jsonTreeReader.nextName());
 assertEquals(JsonToken.STRING, jsonTreeReader.peek());
 assertEquals("New York", jsonTreeReader.nextString());
 jsonTreeReader.endObject();
 assertEquals(JsonToken.END_OBJECT, jsonTreeReader.peek());
 jsonTreeReader.close();
 }

 @Test
 public void testJsonTreeWithComplexNestedStructure() throws Exception {
 JsonTreeReader jsonTreeReader = new JsonTreeReader(JsonElement.parse("{\"name\": \"John\", \"age\": 30, \"city\": \"New York\", \"address\": { \"street\": \"Main St\", \"number\": 123, \"apartments\": [1, 2, 3] }}"));
 assertEquals(JsonToken.BEGIN_OBJECT, jsonTreeReader.peek());
 jsonTreeReader.beginObject();
 assertEquals(JsonToken.NAME, jsonTreeReader.peek());
 assertEquals("name", jsonTreeReader.nextName());
 assertEquals(JsonToken.STRING, jsonTreeReader.peek());
 assertEquals("John", jsonTreeReader.nextString());
 jsonTreeReader.endObject();
 assertEquals(JsonToken.NAME, jsonTreeReader.peek());
 assertEquals("age", jsonTreeReader.nextName());
 assertEquals(JsonToken.NUMBER, jsonTreeReader.peek());
 assertEquals(30, jsonTreeReader.nextInt());
 jsonTreeReader.endObject();
 assertEquals(JsonToken.NAME, jsonTreeReader.peek());
 assertEquals("city", jsonTreeReader.nextName());
 assertEquals(JsonToken.STRING, jsonTreeReader.peek());
 assertEquals("New York", jsonTreeReader.nextString());
 jsonTreeReader.endObject();
 assertEquals(JsonToken.NAME, jsonTreeReader.peek());
 assertEquals("address", jsonTreeReader.nextName());
 jsonTreeReader.beginObject();
 assertEquals(JsonToken.NAME, jsonTreeReader.peek());
 assertEquals("street", jsonTreeReader.nextName());
 assertEquals(JsonToken.STRING, jsonTreeReader.peek());
 assertEquals("Main St", jsonTreeReader.nextString());
 jsonTreeReader.endObject();
 assertEquals(JsonToken.NAME, jsonTreeReader.peek());
 assertEquals("number", jsonTreeReader.nextName());
 assertEquals(JsonToken.NUMBER, jsonTreeReader.peek());
 assertEquals(123, jsonTreeReader.nextInt());
 jsonTreeReader.endObject();
 assertEquals(JsonToken.NAME, jsonTreeReader.peek());
 assertEquals("apartments", jsonTreeReader.nextName());
 jsonTreeReader.beginArray();
 assertEquals(JsonToken.BEGIN_ARRAY, jsonTreeReader.peek());
 jsonTreeReader.beginArray();
 assertEquals(JsonToken.NUMBER, jsonTreeReader.peek());
 assertEquals(1, jsonTreeReader.nextInt());
 jsonTreeReader.endArray();
 jsonTreeReader.beginArray();
 assertEquals(JsonToken.NUMBER, jsonTreeReader.peek());
 assertEquals(2, jsonTreeReader.nextInt());
 jsonTreeReader.endArray();
 jsonTreeReader.beginArray();
 assertEquals(JsonToken.NUMBER, jsonTreeReader.peek());
 assertEquals(3, jsonTreeReader.nextInt());
 jsonTreeReader.endArray();
 jsonTreeReader.endArray();
 assertEquals(JsonToken.END_OBJECT, jsonTreeReader.peek());
 jsonTreeReader.close();
 }

}