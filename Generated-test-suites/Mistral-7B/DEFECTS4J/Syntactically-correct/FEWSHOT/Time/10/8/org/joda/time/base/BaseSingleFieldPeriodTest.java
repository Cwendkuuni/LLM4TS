package org.joda.time.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.joda.time.BaseDateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DurationFieldType;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.FieldUtils;
import org.joda.time.test.BigMillerTest;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testBetween() {
        BaseSingleFieldPeriodTest.testBetween(BaseSingleFieldPeriodTest::instantBetween, true, false);
        BaseSingleFieldPeriodTest.testBetween(BaseSingleFieldPeriodTest::partialBetween, true, true);
    }

    @Test
    public void testStandardPeriodIn() {
        List<Class<? extends BaseSingleFieldPeriod>> types = Arrays.asList(Days.class, Hours.class,
                Minutes.class, Seconds.class);
        for (Class<? extends BaseSingleFieldPeriod> type : types) {
            BaseSingleFieldPeriod period = createPeriod(type, 3);
            long millisPerUnit = FieldUtils.millisPerDay();
            int standardPeriod = BaseSingleFieldPeriod.standardPeriodIn(period, millisPerUnit);
            assertEquals("standardPeriodIn(" + type.getName() + ") returned incorrect value", 3, standardPeriod);
        }
    }

    @Test
    public void testGetValue() {
        List<Class<? extends BaseSingleFieldPeriod>> types = Arrays.asList(Days.class, Hours.class,
                Minutes.class, Seconds.class);
        for (Class<? extends BaseSingleFieldPeriod> type : types) {
            BaseSingleFieldPeriod period = createPeriod(type, 3);
            int value = period.getValue();
            assertEquals("getValue() returned incorrect value", 3, value);
        }
    }

    @Test
    public void testSetValue() {
        List<Class<? extends BaseSingleFieldPeriod>> types = Arrays.asList(Days.class, Hours.class,
                Minutes.class, Seconds.class);
        for (Class<? extends BaseSingleFieldPeriod> type : types) {
            BaseSingleFieldPeriod period = createPeriod(type, 3);
            period.setValue(1);
            int value = period.getValue();
            assertEquals("setValue() and getValue() did not return expected values", 1, value);
            period.setValue(3);
            value = period.getValue();
            assertEquals("setValue() and getValue() did not return expected values", 3, value);
        }
    }

    @Test
    public void testGetFieldType() {
        List<Class<? extends BaseSingleFieldPeriod>> types = Arrays.asList(Days.class, Hours.class,
                Minutes.class, Seconds.class);
        for (Class<? extends BaseSingleFieldPeriod> type : types) {
            BaseSingleFieldPeriod period = createPeriod(type, 3);
            DurationFieldType fieldType = period.getFieldType();
            assertEquals("getFieldType() returned incorrect type", type.getSimpleName(), fieldType.getName());
        }
    }

    @Test
    public void testGetPeriodType() {
        List<Class<? extends BaseSingleFieldPeriod>> types = Arrays.asList(Days.class, Hours.class,
                Minutes.class, Seconds.class);
        for (Class<? extends BaseSingleFieldPeriod> type : types) {
            BaseSingleFieldPeriod period = createPeriod(type, 3);
            PeriodType periodType = period.getPeriodType();
            assertEquals("getPeriodType() returned incorrect type", PeriodType.standard(), periodType);
        }
    }

    @Test
    public void testSize() {
        List<Class<? extends BaseSingleFieldPeriod>> types = Arrays.asList(Days.class, Hours.class,
                Minutes.class, Seconds.class);
        for (Class<? extends BaseSingleFieldPeriod> type : types) {
            BaseSingleFieldPeriod period = createPeriod(type, 3);
            int size = period.size();
            assertEquals("size() returned incorrect value", 1, size);
        }
    }

    @Test
    public void testGetFieldTypeIndex() {
        List<Class<? extends BaseSingleFieldPeriod>> types = Arrays.asList(Days.class, Hours.class,
                Minutes.class, Seconds.class);
        for (Class<? extends BaseSingleFieldPeriod> type : types) {
            BaseSingleFieldPeriod period = createPeriod(type, 3);
            DurationFieldType fieldType = period.getFieldType(0);
            assertEquals("getFieldType(0) returned incorrect type", type.getSimpleName(), fieldType.getName());
        }
    }

    @Test
    public void testGetValueIndex() {
        List<Class<? extends BaseSingleFieldPeriod>> types = Arrays.asList(Days.class, Hours.class,
                Minutes.class, Seconds.class);
        for (Class<? extends BaseSingleFieldPeriod> type : types) {
            BaseSingleFieldPeriod period = createPeriod(type, 3);
            int value = period.getValue(0);
            assertEquals("getValue(0) returned incorrect value", 3, value);
        }
    }

    @Test
    public void testGetValueWithDifferentFieldTypes() {
        List<Class<? extends BaseSingleFieldPeriod>> types = Arrays.asList(Days.class, Hours.class,
                Minutes.class, Seconds.class);
        for (Class<? extends BaseSingleFieldPeriod> type1 : types) {
            for (Class<? extends BaseSingleFieldPeriod> type2 : types) {
                if (type1 == type2) {
                    continue;
                }
                BaseSingleFieldPeriod period1 = createPeriod(type1, 3);
                int value1 = period1.getValue(0);
                assertEquals("getValue(0) returned incorrect value for " + type1.getName(), 3, value1);
                BaseSingleFieldPeriod period2 = createPeriod(type2, 3);
                int value2 = period2.getValue(0);
                assertEquals("getValue(0) returned incorrect value for " + type2.getName(), 3, value2);
                int value = period1.getValue(1);
                assertNull("getValue(1) should return null for different field types", value);
            }
        }
    }

    @Test
    public void testToPeriod() {
        List<Class<? extends BaseSingleFieldPeriod>> types = Arrays.asList(Days.class, Hours.class,
                Minutes.class, Seconds.class);
        for (Class<? extends BaseSingleFieldPeriod> type : types) {
            BaseSingleFieldPeriod period = createPeriod(type, 3);
            Period toPeriod = period.toPeriod();
            assertEquals("toPeriod() returned incorrect period", new Period(3, PeriodType.standard()), toPeriod);
        }
    }

    @Test
    public void testToMutablePeriod() {
        List<Class<? extends BaseSingleFieldPeriod>> types = Arrays.asList(Days.class, Hours.class,
                Minutes.class, Seconds.class);
        for (Class<? extends BaseSingleFieldPeriod> type : types) {
            BaseSingleFieldPeriod period = createPeriod(type, 3);
            MutablePeriod toMutablePeriod = period.toMutablePeriod();
            assertEquals("toMutablePeriod() returned incorrect period", new MutablePeriod(3, PeriodType.standard()), toMutablePeriod);
        }
    }

    @Test
    public void testEquals() {
        List<Class<? extends BaseSingleFieldPeriod>> types = Arrays.asList(Days.class, Hours.class,
                Minutes.class, Seconds.class);
        for (Class<? extends BaseSingleFieldPeriod> type : types) {
            BaseSingleFieldPeriod period1 = createPeriod(type, 3);
            BaseSingleFieldPeriod period2 = createPeriod(type, 3);
            BaseSingleFieldPeriod period3 = createPeriod(type, 4);
            assertEquals("equals() returned incorrect value for equal periods", period1, period1);
            assertEquals("equals() returned incorrect value for equal periods", period1, period2);
            assertFalse("equals() returned incorrect value for different periods", period1, period3);
        }
    }

    @Test
    public void testHashCode() {
        List<Class<? extends BaseSingleFieldPeriod>> types = Arrays.asList(Days.class, Hours.class,
                Minutes.class, Seconds.class);
        for (Class<? extends BaseSingleFieldPeriod> type : types) {
            BaseSingleFieldPeriod period1 = createPeriod(type, 3);
            BaseSingleFieldPeriod period2 = createPeriod(type, 3);
            BaseSingleFieldPeriod period3 = createPeriod(type, 4);
            int hashCode1 = period1.hashCode();
            int hashCode2 = period1.hashCode();
            assertEquals("hashCode() returned incorrect value for equal periods", hashCode1, hashCode2);
            assertNotEquals("hashCode() returned incorrect value for different periods", hashCode1, hashCode3 = period3.hashCode());
        }
    }

    @Test
    public void testCompareTo() {
        List<Class<? extends BaseSingleFieldPeriod>> types = Arrays.asList(Days.class, Hours.class,
                Minutes.class, Seconds.class);
        for (Class<? extends BaseSingleFieldPeriod> type : types) {
            BaseSingleFieldPeriod period1 = createPeriod(type, 3);
            BaseSingleFieldPeriod period2 = createPeriod(type, 2);
            BaseSingleFieldPeriod period3 = createPeriod(type, 4);
            int comparison = period1.compareTo(period2);
            assertTrue("compareTo() returned incorrect comparison for period1 > period2", comparison > 0);
            comparison = period1.compareTo(period1);
            assertEquals("compareTo() returned incorrect comparison for period1 == period1", 0, comparison);
            comparison = period1.compareTo(period3);
            assertTrue("compareTo() returned incorrect comparison for period1 < period3", comparison < 0);
        }
    }

    private static int instantBetween(ReadableInstant start, ReadableInstant end, DurationFieldType field) {
        return BaseSingleFieldPeriod.between(start, end, field);
    }

    private static int partialBetween(ReadablePartial start, ReadablePartial end, ReadablePeriod zeroInstance) {
        return BaseSingleFieldPeriod.between(start, end, zeroInstance);
    }

    private static <T extends BaseSingleFieldPeriod> T createPeriod(Class<T> clazz, int value) {
        try {
            T period = (T) clazz.newInstance();
            period.setValue(value);
            return period;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static class Days extends BaseSingleFieldPeriod {
    }

    static class Hours extends BaseSingleFieldPeriod {
    }

    static class Minutes extends BaseSingleFieldPeriod {
    }

    static class Seconds extends BaseSingleFieldPeriod {
    }

    static class BigMillerTest implements ReadableInstant, ReadablePartial {

        private final long millis;

        public BigMillerTest(long millis) {
            this.millis = millis;
        }

        @Override
        public long getMillis() {
            return millis;
        }

        @Override
        public Chronology getChronology() {
            return ISOChronology.getInstanceUTC();
        }

        @Override
        public long getInstant() {
            return millis;
        }

        @Override
        public long getMillis(DurationField field) {
            return 0;
        }

        @Override
        public int get(int field) {
            switch (field) {
                case DateTimeConstants.MILLISECS:
                    return (int) (millis % 1000);
                case DateTimeConstants.SECS:
                    return (int) (millis / 1000);
                case DateTimeConstants.MINS:
                    return (int) ((millis / (1000 * 60)));
                case DateTimeConstants.HOURS:
                    return (int) ((millis / (1000 * 60 * 60)));
                case DateTimeConstants.DAYS:
                    return (int) ((millis / (1000L * 60 * 60 * 24)));
                default:
                    throw new IllegalArgumentException();
            }
        }

        @Override
        public int get(int field, ReadablePartial partial) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int size() {
            throw new UnsupportedOperationException();
        }

        @Override
        public DurationFieldType getFieldType(int index) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int getValue(int index) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int getValue(DurationFieldType type) {
            switch (type.getLength()) {
                case MILLISECS:
                    return (int) (millis % 1000);
                case SECS:
                    return (int) (millis / 1000);
                case MINUTES:
                    return (int) ((millis / (1000 * 60)));
                case HOURS:
                    return (int) ((millis / (1000 * 60 * 60)));
                case DAYS:
                    return (int) ((millis / (1000L * 60 * 60 * 24)));
                default:
                    throw new IllegalArgumentException();
            }
        }

        @Override
        public boolean isContiguous() {
            return true;
        }

        @Override
        public String toString() {
            return Long.toString(millis);
        }
    }
}