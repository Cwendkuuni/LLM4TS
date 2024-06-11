import org.apache.commons.lang3.builder.CompareToBuilder;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CompareToBuilderTest {

    @Test
    public void testAppend() {
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        Object lhs = new Object();
        Object rhs = new Object();
        compareToBuilder.append(lhs, rhs);
        int comparison = compareToBuilder.toComparison();
        assertEquals(0, comparison);
    }

    @Test
    public void testAppendNullLhs() {
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        Object lhs = null;
        Object rhs = new Object();
        compareToBuilder.append(lhs, rhs);
        int comparison = compareToBuilder.toComparison();
        assertEquals(-1, comparison);
    }

    @Test
    public void testAppendNullRhs() {
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        Object lhs = new Object();
        Object rhs = null;
        compareToBuilder.append(lhs, rhs);
        int comparison = compareToBuilder.toComparison();
        assertEquals(1, comparison);
    }

    @Test(expected = ClassCastException.class)
    public void testAppendDifferentClasses() {
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        Object lhs = new String("hello");
        Object rhs = new Integer(5);
        compareToBuilder.append(lhs, rhs);
    }

    @Test
    public void testAppendSameReference() {
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        Object lhs = new Object();
        Object rhs = lhs;
        compareToBuilder.append(lhs, rhs);
        int comparison = compareToBuilder.toComparison();
        assertEquals(0, comparison);
    }

    @Test
    public void testAppendComparator() {
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        Object lhs = new Object();
        Object rhs = new Object();
        Comparator<Object> comparator = new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                return 1;
            }
        };
        compareToBuilder.append(lhs, rhs, comparator);
        int comparison = compareToBuilder.toComparison();
        assertEquals(1, comparison);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testAppendDifferentArrayLengths() {
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        Object[] lhs = {1, 2, 3, 4};
        Object[] rhs = {1, 2, 3};
        compareToBuilder.append(lhs, rhs);
    }

    @Test
    public void testAppendArrays() {
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        Object[] lhs = {1, 2, 3, 4};
        Object[] rhs = {1, 2, 3, 4};
        compareToBuilder.append(lhs, rhs);
        int comparison = compareToBuilder.toComparison();
        assertEquals(0, comparison);
    }

    @Test
    public void testAppendArraysDifferentClasses() {
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        Object[] lhs = {"hello", "world"};
        Object[] rhs = {1, 2, 3, 4};
        compareToBuilder.append(lhs, rhs);
        int comparison = compareToBuilder.toComparison();
        assertEquals(-1, comparison);
    }

    @Test
    public void testAppendArraysNull() {
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        Object[] lhs = null;
        Object[] rhs = new Object[0];
        compareToBuilder.append(lhs, rhs);
        int comparison = compareToBuilder.toComparison();
        assertEquals(-1, comparison);
    }

    @Test
    public void testAppendArraysNullAndEmpty() {
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        Object[] lhs = null;
        Object[] rhs = null;
        compareToBuilder.append(lhs, rhs);
        int comparison = compareToBuilder.toComparison();
        assertEquals(0, comparison);
    }

    @Test
    public void testAppendArraysWithSameObjects() {
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        Object[] lhs = {new Object(), new Object()};
        Object[] rhs = {new Object(), new Object()};
        compareToBuilder.append(lhs, rhs);
        int comparison = compareToBuilder.toComparison();
        assertEquals(0, comparison);
    }

    @Test
    public void testAppendArraysWithDifferentObjects() {
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        Object[] lhs = {new Object(), new Object()};
        Object[] rhs = {new Object(), new Object()[0]};
        compareToBuilder.append(lhs, rhs);
        int comparison = compareToBuilder.toComparison();
        assertEquals(-1, comparison);
    }

    @Test
    public void testAppendArraysWithDifferentObjectClass() {
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        Object[] lhs = {new String("hello"), new String("world")};
        Object[] rhs = {new Integer(5), new Integer(6)};
        compareToBuilder.append(lhs, rhs);
        int comparison = compareToBuilder.toComparison();
        assertEquals(-1, comparison);
    }

    @Test
    public void testAppendArraysWithTransients() {
        class TestClass {
            private transient int transientField;
            private int nonTransientField;

            @Override
            public String toString() {
                return Integer.toString(nonTransientField);
            }
        }

        CompareToBuilder compareToBuilder = new CompareToBuilder();
        TestClass lhs = new TestClass();
        TestClass rhs = new TestClass();
        lhs.nonTransientField = 1;
        rhs.nonTransientField = 2;
        compareToBuilder.append(lhs, rhs, true, TestClass.class, new String[]{"transientField"});
        int comparison = compareToBuilder.toComparison();
        assertEquals(-1, comparison);
    }

    @Test
    public void testAppendArraysWithNonTransients() {
        class TestClass {
            private int transientField;
            private int nonTransientField;

            @Override
            public String toString() {
                return Integer.toString(nonTransientField);
            }
        }

        CompareToBuilder compareToBuilder = new CompareToBuilder();
        TestClass lhs = new TestClass();
        TestClass rhs = new TestClass();
        lhs.nonTransientField = 1;
        rhs.nonTransientField = 2;
        compareToBuilder.append(lhs, rhs, false, TestClass.class, new String[0]);
        int comparison = compareToBuilder.toComparison();
        assertEquals(-1, comparison);
    }

    @Test
    public void testAppendArraysWithDerivedClasses() {
        class BaseClass {
            private int baseField;

            @Override
            public String toString() {
                return Integer.toString(baseField);
            }
        }

        class DerivedClass extends BaseClass {
            private int derivedField;
        }

        CompareToBuilder compareToBuilder = new CompareToBuilder();
        BaseClass lhs = new DerivedClass();
        BaseClass rhs = new DerivedClass();
        lhs.baseField = 1;
        lhs.derivedField = 2;
        rhs.baseField = 2;
        rhs.derivedField = 1;
        compareToBuilder.append(lhs, rhs);
        int comparison = compareToBuilder.toComparison();
        assertEquals(-1, comparison);
    }

    @Test
    public void testAppendArraysWithSuperClass() {
        class BaseClass {
            private int baseField;

            @Override
            public String toString() {
                return Integer.toString(baseField);
            }
        }

        class DerivedClass extends BaseClass {
            private int derivedField;
        }

        CompareToBuilder compareToBuilder = new CompareToBuilder();
        BaseClass lhs = new DerivedClass();
        Object rhs = new Object();
        lhs.baseField = 1;
        compareToBuilder.appendSuper(compareToBuilder.append(lhs, rhs).toComparison());
        int comparison = compareToBuilder.toComparison();
        assertEquals(-1, comparison);
    }
}