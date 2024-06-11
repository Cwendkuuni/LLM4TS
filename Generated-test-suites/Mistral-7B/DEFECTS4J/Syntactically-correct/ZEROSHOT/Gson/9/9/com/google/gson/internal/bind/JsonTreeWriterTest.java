import static org.junit.Assert.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class JsonTreeWriterTest {

  @Test
  public void testEmptyJsonTree() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    JsonElement jsonElement = writer.get();
    assertEquals(JsonNull.INSTANCE, jsonElement);
  }

  @Test
  public void testJsonTreeWithArray() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.value("element1");
    writer.value(1);
    writer.value("element3");
    JsonElement jsonElement = writer.get();

    JsonArray jsonArray = jsonElement.getAsJsonArray();
    assertEquals(3, jsonArray.size());
    assertEquals("element1", jsonArray.get(0).getAsString());
    assertEquals(1, jsonArray.get(1).getAsInt());
    assertEquals("element3", jsonArray.get(2).getAsString());
  }

  @Test
  public void testJsonTreeWithObject() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name");
    writer.value("John Doe");
    writer.name("age");
    writer.value(30);
    writer.name("city");
    writer.value("New York");
    writer.endObject();
    JsonElement jsonElement = writer.get();

    JsonObject jsonObject = jsonElement.getAsJsonObject();
    assertEquals("John Doe", jsonObject.get("name").getAsString());
    assertEquals(30, jsonObject.get("age").getAsInt());
    assertEquals("New York", jsonObject.get("city").getAsString());
  }

  @Test
  public void testJsonTreeWithNestedArrayAndObject() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name");
    writer.value("John Doe");
    writer.name("age");
    writer.value(30);
    writer.name("cities");
    writer.beginArray();
    writer.value("New York");
    writer.beginArray();
    writer.value("boroughs");
    writer.beginArray();
    writer.value("Manhattan");
    writer.value("Bronx");
    writer.value("Brooklyn");
    writer.value("Queens");
    writer.endArray();
    writer.endArray();
    writer.endObject();
    JsonElement jsonElement = writer.get();

    JsonObject jsonObject = jsonElement.getAsJsonObject();
    assertEquals("John Doe", jsonObject.get("name").getAsString());
    assertEquals(30, jsonObject.get("age").getAsInt());
    JsonArray citiesJsonArray = jsonObject.get("cities").getAsJsonArray();
    assertEquals(1, citiesJsonArray.size());
    JsonElement cityJsonElement = citiesJsonArray.get(0);
    assertEquals("New York", cityJsonElement.getAsJsonObject().get("name").getAsString());
    JsonArray boroughsJsonArray = cityJsonElement.getAsJsonObject().get("boroughs").getAsJsonArray();
    assertEquals(4, boroughsJsonArray.size());
    assertEquals("Manhattan", boroughsJsonArray.get(0).getAsString());
    assertEquals("Bronx", boroughsJsonArray.get(1).getAsString());
    assertEquals("Brooklyn", boroughsJsonArray.get(2).getAsString());
    assertEquals("Queens", boroughsJsonArray.get(3).getAsString());
  }

  @Test
  public void testJsonTreeWithNestedObjectAndArray() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name");
    writer.value("John Doe");
    writer.name("age");
    writer.value(30);
    writer.name("cities");
    writer.beginArray();
    writer.beginObject();
    writer.name("name");
    writer.value("New York");
    writer.name("boroughs");
    writer.beginArray();
    writer.value("Manhattan");
    writer.value("Bronx");
    writer.value("Brooklyn");
    writer.value("Queens");
    writer.endArray();
    writer.endObject();
    writer.value("Los Angeles");
    writer.endArray();
    writer.endObject();
    JsonElement jsonElement = writer.get();

    JsonObject jsonObject = jsonElement.getAsJsonObject();
    assertEquals("John Doe", jsonObject.get("name").getAsString());
    assertEquals(30, jsonObject.get("age").getAsInt());
    JsonArray citiesJsonArray = jsonObject.get("cities").getAsJsonArray();
    assertEquals(2, citiesJsonArray.size());
    JsonElement cityJsonElement1 = citiesJsonArray.get(0);
    assertEquals("New York", cityJsonElement1.getAsJsonObject().get("name").getAsString());
    JsonArray boroughsJsonArray1 = cityJsonElement1.getAsJsonObject().get("boroughs").getAsJsonArray();
    assertEquals(4, boroughsJsonArray1.size());
    assertEquals("Manhattan", boroughsJsonArray1.get(0).getAsString());
    assertEquals("Bronx", boroughsJsonArray1.get(1).getAsString());
    assertEquals("Brooklyn", boroughsJsonArray1.get(2).getAsString());
    assertEquals("Queens", boroughsJsonArray1.get(3).getAsString());
    JsonElement cityJsonElement2 = citiesJsonArray.get(1);
    assertEquals("Los Angeles", cityJsonElement2.getAsString());
  }
}