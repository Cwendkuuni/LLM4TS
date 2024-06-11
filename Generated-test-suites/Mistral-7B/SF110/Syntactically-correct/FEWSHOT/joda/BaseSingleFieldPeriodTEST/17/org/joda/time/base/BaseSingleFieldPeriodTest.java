package org.joda.time.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DurationFieldType;
import org.joda.time.FieldUtils;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePartial;
import org.joda.time.DateTimeUtils;
import org.junit.Test;

public abstract class BaseSingleFieldPeriodTest {

    @Test
    public void testGetFieldType() {
        assertNotNull(getFieldType());
    }

    @Test
    public void testGetPeriodType() {
        assertNotNull(getPeriodType());
    }

    @Test
    public void testSize() {
        assertEquals(1, size());
    }

    @Test
    public void testGetFieldTypeAt() {
        assertSame(getFieldType(), getFieldType(0));
    }

    @Test
    public void testGetValueAt() {
        assertSame(getValue(), getValue(0));
    }

    @Test
    public void testGetValue() {
        assertEquals(getValue(), getValue());
    }

    @Test
    public void testSetValue() {
        int originalValue = getValue();
        setValue(42);
        assertEquals(42, getValue());
        setValue(originalValue);
        assertEquals(originalValue, getValue());
    }

    @Test
    public void testEquals() {
        BaseSingleFieldPeriod other = createInstance(getValue());
        assertTrue(equals(other));
        assertFalse(equals(null));
        assertFalse(equals(new Object()));
        assertFalse(equals(new BaseSingleFieldPeriod(getValue() + 1)));
    }

    @Test
    public void testHashCode() {
        int hashCode = getClass().hashCode();
        int value = getValue();
        BaseSingleFieldPeriod other = createInstance(value);
        int otherHashCode = other.hashCode();
        assertEquals(hashCode, (hashCode * 31 + value) * 31 + getFieldType().hashCode());
        assertEquals(otherHashCode, (otherHashCode * 31 + value) * 31 + getFieldType().hashCode());
    }

    @Test
    public void testCompareToZero() {
        assertEquals(0, compareTo(BaseSingleFieldPeriod.ZERO));
    }

    @Test
    public void testCompareToNegative() {
        BaseSingleFieldPeriod other = createInstance(-getValue());
        if (getValue() > 0) {
            assertEquals(-1, compareTo(other));
        } else {
            assertEquals(1, compareTo(other));
        }
    }

    @Test
    public void testCompareToPositive() {
        BaseSingleFieldPeriod other = createInstance(getValue() + 1);
        assertEquals(1, compareTo(other));
    }

    @Test
    public void testCompareToSameInstance() {
        assertEquals(0, compareTo(this));
    }

    @Test(expected = ClassCastException.class)
    public void testCompareToDifferentClass() {
        BaseSingleFieldPeriod other = new DifferentClass();
        compareTo(other);
    }

    @Test
    public void testToPeriod() {
        Period period = toPeriod();
        assertEquals(getValue(), period.getValues(getFieldType()).get(0));
    }

    @Test
    public void testToMutablePeriod() {
        MutablePeriod mutablePeriod = toMutablePeriod();
        assertEquals(getValue(), mutablePeriod.getValue(getFieldType()));
    }

    @Test
    public void testBetweenReadableInstantReadableInstant() {
        BaseDateTime baseDateTime = new BaseDateTime(63072000000L, DateTimeUtils.UTC);
        BaseSingleFieldPeriod period = createInstance(getValue());
        int result = BaseSingleFieldPeriod.between(baseDateTime, baseDateTime.plus(period), getFieldType());
        assertEquals(getValue(), result);
    }

    @Test
    public void testBetweenReadableInstantNull() {
        BaseDateTime baseDateTime = new BaseDateTime(63072000000L, DateTimeUtils.UTC);
        assertThrows(IllegalArgumentException.class, () -> BaseSingleFieldPeriod.between(baseDateTime, null, getFieldType()));
    }

