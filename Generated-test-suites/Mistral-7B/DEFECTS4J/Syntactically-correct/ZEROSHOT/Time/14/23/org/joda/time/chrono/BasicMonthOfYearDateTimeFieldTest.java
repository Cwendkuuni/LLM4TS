import org.joda.time.BaseDateTime;
import org.joda.time.BasicDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DurationField;
import org.joda.time.ImpreciseDateTimeField;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.field.FieldUtils;
import org.joda.time.field.MonthAdjustable;
import org.joda.time.field.MonthAdjustableField;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testBasicMonthOfYearDateTimeField() {
        Chronology gregorianChronology = DateTimeConstants.GREGORIAN_CHRONOLOGY;
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(gregorianChronology, DateTimeConstants.JANUARY);

        // Test isLenient()
        assertFalse(field.isLenient());

        // Test get()
        long instant = DateTimeConstants.MINUTE_OF_DAY;
        int expectedMonth = DateTimeConstants.JANUARY;
        int actualMonth = field.get(instant);
        assertEquals(expectedMonth, actualMonth);

        // Test add()
        long newInstant = field.add(instant, 1);
        int expectedNewMonth = DateTimeConstants.FEBRUARY;
        int actualNewMonth = field.get(newInstant);
        assertEquals(expectedNewMonth, actualNewMonth);

        // Test addWrapField()
        long wrappedInstant = field.addWrapField(instant, 12);
        int expectedWrappedMonth = DateTimeConstants.DECEMBER;
        int actualWrappedMonth = field.get(wrappedInstant);
        assertEquals(expectedWrappedMonth, actualWrappedMonth);

        // Test getDifferenceAsLong()
        long laterInstant = DateTimeConstants.MILLIS_PER_DAY * 365 + instant;
        long difference = field.getDifferenceAsLong(instant, laterInstant);
        int expectedDifference = 1;
        assertEquals(expectedDifference, difference);

        // Test set()
        long setInstant = field.set(instant, DateTimeConstants.DECEMBER);
        int expectedSetMonth = DateTimeConstants.DECEMBER;
        int actualSetMonth = field.get(setInstant);
        assertEquals(expectedSetMonth, actualSetMonth);

        // Test getRangeDurationField()
        DurationField rangeDurationField = field.getRangeDurationField();
        int expectedRangeDuration = DateTimeConstants.MAX_YEARS;
        int actualRangeDuration = rangeDurationField.getValues()[0];
        assertEquals(expectedRangeDuration, actualRangeDuration);

        // Test isLeap()
        long leapInstant = DateTimeConstants.MILLIS_PER_DAY * 365 + DateTimeConstants.JANUARY_31_2000;
        assertTrue(field.isLeap(leapInstant));

        // Test getLeapAmount()
        long leapYearInstant = DateTimeConstants.MILLIS_PER_DAY * 366 + DateTimeConstants.JANUARY_31_2000;
        int expectedLeapAmount = 1;
        int actualLeapAmount = field.getLeapAmount(leapYearInstant);
        assertEquals(expectedLeapAmount, actualLeapAmount);

        // Test getLeapDurationField()
        DurationField leapDurationField = field.getLeapDurationField();
        int expectedLeapDuration = DateTimeConstants.DAYS_PER_MONTH;
        int actualLeapDuration = leapDurationField.getValues()[0];
        assertEquals(expectedLeapDuration, actualLeapDuration);

        // Test getMinimumValue()
        int expectedMinimumValue = DateTimeConstants.JANUARY;
        int actualMinimumValue = field.getMinimumValue();
        assertEquals(expectedMinimumValue, actualMinimumValue);

        // Test getMaximumValue()
        int expectedMaximumValue = DateTimeConstants.DECEMBER;
        int actualMaximumValue = field.getMaximumValue();
        assertEquals(expectedMaximumValue, actualMaximumValue);

        // Test roundFloor()
        long roundFloorInstant = DateTimeConstants.MILLIS_PER_DAY * 315 + DateTimeConstants.JANUARY_31_2000;
        long expectedRoundFloor = DateTimeConstants.getMillis(2001, 1, 31);
        long actualRoundFloor = field.roundFloor(roundFloorInstant);
        assertEquals(expectedRoundFloor, actualRoundFloor);

        // Test remainder()
        long remainderInstant = DateTimeConstants.MILLIS_PER_DAY * 316;
        long expectedRemainder = DateTimeConstants.MILLIS_PER_DAY;
        long actualRemainder = field.remainder(remainderInstant);
        assertEquals(expectedRemainder, actualRemainder);

        // Test serialization
        BasicMonthOfYearDateTimeField serializedField = (BasicMonthOfYearDateTimeField) field.readResolve();
        assertEquals(field, serializedField);

        // Test add(ReadablePartial, int[], int, int)
        LocalDateTime partial = new LocalDateTime(2001, 1, 31, 12, 0, 0);
        int[] values = new int[]{1, 1};
        int valueToAdd = 1;
        LocalDateTime result = new LocalDateTime();
        LocalDateTime expectedResult = new LocalDateTime(2002, 1, 31, 12, 0, 0);
        result = field.add(partial, 0, values, valueToAdd);
        assertEquals(expectedResult, result);

        // Test addWrapField(ReadablePartial, int, int)
        LocalDateTime wrappedPartial = new LocalDateTime(2001, 1, 31, 12, 0, 0);
        int wrappedFieldIndex = 0;
        int wrappedValueToAdd = 12;
        LocalDateTime wrappedResult = new LocalDateTime();
        LocalDateTime expectedWrappedResult = new LocalDateTime(2002, 1, 31, 12, 0, 0);
        wrappedResult = field.add(wrappedPartial, wrappedFieldIndex, values, wrappedValueToAdd);
        assertEquals(expectedWrappedResult, wrappedResult);
    }
}