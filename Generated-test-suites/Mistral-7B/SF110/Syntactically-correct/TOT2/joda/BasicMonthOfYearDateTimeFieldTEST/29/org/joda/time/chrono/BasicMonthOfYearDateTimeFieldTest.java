import org.joda.time.*;
import org.junit.*;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {
    @Test
    public void testIsLenient() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ChronologyUtils.ISO_8601_DATE, 2);
        assertFalse(field.isLenient());
    }

    @Test
    public void testGet(long n) {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ChronologyUtils.ISO_8601_DATE, 2);
        assertEquals(1, field.get(DateTimeConstants.MINUTE_OF_JANUARY));
        assertEquals(12, field.get(DateTimeConstants.DECEMBER));
    }

    @Test
    public void testAdd() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ChronologyUtils.ISO_8601_DATE, 2);
        assertEquals(DateTimeConstants.JANUARY + 1, field.add(DateTimeConstants.MINUTE_OF_JANUARY, 1));
        assertEquals(DateTimeConstants.DECEMBER - 11, field.add(DateTimeConstants.DECEMBER, -11));
        assertEquals(DateTimeConstants.FEBRUARY, field.add(DateTimeConstants.JANUARY, 11));
        assertEquals(DateTimeConstants.JANUARY, field.add(DateTimeConstants.DECEMBER, -11));
    }

    @Test
    public void testAddLong() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ChronologyUtils.ISO_8601_DATE, 2);
        assertEquals(DateTimeConstants.JANUARY + 3651, field.add(DateTimeConstants.MINUTE_OF_JANUARY, 3651));
    }

    @Test
    public void testAddWrapField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ChronologyUtils.ISO_8601_DATE, 2);
        assertEquals(DateTimeConstants.JANUARY, field.addWrapField(DateTimeConstants.JANUARY + 13, 1));
    }

    @Test
    public void testGetDifferenceAsLong() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ChronologyUtils.ISO_8601_DATE, 2);
        long difference = field.getDifferenceAsLong(DateTimeConstants.MINUTE_OF_JANUARY, DateTimeConstants.FEBRUARY);
        assertEquals(30, difference);
    }

    @Test
    public void testSet() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ChronologyUtils.ISO_8601_DATE, 2);
        assertEquals(DateTimeConstants.JANUARY + 3, field.set(DateTimeConstants.MINUTE_OF_JANUARY, 3));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddExceptions() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ChronologyUtils.ISO_8601_DATE, 2);
        field.add(DateTimeConstants.MINUTE_OF_JANUARY, 365 * 2 + 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddWrapFieldExceptions() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ChronologyUtils.ISO_8601_DATE, 2);
        field.addWrapField(DateTimeConstants.JANUARY, 13 + 12);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetExceptions() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ChronologyUtils.ISO_8601_DATE, 2);
        field.set(DateTimeConstants.MINUTE_OF_JANUARY, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetExceptionsWithInvalidChronology() {
        try {
            new BasicMonthOfYearDateTimeField(null, 2);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddWithInvalidChronology() {
        try {
            new BasicMonthOfYearDateTimeField(ChronologyUtils.INVALID_UTC_DATE, 2);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}