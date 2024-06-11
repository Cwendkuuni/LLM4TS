package com.google.gson.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class DefaultDateTypeAdapterTest {

  @org.junit.Test
  public void testDefaultDateTypeAdapter_Date_DefaultConstructor() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter();

    // Test writing a Date to a JsonWriter
    Gson gson = new Gson();
    JsonWriter writer = new JsonWriter(System.out);
    gson.toJson(new Date(), adapter, writer);
    writer.flush();

    // Test reading a Date from a JsonReader
    JsonReader reader = new JsonReader(new StringReader("\"2022-01-01 12:00:00\""));
    Date date = adapter.read(reader);
    assertEquals(new Date(1641344000000L), date);

    // Test reading a null Date from a JsonReader
    reader = new JsonReader(new StringReader("null"));
    date = adapter.read(reader);
    assertNull(date);

    // Test reading an invalid date from a JsonReader
    reader = new JsonReader(new StringReader("\"2022-02-31 12:00:00\""));
    assertThrows(JsonSyntaxException.class, () -> adapter.read(reader));
  }

  @org.junit.Test
  public void testDefaultDateTypeAdapter_Date_StringConstructor() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter("yyyy-MM-dd HH:mm:ss");

    // Test writing a Date to a JsonWriter
    Gson gson = new Gson();
    JsonWriter writer = new JsonWriter(System.out);
    gson.toJson(new Date(1641344000000L), adapter, writer);
    writer.flush();

    // Test reading a Date from a JsonReader
    JsonReader reader = new JsonReader(new StringReader("\"2022-01-01 12:00:00\""));
    Date date = adapter.read(reader);
    assertEquals(new Date(1641344000000L), date);

    // Test reading a null Date from a JsonReader
    reader = new JsonReader(new StringReader("null"));
    date = adapter.read(reader);
    assertNull(date);

    // Test reading an invalid date from a JsonReader
    reader = new JsonReader(new StringReader("\"2022-02-31 12:00:00\""));
    assertThrows(JsonSyntaxException.class, () -> adapter.read(reader));
  }

  @org.junit.Test
  public void testDefaultDateTypeAdapter_Date_IntConstructor() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, Calendar.DATE, Calendar.MINUTE);

    // Test writing a Date to a JsonWriter
    Gson gson = new Gson();
    JsonWriter writer = new JsonWriter(System.out);
    gson.toJson(new Date(1641344000000L), adapter, writer);
    writer.flush();

    // Test reading a Date from a JsonReader
    JsonReader reader = new JsonReader(new StringReader("\"2022-01-01 12:00:00\""));
    Date date = adapter.read(reader);
    assertEquals(new Date(1641344000000L), date);

    // Test reading a null Date from a JsonReader
    reader = new JsonReader(new StringReader("null"));
    date = adapter.read(reader);
    assertNull(date);

    // Test reading an invalid date from a JsonReader
    reader = new JsonReader(new StringReader("\"2022-02-31 12:00:00\""));
    assertThrows(JsonSyntaxException.class, () -> adapter.read(reader));
  }

  @org.junit.Test
  public void testDefaultDateTypeAdapter_Timestamp_DefaultConstructor() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Timestamp.class);

    // Test writing a Timestamp to a JsonWriter
    Gson gson = new Gson();
    JsonWriter writer = new JsonWriter(System.out);
    gson.toJson(new Timestamp(1641344000000L), adapter, writer);
    writer.flush();

    // Test reading a Timestamp from a JsonReader
    JsonReader reader = new JsonReader(new StringReader("\"2022-01-01 12:00:00.123\""));
    Timestamp timestamp = adapter.read(reader);
    assertEquals(new Timestamp(1641344000123L), timestamp);

    // Test reading a null Timestamp from a JsonReader
    reader = new JsonReader(new StringReader("null"));
    timestamp = adapter.read(reader);
    assertNull(timestamp);

    // Test reading an invalid date from a JsonReader
    reader = new JsonReader(new StringReader("\"2022-02-31 12:00:00.123\""));
    assertThrows(JsonSyntaxException.class, () -> adapter.read(reader));
  }

  @org.junit.Test
  public void testDefaultDateTypeAdapter_JavaSqlDate_DefaultConstructor() throws SQLException {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(java.sql.Date.class);

    // Test writing a java.sql.Date to a JsonWriter
    Gson gson = new Gson();
    JsonWriter writer = new JsonWriter(System.out);
    java.sql.Date javaSqlDate = new java.sql.Date(1641344000000L);
    gson.toJson(javaSqlDate, adapter, writer);
    writer.flush();

    // Test reading a java.sql.Date from a JsonReader
    JsonReader reader = new JsonReader(new StringReader("\"2022-01-01\""));
    java.sql.Date javaSqlDateRead = adapter.read(reader);
    assertEquals(javaSqlDate, javaSqlDateRead);

    // Test reading a null java.sql.Date from a JsonReader
    reader = new JsonReader(new StringReader("null"));
    javaSqlDateRead = adapter.read(reader);
    assertNull(javaSqlDateRead);

    // Test reading an invalid date from a JsonReader
    reader = new JsonReader(new StringReader("\"2022-02-31\""));
    assertThrows(JsonSyntaxException.class, () -> adapter.read(reader));
  }

  @org.junit.Test
  public void testParseExceptions() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);

    // Test parse exception when parsing an invalid date using localFormat
    JsonReader reader = new JsonReader(new StringReader("\"2022-03-32 12:00:00\""));
    assertThrows(ParseException.class, () -> adapter.deserializationContext.handleParseFailure(adapter, reader, null));

    // Test parse exception when parsing an invalid date using enUsFormat
    reader = new JsonReader(new StringReader("\"2022-32-01 12:00:00\""));
    assertThrows(ParseException.class, () -> adapter.deserializationContext.handleParseFailure(adapter, reader, null));

    // Test parse exception when parsing an invalid date using ISO8601Utils
    reader = new JsonReader(new StringReader("\"2022-01-01Z23:59:59.123\""));
    assertThrows(JsonSyntaxException.class, () -> adapter.read(reader));
  }
}