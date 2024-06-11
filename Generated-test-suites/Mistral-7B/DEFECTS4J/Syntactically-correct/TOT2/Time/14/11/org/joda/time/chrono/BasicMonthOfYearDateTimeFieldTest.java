import org.joda.time.*;
import org.joda.time.chrono.BasicMonthOfYearDateTimeField;
import org.joda.time.chrono.BasicChronology;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testBasicGetMethod() {
        BasicChronology chronology = new GregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2); // February

        // Test with valid input
        long instant = DateTimeConstants.MINUTE_OF_DAY;
        int expectedResult = 1;
        int result = field.get(instant);
        assertEquals(expectedResult, result);

        instant += DateTimeConstants.DAYS_PER_MONTH * 3; // March 3rd
        expectedResult = 3;
        result = field.get(instant);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testBasicAddMethod() {
        BasicChronology chronology = new GregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2); // February

        // Test with valid input
        long instant = DateTimeConstants.MINUTE_OF_DAY;
        int monthsToAdd = 2;
        long expectedResult = DateTimeConstants.DAYS_PER_MONTH * 2 + DateTimeConstants.MINUTE_OF_DAY;
        long result = field.add(instant, monthsToAdd);
        assertEquals(expectedResult, result);

        // Test with negative months
        monthsToAdd = -2;
        expectedResult = DateTimeConstants.MINUTE_OF_DAY;
        result = field.add(instant, monthsToAdd);
        assertEquals(expectedResult, result);

        // Test with the maximum number of months
        monthsToAdd = 12;
        expectedResult += DateTimeConstants.DAYS_PER_MONTH * 12;
        result = field.add(instant, monthsToAdd);
        assertEquals(expectedResult, result);

        // Test with maximum + 1 month
        monthsToAdd++;
        assertThrows(IllegalArgumentException.class, () -> field.add(instant, monthsToAdd));
    }

    @Test
    public void testEdgeCasesAndExceptions() {
        BasicChronology chronology = new GregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2); // February

        // Test with minimum year
        long instant = DateTimeConstants.MILLIS_PER_DAY * DateTimeConstants.DAYS_PER_YEAR * DateTimeConstants.MIN_YEAR;
        int monthsToAdd = 1;
        long expectedResult = DateTimeConstants.MILLIS_PER_DAY * DateTimeConstants.DAYS_PER_YEAR * DateTimeConstants.MIN_YEAR +
                DateTimeConstants.DAYS_PER_MONTH;
        long result = field.add(instant, monthsToAdd);
        assertEquals(expectedResult, result);

        // Test with maximum year
        instant = DateTimeConstants.MILLIS_PER_DAY * DateTimeConstants.DAYS_PER_YEAR * DateTimeConstants.MAX_YEAR;
        monthsToAdd = 1;
        expectedResult += DateTimeConstants.DAYS_PER_MONTH * DateTimeConstants.MAX_MONTHS_PER_YEAR;
        result = field.add(instant, monthsToAdd);
        assertEquals(expectedResult, result);

        // Test with leap year
        chronology = new JulianLenientChronology();
        field = new BasicMonthOfYearDateTimeField(chronology, 2); // February
        instant = DateTimeConstants.MILLIS_PER_DAY * DateTimeConstants.DAYS_PER_YEAR * 400;
        monthsToAdd = 2;
        long expectedLeapYearResult = DateTimeConstants.MILLIS_PER_DAY * DateTimeConstants.DAYS_PER_YEAR * 400 +
                DateTimeConstants.DAYS_PER_MONTH * 2;
        result = field.add(instant, monthsToAdd);
        assertEquals(expectedLeapYearResult, result);

        // Test with non-leap year
        instant = DateTimeConstants.MILLIS_PER_DAY * DateTimeConstants.DAYS_PER_YEAR * 401;
        monthsToAdd = 2;
        long expectedNonLeapYearResult = DateTimeConstants.MILLIS_PER_DAY * DateTimeConstants.DAYS_PER_YEAR * 401 +
                DateTimeConstants.DAYS_PER_MONTH * 2 - 1;
        result = field.add(instant, monthsToAdd);
        assertEquals(expectedNonLeapYearResult, result);
    }
}