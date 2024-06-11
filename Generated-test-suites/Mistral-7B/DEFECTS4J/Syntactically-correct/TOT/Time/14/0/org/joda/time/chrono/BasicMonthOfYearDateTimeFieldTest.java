import org.joda.time.BasicChronology;
import org.joda.time. ChronologyUtils;
import org.joda.time.DateTimeConstants;
import org.joda.time.DurationField;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalDateTimeFieldVerifier;
import org.joda.time.Months;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testBasicMonthOfYearDateTimeField() {
        // Create a Gregorian chronology with January as leap month
        BasicChronology chronology = new BasicGregorianChronology(ChronologyUtils.UTC, new BasicGregorianLeapYearRules(DateTimeConstants.JANUARY));
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, DateTimeConstants.JANUARY);

        // Test get methods
        assertEquals(DateTimeConstants.JANUARY, field.get(0L));
        assertEquals(DateTimeConstants.JANUARY, field.get(DateTimeConstants.MINUTE_OF_DAY.getMillis(0)));
        assertEquals(DateTimeConstants.JANUARY, field.get(DateTimeConstants.HOUR_OF_DAY.getMillis(0, DateTimeConstants.MILLIS_PER_HOUR)));
        assertEquals(DateTimeConstants.JANUARY, field.get(DateTimeConstants.DAY_OF_MONTH.getMillis(1, DateTimeConstants.JANUARY_31)));
        assertEquals(DateTimeConstants.JANUARY, field.get(DateTimeConstants.DAY_OF_YEAR.getMillis(1, DateTimeConstants.JANUARY_31, DateTimeConstants.GREGORIAN)));

        // Test set methods
        long testSet = LocalDateTime.parse("2015-02-29T12:30:00.000Z").toMillisecond();
        assertEquals(DateTimeConstants.FEBRUARY, field.set(testSet, DateTimeConstants.FEBRUARY + 1));
        assertEquals(DateTimeConstants.MARCH, field.set(testSet, DateTimeConstants.MARCH - 1));
        assertEquals(DateTimeConstants.JANUARY, field.set(testSet, DateTimeConstants.JANUARY));

        // Test add methods
        long testAdd = LocalDateTime.parse("2015-02-29T12:30:00.000Z").toMillisecond();
        assertEquals(DateTimeConstants.MARCH, field.add(testAdd, 1));
        assertEquals(DateTimeConstants.JANUARY, field.add(testAdd, -13));

        // Test add wrap field method
        assertEquals(DateTimeConstants.DECEMBER, field.addWrapField(testAdd, 13));
        assertEquals(DateTimeConstants.JANUARY, field.addWrapField(testAdd, -13));

        // Test getDifferenceAsLong method
        long testGetDifference = LocalDateTime.parse("2015-03-31T12:30:00.000Z").toMillisecond();
        assertEquals(1, field.getDifferenceAsLong(testAdd, testGetDifference));

        // Test set method with invalid month
        try {
            field.set(testAdd, 14);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test add method with invalid amount
        try {
            field.add(testAdd, 14);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test add method with large amount
        long testAddLarge = LocalDateTime.parse("1970-01-01T12:30:00.000Z").toMillisecond();
        try {
            field.add(testAddLarge, 10000000000L);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test addWrapField method with large amount
        try {
            field.addWrapField(testAddLarge, 10000000000L);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test getRangeDurationField method
        DurationField range = field.getRangeDurationField();
        assertEquals(Months.getSingleton(1), range);

        // Test isLeap method
        assertEquals(true, field.isLeap(testAdd));
        assertEquals(false, field.isLeap(testAdd + 1));

        // Test getLeapAmount method
        assertEquals(0, field.getLeapAmount(testAdd));
        assertEquals(1, field.getLeapAmount(testAdd + 1));

        // Test getLeapDurationField method
        DurationField leap = field.getLeapDurationField();
        assertEquals(DurationField.days(1), leap);
    }
}