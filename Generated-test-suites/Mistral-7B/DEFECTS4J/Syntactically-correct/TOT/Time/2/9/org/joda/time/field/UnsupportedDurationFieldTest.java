import org.junit.Test;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.UnsupportedOperationException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class UnsupportedDurationFieldTest {

 private static final DurationFieldType INSTANT_SECONDS = DurationFieldType.instantSeconds();
 private static final DurationFieldType DAYS = DurationFieldType.days();

 @Test
 public void testUnsupportedDurationFieldInstance() {
 UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(INSTANT_SECONDS);
 assertEquals(INSTANT_SECONDS, unsupported.getType());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueUnsupported() {
 UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(INSTANT_SECONDS);
 long duration = 1L;
 unsupported.getValue(duration);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAsLongUnsupported() {
 UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(INSTANT_SECONDS);
 long duration = 1L;
 unsupported.getValueAsLong(duration);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueWithInstantUnsupported() {
 UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(INSTANT_SECONDS);
 long duration = 1L;
 long instant = 1L;
 unsupported.getValue(duration, instant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAsLongWithInstantUnsupported() {
 UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(INSTANT_SECONDS);
 long duration = 1L;
 long instant = 1L;
 unsupported.getValueAsLong(duration, instant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisUnsupported() {
 UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(INSTANT_SECONDS);
 int value = 1;
 unsupported.getMillis(value);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisWithValueUnsupported() {
 UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(INSTANT_SECONDS);
 long value = 1L;
 unsupported.getMillis(value);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisWithValueInstantUnsupported() {
 UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(INSTANT_SECONDS);
 int value = 1;
 long instant = 1L;
 unsupported.getMillis(value, instant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisWithInstantValueUnsupported() {
 UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(INSTANT_SECONDS);
 long value = 1L;
 long instant = 1L;
 unsupported.getMillis(value, instant);
 }

 @Test
 public void testAddUnsupported() {
 UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(INSTANT_SECONDS);
 long instant = 1L;
 assertThrows(UnsupportedOperationException.class, () -> unsupported.add(instant, 1));
 }

 @Test
 public void testAddWithInstantUnsupported() {
 UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(INSTANT_SECONDS);
 long instant = 1L;
 assertThrows(UnsupportedOperationException.class, () -> unsupported.add(instant, 1L));
 }

 @Test
 public void testGetDifferenceUnsupported() {
 UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(INSTANT_SECONDS);
 long minuendInstant = 1L;
 long subtrahendInstant = 1L;
 assertThrows(UnsupportedOperationException.class, () -> unsupported.getDifference(minuendInstant, subtrahendInstant));
 }

 @Test
 public void testGetDifferenceAsLongUnsupported() {
 UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(INSTANT_SECONDS);
 long minuendInstant = 1L;
 long subtrahendInstant = 1L;
 assertThrows(UnsupportedOperationException.class, () -> unsupported.getDifferenceAsLong(minuendInstant, subtrahendInstant));
 }

 @Test
 public void testGetUnitMillisUnsupported() {
 UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(INSTANT_SECONDS);
 assertEquals(0, unsupported.getUnitMillis());
 }

 @Test
 public void testCompareToUnsupported() {
 UnsupportedDurationField unsupported1 = UnsupportedDurationField.getInstance(INSTANT_SECONDS);
 UnsupportedDurationField unsupported2 = UnsupportedDurationField.getInstance(INSTANT_SECONDS);
 assertEquals(0, unsupported1.compareTo(unsupported2));
 }

 @Test
 public void testEqualsUnsupported() {
 UnsupportedDurationField unsupported1 = UnsupportedDurationField.getInstance(INSTANT_SECONDS);
 UnsupportedDurationField unsupported2 = UnsupportedDurationField.getInstance(INSTANT_SECONDS);
 assertEquals(unsupported1, unsupported2);
 UnsupportedDurationField unsupported3 = UnsupportedDurationField.getInstance(DAYS);
 assertNotEquals(unsupported1, unsupported3);
 }

 @Test
 public void testHashCodeUnsupported() {
 UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(INSTANT_SECONDS);
 assertEquals(INSTANT_SECONDS.hashCode(), unsupported.hashCode());
 }

 @Test
 public void testToStringUnsupported() {
 UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(INSTANT_SECONDS);
 assertEquals("UnsupportedDurationField[instantSeconds]", unsupported.toString());
 }
}