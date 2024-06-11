import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeFieldType;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.MutableDateTime;
import org.joda.time.MutableLocalDateTime;
import org.joda.time.ZoneId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class LenientDateTimeFieldTest {

 private static DateTimeFieldField testField = DateTimeFieldType.dayOfWeek();
 private static Chronology chrono = new org.joda.time.chrono.GregorianChronology();
 private MutableDateTime dateTime;
 private MutableLocalDateTime localDateTime;

 @Test
 public void testSetLenientDateTimeField() {
 DateTimeField lenientField = LenientDateTimeField.getInstance(testField, chrono);
 dateTime = new MutableDateTime();
 localDateTime = new MutableLocalDateTime();

 // Test setting valid value
 dateTime.set(2012, 1, 1, 0, 0, 0, 1);
 localDateTime.set(2012, 1, 1, 0, 0, 0, 1, 0);
 long value = lenientField.get(dateTime.getMillis());
 lenientField.set(dateTime.getMillis(), value + 1);
 assertEquals(value + 1, lenientField.get(dateTime.getMillis()));

 // Test setting out of bounds value
 dateTime.set(2012, 1, 1, 0, 0, 0, 1);
 lenientField.set(dateTime.getMillis(), value + 7);
 assertEquals(value + 1, lenientField.get(dateTime.getMillis()));

 // Test setting boundary value
 dateTime.set(2012, 1, 1, 0, 0, 0, 1);
 lenientField.set(dateTime.getMillis(), value);
 assertEquals(value, lenientField.get(dateTime.getMillis()));

 // Test setting negative value
 dateTime.set(2012, 1, 1, 0, 0, 0, 1);
 lenientField.set(dateTime.getMillis(), value - 1);
 assertEquals(value, lenientField.get(dateTime.getMillis()));

 // Test setting value in a local datetime
 localDateTime.set(2012, 1, 1, 1, 1, 1, 1, 1);
 long localValue = lenientField.get(localDateTime.toInstant().getEpochMilli());
 lenientField.set(localDateTime.toInstant().getEpochMilli(), localValue + 1);
 assertEquals(localValue + 1, lenientField.get(localDateTime.toInstant().getEpochMilli()));

 // Test setting value in a base datetime
 BaseDateTime baseDateTime = new BaseDateTime(2012, 1, 1, 0, 0, 0, 0);
 long baseValue = lenientField.get(baseDateTime.getMillis());
 lenientField.set(baseDateTime.getMillis(), baseValue + 1);
 assertEquals(baseValue + 1, lenientField.get(baseDateTime.getMillis()));

 // Test setting value in a null datetime
 assertNull(lenientField.set(null, 0));
 }

 @Test
 public void testIsLenient() {
 DateTimeField lenientField = LenientDateTimeField.getInstance(testField, chrono);
 DateTimeField strictField = testField;
 assertTrue(lenientField.isLenient());
 assertFalse(strictField.isLenient());
 }
}