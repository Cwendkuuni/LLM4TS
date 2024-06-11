import org.junit.Test;
import org.joda.time.DurationFieldType;
import org.joda.time.DurationField;
import org.joda.time.Duration;
import static org.junit.Assert.*;

public class UnsupportedDurationFieldTest {

 @Test
 public void testGetInstance() {
 DurationField unsupportedField1 = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
 DurationField unsupportedField2 = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
 assertEquals(unsupportedField1, unsupportedField2);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValue() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
 long duration = 5000;
 int value = unsupportedField.getValue(duration);
 fail("Expected UnsupportedOperationException");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAsLong() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
 long duration = 5000;
 long value = unsupportedField.getValueAsLong(duration);
 fail("Expected UnsupportedOperationException");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueWithInstant() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
 long duration = 5000;
 long instant = 1493161235000L;
 int value = unsupportedField.getValue(duration, instant);
 fail("Expected UnsupportedOperationException");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueWithInstantAndValue() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
 long duration = 5000;
 long instant = 1493161235000L;
 int value = unsupportedField.getValue(duration, instant);
 int expectedValue = 5;
 fail("Expected UnsupportedOperationException with given value: " + expectedValue);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillis() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
 int value = 5;
 long millis = unsupportedField.getMillis(value);
 fail("Expected UnsupportedOperationException");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisWithValue() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
 long value = 5000;
 long millis = unsupportedField.getMillis(value);
 fail("Expected UnsupportedOperationException");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisWithValueAndInstant() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
 long value = 5000;
 long instant = 1493161235000L;
 long millis = unsupportedField.getMillis(value, instant);
 fail("Expected UnsupportedOperationException");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAdd() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
 long instant = 1493161235000L;
 long value = 5000;
 long result = unsupportedField.add(instant, value);
 fail("Expected UnsupportedOperationException");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddWithInstantAndValue() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
 long instant = 1493161235000L;
 long value = 5000;
 long result = unsupportedField.add(instant, value);
 long expectedResult = 1493161239000L;
 fail("Expected UnsupportedOperationException with given result: " + expectedResult);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetDifference() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
 long minuendInstant = 1493161235000L;
 long subtrahendInstant = 1493161234000L;
 int difference = unsupportedField.getDifference(minuendInstant, subtrahendInstant);
 fail("Expected UnsupportedOperationException");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetDifferenceWithInstantAndValue() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.seconds());long minuendInstant = 1493161235000L; long subtrahendInstant = 1493161234000L; int difference = unsupportedField.getDifference(minuendInstant, subtrahendInstant); int expectedDifference = 1; fail("Expected UnsupportedOperationException with given difference: " + expectedDifference); }

 @Test
 public void testCompareTo() {
 UnsupportedDurationField unsupportedField1 = UnsupportedDurationField.getInstance(DurationFieldType.seconds()); UnsupportedDurationField unsupportedField2 = UnsupportedDurationField.getInstance(DurationFieldType.minutes()); assertEquals(1, unsupportedField1.compareTo(unsupportedField2)); }

 @Test
 public void testEquals() {
 UnsupportedDurationField unsupportedField1 = UnsupportedDurationField.getInstance(DurationFieldType.seconds()); UnsupportedDurationField unsupportedField2 = UnsupportedDurationField.getInstance(DurationFieldType.seconds()); assertTrue(unsupportedField1.equals(unsupportedField2)); }

 @Test
 public void testHashCode() {
 UnsupportedDurationField unsupportedField1 = UnsupportedDurationField.getInstance(DurationFieldType.seconds()); UnsupportedDurationField unsupportedField2 = UnsupportedDurationField.getInstance(DurationFieldType.seconds()); assertEquals(unsupportedField1.hashCode(), unsupportedField2.hashCode()); }

 @Test
 public void testToString() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.seconds()); assertEquals("UnsupportedDurationField[seconds]", unsupportedField.toString()); }
}