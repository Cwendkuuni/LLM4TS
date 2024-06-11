package com.google.gson.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.junit.Test;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;

public class DefaultDateTypeAdapterTest {

  @Test
  public void testDefaultDateTypeAdapter() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);

    // Test serialization and deserialization with valid dates
    Date date = new Date();
    String jsonDate = new Gson().toJsonTree(date, adapter).getAsJson().get("date").getAsString();
    Date deserializedDate = new Gson().fromJson(jsonDate, Date.class);
    assertEquals(date, deserializedDate);

    // Test serialization and deserialization with valid Timestamps
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    jsonDate = new Gson().toJsonTree(timestamp, adapter).getAsJson().get("date").getAsString();
    Timestamp deserializedTimestamp = new Gson().fromJson(jsonDate, Timestamp.class);
    assertEquals(timestamp, deserializedTimestamp);

    // Test serialization and deserialization with valid java.sql.Dates
    java.sql.Date sqlDate = new java.sql.Date(System.currentTimeMillis());
    jsonDate = new Gson().toJsonTree(sqlDate, adapter).getAsJson().get("date").getAsString();
    java.sql.Date deserializedSqlDate = new Gson().fromJson(jsonDate, java.sql.Date.class);
    assertEquals(sqlDate, deserializedSqlDate);

    // Test serialization and deserialization with null values
    assertNull(adapter.read(new JsonReader(new StringReader("null"))));
    assertNull(new Gson().fromJson(new JsonElement(), adapter));

    // Test serialization and deserialization with invalid dates (empty string and invalid format)
    assertNull(adapter.read(new JsonReader(new StringReader(""))));
    assertNull(adapter.read(new JsonReader(new StringReader("invalidDate"))));

    // Test serialization and deserialization with invalid dates (invalid format in different time zones)
    SimpleDateFormat invalidFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss zzz");
    TimeZone invalidTimeZone = TimeZone.getTimeZone("GMT+12:00");
    invalidFormat.setTimeZone(invalidTimeZone);
    String jsonInvalidDate = invalidFormat.format(new Date());
    assertNull(adapter.read(new JsonReader(new StringReader(jsonInvalidDate))));

    // Test serialization and deserialization with invalid Timestamps (empty string and invalid format)
    assertNull(adapter.read(new JsonReader(new StringReader(""))));
    assertNull(adapter.read(new JsonReader(new StringReader("invalidTimestamp"))));

    // Test serialization and deserialization with invalid java.sql.Dates (empty string and invalid format)
    assertNull(adapter.read(new JsonReader(new StringReader(""))));
    assertNull(adapter.read(new JsonReader(new StringReader("invalidSqlDate"))));

    // Test serialization and deserialization with Timestamps with different timezones
    TimeZone utcTimeZone = TimeZone.getTimeZone("UTC");
    TimeZone easternTimeZone = TimeZone.getTimeZone("America/New_York");
    SimpleDateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    SimpleDateFormat easternFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'-05:00'");
    Timestamp utcTimestamp = new Timestamp(System.currentTimeMillis());
    String jsonUtcTimestamp = utcFormat.format(utcTimestamp);
    String jsonEasternTimestamp = easternFormat.format(utcTimestamp);
    assertEquals(utcTimestamp, new Gson().fromJson(jsonUtcTimestamp, Timestamp.class));
    assertEquals(new Timestamp(utcTimestamp.getTime() - (60 * 60 * 1000)), new Gson().fromJson(jsonEasternTimestamp, Timestamp.class));

    // Test serialization and deserialization with java.sql.Dates with different timezones
    java.sql.Date utcSqlDate = new java.sql.Date(System.currentTimeMillis());
    SimpleDateFormat utcSqldateFormat = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat easternSqldateFormat = new SimpleDateFormat("yyyy-MM-dd-05");
    String jsonUtcSqlDate = utcSqldateFormat.format(utcSqlDate);
    String jsonEasternSqlDate = easternSqldateFormat.format(utcSqlDate);
    assertEquals(utcSqlDate, new Gson().fromJson(jsonUtcSqlDate, java.sql.Date.class));
    assertEquals(new java.sql.Date(utcSqlDate.getTime() - (60 * 60 * 24 * 30 * 12 * 1000)), new Gson().fromJson(jsonEasternSqlDate, java.sql.Date.class));
  }

  // Test method to read a String as JsonElement
  private JsonElement stringToJsonElement(String jsonString) {
    return JsonParser.parseString(jsonString).getAsJson();
  }

  // Test method to read a String as InputStream
  private InputStream StringToInputStream(String jsonString) throws IOException {
    return new ByteArrayInputStream(jsonString.getBytes("UTF-8"));
  }
}