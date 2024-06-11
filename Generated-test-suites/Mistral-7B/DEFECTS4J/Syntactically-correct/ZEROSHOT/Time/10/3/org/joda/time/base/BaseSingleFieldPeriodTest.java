package org.joda.time.base.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.joda.time.BaseSingleFieldPeriod;
import org.joda.time.Chronology;
import org.joda.time.DateTimeUtils;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePartial;
import org.joda.time.ReadablePeriod;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.FieldUtils;

public class BaseSingleFieldPeriodTest {

    /**
     * Test method for {@link BaseSingleFieldPeriod#between(ReadableInstant, ReadableInstant, DurationFieldType)}
     */
    @org.junit.Test
    public void testBetween_Instant() {
        BaseSingleFieldPeriod testee = new BaseSingleFieldPeriod(7) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.DAYS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        Chronology chrono = DateTimeUtils.getInstantChronology(DateTimeUtils.CURRENT_INSTANT);
        ReadableInstant start = chrono.getInstant(0L);
        ReadableInstant end = chrono.getInstant(DateTimeUtils.CURRENT_INSTANT.getMillis() + 7 * 24 * 60 * 60 * 1000);
        int result = BaseSingleFieldPeriod.between(start, end, DurationFieldType.DAYS);

        assertEquals(7, result);
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#between(ReadablePartial, ReadablePartial, ReadablePeriod)}
     */
    @org.junit.Test
    public void testBetween_Partial() {
        BaseSingleFieldPeriod testee = new BaseSingleFieldPeriod(7) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.DAYS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        Chronology chrono = DateTimeUtils.getInstantChronology(DateTimeUtils.CURRENT_INSTANT);
        ReadablePartial start = chrono.getLocalDate(2022, 1, 1);
        ReadablePartial end = chrono.getLocalDate(2022, 1, 8);
        int result = BaseSingleFieldPeriod.between(start, end, testee);

        assertEquals(7, result);
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#standardPeriodIn(ReadablePeriod, long)}
     */
    @org.junit.Test
    public void testStandardPeriodIn_Period() {
        BaseSingleFieldPeriod testee = new BaseSingleFieldPeriod(7) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.DAYS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        ReadablePeriod period = Period.ofDays(10);
        int result = BaseSingleFieldPeriod.standardPeriodIn(period, 86400000);

        assertEquals(7, result);
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#BaseSingleFieldPeriod(int)}
     */
    @org.junit.Test
    public void testConstructor_Int() {
        BaseSingleFieldPeriod testee = new BaseSingleFieldPeriod(7);

        assertEquals(7, testee.getValue());
        assertEquals(DurationFieldType.DAYS, testee.getFieldType());
        assertEquals(PeriodType.standard(), testee.getPeriodType());
        assertEquals(1, testee.size());
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#getValue()}
     */
    @org.junit.Test
    public void testGetValue() {
        BaseSingleFieldPeriod testee = new BaseSingleFieldPeriod(7);

        assertEquals(7, testee.getValue());
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#setValue(int)}
     */
    @org.junit.Test
    public void testSetValue() {
        BaseSingleFieldPeriod testee = new BaseSingleFieldPeriod(7);

        testee.setValue(11);

        assertEquals(11, testee.getValue());
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#getFieldType()}
     */
    @org.junit.Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod testee = new BaseSingleFieldPeriod(7);

        assertEquals(DurationFieldType.DAYS, testee.getFieldType());
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#getPeriodType()}
     */
    @org.junit.Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod testee = new BaseSingleFieldPeriod(7);

        assertEquals(PeriodType.standard(), testee.getPeriodType());
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#size()}
     */
    @org.junit.Test
    public void testSize() {
        BaseSingleFieldPeriod testee = new BaseSingleFieldPeriod(7);

        assertEquals(1, testee.size());
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#getFieldType(int)}
     */
    @org.junit.Test
    public void testGetFieldType_Index() {
        BaseSingleFieldPeriod testee = new BaseSingleFieldPeriod(7);

        assertEquals(DurationFieldType.DAYS, testee.getFieldType(0));
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#getValue(int)}
     */
    @org.junit.Test
    public void testGetValue_Index() {
        BaseSingleFieldPeriod testee = new BaseSingleFieldPeriod(7);

        assertEquals(7, testee.getValue(0));
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#get(DurationFieldType)}
     */
    @org.junit.Test
    public void testGet() {
        BaseSingleFieldPeriod testee = new BaseSingleFieldPeriod(7) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.DAYS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        assertEquals(7, testee.get(DurationFieldType.DAYS));
        assertEquals(0, testee.get(DurationFieldType.MONTHS));
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#isSupported(DurationFieldType)}
     */
    @org.junit.Test
    public void testIsSupported() {
        BaseSingleFieldPeriod testee = new BaseSingleFieldPeriod(7) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.DAYS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        assertTrue(testee.isSupported(DurationFieldType.DAYS));
        assertFalse(testee.isSupported(DurationFieldType.MONTHS));
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#toPeriod()}
     */
    @org.junit.Test
    public void testToPeriod() {
        BaseSingleFieldPeriod testee = new BaseSingleFieldPeriod(7) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.DAYS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        Period result = testee.toPeriod();

        assertEquals(7, result.getDays());
        assertEquals(0, result.getMonths());
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#toMutablePeriod()}
     */
    @org.junit.Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod testee = new BaseSingleFieldPeriod(7) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.DAYS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        MutablePeriod result = testee.toMutablePeriod();

        assertEquals(7, result.getDays());
        assertEquals(0, result.getMonths());
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#equals(Object)}
     */
    @org.junit.Test
    public void testEquals() {
        BaseSingleFieldPeriod testee = new BaseSingleFieldPeriod(7) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.DAYS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        BaseSingleFieldPeriod other = new BaseSingleFieldPeriod(7) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.DAYS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        Object otherObject = new Object();
        assertTrue(testee.equals(other));
        assertFalse(testee.equals(otherObject));
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#hashCode()}
     */
    @org.junit.Test
    public void testHashCode() {
        BaseSingleFieldPeriod testee = new BaseSingleFieldPeriod(7) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.DAYS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        int result = testee.hashCode();
        BaseSingleFieldPeriod other = new BaseSingleFieldPeriod(7) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.DAYS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        assertEquals(result, other.hashCode());
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#compareTo(BaseSingleFieldPeriod)}
     */
    @org.junit.Test
    public void testCompareTo() {
        BaseSingleFieldPeriod testee1 = new BaseSingleFieldPeriod(7) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.DAYS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        BaseSingleFieldPeriod testee2 = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.DAYS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        int result = testee1.compareTo(testee2);

        assertEquals(2, result);
    }
}