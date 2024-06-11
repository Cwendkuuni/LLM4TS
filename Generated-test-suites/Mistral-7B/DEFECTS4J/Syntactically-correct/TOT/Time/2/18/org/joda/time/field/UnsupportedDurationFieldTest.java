import org.junit.Test;
import org.joda.time.DurationFieldType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class UnsupportedDurationFieldTest {

 private static final DurationFieldType DAYS = DurationFieldType.days();
 private static final DurationFieldType HALF_HOURS = DurationFieldType.halfHours();
 private static final DurationFieldType MINUTES = DurationFieldType.minutes();
 private static final DurationFieldType SECONDS = DurationFieldType.seconds();

 @Test
 public void testGetInstance() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);
 UnsupportedDurationField unsupportedHalfHours = UnsupportedDurationField.getInstance(HALF_HOURS);
 UnsupportedDurationField unsupportedMinutes = UnsupportedDurationField.getInstance(MINUTES);
 UnsupportedDurationField unsupportedSeconds = UnsupportedDurationField.getInstance(SECONDS);

 assertEquals(DAYS, unsupportedDays.getType());
 assertEquals(HALF_HOURS, unsupportedHalfHours.getType());
 assertEquals(MINUTES, unsupportedMinutes.getType());
 assertEquals(SECONDS, unsupportedSeconds.getType());
 }

 @Test
 public void testIsSupported() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);
 UnsupportedDurationField unsupportedHalfHours = UnsupportedDurationField.getInstance(HALF_HOURS);
 UnsupportedDurationField unsupportedMinutes = UnsupportedDurationField.getInstance(MINUTES);
 UnsupportedDurationField unsupportedSeconds = UnsupportedDurationField.getInstance(SECONDS);

 assertFalse(unsupportedDays.isSupported());
 assertFalse(unsupportedHalfHours.isSupported());
 assertFalse(unsupportedMinutes.isSupported());
 assertFalse(unsupportedSeconds.isSupported());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValue() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);
 long duration = 12345;
 int expected = unsupportedDays.getValue(duration);

 // Should not reach here
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAsLong() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);
 long duration = 12345;
 long expected = unsupportedDays.getValueAsLong(duration);

 // Should not reach here
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueWithInstant() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);
 long duration = 12345;
 long instant = 621355768000L;
 int expected = unsupportedDays.getValue(duration, instant);

 // Should not reach here
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAsLongWithInstant() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);
 long duration = 12345;
 long instant = 621355768000L;
 long expected = unsupportedDays.getValueAsLong(duration, instant);

 // Should not reach here
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillis() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);
 int value = 12345;
 long expected = unsupportedDays.getMillis(value);

 // Should not reach here
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisWithValue() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);
 long value = 1234567890123L;
 long expected = unsupportedDays.getMillis(value);

 // Should not reach here
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisWithValueAndInstant() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);
 long value = 1234567890123L;
 long instant = 621355768000L;
 long expected = unsupportedDays.getMillis(value, instant);

 // Should not reach here
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAdd() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);
 long instant = 621355768000L;
 int value = 12345;
 long expected = unsupportedDays.add(instant, value);

 // Should not reach here
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddWithLongValue() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);
 long instant = 621355768000L;
 long value = 1234567890123L;
 long expected = unsupportedDays.add(instant, value);

 // Should not reach here
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetDifference() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);
 long minuendInstant = 621355768000L;
 long subtrahendInstant = 621355768000L;
 int expected = unsupportedDays.getDifference(minuendInstant, subtrahendInstant);

 // Should not reach here
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetDifferenceAsLong() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);
 long minuendInstant = 621355768000L;
 long subtrahendInstant = 621355768000L;
 long expected = unsupportedDays.getDifferenceAsLong(minuendInstant, subtrahendInstant);

 // Should not reach here
 }

 @Test
 public void testGetUnitMillis() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);
 long expected = unsupportedDays.getUnitMillis();

 assertEquals(0, expected);
 }

 @Test
 public void testCompareTo() {
 UnsupportedDurationField unsupportedDays1 = UnsupportedDurationField.getInstance(DAYS);
 UnsupportedDurationField unsupportedDays2 = UnsupportedDurationField.getInstance(DAYS);

 assertEquals(0, unsupportedDays1.compareTo(unsupportedDays2));
 }

 @Test
 public void testEquals() {
 UnsupportedDurationField unsupportedDays1 = UnsupportedDurationField.getInstance(DAYS);
 UnsupportedDurationField unsupportedDays2 = UnsupportedDurationField.getInstance(DAYS);
 UnsupportedDurationField unsupportedHalfHours1 = UnsupportedDurationField.getInstance(HALF_HOURS);
 UnsupportedDurationField unsupportedHalfHours2 = UnsupportedDurationField.getInstance(HALF_HOURS);

 assertTrue(unsupportedDays1.equals(unsupportedDays1));
 assertTrue(unsupportedDays1.equals(unsupportedDays2));
 assertFalse(unsupportedDays1.equals(unsupportedHalfHours1));
 }

 @Test
 public void testHashCode() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);
 UnsupportedDurationField unsupportedHalfHours = UnsupportedDurationField.getInstance(HALF_HOURS);

 assertEquals(DAYS.getName().hashCode(), unsupportedDays.hashCode());
 assertEquals(HALF_HOURS.getName().hashCode(), unsupportedHalfHours.hashCode());
 assertNotEquals(unsupportedDays.hashCode(), unsupportedHalfHours.hashCode());
 }

 @Test
 public void testToString() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);

 assertEquals("UnsupportedDurationField[DAYS]", unsupportedDays.toString());
 }
}