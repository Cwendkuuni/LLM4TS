package org.joda.time.base;

import static org.junit.Assert.*;

import org.joda.time.*;
import org.joda.time.chrono.*;
import org.joda.time.field.*;
import org.joda.time.period.*;
import org.junit.Test;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testBetween() {
        CalendarFieldType calendarFieldType = CalendarFieldType.millennia();
        ReadableInstant instant = new DateTime(2022, 1, 1, 0, 0, 0, 0, ISO.UTC);
        ReadableInstant instant2 = new DateTime(2021, 12, 31, 23, 59, 59, 999, ISO.UTC);
        int result = BaseSingleFieldPeriod.between(instant, instant2, calendarFieldType);
        assertEquals(1, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetweenWithNullReadableInstant() {
        CalendarFieldType calendarFieldType = CalendarFieldType.millennia();
        ReadableInstant instant = null;
        ReadableInstant instant2 = new DateTime(2021, 12, 31, 23, 59, 59, 999, ISO.UTC);
        BaseSingleFieldPeriod.between(instant, instant2, calendarFieldType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetweenWithNullReadableInstant2() {
        CalendarFieldType calendarFieldType = CalendarFieldType.millennia();
        ReadableInstant instant = new DateTime(2022, 1, 1, 0, 0, 0, 0, ISO.UTC);
        ReadableInstant instant2 = null;
        BaseSingleFieldPeriod.between(instant, instant2, calendarFieldType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetweenWithDifferentFieldTypes() {
        CalendarFieldType calendarFieldType = CalendarFieldType.millennia();
        CalendarFieldType durationFieldType = DurationFieldType.standardHours();
        ReadableInstant instant = new DateTime(2022, 1, 1, 0, 0, 0, 0, ISO.UTC);
        ReadableInstant instant2 = new DateTime(2021, 12, 31, 23, 59, 59, 999, ISO.UTC);
        BaseSingleFieldPeriod.between(instant, instant2, calendarFieldType);
        BaseSingleFieldPeriod.between(instant, instant2, durationFieldType);
    }

    @Test
    public void testStandardPeriodIn() {
        Period period = new Period(1, PeriodType.DAYS);
        long n = 2;
        int result = BaseSingleFieldPeriod.standardPeriodIn(period, n);
        assertEquals(594, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStandardPeriodInWithNullReadablePeriod() {
        long n = 2;
        BaseSingleFieldPeriod.standardPeriodIn(null, n);
    }

    @Test
    public void testConstructor() {
        int iPeriod = 7;
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(iPeriod);
        assertEquals(iPeriod, baseSingleFieldPeriod.getValue());
    }

    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(3) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.YEARS;
            }
        };
        DurationFieldType durationFieldType = baseSingleFieldPeriod.getFieldType();
        assertEquals(DurationFieldType.YEARS, durationFieldType);
    }

    @Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(3) {
            @Override
            public PeriodType getPeriodType() {
                return PeriodType.MONTHS;
            }
        };
        PeriodType periodType = baseSingleFieldPeriod.getPeriodType();
        assertEquals(PeriodType.MONTHS, periodType);
    }

    @Test
    public void testSize() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(3) {
            @Override
            public int size() {
                return 2;
            }
        };
        int size = baseSingleFieldPeriod.size();
        assertEquals(1, size);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetFieldTypeWithInvalidIndex() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(3) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.YEARS;
            }
        };
        DurationFieldType result = baseSingleFieldPeriod.getFieldType(2);
    }

    @Test
    public void testGetValue() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(3) {
            @Override
            public int getValue() {
                return 5;
            }
        };
        int value = baseSingleFieldPeriod.getValue();
        assertEquals(3, value);
    }

    @Test
    public void testSetValue() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(3) {
            @Override
            public void setValue(int iPeriod) {
                this.iPeriod = iPeriod * 2;
            }
        };
        baseSingleFieldPeriod.setValue(5);
        assertEquals(10, baseSingleFieldPeriod.getValue());
    }

    @Test
    public void testEquals() {
        BaseSingleFieldPeriod baseSingleFieldPeriod1 = new BaseSingleFieldPeriod(3) {};
        BaseSingleFieldPeriod baseSingleFieldPeriod2 = new BaseSingleFieldPeriod(3) {};
        assertTrue(baseSingleFieldPeriod1.equals(baseSingleFieldPeriod2));
    }

    @Test
    public void testNotEquals() {
        BaseSingleFieldPeriod baseSingleFieldPeriod1 = new BaseSingleFieldPeriod(3) {};
        BaseSingleFieldPeriod baseSingleFieldPeriod2 = new BaseSingleFieldPeriod(4) {};
        assertFalse(baseSingleFieldPeriod1.equals(baseSingleFieldPeriod2));
    }

    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod baseSingleFieldPeriod1 = new BaseSingleFieldPeriod(3) {};
        BaseSingleFieldPeriod baseSingleFieldPeriod2 = new BaseSingleFieldPeriod(3) {};
        assertEquals(baseSingleFieldPeriod1.hashCode(), baseSingleFieldPeriod2.hashCode());
    }

    @Test
    public void testCompareToZero() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(3) {};
        int result = baseSingleFieldPeriod.compareTo(new BaseSingleFieldPeriod(0) {});
        assertEquals(1, result);
    }

    @Test
    public void testCompareToSameInstance() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(3) {};
        int result = baseSingleFieldPeriod.compareTo(baseSingleFieldPeriod);
        assertEquals(0, result);
    }

    @Test
    public void testCompareToNegative() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(3) {};
        int result = baseSingleFieldPeriod.compareTo(new BaseSingleFieldPeriod(4) {});
        assertEquals(-1, result);
    }

    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(3) {};
        Period period = baseSingleFieldPeriod.toPeriod();
        assertEquals(PeriodType.DAYS, period.getPeriodType());
        assertEquals(3, period.getValue(PeriodType.DAYS));
    }

    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(3) {};
        MutablePeriod mutablePeriod = baseSingleFieldPeriod.toMutablePeriod();
        mutablePeriod.add(new Period(1, PeriodType.DAYS));
        Period period = mutablePeriod.toPeriod();
        assertEquals(PeriodType.DAYS, period.getPeriodType());
        assertEquals(4, period.getValue(PeriodType.DAYS));
    }
}