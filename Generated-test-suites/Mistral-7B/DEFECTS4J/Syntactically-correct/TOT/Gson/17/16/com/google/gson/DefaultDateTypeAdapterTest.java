package com.google.gson.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import com.google.gson.internal.bind.util.ISO8601Utils;
import com.google.gson.internal.bind.util.ToStringStyle;
import com.google.gson.stream.JsonReader.CloseQuietly;

/**
 * Test class for DefaultDateTypeAdapter.
 *
 * @author john.doe
 */
public class DefaultDateTypeAdapterTest {

  @Test
  public void testDefaultDateTypeAdapter() {
    // Test constructor with Date as dateType
    DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter(Date.class);
    assertSame(Date.class, dateAdapter.dateType);

    // Test write method with US English format
    Date date = new Date();
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    JsonWriter writer = gson.newJsonWriter(System.out);
    dateAdapter.write(writer, date);
    writer.close();

    // Test write method with custom format
    SimpleDateFormat customDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", Locale.US);
    DefaultDateTypeAdapter customDateAdapter = new DefaultDateTypeAdapter(Date.class, customDateFormat, customDateFormat);
    Gson gsonCustom = new GsonBuilder().setPrettyPrinting().create();
    JsonWriter writerCustom = gsonCustom.newJsonWriter(System.out);
    customDateAdapter.write(writerCustom, date);
    writerCustom.close();

    // Test read method with US English format
    JsonElement jsonElement = new JsonParser().parse("\"2022-01-01T00:00:00Z\"");
    Date readDate = dateAdapter.read(new JsonReader(new CloseQuietly(jsonElement.getAsJsonPrimitive().getAsString().openStream())));
    assertEquals(new Date(1641344000000L), readDate);

    // Test read method with custom format
    SimpleDateFormat customDateFormatReader = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", Locale.US);
    JsonElement jsonElementCustom = new JsonParser().parse("\"2022-01-01T00:00:00Z\"");
    Date readDateCustom = customDateAdapter.read(new JsonReader(new CloseQuietly(jsonElementCustom.getAsJsonPrimitive().getAsString().openStream())));
    assertEquals(new Date(1641344000000L), readDateCustom);

    // Test read method with null value
    JsonElement jsonElementNull = new JsonParser().parse("null");
    assertNull(dateAdapter.read(new JsonReader(new CloseQuietly(jsonElementNull.getAsJsonElement().getAsStream()))));

    // Test read method with invalid date format
    String invalidDateFormat = "InvalidDateFormat";
    JsonElement jsonElementInvalidFormat = new JsonParser().parse(invalidDateFormat);
    try {
      dateAdapter.read(new JsonReader(new CloseQuietly(jsonElementInvalidFormat.getAsJsonPrimitive().openStream())));
    } catch (JsonSyntaxException e) {
      assertEquals(invalidDateFormat, e.getMessage());
    }

    // Test read method with empty string
    String emptyString = "";
    JsonElement jsonElementEmptyString = new JsonParser().parse(emptyString);
    assertNull(dateAdapter.read(new JsonReader(new CloseQuietly(jsonElementEmptyString.getAsJsonPrimitive().openStream()))));

    // Test read method with ISO 8601 format
    String isoDateString = "2022-01-01T00:00:00.000Z";
    Date readIsoDate = dateAdapter.read(new JsonReader(new CloseQuietly(new JsonParser().parse(isoDateString).getAsJsonPrimitive().openStream())));
    assertEquals(new Date(1641344000000L), readIsoDate);

    // Test read method with Timestamp type
    TypeAdapter<Timestamp> timestampAdapter = new DefaultDateTypeAdapter(Timestamp.class);
    Timestamp timestamp = new Timestamp(1641344000000L);
    assertSame(Timestamp.class, timestampAdapter.dateType);
    JsonElement jsonElementTimestamp = new JsonParser().parse("\"2022-01-01T00:00:00.000Z\"");
    Timestamp readTimestamp = timestampAdapter.read(new JsonReader(new CloseQuietly(jsonElementTimestamp.getAsJsonPrimitive().getAsString().openStream())));
    assertEquals(timestamp, readTimestamp);

    // Test read method with java.sql.Date type
    TypeAdapter<java.sql.Date> sqlDateAdapter = new DefaultDateTypeAdapter(java.sql.Date.class);
    java.sql.Date sqlDate = new java.sql.Date(1641344000000L);
    assertSame(java.sql.Date.class, sqlDateAdapter.dateType);
    JsonElement jsonElementSqlDate = new JsonParser().parse("\"2022-01-01\"");
    java.sql.Date readSqlDate = sqlDateAdapter.read(new JsonReader(new CloseQuietly(jsonElementSqlDate.getAsJsonPrimitive().getAsString().openStream())));
    assertEquals(sqlDate, readSqlDate);
  }
}