import org.junit.Test;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.UnsupportedOperationException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Test cases for UnsupportedDurationField class.
 */
public class UnsupportedDurationFieldTest {

 private static final DurationFieldType DAYS = DurationFieldType.dayOfMonth();
 private static final DurationFieldType HOURS = DurationFieldType.hourOfDay();
 private static final DurationFieldType MINUTES = DurationFieldType.minute();
 private static final DurationFieldType SECONDS = DurationFieldType.second();
 private static final DurationFieldType MILLISECONDS = DurationFieldType.millis();

 @Test
 public void testUnsupportedGetValue() {
 UnsupportedDurationField unsupportedField_days = UnsupportedDurationField.getInstance(DAYS);
 long duration = 5L * 24 * 60 * 60 * 1000; // 5 days
 UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class, () -> unsupportedField_days.getValue(duration));
 assertEquals("dayOfMonth field is unsupported", exception.getMessage());
 }

 @Test
 public void testUnsupportedGetValueAsLong() {
 UnsupportedDurationField unsupportedField_days = UnsupportedDurationField.getInstance(DAYS);
 long duration = 5L * 24 * 60 * 60 * 1000; // 5 days
 UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class, () -> unsupportedField_days.getValueAsLong(duration));
 assertEquals("dayOfMonth field is unsupported", exception.getMessage());
 }

 @Test
 public void testUnsupportedAdd() {
 UnsupportedDurationField unsupportedField_days = UnsupportedDurationField.getInstance(DAYS);
 long duration = 5L * 24 * 60 * 60 * 1000; // 5 days
 UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class, () -> unsupportedField_days.add(0, duration));
 assertEquals("dayOfMonth field is unsupported", exception.getMessage());
 }

 @Test
 public void testUnsupportedGetValueWithInstant() {
 UnsupportedDurationField unsupportedField_days = UnsupportedDurationField.getInstance(DAYS);
 long duration = 5L * 24 * 60 * 60 * 1000; // 5 days
 UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class, () -> unsupportedField_days.getValue(duration, 12345));
 assertEquals("dayOfMonth field is unsupported", exception.getMessage());
 }

 @Test
 public void testUnsupportedGetValueAsLongWithInstant() {
 UnsupportedDurationField unsupportedField_days = UnsupportedDurationField.getInstance(DAYS);
 long duration = 5L * 24 * 60 * 60 * 1000; // 5 days
 UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class, () -> unsupportedField_days.getValueAsLong(duration, 12345));
 assertEquals("dayOfMonth field is unsupported", exception.getMessage());
 }

 @Test
 public void testUnsupportedGetDifference() {
 UnsupportedDurationField unsupportedField_days = UnsupportedDurationField.getInstance(DAYS);
 long minuendInstant = 12345;
 long subtrahendInstant = 6789;
 UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class, () -> unsupportedField_days.getDifference(minuendInstant, subtrahendInstant));
 assertEquals("dayOfMonth field is unsupported", exception.getMessage());
 }

 @Test
 public void testUnsupportedGetDifferenceAsLong() {
 UnsupportedDurationField unsupportedField_days = UnsupportedDurationField.getInstance(DAYS);
 long minuendInstant = 12345;
 long subtrahendInstant = 6789;
 UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class, () -> unsupportedField_days.getDifferenceAsLong(minuendInstant, subtrahendInstant));
 assertEquals("dayOfMonth field is unsupported", exception.getMessage());
 }

 @Test
 public void testUnsupportedGetUnitMillis() {
 UnsupportedDurationField unsupportedField_days = UnsupportedDurationField.getInstance(DAYS);
 long unitMillis = unsupportedField_days.getUnitMillis();
 assertEquals(0, unitMillis);
 }

 @Test
 public void testUnsupportedCompareTo() {
 UnsupportedDurationField unsupportedField_days = UnsupportedDurationField.getInstance(DAYS);
 UnsupportedDurationField unsupportedField_hours = UnsupportedDurationField.getInstance(HOURS);
 int comparisonResult = unsupportedField_days.compareTo(unsupportedField_hours);
 assertEquals(0, comparisonResult);
 }
}