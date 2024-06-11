import org.junit.Test;
import org.joda.time.DurationFieldType;
import org.joda.time.DurationField;
import org.joda.time.Duration;
import org.joda.time.DateTimeConstants;
import static org.junit.Assert.*;

public class UnsupportedDurationFieldTest {

 private static final DurationFieldType UNSUPPORTED_MINUTES = DurationFieldType.minute();
 private static final DurationField UNSUPPORTED_MINUTES_FIELD = UnsupportedDurationField.getInstance(UNSUPPORTED_MINUTES);

 @Test
 public void testGetType() {
 DurationField durationField = UNSUPPORTED_MINUTES_FIELD;
 assertEquals(UNSUPPORTED_MINUTES, durationField.getType());
 }

 @Test
 public void testGetName() {
 DurationField durationField = UNSUPPORTED_MINUTES_FIELD;
 assertEquals("minute", durationField.getName());
 }

 @Test
 public void testIsSupported() {
 DurationField durationField = UNSUPPORTED_MINUTES_FIELD;
 assertFalse(durationField.isSupported());
 }

 @Test
 public void testIsPrecise() {
 DurationField durationField = UNSUPPORTED_MINUTES_FIELD;
 assertTrue(durationField.isPrecise());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValue() {
 DurationField durationField = UNSUPPORTED_MINUTES_FIELD;
 long duration = DateTimeConstants.MILLIS_PER_DAY;
 durationField.getValue(duration);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAsLong() {
 DurationField durationField = UNSUPPORTED_MINUTES_FIELD;
 long duration = DateTimeConstants.MILLIS_PER_DAY;
 durationField.getValueAsLong(duration);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueWithInstant() {
 DurationField durationField = UNSUPPORTED_MINUTES_FIELD;
 long duration = DateTimeConstants.MILLIS_PER_DAY;
 long instant = DateTimeConstants.MILLIS_PER_DAY;
 durationField.getValue(duration, instant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAsLongWithInstant() {
 DurationField durationField = UNSUPPORTED_MINUTES_FIELD;
 long duration = DateTimeConstants.MILLIS_PER_DAY;
 long instant = DateTimeConstants.MILLIS_PER_DAY;
 durationField.getValueAsLong(duration, instant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillis() {
 DurationField durationField = UNSUPPORTED_MINUTES_FIELD;
 int value = 5;
 durationField.getMillis(value);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisWithValue() {
 DurationField durationField = UNSUPPORTED_MINUTES_FIELD;
 long value = 1000;
 durationField.getMillis(value);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisWithValueAndInstant() {
 DurationField durationField = UNSUPPORTED_MINUTES_FIELD;
 long value = 1000;
 long instant = 1000;
 durationField.getMillis(value, instant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAdd() {
 DurationField durationField = UNSUPPORTED_MINUTES_FIELD;
 long instant = 0;
 int value = 5;
 durationField.add(instant, value);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddWithInstant() {
 DurationField durationField = UNSUPPORTED_MINUTES_FIELD;
 long instant = 0;
 long value = 5;
 durationField.add(instant, value);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetDifference() {
 DurationField durationField = UNSUPPORTED_MINUTES_FIELD;
 long minuendInstant = 0;
 long subtrahendInstant = 0;
 durationField.getDifference(minuendInstant, subtrahendInstant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetDifferenceAsLong() {
 DurationField durationField = UNSUPPORTED_MINUTES_FIELD;
 long minuendInstant = 0;
 long subtrahendInstant = 0;
 durationField.getDifferenceAsLong(minuendInstant, subtrahendInstant);
 }

 @Test
 public void testGetUnitMillis() {
 DurationField durationField = UNSUPPORTED_MINUTES_FIELD;
 long unitMillis = durationField.getUnitMillis();
 assertEquals(0, unitMillis);
 }

 @Test
 public void testCompareTo() {
 DurationField durationField1 = UNSUPPORTED_MINUTES_FIELD;
 DurationField durationField2 = UNSUPPORTED_MINUTES_FIELD;
 int comparison = durationField1.compareTo(durationField2);
 assertEquals(0, comparison);
 }
}