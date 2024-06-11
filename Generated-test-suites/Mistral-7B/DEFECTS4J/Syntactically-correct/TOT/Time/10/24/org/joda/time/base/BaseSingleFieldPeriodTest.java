package org.joda.time.base;

import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.base.BaseSingleFieldPeriod;
import org.joda.time.chrono.ISOChronology;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link BaseSingleFieldPeriod}.
 */
public class BaseSingleFieldPeriodTest {

    /**
     * Tests instant to instant.
     */
    @Test
    public void testInstantToInstant() {
        Chronology chrono = ISOChronology.getInstanceUTC();
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(60) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.MINUTES;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        ReadableInstant start = BaseDateTime.now(chrono).toInstant();
        ReadableInstant end = start.plus(period);
        int minutes = BaseSingleFieldPeriod.between(start, end, DurationFieldType.MINUTES);
        assertEquals(60, minutes);
    }

    /**
     * Tests null instant.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullInstant() {
        Chronology chrono = ISOChronology.getInstanceUTC();
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(60) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.MINUTES;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        ReadableInstant start = null;
        ReadableInstant end = BaseDateTime.now(chrono).toInstant();
        int minutes = BaseSingleFieldPeriod.between(start, end, DurationFieldType.MINUTES);
    }

    /**
     * Tests partial to partial.
     */
    @Test
    public void testPartialToPartial() {
        Chronology chrono = ISOChronology.getInstanceUTC();
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(3) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.DAYS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        ReadablePartial start = new LocalDate(2012, 1, 1).toLocalDateTime(chrono).toLocalDate();
        ReadablePartial end = start.plusDays(3);
        int days = BaseSingleFieldPeriod.between(start, end, period);
        assertEquals(3, days);
    }

    /**
     * Tests null partial.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullPartial() {
        Chronology chrono = ISOChronology.getInstanceUTC();
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(3) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.DAYS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        ReadablePartial start = null;
        ReadablePartial end = new LocalDate(2012, 1, 1).toLocalDateTime(chrono).toLocalDate();
        int days = BaseSingleFieldPeriod.between(start, end, period);
    }

    /**
     * Tests standard period conversion.
     */
    @Test
    public void testStandardPeriodConversion() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(12345678) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.HOURS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        long millisPerUnit = 60L * 60L * 1000L; // hours to milliseconds
        int hours = BaseSingleFieldPeriod.standardPeriodIn(period, millisPerUnit);
        assertEquals(123, hours);
        assertEquals(456, hours % 1000);
        assertEquals(78, hours / 1000);
    }

    /**
     * Tests null period.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullPeriod() {
        BaseSingleFieldPeriod period = null;
        long millisPerUnit = 60L * 60L * 1000L; // hours to milliseconds
        int hours = BaseSingleFieldPeriod.standardPeriodIn(period, millisPerUnit);
    }

    /**
     * Tests immutable period.
     */
    @Test
    public void testImmutablePeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(3) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.DAYS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }

            @Override
            public void setValue(int value) {
                throw new UnsupportedOperationException();
            }
        };
        assertFalse(period.isSupported(DurationFieldType.MINUTES));
        assertNull(period.getField(DurationFieldType.MINUTES));
        period.add(DurationFieldType.MINUTES, 60);
        assertNotNull(period.getField(DurationFieldType.MINUTES));
        assertEquals(3, period.getValue(DurationFieldType.DAYS));
    }

    /**
     * Tests mutable period.
     */
    @Test
    public void testMutablePeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(3) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.DAYS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        MutablePeriod mutablePeriod = period.toMutablePeriod();
        mutablePeriod.add(DurationFieldType.MINUTES, 60);
        assertEquals(3, period.getValue(DurationFieldType.DAYS));
        assertEquals(61, mutablePeriod.getValue(DurationFieldType.MINUTES));
    }

    /**
     * Tests equals.
     */
    @Test
    public void testEquals() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(3) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.DAYS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(3) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.DAYS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(4) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.DAYS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        assertTrue(period1.equals(period2));
        assertFalse(period1.equals(period3));
        assertFalse(period1.equals(null));
        assertFalse(period1.equals(""));
    }

    /**
     * Tests hash code.
     */
    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(3) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.DAYS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(3) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.DAYS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(4) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.DAYS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        int hashCode1 = period.hashCode();
        int hashCode2 = period2.hashCode();
        int hashCode3 = period3.hashCode();
        assertEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
    }

    /**
     * Tests compare to.
     */
    @Test
    public void testCompareTo() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(3) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.DAYS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.DAYS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(3) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.DAYS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        assertEquals(0, period1.compareTo(period3));
        assertEquals(1, period2.compareTo(period1));
        assertEquals(-1, period1.compareTo(period2));
    }

    /**
     * Tests invalid period type.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPeriodType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(3) {
            @Override
            public DurationFieldType getFieldType() {
                return null;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
    }

    /**
     * Tests null field type.
     */
    @Test(expected = NullPointerException.class)
    public void testNullFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(3) {
            @Override
            public DurationFieldType getFieldType() {
                return null;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
    }

    /**
     * Tests invalid chronology.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidChronology() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(3) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.DAYS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }

            @Override
            public Chronology getChronology() {
                return null;
            }
        };
    }

    /**
     * Tests non-UTC chronology.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNonUTCChronology() {
        Chronology nonUTCChrono = DateTimeUtils.getChronology(DateTimeUtils.UTC);
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(3) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.DAYS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }

            @Override
            public Chronology getChronology() {
                return nonUTCChrono;
            }
        };
    }

}