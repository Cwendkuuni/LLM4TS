package org.joda.time.base;

import static org.junit.Assert.*;

import org.joda.time.*;
import org.joda.time.chrono.*;
import org.joda.time.field.*;
import org.junit.Test;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testSize() {
        BaseSingleFieldPeriod period = new DailyFields(2);
        assertEquals(1, period.size());
    }

    @Test
    public void testEquals() {
        BaseSingleFieldPeriod period1 = new DailyFields(2);
        BaseSingleFieldPeriod period2 = new DailyFields(2);
        BaseSingleFieldPeriod period3 = new HourlyFields(3);

        assertEquals(period1, period1);
        assertEquals(period1, period2);
        assertNotEquals(period1, period3);
    }

    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod period = new DailyFields(2);
        assertEquals(ISODateFieldType.DAY_OF_MONTH, period.getFieldType());
    }

    @Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod period = new DailyFields(2);
        assertEquals(PeriodType.DAYS, period.getPeriodType());
    }

    @Test
    public void testGetValue() {
        BaseSingleFieldPeriod period = new DailyFields(2);
        assertEquals(2, period.getValue());
    }

    @Test
    public void testSetValue() {
        BaseSingleFieldPeriod period = new DailyFields(2);
        period.setValue(3);
        assertEquals(3, period.getValue());
    }

    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod period = new DailyFields(2);
        Period expectedPeriod = new Period(2, PeriodType.DAYS);
        Period actualPeriod = period.toPeriod();
        assertEquals(expectedPeriod, actualPeriod);
    }

    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod period = new DailyFields(2);
        MutablePeriod expectedPeriod = new MutablePeriod().withValue(2, PeriodType.DAYS);
        MutablePeriod actualPeriod = period.toMutablePeriod();
        assertEquals(expectedPeriod, actualPeriod);
    }

    @Test
    public void testCompareTo() {
        BaseSingleFieldPeriod period1 = new DailyFields(2);
        BaseSingleFieldPeriod period2 = new DailyFields(3);
        BaseSingleFieldPeriod period3 = new DailyFields(2);

        int compareResult12 = period1.compareTo(period2);
        int compareResult13 = period1.compareTo(period3);
        int compareResult21 = period2.compareTo(period1);
        int compareResult23 = period2.compareTo(period3);
        int compareResult31 = period3.compareTo(period1);
        int compareResult32 = period3.compareTo(period2);

        assertEquals(-1, compareResult12);
        assertEquals(1, compareResult21);
        assertEquals(0, compareResult13);
        assertEquals(0, compareResult32);
        assertEquals(-1, compareResult31);
    }

    @Test
    public void testBetween() {
        try {
            BaseSingleFieldPeriod.between(null, null, ISODateFieldType.DAY_OF_MONTH);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        ISODate inst1 = new ISODate(2019, 1, 15);
        ISODate inst2 = new ISODate(2019, 1, 16);

        int result = BaseSingleFieldPeriod.between(inst1, inst2, ISODateFieldType.DAY_OF_MONTH);
        assertEquals(1, result);

        ISODate inst3 = new ISODate(2019, 1, 15);
        ISODate inst4 = new ISODate(2019, 1, 15);

        int result2 = BaseSingleFieldPeriod.between(inst3, inst4, ISODateFieldType.DAY_OF_MONTH);
        assertEquals(0, result2);
    }

    @Test
    public void testStandardPeriodIn() {
        try {
            BaseSingleFieldPeriod.standardPeriodIn(null, 1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        BaseSingleFieldPeriod dailyPeriod = new DailyFields(2);

        assertEquals(471200000, BaseSingleFieldPeriod.standardPeriodIn(dailyPeriod, 1000));
        assertEquals(1, BaseSingleFieldPeriod.standardPeriodIn(dailyPeriod, 1));
    }
}