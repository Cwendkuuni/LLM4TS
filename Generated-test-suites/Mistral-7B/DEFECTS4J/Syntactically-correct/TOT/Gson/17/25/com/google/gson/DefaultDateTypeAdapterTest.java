package com.google.gson.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.sql.SQLException;
import java.text.ParseException;
import java.text. SimpleDateFormat;
import java.text.ParsePosition;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

import junit.framework.Assert;

public class DefaultDateTypeAdapterTest {

 @Test
 public void testDefaultDateTypeAdapter() {
 TypeAdapter<Date> dateAdapter = new DefaultDateTypeAdapter(Date.class);

 // Test serialization
 Date date = new Date();
 String jsonString = new Gson().toJson(date);
 JsonElement jsonElement = JsonParser.parseString(jsonString);
 Date deserializedDate = dateAdapter.read(new JsonReader(new StringReader(jsonString))).getAsDate();
 assertEquals(date, deserializedDate);

 // Test serialization with custom format
 DateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z");
 customFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
 DefaultDateTypeAdapter customAdapter = new DefaultDateTypeAdapter(Date.class, customFormat, customFormat);
 jsonString = new Gson().toJson(date, new TypeToken<Date>() {}.getType());
 jsonElement = JsonParser.parseString(jsonString);
 deserializedDate = customAdapter.read(new JsonReader(new StringReader(jsonString))).getAsDate();
 assertEquals(date, deserializedDate);

 // Test deserialization with null value
 assertNull(dateAdapter.read(new JsonReader(new StringReader("null"))));

 // Test deserialization with invalid date format
 try {
 dateAdapter.read(new JsonReader(new StringReader("InvalidDateFormat"))).getAsDate();
 Assert.fail();
 } catch (JsonParseException e) {
 // Expected
 }

 // Test deserialization with empty string
 assertNull(dateAdapter.read(new JsonReader(new StringReader(""))));

 // Test deserialization with empty string and null value
 assertNull(dateAdapter.read(new JsonReader(new StringReader("null"))).isJsonNull());
 assertNull(dateAdapter.read(new JsonReader(new StringReader(""))).isJsonNull());

 // Test deserialization with valid dates
 Date validDate1 = new Date(System.currentTimeMillis() - 60000);
 Date validDate2 = new Date();
 Date validDate3 = new Date(System.currentTimeMillis() + 60000);
 String validDateJsonString1 = new Gson().toJson(validDate1);
 String validDateJsonString2 = new Gson().toJson(validDate2);
 String validDateJsonString3 = new Gson().toJson(validDate3);
 Date deserializedValidDate1 = dateAdapter.read(new JsonReader(new StringReader(validDateJsonString1))).getAsDate();
 Date deserializedValidDate2 = dateAdapter.read(new JsonReader(new StringReader(validDateJsonString2))).getAsDate();
 Date deserializedValidDate3 = dateAdapter.read(new JsonReader(new StringReader(validDateJsonString3))).getAsDate();
 assertEquals(validDate1, deserializedValidDate1);
 assertEquals(validDate2, deserializedValidDate2);
 assertEquals(validDate3, deserializedValidDate3);

 // Test deserialization with valid dates and custom format
 DateFormat customFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");
 DefaultDateTypeAdapter customAdapter2 = new DefaultDateTypeAdapter(Date.class, customFormat2, customFormat2);
 String validDateJsonString1Custom = new Gson().toJson(validDate1, new TypeToken<Date>() {}.getType());
 Date deserializedValidDate1Custom = customAdapter2.read(new JsonReader(new StringReader(validDateJsonString1Custom))).getAsDate();
 assertEquals(validDate1, deserializedValidDate1Custom);

 // Test deserialization with valid Timestamp
 Date validTimestamp = new Date(System.currentTimeMillis() - 2 * 60000);
 Timestamp validTimestampObject = new Timestamp(validTimestamp.getTime());
 String validTimestampJsonString = new Gson().toJson(validTimestampObject);
 Date deserializedValidTimestamp = (Date) dateAdapter.read(new JsonReader(new StringReader(validTimestampJsonString))).getAsDate();
 assertEquals(validTimestamp, new Date(deserializedValidTimestamp.getTime()));

 // Test deserialization with valid sql.Date
 java.sql.Date validSqlDate = new java.sql.Date(System.currentTimeMillis() - 3 * 60000);
 String validSqlDateJsonString = new Gson().toJson(validSqlDate);
 Date deserializedValidSqlDate = dateAdapter.read(new JsonReader(new StringReader(validSqlDateJsonString))).getAsDate();
 assertEquals(validSqlDate, new java.sql.Date(deserializedValidSqlDate.getTime()));
 }

 @Test
 public void testDefaultDateTypeAdapter_WithInvalidDateType() {
 try {
 new DefaultDateTypeAdapter((Class<? extends Date>) Integer.class, enUsFormat, localFormat);
 Assert.fail();
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }
}