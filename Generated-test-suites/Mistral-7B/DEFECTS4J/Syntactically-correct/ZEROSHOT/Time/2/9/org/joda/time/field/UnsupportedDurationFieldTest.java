import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class UnsupportedDurationFieldTest {

 private static final DurationFieldType DAYS = DurationFieldType.dayOfMonth();
 private static final DurationFieldType HOURS = DurationFieldType.hourOfDay();
 private static final DurationFieldType MINUTES = DurationFieldType.minute();
 private static final DurationFieldType SECONDS = DurationFieldType.second();
 private static final DurationFieldType MILLISECONDS = DurationFieldType.millis();

 @Test
 public void testGetInstance() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DurationFieldType.dayOfMonth());
 UnsupportedDurationField unsupportedHours = UnsupportedDurationField.getInstance(DurationFieldType.hourOfDay());
 UnsupportedDurationField unsupportedMinutes = UnsupportedDurationField.getInstance(DurationFieldType.minute());
 UnsupportedDurationField unsupportedSeconds = UnsupportedDurationField.getInstance(DurationFieldType.second());
 UnsupportedDurationField unsupportedMillis = UnsupportedDurationField.getInstance(DurationFieldType.millis());

 assertEquals(DurationFieldType.dayOfMonth(), unsupportedDays.getType());
 assertEquals(DurationFieldType.hourOfDay(), unsupportedHours.getType());
 assertEquals(DurationFieldType.minute(), unsupportedMinutes.getType());
 assertEquals(DurationFieldType.second(), unsupportedSeconds.getType());
 assertEquals(DurationFieldType.millis(), unsupportedMillis.getType());
 }

 @Test
 public void testIsSupported() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DurationFieldType.minute());
 assertEquals(false, unsupportedField.isSupported());
 }

 @Test
 public void testIsPrecise() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DurationFieldType.minute());
 assertEquals(true, unsupportedField.isPrecise());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueInt() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DurationFieldType.minute());
 unsupportedField.getValue(1);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueLong() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DurationFieldType.minute());
 unsupportedField.getValueAsLong(1);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueIntLong() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DurationFieldType.minute());
 unsupportedField.getValue(1, 1);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueLongLong() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DurationFieldType.minute());
 unsupportedField.getValueAsLong(1, 1);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisInt() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DurationFieldType.minute());
 unsupportedField.getMillis(1);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisLong() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DurationFieldType.minute());
 unsupportedField.getMillis(1L);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisIntLong() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DurationFieldType.minute());
 unsupportedField.getMillis(1, 1);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisLongLong() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DurationFieldType.minute());
 unsupportedField.getMillis(1L, 1L);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddLong() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DurationFieldType.minute());
 unsupportedField.add(1, 1);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddLongLong() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DurationFieldType.minute());unsupportedField.add(1, 1L); }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetDifferenceInt() {
 UnsupportedDurationField unsupportedField1 = new UnsupportedDurationField(DurationFieldType.minute());
 UnsupportedDurationField unsupportedField2 = new UnsupportedDurationField(DurationFieldType.minute());
 unsupportedField1.getDifference(1, 1);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetDifferenceLong() {
 UnsupportedDurationField unsupportedField1 = new UnsupportedDurationField(DurationFieldType.minute());
 UnsupportedDurationField unsupportedField2 = new UnsupportedDurationField(DurationFieldType.minute());
 unsupportedField1.getDifferenceAsLong(1, 1);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetUnitMillis() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DurationFieldType.minute());
 unsupportedField.getUnitMillis();
 }

 @Test
 public void testCompareTo() {
 UnsupportedDurationField unsupportedField1 = new UnsupportedDurationField(DurationFieldType.minute());
 UnsupportedDurationField unsupportedField2 = new UnsupportedDurationField(DurationFieldType.minute());
 assertEquals(0, unsupportedField1.compareTo(unsupportedField2));
 }

 @Test
 public void testEquals() {
 UnsupportedDurationField unsupportedField1 = new UnsupportedDurationField(DurationFieldType.minute());
 UnsupportedDurationField unsupportedField2 = new UnsupportedDurationField(DurationFieldType.minute());
 UnsupportedDurationField unsupportedField3 = new UnsupportedDurationField(DurationFieldType.hourOfDay());
 UnsupportedDurationField unsupportedField4 = new UnsupportedDurationField(DurationFieldType.minute());
 assertEquals(unsupportedField1, unsupportedField1);
 assertEquals(unsupportedField1, unsupportedField4);
 assertNotEquals(unsupportedField1, unsupportedField2);
 assertNotEquals(unsupportedField1, unsupportedField3);
 }

 @Test
 public void testHashCode() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DurationFieldType.minute());
 assertEquals(MINUTES.hashCode(), unsupportedField.hashCode());
 }

 @Test
 public void testToString() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DurationFieldType.minute());
 assertEquals("UnsupportedDurationField[minute]", unsupportedField.toString());
 }

 @Test
 public void testSingletonSerialization() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DurationFieldType.minute());
 UnsupportedDurationField deserialized = (UnsupportedDurationField) unsupportedField.readResolve();
 assertEquals(unsupportedField, deserialized);
 }
}