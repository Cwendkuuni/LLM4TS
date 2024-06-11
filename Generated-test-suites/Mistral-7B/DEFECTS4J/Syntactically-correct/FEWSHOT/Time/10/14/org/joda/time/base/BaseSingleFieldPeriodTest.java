package org.joda.time.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

import org.joda.time.BaseJodaTest;
import org.joda.time.Chronology;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.base.BaseSingleFieldPeriod;
import org.joda.time.chrono.ISOChronology;

public class BaseSingleFieldPeriodTest extends BaseJodaTest {

    @Test
    public void testBaseSingleFieldPeriod() {
        // Test constructor
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(42);
        assertEquals(42, period.getValue());
        assertEquals(DurationFieldType.DAYS, period.getFieldType());
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertEquals(1, period.size());

        // Test equals
        BaseSingleFieldPeriod other = new BaseSingleFieldPeriod(42);
        assertEquals(period, other);

        // Test not equals
        BaseSingleFieldPeriod other2 = new BaseSingleFieldPeriod(43);
        assertNotEquals(period, other2);

        // Test hashCode
        int h1 = period.hashCode();
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(42);
        int h2 = period2.hashCode();
        assertEquals(h1, h2);

        // Test compareTo
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(41);
        int c1 = period.compareTo(period3);
        assertEquals(1, c1);

        BaseSingleFieldPeriod period4 = new BaseSingleFieldPeriod(43);
        int c2 = period.compareTo(period4);
        assertEquals(-1, c2);

        BaseSingleFieldPeriod period5 = new BaseSingleFieldPeriod(42);
        int c3 = period.compareTo(period5);
        assertEquals(0, c3);

        // Test getValue
        assertEquals(42, period.getValue());

        // Test setValue
        period.setValue(123);
        assertEquals(123, period.getValue());

        // Test getFieldType
        assertEquals(DurationFieldType.DAYS, period.getFieldType());

        // Test getPeriodType
        assertEquals(PeriodType.standard(), period.getPeriodType());

        // Test isSupported
        assertEquals(true, period.isSupported(DurationFieldType.DAYS));
        assertEquals(false, period.isSupported(DurationFieldType.HOURS));

        // Test toPeriod
        Period p = period.toPeriod();
        assertEquals(42, p.getFields().get(0));
        assertEquals(DurationFieldType.DAYS, p.getFieldType(0));
        assertEquals(PeriodType.standard(), p.getPeriodType());

        // Test toMutablePeriod
        MutablePeriod mp = period.toMutablePeriod();
        assertEquals(42, mp.getFields().get(0));
        assertEquals(DurationFieldType.DAYS, mp.getFieldType(0));
        assertEquals(PeriodType.standard(), mp.getPeriodType());

        // Test withFields
        Period p2 = Period.ZERO.withFields(period);
        assertEquals(42, p2.getFields().get(0));
        assertEquals(DurationFieldType.DAYS, p2.getFieldType(0));
        assertEquals(PeriodType.standard(), p2.getPeriodType());

        // Test withFields and check that it creates a new instance
        Period p3 = Period.ZERO.withFields(period);
        Period p4 = Period.ZERO.withFields(period);
        assertNotSame(p3, p4);

        // Test standardPeriodIn
        long millisPerDay = 24 * 60 * 60 * 1000L * 30; // 30 days
        assertEquals(30, BaseSingleFieldPeriod.standardPeriodIn(period, millisPerDay));

        // Test between
        Chronology iso = ISOChronology.getInstanceUTC();
        ReadableInstant start = iso.getInstant(2010, 1, 1, 0, 0, 0, 0, 0);
        ReadableInstant end = iso.getInstant(2010, 1, 2, 0, 0, 0, 0, 0);
        assertEquals(42, BaseSingleFieldPeriod.between(start, end, DurationFieldType.DAYS));

        // Test between with partials
        ReadablePartial start2 = iso.getLocalDate(2010, 1, 1).withFieldAdded(DateTimeConstants.HOUR_OF_DAY, 12);
        ReadablePartial end2 = iso.getLocalDate(2010, 1, 2).withFieldAdded(DateTimeConstants.HOUR_OF_DAY, 13);
        assertEquals(1, BaseSingleFieldPeriod.between(start2, end2, period));

        // Test null check
        assertNull(BaseSingleFieldPeriod.between(null, null, null));
    }

}