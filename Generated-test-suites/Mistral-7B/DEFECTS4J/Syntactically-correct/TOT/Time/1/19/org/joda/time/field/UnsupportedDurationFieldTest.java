import org.joda.time.DurationFieldType;
import org.joda.time.UnsupportedDurationField;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnsupportedDurationFieldTest {

 @Test
 public void testGetInstance() {
 DurationFieldType type = DurationFieldType.SECONDS;
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
 UnsupportedDurationField anotherUnsupportedField = UnsupportedDurationField.getInstance(type);

 assertEquals(type, unsupportedField.getType());
 assertEquals(unsupportedField, anotherUnsupportedField);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueUnsupported() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
 long duration = 10;
 unsupportedField.getValue(duration);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAsLongUnsupported() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
 long duration = 10;
 unsupportedField.getValueAsLong(duration);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueUnsupportedInstant() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
 long duration = 10;
 long instant = 1234567890123L;
 unsupportedField.getValue(duration, instant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAsLongUnsupportedInstant() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
 long duration = 10;
 long instant = 1234567890123L;
 unsupportedField.getValueAsLong(duration, instant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisUnsupported() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
 int value = 10;
 unsupportedField.getMillis(value);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisUnsupportedLong() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
 long value = 10;
 unsupportedField.getMillis(value);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisUnsupportedValueLongInstant() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
 int value = 10;
 long instant = 1234567890123L;
 unsupportedField.getMillis(value, instant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisUnsupportedLongValueInstant() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
 long value = 10;
 long instant = 1234567890123L;
 unsupportedField.getMillis(value, instant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddUnsupported() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
 long instant = 1234567890123L;
 int value = 10;
 unsupportedField.add(instant, value);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddUnsupportedLong() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
 long instant = 1234567890123L;
 long value = 10;
 unsupportedField.add(instant, value);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetDifferenceUnsupported() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
 long minuendInstant = 1234567890123L;
 long subtrahendInstant = 1234567890122L;
 unsupportedField.getDifference(minuendInstant, subtrahendInstant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetDifferenceAsLongUnsupported() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
 long minuendInstant = 1234567890123L;
 long subtrahendInstant = 1234567890122L;
 unsupportedField.getDifferenceAsLong(minuendInstant, subtrahendInstant);
 }

 @Test
 public void testCompareTo() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
 UnsupportedDurationField anotherUnsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
 assertTrue(unsupportedField.compareTo(anotherUnsupportedField) > 0);
 }

}