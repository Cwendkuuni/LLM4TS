package com.google.gson.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.junit.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class DefaultDateTypeAdapterTest {

  @Test
  public void testDefaultDateTypeAdapter_DefaultConstructor() {
    TypeAdapter<Date> adapter = new DefaultDateTypeAdapter<>();

    Date date = new Date();
    String jsonDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(date);

    JsonElement jsonElement = JsonParser.parseString(String.format("{\"date\":\"%s\"}", jsonDate));
    JsonReader jsonReader = new JsonReader(new java.io.StringReader(jsonElement.getAsString()));

    Date deserializedDate = adapter.read(jsonReader);

    assertEquals("Deserialized date should be equal to original date", date, deserializedDate);
  }

  @Test
  public void testDefaultDateTypeAdapter_CustomConstructor() throws SQLException, ParseException {
    String datePattern = "dd-MM-yyyy HH:mm:ss.SSS";
    TypeAdapter<Date> adapter = new DefaultDateTypeAdapter(Date.class, new SimpleDateFormat(datePattern));

    Date date = new Date();
    String jsonDate = new SimpleDateFormat(datePattern).format(date);

    JsonElement jsonElement = JsonParser.parseString(String.format("{\"date\":\"%s\"}", jsonDate));
    JsonReader jsonReader = new JsonReader(new java.io.StringReader(jsonElement.getAsString()));

    Date deserializedDate = adapter.read(jsonReader);

    assertEquals("Deserialized date should be equal to original date", date, deserializedDate);
  }

  @Test
  public void testDefaultDateTypeAdapter_NullDate() {
    TypeAdapter<Date> adapter = new DefaultDateTypeAdapter<>(Date.class);

    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("date", null);

    JsonReader jsonReader = new JsonReader(new java.io.StringReader(jsonObject.toString()));
    Date deserializedDate = adapter.read(jsonReader);

    assertNull("Deserialized date should be null when input is null");
  }

  @Test
  public void testDefaultDateTypeAdapter_EmptyJsonString() {
    TypeAdapter<Date> adapter = new DefaultDateTypeAdapter<>(Date.class);

    JsonElement jsonElement = JsonParser.parseString("");
    JsonReader jsonReader = new JsonReader(new java.io.StringReader(jsonElement.getAsString()));

    try {
      adapter.read(jsonReader);
      fail("Should have thrown JsonParseException when input is empty string");
    } catch (JsonParseException e) {
      // Expected
    }
  }

  @Test
  public void testDefaultDateTypeAdapter_InvalidJsonString() {
    TypeAdapter<Date> adapter = new DefaultDateTypeAdapter<>(Date.class);

    JsonElement jsonElement = JsonParser.parseString("{\"date\":\"invalid_json\"}");
    JsonReader jsonReader = new JsonReader(new java.io.StringReader(jsonElement.getAsString()));

    try {
      adapter.read(jsonReader);
      fail("Should have thrown JsonParseException when input is invalid JSON string");
    } catch (JsonParseException e) {
      // Expected
    }
  }

  @Test
  public void testDefaultDateTypeAdapter_DateTimeFormatException() throws ParseException {
    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-HH:mm:ss.SSS");
    TypeAdapter<Date> adapter = new DefaultDateTypeAdapter(Date.class, dateFormat);

    String jsonDate = "2022-12-31-24:60:00.000";
    JsonElement jsonElement = JsonParser.parseString(String.format("{\"date\":\"%s\"}", jsonDate));
    JsonReader jsonReader = new JsonReader(new java.io.StringReader(jsonElement.getAsString()));

    try {
      adapter.read(jsonReader);
      fail("Should have thrown ParseException when input date string does not match the format");
    } catch (ParseException e) {
      // Expected
    }
  }

  @Test
  public void testDefaultDateTypeAdapter_Serialization() throws IOException {
    TypeAdapter<Date> adapter = new DefaultDateTypeAdapter<>(Date.class);

    Date date = new Date();

    JsonWriter jsonWriter = new JsonWriter(System.out);
    adapter.write(jsonWriter, date);

    JsonElement jsonElement = new JsonParser().parse(System.in);
    JsonObject jsonObject = jsonElement.getAsJsonObject();
    assertEquals("Deserialized date should be equal to original date", jsonObject.get("date").getAsString(), new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(date));
  }

  @Test
  public void testDefaultDateTypeAdapter_Serialization_Timestamp() throws IOException {
    TypeAdapter<Date> adapter = new DefaultDateTypeAdapter<>(Timestamp.class);

    Timestamp timestamp = new Timestamp(System.currentTimeMillis());

    JsonWriter jsonWriter = new JsonWriter(System.out);
    adapter.write(jsonWriter, timestamp);

    JsonElement jsonElement = new JsonParser().parse(System.in);
    JsonObject jsonObject = jsonElement.getAsJsonObject();
    assertEquals("Deserialized timestamp should be equal to original timestamp", jsonObject.get("date").getAsString(), new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(timestamp.toDate()));
  }

  @Test
  public void testDefaultDateTypeAdapter_Serialization_SqlDate() throws IOException {
    TypeAdapter<Date> adapter = new DefaultDateTypeAdapter<>(java.sql.Date.class);

    java.sql.Date sqlDate = new java.sql.Date(System.currentTimeMillis());

    JsonWriter jsonWriter = new JsonWriter(System.out);
    adapter.write(jsonWriter, sqlDate);

    JsonElement jsonElement = new JsonParser().parse(System.in);
    JsonObject jsonObject = jsonElement.getAsJsonObject();
    assertEquals("Deserialized sqlDate should be equal to original sqlDate", jsonObject.get("date").getAsString(), new SimpleDateFormat("yyyy-MM-dd").format(sqlDate.toDate()));
  }
}