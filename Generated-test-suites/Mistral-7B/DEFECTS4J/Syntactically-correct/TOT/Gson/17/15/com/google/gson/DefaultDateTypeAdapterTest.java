import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.bind.util.ISO8601Utils;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class DefaultDateTypeAdapterTest {

  private static final String SIMPLE_NAME = "DefaultDateTypeAdapter";

  @org.junit.Test
  public void testDefaultDateTypeAdapterWithUSDateFormat() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, adapter).create();

    // Test serialization with US DateFormat
    JsonWriter writer = new JsonWriter(System.out);
    Date date = new Date(1517328000000L); // Thu Nov 01 00:00:00 EDT 2018
    adapter.write(writer, date);
    writer.flush();

    String jsonOutput = "2018-11-01T04:00:00.000Z";
    JsonElement jsonElement = JsonParser.parseString(jsonOutput);
    Date deserializedDate = adapter.read(new JsonReader(new StringReader(jsonOutput.toString()))).getAsDate();
    assertEquals(date, deserializedDate);

    // Test deserialization with US DateFormat
    JsonElement jsonInput = JsonParser.parseString("\"2018-11-01T04:00:00.000Z\"");
    Date deserializedDateFromJson = adapter.read(new JsonReader(new StringReader(jsonInput.getAsString()))).getAsDate();
    assertEquals(date, deserializedDateFromJson);
  }

  @org.junit.Test
  public void testDefaultDateTypeAdapterWithLocalDateFormat() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, Locale.FRENCH);
    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, adapter).create();

    // Test serialization with Local DateFormat
    JsonWriter writer = new JsonWriter(System.out);
    Date date = new Date(1517328000000L); // Thu Nov 01 00:00:00 EDT 2018
    adapter.write(writer, date);
    writer.flush();

    String jsonOutput = "01-11-2018 04:00:00"; // French date format: day-month-year hour:minute:second
    JsonElement jsonElement = JsonParser.parseString(jsonOutput);
    Date deserializedDate = adapter.read(new JsonReader(new StringReader(jsonOutput.toString()))).getAsDate();
    assertEquals(date, deserializedDate);

    // Test deserialization with Local DateFormat
    JsonElement jsonInput = JsonParser.parseString("\"01-11-2018 04:00:00\"");
    Date deserializedDateFromJson = adapter.read(new JsonReader(new StringReader(jsonInput.getAsString()))).getAsDate();
    assertEquals(date, deserializedDateFromJson);
  }

  @org.junit.Test
  public void testDefaultDateTypeAdapterWithInvalidStringFormat() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, adapter).create();

    // Test serialization with an invalid string value
    JsonWriter writer = new JsonWriter(System.out);
    Date date = new Date(1517328000000L); // Thu Nov 01 00:00:00 EDT 2018
    String invalidStringValue = "abc";
    try {
      adapter.write(writer, date);
      adapter.write(writer, invalidStringValue);
      writer.flush();
    } catch (AssertionError e) {
      assertEquals("Unexpected value found when unmarshalling one of Date, java.sql.Date or Timestamp", e.getMessage());
      // Continue with tests
    }

    // Test deserialization with an invalid string value
    String invalidJsonString = "\"abc\"";
    try {
      adapter.read(new JsonReader(new StringReader(invalidJsonString)));
      fail("Should throw JsonSyntaxException");
    } catch (JsonSyntaxException e) {
      assertEquals("Expected a Date but was a java.lang.String with value [abc]", e.getMessage());
      // Continue with tests
    }
  }

  @org.junit.Test
  public void testDefaultDateTypeAdapterWithTimestamp() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Timestamp.class);
    Gson gson = new GsonBuilder().registerTypeAdapter(Timestamp.class, adapter).create();

    // Test serialization with Timestamp
    JsonWriter writer = new JsonWriter(System.out);
    Timestamp timestamp = new Timestamp(1517328000000L); // Thu Nov 01 00:00:00 EDT 2018
    adapter.write(writer, timestamp);
    writer.flush();

    String jsonOutput = "2018-11-01T04:00:00.000Z";
    JsonElement jsonElement = JsonParser.parseString(jsonOutput);
    Timestamp deserializedTimestamp = adapter.read(new JsonReader(new StringReader(jsonOutput.toString()))).getAsTimestamp();
    assertEquals(timestamp, deserializedTimestamp);

    // Test deserialization with Timestamp
    JsonElement jsonInput = JsonParser.parseString("\"2018-11-01T04:00:00.000Z\"");
    Timestamp deserializedTimestampFromJson = adapter.read(new JsonReader(new StringReader(jsonInput.getAsString()))).getAsTimestamp();
    assertEquals(timestamp, deserializedTimestampFromJson);
  }

  @org.junit.Test
  public void testDefaultDateTypeAdapterWithSQLDate() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(java.sql.Date.class);
    Gson gson = new GsonBuilder().registerTypeAdapter(java.sql.Date.class, adapter).create();

    // Test serialization with java.sql.Date
    JsonWriter writer = new JsonWriter(System.out);
    java.sql.Date sqlDate = new java.sql.Date(1517328000000L); // Thu Nov 01 00:00:00 EDT 2018
    adapter.write(writer, sqlDate);
    writer.flush();

    String jsonOutput = "2018-11-01";
    JsonElement jsonElement = JsonParser.parseString(jsonOutput);
    java.sql.Date deserializedSqlDate = adapter.read(new JsonReader(new StringReader(jsonOutput.toString()))).getAsJavaSqlDate();
    assertEquals(sqlDate, deserializedSqlDate);

    // Test deserialization with java.sql.Date
    JsonElement jsonInput = JsonParser.parseString("\"2018-11-01\"");
    java.sql.Date deserializedSqlDateFromJson = adapter.read(new JsonReader(new StringReader(jsonInput.getAsString()))).getAsJavaSqlDate();
    assertEquals(sqlDate, deserializedSqlDateFromJson);
  }

  private static class StringReader extends java.io.Reader {
    private final String string;

    public StringReader(String string) {
      this.string = string;
    }

    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {
      return string.substring(0, Math.min(len, string.length())).getBytes().length;
    }

    @Override
    public void close() throws IOException {
      // No-op
    }

    @Override
    public boolean ready() throws IOException {
      return true;
    }
  }
}