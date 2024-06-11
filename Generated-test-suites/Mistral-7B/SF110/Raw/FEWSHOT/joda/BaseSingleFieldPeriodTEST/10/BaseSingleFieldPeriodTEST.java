package org.joda.time.base;

import static org.junit.Assert.*;
import org.joda.time.*;
import org.joda.time.chrono.*;
import org.joda.time.field.*;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testBaseSingleFieldPeriod_getValue() {
        BaseSingleFieldPeriod period = new MySingleFieldPeriod(5);
        int actualValue = period.getValue();
        int expectedValue = 5;
        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testBaseSingleFieldPeriod_equals() {
        BaseSingleFieldPeriod period1 = new MySingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new MySingleFieldPeriod(5);
        assertTrue(period1.equals(period2));
    }

    @Test
    public void testBaseSingleFieldPeriod_notEquals_differentClass() {
        BaseSingleFieldPeriod period1 = new MySingleFieldPeriod(5);
        Object otherObject = new Object();
        assertFalse(period1.equals(otherObject));
    }

    @Test
    public void testBaseSingleFieldPeriod_notEquals_differentType() {
        BaseSingleFieldPeriod period1 = new MySingleFieldPeriod(5);
        BaseMultiFieldPeriod period2 = new BaseMultiFieldPeriod(5, 10);
        assertFalse(period1.equals(period2));
    }

    @Test
    public void testBaseSingleFieldPeriod_notEquals_differentValue() {
        BaseSingleFieldPeriod period1 = new MySingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new MySingleFieldPeriod(10);
        assertFalse(period1.equals(period2));
    }

    @Test
    public void testBaseSingleFieldPeriod_compareTo_sameInstance() {
        BaseSingleFieldPeriod period1 = new MySingleFieldPeriod(5);
        int comparisonResult = period1.compareTo(period1);
        assertEquals(0, comparisonResult);
    }

    @Test
    public void testBaseSingleFieldPeriod_compareTo_sameValue() {
        BaseSingleFieldPeriod period1 = new MySingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new MySingleFieldPeriod(5);
        int comparisonResult = period1.compareTo(period2);
        assertEquals(0, comparisonResult);
    }

    @Test
    public void testBaseSingleFieldPeriod_compareTo_differentValue_less() {
        BaseSingleFieldPeriod period1 = new MySingleFieldPeriod(2);
        BaseSingleFieldPeriod period2 = new MySingleFieldPeriod(5);
        int comparisonResult = period1.compareTo(period2);
        assertEquals(-3, comparisonResult);
    }

    @Test
    public void testBaseSingleFieldPeriod_compareTo_differentValue_greater() {
        BaseSingleFieldPeriod period1 = new MySingleFieldPeriod(8);
        BaseSingleFieldPeriod period2 = new MySingleFieldPeriod(5);
        int comparisonResult = period1.compareTo(period2);
        assertEquals(3, comparisonResult);
    }

    @Test
    public void testBaseSingleFieldPeriod_compareTo_differentClass() {
        BaseSingleFieldPeriod period1 = new MySingleFieldPeriod(5);
        Object otherObject = new Object();
        try {
            period1.compareTo(otherObject);
            fail("IllegalArgumentException expected");
        } catch (ClassCastException e) {
            // Expected
        }
    }

    @Test
    public void testBaseSingleFieldPeriod_getFieldType() {
        BaseSingleFieldPeriod period = new MySingleFieldPeriod(5);
        DurationFieldType fieldType = period.getFieldType();
        assertEquals(MySingleField.class, fieldType.getClass());
    }

    @Test
    public void testBaseSingleFieldPeriod_getPeriodType() {
        BaseSingleFieldPeriod period = new MySingleFieldPeriod(5);
        PeriodType periodType = period.getPeriodType();
        assertEquals(MySingleFieldPeriodType.class, periodType.getClass());
    }

    @Test
    public void testBaseSingleFieldPeriod_size() {
        BaseSingleFieldPeriod period = new MySingleFieldPeriod(5);
        int size = period.size();
        assertEquals(1, size);
    }

    @Test
    public void testBaseSingleFieldPeriod_getValue_outOfBounds() {
        BaseSingleFieldPeriod period = new MySingleFieldPeriod(5);
        try {
            period.getValue(1);
            fail("IndexOutOfBoundsException expected");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }

    @Test
    public void testBaseSingleFieldPeriod_getFieldType_outOfBounds() {
        BaseSingleFieldPeriod period = new MySingleFieldPeriod(5);
        try {
            period.getFieldType(1);
            fail("IndexOutOfBoundsException expected");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }

    @Test
    public void testBaseSingleFieldPeriod_toPeriod() {
        BaseSingleFieldPeriod period = new MySingleFieldPeriod(5);
        Period actualPeriod = period.toPeriod();
        Period expectedPeriod = Period.valueOf("P5D");
        assertPeriodEquals(expectedPeriod, actualPeriod);
    }

    @Test
    public void testBaseSingleFieldPeriod_toMutablePeriod() {
        BaseSingleFieldPeriod period = new MySingleFieldPeriod(5);
        MutablePeriod actualPeriod = period.toMutablePeriod();
        MutablePeriod expectedPeriod = new MutablePeriod();
        expectedPeriod.add(DurationFieldType.DAYS, 5);
        assertPeriodEquals(expectedPeriod, actualPeriod);
    }

    @Test
    public void testBaseSingleFieldPeriod_toString() {
        BaseSingleFieldPeriod period = new MySingleFieldPeriod(5);
        String actualString = period.toString();
        String expectedString = "P5D";
        assertEquals(expectedString, actualString);
    }

    @Test
    public void testBaseSingleFieldPeriod_compareTo_null() {
        BaseSingleFieldPeriod period = new MySingleFieldPeriod(5);
        try {
            period.compareTo(null);
            fail("IllegalArgumentException expected");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    private static void assertPeriodEquals(Period expectedPeriod, Period actualPeriod) {
        assertEquals(expectedPeriod.getPeriodType(), actualPeriod.getPeriodType());
        assertEquals(expectedPeriod.size(), actualPeriod.size());
        for (int i = 0; i < expectedPeriod.size(); i++) {
            assertEquals(expectedPeriod.getFieldType(i), actualPeriod.getFieldType(i));
            assertEquals(expectedPeriod.getValue(i), actualPeriod.getValue(i));
        }
    }

    private static class MySingleFieldPeriod extends BaseSingleFieldPeriod {
        public MySingleFieldPeriod(int iPeriod) {
            super(iPeriod);
        }

        public DurationFieldType getFieldType() {
            return new MySingleField();
        }

        public PeriodType getPeriodType() {
            return MySingleFieldPeriodType.INSTANCE;
        }
    }

    private static class MySingleField implements DurationFieldType {
        @Override
        public String getName() {
            return "MySingleField";
        }

        @Override
        public long getUnitMillis() {
            return 1000 * 60 * 60 * 24;
        }

        @Override
        public long getPreciseValue(long value, Chronology chronology) {
            return value;
        }

        @Override
        public long add(long currentValue, long amountToAdd, Chronology chronology) {
            return currentValue + amountToAdd;
        }
    }

    private static class MySingleFieldPeriodType implements PeriodType {
        static final MySingleFieldPeriodType INSTANCE = new MySingleFieldPeriodType();

        @Override
        public String getID() {
            return "MySingleFieldPeriodType";
        }

        @Override
        public long getMillisecondsPerUnit() {
            return 1000 * 60 * 60 * 24;
        }

        @Override
        public int getPeriodSize(long duration, Chronology chronology) {
            return 1;
        }

        @Override
        public PeriodType baseType() {
            return PeriodType.DAYS;
        }

        @Override
        public PeriodType add(DurationFieldType addedField, PeriodType baseType) {
            return this;
        }

        @Override
        public PeriodType add(int addedFields, PeriodType baseType) {
            return this;
        }

        @Override
        public PeriodType add(PeriodType addedPeriod, PeriodType baseType) {
            return this;
        }

        @Override
        public PeriodType add(DurationFieldType addedField, long addedAmount, PeriodType baseType) {
            return this;
        }

        @Override
        public PeriodType add(int addedFields, long addedAmount, PeriodType baseType) {
            return this;
        }

        @Override
        public PeriodType add(PeriodType addedPeriod, long addedAmount, PeriodType baseType) {
            return this;
        }

        @Override
        public PeriodType add(DurationFieldType addedField, int addedAmount, PeriodType baseType) {
            return this;
        }

        @Override
        public PeriodType add(PeriodType addedPeriod, int addedAmount, PeriodType baseType) {
            return this;
        }

        @Override
        public PeriodType addSet(DurationFieldType addedField, long addedAmount, PeriodType baseType) {
            return this;
        }

        @Override
        public PeriodType addSet(int addedFields, long addedAmount, PeriodType baseType) {
            return this;
        }

        @Override
        public PeriodType addSet(PeriodType addedPeriod, long addedAmount, PeriodType baseType) {
            return this;
        }

        @Override
        public PeriodType addSet(DurationFieldType addedField, int addedAmount, PeriodType baseType) {
            return this;
        }

        @Override
        public PeriodType addSet(PeriodType addedPeriod, int addedAmount, PeriodType baseType) {
            return this;
        }

        @Override
        public int getPeriodSize(Period period) {
            return period.size();
        }
    }
}