import com.google.gson.DefaultDateTypeAdapter;
import com.google.gson.DateTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import static org.junit.Assert.*;

public class DefaultDateTypeAdapterTest {

  private static final String TEST_DATE_STRING = "2022-01-01";
  private static final String TEST_TIMESTAMP_STRING = "2022-01-01T12:34:56Z";
  private static final String TEST_SQL_DATE_STRING = "2022-01-01";

  @org.junit.Before
  public void setUp() {
  }

  @org.junit.Test
  public void testDefaultDateTypeAdapterConstructionWithDateClass() {
    DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter(Date.class);
    assertEquals(Date.class, dateAdapter.dateType);
  }

  @org.junit.Test
  public void testDefaultDateTypeAdapterConstructionWithDatePattern() {
    DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter(Date.class, "yyyy-MM-dd");
    assertEquals(Date.class, dateAdapter.dateType);
  }

  @org.junit.Test
  public void testDefaultDateTypeAdapterConstructionWithDateStyle() {
    DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter(Date.class, DateFormat.DATE_FIELD);
    assertEquals(Date.class, dateAdapter.dateType);
  }

  @org.junit.Test
  public void testDefaultDateTypeAdapterConstructionWithDateStyleAndTimeStyle() {
    DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter(Date.class, DateFormat.DATE_FIELD, DateFormat.TIME_FIELD);
    assertEquals(Date.class, dateAdapter.dateType);
  }

  @org.junit.Test
  public void testDefaultDateTypeAdapterConstructionWithTimestampClass() {
    DefaultDateTypeAdapter timestampAdapter = new DefaultDateTypeAdapter(Timestamp.class);
    assertEquals(Timestamp.class, timestampAdapter.dateType);
  }

  @org.junit.Test
  public void testDefaultDateTypeAdapterConstructionWithJavaSQLDateClass() {
    DefaultDateTypeAdapter sqlDateAdapter = new DefaultDateTypeAdapter(java.sql.Date.class);
    assertEquals(java.sql.Date.class, sqlDateAdapter.dateType);
  }

  @org.junit.Test
  public void testDefaultDateTypeAdapterWriteDate() throws IOException {
    DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter(Date.class);

    JsonWriter jsonWriter = new JsonWriter(System.out);
    Date date = new Date();
    dateAdapter.write(jsonWriter, date);
    jsonWriter.flush();

    assertEquals("2022-01-01T00:00:00.000Z", jsonWriter.toString().trim());
  }

  @org.junit.Test
  public void testDefaultDateTypeAdapterWriteTimestamp() throws IOException {
    DefaultDateTypeAdapter timestampAdapter = new DefaultDateTypeAdapter(Timestamp.class);

    JsonWriter jsonWriter = new JsonWriter(System.out);
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    timestampAdapter.write(jsonWriter, timestamp);
    jsonWriter.flush();

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    assertEquals(dateFormat.format(timestamp), jsonWriter.toString().trim());
  }

  @org.junit.Test
  public void testDefaultDateTypeAdapterWriteSQLDate() throws IOException {
    DefaultDateTypeAdapter sqlDateAdapter = new DefaultDateTypeAdapter(java.sql.Date.class);

    JsonWriter jsonWriter = new JsonWriter(System.out);
    java.sql.Date sqlDate = new java.sql.Date(System.currentTimeMillis());
    sqlDateAdapter.write(jsonWriter, sqlDate);
    jsonWriter.flush();

    assertEquals(TEST_SQL_DATE_STRING, jsonWriter.toString().trim());
  }

  @org.junit.Test
  public void testDefaultDateTypeAdapterReadDate() throws IOException {
    DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter(Date.class);
    Gson gson = new Gson();
    JsonElement jsonElement = JsonParser.parseString(TEST_DATE_STRING);
    Date date = dateAdapter.read(gson.newJsonReader(jsonElement.getAsString()));
    assertEquals("Sat Jan 01 00:00:00 CET 2022", date.toString());
  }

  @org.junit.Test
  public void testDefaultDateTypeAdapterReadTimestamp() throws IOException {
    DefaultDateTypeAdapter timestampAdapter = new DefaultDateTypeAdapter(Timestamp.class);
    Gson gson = new Gson();
    JsonElement jsonElement = JsonParser.parseString(TEST_TIMESTAMP_STRING);
    Timestamp timestamp = timestampAdapter.read(gson.newJsonReader(jsonElement.getAsString()));
    Calendar calendar = GregorianCalendar.getInstance();
    calendar.setTime(timestamp);
    assertEquals(2022, calendar.get(Calendar.YEAR));
    assertEquals(1, calendar.get(Calendar.MONTH) + 1);
    assertEquals(1, calendar.get(Calendar.DAY_OF_MONTH));
    assertEquals(12, calendar.get(Calendar.HOUR_OF_DAY));
    assertEquals(34, calendar.get(Calendar.MINUTE));
    assertEquals(56, calendar.get(Calendar.SECOND));
  }

  @org.junit.Test
  public void testDefaultDateTypeAdapterReadSQLDate() throws IOException {
    DefaultDateTypeAdapter sqlDateAdapter = new DefaultDateTypeAdapter(java.sql.Date.class);
    Gson gson = new Gson();
    JsonElement jsonElement = JsonParser.parseString(TEST_SQL_DATE_STRING);
    java.sql.Date sqlDate = sqlDateAdapter.read(gson.newJsonReader(jsonElement.getAsString()));
    assertEquals("SQL Date: 2022-01-01", sqlDate.toString());
  }

  @org.junit.Test(expected = JsonSyntaxException.class)
  public void testDefaultDateTypeAdapterReadInvalidDate() throws IOException {
    DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter(Date.class);
    Gson gson = new Gson();
    JsonReader jsonReader = gson.newJsonReader("'invalid'");
    dateAdapter.read(jsonReader);
  }

  @org.junit.Test(expected = JsonSyntaxException.class)
  public void testDefaultDateTypeAdapterReadInvalidTimestamp() throws IOException {
    DefaultDateTypeAdapter timestampAdapter = new DefaultDateTypeAdapter(Timestamp.class);
    Gson gson = new Gson();
    JsonReader jsonReader = gson.newJsonReader("'invalid timestamp'");
    timestampAdapter.read(jsonReader);
  }

  @org.junit.Test(expected = JsonSyntaxException.class)
  public void testDefaultDateTypeAdapterReadInvalidSQLDate() throws IOException {
    DefaultDateTypeAdapter sqlDateAdapter = new DefaultDateTypeAdapter(java.sql.Date.class);
    Gson gson = new Gson();
    JsonReader jsonReader = gson.newJsonReader("'invalid sql date'");
    sqlDateAdapter.read(jsonReader);
  }
}