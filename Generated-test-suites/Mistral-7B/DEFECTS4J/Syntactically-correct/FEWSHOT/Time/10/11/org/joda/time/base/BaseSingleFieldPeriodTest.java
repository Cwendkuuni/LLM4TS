package org.joda.time.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import org.joda.time.Chronology;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.base.BaseSingleFieldPeriod;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.FieldUtils;
import org.junit.Test;

/**
 * Test cases for BaseSingleFieldPeriod class.
 */
public class BaseSingleFieldPeriodTest {

    /**
     * Test case for between method with ReadableInstant.
     */
    @Test
    public void testBetweenReadableInstant() {
        Chronology chrono = ISOChronology.getInstanceUTC();
        ReadableInstant start = chrono.getInstant(0L);
        ReadableInstant end = chrono.getInstant(1000L);
        int period = BaseSingleFieldPeriod.between(start, end, DurationFieldType.days());
        assertEquals(1, period);
    }

    /**
     * Test case for between method with ReadablePartial.
     */
    @Test
    public void testBetweenReadablePartial() {
        Chronology chrono = ISOChronology.getInstanceUTC();
        ReadablePartial start = new LocalDate(chrono, 2015, 1, 1);
        ReadablePartial end = new LocalDate(chrono, 2015, 1, 2);
        int period = BaseSingleFieldPeriod.between(start, end, PeriodType.days());
        assertEquals(1, period);
    }

    /**
     * Test case for standardPeriodIn method.
     */
    @Test
    public void testStandardPeriodIn() {
        ReadablePeriod period = new Period(1, 2, 3, 4, 5, 6, 7);
        int millisPerUnit = 1000 * 60 * 60 * 24; // 1 day
        int periodInDays = BaseSingleFieldPeriod.standardPeriodIn(period, millisPerUnit);
        assertEquals(23, periodInDays); // 23 days in the given period
    }

    /**
     * Test case for creating BaseSingleFieldPeriod instance.
     */
    @Test
    public void testBaseSingleFieldPeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(123) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.days();
            }
        };

        assertEquals(123, period.getValue());
        assertEquals(DurationFieldType.days(), period.getFieldType());
        assertEquals(PeriodType.days(), period.getPeriodType());
    }

    /**
     * Test case for comparing two BaseSingleFieldPeriod instances.
     */
    @Test
    public void testCompareTo() {
        BaseSingleFieldPeriod p1 = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.days();
            }
        };
        BaseSingleFieldPeriod p2 = new BaseSingleFieldPeriod(7) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.days();
            }
        };

        assertEquals(-2, p2.compareTo(p1));
        assertEquals(2, p1.compareTo(p2));
        assertEquals(0, p1.compareTo(p1));
    }

    /**
     * Test case for equals method.
     */
    @Test
    public void testEquals() {
        BaseSingleFieldPeriod p1 = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.days();
            }
        };
        BaseSingleFieldPeriod p2 = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.days();
            }
        };
        BaseSingleFieldPeriod p3 = new BaseSingleFieldPeriod(7) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.days();
            }
        };

        assertEquals(p1, p1);
        assertEquals(p1, p2);
        assertNotEquals(p1, p3);
    }

    /**
     * Test case for hashCode method.
     */
    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod p1 = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.days();
            }
        };
        BaseSingleFieldPeriod p2 = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.days();
            }
        };
        BaseSingleFieldPeriod p3 = new BaseSingleFieldPeriod(7) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.days();
            }
        };

        int p1HashCode = p1.hashCode();
        int p2HashCode = p2.hashCode();
        int p3HashCode = p3.hashCode();

        assertEquals(p1.hashCode(), p1.hashCode());
        assertEquals(p1.hashCode(), p2.hashCode());
        assertNotEquals(p1.hashCode(), p3.hashCode());
    }
}