import org.junit.Test;
import org.joda.time.DurationFieldType;
import org.joda.time.DurationField;
import org.joda.time.DateTimeConstants;
import org.joda.time.Duration;
import static org.junit.Assert.assertThrows;

public class UnsupportedDurationFieldTest {

 private static final DurationFieldType DAYS = DurationFieldType.days();
 private static final DurationFieldType HOURS = DurationFieldType.hours();
 private static final DurationFieldType MINUTES = DurationFieldType.minutes();
 private static final DurationFieldType SECONDS = DurationFieldType.seconds();
 private static final DurationFieldType MILLISECONDS = DurationFieldType.millis();

 @Test
 public void testGetInstance() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);
 UnsupportedDurationField unsupportedHours = UnsupportedDurationField.getInstance(HOURS);
 UnsupportedDurationField unsupportedMinutes = UnsupportedDurationField.getInstance(MINUTES);
 UnsupportedDurationField unsupportedSeconds = UnsupportedDurationField.getInstance(SECONDS);
 UnsupportedDurationField unsupportedMillis = UnsupportedDurationField.getInstance(MILLISECONDS);

 assert (unsupportedDays != null);
 assert (unsupportedHours != null);
 assert (unsupportedMinutes != null);
 assert (unsupportedSeconds != null);
 assert (unsupportedMillis != null);
 }

 @Test
 public void testGetType() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);
 UnsupportedDurationField unsupportedHours = UnsupportedDurationField.getInstance(HOURS);
 UnsupportedDurationField unsupportedMinutes = UnsupportedDurationField.getInstance(MINUTES);
 UnsupportedDurationField unsupportedSeconds = UnsupportedDurationField.getInstance(SECONDS);
 UnsupportedDurationField unsupportedMillis = UnsupportedDurationField.getInstance(MILLISECONDS);

 assertEquals(DAYS, unsupportedDays.getType());
 assertEquals(HOURS, unsupportedHours.getType());
 assertEquals(MINUTES, unsupportedMinutes.getType());
 assertEquals(SECONDS, unsupportedSeconds.getType());
 assertEquals(MILLISECONDS, unsupportedMillis.getType());
 }

 @Test
 public void testIsSupported() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);
 UnsupportedDurationField unsupportedHours = UnsupportedDurationField.getInstance(HOURS);
 UnsupportedDurationField unsupportedMinutes = UnsupportedDurationField.getInstance(MINUTES);
 UnsupportedDurationField unsupportedSeconds = UnsupportedDurationField.getInstance(SECONDS);
 UnsupportedDurationField unsupportedMillis = UnsupportedDurationField.getInstance(MILLISECONDS);

 assertFalse(unsupportedDays.isSupported());
 assertFalse(unsupportedHours.isSupported());
 assertFalse(unsupportedMinutes.isSupported());
 assertFalse(unsupportedSeconds.isSupported());
 assertFalse(unsupportedMillis.isSupported());
 }

 @Test
 public void testIsPrecise() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);
 UnsupportedDurationField unsupportedHours = UnsupportedDurationField.getInstance(HOURS);
 UnsupportedDurationField unsupportedMinutes = UnsupportedDurationField.getInstance(MINUTES);
 UnsupportedDurationField unsupportedSeconds = UnsupportedDurationField.getInstance(SECONDS);
 UnsupportedDurationField unsupportedMillis = UnsupportedDurationField.getInstance(MILLISECONDS);

 assertTrue(unsupportedDays.isPrecise());
 assertTrue(unsupportedHours.isPrecise());
 assertTrue(unsupportedMinutes.isPrecise());
 assertTrue(unsupportedSeconds.isPrecise());
 assertTrue(unsupportedMillis.isPrecise());
 }

 @Test
 public void testGetValue() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);
 UnsupportedDurationField unsupportedHours = UnsupportedDurationField.getInstance(HOURS);
 UnsupportedDurationField unsupportedMinutes = UnsupportedDurationField.getInstance(MINUTES);
 UnsupportedDurationField unsupportedSeconds = UnsupportedDurationField.getInstance(SECONDS);
 UnsupportedDurationField unsupportedMillis = UnsupportedDurationField.getInstance(MILLISECONDS);

 assertThrows(UnsupportedOperationException.class, () -> unsupportedDays.getValue(1L));
 assertThrows(UnsupportedOperationException.class, () -> unsupportedHours.getValue(1));
 assertThrows(UnsupportedOperationException.class, () -> unsupportedMinutes.getValue(1));
 assertThrows(UnsupportedOperationException.class, () -> unsupportedSeconds.getValue(1));
 assertThrows(UnsupportedOperationException.class, () -> unsupportedMillis.getValue(1));
 }

 @Test
 public void testGetValueAsLong() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);
 UnsupportedDurationField unsupportedHours = UnsupportedDurationField.getInstance(HOURS);
 UnsupportedDurationField unsupportedMinutes = UnsupportedDurationField.getInstance(MINUTES);
 UnsupportedDurationField unsupportedSeconds = UnsupportedDurationField.getInstance(SECONDS);
 UnsupportedDurationField unsupportedMillis = UnsupportedDurationField.getInstance(MILLISECONDS);

 assertThrows(UnsupportedOperationException.class, () -> unsupportedDays.getValueAsLong(1L));
 assertThrows(UnsupportedOperationException.class, () -> unsupportedHours.getValueAsLong(1));
 assertThrows(UnsupportedOperationException.class, () -> unsupportedMinutes.getValueAsLong(1));
 assertThrows(UnsupportedOperationException.class, () -> unsupportedSeconds.getValueAsLong(1));
 assertThrows(UnsupportedOperationException.class, () -> unsupportedMillis.getValueAsLong(1));
 }

 @Test
 public void testGetValueAndGetValueAsLong() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);
 UnsupportedDurationField unsupportedHours = UnsupportedDurationField.getInstance(HOURS);
 UnsupportedDurationField unsupportedMinutes = UnsupportedDurationField.getInstance(MINUTES);
 UnsupportedDurationField unsupportedSeconds = UnsupportedDurationField.getInstance(SECONDS);
 UnsupportedDurationField unsupportedMillis = UnsupportedDurationField.getInstance(MILLISECONDS);

 assertThrows(UnsupportedOperationException.class, () -> unsupportedDays.getValue(1L));
 assertThrows(UnsupportedOperationException.class, () -> unsupportedDays.getValueAsLong(1L));
 assertThrows(UnsupportedOperationException.class, () -> unsupportedHours.getValue(1));
 assertThrows(UnsupportedOperationException.class, () -> unsupportedHours.getValueAsLong(1));
 assertThrows(UnsupportedOperationException.class, () -> unsupportedMinutes.getValue(1));
 assertThrows(UnsupportedOperationException.class, () -> unsupportedMinutes.getValueAsLong(1));
 assertThrows(UnsupportedOperationException.class, () -> unsupportedSeconds.getValue(1));
 assertThrows(UnsupportedOperationException.class, () -> unsupportedSeconds.getValueAsLong(1));
 assertThrows(UnsupportedOperationException.class, () -> unsupportedMillis.getValue(1));
 assertThrows(UnsupportedOperationException.class, () -> unsupportedMillis.getValueAsLong(1));
 }

 @Test
 public void testGetMillis() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);
 UnsupportedDurationField unsupportedHours = UnsupportedDurationField.getInstance(HOURS);
 UnsupportedDurationField unsupportedMinutes = UnsupportedDurationField.getInstance(MINUTES);
 UnsupportedDurationField unsupportedSeconds = UnsupportedDurationField.getInstance(SECONDS);
 UnsupportedDurationField unsupportedMillis = UnsupportedDurationField.getInstance(MILLISECONDS);

 assertThrows(UnsupportedOperationException.class, () -> unsupportedDays.getMillis(1));
 assertThrows(UnsupportedOperationException.class, () -> unsupportedHours.getMillis(1));
 assertThrows(UnsupportedOperationException.class, () -> unsupportedMinutes.getMillis(1));
 assertThrows(UnsupportedOperationException.class, () -> unsupportedSeconds.getMillis(1));
 assertThrows(UnsupportedOperationException.class, () -> unsupportedMillis.getMillis(1));
 }

 @Test
 public void testCompareTo() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);
 UnsupportedDurationField unsupportedHours = UnsupportedDurationField.getInstance(HOURS);
 UnsupportedDurationField unsupportedMinutes = UnsupportedDurationField.getInstance(MINUTES);
 UnsupportedDurationField unsupportedSeconds = UnsupportedDurationField.getInstance(SECONDS);
 UnsupportedDurationField unsupportedMillis = UnsupportedDurationField.getInstance(MILLISECONDS);
 DurationField standardDays = DurationField.standardMillis();
 DurationField standardHours = DurationField.standardHours();
 DurationField standardMinutes = DurationField.standardMinutes();
 DurationField standardSeconds = DurationField.standardSeconds();
 DurationField standardMillis = DurationField.standardMillis();

 assertEquals(0, unsupportedDays.compareTo(unsupportedDays));
 assertEquals(0, unsupportedHours.compareTo(unsupportedHours));
 assertEquals(0, unsupportedMinutes.compareTo(unsupportedMinutes));
 assertEquals(0, unsupportedSeconds.compareTo(unsupportedSeconds));
 assertEquals(0, unsupportedMillis.compareTo(unsupportedMillis));
 assertEquals(0, unsupportedDays.compareTo(standardDays));
 assertEquals(0, unsupportedHours.compareTo(standardHours));
 assertEquals(0, unsupportedMinutes.compareTo(standardMinutes));
 assertEquals(0, unsupportedSeconds.compareTo(standardSeconds));
 assertEquals(0, unsupportedMillis.compareTo(standardMillis));
 }
}