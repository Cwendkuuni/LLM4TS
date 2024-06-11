import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.google.gson.*;
import java.io.StringReader;
import java.io.StringWriter;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DefaultDateTypeAdapterTest {

 private TypeAdapter<Date> adapter;

 @Before
 public void setup() {
 // Test with default constructor
 adapter = new DefaultDateTypeAdapter(Date.class);
 // Test with US date pattern
 adapter = new DefaultDateTypeAdapter("dd-MM-yyyy HH:mm:ss", Date.class);
 // Test with specific date styles
 adapter = new DefaultDateTypeAdapter(Calendar.SHORT, Calendar.SHORT, Date.class);
 }

 @Test
 public void testWrite() throws IOException {
 // Test writing a null value
 StringWriter sw = new StringWriter();
 JsonWriter jw = new JsonWriter(sw);
 adapter.write(jw, null);
 jw.flush();
 String json = sw.toString();
 assertEquals("null", json);

 // Test writing a valid date
 Date date = new Date();
 StringWriter sw2 = new StringWriter();
 JsonWriter jw2 = new JsonWriter(sw2);
 adapter.write(jw2, date);
 jw2.flush();
 String json2 = sw2.toString();
 assertEquals("\"Thu Aug 04 13:45:35 EDT 2022\"", json2);
 }

 @Test
 public void testRead() throws IOException {
 // Test reading a null value
 String jsonNull = "null";
 JsonReader jr = new JsonReader(new StringReader(jsonNull));
 Date result = adapter.read(jr);
 assertNull(result);

 // Test reading a valid date
 String jsonDate = "\"Thu Aug 04 13:45:35 EDT 2022\"";
 JsonReader jr2 = new JsonReader(new StringReader(jsonDate));
 Date date = adapter.read(jr2);
 assertEquals(new Date(), date);

 // Test reading a valid date with specific date types
 Date sqlDate = new java.sql.Date(System.currentTimeMillis());
 adapter = new DefaultDateTypeAdapter(java.sql.Date.class);
 Date resultSqlDate = adapter.read(jr2);
 assertEquals(sqlDate, resultSqlDate);

 Timestamp timestamp = new Timestamp(System.currentTimeMillis());
 adapter = new DefaultDateTypeAdapter(Timestamp.class);
 Date resultTimestamp = adapter.read(jr2);
 assertEquals(timestamp, new Timestamp(resultTimestamp.getTime()));
 }

 @Test(expected = JsonParseException.class)
 public void testReadInvalidFormat() throws IOException {
 String jsonInvalid = "Thu Aug 04 13:45:35 EDT 2022"; // missing quotes
 JsonReader jr3 = new JsonReader(new StringReader(jsonInvalid));
 adapter.read(jr3);
 }

 @Test(expected = JsonParseException.class)
 public void testReadInvalidDate() throws IOException {
 String jsonInvalidDate = "Thu Aug 04 13:45:35 EDT 2022X";
 JsonReader jr4 = new JsonReader(new StringReader(jsonInvalidDate));
 adapter.read(jr4);
 }

 @Test(expected = AssertionError.class)
 public void testReadInvalidType() throws IOException {
 String jsonInvalidType = "42";
 JsonReader jr5 = new JsonReader(new StringReader(jsonInvalidType));
 adapter.read(jr5);
 }
}