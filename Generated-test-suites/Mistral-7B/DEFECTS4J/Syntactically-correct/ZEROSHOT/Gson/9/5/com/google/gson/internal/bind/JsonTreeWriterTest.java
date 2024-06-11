import org.junit.Test;
import static org.junit.Assert.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class JsonTreeWriterTest {

 @Test
 public void testJsonTreeWriter_Empty() {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 JsonElement jsonElement = jsonTreeWriter.get();
 assertNull(jsonElement);
 }

 @Test
 public void testJsonTreeWriter_BeginArray() {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 jsonTreeWriter.beginArray();
 JsonElement jsonElement = jsonTreeWriter.get();
 JsonArray jsonArray = (JsonArray) jsonElement;
 assertEquals(0, jsonArray.size());
 }

 @Test(expected = IllegalStateException.class)
 public void testJsonTreeWriter_BeginArray_AfterBeginObject() {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 jsonTreeWriter.beginObject();
 jsonTreeWriter.beginArray();
 }

 @Test
 public void testJsonTreeWriter_BeginObject() {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 jsonTreeWriter.beginObject();
 JsonElement jsonElement = jsonTreeWriter.get();
 JsonObject jsonObject = (JsonObject) jsonElement;
 assertNull(jsonObject.get("name"));
 }

 @Test
 public void testJsonTreeWriter_Name() {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 jsonTreeWriter.beginObject();
 jsonTreeWriter.name("name");
 JsonElement jsonElement = jsonTreeWriter.get();
 JsonObject jsonObject = (JsonObject) jsonElement;
 assertEquals("name", jsonObject.get("name").getName());
 }

 @Test(expected = IllegalStateException.class)
 public void testJsonTreeWriter_Name_AfterEndObject() {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 jsonTreeWriter.beginObject();
 jsonTreeWriter.endObject();
 jsonTreeWriter.name("name");
 }

 @Test
 public void testJsonTreeWriter_Value_String() {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 jsonTreeWriter.beginObject();
 jsonTreeWriter.name("string");
 jsonTreeWriter.value("test");
 JsonElement jsonElement = jsonTreeWriter.get();
 JsonObject jsonObject = (JsonObject) jsonElement;
 JsonPrimitive string = jsonObject.get("string");
 assertEquals("test", string.getAsString());
 }

 @Test
 public void testJsonTreeWriter_Value_Boolean() {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 jsonTreeWriter.beginObject();
 jsonTreeWriter.name("boolean");
 jsonTreeWriter.value(true);
 JsonElement jsonElement = jsonTreeWriter.get();
 JsonObject jsonObject = (JsonObject) jsonElement;
 JsonPrimitive boolean_ = jsonObject.get("boolean");
 assertTrue(boolean_.isJsonPrimitive());
 assertTrue(boolean_.isBoolean());
 assertTrue(boolean_.getAsBoolean());
 }

 @Test
 public void testJsonTreeWriter_Value_Number() {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 jsonTreeWriter.beginObject();
 jsonTreeWriter.name("number");
 jsonTreeWriter.value(123);
 JsonElement jsonElement = jsonTreeWriter.get();
 JsonObject jsonObject = (JsonObject) jsonElement;
 JsonPrimitive number = jsonObject.get("number");
 assertEquals(123, number.getAsInt());
 }

 @Test
 public void testJsonTreeWriter_BeginArray_InArray() {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 jsonTreeWriter.beginArray();
 jsonTreeWriter.beginArray();
 JsonElement jsonElement = jsonTreeWriter.get();
 JsonArray jsonArray = (JsonArray) jsonElement;
 assertEquals(0, jsonArray.size());
 }

 @Test
 public void testJsonTreeWriter_EndArray_InArray() {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 jsonTreeWriter.beginArray();
 jsonTreeWriter.beginArray();
 jsonTreeWriter.endArray();
 JsonElement jsonElement = jsonTreeWriter.get();
 JsonArray jsonArray = (JsonArray) jsonElement;
 assertEquals(0, jsonArray.size());
 }

 @Test
 public void testJsonTreeWriter_BeginObject_InArray() {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 jsonTreeWriter.beginArray();
 jsonTreeWriter.beginObject();
 JsonElement jsonElement = jsonTreeWriter.get();
 JsonArray jsonArray = (JsonArray) jsonElement;
 assertEquals(1, jsonArray.size());
 JsonObject jsonObject = jsonArray.get(0).getAsJsonObject();
 assertNull(jsonObject.get("name"));
 }

 @Test
 public void testJsonTreeWriter_EndObject_InObject() {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 JsonTreeWriter innerJsonTreeWriter = new JsonTreeWriter();
 jsonTreeWriter.beginArray();
 jsonTreeWriter.beginObject();
 jsonTreeWriter.name("inner");
 jsonTreeWriter.beginObject();
 innerJsonTreeWriter.name("name");
 innerJsonTreeWriter.value("test");
 jsonTreeWriter.endObject();
 JsonElement jsonElement = jsonTreeWriter.get();
 JsonArray jsonArray = (JsonArray) jsonElement;
 assertEquals(1, jsonArray.size());
 JsonObject jsonObject = jsonArray.get(0).getAsJsonObject();
 JsonObject innerJsonObject = jsonObject.get("inner").getAsJsonObject();
 JsonPrimitive name = innerJsonObject.get("name");
 assertEquals("test", name.getAsString());
 }

 @Test
 public void testJsonTreeWriter_Close() {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 jsonTreeWriter.beginArray();
 jsonTreeWriter.endArray();
 JsonElement jsonElement = jsonTreeWriter.get();
 JsonArray jsonArray = (JsonArray) jsonElement;
 assertEquals(0, jsonArray.size());
 try {
 jsonTreeWriter.get();
 fail();
 } catch (IllegalStateException e) {
 // expected
 }
 }

 @Test(expected = IOException.class)
 public void testJsonTreeWriter_Close_WithWriter() {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 jsonTreeWriter.beginArray();
 StringWriter stringWriter = new StringWriter();
 jsonTreeWriter.setWriter(stringWriter);
 jsonTreeWriter.endArray();
 jsonTreeWriter.get();
 }
}