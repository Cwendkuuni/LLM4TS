package org.joda.time.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.joda.time.*;
import org.joda.time.chrono.*;
import org.joda.time.field.*;
import org.junit.Test;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testBaseSingleFieldPeriod() {
        BaseSingleFieldPeriod period = new Days(7);
        assertEquals(Days.TYPE, period.getPeriodType());
        assertEquals(7, period.getValue());
        assertEquals(1, period.size());
        assertTrue(period.isSupported(Days.TYPE));
        assertSame(Days.TYPE, period.getFieldType());
        assertEquals(Period.ZERO.withFields(period), Period.ZERO.withFieldAdded(Days.TYPE, 7));
    }

    @Test
    public void testBaseSingleFieldPeriodConstructorWithValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(7);
        assertEquals(7, period.getValue());
        assertNull(period.getFieldType());
        assertNull(period.getPeriodType());
        assertEquals(1, period.size());
        assertTrue(period.isSupported(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBaseSingleFieldPeriodConstructorWithWrongValue() {
        new BaseSingleFieldPeriod(2);
    }

    @Test
    public void testBetween() {
        DateTime date1 = new DateTime(2022, 1, 1, 12, 0, 0, 0, DateTimeZone.UTC);
        DateTime date2 = new DateTime(2022, 1, 8, 12, 0, 0, 0, DateTimeZone.UTC);
        BaseSingleFieldPeriod period = new Days(7);

        assertEquals(604800000, BaseSingleFieldPeriod.between(date1, date2, Days.TYPE));
        assertEquals(7, BaseSingleFieldPeriod.between(date1, date2, period));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetweenWithNullReadableInstant() {
        new BaseSingleFieldPeriod().between(null, new DateTime(2022, 1, 1, 12, 0, 0, 0, DateTimeZone.UTC), Days.TYPE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetweenWithNullReadablePeriod() {
        new DateTime(2022, 1, 1, 12, 0, 0, 0, DateTimeZone.UTC).between(null, new DateTime(2022, 1, 1, 12, 0, 0, 0, DateTimeZone.UTC), Days.TYPE);
    }

    @Test
    public void testStandardPeriodIn() {
        DateTime date = new DateTime(2022, 1, 1, 12, 0, 0, 0, DateTimeZone.UTC);
        BaseSingleFieldPeriod period1 = new Hours(1);
        BaseSingleFieldPeriod period2 = new Days(1);
        BaseSingleFieldPeriod period3 = new Weeks(1);

        assertEquals(3600000, BaseSingleFieldPeriod.standardPeriodIn(period1, 1));
        assertEquals(86400000, BaseSingleFieldPeriod.standardPeriodIn(period1, 24 * 60));
        assertEquals(86400000, BaseSingleFieldPeriod.standardPeriodIn(period2, 1));
        assertEquals(86400000 * 7, BaseSingleFieldPeriod.standardPeriodIn(period2, 7 * 24 * 60));
        assertEquals(86400000 * 7 * 7, BaseSingleFieldPeriod.standardPeriodIn(period3, 1));
        assertEquals(604800000 * 365, BaseSingleFieldPeriod.standardPeriodIn(period3, 1 * 12 * 52 * 7));
        assertEquals(Period.ZERO.getMillis(), BaseSingleFieldPeriod.standardPeriodIn(date, 0));

        DateTime date2 = new DateTime(2022, 1, 2, 12, 0, 0, 0, DateTimeZone.UTC);
        assertEquals(BaseSingleFieldPeriod.standardPeriodIn(date, period1), Period.ZERO.getMillis() + 3600000);
        assertEquals(BaseSingleFieldPeriod.standardPeriodIn(date, period2), Period.ZERO.getMillis() + 86400000);
        assertEquals(BaseSingleFieldPeriod.standardPeriodIn(date, period3), Period.ZERO.getMillis() + 604800000 * 7);
        assertEquals(BaseSingleFieldPeriod.standardPeriodIn(date2, period1), Period.ZERO.getMillis() + 3600000);
        assertEquals(BaseSingleFieldPeriod.standardPeriodIn(date2, period2), Period.ZERO.getMillis() + 86400000);
        assertEquals(BaseSingleFieldPeriod.standardPeriodIn(date2, period3), Period.ZERO.getMillis() + 604800000 * 7);
    }

    @Test
    public void testEqualsAndHashCode() {
        BaseSingleFieldPeriod period1 = new Days(7);
        BaseSingleFieldPeriod period2 = new Days(7);
        BaseSingleFieldPeriod period3 = new Hours(1);

        assertEquals(period1, period1);
        assertEquals(period1, period2);
        assertEquals(period1.hashCode(), period2.hashCode());
        assertNotSame(period1, period3);
        assertNotSame(period1.hashCode(), period3.hashCode());
    }

    @Test
    public void testCompareTo() {
        BaseSingleFieldPeriod period1 = new Days(7);
        BaseSingleFieldPeriod period2 = new Hours(24 * 7);
        BaseSingleFieldPeriod period3 = new Days(5);

        assertEquals(0, period1.compareTo(period1));
        assertEquals(1, period1.compareTo(period2));
        assertEquals(-1, period1.compareTo(period3));
    }
}