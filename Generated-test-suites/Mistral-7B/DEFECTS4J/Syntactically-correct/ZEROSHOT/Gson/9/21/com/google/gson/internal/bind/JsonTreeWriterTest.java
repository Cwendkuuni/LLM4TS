import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class JsonTreeWriterTest {

 @Test
 public void testJsonTreeWriter_empty() {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 JsonElement jsonElement = jsonTreeWriter.get();
 assertEquals(JsonNull.INSTANCE, jsonElement);
 }

 @Test
 public void testJsonTreeWriter_beginArray() {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 jsonTreeWriter.beginArray();
 JsonElement jsonElement = jsonTreeWriter.get();
 assertTrue(jsonElement instanceof JsonArray);
 jsonTreeWriter.endArray();
 jsonElement = jsonTreeWriter.get();
 assertEquals(JsonNull.INSTANCE, jsonElement);
 }

 @Test
 public void testJsonTreeWriter_beginObject() {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 jsonTreeWriter.beginObject();
 jsonTreeWriter.name("name").value("value");
 jsonTreeWriter.endObject();
 JsonElement jsonElement = jsonTreeWriter.get();
 JsonObject jsonObject = jsonElement.getAsJsonObject();
 assertEquals("value", jsonObject.get("name").getAsString());
 jsonTreeWriter.endObject();
 jsonElement = jsonTreeWriter.get();
 assertEquals(JsonNull.INSTANCE, jsonElement);
 }

 @Test(expected = IllegalStateException.class)
 public void testJsonTreeWriter_mixArrayAndObject() {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 jsonTreeWriter.beginArray();
 jsonTreeWriter.name("name").value("value");
 jsonTreeWriter.endObject();
 jsonTreeWriter.endArray();
 }

 @Test(expected = IllegalStateException.class)
 public void testJsonTreeWriter_missingEndObject() {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 jsonTreeWriter.beginObject();
 jsonTreeWriter.name("name").value("value");
 }

 @Test(expected = IllegalStateException.class)
 public void testJsonTreeWriter_missingEndArray() {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 jsonTreeWriter.beginArray();
 jsonTreeWriter.beginObject();
 jsonTreeWriter.name("name").value("value");
 }

 @Test
 public void testJsonTreeWriter_nestedObjects() {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 jsonTreeWriter.beginObject();
 jsonTreeWriter.name("outer").beginObject();
 jsonTreeWriter.name("inner").value("value");
 jsonTreeWriter.endObject();
 jsonTreeWriter.endObject();
 JsonElement jsonElement = jsonTreeWriter.get();
 JsonObject jsonObject = jsonElement.getAsJsonObject();
 JsonObject outer = jsonObject.getAsJsonObject().get("outer").getAsJsonObject();
 assertEquals("value", outer.get("inner").getAsString());
 jsonTreeWriter.endObject();
 jsonElement = jsonTreeWriter.get();
 assertEquals(JsonNull.INSTANCE, jsonElement);
 }

 @Test
 public void testJsonTreeWriter_nullValue() {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 jsonTreeWriter.beginObject().name("name").value(null);
 jsonTreeWriter.endObject();
 JsonElement jsonElement = jsonTreeWriter.get();
 JsonObject jsonObject = jsonElement.getAsJsonObject();
 assertNull(jsonObject.get("name"));
 jsonTreeWriter.endObject();
 jsonElement = jsonTreeWriter.get();
 assertEquals(JsonNull.INSTANCE, jsonElement);
 }

 @Test
 public void testJsonTreeWriter_booleanValue() {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 jsonTreeWriter.beginObject().name("name").value(true);
 jsonTreeWriter.endObject();
 JsonElement jsonElement = jsonTreeWriter.get();
 JsonObject jsonObject = jsonElement.getAsJsonObject();
 JsonPrimitive booleanValue = jsonObject.get("name").getAsJsonPrimitive();
 assertTrue(booleanValue.isBoolean());
 assertTrue(booleanValue.getAsBoolean());
 jsonTreeWriter.endObject();
 jsonElement = jsonTreeWriter.get();
 assertEquals(JsonNull.INSTANCE, jsonElement);
 }

 @Test
 public void testJsonTreeWriter_numberValue() {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 jsonTreeWriter.beginObject().name("name").value(123.45);
 jsonTreeWriter.endObject();
 JsonElement jsonElement = jsonTreeWriter.get();
 JsonObject jsonObject = jsonElement.getAsJsonObject();
 JsonPrimitive numberValue = jsonObject.get("name").getAsJsonPrimitive();
 assertEquals(123.45, numberValue.getAsDouble(), 0.001);
 jsonTreeWriter.endObject();
 jsonElement = jsonTreeWriter.get();
 assertEquals(JsonNull.INSTANCE, jsonElement);
 }

 @Test
 public void testJsonTreeWriter_stringValue() {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 jsonTreeWriter.beginObject().name("name").value("value");
 jsonTreeWriter.endObject();
 JsonElement jsonElement = jsonTreeWriter.get();
 JsonObject jsonObject = jsonElement.getAsJsonObject();
 JsonPrimitive stringValue = jsonObject.get("name").getAsJsonPrimitive();
 assertEquals("value", stringValue.getAsString());
 jsonTreeWriter.endObject();
 jsonElement = jsonTreeWriter.get();
 assertEquals(JsonNull.INSTANCE, jsonElement);
 }

 @Test
 public void testJsonTreeWriter_arrayValue() {
 JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
 jsonTreeWriter.beginObject().name("name").beginArray().value(1).value(2).endArray();
 jsonTreeWriter.endObject();
 JsonElement jsonElement = jsonTreeWriter.get();
 JsonObject jsonObject = jsonElement.getAsJsonObject();
 JsonPrimitive name = jsonObject.get("name").getAsJsonPrimitive();
 assertEquals("name", name.getAsString());
 JsonArray array = name.getAsJsonArray();
 assertEquals(2, array.size());
 assertEquals(1, array.get(0).getAsInt());
 assertEquals(2, array.get(1).getAsInt());
 jsonTreeWriter.endObject();
 jsonElement = jsonTreeWriter.get();
 assertEquals(JsonNull.INSTANCE, jsonElement);
 }
}