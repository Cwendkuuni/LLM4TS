package com.google.gson.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class DefaultDateTypeAdapterTest {

  @Test
  public void testDefaultDateTypeAdapter_DefaultConstructor() {
    TypeAdapter<Date> adapter = new DefaultDateTypeAdapter<>();
    assertEquals(DefaultDateTypeAdapter.class, adapter.getClass());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDefaultDateTypeAdapter_IllegalDateTypeConstructor() {
    new DefaultDateTypeAdapter<>(Object.class);
  }

  @Test
  public void testDefaultDateTypeAdapter_StringDatePatternConstructor() {
    TypeAdapter<Date> adapter = new DefaultDateTypeAdapter<>("yyyy-MM-dd HH:mm:ss");
    assertEquals(DefaultDateTypeAdapter.class, adapter.getClass());
  }

  @Test
  public void testDefaultDateTypeAdapter_DateStyleIntConstructor() {
    TypeAdapter<Date> adapter = new DefaultDateTypeAdapter<>(Date.class, Date.ABBREVIATED_FORMAT);
    assertEquals(DefaultDateTypeAdapter.class, adapter.getClass());
  }

  @Test
  public void testDefaultDateTypeAdapter_DateStyleIntTimeStyleConstructor() {
    TypeAdapter<Date> adapter = new DefaultDateTypeAdapter<>(Date.class, Date.ABBREVIATED_FORMAT, Date.SHORT);
    assertEquals(DefaultDateTypeAdapter.class, adapter.getClass());
  }

  @Test
  public void testDefaultDateTypeAdapter_DateStyleIntTimeStyleLocaleConstructor() {
    TypeAdapter<Date> adapter = new DefaultDateTypeAdapter<>(Date.class, Date.ABBREVIATED_FORMAT, Date.SHORT, Locale.US);
    assertEquals(DefaultDateTypeAdapter.class, adapter.getClass());
  }

  @Test
  public void testDefaultDateTypeAdapter_WriteDate() throws IOException {
    Gson gson = new Gson();
    TypeAdapter<Date> adapter = new DefaultDateTypeAdapter<>(Date.class);
    Date date = new Date();
    String json = gson.toJson(date, adapter);
    JsonElement jsonElement = JsonParser.parseString(json);
    JsonObject jsonObject = jsonElement.getAsJsonObject();
    assertEquals("2022-12-31 12:00:00", jsonObject.get("date").getAsString());
  }

  @Test
  public void testDefaultDateTypeAdapter_ReadNullDate() throws IOException {
    Gson gson = new Gson();
    TypeAdapter<Date> adapter = new DefaultDateTypeAdapter<>(Date.class);
    JsonReader jsonReader = new JsonReader(new StringReader("null"));
    assertNull(adapter.read(jsonReader));
  }

  @Test
  public void testDefaultDateTypeAdapter_ReadDate() throws IOException {
    Gson gson = new Gson();
    TypeAdapter<Date> adapter = new DefaultDateTypeAdapter<>(Date.class);
    JsonReader jsonReader = new JsonReader(new StringReader("\"2022-12-31 12:00:00\""));
    Date date = adapter.read(jsonReader);
    assertEquals(new Date(1672867200000L), date);
  }

  @Test(expected = JsonParseException.class)
  public void testDefaultDateTypeAdapter_ReadInvalidJsonString() throws IOException {
    Gson gson = new Gson();
    TypeAdapter<Date> adapter = new DefaultDateTypeAdapter<>(Date.class);
    JsonReader jsonReader = new JsonReader(new StringReader("\"invalidJsonString\""));
    adapter.read(jsonReader);
  }

  @Test(expected = JsonSyntaxException.class)
  public void testDefaultDateTypeAdapter_ReadInvalidDateFormat() throws IOException {
    Gson gson = new Gson();
    TypeAdapter<Date> adapter = new DefaultDateTypeAdapter<>(Date.class, "invalidFormat");
    JsonReader jsonReader = new JsonReader(new StringReader("\"2022-12-31 12:00:00\""));
    adapter.read(jsonReader);
  }

  @Test
  public void testDefaultDateTypeAdapter_ReadTimestamp() throws IOException, SQLException {
    Gson gson = new Gson();
    TypeAdapter<Date> adapter = new DefaultDateTypeAdapter<>(Timestamp.class);
    Timestamp timestamp = new Timestamp(1672867200000L);
    String json = gson.toJson(timestamp);
    JsonElement jsonElement = JsonParser.parseString(json);
    JsonObject jsonObject = jsonElement.getAsJsonObject();
    assertEquals("2022-12-31 12:00:00", jsonObject.get("date").getAsString());
    Date date = adapter.read(new JsonReader(new StringReader(json)));
    assertEquals(timestamp, new Timestamp(date.getTime()));
  }

  @Test(expected = JsonParseException.class)
  public void testDefaultDateTypeAdapter_ReadJsonNull() throws IOException {
    Gson gson = new Gson();
    TypeAdapter<Date> adapter = new DefaultDateTypeAdapter<>(Date.class);
    JsonReader jsonReader = new JsonReader(new StringReader(null));
    assertNull(adapter.read(jsonReader));
  }

  @Test(expected = JsonSyntaxException.class)
  public void testDefaultDateTypeAdapter_ReadInvalidJsonElement() throws IOException {
    Gson gson = new Gson();
    TypeAdapter<Date> adapter = new DefaultDateTypeAdapter<>(Date.class);
    JsonReader jsonReader = new JsonReader(new StringReader("1234"));
    adapter.read(jsonReader);
  }

  @Test(expected = JsonSyntaxException.class)
  public void testDefaultDateTypeAdapter_ReadInvalidJsonObject() throws IOException {
    Gson gson = new Gson();
    TypeAdapter<Date> adapter = new DefaultDateTypeAdapter<>(Date.class);
    JsonReader jsonReader = new JsonReader(new StringReader("{\"invalidJsonObject\":\"invalidJsonString\"}"));
    adapter.read(jsonReader);
  }

  @Test
  public void testDefaultDateTypeAdapter_ReadJavaSqlDate() throws IOException, SQLException {
    Gson gson = new Gson();
    TypeAdapter<Date> adapter = new DefaultDateTypeAdapter<>(java.sql.Date.class);
    java.sql.Date javaSqlDate = new java.sql.Date(1672867200000L);
    String json = gson.toJson(javaSqlDate);
    JsonElement jsonElement = JsonParser.parseString(json);
    JsonObject jsonObject = jsonElement.getAsJsonObject();
    assertEquals("2022-12-31", jsonObject.get("date").getAsString());
    Date date = adapter.read(new JsonReader(new StringReader(json)));
    assertEquals(javaSqlDate, new java.sql.Date(date.getTime()));
  }

  @Test(expected = JsonParseException.class)
  public void testDefaultDateTypeAdapter_ReadInvalidJsonStringJavaSqlDate() throws IOException {
    Gson gson = new Gson();
    TypeAdapter<Date> adapter = new DefaultDateTypeAdapter<>(java.sql.Date.class);
    JsonReader jsonReader = new JsonReader(new StringReader("\"invalidJsonString\""));
    adapter.read(jsonReader);
  }
}