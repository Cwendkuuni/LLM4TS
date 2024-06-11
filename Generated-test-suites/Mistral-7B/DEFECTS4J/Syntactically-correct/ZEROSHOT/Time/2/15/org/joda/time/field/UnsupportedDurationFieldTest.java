import org.joda.time.BaseDateTime;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.UnsupportedOperationException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

public class UnsupportedDurationFieldTest {

 @Test
 public void testGetInstance() {
 DurationFieldType type = DurationFieldType.INSTANT_SECONDS;
 UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
 UnsupportedDurationField another = UnsupportedDurationField.getInstance(type);
 assertEquals(unsupported, another);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValue() {
 UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
 long duration = 123456789;
 unsupported.getValue(duration);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAsLong() {
 UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
 long duration = 123456789;
 unsupported.getValueAsLong(duration);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAtInstant() {
 UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
 long duration = 123456789;
 long instant = System.currentTimeMillis();
 unsupported.getValue(duration, instant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAsLongAtInstant() {
 UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
 long duration = 123456789;
 long instant = System.currentTimeMillis();
 unsupported.getValueAsLong(duration, instant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillis() {
 UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
 unsupported.getMillis(5);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisAtInstant() {
 UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
 unsupported.getMillis(5, System.currentTimeMillis());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAdd() {
 UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
 long instant = System.currentTimeMillis();
 unsupported.add(instant, 5);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetDifference() {
 UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
 long minuendInstant = System.currentTimeMillis();
 long subtrahendInstant = System.currentTimeMillis() - 1000;
 unsupported.getDifference(minuendInstant, subtrahendInstant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetDifferenceAsLong() {
 UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
 long minuendInstant = System.currentTimeMillis();
 long subtrahendInstant = System.currentTimeMillis() - 1000;
 unsupported.getDifferenceAsLong(minuendInstant, subtrahendInstant);
 }

 @Test
 public void testGetUnitMillis() {
 UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
 assertEquals(0, unsupported.getUnitMillis());
 }

 @Test
 public void testCompareTo() {
 UnsupportedDurationField unsupported1 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
 UnsupportedDurationField unsupported2 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
 assertEquals(0, unsupported1.compareTo(unsupported2));
 }

 @Test
 public void testHashCode() {
 UnsupportedDurationField unsupported1 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
 UnsupportedDurationField unsupported2 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
 assertEquals(unsupported1.hashCode(), unsupported2.hashCode());
 }

 @Test
 public void testEquals() {
 UnsupportedDurationField unsupported1 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
 UnsupportedDurationField unsupported2 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
 UnsupportedDurationField unsupported3 = UnsupportedDurationField.getInstance(DurationFieldType.INSTANT_MILLIS);
 assertEquals(unsupported1, unsupported1);
 assertEquals(unsupported1, unsupported2);
 assertNotEquals(unsupported1, unsupported3);
 }

 @Test
 public void testToString() {
 UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
 assertEquals("UnsupportedDurationField[DAYS]", unsupported.toString());
 }

}