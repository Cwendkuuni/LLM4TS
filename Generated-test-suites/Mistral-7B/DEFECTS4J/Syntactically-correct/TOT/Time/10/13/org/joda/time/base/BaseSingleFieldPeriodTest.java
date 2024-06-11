package org.joda.time.base;

import org.joda.time.BaseSingleFieldPeriod;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the BaseSingleFieldPeriod class.
 */
public class BaseSingleFieldPeriodTest {

    /**
     * Tests a BaseSingleFieldPeriod with a single day.
     */
    @Test
    public void testSingleDay() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.DAYS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        Period expected = new Period(1, 0, 0, 0);
        assertEquals(expected, period.toPeriod());

        MutablePeriod mutablePeriod = new MutablePeriod();
        mutablePeriod.add(period);
        Period actual = mutablePeriod.toPeriod();
        assertEquals(expected, actual);
    }

    /**
     * Tests a BaseSingleFieldPeriod with a negative single day.
     */
    @Test
    public void testNegativeSingleDay() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(-1) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.DAYS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        Period expected = new Period(-1, 0, 0, 0);
        assertEquals(expected, period.toPeriod());

        MutablePeriod mutablePeriod = new MutablePeriod();
        mutablePeriod.add(period);
        Period actual = mutablePeriod.toPeriod();
        assertEquals(expected, actual);
    }

    /**
     * Tests a BaseSingleFieldPeriod with a zero value.
     */
    @Test
    public void testZero() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(0) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.DAYS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        Period expected = Period.ZERO;
        assertEquals(expected, period.toPeriod());

        MutablePeriod mutablePeriod = new MutablePeriod();
        mutablePeriod.add(period);
        Period actual = mutablePeriod.toPeriod();
        assertEquals(expected, actual);
    }

    /**
     * Tests a BaseSingleFieldPeriod with a single hour.
     */
    @Test
    public void testSingleHour() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.HOURS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        Period expected = new Period(0, 1, 0, 0);
        assertEquals(expected, period.toPeriod());

        MutablePeriod mutablePeriod = new MutablePeriod();
        mutablePeriod.add(period);
        Period actual = mutablePeriod.toPeriod();
        assertEquals(expected, actual);
    }

    /**
     * Tests a BaseSingleFieldPeriod with a negative single hour.
     */
    @Test
    public void testNegativeSingleHour() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(-1) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.HOURS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        Period expected = new Period(0, -1, 0, 0);
        assertEquals(expected, period.toPeriod());

        MutablePeriod mutablePeriod = new MutablePeriod();
        mutablePeriod.add(period);
        Period actual = mutablePeriod.toPeriod();
        assertEquals(expected, actual);
    }

    /**
     * Tests a BaseSingleFieldPeriod with a single minute.
     */
    @Test
    public void testSingleMinute() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.MINUTES;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        Period expected = new Period(0, 0, 1, 0);
        assertEquals(expected, period.toPeriod());

        MutablePeriod mutablePeriod = new MutablePeriod();
        mutablePeriod.add(period);
        Period actual = mutablePeriod.toPeriod();
        assertEquals(expected, actual);
    }

    /**
     * Tests a BaseSingleFieldPeriod with a negative single minute.
     */
    @Test
    public void testNegativeSingleMinute() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(-1) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.MINUTES;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        Period expected = new Period(0, 0, -1, 0);
        assertEquals(expected, period.toPeriod());

        MutablePeriod mutablePeriod = new MutablePeriod();
        mutablePeriod.add(period);
        Period actual = mutablePeriod.toPeriod();
        assertEquals(expected, actual);
    }

    /**
     * Tests adding two BaseSingleFieldPeriods of the same type.
     */
    @Test
    public void testAddSameType() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(1) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.DAYS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(1) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.DAYS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        Period expected = new Period(2, 0, 0, 0);
        MutablePeriod mutablePeriod = new MutablePeriod();
        mutablePeriod.add(period1);
        mutablePeriod.add(period2);
        Period actual = mutablePeriod.toPeriod();
        assertEquals(expected, actual);
    }

    /**
     * Tests adding a BaseSingleFieldPeriod of a different type.
     */
    @Test(expected = AssertionError.class)
    public void testAddDifferentType() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(1) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.DAYS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(1) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.MINUTES;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        MutablePeriod mutablePeriod = new MutablePeriod();
        mutablePeriod.add(period1);
        mutablePeriod.add(period2);
    }
}