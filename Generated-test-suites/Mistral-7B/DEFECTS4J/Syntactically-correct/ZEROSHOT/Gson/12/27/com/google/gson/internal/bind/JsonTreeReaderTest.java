import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonTreeReaderTest {

 private JsonElement createJsonElement(String jsonString) {
 JsonReader jsonReader = new JsonReader(new StringReader(jsonString));
 JsonElement jsonElement = new JsonTreeReader(jsonReader.peek()).readTree();
 jsonReader.close();
 return jsonElement;
 }

 @Test
 public void testEmptyJson() throws IOException {
 JsonTreeReader jsonTreeReader = new JsonTreeReader(JsonElement.NULL);
 assertFalse(jsonTreeReader.hasNext());
 jsonTreeReader.close();
 }

 @Test
 public void testBeginArray() throws IOException {
 JsonTreeReader jsonTreeReader = new JsonTreeReader(createJsonElement("[1, 2, 3]"));
 jsonTreeReader.beginArray();
 assertEquals("1", jsonTreeReader.nextString());
 jsonTreeReader.beginArray();
 assertEquals("2", jsonTreeReader.nextString());
 assertEquals("3", jsonTreeReader.nextString());
 jsonTreeReader.endArray();
 jsonTreeReader.endArray();
 jsonTreeReader.close();
 }

 @Test
 public void testBeginObject() throws IOException {
 JsonTreeReader jsonTreeReader = new JsonTreeReader(createJsonElement("{\"name\":{\"first\":\"John\",\"last\":\"Doe\"}}"));
 jsonTreeReader.beginObject();
 jsonTreeReader.beginObject();
 assertEquals("first", jsonTreeReader.nextName());
 assertEquals("John", jsonTreeReader.nextString());
 jsonTreeReader.endObject();
 jsonTreeReader.beginObject();
 assertEquals("last", jsonTreeReader.nextName());
 assertEquals("Doe", jsonTreeReader.nextString());
 jsonTreeReader.endObject();
 jsonTreeReader.endObject();
 jsonTreeReader.close();
 }

 @Test(expected = IllegalStateException.class)
 public void testUnexpectedEndObject() throws IOException {
 JsonTreeReader jsonTreeReader = new JsonTreeReader(createJsonElement("{}"));
 jsonTreeReader.beginObject();
 jsonTreeReader.nextString();
 jsonTreeReader.nextName(); // should throw an exception
 jsonTreeReader.close();
 }

 @Test(expected = IllegalStateException.class)
 public void testUnexpectedEndArray() throws IOException {
 JsonTreeReader jsonTreeReader = new JsonTreeReader(createJsonElement("[1, 2]"));
 jsonTreeReader.beginArray();
 jsonTreeReader.nextString(); // should throw an exception
 jsonTreeReader.close();
 }

 @Test
 public void testReadNull() throws IOException {
 JsonTreeReader jsonTreeReader = new JsonTreeReader(createJsonElement("null"));
 jsonTreeReader.nextNull();
 jsonTreeReader.close();
 }

 @Test
 public void testReadBoolean() throws IOException {
 JsonTreeReader jsonTreeReader = new JsonTreeReader(createJsonElement("{\"bool\":true}"));
 jsonTreeReader.beginObject();
 jsonTreeReader.nextName();
 jsonTreeReader.nextName(); // skip "bool"
 jsonTreeReader.nextBoolean();
 jsonTreeReader.endObject();
 jsonTreeReader.close();
 }

 @Test
 public void testReadNumber() throws IOException {
 JsonTreeReader jsonTreeReader = new JsonTreeReader(createJsonElement("{\"number\":123.5}"));
 jsonTreeReader.beginObject();
 jsonTreeReader.nextName();
 jsonTreeReader.nextName(); // skip "number"
 jsonTreeReader.nextDouble();
 jsonTreeReader.endObject();
 jsonTreeReader.close();
 }

 @Test
 public void testReadString() throws IOException {
 JsonTreeReader jsonTreeReader = new JsonTreeReader(createJsonElement("{\"string\":\"Hello World!\"}"));
 jsonTreeReader.beginObject();
 jsonTreeReader.nextName();
 jsonTreeReader.nextName(); // skip "string"
 jsonTreeReader.nextString();
 jsonTreeReader.endObject();
 jsonTreeReader.close();
 }

 @Test
 public void testReadArrayElement() throws IOException {
 JsonTreeReader jsonTreeReader = new JsonTreeReader(createJsonElement("[1, 2, 3]"));
 jsonTreeReader.beginArray();
 jsonTreeReader.nextInt();
 jsonTreeReader.nextInt();
 jsonTreeReader.nextInt();
 jsonTreeReader.endArray();
 jsonTreeReader.close();
 }

 @Test
 public void testReadObjectElement() throws IOException {
 JsonTreeReader jsonTreeReader = new JsonTreeReader(createJsonElement("{\"array\":[1, 2, 3], \"string\":\"Hello World!\"}"));
 jsonTreeReader.beginObject();
 jsonTreeReader.nextName();
 jsonTreeReader.nextName(); // skip "array"
 jsonTreeTreeReader.beginArray();
 jsonTreeReader.nextInt();
 jsonTreeReader.nextInt();
 jsonTreeReader.nextInt();
 jsonTreeReader.endArray();
 jsonTreeReader.nextName();
 jsonTreeReader.nextName(); // skip "string"
 jsonTreeReader.nextString();
 jsonTreeReader.endObject();
 jsonTreeReader.close();
 }
}