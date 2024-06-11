import org.junit.Test;
import org.joda.time.DurationFieldType;
import org.joda.time.UnsupportedDurationField;
import static org.junit.Assert.assertEquals;

public class UnsupportedDurationFieldTest {

 @Test
 public void testGetInstance() {
 UnsupportedDurationField unsupportedField1 = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
 UnsupportedDurationField unsupportedField2 = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
 assertEquals(unsupportedField1, unsupportedField2);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testIsSupported() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
 assertEquals(false, unsupportedField.isSupported());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValue() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
 long duration = 10L;
 unsupportedField.getValue(duration);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAsLong() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
 long duration = 10L;
 unsupportedField.getValueAsLong(duration);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAtInstant() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
 long instant = 10L;
 unsupportedField.getValue(instant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAsLongAtInstant() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
 long instant = 10L;
 unsupportedField.getValueAsLong(instant);
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
 unsupportedField.getMillis(value);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAdd() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
 long instant = 10L;
 long value = 5L;
 unsupportedField.add(instant, value);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetDifference() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
 long minuendInstant = 10L;
 long subtrahendInstant = 5L;
 unsupportedField.getDifference(minuendInstant, subtrahendInstant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetDifferenceAsLong() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
 long minuendInstant = 10L;
 long subtrahendInstant = 5L;
 unsupportedField.getDifferenceAsLong(minuendInstant, subtrahendInstant);
 }

 @Test
 public void testGetName() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
 assertEquals("seconds", unsupportedField.getName());
 }

 @Test
 public void testGetType() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
 assertEquals(DurationFieldType.SECONDS, unsupportedField.getType());
 }

 @Test
 public void testEquals() {
 UnsupportedDurationField unsupportedField1 = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
 UnsupportedDurationField unsupportedField2 = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
 UnsupportedDurationField unsupportedField3 = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
 assertEquals(unsupportedField1, unsupportedField1);
 assertEquals(unsupportedField1, unsupportedField2);
 assertNotEquals(unsupportedField1, unsupportedField3);
 }

 @Test
 public void testHashCode() {
 UnsupportedDurationField unsupportedField1 = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
 UnsupportedDurationField unsupportedField2 = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
 UnsupportedDurationField unsupportedField3 = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
 int hashcode1 = unsupportedField1.hashCode();
 int hashcode2 = unsupportedField2.hashCode();
 int hashcode3 = unsupportedField3.hashCode();
 assertEquals(hashcode1, hashcode1);
 assertEquals(hashcode1, hashcode2);
 assertNotEquals(hashcode1, hashcode3);
 }
}