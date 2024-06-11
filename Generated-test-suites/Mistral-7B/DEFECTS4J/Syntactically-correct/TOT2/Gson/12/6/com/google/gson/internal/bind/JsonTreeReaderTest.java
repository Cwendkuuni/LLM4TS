import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonTreeReaderTest {

 private JsonTreeReader reader;

 @Test
 public void testEmptyJson() throws IOException {
 reader = new JsonTreeReader(JsonElement.parse("{}"));
 assertFalse(reader.hasNext());
 reader.close();
 }

 @Test
 public void testSimpleObject() throws IOException {
 reader = new JsonTreeReader(JsonElement.parse("{\"name\": \"John\", \"age\": 30}"));
 assertTrue(reader.hasNext());
 assertEquals("name", reader.nextName());
 assertEquals("John", reader.nextString());
 assertEquals("age", reader.nextName());
 assertEquals(30, reader.nextInt());
 reader.close();
 }

 @Test
 public void testEmptyArray() throws IOException {
 reader = new JsonTreeReader(JsonElement.parse("[ ]"));
 assertTrue(reader.hasNext());
 reader.beginArray();
 assertFalse(reader.hasNext());
 reader.endArray();
 reader.close();
 }

 @Test
 public void testSimpleArray() throws IOException {
 reader = new JsonTreeReader(JsonElement.parse("[1, 2, 3]"));
 assertTrue(reader.hasNext());
 reader.beginArray();
 assertEquals(1, reader.nextInt());
 assertEquals(2, reader.nextInt());
 assertEquals(3, reader.nextInt());
 reader.endArray();
 reader.close();
 }

 @Test(expected = IllegalStateException.class)
 public void testClosedReader() throws IOException {
 reader = new JsonTreeReader(JsonElement.parse("{}"));
 reader.close();
 reader.nextName();
 }

 @Test
 public void testNestedObjects() throws IOException {
 reader = new JsonTreeReader(JsonElement.parse("{" +
 "\"name\": {" +
 "\"first\": \"John\"," +
 "\"last\": \"Doe\"" +
 "}," +
 "\"age\": 30" +
 "}"));
 assertTrue(reader.hasNext());
 reader.beginObject();
 assertEquals("name", reader.nextName());
 reader.beginObject();
 assertEquals("first", reader.nextName());
 assertEquals("John", reader.nextString());
 reader.endObject();
 assertEquals("last", reader.nextName());
 assertEquals("Doe", reader.nextString());
 reader.endObject();
 assertEquals("age", reader.nextName());
 assertEquals(30, reader.nextInt());
 reader.endObject();
 reader.close();
 }

 @Test
 public void testNestedArrays() throws IOException {
 reader = new JsonTreeReader(JsonElement.parse("{" +
 "\"name\": [" +
 "{\"first\": \"John\"," +
 "\"last\": \"Doe\"" +
 "}," +
 "{\"first\": \"Jane\"," +
 "\"last\": \"Doe\"" +
 "}]," +
 "\"age\": 30" +
 "}"));
 assertTrue(reader.hasNext());
 reader.beginObject();
 assertEquals("name", reader.nextName());
 reader.beginArray();
 assertTrue(reader.hasNext());
 reader.beginObject();
 assertEquals("first", reader.nextName());
 assertEquals("John", reader.nextString());
 reader.endObject();
 assertTrue(reader.hasNext());
 reader.beginObject();
 assertEquals("first", reader.nextName());
 assertEquals("Jane", reader.nextString());
 reader.endObject();
 reader.endArray();
 assertEquals("age", reader.nextName());
 assertEquals(30, reader.nextInt());
 reader.endObject();
 reader.close();
 }

 @Test
 public void testNullValues() throws IOException {
 reader = new JsonTreeReader(JsonElement.parse("{" +
 "\"name\": null," +
 "\"age\": null" +
 "}"));
 assertTrue(reader.hasNext());
 reader.beginObject();
 assertEquals(null, reader.nextName());
 reader.nextNull();
 assertEquals(null, reader.nextName());
 reader.nextNull();
 reader.endObject();
 reader.close();
 }

 @Test
 public void testComplexJson() throws IOException {
 reader = new JsonTreeReader(JsonElement.parse("{" +
 "\"name\": {" +
 "\"first\": \"John\"," +
 "\"last\": \"Doe\"" +
 "}," +
 "\"age\": 30," +
 "\"hobbies\": [" +
 "\"reading\"," +
 "\"golfing\"" +
 "]," +
 "\"address\": {" +
 "\"street\": \"123 Main St\"," +
 "\"city\": \"Anytown\"," +
 "\"state\": \"CA\"," +
 "\"zip\": 12345" +
 "}" +
 "}"));
 assertTrue(reader.hasNext());
 reader.beginObject();
 assertEquals("name", reader.nextName());
 reader.beginObject();
 assertEquals("first", reader.nextName());
 assertEquals("John", reader.nextString());
 reader.endObject();
 assertEquals("last", reader.nextName());
 assertEquals("Doe", reader.nextString());
 reader.endObject();
 assertEquals("age", reader.nextName());
 assertEquals(30, reader.nextInt());
 reader.beginArray();
 assertEquals("hobbies", reader.nextName());
 reader.beginArray();
 assertEquals("reading", reader.nextString());
 reader.endArray();
 reader.beginArray();
 assertEquals("golfing", reader.nextString());
 reader.endArray();
 reader.endArray();
 reader.beginObject();
 assertEquals("address", reader.nextName());
 reader.beginObject();
 assertEquals("street", reader.nextName());
 assertEquals("123 Main St", reader.nextString());
 reader.endObject();
 reader.beginObject();
 assertEquals("city", reader.nextName());
 assertEquals("Anytown", reader.nextString());
 reader.endObject();
 reader.beginObject();
 assertEquals("state", reader.nextName());
 assertEquals("CA", reader.nextString());
 reader.endObject();
 reader.beginObject();
 assertEquals("zip", reader.nextName());
 assertEquals(12345, reader.nextInt());
 reader.endObject();
 reader.endObject();
 reader.close();
 }
}