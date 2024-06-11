package org.joda.time.base;

import static org.junit.Assert.*;

import org.junit.Test;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod testInstance = new MySingleFieldPeriod(5);
        DurationFieldType expectedType = new AbstractDurationFieldType() {
            @Override
            public String getName() {
                return "MyDurationFieldType";
            }

            @Override
            public int getNumberOfValues() {
                return 1;
            }

            @Override
            public long getUnitMillis() {
                return 1;
            }

            @Override
            public boolean isPrecise() {
                return true;
            }
        };
        assertSame(expectedType, testInstance.getFieldType());
    }

    @Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod testInstance = new MySingleFieldPeriod(5);
        PeriodType expectedType = new DefaultPeriodType() {
            @Override
            public String getID() {
                return "MyPeriodType";
            }
        };
        assertSame(expectedType, testInstance.getPeriodType());
    }

    @Test
    public void testSize() {
        BaseSingleFieldPeriod testInstance = new MySingleFieldPeriod(5);
        assertEquals(1, testInstance.size());
    }

    @Test
    public void testGetFieldTypeAt() {
        BaseSingleFieldPeriod testInstance = new MySingleFieldPeriod(5);
        DurationFieldType expectedType = testInstance.getFieldType();
        assertEquals(expectedType, testInstance.getFieldType(0));
    }

    @Test
    public void testGetValueAt() {
        BaseSingleFieldPeriod testInstance = new MySingleFieldPeriod(5);
        int expectedValue = 5;
        assertEquals(expectedValue, testInstance.getValue(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValueAtNegativeIndex() {
        BaseSingleFieldPeriod testInstance = new MySingleFieldPeriod(5);
        testInstance.getValue(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetFieldTypeAtNegativeIndex() {
        BaseSingleFieldPeriod testInstance = new MySingleFieldPeriod(5);
        testInstance.getFieldType(-1);
    }

    @Test
    public void testEquals() {
        BaseSingleFieldPeriod testInstance1 = new MySingleFieldPeriod(5);
        BaseSingleFieldPeriod testInstance2 = new MySingleFieldPeriod(5);
        assertTrue(testInstance1.equals(testInstance2));
        assertTrue(testInstance2.equals(testInstance1));

        BaseSingleFieldPeriod testInstance3 = new MySingleFieldPeriod(6);
        assertFalse(testInstance1.equals(testInstance3));
        assertFalse(testInstance3.equals(testInstance1));

        Object testInstance4 = new Object();
        assertFalse(testInstance1.equals(testInstance4));
        assertFalse(testInstance4.equals(testInstance1));
    }

    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod testInstance1 = new MySingleFieldPeriod(5);
        BaseSingleFieldPeriod testInstance2 = new MySingleFieldPeriod(5);
        int expectedHashCode = 27 * (27 * 17 + 5) + testInstance1.getFieldType().hashCode();
        assertEquals(expectedHashCode, testInstance1.hashCode());
        assertEquals(expectedHashCode, testInstance2.hashCode());

        BaseSingleFieldPeriod testInstance3 = new MySingleFieldPeriod(6);
        int differentHashCode = 27 * (27 * 17 + 6) + testInstance3.getFieldType().hashCode();
        assertNotEquals(expectedHashCode, testInstance3.hashCode());
    }

    @Test
    public void testCompareTo() {
        BaseSingleFieldPeriod testInstance1 = new MySingleFieldPeriod(5);
        BaseSingleFieldPeriod testInstance2 = new MySingleFieldPeriod(5);
        assertEquals(0, testInstance1.compareTo(testInstance2));

        BaseSingleFieldPeriod testInstance3 = new MySingleFieldPeriod(6);
        assertEquals(1, testInstance1.compareTo(testInstance3));

        BaseSingleFieldPeriod testInstance4 = new MySingleFieldPeriod(4);
        assertEquals(-1, testInstance3.compareTo(testInstance4));
    }

    private static class MySingleFieldPeriod extends BaseSingleFieldPeriod {
        MySingleFieldPeriod(int period) {
            super(period);
        }

        @Override
        public DurationFieldType getFieldType() {
            return new MyDurationFieldType();
        }

        @Override
        public PeriodType getPeriodType() {
            return new MyPeriodType();
        }
    }

    private static class MyDurationFieldType extends AbstractDurationFieldType {
        @Override
        public String getName() {
            return "MyDurationFieldType";
        }

        @Override
        public int getNumberOfValues() {
            return 1;
        }

        @Override
        public long getUnitMillis() {
            return 1;
        }

        @Override
        public boolean isPrecise() {
            return true;
        }
    }

    private static class MyPeriodType extends DefaultPeriodType {
        @Override
        public String getID() {
            return "MyPeriodType";
        }
    }
}