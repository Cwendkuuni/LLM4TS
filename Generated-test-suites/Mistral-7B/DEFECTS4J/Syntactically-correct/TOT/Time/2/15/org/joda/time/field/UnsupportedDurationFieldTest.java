import org.junit.Test;
import org.joda.time.DurationFieldType;
import static org.junit.Assert.assertThrows;

public class UnsupportedDurationFieldTest {

 @Test
 public void testUnsupportedDurationFieldGetInstance() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.INSTANCE_DAY_OF_MONTH);
 UnsupportedDurationField anotherUnsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.INSTANCE_DAY_OF_MONTH);

 // Test instance equality
 assert(unsupportedField == anotherUnsupportedField);

 // Test instance name
 assert(unsupportedField.getName().equals(DurationFieldType.INSTANCE_DAY_OF_MONTH.getName()));
 }

 @Test
 public void testUnsupportedDurationFieldGetValueThrowsException() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.INSTANCE_DAY_OF_MONTH);

 assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getValue(1234567890123L));
 assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getValueAsLong(1234567890123L));
 }

 @Test
 public void testUnsupportedDurationFieldAddThrowsException() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.INSTANCE_DAY_OF_MONTH);

 assertThrows(UnsupportedOperationException.class, () -> unsupportedField.add(1234567890123L, 1));
 assertThrows(UnsupportedOperationException.class, () -> unsupportedField.add(1234567890123L, 2));
 }

 @Test
 public void testUnsupportedDurationFieldGetDifferenceThrowsException() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.INSTANCE_DAY_OF_MONTH);

 assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getDifference(1234567890123L, 9876543210987L));
 assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getDifferenceAsLong(1234567890123L, 9876543210987L));
 }

 @Test
 public void testUnsupportedDurationFieldCompareToReturnsZero() {
 UnsupportedDurationField unsupportedField1 = UnsupportedDurationField.getInstance(DurationFieldType.INSTANCE_DAY_OF_MONTH);
 UnsupportedDurationField unsupportedField2 = UnsupportedDurationField.getInstance(DurationFieldType.INSTANCE_DAY_OF_MONTH);

 assert(unsupportedField1.compareTo(unsupportedField2) == 0);
 }
}