package org.joda.time.base;

import static org.junit.Assert.*;

import org.joda.time.*;
import org.joda.time.chrono.*;
import org.joda.time.field.*;
import org.junit.Test;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testGetValue() {
        BaseSingleFieldPeriod period = new TestSingleFieldPeriod(5);
        assertEquals(5, period.getValue());
    }

    @Test
    public void testSetValue() {
        BaseSingleFieldPeriod period = new TestSingleFieldPeriod(5);
        period.setValue(10);
        assertEquals(10, period.getValue());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValueNegativeIndex() {
        BaseSingleFieldPeriod period = new TestSingleFieldPeriod(5);
        period.getValue(-1);
    }

    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod period = new TestSingleFieldPeriod(5);
        assertEquals(TestDurationFieldType.INSTANCE, period.getFieldType());
    }

    @Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod period = new TestSingleFieldPeriod(5);
        assertEquals(TestPeriodType.INSTANCE, period.getPeriodType());
    }

    @Test
    public void testSize() {
        BaseSingleFieldPeriod period = new TestSingleFieldPeriod(5);
        assertEquals(1, period.size());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetFieldTypeNegativeIndex() {
        BaseSingleFieldPeriod period = new TestSingleFieldPeriod(5);
        period.getFieldType(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValueNegativeIndex() {
        BaseSingleFieldPeriod period = new TestSingleFieldPeriod(5);
        period.getValue(-1);
    }

    @Test
    public void testGet() {
        BaseSingleFieldPeriod period = new TestSingleFieldPeriod(5);
        assertEquals(5, period.get(TestDurationFieldType.INSTANCE));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetUnsupportedFieldType() {
        BaseSingleFieldPeriod period = new TestSingleFieldPeriod(5);
        period.get(TestUnsupportedDurationFieldType.INSTANCE);
    }

    @Test
    public void testEquals() {
        BaseSingleFieldPeriod period1 = new TestSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new TestSingleFieldPeriod(5);
        assertTrue(period1.equals(period2));
    }

    @Test
    public void testEqualsDifferentClass() {
        BaseSingleFieldPeriod period1 = new TestSingleFieldPeriod(5);
        Object obj = new Object();
        assertFalse(period1.equals(obj));
    }

    @Test
    public void testEqualsNull() {
        BaseSingleFieldPeriod period = new TestSingleFieldPeriod(5);
        assertFalse(period.equals(null));
    }

    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod period1 = new TestSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new TestSingleFieldPeriod(5);
        assertEquals(period1.hashCode(), period2.hashCode());
    }

    @Test
    public void testCompareToSameInstance() {
        BaseSingleFieldPeriod period = new TestSingleFieldPeriod(5);
        assertEquals(0, period.compareTo(period));
    }

    @Test
    public void testCompareToDifferentInstancesEqualValue() {
        BaseSingleFieldPeriod period1 = new TestSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new TestSingleFieldPeriod(5);
        assertEquals(0, period1.compareTo(period2));
    }

    @Test
    public void testCompareToDifferentInstancesDifferentValues() {
        BaseSingleFieldPeriod period1 = new TestSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new TestSingleFieldPeriod(10);
        assertEquals(5, period1.compareTo(period2));
    }

    @Test(expected = ClassCastException.class)
    public void testCompareToDifferentClasses() {
        BaseSingleFieldPeriod period1 = new TestSingleFieldPeriod(5);
        Object obj = new Object();
        period1.compareTo(obj);
    }

    static class TestSingleFieldPeriod extends BaseSingleFieldPeriod {
        TestSingleFieldPeriod(int i) {
            super(i);
        }

        @Override
        public DurationFieldType getFieldType() {
            return TestDurationFieldType.INSTANCE;
        }

        @Override
        public PeriodType getPeriodType() {
            return TestPeriodType.INSTANCE;
        }

        static class TestDurationFieldType implements DurationFieldType {
            static final TestDurationFieldType INSTANCE = new TestDurationFieldType();

            @Override
            public String getName() {
                return "TestDurationFieldType";
            }

            @Override
            public int getLength() {
                return 5;
            }

            @Override
            public int getPreciseDivisor() {
                return 5;
            }

            @Override
            public DurationField getField(Chronology chronology) {
                return new TestDurationField();
            }
        }

        static class TestPeriodType implements PeriodType {
            static final TestPeriodType INSTANCE = new TestPeriodType();

            @Override
            public String getID() {
                return "TestPeriodType";
            }

            @Override
            public String getIDString() {
                return "TestPeriodType";
            }

            @Override
            public String getName() {
                return "TestPeriodType";
            }
        }

        static class TestDurationField implements DurationField {
            @Override
            public int getValue(ReadableDuration readableDuration) {
                BaseSingleFieldPeriod period = (BaseSingleFieldPeriod) readableDuration;
                return period.getValue();
            }

            @Override
            public void setValue(WritableDuration writableDuration, int value) {
                BaseSingleFieldPeriod period = (BaseSingleFieldPeriod) writableDuration;
                period.setValue(value);
            }

            @Override
            public int getUnitMillis() {
                return 1;
            }

            @Override
            public String getName() {
                return "TestDurationField";
            }
        }
    }
}