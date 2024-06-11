import org.junit.Test;
import org.joda.time.DurationFieldType;
import org.joda.time.DurationField;
import org.joda.time.Duration;
import static org.junit.Assert.assertEquals;

public class UnsupportedDurationFieldTest {

 @Test
 public void testGetInstance() {
 UnsupportedDurationField unsupportedField1 = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
 UnsupportedDurationField unsupportedField2 = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);

 assertEquals(unsupportedField1, unsupportedField2);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValue() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
 long duration = 1000L;

 unsupportedField.getValue(duration);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAsLong() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
 long duration = 1000L;

 unsupportedField.getValueAsLong(duration);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAtInstant() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
 long duration = 1000L;
 long instant = 1234567890000L;

 unsupportedField.getValue(duration, instant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAsLongAtInstant() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
 long duration = 1000L;
 long instant = 1234567890000L;

 unsupportedField.getValueAsLong(duration, instant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillis() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
 int value = 10;

 unsupportedField.getMillis(value);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisAtInstant() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
 long value = 10L;
 long instant = 1234567890000L;

 unsupportedField.getMillis(value, instant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAdd() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
 long instant = 1234567890000L;
 int value = 10;

 unsupportedField.add(instant, value);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddLong() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
 long instant = 1234567890000L;
 long value = 10L;

 unsupportedField.add(instant, value);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetDifference() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
 long minuendInstant = 1234567890000L;
 long subtrahendInstant = 1234567880000L;

 unsupportedField.getDifference(minuendInstant, subtrahendInstant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetDifferenceAsLong() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
 long minuendInstant = 1234567890000L;
 long subtrahendInstant = 1234567880000L;

 unsupportedField.getDifferenceAsLong(minuendInstant, subtrahendInstant);
 }

 @Test
 public void testCompareTo() {
 UnsupportedDurationField unsupportedField1 = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
 UnsupportedDurationField unsupportedField2 = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);

 assertEquals(1, unsupportedField1.compareTo(unsupportedField2));
 }
}