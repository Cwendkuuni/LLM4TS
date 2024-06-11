import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationFieldType;
import org.joda.time.LocalDateTime;
import org.joda.time.MutableDateTime;
import org.joda.time.Period;
import org.joda.time.DateTimeConstants;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class LenientDateTimeFieldTest {

 private static final Chronology UTC = DateTimeConstants.UTC;

 @Test
 public void testLenientDateTimeField() {
 DateTimeField lenientField = LenientDateTimeField.getInstance(DateTimeFieldType.dayOfMonth(), UTC);
 MutableDateTime date = new MutableDateTime(2022, 1, 31, UTC);

 // Test setting a valid value
 int validValue = DateTimeConstants.DEC_FIFTH;
 long result = lenientField.set(date.getMillis(), validValue);
 assertEquals(DateTimeConstants.DEC_FIFTH, lenientField.get(result));

 // Test setting a value out of bounds (negative)
 int outOfBoundsNegativeValue = DateTimeConstants.DEC_SECOND_OF_DAY - 1;
 long resultOutOfBoundsNegative = lenientField.set(date.getMillis(), outOfBoundsNegativeValue);
 assertEquals(DateTimeConstants.DEC_FIFTH - 1, lenientField.get(resultOutOfBoundsNegative));

 // Test setting a value out of bounds (positive)
 int outOfBoundsPositiveValue = DateTimeConstants.DEC_SECOND_OF_DAY + 1;
 long resultOutOfBoundsPositive = lenientField.set(date.getMillis(), outOfBoundsPositiveValue);
 assertEquals(DateTimeConstants.DEC_SIXTH, lenientField.get(resultOutOfBoundsPositive));

 // Test setting the same value
 int sameValue = DateTimeConstants.DEC_FIFTH;
 long resultSameValue = lenientField.set(result, sameValue);
 assertEquals(sameValue, lenientField.get(resultSameValue));

 // Test setting a null DateTime
 assertNull(lenientField.set(null, validValue));

 // Test setting a DateTime with a null field
 MutableDateTime nullDateTime = new MutableDateTime(null, UTC);
 assertNull(lenientField.set(nullDateTime.getMillis(), validValue));

 // Test setting a DateTime with an invalid field type
 DateTimeField invalidField = DateTimeFieldType.hourOfDay();
 assertNull(LenientDateTimeField.getInstance(invalidField, UTC).set(date.getMillis(), validValue));

 // Test setting a DateTime with an invalid value
 int invalidValue = 100;
 assertNull(lenientField.set(date.getMillis(), invalidValue));

 // Test adding a Period
 Period period = new Period(1, 2, 3);
 lenientField.set(date.getMillis(), validValue);
 date.add(period);
 long resultAddPeriod = lenientField.set(date.getMillis(), validValue);
 assertEquals(DateTimeConstants.DEC_SEVENTH, lenientField.get(resultAddPeriod));

 // Test adding a Duration
 long duration = DurationFieldType.millisPerMinute.getIntegralValue(30);
 lenientField.set(date.getMillis(), validValue);
 date.add(DurationFieldType.standardMinutes(1));
 long resultAddDuration = lenientField.set(date.getMillis(), validValue);
 assertEquals(DateTimeConstants.DEC_SEVENTH, lenientField.get(resultAddDuration));
 }
}