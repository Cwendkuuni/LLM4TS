package com.google.gson.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.StringReader;
import java.io.StringWriter;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class DefaultDateTypeAdapterTest {

  private DefaultDateTypeAdapter dateAdapter;
  private Gson gson;

  @Before
  public void setup() {
    dateAdapter = new DefaultDateTypeAdapter(java.sql.Date.class, DateFormat.LONG, DateFormat.LONG);
    gson = new GsonBuilder().registerTypeAdapter(java.sql.Date.class, dateAdapter).create();
  }

  @Test
  public void testSerializationAndDeserializationOfValidDate() {
    Date date = new Date();
    String json = gson.toJson(date);
    Date deserializedDate = gson.fromJson(json, java.sql.Date.class);
    assertEquals(date, deserializedDate);
  }

  @Test
  public void testSerializationAndDeserializationOfValidTimestamp() {
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    String json = gson.toJson(timestamp);
    Timestamp deserializedTimestamp = gson.fromJson(json, Timestamp.class);
    assertEquals(timestamp, deserializedTimestamp);
  }

  @Test
  public void testSerializationAndDeserializationOfNullDate() {
    String json = gson.toJson(null);
    JsonElement jsonElement = new JsonReader(new StringReader(json)).parse();
    assertNull(gson.fromJson(jsonElement, java.sql.Date.class));
  }

  @Test(expected = JsonParseException.class)
  public void testDeserializationOfInvalidString() {
    gson.fromJson("invalidString", java.sql.Date.class);
  }

  @Test(expected = JsonSyntaxException.class)
  public void testDeserializationOfStringWithInvalidFormat() {
    String json = "2022-33-01"; // Invalid format
    gson.fromJson(new StringReader(json), java.sql.Date.class);
  }

  @Test(expected = JsonIOException.class)
  public void testDeserializationOfEmptyJson() throws IOException {
    JsonReader jsonReader = new JsonReader(new StringReader(""));
    gson.fromJson(jsonReader, java.sql.Date.class);
  }

  @Test(expected = JsonParseException.class)
  public void testDeserializationOfInvalidJsonObject() throws SQLException, ParseException {
    String json = "{\"invalidObject\": \"invalidString\"}";
    JsonReader jsonReader = new JsonReader(new StringReader(json));
    jsonReader.beginObject();
    jsonReader.nextName();
    jsonReader.nextString();
    jsonReader.endObject();
    gson.fromJson(jsonReader, java.sql.Date.class);
  }

  @Test(expected = AssertionError.class)
  public void testDeserializationOfInvalidType() throws SQLException, ParseException {
    String json = "{\"invalidObject\": 123}";
    JsonReader jsonReader = new JsonReader(new StringReader(json));
    jsonReader.beginObject();
    jsonReader.nextName();
    jsonReader.nextInt();
    jsonReader.endObject();
    gson.fromJson(jsonReader, java.sql.Date.class);
  }

  @Test
  public void testToString() {
    String expectedToString = "DefaultDateTypeAdapter(java.sql.DateFormat[length=37, className=class com.google.gson.internal.bind.util.ISO8601Utils, simple name=ISO8601Utils])";
    assertEquals(expectedToString, dateAdapter.toString());
  }
}