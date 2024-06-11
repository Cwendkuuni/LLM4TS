package org.junit.platform.commons.annotation.Generated;
import org.joda.time.BasePeriod;
import org.joda.time.Duration;
import org.joda.time.PeriodType;
import org.junit.Test;
import static org.junit.Assert.*;

@Generated("org.junit.platform.commons.annotation.Generated")
public class BasePeriodTest {

    @Test
    public void testBasePeriodConstructorWithArguments() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        PeriodType expectedType = PeriodType.standard();
        int[] expectedValues = new int[]{1, 2, 3, 4, 5, 6, 7, 8};
        assertEquals(expectedType, period.getPeriodType());
        assertArrayEquals(expectedValues, period.getValues());
    }

    @Test
    public void testBasePeriodConstructorWithLong() {
        BasePeriod period = new BasePeriod(123456789L);
        PeriodType expectedType = PeriodType.standard();
        int[] expectedValues = new int[]{1, 0, 0, 0, 0, 0, 0, 0, 0};
        assertEquals(expectedType, period.getPeriodType());
        assertArrayEquals(expectedValues, period.getValues());
    }

    @Test
    public void testBasePeriodConstructorWithInstantAndInstant() {
        BasePeriod period = new BasePeriod(DateTimeUtils.parseLocalDate("2022-01-01"), DateTimeUtils.parseLocalDate("2022-01-02"), PeriodType.standard());
        PeriodType expectedType = PeriodType.standard();
        int[] expectedValues = new int[]{0, 0, 0, 31, 0, 0, 0, 0};
        assertEquals(expectedType, period.getPeriodType());
        assertArrayEquals(expectedValues, period.getValues());
    }

    @Test
    public void testBasePeriodConstructorWithInstantAndDuration() {
        BasePeriod period = new BasePeriod(DateTimeUtils.parseLocalDate("2022-01-01"), new Duration(1, 2, 3, 4, 5, 6, 7, 8), PeriodType.standard());
        PeriodType expectedType = PeriodType.standard();
        int[] expectedValues = new int[]{0, 0, 1, 2, 3, 4, 5, 6, 8};
        assertEquals(expectedType, period.getPeriodType());
        assertArrayEquals(expectedValues, period.getValues());
    }

    @Test
    public void testBasePeriodConstructorWithReadablePeriodAndPeriodType() {
        BasePeriod period = new BasePeriod(DateTimeUtils.parseLocalDate("2022-01-01"), DateTimeUtils.parseLocalDate("2022-01-03"), PeriodType.standard());
        PeriodType expectedType = PeriodType.standard();
        int[] expectedValues = new int[]{0, 0, 1, 0, 0, 0, 0, 0, 0};
        assertEquals(expectedType, period.getPeriodType());
        assertArrayEquals(expectedValues, period.getValues());
    }

    @Test
    public void testBasePeriodConstructorWithReadableDurationAndReadableInstant() {
        BasePeriod period = new BasePeriod(new Duration(1, 2, 3, 4, 5, 6, 7, 8), DateTimeUtils.parseLocalDate("2022-01-01"), PeriodType.standard());
        PeriodType expectedType = PeriodType.standard();
        int[] expectedValues = new int[]{0, 0, 1, 2, 3, 4, 5, 6, 8};
        assertEquals(expectedType, period.getPeriodType());
        assertArrayEquals(expectedValues, period.getValues());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBasePeriodConstructorWithNullReadablePartial() {
        new BasePeriod(null, null, PeriodType.standard());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBasePeriodConstructorWithNullReadablePartial2() {
        new BasePeriod(DateTimeUtils.parseLocalDate("2022-01-01"), null, PeriodType.standard());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBasePeriodConstructorWithDifferentFieldTypes() {
        new BasePeriod(DateTimeUtils.parseLocalDate("2022-01-01"), DateTimeUtils.parseLocalDate("2022-01-02"), PeriodType.standard());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBasePeriodConstructorWithNonContiguousReadablePartial() {
        new BasePeriod(DateTimeUtils.parseLocalDate("2022-01-01"), DateTimeUtils.parseLocalDateTime("2022-01-02T01:02:03.045Z"), PeriodType.standard());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBasePeriodConstructorWithNullObject() {
        new BasePeriod(null, PeriodType.standard(), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBasePeriodConstructorWithNullPeriodType() {
        new BasePeriod(null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBasePeriodConstructorWithNullChronology() {
        new BasePeriod(null, null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBasePeriodConstructorWithNullIntArray() {
        new BasePeriod(null, PeriodType.standard(), new int[0]);
    }

    @Test
    public void testGetPeriodType() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        PeriodType expectedType = PeriodType.standard();
        assertEquals(expectedType, period.getPeriodType());
    }

    @Test
    public void testGetValue() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        int value = period.getValue(0);
        int expectedValue = 1;
        assertEquals(expectedValue, value);
    }

    @Test
    public void testToDurationFrom() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        Duration duration = period.toDurationFrom(DateTimeUtils.parseLocalDate("2022-01-01"));
        long expectedMillis = 1349356800000L;
        assertEquals(expectedMillis, duration.getMillis());
    }

    @Test
    public void testToDurationTo() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        Duration duration = period.toDurationTo(DateTimeUtils.parseLocalDate("2022-01-01"));
        long expectedMillis = 1349356400000L;
        assertEquals(expectedMillis, duration.getMillis());
    }

    @Test
    public void testCheckAndUpdate() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        int[] values = new int[8];
        int expectedValue = 1;
        period.checkAndUpdate(DurationFieldType.years(), values, expectedValue);
        int actualValue = values[0];
        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testSetPeriod() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        int[] inputValues = new int[]{1, 2, 3, 4, 5, 6, 7, 8};
        period.setPeriod(new BasePeriod(inputValues, PeriodType.standard()));
        assertArrayEquals(inputValues, period.getValues());
    }

    @Test
    public void testSetPeriodInternal() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        int[] inputValues = new int[]{1, 2, 3, 4, 5, 6, 7, 8};
        period.setPeriodInternal(new BasePeriod(inputValues, PeriodType.standard()));
        assertArrayEquals(inputValues, period.getValues());
    }

    @Test
    public void testSetPeriodWithNull() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        period.setPeriod(null);
        assertArrayEquals(new int[8], period.getValues());
    }

    @Test
    public void testSetValues() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        int[] inputValues = new int[]{1, 2, 3, 4, 5, 6, 7, 8};
        period.setValues(inputValues);
        assertArrayEquals(inputValues, period.getValues());
    }
}