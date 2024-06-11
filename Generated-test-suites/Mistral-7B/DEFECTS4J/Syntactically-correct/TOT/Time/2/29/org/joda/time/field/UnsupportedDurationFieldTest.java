import org.junit.Test;
import org.joda.time.DurationFieldType;
import org.joda.time.DurationField;
import org.joda.time.LocalDateTime;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class UnsupportedDurationFieldTest {

 private static final DurationFieldType DAYS = DurationFieldType.days();
 private static final DurationFieldType HOURS = DurationFieldType.hours();
 private static final DurationFieldType MINUTES = DurationFieldType.minutes();
 private static final DurationFieldType SECONDS = DurationFieldType.seconds();

 @Test
 public void testGetInstance() {
 UnsupportedDurationField field = UnsupportedDurationField.getInstance(DAYS);
 assertEquals(DAYS, field.getType());
 }

 @Test
 public void testGetValueThrowsException() {
 UnsupportedDurationField field = UnsupportedDurationField.getInstance(SECONDS);
 assertThrows(UnsupportedOperationException.class, () -> field.getValue(1000L));
 }

 @Test
 public void testGetValueAsLongThrowsException() {
 UnsupportedDurationField field = UnsupportedDurationField.getInstance(HOURS);
 assertThrows(UnsupportedOperationException.class, () -> field.getValueAsLong(123456789L));
 }

 @Test
 public void testGetValueWithInstantThrowsException() {
 UnsupportedDurationField field = UnsupportedDurationField.getInstance(MINUTES);
 assertThrows(UnsupportedOperationException.class, () -> field.getValue(123456789L, 1234567890123L));
 }

 @Test
 public void testGetMillisThrowsException() {
 UnsupportedDurationField field = UnsupportedDurationField.getInstance(SECONDS);
 assertThrows(UnsupportedOperationException.class, () -> field.getMillis(12345));
 }

 @Test
 public void testGetMillisWithInstantThrowsException() {
 UnsupportedDurationField field = UnsupportedDurationField.getInstance(MINUTES);
 assertThrows(UnsupportedOperationException.class, () -> field.getMillis(123456789L, 1234567890123L));
 }

 @Test
 public void testAddThrowsException() {
 UnsupportedDurationField field = UnsupportedDurationField.getInstance(DAYS);
 assertThrows(UnsupportedOperationException.class, () -> field.add(LocalDateTime.now().toDate().getTime(), 12345));
 }

 @Test
 public void testGetDifferenceThrowsException() {
 UnsupportedDurationField field = UnsupportedDurationField.getInstance(HOURS);
 assertThrows(UnsupportedOperationException.class, () -> field.getDifference(1234567890123L, 1234567890122L));
 }

 @Test
 public void testGetDifferenceAsLongThrowsException() {
 UnsupportedDurationField field = UnsupportedDurationField.getInstance(MINUTES);
 assertThrows(UnsupportedOperationException.class, () -> field.getDifferenceAsLong(1234567890123L, 1234567890122L));
 }

 @Test
 public void testGetUnitMillisReturnsZero() {
 UnsupportedDurationField field = UnsupportedDurationField.getInstance(DAYS);
 assertEquals(0, field.getUnitMillis());
 }

 @Test
 public void testCompareToReturnsZero() {
 UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DAYS);
 UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DAYS);
 assertEquals(0, field1.compareTo(field2));
 }

}