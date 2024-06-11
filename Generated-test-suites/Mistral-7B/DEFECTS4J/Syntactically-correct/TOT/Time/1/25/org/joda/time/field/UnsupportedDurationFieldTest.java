import org.joda.time.DurationFieldType;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnsupportedDurationFieldTest {

 @Test
 public void testUnsupportedFieldInstance() {
 DurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
 DurationFieldType type = unsupportedField.getType();
 assertEquals(DurationFieldType.DAYS, type);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testUnsupportedGetValue() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
 long duration = 10;
 int value = unsupportedField.getValue(duration);
 fail("UnsupportedOperationException expected");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testUnsupportedGetValueAsLong() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
 long duration = 10;
 long value = unsupportedField.getValueAsLong(duration);
 fail("UnsupportedOperationException expected");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testUnsupportedGetValueAtInstant() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
 long duration = 10;
 long instant = 1234567890L;
 int value = unsupportedField.getValue(duration, instant);
 fail("UnsupportedOperationException expected");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testUnsupportedGetValueAsLongAtInstant() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
 long duration = 10;
 long instant = 1234567890L;
 long value = unsupportedField.getValueAsLong(duration, instant);
 fail("UnsupportedOperationException expected");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testUnsupportedGetMillis() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
 int value = unsupportedField.getMillis(5);
 fail("UnsupportedOperationException expected");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testUnsupportedGetMillisAtInstant() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
 long value = unsupportedField.getMillis(5, 1234567890L);
 fail("UnsupportedOperationException expected");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testUnsupportedAdd() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
 long instant = 1234567890L;
 long value = unsupportedField.add(instant, 5);
 fail("UnsupportedOperationException expected");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testUnsupportedAddLong() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
 long instant = 1234567890L;
 long value = unsupportedField.add(instant, 5L);
 fail("UnsupportedOperationException expected");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testUnsupportedGetDifference() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
 long minuendInstant = 1234567890L;
 long subtrahendInstant = 1234567880L;
 int difference = unsupportedField.getDifference(minuendInstant, subtrahendInstant);
 fail("UnsupportedOperationException expected");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testUnsupportedGetDifferenceAsLong() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
 long minuendInstant = 1234567890L;
 long subtrahendInstant = 1234567880L;
 long difference = unsupportedField.getDifferenceAsLong(minuendInstant, subtrahendInstant);
 fail("UnsupportedOperationException expected");
 }

 @Test
 public void testSingleton() {
 UnsupportedDurationField unsupportedField1 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
 UnsupportedDurationField unsupportedField2 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
 assertEquals(unsupportedField1, unsupportedField2);
 }
}