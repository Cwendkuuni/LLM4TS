package org.joda.time.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.joda.time.Chronology;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePeriod;
import org.joda.time.ReadablePartial;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.FieldUtils;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testBetween() {
        // Test with Instants
        ReadableInstant start = ISOChronology.getInstanceUTC().getInstant(2015, 1, 1, 0, 0, 0, 0, 0);
        ReadableInstant end = ISOChronology.getInstanceUTC().getInstant(2015, 1, 2, 0, 0, 0, 0, 0);
        assertEquals(24 * 60 * 60 * 1000, BaseSingleFieldPeriod.between(start, end, DurationFieldType.hours()));

        // Test with Partial Datetimes
        ReadablePartial startPartial = new org.joda.time.local.LocalDate(2015, 1, 1);
        ReadablePartial endPartial = new org.joda.time.local.LocalDate(2015, 1, 2);
        ReadablePeriod zeroInstance = Period.ZERO;
        assertEquals(2, BaseSingleFieldPeriod.between(startPartial, endPartial, zeroInstance));

        // Test negative between
        startPartial = new org.joda.time.local.LocalDate(2015, 1, 2);
        endPartial = new org.joda.time.local.LocalDate(2015, 1, 1);
        assertEquals(-2, BaseSingleFieldPeriod.between(startPartial, endPartial, zeroInstance));

        // Test with invalid arguments
        assertNull(BaseSingleFieldPeriod.between(null, end, DurationFieldType.hours()));
        assertNull(BaseSingleFieldPeriod.between(start, null, DurationFieldType.hours()));
        assertNull(BaseSingleFieldPeriod.between(start, end, null));
    }

    @Test
    public void testStandardPeriodIn() {
        ReadablePeriod period = new MutablePeriod().withDays(2).withHours(3).withMinutes(45).toImmutable();
        assertEquals(53 * 24 + 3 * 60 + 45, BaseSingleFieldPeriod.standardPeriodIn(period, 1000));
        assertEquals(53 * 24 * 60 * 60 * 1000 + 3 * 60 * 60 * 1000 + 45 * 1000, BaseSingleFieldPeriod.standardPeriodIn(period, 1000000));

        // Test with non-supporting period
        ReadablePeriod nonSupportingPeriod = new MutablePeriod().withMonths(2).toImmutable();
        assertNull(BaseSingleFieldPeriod.standardPeriodIn(nonSupportingPeriod, 1000));
    }

    @Test
    public void testBaseSingleFieldPeriod() {
        DurationFieldType fieldType = DurationFieldType.days();
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(1) {
            @Override
            public DurationFieldType getFieldType() {
                return fieldType;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(2) {
            @Override
            public DurationFieldType getFieldType() {
                return fieldType;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        assertEquals(1, period1.compareTo(period1));
        assertEquals(0, period1.compareTo(period1));
        assertEquals(-1, period1.compareTo(period2));

        assertEquals(1, period1.getValue());
        assertEquals(2, period2.getValue());

        assertEquals(fieldType, period1.getFieldType());
        assertEquals(fieldType, period2.getFieldType());

        assertEquals(PeriodType.standard(), period1.getPeriodType());
        assertEquals(PeriodType.standard(), period2.getPeriodType());

        assertEquals(period1.getClass(), period1.getClass());
        assertEquals(period2.getClass(), period2.getClass());

        assertEquals(period1, period1.toPeriod());
        MutablePeriod mutablePeriod = new MutablePeriod();
        mutablePeriod.add(period1);
        assertEquals(period1, mutablePeriod.toImmutable());

        assertEquals(period1.getClass(), period1.getClass());
        assertEquals(period1.getClass().getSuperclass(), ReadablePeriod.class);
        assertEquals(period1.getClass().getSuperclass().getSuperclass(), Serializable.class);
        assertTrue(period1 instanceof Comparable);
    }

}