import com.google.gson.*;
import org.junit.Test;
import static org.junit.Assert.*;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DefaultDateTypeAdapterTest {

 @Test
 public void testDefaultDateTypeAdapter() {
 DefaultDateTypeAdapter dateTypeAdapter = new DefaultDateTypeAdapter(Date.class);
 assertEquals("DefaultDateTypeAdapter(com.google.gson.internal.java.util.Date)", dateTypeAdapter.toString());

 Date date = new Date();
 JsonWriter jsonWriter = new JsonWriter(System.out);
 dateTypeAdapter.write(jsonWriter, date);
 jsonWriter.flush();
 System.out.println(); // Print the JSON string
 assertJsonEquals("{\"date\":\"Sun Jan 23 10:52:03 EST 2023\"}", System.out.getBuffer().toString());

 JsonReader jsonReader = new JsonReader(new StringReader(System.out.getBuffer().toString()));
 Date deserializedDate = dateTypeAdapter.read(jsonReader);
 assertEquals(date, deserializedDate);
 }

 @Test
 public void testDefaultDateTypeAdapterWithUSFormat() {
 SimpleDateFormat usFormat = new SimpleDateFormat("MM/dd/yyyy h:mm a z");
 DefaultDateTypeAdapter dateTypeAdapter = new DefaultDateTypeAdapter(Date.class, usFormat);
 assertEquals("DefaultDateTypeAdapter(com.google.gson.internal.java.util.Date, " + usFormat.getClass().getSimpleName() + ")", dateTypeAdapter.toString());

 Date date = new Date();
 JsonWriter jsonWriter = new JsonWriter(System.out);
 dateTypeAdapter.write(jsonWriter, date);
 jsonWriter.flush();
 System.out.println(); // Print the JSON string
 assertJsonEquals("{\"date\":\"01/23/2023 10:52 AM EST\"}", System.out.getBuffer().toString());

 JsonReader jsonReader = new JsonReader(new StringReader(System.out.getBuffer().toString()));
 Date deserializedDate = dateTypeAdapter.read(jsonReader);
 assertEquals(date, deserializedDate);
 }

 @Test
 public void testDefaultDateTypeAdapterWithLocalFormat() {
 Locale locale = Locale.ENGLISH;
 SimpleDateFormat localFormat = new SimpleDateFormat("dd/MM/yyyy h:mm a", locale);
 DefaultDateTypeAdapter dateTypeAdapter = new DefaultDateTypeAdapter(Date.class, localFormat);
 assertEquals("DefaultDateTypeAdapter(com.google.gson.internal.java.util.Date, " + localFormat.getClass().getSimpleName() + ")", dateTypeAdapter.toString());

 Date date = new Date();
 JsonWriter jsonWriter = new JsonWriter(System.out);
 dateTypeAdapter.write(jsonWriter, date);
 jsonWriter.flush();
 System.out.println(); // Print the JSON string
 assertJsonEquals("{\"date\":\"23/01/2023 10:52 AM\"}", System.out.getBuffer().toString());

 JsonReader jsonReader = new JsonReader(new StringReader(System.out.getBuffer().toString()));
 Date deserializedDate = dateTypeAdapter.read(jsonReader);
 assertEquals(date, deserializedDate);
 }

 @Test
 public void testDefaultDateTypeAdapterWithTimestamp() {
 DefaultDateTypeAdapter dateTypeAdapter = new DefaultDateTypeAdapter(Timestamp.class);
 assertEquals("DefaultDateTypeAdapter(class java.sql.Timestamp)", dateTypeAdapter.toString());

 Date date = new Date();
 Timestamp timestamp = new Timestamp(date.getTime());
 JsonWriter jsonWriter = new JsonWriter(System.out);
 dateTypeAdapter.write(jsonWriter, timestamp);
 jsonWriter.flush();
 System.out.println(); // Print the JSON string
 assertJsonEquals("{\"timestamp\":\"2023-01-23 10:52:03.000\"}", System.out.getBuffer().toString());

 JsonReader jsonReader = new JsonReader(new StringReader(System.out.getBuffer().toString()));
 Timestamp deserializedTimestamp = (Timestamp) dateTypeAdapter.read(jsonReader);
 assertEquals(timestamp, deserializedTimestamp);
 }

 @Test
 public void testDefaultDateTypeAdapterWithSQLDate() {
 DefaultDateTypeAdapter dateTypeAdapter = new DefaultDateTypeAdapter(java.sql.Date.class);
 assertEquals("DefaultDateTypeAdapter(class java.sql.Date)", dateTypeAdapter.toString());

 Date date = new Date();
 java.sql.Date sqlDate = new java.sql.Date(date.getTime());
 JsonWriter jsonWriter = new JsonWriter(System.out);
 dateTypeAdapter.write(jsonWriter, sqlDate);
 jsonWriter.flush();
 System.out.println(); // Print the JSON string
 assertJsonEquals("{\"date\":\"2023-01-23\"}", System.out.getBuffer().toString());

 JsonReader jsonReader = new JsonReader(new StringReader(System.out.getBuffer().toString()));
 java.sql.Date deserializedSqlDate = (java.sql.Date) dateTypeAdapter.read(jsonReader);
 assertEquals(sqlDate, deserializedSqlDate);
 }

 @Test(expected = JsonParseException.class)
 public void testDefaultDateTypeAdapterWithInvalidDateFormat() {
 DefaultDateTypeAdapter dateTypeAdapter = new DefaultDateTypeAdapter(Date.class, new SimpleDateFormat("dd/MM/yyyy-MM-dd"));
 assertEquals("DefaultDateTypeAdapter(com.google.gson.internal.java.util.Date, " + SimpleDateFormat.class.getSimpleName() + ")", dateTypeAdapter.toString());

 JsonReader jsonReader = new JsonReader(new StringReader("{\"date\":\"23/01/2023-01-01\"}"));
 dateTypeAdapter.read(jsonReader);
 }

 @Test(expected = JsonSyntaxException.class)
 public void testDefaultDateTypeAdapterWithInvalidDateString() {
 DefaultDateTypeAdapter dateTypeAdapter = new DefaultDateTypeAdapter(Date.class);
 assertEquals("DefaultDateTypeAdapter(com.google.gson.internal.java.util.Date)", dateTypeAdapter.toString());

 JsonReader jsonReader = new JsonReader(new StringReader("{\"date\":\"invalid_date_string\"}"));
 dateTypeAdapter.read(jsonReader);
 }

 private void assertJsonEquals(String jsonString, String buffer) {
 assertEquals(jsonString, new String(buffer));
 }

}