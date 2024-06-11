package org.joda.time.base;

import static org.junit.Assert.*;

import org.joda.time.*;
import org.joda.time.chrono.*;
import org.joda.time.field.*;
import org.joda.time.period.*;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testAbstractMethods() {
        assertNotNull(new BaseSingleFieldPeriod(0).getFieldType());
        assertNotNull(new BaseSingleFieldPeriod(0).getPeriodType());
    }

    @Test
    public void testSize() {
        assertEquals(1, new BaseSingleFieldPeriod(0).size());
    }

    @Test
    public void testGetFieldType() {
        assertSame(DurationFieldType.Millis, new BaseSingleFieldPeriod(0).getFieldType(0));
    }

    @Test
    public void testGetValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertEquals(5, period.getValue());
    }

    @Test
    public void testSetValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        period.setValue(6);
        assertEquals(6, period.getValue());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValueOutOfBounds() {
        new BaseSingleFieldPeriod(0).getValue(1);
    }

    @Test
    public void testGet() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertEquals(5, period.get(DurationFieldType.Millis));
    }

    @Test
    public void testIsSupported() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertTrue(period.isSupported(DurationFieldType.Millis));
    }

    @Test(expected = ClassCastException.class)
    public void testCompareToWrongClass() {
        new BaseSingleFieldPeriod(0).compareTo(new Object());
    }

    @Test
    public void testCompareToEquals() {
        BaseSingleFieldPeriod p1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod p2 = new BaseSingleFieldPeriod(5);
        assertEquals(0, p1.compareTo(p2));
    }

    @Test
    public void testCompareToGreater() {
        BaseSingleFieldPeriod p1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod p2 = new BaseSingleFieldPeriod(6);
        assertEquals(1, p1.compareTo(p2));
    }

    @Test
    public void testCompareToLess() {
        BaseSingleFieldPeriod p1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod p2 = new BaseSingleFieldPeriod(6);
        assertEquals(-1, p2.compareTo(p1));
    }

    @Test
    public void testEquals() {
        BaseSingleFieldPeriod p1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod p2 = new BaseSingleFieldPeriod(5);
        assertTrue(p1.equals(p2));
    }

    @Test
    public void testNotEquals() {
        BaseSingleFieldPeriod p1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod p2 = new BaseSingleFieldPeriod(6);
        assertFalse(p1.equals(p2));
    }

    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod p1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod p2 = new BaseSingleFieldPeriod(5);
        assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertEquals(PeriodType.INSTANCE, period.getPeriodType());
    }

    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        Period result = period.toPeriod();
        assertEquals(Period.MILLISECS.withMillis(5), result);
    }

    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        MutablePeriod result = period.toMutablePeriod();
        assertEquals(Period.MILLISECS.withMillis(5), result.toPeriod());
    }

    @Test
    public void testBetween() {
        Instant instant = new DateTime(2022, 1, 1, 0, 0, 0, 0, StandardCalendar.UTC).toInstant();
        Instant instant2 = new DateTime(2022, 1, 1, 1, 0, 0, 0, StandardCalendar.UTC).toInstant();
        ReadablePeriod period = Duration.standardDays(1);
        assertEquals(60 * 60 * 24 * 1000, BaseSingleFieldPeriod.between(instant, instant2, DurationFieldType.Millis));
        assertEquals(1, BaseSingleFieldPeriod.between(period, 1000 * 60 * 60 * 24));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetweenNull() {
        BaseSingleFieldPeriod.between(null, null, DurationFieldType.Millis);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetweenNull2() {
        BaseSingleFieldPeriod.between(new BaseSingleFieldPeriod(0), null, DurationFieldType.Millis);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetweenNull3() {
        BaseSingleFieldPeriod.between(null, new BaseSingleFieldPeriod(0), DurationFieldType.Millis);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetweenDifferentSizes() {
        Instant instant = new DateTime(2022, 1, 1, 0, 0, 0, 0, StandardCalendar.UTC).toInstant();
        Instant instant2 = new DateTime(2022, 1, 1, 1, 0, 0, 0, StandardCalendar.UTC).toInstant();
        ReadablePartial readablePartial = new GeneralDate();
        assertEquals(60 * 60 * 24 * 1000, BaseSingleFieldPeriod.between(instant, instant2, DurationFieldType.Millis));
        ReadablePeriod period = Duration.standardDays(1).withFieldAdded(new DateTimeFields.Second(), 1);
        throw new AssertionError("Expected IllegalArgumentException, but got: " + BaseSingleFieldPeriod.between(readablePartial, period, DurationFieldType.Milliseconds));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetweenDifferentSetsOfFields() {
        Instant instant = new DateTime(2022, 1, 1, 0, 0, 0, 0, StandardCalendar.UTC).toInstant();
        Instant instant2 = new DateTime(2022, 1, 1, 1, 0, 0, 0, StandardCalendar.UTC).toInstant();
        ReadablePartial readablePartial = new GeneralDate();
        GeneralDate generalDate = new GeneralDate();
        generalDate.setYear(2022);
        generalDate.setMonthOfYear(1);
        generalDate.setDayOfMonth(2);
        throw new AssertionError("Expected IllegalArgumentException, but got: " + BaseSingleFieldPeriod.between(readablePartial, new DateMidnight(generalDate), DurationFieldType.Milliseconds));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetweenNonContiguous() {
        Instant instant = new DateTime(2022, 1, 1, 0, 0, 0, 0, StandardCalendar.UTC).toInstant();
        Instant instant2 = new DateTime(2022, 1, 1, 1, 0, 0, 0, StandardCalendar.UTC).toInstant();
        GeneralDate generalDate = new GeneralDate();
        generalDate.setYear(2022);
        generalDate.setMonthOfYear(1);
        generalDate.setDayOfMonth(3);
        throw new AssertionError("Expected IllegalArgumentException, but got: " + BaseSingleFieldPeriod.between(new DateMidnight(instant), new DateMidnight(generalDate), DurationFieldType.Milliseconds));
    }

    @Test
    public void testStandardPeriodIn() {
        ReadablePeriod period = Duration.standardDays(1);
        assertEquals(24 * 60 * 60 * 1000, BaseSingleFieldPeriod.standardPeriodIn(period, 1000 * 60 * 60 * 24));
        assertEquals(1, BaseSingleFieldPeriod.standardPeriodIn(Duration.standardMinutes(1), 60));
        assertEquals(0, BaseSingleFieldPeriod.standardPeriodIn(Duration.standardMinutes(1), 120));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStandardPeriodInNull() {
        BaseSingleFieldPeriod.standardPeriodIn(null, 1000 * 60 * 60 * 24);
    }
}