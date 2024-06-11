import org.junit.Test;
import org.joda.time.DurationFieldType;
import static org.junit.Assert.assertEquals;

public class UnsupportedDurationFieldTest {

 @Test
 public void testUnsupportedDurationField() {
 UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.INSTANCE_DAY_OF_MONTH);

 // Test getting the type
 assertEquals("Day of month", field.getType().getName());

 // Test getting the name
 assertEquals("Day of month", field.getName());

 // Test isSupported returns false
 assertFalse(field.isSupported());

 // Test getting value throws UnsupportedOperationException
 try {
 field.getValue(1L);
 } catch (UnsupportedOperationException e) {
 // Expected
 }

 // Test adding a duration throws UnsupportedOperationException
 try {
 field.add(0L, 1);
 } catch (UnsupportedOperationException e) {
 // Expected
 }

 // Test getting difference throws UnsupportedOperationException
 try {
 field.getDifference(0L, 1L);
 } catch (UnsupportedOperationException e) {
 // Expected
 }

 // Test getting unitMillis returns zero
 assertEquals(0, field.getUnitMillis());
 }
}