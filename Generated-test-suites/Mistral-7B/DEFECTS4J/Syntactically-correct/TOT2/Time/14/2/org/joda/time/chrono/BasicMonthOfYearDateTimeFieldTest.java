import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DurationField;
import org.joda.time.FieldUtils;
import org.joda.time.ImpreciseDateTimeField;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

 private static final Chronology UTC = DateTimeUtils.UTC;

 @Test
 public void testBasicGet() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(UTC, 2); // February
 DateTime date = new DateTime(2900, 2, 29); // Leap year
 assertEquals(2, field.get(date.getMillisecond()));
 }

 @Test
 public void testBasicSet() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(UTC, 2); // February
 DateTime date = new DateTime(2900, 2, 28); // Non-leap year
 long newValue = 3; // March
 long expected = new DateTime(2900, 3, 1).getMillisecond();
 field.set(date.getMillisecond(), newValue);
 assertEquals(expected, field.get(date.getMillisecond()));
 }

 @Test
 public void testBasicAdd() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(UTC, 2); // February
 DateTime date = new DateTime(2000, 2, 28); // Non-leap year
 long newValue = 2; // February again
 long expected = new DateTime(2000, 3, 2).getMillisecond();
 assertEquals(expected, field.add(date.getMillisecond(), newValue));
 }

 @Test
 public void testBasicAddWrap() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(UTC, 12); // December
 DateTime date = new DateTime(2900, 12, 31); // Last day of the year
 long newValue = 1; // January
 long expected = new DateTime(2900, 1, 1).getMillisecond();
 assertEquals(expected, field.addWrapField(date.getMillisecond(), newValue));
 }

 @Test
 public void testBasicSubtract() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(UTC, 2); // February
 DateTime date = new DateTime(2900, 3, 1); // First day of March
 long newValue = -1; // February
 long expected = new DateTime(2900, 2, 29).getMillisecond(); // A leap year
 assertEquals(expected, field.add(date.getMillisecond(), newValue));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBasicInvalidValueNegative() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(UTC, 2); // February
 long invalidValue = -1;
 field.set(Long.MIN_VALUE, invalidValue);
 fail("Should have thrown an IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBasicInvalidValueGreater() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(UTC, 12); // December
 long invalidValue = 13; // Greater than the maximum value
 field.set(Long.MAX_VALUE, invalidValue);
 fail("Should have thrown an IllegalArgumentException");
 }

 @Test
 public void testBasicMinMaxValues() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(UTC, 2); // February
 long minValue = field.getMinimumValue();
 long maxValue = field.getMaximumValue();
 long minDateTime = UTC.getMillis(new LocalDate(1, 1, 1));
 long maxDateTime = UTC.getMillis(new LocalDate(3000, 12, 31));
 long invalidValue = -1; // Below the minimum value
 long expectedMin = minDateTime;
 long expectedMax = maxDateTime;
 long value = minValue;
 assertEquals(expectedMin, value);
 value++;
 assertEquals(minValue + 1, value);
 assertEquals(expectedMin, field.get(value));
 value++;
 // Test the maximum value
 assertEquals(expectedMax, value);
 assertEquals(expectedMax, field.get(value));
 value++;
 assertEquals(minValue, field.get(value));
 // Test invalid value
 assertEquals(minValue, field.get(value + 1));
 assertEquals(minValue, field.get(FieldUtils.VALID_VALUE_FOR_TEST));
 assertEquals(minValue, field.get(Long.MIN_VALUE));
 assertEquals(minValue, field.get(Long.MIN_VALUE + 1));
 assertEquals(minValue, field.get(Long.MAX_VALUE));
 assertEquals(minValue, field.get(Long.MAX_VALUE - 1));
 assertEquals(minValue, field.get(invalidValue));
 }

 @Test
 public void testBasicGetFromInstant() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(UTC, 2); // February
 long instant = new DateTime(2900, 2, 28).getMillisecond(); // A non-leap year
 assertEquals(2, field.get(instant));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testBasicGetFromField() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(UTC, 2); // February
 DurationField fieldToGet = field.getRangeDurationField();
 try {
 field.get(Long.MIN_VALUE, fieldToGet);
 fail("Should have thrown an UnsupportedOperationException");
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testBasicSetToField() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(UTC, 2); // February
 DurationField fieldToSet = field.getRangeDurationField();
 try {
 field.set(Long.MIN_VALUE, fieldToSet, 1);
 fail("Should have thrown an UnsupportedOperationException");
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testBasicAddToField() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(UTC, 2); // February
 DurationField fieldToAdd = field.getRangeDurationField();
 try {
 field.add(Long.MIN_VALUE, fieldToAdd);
 fail("Should have thrown an UnsupportedOperationException");
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 @Test
 public void testBasicAddWrapFieldToField() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(UTC, 12); // December
 DurationField fieldToAdd = field.getRangeDurationField();
 long instant = new DateTime(2900, 12, 31).getMillisecond(); // Last day of the year
 long newValue = -1; // Below the minimum value
 long result = field.addWrapField(instant, newValue);
 long expected = new DateTime(2900, 1, 1).getMillisecond();
 assertEquals(expected, result);
 }

 @Test
 public void testBasicGetFromReadablePartial() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(UTC, 2); // February
 ReadablePartial partial = new LocalDate();
 int[] values = new int[] { 2900, 2, 28 };
 long result = field.add(partial, 2, values, 1).getMillis();
 assertEquals(new DateTime(2900, 3, 1).getMillisecond(), result);
 }
}