package org.joda.time.base;

import static org.junit.Assert.*;
import org.joda.time.*;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testBaseSingleFieldPeriod_getValue() {
        BaseSingleFieldPeriod period = new FixedPeriod(1);
        int value = period.getValue();
        assertEquals(1, value);
    }

    @Test
    public void testBaseSingleFieldPeriod_setValue() {
        BaseSingleFieldPeriod period = new FixedPeriod(1);
        period.setValue(2);
        int value = period.getValue();
        assertEquals(2, value);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testBaseSingleFieldPeriod_getValue_outOfBounds() {
        BaseSingleFieldPeriod period = new FixedPeriod(1);
        period.getValue(2);
    }

    @Test
    public void testBaseSingleFieldPeriod_getFieldType() {
        BaseSingleFieldPeriod period = new FixedPeriod(1);
        DurationFieldType fieldType = period.getFieldType();
        assertEquals(DurationFieldType.instance(IAVING_SECONDS), fieldType);
    }

    @Test
    public void testBaseSingleFieldPeriod_getPeriodType() {
        BaseSingleFieldPeriod period = new FixedPeriod(1);
        PeriodType periodType = period.getPeriodType();
        assertEquals(PeriodType.instance(FixedPeriod.class), periodType);
    }

    @Test
    public void testBaseSingleFieldPeriod_size() {
        BaseSingleFieldPeriod period = new FixedPeriod(1);
        assertEquals(1, period.size());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testBaseSingleFieldPeriod_getFieldType_outOfBounds() {
        BaseSingleFieldPeriod period = new FixedPeriod(1);
        period.getFieldType(2);
    }

    @Test
    public void testBaseSingleFieldPeriod_getValue_withPeriod() {
        BaseSingleFieldPeriod period = new FixedPeriod(1);
        int value = period.getValue(new Days(1));
        assertEquals(2, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBaseSingleFieldPeriod_between_nullReadableInstant() {
        ReadableInstant nullReadableInstant = null;
        ReadableInstant readableInstant = new Instant(0);
        DurationFieldType durationFieldType = DurationFieldType.instance(IAVING_SECONDS);
        int between = BaseSingleFieldPeriod.between(nullReadableInstant, readableInstant, durationFieldType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBaseSingleFieldPeriod_between_nullReadablePartial() {
        ReadablePartial nullReadablePartial = null;
        ReadablePartial readablePartial = new DateTime(0);
        ReadablePeriod readablePeriod = Period.ONE_DAY;
        int between = BaseSingleFieldPeriod.between(nullReadablePartial, readablePartial, readablePeriod);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBaseSingleFieldPeriod_between_differentSize() {
        ReadablePartial readablePartial = new DateTime(0);
        ReadablePartial readablePartial2 = new LocalDateTime(2023, 1, 1, 12, 0);
        ReadablePeriod readablePeriod = Period.ONE_DAY;
        int between = BaseSingleFieldPeriod.between(readablePartial, readablePartial2, readablePeriod);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBaseSingleFieldPeriod_between_nonContiguous() {
        ReadablePartial readablePartial = new DateTime(0);
        readablePartial = readablePartial.withFieldAdded(ChronoField.HOUR_OF_DAY, 1);
        ReadablePartial readablePartial2 = new DateTime(0);
        ReadablePeriod readablePeriod = Period.ONE_DAY;
        int between = BaseSingleFieldPeriod.between(readablePartial, readablePartial2, readablePeriod);
    }

    @Test
    public void testBaseSingleFieldPeriod_standardPeriodIn() {
        BaseSingleFieldPeriod period = new FixedPeriod(1);
        long n = 2;
        int standardPeriodIn = BaseSingleFieldPeriod.standardPeriodIn(period, n);
        assertEquals(n / 1, standardPeriodIn);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBaseSingleFieldPeriod_standardPeriodIn_nullReadablePeriod() {
        BaseSingleFieldPeriod period = new FixedPeriod(1);
        long n = 2;
        int standardPeriodIn = BaseSingleFieldPeriod.standardPeriodIn(null, n);
    }

    @Test
    public void testBaseSingleFieldPeriod_equals() {
        BaseSingleFieldPeriod period1 = new FixedPeriod(1);
        BaseSingleFieldPeriod period2 = new FixedPeriod(1);
        BaseSingleFieldPeriod period3 = new FixedPeriod(2);
        assertTrue(period1.equals(period1));
        assertTrue(period1.equals(period2));
        assertFalse(period1.equals(period3));
    }

    @Test
    public void testBaseSingleFieldPeriod_hashCode() {
        BaseSingleFieldPeriod period1 = new FixedPeriod(1);
        BaseSingleFieldPeriod period2 = new FixedPeriod(1);
        BaseSingleFieldPeriod period3 = new FixedPeriod(2);
        int hashCode1 = period1.hashCode();
        int hashCode2 = period1.hashCode();
        int hashCode3 = period2.hashCode();
        assertNotEquals(hashCode1, hashCode3);
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testBaseSingleFieldPeriod_compareTo() {
        BaseSingleFieldPeriod period1 = new FixedPeriod(1);
        BaseSingleFieldPeriod period2 = new FixedPeriod(2);
        BaseSingleFieldPeriod period3 = new FixedPeriod(1);
        int comparisonResult1 = period1.compareTo(period1);
        int comparisonResult2 = period1.compareTo(period2);
        int comparisonResult3 = period2.compareTo(period3);
        int comparisonResult4 = period1.compareTo(period3);

        assertGreaterThan(comparisonResult1, 0);
        assertLessThan(comparisonResult2, 0);
        assertGreaterThan(comparisonResult3, comparisonResult1);
        assertGreaterThan(comparisonResult4, 0);
    }
}