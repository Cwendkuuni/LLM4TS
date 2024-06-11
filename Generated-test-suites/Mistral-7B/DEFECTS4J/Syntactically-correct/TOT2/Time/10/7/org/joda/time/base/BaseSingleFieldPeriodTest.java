package org.joda.time.base;

import static org.junit.Assert.*;

import org.joda.time.BaseSingleFieldPeriod;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.DateTimeUtils;
import org.joda.time.Chronology;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

public class BaseSingleFieldPeriodTest {

    @org.junit.Test
    public void testSimpleConstructor() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(3);
        assertEquals("Value should be 3", 3, period.getValue());
        DurationFieldType fieldType = period.getFieldType();
        assertEquals("Field type should match", DurationFieldType.days(), fieldType);
    }

    @org.junit.Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(3);
        PeriodType periodType = period.getPeriodType();
        assertEquals("Period type should be standard", PeriodType.standard(), periodType);
    }

    @org.junit.Test
    public void testSize() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(3);
        int size = period.size();
        assertEquals("Size should be 1", 1, size);
    }

    @org.junit.Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(3);
        DurationFieldType fieldType = period.getFieldType();
        assertEquals("Field type should be days", DurationFieldType.days(), fieldType);
    }

    @org.junit.Test
    public void testGetValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(3);
        int value = period.getValue();
        assertEquals("Value should be 3", 3, value);
    }

    @org.junit.Test
    public void testSetValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(3);
        period.setValue(5);
        int value = period.getValue();
        assertEquals("Value should be 5", 5, value);
    }

    @org.junit.Test
    public void testToPeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(3);
        Period p = period.toPeriod();
        assertEquals("Period values should match", 3, p.getValues()[0]);
        assertEquals("Period types should match", PeriodType.standard(), p.getPeriodType());
    }

    @org.junit.Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(3);
        MutablePeriod mp = period.toMutablePeriod();
        assertEquals("Values should match", 3, mp.getMillis());
    }

    @org.junit.Test
    public void testEquals() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(3);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(3);
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(4);

        assertTrue("period1 and period2 should be equal", period1.equals(period2));
        assertTrue("period1 and period2 should be equal (reverse)", period2.equals(period1));
        assertFalse("period1 and period3 should not be equal", period1.equals(period3));
    }

    @org.junit.Test
    public void testHashCode() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(3);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(3);
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(4);

        int hashCode1 = period1.hashCode();
        int hashCode2 = period2.hashCode();
        int hashCode3 = period3.hashCode();

        //should be same if equal
        assertEquals("Hashcodes of equal objects should be the same", hashCode1, hashCode2);

        //should be different if not equal
        assertNotSame("Hashcodes of non-equal objects should be different", hashCode1, hashCode3);
    }

    @org.junit.Test
    public void testCompareTo() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(3);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(3);

        int comparisonResult1 = period1.compareTo(period2);
        int comparisonResult2 = period2.compareTo(period1);
        int comparisonResult3 = period1.compareTo(period3);

        assertTrue("Comparison result of period1 and period2 should be negative", comparisonResult1 < 0);
        assertTrue("Comparison result of period2 and period1 should be positive", comparisonResult2 > 0);
        assertTrue("Comparison result of period1 and period3 should be zero", comparisonResult3 == 0);
    }

    @org.junit.Test
    public void testBetween() {
        LocalDate startDate = new LocalDate(2022, 1, 1);
        LocalDate endDate = new LocalDate(2022, 1, 5);

        int between1 = BaseSingleFieldPeriod.between(startDate, endDate, DurationFieldType.days());
        assertEquals("Number of days between start and end date should be 4", 4, between1);

        LocalDateTime startDateTime = new LocalDateTime(2022, 1, 1, 12, 0, 0);
        LocalDateTime endDateTime = new LocalDateTime(2022, 1, 3, 14, 30, 0);

        int between2 = BaseSingleFieldPeriod.between(startDateTime, endDateTime, DurationFieldType.minutes());
        assertEquals("Number of minutes between start and end datetime should be 102", 102, between2);
    }

    @org.junit.Test
    public void testStandardPeriodIn() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(365);
        int millisPerUnit = 1000 * 60 * 60 * 24;
        int standardPeriodInMillis = BaseSingleFieldPeriod.standardPeriodIn(period, millisPerUnit);
        assertEquals("Standard period in milliseconds should be 12315200000L", 12315200000L, standardPeriodInMillis);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testBetweenWithInvalidReadablePartial() {
        LocalDate startDate = null;
        LocalDate endDate = new LocalDate(2022, 1, 1);
        int between = BaseSingleFieldPeriod.between(startDate, endDate, DurationFieldType.days());
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testBetweenWithInvalidChronology() {
        LocalDate startDate = new LocalDate(2022, 1, 1, DateTimeUtils.CENTURY_OFFSET);
        LocalDate endDate = new LocalDate(2022, 1, 1);
        int between = BaseSingleFieldPeriod.between(startDate, endDate, DurationFieldType.days());
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testBetweenWithInvalidFieldType() {
        LocalDate startDate = new LocalDate(2022, 1, 1);
        LocalDate endDate = new LocalDate(2022, 1, 1);
        int between = BaseSingleFieldPeriod.between(startDate, endDate, null);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testBetweenWithNullStartDate() {
        LocalDate endDate = new LocalDate(2022, 1, 1);
        int between = BaseSingleFieldPeriod.between(null, endDate, DurationFieldType.days());
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testBetweenWithNullEndDate() {
        LocalDate startDate = new LocalDate(2022, 1, 1);
        int between = BaseSingleFieldPeriod.between(startDate, null, DurationFieldType.days());
    }

    @org.junit.Test
    public void testToString() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(3);
        String stringRepresentation = period.toString();
        assertEquals("String representation of period should be 'PT3D'", "PT3D", stringRepresentation);
    }
}