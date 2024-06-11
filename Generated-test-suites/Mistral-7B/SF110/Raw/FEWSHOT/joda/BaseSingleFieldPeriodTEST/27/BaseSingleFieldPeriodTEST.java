package org.joda.time.base;

import static org.junit.Assert.*;

import org.joda.time.*;
import org.joda.time.chrono.*;
import org.joda.time.field.*;
import org.junit.Test;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testBaseSingleFieldPeriod() {
        BaseSingleFieldPeriod instance = new MySingleFieldPeriod(5);
        assertEquals(5, instance.getValue());
        instance.setValue(10);
        assertEquals(10, instance.getValue());

        DurationFieldType fieldType = instance.getFieldType();
        assertEquals(DurationFieldType.FIELD_SECONDS, fieldType);
        assertEquals(PeriodType.MILLISECONDS, instance.getPeriodType());
        assertEquals(1, instance.size());

        assertEquals(instance.getClass(), instance.getFieldType().getField(ISOChronology.getInstanceUTC()).getFieldClass());
        assertEquals(MySingleFieldPeriod.class, instance.getClass());

        assertFalse(instance.isSupported(DurationFieldType.FIELD_MILLISECONDS));
        assertTrue(instance.isSupported(DurationFieldType.FIELD_SECONDS));
        assertTrue(instance.isSupported(DurationFieldType.FIELD_MILLISECONDS.getUnit(DurationField.HOURS)));

        Period period = instance.toPeriod();
        assertEquals(0, period.getValues().length);
        assertEquals(instance, period.withFields(instance));

        MutablePeriod mutablePeriod = instance.toMutablePeriod();
        mutablePeriod.add(instance);
        assertEquals(instance, mutablePeriod.toPeriod());

        assertEquals(instance, instance.toPeriod().withFields(instance));
    }

    @Test
    public void testBetween() {
        ReadableInstant start = DateTime.parse("1972-01-01T00:00:00.001Z");
        ReadableInstant end = DateTime.parse("1972-01-01T00:00:01.002Z");
        BaseSingleFieldPeriod p1 = new MySingleFieldPeriod(1);
        BaseSingleFieldPeriod p2 = new MySingleFieldPeriod(2);

        assertEquals(1, BaseSingleFieldPeriod.between(start, end, DurationFieldType.FIELD_SECONDS));
        assertEquals(-1, BaseSingleFieldPeriod.between(end, start, DurationFieldType.FIELD_SECONDS));
        assertEquals(0, BaseSingleFieldPeriod.between(start, start, DurationFieldType.FIELD_SECONDS));

        assertEquals(1, BaseSingleFieldPeriod.between(p1, p2, DurationFieldType.FIELD_SECONDS));
        assertEquals(-1, BaseSingleFieldPeriod.between(p2, p1, DurationFieldType.FIELD_SECONDS));
        assertEquals(0, BaseSingleFieldPeriod.between(p1, p1, DurationFieldType.FIELD_SECONDS));

        try {
            BaseSingleFieldPeriod.between(null, end, DurationFieldType.FIELD_SECONDS);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            BaseSingleFieldPeriod.between(start, null, DurationFieldType.FIELD_SECONDS);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testStandardPeriodIn() {
        BaseSingleFieldPeriod p1 = new MySingleFieldPeriod(5);
        assertEquals(60, BaseSingleFieldPeriod.standardPeriodIn(p1, 60));
        assertEquals(21600, BaseSingleFieldPeriod.standardPeriodIn(p1, 60 * 60));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStandardPeriodInWithNullPeriod() {
        BaseSingleFieldPeriod.standardPeriodIn(null, 60);
    }

    static class MySingleFieldPeriod extends BaseSingleFieldPeriod {
        MySingleFieldPeriod(int i) {
            super(i);
        }

        @Override
        public DurationFieldType getFieldType() {
            return DurationFieldType.FIELD_SECONDS;
        }

        @Override
        public PeriodType getPeriodType() {
            return PeriodType.MILLISECONDS;
        }
    }
}