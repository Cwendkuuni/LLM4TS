import org.junit.Test;
import static org.junit.Assert.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class JsonTreeWriterTest {

 @Test
 public void testJsonTreeWriterBasicUsage() {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();

 // Begin object
 jsonTreeWriter.beginObject();
 jsonTreeWriter.name("key1");
 jsonTreeWriter.value("value1");
 jsonTreeWriter.name("key2");
 jsonTreeWriter.value(123);
 jsonTreeWriter.endObject();

 JsonElement jsonElement = jsonTreeWriter.get();
 JsonObject jsonObject = jsonElement.getAsJsonObject();

 assertEquals("value1", jsonObject.get("key1").getAsString());
 assertEquals(123, jsonObject.get("key2").getAsInt());
 }

 @Test
 public void testJsonTreeWriterArrayUsage() {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();

 // Begin array
 jsonTreeWriter.beginArray();
 jsonTreeWriter.value("element1");
 jsonTreeWriter.value(1);
 jsonTreeWriter.value(true);
 jsonTreeWriter.endArray();

 JsonElement jsonElement = jsonTreeWriter.get();
 JsonArray jsonArray = jsonElement.getAsJsonArray();

 assertEquals(3, jsonArray.size());
 assertEquals("element1", jsonArray.get(0).getAsString());
 assertEquals(1, jsonArray.get(1).getAsInt());
 assertEquals(true, jsonArray.get(2).isJsonPrimitive());
 }

 @Test
 public void testJsonTreeWriterNullValue() {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();

 jsonTreeWriter.value(null);
 JsonElement jsonElement = jsonTreeWriter.get();

 assertTrue(jsonElement.isJsonNull());
 }

 @Test(expected = IllegalStateException.class)
 public void testJsonTreeWriterInvalidUsage1() {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 jsonTreeWriter.beginObject();
 jsonTreeWriter.name("key1");
 jsonTreeWriter.value("value1");
 jsonTreeWriter.value(123);
 jsonTreeWriter.beginArray();
 jsonTreeWriter.name("name");
 jsonTreeWriter.value("value");
 jsonTreeWriter.endArray();
 jsonTreeWriter.endObject();
 }

 @Test(expected = IllegalStateException.class)
 public void testJsonTreeWriterInvalidUsage2() {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 jsonTreeWriter.beginArray();
 jsonTreeWriter.value("value1");
 jsonTreeWriter.beginObject();
 jsonTreeWriter.name("key1");
 jsonTreeWriter.value("value2");
 jsonTreeWriter.endObject();
 jsonTreeWriter.value("value3");
 jsonTreeWriter.endArray();
 }

 @Test(expected = IllegalStateException.class)
 public void testJsonTreeWriterInvalidUsage3() {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 jsonTreeWriter.beginArray();
 jsonTreeWriter.value("value1");
 jsonTreeWriter.value("value2");
 jsonTreeWriter.name("name");
 jsonTreeWriter.endArray();
 }

 @Test(expected = IllegalStateException.class)
 public void testJsonTreeWriterInvalidUsage4() {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 jsonTreeWriter.beginObject();
 jsonTreeWriter.name("name1");
 jsonTreeWriter.name("name2");
 jsonTreeWriter.endObject();
 jsonTreeWriter.name("name3");
 }

 @Test(expected = IllegalStateException.class)
 public void testJsonTreeWriterInvalidUsage5() {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 jsonTreeWriter.beginArray();
 jsonTreeWriter.endArray();
 jsonTreeWriter.value("value");
 }

 @Test(expected = IllegalStateException.class)
 public void testJsonTreeWriterInvalidUsage6() {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 jsonTreeWriter.beginObject();
 jsonTreeWriter.value("value");
 jsonTreeWriter.name("name");
 jsonTreeWriter.endObject();
 }

 @Test
 public void testJsonTreeWriterClose() throws IOException {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 jsonTreeWriter.beginObject();
 jsonTreeWriter.name("key1");
 jsonTreeWriter.value("value1");
 jsonTreeWriter.name("key2");
 jsonTreeWriter.value(123);
 jsonTreeWriter.endObject();
 jsonTreeWriter.close();

 JsonElement jsonElement = jsonTreeWriter.get();
 JsonObject jsonObject = jsonElement.getAsJsonObject();

 assertEquals("value1", jsonObject.get("key1").getAsString());
 assertEquals(123, jsonObject.get("key2").getAsInt());
 }

 @Test(expected = IOException.class)
 public void testJsonTreeWriterCloseTwice() throws IOException {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 jsonTreeWriter.beginObject();
 jsonTreeWriter.name("key1");
 jsonTreeWriter.value("value1");
 jsonTreeWriter.name("key2");
 jsonTreeWriter.value(123);
 jsonTreeWriter.endObject();
 jsonTreeWriter.close();
 jsonTreeWriter.close();
 }
}