import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.JsonReader;
import com.google.gson.JsonSyntaxException;

public class DefaultDateTypeAdapterTest {

 private DefaultDateTypeAdapter adapter;
 private JsonReader reader;
 private JsonWriter writer;

 @Before
 public void setup() {
 adapter = new DefaultDateTypeAdapter(Date.class, DateFormat.DEFAULT, DateFormat.DEFAULT);
 reader = mock(JsonReader.class);
 writer = mock(JsonWriter.class);
 }

 @Test
 public void testDefaultConstructor() {
 adapter = new DefaultDateTypeAdapter(Date.class);
 assertEquals(Date.class, adapter.dateType);
 }

 @Test
 public void testStringConstructor() {
 adapter = new DefaultDateTypeAdapter(Date.class, "dd-MM-yyyy HH:mm:ss z");
 assertEquals("dd-MM-yyyy HH:mm:ss z", adapter.localFormat.toPattern());
 }

 @Test
 public void testIntConstructor() {
 adapter = new DefaultDateTypeAdapter(DateFormat.ABBREVIATED, DateFormat.ABBREVIATED);
 assertEquals(DateFormat.ABBREVIATED, adapter.enUsFormat.getCalendar().getMinimum(Calendar.MONTH));
 }

 @Test
 public void testWritingNullValue() {
 adapter.write(writer, null);
 writer.flush();
 assertEquals("null", writer.toString());
 }

 @Test(expected = JsonParseException.class)
 public void testReadingNullValue() throws IOException {
 adapter.read(reader);
 }

 @Test
 public void testWritingAndReadingValidDate() throws IOException {
 Calendar calendar = Calendar.getInstance();
 Date date = new Date(calendar.getTimeInMillis());
 adapter.write(writer, date);
 writer.flush();
 String json = writer.toString();
 reader.setLenient(true);
 reader.setLenient(false); // reset after testing lenient parsing
 Date dateFromJson = adapter.read(reader);
 assertEquals(date, dateFromJson);
 }

 @Test(expected = JsonSyntaxException.class)
 public void testReadingInvalidFormat1() throws IOException {
 String invalidFormat1 = "InvalidFormat1";
 when(reader.nextString()).thenReturn(invalidFormat1);
 adapter.read(reader);
 }

 @Test(expected = JsonSyntaxException.class)
 public void testReadingInvalidFormat2() throws IOException {
 String invalidFormat2 = "InvalidFormat2";
 when(reader.nextString()).thenReturn(invalidFormat2);
 adapter.read(reader);
 }

 @Test(expected = JsonParseException.class)
 public void testReadingEmptyString() throws IOException {
 when(reader.nextString()).thenReturn("");
 adapter.read(reader);
 }

 @Test(expected = JsonParseException.class)
 public void testReadingMalformedString() throws IOException {
 String malformedString = "MalformedString";
 when(reader.nextString()).thenReturn(malformedString);
 adapter.read(reader);
 }

 @Test
 public void testReadingIsoFormatWithOffset() throws IOException, ParseException {
 String isoFormatWithOffset = "2015-12-31T23:59:59+01:00";
 when(reader.nextString()).thenReturn(isoFormatWithOffset);
 Date date = adapter.read(reader);
 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
 assertEquals(new Date(sdf.parse(isoFormatWithOffset).getTime() + 60 * 60 * 1000), date); // Add one hour for offset
 }
}