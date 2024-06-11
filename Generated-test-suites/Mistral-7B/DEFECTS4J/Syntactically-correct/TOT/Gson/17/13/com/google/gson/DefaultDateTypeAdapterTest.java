package com.google.gson.tests;

import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.junit.Test;

import com.google.gson.ExcludedMember;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

public class DefaultDateTypeAdapterTest {

  @Test
  public void testDefaultDateTypeAdapter_defaultConstructor() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter();
    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, adapter).create();

    Date date = new Date(1633059200000L); // Mon Jan 24 12:00:00 UTC 2022
    String jsonDate = "\"2022-01-24T12:00:00Z\"";

    // Serialize
    JsonElement serializedDate = gson.toJsonTree(date);
    assertEquals(jsonDate, serializedDate.getAsString());

    // Deserialize
    Date deserializedDate = gson.fromJson(jsonDate, Date.class);
    assertEquals(date, deserializedDate);

    // Test with other date formats
    DefaultDateTypeAdapter adapterWithUsFormat = new DefaultDateTypeAdapter("US");
    Gson gsonWithUsFormat = new GsonBuilder().registerTypeAdapter(Date.class, adapterWithUsFormat).create();
    JsonElement serializedDateUsFormat = gsonWithUsFormat.toJsonTree(date);
    assertEquals(jsonDate, serializedDateUsFormat.getAsString());
    Date deserializedDateUsFormat = gsonWithUsFormat.fromJson(jsonDate, Date.class);
    assertEquals(date, deserializedDateUsFormat);
  }

  @Test
  public void testDefaultDateTypeAdapter_dateStyle() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, Calendar.SHORT, Calendar.SHORT);
    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, adapter).create();

    Date date = new Date(1633059200000L); // Mon Jan 24 12:00:00 UTC 2022
    String jsonDate = "\"01/24/2022\",\"12:00:00 PM\"";

    // Serialize
    JsonElement serializedDate = gson.toJsonTree(date);
    assertEquals(jsonDate, serializedDate.getAsJsonArray().get(0).getAsString());

    // Deserialize
    Date deserializedDate = gson.fromJson(jsonDate, Date[].class)[0];
    assertEquals(date, deserializedDate);
  }

  @Test
  public void testDefaultDateTypeAdapter_timestamp() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Timestamp.class);
    Gson gson = new GsonBuilder().registerTypeAdapter(Timestamp.class, adapter).create();

    Date date = new Date(1633059200000L); // Mon Jan 24 12:00:00 UTC 2022
    Timestamp timestamp = new Timestamp(date.getTime());
    String jsonTimestamp = "\"2022-01-24T12:00:00.000Z\"";

    // Serialize
    JsonElement serializedTimestamp = gson.toJsonTree(timestamp);
    assertEquals(jsonTimestamp, serializedTimestamp.getAsString());

    // Deserialize
    Timestamp deserializedTimestamp = gson.fromJson(jsonTimestamp, Timestamp.class);
    assertEquals(timestamp, deserializedTimestamp);
  }

  @Test(expected = JsonIOException.class)
  public void testDefaultDateTypeAdapter_deserialization_null() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter();
    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, adapter).create();

    String jsonNull = "null";
    gson.fromJson(jsonNull, Date.class);
  }

  @Test(expected = JsonSyntaxException.class)
  public void testDefaultDateTypeAdapter_deserialization_invalidFormat() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter();
    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, adapter).create();

    String jsonInvalidFormat = "\"Invalid Date\"";
    gson.fromJson(jsonInvalidFormat, Date.class);
  }

  @Test(expected = JsonParseException.class)
  public void testDefaultDateTypeAdapter_deserialization_parseException() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter();
    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, adapter).create();

    String jsonParseException = "\"Invalid date: Invalid format: \"Invalid Date\"\"";
    gson.fromJson(jsonParseException, Date.class);
  }

  @Test
  public void testDefaultDateTypeAdapter_dateStyle_deserialization_excludeField() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, Calendar.SHORT, Calendar.SHORT);
    Gson gson = new GsonBuilder()
        .registerTypeAdapter(Date.class, adapter)
        .excludeFieldsWithoutExposeAnnotation()
        .create();

    Date date = new Date(1633059200000L); // Mon Jan 24 12:00:00 UTC 2022
    String jsonDate = "{\"notDateField\":\"value\"}";

    // Deserialize
    Date deserializedDate = gson.fromJson(jsonDate, Date.class);
    assertNull(deserializedDate);
  }
}