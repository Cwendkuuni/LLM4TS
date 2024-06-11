import static org.junit.Assert.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.Test;

public class JsonTreeWriterTest {

 // Test constructor and get method
 @Test
 public void testJsonTreeWriterConstructionAndGet() {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 JsonElement jsonElement = jsonTreeWriter.get();
 assertNull(jsonElement);
 }

 // Test beginArray method
 @Test
 public void testBeginArrayMethod() {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 jsonTreeWriter.beginArray();
 JsonArray jsonArray = (JsonArray) jsonTreeWriter.peek();
 assertTrue(jsonArray.isJsonArray());
 jsonTreeWriter.endArray();
 JsonElement jsonElement = jsonTreeWriter.get();
 assertFalse(jsonElement.isJsonArray());
 }

 // Test beginObject method
 @Test
 public void testBeginObjectMethod() {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 jsonTreeWriter.beginObject();
 JsonObject jsonObject = (JsonObject) jsonTreeWriter.peek();
 assertTrue(jsonObject.isJsonObject());
 jsonTreeWriter.endObject();
 JsonElement jsonElement = jsonTreeWriter.get();
 assertFalse(jsonElement.isJsonObject());
 }

 // Test name method with valid name
 @Test
 public void testNameMethodWithValidName() {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 jsonTreeWriter.beginObject();
 jsonTreeWriter.name("testName");
 JsonObject jsonObject = (JsonObject) jsonTreeWriter.peek();
 assertEquals("testName", jsonObject.name());
 }

 // Test value method with valid string
 @Test
 public void testValueMethodWithValidString() {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 jsonTreeWriter.beginObject();
 jsonTreeWriter.name("testName");
 jsonTreeWriter.value("testValue");
 JsonObject jsonObject = (JsonObject) jsonTreeWriter.get();
 JsonPrimitive jsonPrimitive = jsonObject.getAsJsonPrimitive("testName");
 assertEquals("testValue", jsonPrimitive.getAsString());
 }

 // Test value method with null value
 @Test
 public void testValueMethodWithNullValue() {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 jsonTreeWriter.beginObject();
 jsonTreeWriter.name("testName");
 jsonTreeWriter.value(null);
 JsonObject jsonObject = (JsonObject) jsonTreeWriter.get();
 assertTrue(jsonObject.isJsonObject());
 JsonPrimitive jsonPrimitive = jsonObject.get("testName");
 assertNull(jsonPrimitive);
 }

 // Test value method with valid boolean
 @Test
 public void testValueMethodWithValidBoolean() {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 jsonTreeWriter.beginObject();
 jsonTreeWriter.name("testName");
 jsonTreeWriter.value(true);
 JsonObject jsonObject = (JsonObject) jsonTreeWriter.get();
 JsonPrimitive jsonPrimitive = jsonObject.getAsJsonPrimitive("testName");
 assertTrue(jsonPrimitive.isBoolean());
 assertEquals(true, jsonPrimitive.getAsBoolean());
 }

 // Test value method with valid number
 @Test
 public void testValueMethodWithValidNumber() {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 jsonTreeWriter.beginObject();
 jsonTreeWriter.name("testName");
 jsonTreeWriter.value(1.23);
 JsonObject jsonObject = (JsonObject) jsonTreeWriter.get();
 JsonPrimitive jsonPrimitive = jsonObject.getAsJsonPrimitive("testName");
 assertTrue(jsonPrimitive.isNumber());
 assertEquals(1.23, jsonPrimitive.getAsDouble(), 0.0);
 }

 // Test value method with valid long
 @Test
 public void testValueMethodWithValidLong() {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 jsonTreeWriter.beginObject();
 jsonTreeWriter.name("testName");
 jsonTreeWriter.value(42);
 JsonObject jsonObject = (JsonObject) jsonTreeWriter.get();
 JsonPrimitive jsonPrimitive = jsonObject.getAsJsonPrimitive("testName");
 assertTrue(jsonPrimitive.isNumber());
 assertEquals(42L, jsonPrimitive.getAsLong());
 }

 // Test endObject method with empty stack
 @Test(expected = IllegalStateException.class)
 public void testEndObjectMethodWithEmptyStack() {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 jsonTreeWriter.endObject();
 }

 // Test endArray method with empty stack
 @Test(expected = IllegalStateException.class)
 public void testEndArrayMethodWithEmptyStack() {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 jsonTreeWriter.endArray();
 }

 // Test put method with valid JsonElement
 @Test
 public void testPutMethodWithValidJsonElement() {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 jsonTreeWriter.beginArray();
 jsonTreeWriter.put(new JsonPrimitive(1));
 jsonTreeWriter.put(new JsonPrimitive(2));
 jsonTreeWriter.endArray();
 JsonArray jsonArray = (JsonArray) jsonTreeWriter.get();
 assertEquals(2, jsonArray.size());
 assertEquals(1, jsonArray.get(0).getAsInt());
 assertEquals(2, jsonArray.get(1).getAsInt());
 }

 // Test put method with valid JsonObject
 @Test
 public void testPutMethodWithValidJsonObject() {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 jsonTreeWriter.beginObject();
 jsonTreeWriter.name("testName1");
 jsonTreeWriter.put(new JsonPrimitive(1));
 jsonTreeWriter.name("testName2");
 jsonTreeWriter.put(new JsonObject());
 jsonTreeWriter.endObject();
 JsonObject jsonObject = (JsonObject) jsonTreeWriter.get();
 JsonPrimitive testName1 = jsonObject.getAsJsonPrimitive("testName1");
 assertEquals(1, testName1.getAsInt());
 JsonObject testName2 = jsonObject.getAsJsonObject();
 assertTrue(testName2.isEmpty());
 }

 // Test put method with invalid JsonElement
 @Test(expected = IllegalStateException.class)
 public void testPutMethodWithInvalidJsonElement() {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 jsonTreeWriter.beginObject();
 jsonTreeWriter.name("testName");
 jsonTreeWriter.put(new Object());
 }

 // Test nullValue method
 @Test
 public void testNullValueMethod() {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 jsonTreeWriter.nullValue();
 JsonElement jsonElement = jsonTreeWriter.get();
 assertTrue(jsonElement.isJsonNull());
 }
}