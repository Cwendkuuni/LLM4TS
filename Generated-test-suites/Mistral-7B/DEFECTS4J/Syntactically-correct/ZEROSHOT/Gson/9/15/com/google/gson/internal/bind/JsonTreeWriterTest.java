import static org.junit.Assert.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.Test;

public class JsonTreeWriterTest {

  @Test
  public void testJsonTreeWriter() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();

    // Test empty writer
    assertEquals(JsonNull.INSTANCE, jsonTreeWriter.get());

    // Test beginObject and name
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("key1");
    jsonTreeWriter.value("value1");
    jsonTreeWriter.endObject();
    JsonElement jsonElement = jsonTreeWriter.get();
    JsonObject jsonObject = jsonElement.getAsJsonObject();
    assertEquals("value1", jsonObject.get("key1").getAsString());

    // Test beginArray and value
    jsonTreeWriter.beginArray();
    jsonTreeWriter.value(1);
    jsonTreeWriter.value(2);
    jsonTreeWriter.value(3.0);
    jsonTreeWriter.endArray();
    JsonArray jsonArray = jsonElement.getAsJsonArray();
    assertEquals(3, jsonArray.size());
    assertEquals(1, jsonArray.get(0).getAsInt());
    assertEquals(2, jsonArray.get(1).getAsInt());
    assertEquals(3.0, jsonArray.get(2).getAsDouble(), 0.001);

    // Test beginArray, value, and endObject
    jsonTreeWriter.beginArray();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("key2");
    jsonTreeWriter.value(4);
    jsonTreeWriter.endObject();
    jsonTreeWriter.value("value3");
    jsonTreeWriter.endArray();
    JsonArray jsonArray1 = jsonElement.getAsJsonArray();
    JsonElement jsonElement1 = jsonArray1.get(0);
    JsonObject jsonObject1 = jsonElement1.getAsJsonObject();
    assertEquals("value3", jsonElement1.getAsJsonArray().get(0).getAsString());
    assertEquals(4, jsonObject1.get("key2").getAsInt());

    // Test beginObject, name, and value
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("key3");
    jsonTreeWriter.value(new JsonPrimitive("value4"));
    jsonTreeWriter.endObject();
    JsonObject jsonObject2 = jsonElement.getAsJsonObject();
    assertEquals("value4", jsonObject2.get("key3").getAsString());

    // Test close writer without adding any value
    jsonTreeWriter.close();
    assertEquals(JsonPrimitive.valueOf("closed"), jsonTreeWriter.get());
  }
}