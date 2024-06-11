import org.joda.time.DurationFieldType;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class UnsupportedDurationFieldTest {

 private static final DurationFieldType DAYS = DurationFieldType.days();
 private static final DurationFieldType HALF_HOURS = DurationFieldType.halfHours();
 private static final DurationFieldType MINUTES = DurationFieldType.minutes();
 private static final DurationFieldType SECONDS = DurationFieldType.seconds();
 private static final DurationFieldType MILLISECONDS = DurationFieldType.millis();

 @Test
 public void testGetInstance() {
 UnsupportedDurationField daysField = UnsupportedDurationField.getInstance(DAYS);
 UnsupportedDurationField halfHoursField = UnsupportedDurationField.getInstance(HALF_HOURS);
 UnsupportedDurationField minutesField = UnsupportedDurationField.getInstance(MINUTES);
 UnsupportedDurationField secondsField = UnsupportedDurationField.getInstance(SECONDS);
 UnsupportedDurationField millisecondsField = UnsupportedDurationField.getInstance(MILLISECONDS);

 assertEquals(DAYS, daysField.getType());
 assertEquals(DAYS, halfHoursField.getType());
 assertEquals(DAYS, minutesField.getType());
 assertEquals(DAYS, secondsField.getType());
 assertEquals(DAYS, millisecondsField.getType());
 }

 @Test
 public void testIsSupported() {
 UnsupportedDurationField daysField = UnsupportedDurationField.getInstance(DAYS);
 UnsupportedDurationField halfHoursField = UnsupportedDurationField.getInstance(HALF_HOURS);
 UnsupportedDurationField minutesField = UnsupportedDurationField.getInstance(MINUTES);
 UnsupportedDurationField secondsField = UnsupportedDurationField.getInstance(SECONDS);
 UnsupportedDurationField millisecondsField = UnsupportedDurationField.getInstance(MILLISECONDS);

 assertEquals(false, daysField.isSupported());
 assertEquals(false, halfHoursField.isSupported());
 assertEquals(false, minutesField.isSupported());
 assertEquals(false, secondsField.isSupported());
 assertEquals(false, millisecondsField.isSupported());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValue() {
 UnsupportedDurationField daysField = UnsupportedDurationField.getInstance(DAYS);
 long duration = 1000;
 int expected = 3; // 1000 milliseconds is equivalent to 3 days

 assertEquals(expected, daysField.getValue(duration));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAsLong() {
 UnsupportedDurationField daysField = UnsupportedDurationField.getInstance(DAYS);
 long duration = 1000;
 long expected = 3L; // 1000 milliseconds is equivalent to 3 days

 assertEquals(expected, daysField.getValueAsLong(duration));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAtInstant() {
 UnsupportedDurationField daysField = UnsupportedDurationField.getInstance(DAYS);
 long duration = 1000;
 long instant = 1234567890123L;
 int expected = 3; // 1000 milliseconds is equivalent to 3 days

 assertEquals(expected, daysField.getValue(duration, instant));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAsLongAtInstant() {
 UnsupportedDurationField daysField = UnsupportedDurationField.getInstance(DAYS);
 long duration = 1000;
 long instant = 1234567890123L;
 long expected = 3L; // 1000 milliseconds is equivalent to 3 days

 assertEquals(expected, daysField.getValueAsLong(duration, instant));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillis() {
 UnsupportedDurationField daysField = UnsupportedDurationField.getInstance(DAYS);
 int value = 3;

 assertEquals(0, daysField.getMillis(value));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisAtInstant() {
 UnsupportedDurationField daysField = UnsupportedDurationField.getInstance(DAYS);
 long value = 3L;
 long instant = 1234567890123L;

 assertEquals(0, daysField.getMillis(value, instant));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAdd() {
 UnsupportedDurationField daysField = UnsupportedDurationField.getInstance(DAYS);
 long instant = 1234567890123L;
 int value = 3;

 daysField.add(instant, value);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddAtInstant() {
 UnsupportedDurationField daysField = UnsupportedDurationField.getInstance(DAYS);
 long instant = 1234567890123L;
 long value = 3L;

 daysField.add(instant, value);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetDifference() {
 UnsupportedDurationField daysField = UnsupportedDurationField.getInstance(DAYS);
 long minuendInstant = 1234567890123L;
 long subtrahendInstant = 1234567880123L;

 assertEquals(0, daysField.getDifference(minuendInstant, subtrahendInstant));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetDifferenceAsLong() {
 UnsupportedDurationField daysField = UnsupportedDurationField.getInstance(DAYS);
 long minuendInstant = 1234567890123L;
 long subtrahendInstant = 1234567880123L;

 assertEquals(0L, daysField.getDifferenceAsLong(minuendInstant, subtrahendInstant));
 }

 @Test
 public void testEquals() {
 UnsupportedDurationField daysField1 = UnsupportedDurationField.getInstance(DAYS);
 UnsupportedDurationField daysField2 = UnsupportedDurationField.getInstance(DAYS);
 UnsupportedDurationField minutesField = UnsupportedDurationField.getInstance(MINUTES);

 assertEquals(daysField1, daysField1);
 assertEquals(daysField1, daysField2);
 assertNotEquals(daysField1, minutesField);
 }

 @Test
 public void testHashCode() {
 UnsupportedDurationField daysField = UnsupportedDurationField.getInstance(DAYS);
 UnsupportedDurationField minutesField = UnsupportedDurationField.getInstance(MINUTES);

 assertEquals(daysField.hashCode(), daysField.hashCode());
 assertNotEquals(daysField.hashCode(), minutesField.hashCode());
 }

 @Test
 public void testToString() {
 UnsupportedDurationField daysField = UnsupportedDurationField.getInstance(DAYS);

 assertEquals("UnsupportedDurationField[days]", daysField.toString());
 }
}