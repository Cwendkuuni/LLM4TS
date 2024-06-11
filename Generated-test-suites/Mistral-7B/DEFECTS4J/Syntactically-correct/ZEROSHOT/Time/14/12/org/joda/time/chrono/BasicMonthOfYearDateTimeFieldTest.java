import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeConstants;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.field.FieldUtils;
import org.joda.time.field.ImpreciseDateTimeField;
import org.joda.time.chrono.BasicChronology;
import org.joda.time.chrono.ISOChronology;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testBasicMonthOfYearDateTimeField() {
        Chronology isoChronology = new ISOChronology();
        BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(isoChronology, DateTimeConstants.JANUARY);

        // Test basic functionality
        assertEquals(1, testField.get(0));
        assertEquals(2, testField.get(DateTimeConstants.MILLIS_PER_DAY * 31));
        assertEquals(12, testField.get(DateTimeConstants.MILLIS_PER_DAY * 365 + DateTimeConstants.MILLIS_PER_DAY * 30));
        assertEquals(-1, testField.get(DateTimeConstants.MILLIS_PER_DAY * 366 - DateTimeConstants.MILLIS_PER_DAY * 31));

        assertEquals(1, testField.get(testField.set(0)));
        assertEquals(3, testField.get(testField.set(DateTimeConstants.MILLIS_PER_DAY * 31 + DateTimeConstants.MILLIS_PER_DAY * 2)));
        assertEquals(12, testField.get(testField.set(DateTimeConstants.MILLIS_PER_DAY * 365 + DateTimeConstants.MILLIS_PER_DAY * 30 - DateTimeConstants.MILLIS_PER_DAY * 11)));
        assertEquals(-1, testField.get(testField.set(DateTimeConstants.MILLIS_PER_DAY * 366 + DateTimeConstants.MILLIS_PER_DAY * 30 - DateTimeConstants.MILLIS_PER_DAY * 12)));

        // Test add functionality
        assertEquals(DateTimeConstants.MILLIS_PER_DAY * 31, testField.add(0, 1));
        assertEquals(DateTimeConstants.MILLIS_PER_DAY * 365 + DateTimeConstants.MILLIS_PER_DAY * 30, testField.add(DateTimeConstants.MILLIS_PER_DAY * 365, 12));
        assertEquals(DateTimeConstants.MILLIS_PER_DAY * 365 + DateTimeConstants.MILLIS_PER_DAY * 29, testField.add(DateTimeConstants.MILLIS_PER_DAY * 365, 11, ChronologyUtils.ISO_CHRONOLOGY, DateTimeFieldType.monthOfYear()));
        assertEquals(DateTimeConstants.MILLIS_PER_DAY * 366 + DateTimeConstants.MILLIS_PER_DAY * 29, testField.add(DateTimeConstants.MILLIS_PER_DAY * 366, 11, ChronologyUtils.ISO_CHRONOLOGY, DateTimeFieldType.monthOfYear()));
        assertEquals(DateTimeConstants.MILLIS_PER_DAY * 365 + DateTimeConstants.MILLIS_PER_DAY * 28 + DateTimeConstants.MILLIS_PER_DAY, testField.add(DateTimeConstants.MILLIS_PER_DAY * 365, 12, ChronologyUtils.ISO_CHRONOLOGY, DateTimeFieldType.monthOfYear()));

        // Test add wrap functionality
        assertEquals(DateTimeConstants.MILLIS_PER_DAY * 365 + DateTimeConstants.MILLIS_PER_DAY * 12, testField.addWrapField(DateTimeConstants.MILLIS_PER_DAY * 365, 12));
        assertEquals(DateTimeConstants.MILLIS_PER_DAY * 365 + DateTimeConstants.MILLIS_PER_DAY * 11, testField.addWrapField(DateTimeConstants.MILLIS_PER_DAY * 365, 11));

        // Test getDifferenceAsLong functionality
        assertEquals(11, testField.getDifferenceAsLong(DateTimeConstants.MILLIS_PER_DAY * 365, DateTimeConstants.MILLIS_PER_DAY * 366));
        assertEquals(11, testField.getDifferenceAsLong(DateTimeConstants.MILLIS_PER_DAY * 365, DateTimeConstants.MILLIS_PER_DAY * 365 + DateTimeConstants.MILLIS_PER_DAY * 11));
        assertEquals(-11, testField.getDifferenceAsLong(DateTimeConstants.MILLIS_PER_DAY * 366, DateTimeConstants.MILLIS_PER_DAY * 365));
        assertEquals(-11, testField.getDifferenceAsLong(DateTimeConstants.MILLIS_PER_DAY * 366, DateTimeConstants.MILLIS_PER_DAY * 365 + DateTimeConstants.MILLIS_PER_DAY * 11));

        // Test set functionality
        assertEquals(DateTimeConstants.MILLIS_PER_DAY * 365 + DateTimeConstants.MILLIS_PER_DAY * 3, testField.set(DateTimeConstants.MILLIS_PER_DAY * 365, 3));
        assertEquals(DateTimeConstants.MILLIS_PER_DAY * 366 + DateTimeConstants.MILLIS_PER_DAY * 29, testField.set(DateTimeConstants.MILLIS_PER_DAY * 366, 2, ChronologyUtils.ISO_CHRONOLOGY, DateTimeFieldType.monthOfYear()));

        // Test getRangeDurationField functionality
        DurationField durationField = testField.getRangeDurationField();
        assertEquals(DurationFieldType.YEARS, durationField.getFieldType());
        assertEquals(1, durationField.getIntegralValues());
        assertEquals(0, durationField.getLongestIntegralValue());
        assertEquals(1, durationField.getShortestIntegralValue());
        assertEquals(1, durationField.getMillis(1));
        assertEquals(31557600000L, durationField.getMillis(10));

        // Test isLeap functionality
        assertTrue(testField.isLeap(DateTimeConstants.MILLIS_PER_DAY * 365 + DateTimeConstants.MILLIS_PER_DAY * 28 + DateTimeConstants.MILLIS_PER_DAY * 29));
        assertFalse(testField.isLeap(DateTimeConstants.MILLIS_PER_DAY * 365 + DateTimeConstants.MILLIS_PER_DAY * 28 + DateTimeConstants.MILLIS_PER_DAY * 28));

        // Test getLeapAmount functionality
        assertEquals(1, testField.getLeapAmount(DateTimeConstants.MILLIS_PER_DAY * 365 + DateTimeConstants.MILLIS_PER_DAY * 28 + DateTimeConstants.MILLIS_PER_DAY * 29));
        assertEquals(0, testField.getLeapAmount(DateTimeConstants.MILLIS_PER_DAY * 365 + DateTimeConstants.MILLIS_PER_DAY * 28 + DateTimeConstants.MILLIS_PER_DAY * 28));

        // Test getLeapDurationField functionality
        DurationField leapDurationField = testField.getLeapDurationField();
        assertEquals(DurationFieldType.DAYS, leapDurationField.getFieldType());
        assertEquals(1, leapDurationField.getIntegralValues());
        assertEquals(1, leapDurationField.getLongestIntegralValue());
        assertEquals(1, leapDurationField.getShortestIntegralValue());
        assertEquals(1, leapDurationField.getMillis(1));
        assertEquals(86400000L, leapDurationField.getMillis(10));

        // Test min and max values
        assertEquals(1, testField.getMinimumValue());
        assertEquals(12, testField.getMaximumValue());

        // Test roundFloor functionality
        assertEquals(DateTimeConstants.MILLIS_PER_DAY * 365, testField.roundFloor(DateTimeConstants.MILLIS_PER_DAY * 365));
        assertEquals(DateTimeConstants.MILLIS_PER_DAY * 365 + DateTimeConstants.MILLIS_PER_DAY * 30, testField.roundFloor(DateTimeConstants.MILLIS_PER_DAY * 365 + DateTimeConstants.MILLIS_PER_DAY * 30));

        // Test remainder functionality
        assertEquals(0, testField.remainder(DateTimeConstants.MILLIS_PER_DAY * 365));
        assertEquals(DateTimeConstants.MILLIS_PER_DAY, testField.remainder(DateTimeConstants.MILLIS_PER_DAY * 365 + DateTimeConstants.MILLIS_PER_DAY * 30));

        // Test serialization
        BaseDateTime testDateTime = new BaseDateTime(DateTimeConstants.MILLIS_PER_DAY * 365, ISOChronology.getInstanceUTC());
        testField = (BasicMonthOfYearDateTimeField) testField.readResolve();
        assertEquals(1, testField.get(testDateTime.getMillis()));
    }
}