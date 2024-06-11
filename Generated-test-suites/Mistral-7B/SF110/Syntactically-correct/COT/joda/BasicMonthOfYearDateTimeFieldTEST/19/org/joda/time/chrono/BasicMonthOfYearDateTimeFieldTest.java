import org.joda.time.BaseDateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.Chronology;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.Assert.*;

import static org.joda.time.DateTimeUtils.isContiguous;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testIsLenient() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(Chronology.getInstanceUTC(), 2);
        assertFalse(field.isLenient());
    }

    @Test
    public void testGet_valid() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(Chronology.getInstanceUTC(), 2);
        long input = new BaseDateTime(0, 2022, 2, 25, 0, 0, 0).getMillis();
        int expected = DateTimeConstants.FEBRUARY;
        int actual = field.get(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testGet_invalid() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(Chronology.getInstanceUTC(), 2);
        long input = new BaseDateTime(-1, 2022, 13, 25, 0, 0, 0).getMillis();
        try {
            field.get(input);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testAdd_valid() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(Chronology.getInstanceUTC(), 2);
        long input = new BaseDateTime(0, 2022, DateTimeConstants.FEBRUARY, 25, 0, 0, 0).getMillis();
        long expected = new BaseDateTime(0, 2022, DateTimeConstants.MARCH, 25, 0, 0, 0).getMillis();
        long actual = field.add(input, 1);
        assertEquals(expected, actual);
    }

    @Test
    public void testAdd_invalid_amount() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(Chronology.getInstanceUTC(), 2);
        long input = new BaseDateTime(0, 2022, DateTimeConstants.FEBRUARY, 25, 0, 0, 0).getMillis();
        try {
            field.add(input, Integer.MAX_VALUE + 1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testAdd_contiguous() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(Chronology.getInstanceUTC(), 2);
        LocalDate date1 = new LocalDate(2022, 2, 25);
        LocalDate date2 = new LocalDate(2022, 3, 15);
        long input = date1.toInstant().getEpochMilli();
        long[] expected = {date2.toInstant().getEpochMilli()};
        long[] actual = field.add(input, 10).getValues(DateTimeFieldType.millisOfDay());
        assertArrayEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAdd_nonContiguous() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(Chronology.getInstanceUTC(), 2);
        LocalDate date1 = new LocalDate(2022, 2, 25);
        LocalDate date2 = new LocalDate(2022, 4, 15);
        long input = date1.toInstant().getEpochMilli();
        field.add(input, 1).getValues(DateTimeFieldType.monthOfYear());
    }

    @Test
    public void testAddWrapField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(Chronology.getInstanceUTC(), 2);
        long input = new BaseDateTime(0, 2022, 13, 25, 0, 0, 0).getMillis();
        long expected = new BaseDateTime(0, 2022, 1, 25, 0, 0, 0).getMillis();
        long actual = field.addWrapField(input, 11);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetDifferenceAsLong() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(Chronology.getInstanceUTC(), 2);
        long date1 = new BaseDateTime(0, 2022, DateTimeConstants.FEBRUARY, 25, 0, 0, 0).getMillis();
        long date2 = new BaseDateTime(0, 2022, DateTimeConstants.MARCH, 15, 0, 0, 0).getMillis();
        long expected = 29;
        long actual = field.getDifferenceAsLong(date1, date2);
        assertEquals(expected, actual);
    }

    @Test
    public void testSet() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(Chronology.getInstanceUTC(), 2);
        long input = new BaseDateTime(0, 2022, DateTimeConstants.FEBRUARY, 25, 0, 0, 0).getMillis();
        int expected = DateTimeConstants.MARCH;
        long actual = field.set(input, 3);
        assertEquals(expected, field.get(actual));
    }

    @Test
    public void testSet_invalid() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(Chronology.getInstanceUTC(), 2);
        long input = new BaseDateTime(0, 2022, 2, 25, 0, 0, 0).getMillis();
        try {
            field.set(input, 13);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testGetMinimumValue() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(Chronology.getInstanceUTC(), 2);
        int expected = 1;
        int actual = field.getMinimumValue();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetMaximumValue() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(Chronology.getInstanceUTC(), 2);
        int expected = 12;
        int actual = field.getMaximumValue();
        assertEquals(expected, actual);
    }

    @Test
    public void testRoundFloor() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(Chronology.getInstanceUTC(), 2);
        long input = new BaseDateTime(0, 2022, 2, 26, 0, 0, 0).getMillis();
        long expected = new BaseDateTime(0, 2022, 2, 25, 0, 0, 0).getMillis();
        long actual = field.roundFloor(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testRemainder() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(Chronology.getInstanceUTC(), 2);
        long input = new BaseDateTime(0, 2022, 2, 26, 0, 0, 0).getMillis();
        long expected = 1;
        long actual = field.remainder(input);
        assertEquals(expected, actual);
    }
}