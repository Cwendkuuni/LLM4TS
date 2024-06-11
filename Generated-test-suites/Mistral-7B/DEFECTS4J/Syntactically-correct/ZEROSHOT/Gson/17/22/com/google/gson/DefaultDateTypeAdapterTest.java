import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonReader;
import com.google.gson.JsonSyntaxException;
import com.google.gson.JsonWriter;
import com.google.gson.stream.JsonReader.CloseQuietly;
import com.google.gson.stream.JsonWriter.CloseQuietlyWriter;

public class DefaultDateTypeAdapterTest {

  private DefaultDateTypeAdapter dateAdapter;
  private DateFormat enUsFormatMock;
  private DateFormat localFormatMock;

  @Before
  public void setup() {
    enUsFormatMock = mock(DateFormat.class);
    localFormatMock = mock(DateFormat.class);
    when(enUsFormatMock.parse("2022-03-15 12:30:45")).thenReturn(new Date(1647155245000L));
    when(localFormatMock.parse("2022-03-15 12:30:45")).thenReturn(new Date(1647155245000L));
    when(enUsFormatMock.getCalendar(new GregorianCalendar(2022, 2, 15))).thenReturn(new GregorianCalendar(2022, 2, 15));
    when(localFormatMock.getCalendar(new GregorianCalendar(2022, 2, 15))).thenReturn(new GregorianCalendar(2022, 2, 15));

    dateAdapter = new DefaultDateTypeAdapter(Date.class, enUsFormatMock, localFormatMock);
  }

  @Test
  public void testDefaultConstructor() {
    DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter(Date.class);
    assertEquals(Date.class, dateAdapter.dateType);
    assertEquals(DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US), dateAdapter.enUsFormat);
    assertEquals(DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT), dateAdapter.localFormat);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorWithInvalidDateType() {
    new DefaultDateTypeAdapter(Integer.class, enUsFormatMock, localFormatMock);
  }

  @Test
  public void testWrite() throws IOException {
    JsonWriter jsonWriter = new JsonWriter(new CloseQuietlyWriter(System.out));
    Date date = new Date(1647155245000L);
    dateAdapter.write(jsonWriter, date);
    assertEquals("\"2022-03-15 12:30:45\"", jsonWriter.toString());
  }

  @Test
  public void testReadDate() throws IOException {
    JsonElement element = new JsonParser().parse("{\"date\": \"2022-03-15 12:30:45\"}");
    JsonReader jsonReader = new JsonReader(new JsonReader.CloseQuietly(element.getAsString()));
    Date date = dateAdapter.read(jsonReader);
    assertEquals(new Date(1647155245000L), date);
  }

  @Test
  public void testReadTimestamp() throws IOException {
    JsonElement element = new JsonParser().parse("{\"timestamp\": \"2022-03-15 12:30:45\"}");
    JsonReader jsonReader = new JsonReader(new JsonReader.CloseQuietly(element.getAsString()));
    Timestamp timestamp = dateAdapter.read(jsonReader);
    assertEquals(new Timestamp(1647155245000L), timestamp);
  }

  @Test
  public void testReadSqlDate() throws IOException {
    JsonElement element = new JsonParser().parse("{\"sqlDate\": \"2022-03-15\"}");
    JsonReader jsonReader = new JsonReader(new JsonReader.CloseQuietly(element.getAsString()));
    Date sqlDate = dateAdapter.read(jsonReader);
    assertEquals(new Date(1647110400000L), sqlDate); // 15th of March 2022
  }

  @Test(expected = JsonParseException.class)
  public void testReadInvalidFormat() throws IOException {
    JsonElement element = new JsonParser().parse("{\"invalidDate\": \"2022-03-15-12:30:45\"}");
    JsonReader jsonReader = new JsonReader(new JsonReader.CloseQuietly(element.getAsString()));
    dateAdapter.read(jsonReader);
  }

  @Test
  public void testReadNull() throws IOException {
    JsonElement element = new JsonParser().parse("{\"date\": null}");
    JsonReader jsonReader = new JsonReader(new JsonReader.CloseQuietly(element.getAsString()));
    assertNull(dateAdapter.read(jsonReader));
  }

  @Test(expected = JsonSyntaxException.class)
  public void testReadInvalidJson() throws IOException {
    JsonElement element = new JsonParser().parse("{\"invalidJson\": [\"2022-03-15 12:30:45\", 42]}");
    JsonReader jsonReader = new JsonReader(new JsonReader.CloseQuietly(element.getAsString()));
    dateAdapter.read(jsonReader);
  }
}