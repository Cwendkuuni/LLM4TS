package org.joda.time.base;

import static org.junit.Assert.*;

import org.joda.time.*;
import org.joda.time.chrono.*;
import org.joda.time.field.*;
import org.junit.Test;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testBaseSingleFieldPeriod_getValue() {
        BaseSingleFieldPeriod period = new MySingleFieldPeriod(5);
        assertEquals(5, period.getValue());
    }

    @Test
    public void testBaseSingleFieldPeriod_setValue() {
        BaseSingleFieldPeriod period = new MySingleFieldPeriod(5);
        period.setValue(10);
        assertEquals(10, period.getValue());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testBaseSingleFieldPeriod_getFieldType_wrongIndex() {
        BaseSingleFieldPeriod period = new MySingleFieldPeriod(5);
        period.getFieldType(2);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testBaseSingleFieldPeriod_getValue_wrongIndex() {
        BaseSingleFieldPeriod period = new MySingleFieldPeriod(5);
        period.getValue(2);
    }

    @Test
    public void testBaseSingleFieldPeriod_equals() {
        BaseSingleFieldPeriod period1 = new MySingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new MySingleFieldPeriod(5);
        assertTrue(period1.equals(period2));
    }

    @Test
    public void testBaseSingleFieldPeriod_notEquals() {
        BaseSingleFieldPeriod period1 = new MySingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new MySingleFieldPeriod(6);
        assertFalse(period1.equals(period2));
    }

    @Test
    public void testBaseSingleFieldPeriod_hashCode() {
        BaseSingleFieldPeriod period1 = new MySingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new MySingleFieldPeriod(5);
        assertEquals(period1.hashCode(), period2.hashCode());

        BaseSingleFieldPeriod period3 = new MySingleFieldPeriod(6);
        assertNotEquals(period1.hashCode(), period3.hashCode());
    }

    @Test
    public void testBaseSingleFieldPeriod_compareTo() {
        BaseSingleFieldPeriod period1 = new MySingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new MySingleFieldPeriod(6);
        BaseSingleFieldPeriod period3 = new MySingleFieldPeriod(5);

        assertEquals(-1, period1.compareTo(period2));
        assertEquals(0, period1.compareTo(period3));
        assertEquals(1, period3.compareTo(period1));
    }

    @Test
    public void testBaseSingleFieldPeriod_getFieldType() {
        BaseSingleFieldPeriod period = new MySingleFieldPeriod(5);
        assertEquals(MyCustomDurationFieldType.INSTANCE, period.getFieldType());
    }

    @Test
    public void testBaseSingleFieldPeriod_getPeriodType() {
        BaseSingleFieldPeriod period = new MySingleFieldPeriod(5);
        assertEquals(MyCustomPeriodType.INSTANCE, period.getPeriodType());
    }

    @Test
    public void testBaseSingleFieldPeriod_size() {
        BaseSingleFieldPeriod period = new MySingleFieldPeriod(5);
        assertEquals(1, period.size());
    }

    @Test
    public void testBaseSingleFieldPeriod_isSupported() {
        BaseSingleFieldPeriod period = new MySingleFieldPeriod(5);
        assertTrue(period.isSupported(MyCustomDurationFieldType.INSTANCE));
        assertFalse(period.isSupported(null));
        assertFalse(period.isSupported(MyCustomDurationFieldType2.INSTANCE));
    }

    @Test
    public void testBaseSingleFieldPeriod_toPeriod() {
        BaseSingleFieldPeriod period = new MySingleFieldPeriod(5);
        Period result = period.toPeriod();
        assertEquals(5, result.getDays());
    }

    @Test
    public void testBaseSingleFieldPeriod_toMutablePeriod() {
        BaseSingleFieldPeriod period = new MySingleFieldPeriod(5);
        MutablePeriod result = period.toMutablePeriod();
        result.add(DurationFieldType.DAYS, 1);
        assertEquals(6, period.getValue());
        assertEquals(1, result.getDays());
    }

    @Test
    public void testBaseSingleFieldPeriod_between() {
        Instant instant = new DateTime(2022, 12, 31, 23, 59, 59, 999).toInstant();
        Instant instant2 = new DateTime(2023, 1, 1, 0, 0, 0, 0).toInstant();
        BaseSingleFieldPeriod period = new MySingleFieldPeriod(5);
        DurationFieldType durationFieldType = MyCustomDurationFieldType.INSTANCE;

        int result = BaseSingleFieldPeriod.between(instant, instant2, durationFieldType);
        assertEquals(0, result);

        instant2 = new DateTime(2022, 12, 31, 23, 59, 59, 1000).toInstant();
        result = BaseSingleFieldPeriod.between(instant, instant2, durationFieldType);
        assertEquals(1, result);
    }

    @Test
    public void testBaseSingleFieldPeriod_standardPeriodIn() {
        BaseSingleFieldPeriod period = new MySingleFieldPeriod(5);
        long n = 2;
        int result = BaseSingleFieldPeriod.standardPeriodIn(period, n);
        assertEquals(2 * 5, result);
    }

    private static class MyCustomDurationFieldType implements DurationFieldType {
        public static final MyCustomDurationFieldType INSTANCE = new MyCustomDurationFieldType();
        private static final int UNITS = 1;

        @Override
        public String getName() {
            return "MyCustomField";
        }

        @Override
        public String getDisplayName() {
            return "My Custom Field";
        }

        @Override
        public int getUnits() {
            return UNITS;
        }

        @Override
        public int getLength(ReadableInstant instant, ReadableInstant baseDate) {
            return 5;
        }

        @Override
        public long getUnitMillis() {
            return 1;
        }
    }

    private static class MyCustomPeriodType implements PeriodType {
        public static final MyCustomPeriodType INSTANCE = new MyCustomPeriodType();

        @Override
        public String getID() {
            return "MyCustomPeriod";
        }

        @Override
        public String getNameKey() {
            return "MyCustomPeriodName";
        }
    }

    private static class MySingleFieldPeriod extends BaseSingleFieldPeriod {
        public MySingleFieldPeriod(int iPeriod) {
            super(iPeriod);
        }

        @Override
        public DurationFieldType getFieldType() {
            return MyCustomDurationFieldType.INSTANCE;
        }

        @Override
        public PeriodType getPeriodType() {
            return MyCustomPeriodType.INSTANCE;
        }
    }
}