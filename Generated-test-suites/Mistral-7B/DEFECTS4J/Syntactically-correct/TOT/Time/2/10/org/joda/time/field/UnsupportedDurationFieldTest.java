import org.junit.Test;
import org.joda.time.DurationFieldType;
import org.joda.time.DurationField;
import org.joda.time.DateTimeConstants;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class UnsupportedDurationFieldTest {

 private static final DurationFieldType DAYS = DurationFieldType.days();
 private static final DurationFieldType MINUTES = DurationFieldType.minutes();
 private static final DurationFieldType SECONDS = DurationFieldType.seconds();

 @Test
 public void testGetInstance() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);
 UnsupportedDurationField unsupportedMinutes = UnsupportedDurationField.getInstance(MINUTES);
 UnsupportedDurationField unsupportedSeconds = UnsupportedDurationField.getInstance(SECONDS);

 assertEquals(DAYS, unsupportedDays.getType());
 assertEquals(MINUTES, unsupportedMinutes.getType());
 assertEquals(SECONDS, unsupportedSeconds.getType());
 }

 @Test
 public void testIsSupported() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DAYS);

 assertEquals(false, unsupportedField.isSupported());
 }

 @Test
 public void testIsPrecise() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DAYS);

 assertEquals(true, unsupportedField.isPrecise());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValue() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DAYS);

 unsupportedField.getValue(DateTimeConstants.MILLIS_PER_DAY);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAsLong() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DAYS);

 unsupportedField.getValueAsLong(DateTimeConstants.MILLIS_PER_DAY);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueWithInstant() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DAYS);

 unsupportedField.getValue(DateTimeConstants.MILLIS_PER_DAY, DateTimeConstants.MILLIS_PER_DAY);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAsLongWithInstant() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DAYS);

 unsupportedField.getValueAsLong(DateTimeConstants.MILLIS_PER_DAY, DateTimeConstants.MILLIS_PER_DAY);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillis() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DAYS);

 unsupportedField.getMillis(1);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisWithValue() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DAYS);

 unsupportedField.getMillis(DateTimeConstants.DAYS_PER_YEAR);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAdd() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DAYS);

 unsupportedField.add(DateTimeConstants.MILLIS_PER_DAY, 1);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddWithInstant() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DAYS);

 unsupportedField.add(DateTimeConstants.MILLIS_PER_DAY, DateTimeConstants.MILLIS_PER_DAY);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetDifference() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DAYS);

 unsupportedField.getDifference(DateTimeConstants.MILLIS_PER_DAY, DateTimeConstants.MILLIS_PER_DAY);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetDifferenceAsLong() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DAYS);

 unsupportedField.getDifferenceAsLong(DateTimeConstants.MILLIS_PER_DAY, DateTimeConstants.MILLIS_PER_DAY);
 }

 @Test
 public void testGetUnitMillis() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DAYS);

 assertEquals(0, unsupportedField.getUnitMillis());
 }

 @Test
 public void testCompareTo() {
 UnsupportedDurationField unsupportedField1 = UnsupportedDurationField.getInstance(DAYS);
 UnsupportedDurationField unsupportedField2 = UnsupportedDurationField.getInstance(DAYS);

 assertEquals(0, unsupportedField1.compareTo(unsupportedField2));
 }
}