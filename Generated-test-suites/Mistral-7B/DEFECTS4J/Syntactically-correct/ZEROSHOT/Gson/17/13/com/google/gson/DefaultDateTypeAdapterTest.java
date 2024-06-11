import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonReader;
import com.google.gson.JsonSyntaxException;
import com.google.gson.JsonWriter;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonParser.NumberType;

public class DefaultDateTypeAdapterTest {

  private DefaultDateTypeAdapter adapter;
  private SimpleDateFormat simpleDateFormat;
  private DateFormat dateFormat;

  @Before
  public void setup() {
    simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    dateFormat = mock(DateFormat.class);
    adapter = new DefaultDateTypeAdapter(Date.class, dateFormat, simpleDateFormat);
  }

  @Test
  public void testWrite() throws IOException {
    JsonWriter jsonWriter = mock(JsonWriter.class);
    when(jsonWriter.value(String.class)).thenCallRealMethod();

    Date date = new Date();
    adapter.write(jsonWriter, date);

    assertEquals("2022-12-31T23:59:59.000Z", jsonWriter.getJsonWriter().peek());
  }

  @Test
  public void testWriteTimestamp() throws IOException {
    JsonWriter jsonWriter = mock(JsonWriter.class);
    when(jsonWriter.value(String.class)).thenCallRealMethod();

    Timestamp timestamp = new Timestamp(1652386635000L);
    adapter.write(jsonWriter, timestamp);

    assertEquals("2022-12-31T18:10:35.000Z", jsonWriter.getJsonWriter().peek());
  }

  @Test
  public void testWriteSqlDate() throws IOException {
    JsonWriter jsonWriter = mock(JsonWriter.class);
    when(jsonWriter.value(String.class)).thenCallRealMethod();

    java.sql.Date sqlDate = new java.sql.Date(1652386635000L);
    adapter.write(jsonWriter, sqlDate);

    assertEquals("2022-12-31", jsonWriter.getJsonWriter().peek());
  }

  @Test
  public void testReadDate() throws IOException {
    String jsonString = "\"2022-12-31T23:59:59.000Z\"";
    JsonReader jsonReader = new JsonReader(new StringReader(jsonString));

    Date date = adapter.read(jsonReader);

    assertEquals(new Date(1672492799000L), date);
  }

  @Test(expected = JsonParseException.class)
  public void testReadInvalidDateFormat() throws IOException {
    String jsonString = "\"InvalidDateFormat\"";
    JsonReader jsonReader = new JsonReader(new StringReader(jsonString));

    adapter.read(jsonReader);
  }

  @Test(expected = JsonSyntaxException.class)
  public void testReadDateParseException() throws IOException {
    String jsonString = "\"Invalid Date\"";
    JsonReader jsonReader = new JsonReader(new StringReader(jsonString));

    adapter.read(jsonReader);
  }

  @Test
  public void testReadTimestamp() throws IOException {
    String jsonString = "\"2022-12-31T18:10:35.000Z\"";
    JsonReader jsonReader = new JsonReader(new StringReader(jsonString));

    Timestamp timestamp = adapter.read(jsonReader);

    assertEquals(new Timestamp(1652386635000L), timestamp);
  }

  @Test
  public void testReadSqlDate() throws IOException {
    String jsonString = "\"2022-12-31\"";
    JsonReader jsonReader = new JsonReader(new StringReader(jsonString));

    java.sql.Date sqlDate = adapter.read(jsonReader);

    assertEquals(new java.sql.Date(1672492799000L), sqlDate);
  }

  @Test
  public void testNull() throws IOException {
    JsonReader jsonReader = mock(JsonReader.class);
    when(jsonReader.peek()).thenReturn(JsonToken.NULL);

    assertNull(adapter.read(jsonReader));
  }

  @Test
  public void testReadInvalidJson() throws IOException {
    String jsonString = "{\"InvalidJson\":\"Value\"}";
    JsonReader jsonReader = new JsonReader(new StringReader(jsonString));

    assertFalse(adapter.read(jsonReader) instanceof Date);
  }

  @Test
  public void testReadJsonWithNumber() throws IOException {
    String jsonString = "{\"date\":1672492799000}";
    JsonReader jsonReader = new JsonReader(new StringReader(jsonString));

    when(jsonReader.nextInt()).thenReturn(1672492799000);
    when(jsonReader.nextNull()).thenCallRealMethod();

    JsonElement jsonElement = new Gson().fromJson(jsonString, JsonElement.class);
    NumberType numberType = JsonParser.parseNumberType(jsonElement.getAsJsonPrimitive().getAsString());

    assertSame(JsonParser.NumberType.INT, numberType);

    Date date = adapter.read(jsonReader);

    assertEquals(new Date(1672492799000L), date);
  }
}