import org.joda.time.DurationFieldType;
import org.joda.time.DurationField;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnsupportedDurationFieldTest {

 private static final DurationFieldType INSTANCED_TYPE = DurationFieldType.WEEKS;

 @Test
 public void testGetInstance() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(INSTANCED_TYPE);
 UnsupportedDurationField unsupportedField2 = UnsupportedDurationField.getInstance(INSTANCED_TYPE);
 assertEquals(unsupportedField, unsupportedField2);
 }

 @Test
 public void testUnsupportedGetValue() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(INSTANCED_TYPE);
 long duration = 123456L;
 try {
 unsupportedField.getValue(duration);
 fail("Expected UnsupportedOperationException");
 } catch (UnsupportedOperationException e) {
 // expected
 }
 }

 @Test
 public void testUnsupportedAdd() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(INSTANCED_TYPE);
 long instant = 1234567890L;
 long value = 12345L;
 try {
 unsupportedField.add(instant, value);
 fail("Expected UnsupportedOperationException");
 } catch (UnsupportedOperationException e) {
 // expected
 }
 }

 @Test
 public void testUnsupportedCompareTo() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(INSTANCED_TYPE);
 DurationField supportedField = DurationFieldType.MILLIS.getStandardField();
 try {
 int comparisonResult = unsupportedField.compareTo(supportedField);
 fail("Expected UnsupportedOperationException");
 } catch (UnsupportedOperationException e) {
 // expected
 }
 assertEquals(1, comparisonResult);
 }

 @Test
 public void testEquals() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(INSTANCED_TYPE);
 UnsupportedDurationField unsupportedField2 = UnsupportedDurationField.getInstance(INSTANCED_TYPE);
 UnsupportedDurationField unsupportedField3 = UnsupportedDurationField.getInstance(DurationFieldType.MINUTES);
 assertEquals(unsupportedField, unsupportedField2);
 assertNotEquals(unsupportedField, unsupportedField3);
 }
}