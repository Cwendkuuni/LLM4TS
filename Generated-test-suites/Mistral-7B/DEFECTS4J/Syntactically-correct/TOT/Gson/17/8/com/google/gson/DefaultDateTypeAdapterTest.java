package com.google.gson.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
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
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class DefaultDateTypeAdapterTest {

  private static final String ISO_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

  @Test
  public void testDefaultDateTypeAdapter_USFormat() throws IOException, ParseException, SQLException {
    TypeAdapter<Date> dateTypeAdapter = new DefaultDateTypeAdapter(Date.class);

    Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy hh:mm a z").create();

    String jsonDate = "25/12/2021 12:00 PM EST";
    JsonElement jsonElement = new JsonParser().parse(jsonDate);
    Date date = dateTypeAdapter.read(new JsonReader(jsonElement.getAsStream()));
    assertEquals("Wed Dec 25 12:00:00 EST 2021", dateTypeAdapter.toString().toString());
    assertEquals("25-Dec-2021 12:00:00 PM", dateFormatToString(date));

    JsonWriter jsonWriter = new JsonWriter(System.out);
    dateTypeAdapter.write(jsonWriter, date);
    jsonWriter.flush();
  }

  @Test
  public void testDefaultDateTypeAdapter_ISOFormat() throws IOException, ParseException, SQLException {
    TypeAdapter<Date> dateTypeAdapter = new DefaultDateTypeAdapter(Date.class, "yyyy-MM-dd'T'HH:mm:ss'Z'");

    Gson gson = new GsonBuilder().create();

    String jsonDate = "2021-12-25T12:00:00Z";
    JsonElement jsonElement = new JsonParser().parse(jsonDate);
    Date date = dateTypeAdapter.read(new JsonReader(jsonElement.getAsStream()));
    assertEquals("Sat Dec 25 12:00:00 UTC 2021", dateTypeAdapter.toString().toString());
    assertEquals("2021-12-25T12:00:00Z", jsonDateFromDateFormat(date));

    JsonWriter jsonWriter = new JsonWriter(System.out);
    dateTypeAdapter.write(jsonWriter, date);
    jsonWriter.flush();
  }

  @Test
  public void testDefaultDateTypeAdapter_NullValue() throws IOException {
    TypeAdapter<Date> dateTypeAdapter = new DefaultDateTypeAdapter(Date.class);

    String jsonNull = "null";
    JsonElement jsonElement = new JsonParser().parse(jsonNull);

    Date date = dateTypeAdapter.read(new JsonReader(jsonElement.getAsStream()));
    assertNull(date);
  }

  @Test
  public void testDefaultDateTypeAdapter_InvalidFormat() throws IOException, ParseException {
    TypeAdapter<Date> dateTypeAdapter = new DefaultDateTypeAdapter(Date.class);

    String jsonDate = "InvalidDateFormat";
    JsonElement jsonElement = new JsonParser().parse(jsonDate);

    try {
      Date date = dateTypeAdapter.read(new JsonReader(jsonElement.getAsStream()));
      fail("Expected JsonParseException");
    } catch (JsonParseException e) {
      // Expected
    }
  }

  private String jsonDateFromDateFormat(Date date) {
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    return format.format(date);
  }

  private String dateFormatToString(Date date) {
    SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy hh:mm a z", Locale.US);
    return format.format(date);
  }
}