import static org.junit.Assert.*;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonWriter;

import org.junit.Before;
import org.junit.Test;

public class JsonTreeWriterTest {

 private JsonTreeWriter writer;

 @Before
 public void setup() {
 writer = new JsonTreeWriter();
 }

 @Test
 public void testGet() {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 JsonObject jsonObject = jsonTreeWriter.beginObject().name("key").value("value").endObject();
 assertEquals(jsonObject, jsonTreeWriter.get());
 }

 @Test
 public void testBeginArray() {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 JsonArray jsonArray = jsonTreeWriter.beginArray().endArray();
 assertEquals(jsonArray, jsonTreeWriter.get());
 }

 @Test(expected = IllegalStateException.class)
 public void testBeginArrayTwice() {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 jsonTreeWriter.beginArray();
 jsonTreeWriter.beginArray();
 }

 @Test(expected = IllegalStateException.class)
 public void testEndArrayWithoutBeginArray() {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 jsonTreeWriter.endArray();
 }

 @Test
 public void testBeginObject() {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 JsonObject jsonObject = jsonTreeWriter.beginObject().name("key1", "value1").name("key2", "value2").endObject();
 assertEquals(jsonObject, jsonTreeWriter.get());
 }

 @Test(expected = IllegalStateException.class)
 public void testBeginObjectTwice() {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 jsonTreeWriter.beginObject();
 jsonTreeWriter.beginObject();
 }

 @Test(expected = IllegalStateException.class)
 public void testEndObjectWithoutBeginObject() {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 jsonTreeWriter.endObject();
 }

 @Test
 public void testName() {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 JsonObject jsonObject = jsonTreeWriter.beginObject().name("key1", null).name("key2", "value2").endObject();
 JsonParser parser = new Gson().newParser(new Gson().toJson(jsonObject));
 JsonObject json = parser.getAsJsonObject();
 assertEquals("value2", json.get("key2").getAsString());
 }

 @Test
 public void testValueString() {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 JsonObject jsonObject = jsonTreeWriter.beginObject().name("key", "").value("value").endObject();
 assertEquals("value", jsonTreeWriter.get().getAsJsonObject().get("value").getAsString());
 }

 @Test
 public void testValueBoolean() {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 JsonObject jsonObject = jsonTreeWriter.beginObject().name("key", "").value(true).endObject();
 assertEquals(true, jsonTreeWriter.get().getAsJsonObject().get("value").getAsBoolean());
 }

 @Test
 public void testValueDouble() {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 JsonObject jsonObject = jsonTreeWriter.beginObject().name("key", "").value(3.14).endObject();
 assertEquals(3.14, jsonTreeWriter.get().getAsJsonObject().get("value").getAsDouble(), 0.0);
 }

 @Test
 public void testValueLong() {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 JsonObject jsonObject = jsonTreeWriter.beginObject().name("key", "").value(42L).endObject();
 assertEquals(42L, jsonTreeWriter.get().getAsJsonObject().get("value").getAsLong());
 }

 @Test
 public void testValueNumber() {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 JsonObject jsonObject = jsonTreeWriter.beginObject().name("key", "").value(new Long(42)).endObject();
 assertEquals(42L, jsonTreeWriter.get().getAsJsonObject().get("value").getAsLong());
 }

 @Test
 public void testBeginArrayAddValue() {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 JsonArray jsonArray = jsonTreeWriter.beginArray().value("value1").value("value2").endArray();
 assertEquals("[\"value1\", \"value2\"]", jsonTreeWriter.get().toString());
 }

 @Test
 public void testBeginObjectAddValue() {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 JsonObject jsonObject = jsonTreeWriter.beginObject().name("key", "").value("value1").endObject();
 assertEquals("{\"key\":\"value1\"}", jsonTreeWriter.get().toString());
 }

 @Test
 public void testBeginArrayAddObject() {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 JsonArray jsonArray = jsonTreeWriter.beginArray().beginObject().name("key", "").value("value").endObject().endArray();
 assertEquals("[{\"key\":\"value\"}]", jsonTreeWriter.get().toString());
 }

 @Test
 public void testBeginObjectAddArray() {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 JsonObject jsonObject = jsonTreeWriter.beginObject().name("key", new JsonArray()).endObject();
 assertEquals("{\"key\":[]}", jsonTreeWriter.get().toString());
 }

 @Test
 public void testBeginObjectAddArrayWithValue() {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 JsonObject jsonObject = jsonTreeWriter.beginObject().name("key", new JsonArray().value("value")).endObject();
 assertEquals("{\"key\":[\"value\"]}", jsonTreeWriter.get().toString());
 }

 @Test
 public void testBeginObjectAddObjectWithArray() {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 JsonObject jsonObject = jsonTreeWriter.beginObject().name("key", new JsonObject().name("subKey", "value")).endObject();
 assertEquals("{\"key\":{\"subKey\":\"value\"}}", jsonTreeWriter.get().toString());
 }

 @Test
 public void testBeginObjectAddObjectWithValue() {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 JsonObject jsonObject = jsonTreeWriter.beginObject().name("key", new JsonObject().name("subKey", "value1").name("subKey2", "value2")).endObject();
 assertEquals("{\"key\":{\"subKey\":\"value1\",\"subKey2\":\"value2\"}}", jsonTreeWriter.get().toString());
 }

 @Test
 public void testClose() throws IOException {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 jsonTreeWriter.beginArray().value("value1").value("value2").endArray();
 jsonTreeWriter.close();
 assertEquals("[\"value1\", \"value2\"]", jsonTreeWriter.get().toString());
 }
}