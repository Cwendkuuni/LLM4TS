import org.joda.time.DurationFieldType;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.junit.Test;
import static org.junit.Assert.assertThrows;

public class UnsupportedDurationFieldTest {

 @Test
 public void testUnsupportedDurationFieldInstance() {
 DurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.INDETERMINATE);
 DurationFieldType type = unsupportedField.getType();
 assert (type == DurationFieldType.INDETERMINATE);
 }

 @Test
 public void testUnsupportedGetValue() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.INDETERMINATE);
 long duration = 123456789;
 assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getValue(duration));
 }

 @Test
 public void testUnsupportedGetValueAsLong() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.INDETERMINATE);
 long duration = 123456789;
 assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getValueAsLong(duration));
 }

 @Test
 public void testUnsupportedAdd() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.INDETERMINATE);
 long instant = 123456789;
 assertThrows(UnsupportedOperationException.class, () -> unsupportedField.add(instant, 1));
 }

 @Test
 public void testUnsupportedGetDifference() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.INDETERMINATE);
 long minuendInstant = 123456789;
 long subtrahendInstant = 567890123;
 assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getDifference(minuendInstant, subtrahendInstant));
 }

 @Test
 public void testUnsupportedGetDifferenceAsLong() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.INDETERMINATE);
 long minuendInstant = 123456789;
 long subtrahendInstant = 567890123;
 assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getDifferenceAsLong(minuendInstant, subtrahendInstant));
 }
}