    @Test
    public void testBetweenReadableInstantUnknownFieldType() {
        BaseDateTime baseDateTime = new BaseDateTime(63072000000L, DateTimeUtils.UTC);
        assertThrows(IllegalArgumentException.class, () -> BaseSingleFieldPeriod.between(baseDateTime, baseDateTime.plus(this), new DurationFieldType("foo"){}) );
    }

    @Test
    public void testBetweenReadablePartialReadablePeriod() {
        ReadablePartial readablePartial = new BaseDateTime(63072000000L, DateTimeUtils.UTC).toLocalDateTime().with(new LocalDateTime().withDate(2022, 12, 31));
        BaseSingleFieldPeriod period = createInstance(getValue());
        Chronology chronology = DateTimeUtils.getChronology(readablePartial.getChronology());
        int result = BaseSingleFieldPeriod.between(readablePartial, readablePartial.plus(period), period.getClass().cast(period));
        assertEquals(getValue(), result);
    }

    @Test
    public void testBetweenReadablePartialNull() {
        ReadablePartial readablePartial = null;
        BaseSingleFieldPeriod period = createInstance(getValue());
        assertThrows(IllegalArgumentException.class, () -> BaseSingleFieldPeriod.between(readablePartial, readablePartial, period.getClass().cast(period)) );
    }

    @Test
    public void testBetweenReadablePartialUnknownFieldType() {
        ReadablePartial readablePartial = new BaseDateTime(63072000000L, DateTimeUtils.UTC).toLocalDateTime().with(new LocalDateTime().withDate(2022, 12, 31));
        BaseSingleFieldPeriod period = createInstance(getValue());
        assertThrows(IllegalArgumentException.class, () -> BaseSingleFieldPeriod.between(readablePartial, readablePartial, new DurationFieldType("foo"){}) );
    }

    @Test
    public void testBetweenReadablePeriodReadablePeriod() {
        BaseSingleFieldPeriod period1 = createInstance(getValue());
        BaseSingleFieldPeriod period2 = createInstance(-getValue());
        int result = BaseSingleFieldPeriod.between(period1, period2, period1.getClass().cast(period1));
        assertEquals(-getValue(), result);
    }

    @Test
    public void testBetweenReadablePeriodNull() {
        BaseSingleFieldPeriod period = createInstance(getValue());
        assertThrows(IllegalArgumentException.class, () -> BaseSingleFieldPeriod.between(null, period, period.getClass().cast(period)) );
    }

    @Test
    public void testBetweenReadablePeriodUnknownFieldType() {
        BaseSingleFieldPeriod period1 = createInstance(getValue());
        BaseSingleFieldPeriod period2 = createInstance(-getValue());
        assertThrows(IllegalArgumentException.class, () -> BaseSingleFieldPeriod.between(period1, period2, new DurationFieldType("foo"){}) );
    }

    @Test
    public void testStandardPeriodInReadablePeriod() {
        BaseSingleFieldPeriod period = createInstance(getValue());
        long result = BaseSingleFieldPeriod.standardPeriodIn(period, 7);
        assertEquals(getSecondsPerMillennium() * 7, result);
    }

    @Test
    public void testStandardPeriodInNull() {
        assertNull(BaseSingleFieldPeriod.standardPeriodIn(null, 7));
    }

    @Test
    public void testStandardPeriodInUnknownFieldType() {
        assertThrows(IllegalArgumentException.class, () -> BaseSingleFieldPeriod.standardPeriodIn(createInstance(1), new DurationFieldType("foo"){}) );
    }

    protected abstract BaseSingleFieldPeriod createInstance(int i);

    protected abstract DurationFieldType getFieldType();

    protected abstract PeriodType getPeriodType();

    protected abstract long getSecondsPerMillennium();

    protected static class DifferentClass extends BaseSingleFieldPeriod {
        public DifferentClass() {
            super(0);
        }

        @Override
        public DurationFieldType getFieldType() {
            throw new UnsupportedOperationException();
        }

        @Override
        public PeriodType getPeriodType() {
            throw new UnsupportedOperationException();
        }
    }
}