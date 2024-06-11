import org.joda.time.Chronology;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationFieldType;
import org.joda.time.FieldUtils;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalDateTimeFieldVerifier;
import org.joda.time.DateTimeField;
import org.joda.time.chrono.JulianDate;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class LenientDateTimeFieldTest {

 private static final Chronology UTC = JulianDate.UTC;

 @Test
 public void testLenientDateTimeField() {
 DateTimeField field = LenientDateTimeField.getInstance(DateTimeFieldType.dayOfMonth(), UTC);

 // test setting within bounds
 LocalDateTime dateTime = new LocalDateTime(UTC, 2022, 2, 1, 0, 0);
 long value = 1;
 long newInstant = field.set(dateTime.toLocalMillis(), value);
 LocalDateTime expectedDateTime = new LocalDateTime(UTC, 2022, 2, 1, 0, 0).plusDays(value);
 LocalDateTime resultDateTime = new LocalDateTime(newJodaUtil.longToLocalMillis(newInstant));
 LocalDateTimeFieldVerifier.verify(expectedDateTime, resultDateTime);

 // test setting out of bounds
 value = 32;
 newInstant = field.set(dateTime.toLocalMillis(), value);
 expectedDateTime = new LocalDateTime(UTC, 2022, 2, 1, 0, 0).plusDays(value - DateTimeConstants.DAYS_PER_MONTH);
 LocalDateTime resultDateTimeOutOfBounds = new LocalDateTime(newJodaUtil.longToLocalMillis(newInstant));
 LocalDateTimeFieldVerifier.verify(expectedDateTime, resultDateTimeOutOfBounds);

 // test setting negative value
 value = -1;
 newInstant = field.set(dateTime.toLocalMillis(), value);
 expectedDateTime = new LocalDateTime(UTC, 2022, 2, 1, 0, 0).minusDays(value);
 LocalDateTime resultDateTimeNegative = new LocalDateTime(newJodaUtil.longToLocalMillis(newInstant));
 LocalDateTimeFieldVerifier.verify(expectedDateTime, resultDateTimeNegative);

 // test setting max value for month
 dateTime = new LocalDateTime(UTC, 2022, 2, 28, 0, 0);
 LocalDateTime expectedDateTimeMax = new LocalDateTime(UTC, 2022, 2, 29, 0, 0);
 newInstant = field.set(dateTime.toLocalMillis(), DateTimeConstants.MAX_VALUE_FOR_DAY_OF_MONTH);
 LocalDateTime resultDateTimeMax = new LocalDateTime(newJodaUtil.longToLocalMillis(newInstant));
 LocalDateTimeFieldVerifier.verify(expectedDateTimeMax, resultDateTimeMax);

 // test setting max value for year
 dateTime = new LocalDateTime(UTC, 2022, 12, 1, 0, 0);
 LocalDateTime expectedDateTimeMaxYear = new LocalDateTime(UTC, 2100, 12, 1, 0, 0);
 newInstant = field.set(dateTime.toLocalMillis(), DateTimeConstants.MAX_VALUE_FOR_YEAR);
 LocalDateTime resultDateTimeMaxYear = new LocalDateTime(newJodaUtil.longToLocalMillis(newInstant));
 LocalDateTimeFieldVerifier.verify(expectedDateTimeMaxYear, resultDateTimeMaxYear);

 // test adding a duration
 LocalDateTime baseDateTime = new LocalDateTime(UTC, 2022, 2, 1, 0, 0);
 DurationFieldType durFieldType = DurationFieldType.standardHours();
 long duration = 24;
 long newInstantWithDuration = field.set(baseDateTime.toLocalMillis(), value) + durFieldType.getStandardMillis(duration);
 LocalDateTime expectedDateTimeWithDuration = new LocalDateTime(baseDateTime).plusHours(duration);
 LocalDateTime resultDateTimeWithDuration = new LocalDateTime(newJodaUtil.longToLocalMillis(newInstantWithDuration));
 LocalDateTimeFieldVerifier.verify(expectedDateTimeWithDuration, resultDateTimeWithDuration);
 }
}