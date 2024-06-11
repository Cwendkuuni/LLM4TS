import org.joda.time.Chronology;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeFieldType;
import org.joda.time.LocalDateTime;
import org.joda.time.MutableDateTime;
import org.joda.time.DateTimeField;
import org.joda.time.format.DateTimeFormat;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class LenientDateTimeFieldTest {

 private Chronology isoChronology;
 private DateTimeField dateField;
 private LenientDateTimeField lenientField;

 @Before
 public void setUp() {
 isoChronology = DateTimeUtils.getISOLocalDateInstance();
 dateField = DateTimeFieldType.dayOfMonth();
 lenientField = LenientDateTimeField.getInstance(dateField, isoChronology);
 }

 @Test
 public void testLenientSetValidValue() {
 MutableDateTime dateTime = new MutableDateTime(2022, DateTimeConstants.DECEMBER, 31, 23, 59, 59, 999);
 long expected = 2022 - 2000L + DateTimeConstants.DAYS_PER_YEAR * 11 + DateTimeConstants.DAYS_PER_MONTH * 12 + DateTimeConstants.DAYS_PER_MONTH * 11 + 31;
 long actual = lenientField.set(dateTime.getLong(), 1);
 assertEquals(expected, actual);
 }

 @Test
 public void testLenientSetOutOfBoundsValue() {
 MutableDateTime dateTime = new MutableDateTime(2022, DateTimeConstants.DECEMBER, 31, 23, 59, 59, 999);
 long actual = lenientField.set(dateTime.getLong(), DateTimeConstants.DAYS_PER_YEAR * 13);
 long expected = 2023 - 2000L + DateTimeConstants.DAYS_PER_YEAR * 1 + DateTimeConstants.DAYS_PER_MONTH * 1 + 1;
 assertEquals(expected, actual);
 }

 @Test
 public void testLenientSetNegativeValue() {
 MutableDateTime dateTime = new MutableDateTime(2022, DateTimeConstants.DECEMBER, 31, 23, 59, 59, 999);
 long actual = lenientField.set(dateTime.getLong(), -1);
 long expected = 2022 - 2000L + DateTimeConstants.DAYS_PER_YEAR * 11 + DateTimeConstants.DAYS_PER_MONTH * 12 + DateTimeConstants.DAYS_PER_MONTH * 11 + 30;
 assertEquals(expected, actual);
 }

 @Test
 public void testLenientSetValueWithWrongFieldType() {
 MutableDateTime dateTime = new MutableDateTime(2022, DateTimeConstants.DECEMBER, 31, 23, 59, 59, 999);
 DateTimeField monthField = DateTimeFieldType.monthOfYear();
 LenientDateTimeField lenientMonthField = LenientDateTimeField.getInstance(monthField, isoChronology);
 long actual = lenientMonthField.set(dateTime.getLong(), 32);
 long expected = 2023 - 2000L + DateTimeConstants.DAYS_PER_YEAR * 1 + DateTimeConstants.DAYS_PER_MONTH * 1 + 1;
 assertEquals(expected, actual);
 }

 @Test
 public void testLenientGetValue() {
 MutableDateTime dateTime = new MutableDateTime(2022, DateTimeConstants.DECEMBER, 31, 23, 59, 59, 999);
 lenientField.set(dateTime.getLong(), 1);
 int expected = 1;
 int actual = lenientField.get(dateTime.getLong());
 assertEquals(expected, actual);
 }

 @Test
 public void testLenientGetValueWithWrongFieldType() {
 MutableDateTime dateTime = new MutableDateTime(2022, DateTimeConstants.DECEMBER, 31, 23, 59, 59, 999);
 lenientField.set(dateTime.getLong(), 1);
 DateTimeField monthField = DateTimeFieldType.monthOfYear();
 LenientDateTimeField lenientMonthField = new LenientDateTimeField(monthField, isoChronology);
 int actual = lenientMonthField.get(dateTime.getLong());
 int expected = 12;
 assertEquals(expected, actual);
 }
}