package com.google.gson.tests;

import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class DefaultDateTypeAdapterTest {

  @Test
  public void testDefaultDateTypeAdapter_DefaultConstructor() {
    TypeAdapter<Date> adapter = new DefaultDateTypeAdapter<>();

    Date date = new Date();
    JsonElement jsonElement = new Gson().toJsonTree(date, adapter.getType());
    Date deserializedDate = adapter.read(new JsonReader(jsonElement.getAsReader()));
    assertEquals(date, deserializedDate);
  }

  @Test
  public void testDefaultDateTypeAdapter_StringDatePatternConstructor() {
    TypeAdapter<Date> adapter = new DefaultDateTypeAdapter("dd-MM-yyyy");

    Date date = new Date();
    String dateString = new SimpleDateFormat("dd-MM-yyyy", Locale.US).format(date);

    JsonElement jsonElement = new Gson().toJsonTree(dateString, adapter.getType());
    Date deserializedDate = adapter.read(new JsonReader(jsonElement.getAsReader()));
    assertEquals(date, deserializedDate);
  }

  @Test
  public void testDefaultDateTypeAdapter_IntDateStyleAndIntTimeStyleConstructor() {
    TypeAdapter<Date> adapter = new DefaultDateTypeAdapter(DateFormat.ABBREV_MONTH, DateFormat.SHORT);

    Date date = new Date();
    JsonElement jsonElement = new Gson().toJsonTree(date, adapter.getType());
    Date deserializedDate = adapter.read(new JsonReader(jsonElement.getAsReader()));
    assertEquals(date, deserializedDate);
  }

  @Test(expected = JsonParseException.class)
  public void testDefaultDateTypeAdapter_InvalidJsonString() {
    TypeAdapter<Date> adapter = new DefaultDateTypeAdapter<>();
    JsonReader jsonReader = new JsonReader(new StringReader("{ \"invalid_json\": 1 }"));
    adapter.read(jsonReader);
  }

  @Test(expected = JsonSyntaxException.class)
  public void testDefaultDateTypeAdapter_InvalidDateString() {
    TypeAdapter<Date> adapter = new DefaultDateTypeAdapter<>();
    JsonReader jsonReader = new JsonReader(new StringReader("invalid_date_string"));
    adapter.read(jsonReader);
  }

  @Test(expected = JsonParseException.class)
  public void testDefaultDateTypeAdapter_NullJsonString() {
    TypeAdapter<Date> adapter = new DefaultDateTypeAdapter<>();
    JsonReader jsonReader = new JsonReader(new StringReader(null));
    adapter.read(jsonReader);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDefaultDateTypeAdapter_DateTypeMismatch() {
    TypeAdapter<Long> adapter = new DefaultDateTypeAdapter(Integer.class);
  }

  @Test
  public void testDefaultDateTypeAdapter_WriteDate() throws IOException {
    TypeAdapter<Date> adapter = new DefaultDateTypeAdapter<>();
    Date date = new Date();

    String jsonString = "";
    JsonWriter jsonWriter = new JsonWriter(new StringWriter(jsonString));
    adapter.write(jsonWriter, date);
    jsonWriter.flush();

    JsonElement jsonElement = new JsonParser().parse(jsonString);
    Date deserializedDate = adapter.read(new JsonReader(jsonElement.getAsReader()));
    assertEquals(date, deserializedDate);
  }

  @Test(expected = IOException.class)
  public void testDefaultDateTypeAdapter_IOException() throws IOException {
    TypeAdapter<Date> adapter = new DefaultDateTypeAdapter<>();
    JsonWriter jsonWriter = new JsonWriter(new StringWriter(""));
    adapter.write(jsonWriter, new Date());
  }

  @Test(expected = SQLException.class)
  public void testDefaultDateTypeAdapter_SQLException() throws IOException, SQLException {
    TypeAdapter<Date> adapter = new DefaultDateTypeAdapter<>();
    JsonWriter jsonWriter = new JsonWriter(new StringWriter(""));
    adapter.write(jsonWriter, new Timestamp(System.currentTimeMillis()));
  }
}