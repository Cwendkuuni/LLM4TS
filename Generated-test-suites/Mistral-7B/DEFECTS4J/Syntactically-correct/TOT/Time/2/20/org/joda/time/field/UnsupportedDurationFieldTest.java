import org.junit.Test;
import org.joda.time.DurationFieldType;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.UnsupportedDurationField;

import static org.junit.Assert.assertThrows;

public class UnsupportedDurationFieldTest {

 @Test
 public void testUnsupportedGetValue() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
 long duration = 1234567890L;
 assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getValue(duration));
 }

 @Test
 public void testUnsupportedGetValueAsLong() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
 long duration = 1234567890L;
 assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getValueAsLong(duration));
 }

 @Test
 public void testUnsupportedGetValueAtInstant() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
 long duration = 1234567890L;
 long instant = 1234567890123L;
 assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getValue(duration, instant));
 }

 @Test
 public void testUnsupportedGetValueAsLongAtInstant() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
 long duration = 1234567890L;
 long instant = 1234567890123L;
 assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getValueAsLong(duration, instant));
 }

 @Test
 public void testUnsupportedGetMillis() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
 int value = 123;
 assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getMillis(value));
 }

 @Test
 public void testUnsupportedGetMillisAtInstant() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
 long value = 1234567890123L;
 assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getMillis(value));
 }

 @Test
 public void testUnsupportedAdd() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
 long instant = 1234567890123L;
 int value = 123;
 assertThrows(UnsupportedOperationException.class, () -> unsupportedField.add(instant, value));
 }

 @Test
 public void testUnsupportedAddLong() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
 long instant = 1234567890123L;
 long value = 1234567890L;
 assertThrows(UnsupportedOperationException.class, () -> unsupportedField.add(instant, value));
 }

 @Test
 public void testUnsupportedGetDifference() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
 long minuendInstant = 1234567890123L;
 long subtrahendInstant = 1234567890L;
 assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getDifference(minuendInstant, subtrahendInstant));
 }

 @Test
 public void testUnsupportedGetDifferenceAsLong() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
 long minuendInstant = 1234567890123L;
 long subtrahendInstant = 1234567890L;
 assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getDifferenceAsLong(minuendInstant, subtrahendInstant));
 }

 @Test
 public void testUnsupportedGetUnitMillis() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
 assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getUnitMillis());
 }

 @Test
 public void testUnsupportedCompareTo() {
 UnsupportedDurationField unsupportedField1 = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
 UnsupportedDurationField unsupportedField2 = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
 assertThrows(UnsupportedOperationException.class, () -> unsupportedField1.compareTo(unsupportedField2));
 }
}