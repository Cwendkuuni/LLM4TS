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
 assertTrue(result.isJsonNull());
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
 assertTrue(result.isJsonArray());
 assertEquals(0, result.getAsJsonArray().size());
 }

 @Test
 public void testBeginArrayAddOneValueEndArray() {
 JsonTreeWriter writer = new JsonTreeWriter();
 writer.beginArray();
 writer.value("test");
 writer.endArray();
 JsonElement result = writer.get();
 assertTrue(result.isJsonArray());
 JsonArray jsonArray = result.getAsJsonArray();
 assertEquals(1, jsonArray.size());
 assertEquals("test", jsonArray.get(0).getAsString());
 }

 @Test
 public void testBeginArrayAddMultipleValuesEndArray() {
 JsonTreeWriter writer = new JsonTreeWriter();
 writer.beginArray();
 writer.value("test1");
 writer.value("test2");
 writer.value("test3");
 writer.endArray();
 JsonElement result = writer.get();
 assertTrue(result.isJsonArray());
 JsonArray jsonArray = result.getAsJsonArray();
 assertEquals(3, jsonArray.size());
 assertEquals("test1", jsonArray.get(0).getAsString());
 assertEquals("test2", jsonArray.get(1).getAsString());
 assertEquals("test3", jsonArray.get(2).getAsString());
 }

 @Test(expected = IllegalStateException.class)
 public void testBeginObjectWithoutName() {
 JsonTreeWriter writer = new JsonTreeWriter();
 writer.beginObject();
 }

 @Test
 public void testBeginObjectWithNameEndObject() {
 JsonTreeWriter writer = new JsonTreeWriter();
 writer.beginObject();
 writer.name("test").endObject();
 JsonElement result = writer.get();
 assertTrue(result.isJsonObject());
 assertEquals(0, result.getAsJsonObject().size());
 }

 @Test
 public void testBeginObjectAddOneKeyValuePairEndObject() {
 JsonTreeWriter writer = new JsonTreeWriter();
 writer.beginObject();
 writer.name("test").value("test");
 writer.endObject();
 JsonElement result = writer.get();
 assertTrue(result.isJsonObject());
 JsonObject jsonObject = result.getAsJsonObject();
 assertEquals(1, jsonObject.size());
 assertEquals("test", jsonObject.keySet().iterator().next());
 assertEquals("test", jsonObject.get("test").getAsString());
 }

 @Test
 public void testBeginObjectAddMultipleKeyValuePairsEndObject() {
 JsonTreeWriter writer = new JsonTreeWriter();
 writer.beginObject();
 writer.name("test1").value("test1");
 writer.name("test2").value("test2");
 writer.endObject();
 JsonElement result = writer.get();
 assertTrue(result.isJsonObject());
 JsonObject jsonObject = result.getAsJsonObject();
 assertEquals(2, jsonObject.size());
 assertEquals("test1", jsonObject.keySet().iterator().next());
 assertEquals("test1", jsonObject.get("test1").getAsString());
 assertEquals("test2", jsonObject.get("test2").getAsString());
 }

 @Test(expected = IllegalStateException.class)
 public void testNullValueAfterBeginObject() {
 JsonTreeWriter writer = new JsonTreeWriter();
 writer.beginObject();
 writer.nullValue();
 }

 @Test(expected = IllegalStateException.class)
 public void testNameAfterBeginArray() {
 JsonTreeWriter writer = new JsonTreeWriter();
 writer.beginArray();
 writer.name("test");
 }

 @Test(expected = IllegalStateException.class)
 public void testValueAfterBeginObjectName() {
 JsonTreeWriter writer = new JsonTreeWriter();
 writer.beginObject();
 writer.name("test");
 writer.value("test");
 writer.name("test2");
 }

 @Test(expected = IllegalStateException.class)
 public void testEndArrayAfterBeginObject() {
 JsonTreeWriter writer = new JsonTreeWriter();
 writer.beginObject();
 writer.beginArray();
 writer.endArray();
 }

 @Test(expected = IllegalStateException.class)
 public void testEndObjectAfterBeginArray() {
 JsonTreeWriter writer = new JsonTreeWriter();
 writer.beginArray();
 writer.endObject();
 }

 @Test(expected = IllegalStateException.class)
 public void testBeginObjectAfterBeginObject() {
 JsonTreeWriter writer = new JsonTreeWriter();
 writer.beginObject();
 writer.beginObject();
 }

 @Test(expected = IllegalStateException.class)
 public void testCloseWriterMultipleTimes() {
 JsonTreeWriter writer = new JsonTreeWriter();
 writer.close();
 writer.close();
 }
}