import org.junit.Assert;
import org.junit.Test;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class StdDateFormatTest {

 private static final StdDateFormat STD_DATE_FORMAT = StdDateFormat.instance;

 @Test
 public void testParseValidISO8601Date() throws ParseException {
 Date date = STD_DATE_FORMAT.parse("2022-01-01T12:30:45.123+01:00");
 Assert.assertNotNull(date);
 }

 @Test
 public void testParseValidRFC1123Date() throws ParseException {
 Date date = STD_DATE_FORMAT.parse("Wed, 31 Dec 2034 11:59:59 GMT");
 Assert.assertNotNull(date);
 }

 @Test
 public void testParseValidPlainDate() throws ParseException {
 Date date = STD_DATE_FORMAT.parse("2022-12-31");
 Assert.assertNotNull(date);
 }

 @Test(expected = ParseException.class)
 public void testParseInvalidDate() throws ParseException {
 STD_DATE_FORMAT.parse("abc");
 }

 @Test
 public void testParseISO8601DateWithMissingTimezone() throws ParseException {
 Date date = STD_DATE_FORMAT.parse("2022-01-01T12:30:45.123");
 Assert.assertNotNull(date);
 }

 @Test
 public void testParseRFC1123DateWithMissingTimezone() throws ParseException {
 Date date = STD_DATE_FORMAT.parse("Tue, 31 Dec 2034 11:59:59");
 Assert.assertNotNull(date);
 }

 @Test
 public void testParsePlainDateWithMissingTimezone() throws ParseException {
 Date date = STD_DATE_FORMAT.parse("2022-12-31");
 Assert.assertNotNull(date);
 }

 @Test
 public void testParseISO8601DateWithInvalidTimezone() throws ParseException {
 TimeZone invalidTimezone = new TimeZone("InvalidTimezone") {
 @Override
 public int getRawOffset() {
 throw new UnsupportedOperationException();
 }
 };
 StdDateFormat dateFormatWithInvalidTimezone = new StdDateFormat(invalidTimezone, Locale.US);
 Date date = dateFormatWithInvalidTimezone.parse("2022-01-01T12:30:45.123+01:00");
 Assert.assertNull(date);
 }

 @Test
 public void testParseRFC1123DateWithInvalidTimezone() throws ParseException {
 TimeZone invalidTimezone = new TimeZone("InvalidTimezone") {
 @Override
 public int getRawOffset() {
 throw new UnsupportedOperationException();
 }
 };
 StdDateFormat dateFormatWithInvalidTimezone = new StdDateFormat(invalidTimezone, Locale.US);
 Date date = dateFormatWithInvalidTimezone.parse("Wed, 31 Dec 2034 11:59:59 GMT");
 Assert.assertNull(date);
 }

 @Test
 public void testParsePlainDateWithInvalidTimezone() throws ParseException {
 TimeZone invalidTimezone = new TimeZone("InvalidTimezone") {
 @Override
 public int getRawOffset() {
 throw new UnsupportedOperationException();
 }
 };
 StdDateFormat dateFormatWithInvalidTimezone = new StdDateFormat(invalidTimezone, Locale.US);
 Date date = dateFormatWithInvalidTimezone.parse("2022-12-31");
 Assert.assertNull(date);
 }

 @Test
 public void testFormatValidDate() {
 Date date = new Date(1668680000000L);
 String formattedDate = STD_DATE_FORMAT.format(date, new StringBuffer(), new FieldPosition(0, 0));
 Assert.assertEquals("Sun, 31 Dec 2022 12:00:00 GMT", formattedDate.toString());
 }

 @Test
 public void testFormatISODate() {
 Date date = new Date(1668680000000L);
 String formattedDate = STD_DATE_FORMAT.format(date, new StringBuffer(), new FieldPosition(0, 0));
 String formattedISODate = STD_DATE_FORMAT.format(date, null);
 Assert.assertEquals("2022-12-31T12:00:00.000Z", formattedISODate);
 }

 @Test
 public void testFormatRFC1123Date() {
 Date date = new Date(1668680000000L);
 String formattedDate = STD_DATE_FORMAT.format(date, new StringBuffer(), new FieldPosition(0, 0));
 Assert.assertEquals("Sun, 31 Dec 2022 12:00:00 GMT", formattedDate.toString());
 }

 @Test
 public void testFormatPlainDate() {
 Date date = new Date(1668680000000L);
 String formattedDate = STD_DATE_FORMAT.format(date, new StringBuffer(), new FieldPosition(0, 0));
 String formattedPlainDate = STD_DATE_FORMAT.format(date);
 Assert.assertEquals("31 Dec 2022", formattedPlainDate);
 }
}