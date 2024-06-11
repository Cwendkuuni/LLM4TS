import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonTreeWriterTest {

 // Test for the constructor and get method
 @Test
 public void testJsonTreeWriterConstructionAndGet() {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 JsonElement jsonElement = jsonTreeWriter.get();
 assertNull(jsonElement);
 }

 // Test for beginArray method
 @Test
 public void testBeginArray() {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 jsonTreeWriter.beginArray();
 JsonElement jsonElement = jsonTreeWriter.get();
 JsonArray jsonArray = (JsonArray) jsonElement;
 assertTrue(jsonArray.isJsonArray());
 jsonTreeWriter.endArray();
 }

 // Test for endArray method when array is empty
 @Test
 public void testEndArrayWhenArrayIsEmpty() {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 jsonTreeWriter.beginArray();
 jsonTreeWriter.endArray();
 JsonElement jsonElement = jsonTreeWriter.get();
 JsonArray jsonArray = (JsonArray) jsonElement;
 assertTrue(jsonArray.isEmpty());
 }

 // Test for endArray method when array contains elements
 @Test
 public void testEndArrayWhenArrayContainsElements() {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 jsonTreeWriter.beginArray();
 jsonTreeWriter.value("element1");
 jsonTreeWriter.value(1);
 jsonTreeWriter.endArray();
 JsonElement jsonElement = jsonTreeWriter.get();
 JsonArray jsonArray = (JsonArray) jsonElement;
 assertEquals(2, jsonArray.size());
 }

 // Test for beginObject method
 @Test
 public void testBeginObject() {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 jsonTreeWriter.beginObject();
 jsonTreeWriter.name("name1").value("value1");
 jsonTreeWriter.name("name2").value(1);
 jsonTreeWriter.endObject();
 JsonElement jsonElement = jsonTreeWriter.get();
 JsonObject jsonObject = (JsonObject) jsonElement;
 assertEquals(2, jsonObject.size());
 }

 // Test for endObject method when object is empty
 @Test
 public void testEndObjectWhenObjectIsEmpty() {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 jsonTreeWriter.beginObject();
 jsonTreeWriter.endObject();
 JsonElement jsonElement = jsonTreeWriter.get();
 JsonObject jsonObject = (JsonObject) jsonElement;
 assertTrue(jsonObject.isEmpty());
 }

 // Test for endObject method when object contains name-value pairs
 @Test
 public void testEndObjectWhenObjectContainsNameValuePairs() {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 jsonTreeWriter.beginObject();
 jsonTreeWriter.name("name1").value("value1");
 jsonTreeWriter.name("name2").value(1);
 jsonTreeWriter.endObject();
 JsonElement jsonElement = jsonTreeWriter.get();
 JsonObject jsonObject = (JsonObject) jsonElement;
 assertEquals("value1", jsonObject.get("name1").getAsString());
 assertEquals(1, jsonObject.get("name2").getAsJsonPrimitive().getAsInt());
 }

 // Test for name method
 @Test
 public void testNameMethod() {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 jsonTreeWriter.beginObject();
 jsonTreeWriter.name("name1");
 jsonTreeWriter.name("name2");
 jsonTreeWriter.endObject();
 JsonElement jsonElement = jsonTreeWriter.get();
 JsonObject jsonObject = (JsonObject) jsonElement;
 assertNotNull(jsonObject.get("name1"));
 assertNotNull(jsonObject.get("name2"));
 }

 // Test for value method with String argument
 @Test
 public void testValueMethodWithStringArgument() {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 jsonTreeWriter.beginObject();
 jsonTreeWriter.name("name1").value("value1");
 jsonTreeWriter.endObject();
 JsonElement jsonElement = jsonTreeWriter.get();
 JsonObject jsonObject = (JsonObject) jsonElement;
 assertEquals("value1", jsonObject.get("name1").getAsString());
 }

 // Test for value method with boolean argument
 @Test
 public void testValueMethodWithBooleanArgument() {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 jsonTreeWriter.beginObject();
 jsonTreeWriter.name("name1").value(true);
 jsonTreeWriter.endObject();
 JsonElement jsonElement = jsonTreeWriter.get();
 JsonObject jsonObject = (JsonObject) jsonElement;
 assertTrue(jsonObject.get("name1").getAsJsonPrimitive().getAsBoolean());
 }

 // Test for value method with double argument
 @Test
 public void testValueMethodWithDoubleArgument() {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 jsonTreeWriter.beginObject();
 jsonTreeWriter.name("name1").value(3.14);
 jsonTreeWriter.endObject();
 JsonElement jsonElement = jsonTreeWriter.get();
 JsonObject jsonObject = (JsonObject) jsonElement;
 assertEquals(3.14, jsonObject.get("name1").getAsJsonPrimitive().getAsDouble(), 0.0);
 }

 // Test for value method with long argument
 @Test
 public void testValueMethodWithLongArgument() {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 jsonTreeWriter.beginObject();
 jsonTreeWriter.name("name1").value(42);
 jsonTreeWriter.endObject();
 JsonElement jsonElement = jsonTreeWriter.get();
 JsonObject jsonObject = (JsonObject) jsonElement;
 assertEquals(42, jsonObject.get("name1").getAsJsonPrimitive().getAsLong());
 }

 // Test for value method with Number argument
 @Test
 public void testValueMethodWithNumberArgument() {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 jsonTreeWriter.beginObject();
 jsonTreeWriter.name("name1").value(new BigDecimal("3.14"));
 jsonTreeWriter.endObject();
 JsonElement jsonElement = jsonTreeWriter.get();
 JsonObject jsonObject = (JsonObject) jsonElement;
 assertEquals("3.14", jsonObject.get("name1").getAsJsonPrimitive().getAsString());
 }

 // Test for nullValue method
 @Test
 public void testNullValueMethod() {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 jsonTreeWriter.value(null);
 JsonElement jsonElement = jsonTreeWriter.get();
 assertEquals(JsonNull.INSTANCE, jsonElement);
 }

 // Test for flush method
 @Test
 public void testFlushMethod() {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 jsonTreeWriter.beginObject();
 jsonTreeWriter.name("name1").value("value1");
 jsonTreeWriter.flush();
 JsonElement jsonElement = jsonTreeWriter.get();
 JsonObject jsonObject = (JsonObject) jsonElement;
 assertEquals("value1", jsonObject.get("name1").getAsString());
 }

 // Test for close method
 @Test
 public void testCloseMethod() {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 jsonTreeWriter.beginObject();
 jsonTreeWriter.name("name1").value("value1");
 jsonTreeWriter.close();
 JsonElement jsonElement = jsonTreeWriter.get();
 JsonObject jsonObject = (JsonObject) jsonElement;
 assertEquals("value1", jsonObject.get("name1").getAsString());
 }

 // Test for getting an element from an empty writer
 @Test(expected = IllegalStateException.class)
 public void testGetFromEmptyWriter() {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 JsonElement jsonElement = jsonTreeWriter.get();
 }

 // Test for adding an element to a non-array stack
 @Test(expected = IllegalStateException.class)
 public void testAddToNonArrayStack() {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 jsonTreeWriter.beginObject();
 jsonTreeWriter.put(new JsonPrimitive("value"));
 }

 // Test for adding an object to an array stack when the array is not empty
 @Test(expected = IllegalStateException.class)
 public void testAddToArrayStackWhenArrayIsNotEmpty() {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 jsonTreeWriter.beginArray();
 jsonTreeWriter.value("element1");
 jsonTreeWriter.put(new JsonObject());
 }

 // Test for adding a name-value pair to an object stack when the object is not empty
 @Test(expected = IllegalStateException.class)
 public void testAddNameValuePairToObjectStackWhenObjectIsNotEmpty() {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 jsonTreeWriter.beginObject();
 jsonTreeWriter.name("name1").value("value1");
 jsonTreeWriter.put(new JsonPrimitive("value2"));
 }

 // Test for adding an array to an array stack when the array is not empty
 @Test(expected = IllegalStateException.class)
 public void testAddArrayToArrayStackWhenArrayIsNotEmpty() {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 jsonTreeWriter.beginArray();
 jsonTreeWriter.value("element1");
 jsonTreeWriter.beginArray();
 }

 // Test for adding an object to an object stack when the object is not empty
 @Test(expected = IllegalStateException.class)
 public void testAddObjectToObjectStackWhenObjectIsNotEmpty() {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 jsonTreeWriter.beginObject();
 jsonTreeWriter.name("name1").value("value1");
 jsonTreeWriter.beginObject();
 }
}