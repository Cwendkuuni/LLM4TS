import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class UnsupportedDurationFieldTest {

 private static final DurationFieldType DAYS = DurationFieldType.days();
 private static final DurationFieldType HOURS = DurationFieldType.hours();
 private static final DurationFieldType MINUTES = DurationFieldType.minutes();
 private static final DurationFieldType SECONDS = DurationFieldType.seconds();

 @Test
 public void testGetInstance() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);
 UnsupportedDurationField unsupportedHours = UnsupportedDurationField.getInstance(HOURS);
 UnsupportedDurationField unsupportedMinutes = UnsupportedDurationField.getInstance(MINUTES);
 UnsupportedDurationField unsupportedSeconds = UnsupportedDurationField.getInstance(SECONDS);

 assertEquals(DAYS, unsupportedDays.getType());
 assertEquals(HOURS, unsupportedHours.getType());
 assertEquals(MINUTES, unsupportedMinutes.getType());
 assertEquals(SECONDS, unsupportedSeconds.getType());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testIsSupported() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DurationFieldType.milliseconds());
 assertFalse(unsupportedField.isSupported());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValue() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DurationFieldType.milliseconds());
 long duration = 1000L;
 assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getValue(duration));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAsLong() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DurationFieldType.milliseconds());
 long duration = 1000L;
 assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getValueAsLong(duration));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAtInstant() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DurationFieldType.milliseconds());
 long duration = 1000L;
 long instant = 1L;
 assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getValue(duration, instant));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAsLongAtInstant() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DurationFieldType.milliseconds());
 long duration = 1000L;
 long instant = 1L;
 assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getValueAsLong(duration, instant));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillis() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DurationFieldType.milliseconds());
 int value = 1;
 assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getMillis(value));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisAtInstant() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DurationFieldType.milliseconds());
 long value = 1L;
 long instant = 1L;
 assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getMillis(value, instant));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAdd() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DurationFieldType.milliseconds());
 long instant = 1L;
 int value = 1;
 assertThrows(UnsupportedOperationException.class, () -> unsupportedField.add(instant, value));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddAtInstant() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DurationFieldType.milliseconds());
 long instant = 1L;
 long value = 1L;
 assertThrows(UnsupportedOperationException.class, () -> unsupportedField.add(instant, value));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetDifference() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DurationFieldType.milliseconds());
 long minuendInstant = 1L;
 long subtrahendInstant = 1L;
 assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getDifference(minuendInstant, subtrahendInstant));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetDifferenceAsLong() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DurationFieldType.milliseconds());
 long minuendInstant = 1L;
 long subtrahendInstant = 1L;
 assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getDifferenceAsLong(minuendInstant, subtrahendInstant));
 }

 @Test
 public void testGetUnitMillis() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DurationFieldType.milliseconds());
 long unitMillis = unsupportedField.getUnitMillis();
 assertEquals(0, unitMillis);
 }

 @Test
 public void testCompareTo() {
 UnsupportedDurationField unsupportedField1 = new UnsupportedDurationField(DurationFieldType.milliseconds());
 UnsupportedDurationField unsupportedField2 = new UnsupportedDurationField(DurationFieldType.milliseconds());
 int comparisonResult = unsupportedField1.compareTo(unsupportedField2);
 assertEquals(0, comparisonResult);
 }

 @Test
 public void testEquals() {
 UnsupportedDurationField unsupportedField1 = new UnsupportedDurationField(DurationFieldType.milliseconds());
 UnsupportedDurationField unsupportedField2 = new UnsupportedDurationField(DurationFieldType.milliseconds());
 UnsupportedDurationField unsupportedField3 = new UnsupportedDurationField(DAYS);

 assertTrue(unsupportedField1.equals(unsupportedField1));
 assertTrue(unsupportedField1.equals(unsupportedField2));
 assertFalse(unsupportedField1.equals(unsupportedField3));
 }

 @Test
 public void testHashCode() {
 UnsupportedDurationField unsupportedField1 = new UnsupportedDurationField(DurationFieldType.milliseconds());
 UnsupportedDurationField unsupportedField2 = new UnsupportedDurationField(DurationFieldType.milliseconds());
 UnsupportedDurationField unsupportedField3 = new UnsupportedDurationField(DAYS);

 int hashCode1 = unsupportedField1.hashCode();
 int hashCode2 = unsupportedField2.hashCode();
 int hashCode3 = unsupportedField3.hashCode();

 assertEquals(hashCode1, hashCode2);
 assertNotEquals(hashCode1, hashCode3);
 }

 @Test
 public void testToString() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DurationFieldType.milliseconds());
 String toString = unsupportedField.toString();
 assertEquals("UnsupportedDurationField[milliseconds]", toString);
 }
}