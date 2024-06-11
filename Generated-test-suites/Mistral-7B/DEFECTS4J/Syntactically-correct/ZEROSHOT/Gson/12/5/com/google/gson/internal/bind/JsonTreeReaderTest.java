import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonTreeReaderTest {

 @Test
 public void testBeginArray() throws Exception {
 JsonTreeReader reader = new JsonTreeReader(new JsonElement() {
 @Override
 public JsonElement getAsJsonArray() {
 return new JsonArray();
 }

 @Override
 public JsonElement getAsJsonObject() {
 throw new UnsupportedOperationException();
 }

 @Override
 public JsonElement getAsJsonPrimitive() {
 throw new UnsupportedOperationException();
 }

 @Override
 public String toString() {
 return "[]";
 }
 });
 reader.beginArray();
 assertTrue(reader.hasNext());
 reader.endArray();
 assertFalse(reader.hasNext());
 }

 @Test(expected = IllegalStateException.class)
 public void testBeginArrayTwice() throws Exception {
 JsonTreeReader reader = new JsonTreeReader(new JsonElement() {
 @Override
 public JsonElement getAsJsonArray() {
 return new JsonArray();
 }

 @Override
 public JsonElement getAsJsonObject() {
 throw new UnsupportedOperationException();
 }

 @Override
 public JsonElement getAsJsonPrimitive() {
 throw new UnsupportedOperationException();
 }

 @Override
 public String toString() {
 return "[]";
 }
 });
 reader.beginArray();
 reader.beginArray();
 }

 @Test
 public void testEndArray() throws Exception {
 JsonTreeReader reader = new JsonTreeReader(new JsonElement() {
 @Override
 public JsonElement getAsJsonArray() {
 JsonArray array = new JsonArray();
 array.add(new JsonPrimitive("foo"));
 array.add(new JsonPrimitive(1));
 array.add(new JsonPrimitive(true));
 return array;
 }

 @Override
 public JsonElement getAsJsonObject() {
 throw new UnsupportedOperationException();
 }

 @Override
 public JsonElement getAsJsonPrimitive() {
 throw new UnsupportedOperationException();
 }

 @Override
 public String toString() {
 return "[\"foo\",1,true]";
 }
 });
 reader.beginArray();
 assertEquals("foo", reader.nextString());
 assertEquals(1, reader.nextInt());
 assertTrue(reader.nextBoolean());
 reader.endArray();
 assertFalse(reader.hasNext());
 }

 @Test(expected = IllegalStateException.class)
 public void testEndArrayWithoutBeginArray() throws Exception {
 JsonTreeReader reader = new JsonTreeReader(new JsonElement() {
 @Override
 public JsonElement getAsJsonArray() {
 return new JsonArray();
 }

 @Override
 public JsonElement getAsJsonObject() {
 throw new UnsupportedOperationException();
 }

 @Override
 public JsonElement getAsJsonPrimitive() {
 throw new UnsupportedOperationException();
 }

 @Override
 public String toString() {
 return "[\"foo\",1,true]";
 }
 });
 reader.nextString();
 reader.nextInt();
 reader.nextBoolean();
 reader.endArray();
 }

 @Test
 public void testBeginObject() throws Exception {
 JsonTreeReader reader = new JsonTreeReader(new JsonElement() {
 @Override
 public JsonElement getAsJsonArray() {
 throw new UnsupportedOperationException();
 }

 @Override
 public JsonElement getAsJsonObject() {
 JsonObject object = new JsonObject();
 object.addProperty("name", "John");
 object.addProperty("age", 30);
 object.addProperty("city", "New York");
 return object;
 }

 @Override
 public JsonElement getAsJsonPrimitive() {
 throw new UnsupportedOperationException();
 }

 @Override
 public String toString() {
 return "{\"name\":\"John\",\"age\":30,\"city\":\"New York\"}";
 }
 });
 reader.beginObject();
 assertEquals("name", reader.nextName());
 assertEquals("John", reader.nextString());
 assertEquals(30, reader.nextInt());
 assertEquals("city", reader.nextName());
 assertEquals("New York", reader.nextString());
 reader.endObject();
 assertFalse(reader.hasNext());
 }

 @Test(expected = IllegalStateException.class)
 public void testBeginObjectTwice() throws Exception {
 JsonTreeReader reader = new JsonTreeReader(new JsonElement() {
 @Override
 public JsonElement getAsJsonArray() {
 throw new UnsupportedOperationException();
 }

 @Override
 public JsonElement getAsJsonObject() {
 JsonObject object = new JsonObject();
 object.addProperty("name", "John");
 object.addProperty("age", 30);
 object.addProperty("city", "New York");
 return object;
 }

 @Override
 public JsonElement getAsJsonPrimitive() {
 throw new UnsupportedOperationException();
 }

 @Override
 public String toString() {
 return "{\"name\":\"John\",\"age\":30,\"city\":\"New York\"}";
 }
 });
 reader.beginObject();
 reader.beginObject();
 }

 @Test
 public void testEndObject() throws Exception {
 JsonTreeReader reader = new JsonTreeReader(new JsonElement() {
 @Override
 public JsonElement getAsJsonArray() {
 throw new UnsupportedOperationException();
 }

 @Override
 public JsonElement getAsJsonObject() {
 JsonObject object = new JsonObject();
 object.addProperty("name", "John");
 object.addProperty("age", 30);
 object.addProperty("city", "New York");
 return object;
 }

 @Override
 public JsonElement getAsJsonPrimitive() {
 throw new UnsupportedOperationException();
 }

 @Override
 public String toString() {
 return "{\"name\":\"John\",\"age\":30,\"city\":\"New York\"}";
 }
 });
 reader.beginObject();
 assertEquals("name", reader.nextName());
 assertEquals("John", reader.nextString());
 assertEquals(30, reader.nextInt());
 assertEquals("city", reader.nextName());
 assertEquals("New York", reader.nextString());
 reader.endObject();
 assertFalse(reader.hasNext());
 }

 @Test(expected = IllegalStateException.class)
 public void testEndObjectWithoutBeginObject() throws Exception {
 JsonTreeReader reader = new JsonTreeReader(new JsonElement() {
 @Override
 public JsonElement getAsJsonArray() {
 throw new UnsupportedOperationException();
 }

 @Override
 public JsonElement getAsJsonObject() {
 JsonObject object = new JsonObject();
 object.addProperty("name", "John");
 object.addProperty("age", 30);
 object.addProperty("city", "New York");
 return object;
 }

 @Override
 public JsonElement getAsJsonPrimitive() {
 throw new UnsupportedOperationException();
 }

 @Override
 public String toString() {
 return "{\"name\":\"John\",\"age\":30,\"city\":\"New York\"}";
 }
 });
 reader.nextName();
 reader.nextString();
 reader.nextInt();
 reader.nextName();
 reader.nextString();
 reader.endObject();
 }

 @Test
 public void testHasNext() throws Exception {
 JsonTreeReader reader = new JsonTreeReader(new JsonElement() {
 @Override
 public JsonElement getAsJsonArray() {
 return new JsonArray() {{
 add(new JsonPrimitive("foo"));
 add(new JsonPrimitive(1));
 add(new JsonPrimitive(true));
 }};
 }

 @Override
 public JsonElement getAsJsonObject() {
 return new JsonObject() {{
 addProperty("name", "John");
 addProperty("age", 30);
 addProperty("city", "New York");
 }};
 }

 @Override
 public JsonElement getAsJsonPrimitive() {
 throw new UnsupportedOperationException();
 }

 @Override
 public String toString() {
 return "[\"foo\",1,true]";
 }
 });
 reader.beginArray();
 assertTrue(reader.hasNext());
 assertEquals("foo", reader.nextString());
 assertTrue(reader.hasNext());
 assertEquals(1, reader.nextInt());
 assertTrue(reader.hasNext());
 assertEquals(true, reader.nextBoolean());
 assertFalse(reader.hasNext());
 }

 @Test
 public void testPeek() throws Exception {
 JsonTreeReader reader = new JsonTreeReader(new JsonElement() {
 @Override
 public JsonElement getAsJsonArray() {
 return new JsonArray() {{
 add(new JsonPrimitive("foo"));
 add(new JsonPrimitive(1));
 add(new JsonPrimitive(true));
 }};
 }

 @Override
 public JsonElement getAsJsonObject() {
 return new JsonObject() {{
 addProperty("name", "John");
 addProperty("age", 30);
 addProperty("city", "New York");
 }};
 }

 @Override
 public JsonElement getAsJsonPrimitive() {
 throw new UnsupportedOperationException();
 }

 @Override
 public String toString() {
 return "[\"foo\",1,true]";
 }
 });
 reader.beginArray();
 assertEquals("foo", reader.peek().getAsString());
 assertEquals(1, reader.peek().getAsInt());
 assertEquals(true, reader.peek().isBoolean());
 reader.endArray();
 }

 @Test
 public void testPushPop() throws Exception {
 JsonTreeReader reader = new JsonTreeReader(new JsonElement() {
 @Override
 public JsonElement getAsJsonArray() {
 return new JsonArray() {{
 add(new JsonPrimitive("foo"));
 add(new JsonPrimitive(1));
 add(new JsonPrimitive(true));
 }};
 }

 @Override
 public JsonElement getAsJsonObject() {
 return new JsonObject() {{
 addProperty("name", "John");
 addProperty("age", 30);
 addProperty("city", "New York");
 }};
 }

 @Override
 public JsonElement getAsJsonPrimitive() {
 throw new UnsupportedOperationException();
 }

 @Override
 public String toString() {
 return "[\"foo\",1,true]";
 }
 });
 reader.beginArray();
 reader.push(reader.nextString());
 reader.push(reader.nextInt());
 reader.push(reader.nextBoolean());
 reader.endArray();
 reader.pop();
 reader.pop();
 reader.pop();
 assertEquals("foo", reader.nextString());
 assertEquals(1, reader.nextInt());
 assertEquals(true, reader.nextBoolean());
 reader.endArray();
 assertFalse(reader.hasNext());
 }
}