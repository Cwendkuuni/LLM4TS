import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonTreeWriterTest {

 @Test
 public void testEmptyWriter() {
 JsonTreeWriter writer = new JsonTreeWriter();
 JsonElement result = writer.get();
 assertEquals(JsonNull.INSTANCE, result);
 }

 @Test(expected = IllegalStateException.class)
 public void testBeginArrayWithoutEndArray() {
 JsonTreeWriter writer = new JsonTreeWriter();
 writer.beginArray();
 }

 @Test
 public void testBeginArrayEndArray() {
 JsonTreeWriter writer = new JsonTreeWriter();
 writer.beginArray();
 writer.endArray();
 JsonElement result = writer.get();
 assertEquals(JsonArray.class, result.getClass());
 assertEquals(0, result.getAsJsonArray().size());
 }

 @Test
 public void testBeginArrayAddElementEndArray() {
 JsonTreeWriter writer = new JsonTreeWriter();
 writer.beginArray();
 writer.value("element1");
 writer.endArray();
 JsonElement result = writer.get();
 assertEquals(JsonArray.class, result.getClass());
 assertEquals(1, result.getAsJsonArray().size());
 JsonElement element = result.getAsJsonArray().get(0);
 assertEquals("element1", element.getAsString());
 }

 @Test
 public void testBeginArrayAddMultipleElementsEndArray() {
 JsonTreeWriter writer = new JsonTreeWriter();
 writer.beginArray();
 writer.value("element1");
 writer.value("element2");
 writer.endArray();
 JsonElement result = writer.get();
 assertEquals(JsonArray.class, result.getClass());
 assertEquals(2, result.getAsJsonArray().size());
 }

 @Test
 public void testBeginObjectNameValueEndObject() {
 JsonTreeWriter writer = new JsonTreeWriter();
 writer.beginObject();
 writer.name("name1");
 writer.value("value1");
 writer.endObject();
 JsonElement result = writer.get();
 assertEquals(JsonObject.class, result.getClass());
 JsonObject jsonObject = result.getAsJsonObject();
 assertEquals("name1", jsonObject.name("name1").getAsString());
 assertEquals("value1", jsonObject.get("name1").getAsString());
 }

 @Test
 public void testBeginObjectAddMultipleNameValuePairEndObject() {
 JsonTreeWriter writer = new JsonTreeWriter();
 writer.beginObject();
 writer.name("name1");
 writer.value("value1");
 writer.name("name2");
 writer.value("value2");
 writer.endObject();
 JsonElement result = writer.get();
 assertEquals(JsonObject.class, result.getClass());
 JsonObject jsonObject = result.getAsJsonObject();
 assertEquals("value1", jsonObject.get("name1").getAsString());
 assertEquals("value2", jsonObject.get("name2").getAsString());
 }

 @Test(expected = IllegalStateException.class)
 public void testBeginObjectWithoutNameValuePairOrEndObject() {
 JsonTreeWriter writer = new JsonTreeWriter();
 writer.beginObject();
 }

 @Test(expected = IllegalStateException.class)
 public void testNameWithoutValue() {
 JsonTreeWriter writer = new JsonTreeWriter();
 writer.beginObject();
 writer.name("name1");
 writer.endObject();
 }

 @Test
 public void testNullValue() {
 JsonTreeWriter writer = new JsonTreeWriter();
 writer.value(null);
 JsonElement result = writer.get();
 assertTrue(result.isJsonNull());
 }

 @Test
 public void testBooleanValueTrue() {
 JsonTreeWriter writer = new JsonTreeWriter();
 writer.value(true);
 JsonElement result = writer.get();
 assertEquals(JsonPrimitive.class, result.getClass());
 assertEquals(true, result.getAsBoolean());
 }

 @Test
 public void testBooleanValueFalse() {
 JsonTreeWriter writer = new JsonTreeWriter();
 writer.value(false);
 JsonElement result = writer.get();
 assertEquals(JsonPrimitive.class, result.getClass());
 assertEquals(false, result.getAsBoolean());
 }

 @Test
 public void testStringValue() {
 JsonTreeWriter writer = new JsonTreeWriter();
 writer.value("testString");
 JsonElement result = writer.get();
 assertEquals(JsonPrimitive.class, result.getClass());
 assertEquals("testString", result.getAsString());
 }

 @Test
 public void testIntegerValue() {
 JsonTreeWriter writer = new JsonTreeWriter();
 writer.value(123);
 JsonElement result = writer.get();
 assertEquals(JsonPrimitive.class, result.getClass());
 assertEquals(123, result.getAsInt());
 }

 @Test
 public void testLongValue() {
 JsonTreeWriter writer = new JsonTreeWriter();writer.value(123L);
JsonElement result = writer.get();
assertEquals(JsonPrimitive.class, result.getClass());
assertEquals(123, result.getAsLong());
}

 @Test
 public void testDoubleValue() {
 JsonTreeWriter writer = new JsonTreeWriter();writer.value(123.45);
JsonElement result = writer.get();
assertEquals(JsonPrimitive.class, result.getClass());
assertEquals(123.45, result.getAsDouble(), 0.01);
}

}