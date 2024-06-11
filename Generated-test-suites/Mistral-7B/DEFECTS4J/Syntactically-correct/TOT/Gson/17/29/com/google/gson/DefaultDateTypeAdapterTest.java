package com.google.gson.tests;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.stream.JsonReader;

import junit.framework.Assert;

public class DefaultDateTypeAdapterTest {

  @Test
  public void testSerializationAndDeserialization() throws ParseException, SQLException {
    // Create an instance of DefaultDateTypeAdapter
    Gson gson = new Gson();
    TypeAdapter<Date> dateAdapter = gson.getAdapter(Date.class);

    // Serialize a Date object
    Date date = new Date();
    String jsonDate = gson.toJson(date, Date.class);

    // Deserialize the JSON string back to a Date object
    JsonParser parser = new JsonParser();
    JsonElement jsonDateElement = parser.parse(jsonDate);
    Date deserializedDate = dateAdapter.read(parser.newStreamReader(jsonDateElement.getAsString()));

    // Verify that the deserialized Date object is the same as the original one
    assertEquals(date, deserializedDate);

    // Test serialization and deserialization for Timestamp and java.sql.Date
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    assertNotNull(timestamp);
    String jsonTimestamp = gson.toJson(timestamp, Timestamp.class);
    Timestamp deserializedTimestamp = gson.fromJson(jsonTimestamp, Timestamp.class);
    assertEquals(timestamp, deserializedTimestamp);

    java.sql.Date sqlDate = new java.sql.Date(System.currentTimeMillis());
    assertNotNull(sqlDate);
    String jsonSqlDate = gson.toJson(sqlDate, java.sql.Date.class);
    java.sql.Date deserializedSqlDate = gson.fromJson(jsonSqlDate, java.sql.Date.class);
    assertEquals(sqlDate, deserializedSqlDate);
  }

  @Test
  public void testSerializationAndDeserializationWithCustomDateFormat() throws ParseException {
    // Set up a custom DateFormat for testing
    SimpleDateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US);

    // Create a Gson builder with the custom DateFormat
    GsonBuilder gsonBuilder = new GsonBuilder();
    gsonBuilder.registerTypeAdapter(Date.class, new DefaultDateTypeAdapter(Date.class, customFormat, customFormat));
    Gson gson = gsonBuilder.create();

    // Serialize a Date object using the custom DateFormat
    Date date = new Date();
    String jsonDate = gson.toJson(date, Date.class);

    // Deserialize the JSON string back to a Date object using the custom DateFormat
    JsonParser parser = new JsonParser();
    JsonElement jsonDateElement = parser.parse(jsonDate);
    Date deserializedDate = gson.fromJson(jsonDateElement, Date.class);

    // Verify that the deserialized Date object is the same as the original one
    assertEquals(date, deserializedDate);
  }

  @Test
  public void testErrorHandling() {
    // Create an instance of DefaultDateTypeAdapter
    Gson gson = new Gson();
    TypeAdapter<Date> dateAdapter = gson.getAdapter(Date.class);

    // Test deserialization with a null input
    try {
      dateAdapter.read(null);
      fail("Expected JsonParseException");
    } catch (JsonParseException e) {
      // Expected
    }

    // Test deserialization with an empty string
    try {
      dateAdapter.read(new JsonReader(new StringReader("")));
      fail("Expected JsonParseException");
    } catch (JsonParseException e) {
      // Expected
    }

    // Test deserialization with an invalid date format
    try {
      dateAdapter.read(new JsonReader(new StringReader("2023-33-01T12:34:56")));
      fail("Expected JsonSyntaxException");
    } catch (JsonSyntaxException e) {
      // Expected
    }
  }
}