package org.joda.time.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DurationFieldType;
import org.joda.time.FieldUtils;
import org.joda.time.Instant;
import org.joda.time.LocalDateTime;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePartial;
import org.joda.time.base.BaseSingleFieldPeriod;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.FieldUtils;
import org.joda.time.field.FieldType;
import org.joda.time.format.DateTimeFormat;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testBetween() {
        final ReadableInstant readableInstant = new BaseDateTime(63072000000L, ISOChronology.getInstanceUTC()).toInstant();
        final ReadableInstant readableInstant2 = new BaseDateTime(63072001000L, ISOChronology.getInstanceUTC()).toInstant();
        final DurationFieldType durationFieldType = DurationFieldType.days();
        assertEquals(1, BaseSingleFieldPeriod.between(readableInstant, readableInstant2, durationFieldType));

        final ReadablePartial readablePartial = new LocalDateTime(2012, 1, 31, 12, 30, 0).toLocalDateTime();
        final ReadablePartial readablePartial2 = new LocalDateTime(2012, 1, 30, 12, 30, 0).toLocalDateTime();
        final ReadablePeriod readablePeriod = Period.days(1);
        assertEquals(1, BaseSingleFieldPeriod.between(readablePartial, readablePartial2, readablePeriod));

        final Chronology chronology = DateTimeUtils.getChronology(ISOChronology.getInstance());
        assertNull(BaseSingleFieldPeriod.between(null, readableInstant, durationFieldType));
        assertNull(BaseSingleFieldPeriod.between(readableInstant, null, durationFieldType));
        assertNull(BaseSingleFieldPeriod.between(readableInstant, readableInstant, null));
        assertNull(BaseSingleFieldPeriod.between(null, null, durationFieldType));

        assertNull(BaseSingleFieldPeriod.between(readablePartial, null, readablePeriod));
        assertNull(BaseSingleFieldPeriod.between(null, readablePartial, readablePeriod));
        assertNull(BaseSingleFieldPeriod.between(readablePartial, readablePartial2, null));
        assertNull(BaseSingleFieldPeriod.between(null, null, readablePeriod));

        assertNull(BaseSingleFieldPeriod.between(readablePartial, readablePartial2, null));
        assertNull(BaseSingleFieldPeriod.between(null, readablePartial, readablePeriod));
        assertNull(BaseSingleFieldPeriod.between(readablePartial, null, readablePeriod));
        assertNull(BaseSingleFieldPeriod.between(null, readablePartial2, readablePeriod));

        assertNull(BaseSingleFieldPeriod.between(readablePartial, readablePartial2, new PeriodType() {
            @Override
            public String getName() {
                return "Test";
            }

            @Override
            public boolean isTimeBased() {
                return true;
            }

            @Override
            public PeriodType getBaseType() {
                return null;
            }

            @Override
            public DurationFieldType[] getFieldTypes() {
                return new DurationFieldType[0];
            }
        }));

        final Instant instant = new BaseDateTime(63072000000L, ISOChronology.getInstanceUTC()).toInstant();
        final BaseSingleFieldPeriod testInstance = new TestSingleFieldPeriod(1);
        final BaseSingleFieldPeriod testInstance2 = new TestSingleFieldPeriod(2);
        assertNull(BaseSingleFieldPeriod.between(null, testInstance, testInstance.getFieldType()));
        assertNull(BaseSingleFieldPeriod.between(testInstance, null, testInstance.getFieldType()));
        assertNull(BaseSingleFieldPeriod.between(testInstance, testInstance, null));
        assertNull(BaseSingleFieldPeriod.between(null, null, testInstance.getFieldType()));

        assertEquals(1, BaseSingleFieldPeriod.between(instant, readableInstant, testInstance.getFieldType()));
        assertEquals(1, BaseSingleFieldPeriod.between(readableInstant, instant, testInstance.getFieldType()));
        assertEquals(-1, BaseSingleFieldPeriod.between(instant, testInstance2.getInstant(), testInstance.getFieldType()));
        assertEquals(-1, BaseSingleFieldPeriod.between(testInstance2.getInstant(), instant, testInstance.getFieldType()));
        assertEquals(0, BaseSingleFieldPeriod.between(instant, testInstance.getInstant(), testInstance.getFieldType()));
    }

    @Test
    public void testStandardPeriodIn() {
        final PeriodType periodType = PeriodType.standard();
        final DurationFieldType durationFieldType = DurationFieldType.days();

        assertEquals(1, BaseSingleFieldPeriod.standardPeriodIn(Period.days(1), 1));
        assertEquals(365, BaseSingleFieldPeriod.standardPeriodIn(Period.days(365), 1));
        assertEquals(365, BaseSingleFieldPeriod.standardPeriodIn(Period.days(365).withFields(new TestSingleFieldPeriod(1)), 1));
        assertEquals(365 * 2, BaseSingleFieldPeriod.standardPeriodIn(Period.days(365 * 2), 1));
        assertEquals(365 * 2, BaseSingleFieldPeriod.standardPeriodIn(Period.days(365 * 2).withFields(new TestSingleFieldPeriod(2)), 1));
        assertEquals(365 * 2, BaseSingleFieldPeriod.standardPeriodIn(Period.days(365).withFields(new TestSingleFieldPeriod(2)), 1));
        assertEquals(365 * 2, BaseSingleFieldPeriod.standardPeriodIn(Period.days(365 * 2).withFields(new TestSingleFieldPeriod(1)), 1));
        assertEquals(365 * 2, BaseSingleFieldPeriod.standardPeriodIn(Period.days(365).withFields(new TestSingleFieldPeriod(1), new TestSingleFieldPeriod(1)), 1));

        assertEquals(0, BaseSingleFieldPeriod.standardPeriodIn(null, 1));
        assertEquals(0, BaseSingleFieldPeriod.standardPeriodIn(Period.ZERO, 1));
        assertEquals(0, BaseSingleFieldPeriod.standardPeriodIn(Period.ZERO.withFields(new TestSingleFieldPeriod(0)), 1));
        assertEquals(0, BaseSingleFieldPeriod.standardPeriodIn(Period.ZERO.withFields(new TestSingleFieldPeriod(1)), 1));
        assertEquals(0, BaseSingleFieldPeriod.standardPeriodIn(Period.ZERO.withFields(new TestSingleFieldPeriod(1), new TestSingleFieldPeriod(0)), 1));

        try {
            BaseSingleFieldPeriod.standardPeriodIn(null, 1);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            BaseSingleFieldPeriod.standardPeriodIn(Period.days(1), 0);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            BaseSingleFieldPeriod.standardPeriodIn(Period.days(1), -1);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            BaseSingleFieldPeriod.standardPeriodIn(Period.days(1).withFields(new TestSingleFieldPeriod(1)), 0);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            BaseSingleFieldPeriod.standardPeriodIn(Period.days(1).withFields(new TestSingleFieldPeriod(1), new TestSingleFieldPeriod(-1)), 1);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testGetters() {
        final BaseSingleFieldPeriod testInstance = new TestSingleFieldPeriod(1);
        assertNotNull(testInstance.getFieldType());
        assertSame(TestSingleFieldPeriod.class, testInstance.getClass());
        assertEquals(1, testInstance.getValue());
        assertEquals(TestSingleFieldPeriod.class, testInstance.getPeriodType());
        assertEquals(1, testInstance.size());
        assertTrue(testInstance.isSupported(testInstance.getFieldType()));

        final DurationFieldType durationFieldType = testInstance.getFieldType();
        assertEquals(TestSingleFieldPeriod.class, durationFieldType.getField(DateTimeUtils.getChronology(DateTimeUtils.UTC)).getClass());
        assertEquals(TestSingleFieldPeriod.class, durationFieldType.getField(ISOChronology.getInstanceUTC()).getClass());
    }

    @Test
    public void testToPeriod() {
        final BaseSingleFieldPeriod testInstance = new TestSingleFieldPeriod(1);
        final Period expectedPeriod = Period.days(1);
        assertEquals(expectedPeriod, testInstance.toPeriod());
        assertEquals(expectedPeriod, testInstance.toMutablePeriod().toPeriod());
    }

    @Test
    public void testEquals() {
        final BaseSingleFieldPeriod testInstance = new TestSingleFieldPeriod(1);
        final BaseSingleFieldPeriod testInstance2 = new TestSingleFieldPeriod(1);
        final BaseSingleFieldPeriod testInstance3 = new TestSingleFieldPeriod(2);
        final TestSingleFieldPeriod testInstance4 = new TestSingleFieldPeriod(1);
        assertTrue(testInstance.equals(testInstance));
        assertTrue(testInstance.equals(testInstance2));
        assertTrue(testInstance.equals(testInstance4));
        assertFalse(testInstance.equals(testInstance3));
        assertFalse(testInstance.equals(null));
        assertFalse(testInstance.equals(""));
        assertFalse(testInstance.equals(new Object()));
    }

    @Test
    public void testHashCode() {
        final BaseSingleFieldPeriod testInstance = new TestSingleFieldPeriod(1);
        final BaseSingleFieldPeriod testInstance2 = new TestSingleFieldPeriod(1);
        final BaseSingleFieldPeriod testInstance3 = new TestSingleFieldPeriod(2);
        assertEquals(27 * (27 * 17 + 1) + TestSingleFieldPeriod.class.hashCode(), testInstance.hashCode());
        assertEquals(27 * (27 * 17 + 1) + TestSingleFieldPeriod.class.hashCode(), testInstance2.hashCode());
        assertNotEquals(testInstance.hashCode(), testInstance3.hashCode());
    }

    @Test
    public void testCompareTo() {
        final BaseSingleFieldPeriod testInstance = new TestSingleFieldPeriod(1);
        final BaseSingleFieldPeriod testInstance2 = new TestSingleFieldPeriod(1);
        final BaseSingleFieldPeriod testInstance3 = new TestSingleFieldPeriod(2);
        assertEquals(0, testInstance.compareTo(testInstance));
        assertEquals(0, testInstance.compareTo(testInstance2));
        assertEquals(-1, testInstance.compareTo(testInstance3));
    }

    static class TestSingleFieldPeriod extends BaseSingleFieldPeriod {

        public TestSingleFieldPeriod(final int iPeriod) {
            super(iPeriod);
        }

        @Override
        public DurationFieldType getFieldType() {
            return new TestFieldType();
        }

        @Override
        public PeriodType getPeriodType() {
            return PeriodType.standard();
        }

        static class TestFieldType extends DurationFieldType {

            @Override
            public String getName() {
                return "Test";
            }

            @Override
            public int getPrecision() {
                return 0;
            }

            @Override
            public int getLength() {
                return 1;
            }

            @Override
            public long getUnitMillis() {
                return 1;
            }

            @Override
            public FieldType getBaseType() {
                return null;
            }

            @Override
            public DurationFieldType[] getValidFields() {
                return new DurationFieldType[0];
            }

            @Override
            public boolean isPrecise() {
                return true;
            }
        }

        @Override
        public int getValue() {
            return super.getValue();
        }

        @Override
        public void setValue(final int iPeriod) {
            super.setValue(iPeriod);
        }

        @Override
        public Instant getInstant() {
            final Instant instant = new BaseDateTime(63072000000L + FieldUtils.safeMultiply(1, getValue()) * 86400000L, ISOChronology.getInstanceUTC()).toInstant();
            return instant;
        }
    }
}