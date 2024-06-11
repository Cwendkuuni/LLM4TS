package org.apache.commons.lang3.builder;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.CompareToBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(MockitoJUnitRunner.class)
public class CompareToBuilderTest {

    @Test
    public void testComparison_SameObjects() {
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        Object lhs = new Object();
        Object rhs = lhs;
        int comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
        compareToBuilder.appendSuper(comparison);
        assertEquals(0, compareToBuilder.toComparison());
    }

    @Test
    public void testComparison_NullLhs() {
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        Object lhs = null;
        Object rhs = new Object();
        int comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
        compareToBuilder.appendSuper(comparison);
        assertEquals(-1, compareToBuilder.toComparison());
    }

    @Test
    public void testComparison_NullRhs() {
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        Object lhs = new Object();
        Object rhs = null;
        int comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
        compareToBuilder.appendSuper(comparison);
        assertEquals(1, compareToBuilder.toComparison());
    }

    @Test(expected = NullPointerException.class)
    public void testComparison_NullLhsAndRhs() {
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        Object lhs = null;
        Object rhs = null;
        CompareToBuilder.reflectionCompare(lhs, rhs);
    }

    @Test
    public void testComparison_DifferentClasses() {
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        Object lhs = new Object();
        Object rhs = new String("test");
        int comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
        compareToBuilder.appendSuper(comparison);
        assertNotEquals(0, compareToBuilder.toComparison());
    }

    @Test
    public void testAppend_SameObjects() {
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        Object lhs = new Object();
        Object rhs = lhs;
        compareToBuilder.append(lhs, rhs);
        assertEquals(0, compareToBuilder.toComparison());
    }

    @Test
    public void testAppend_NullLhs() {
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        Object lhs = null;
        Object rhs = new Object();
        compareToBuilder.append(lhs, rhs);
        assertEquals(-1, compareToBuilder.toComparison());
    }

    @Test
    public void testAppend_NullRhs() {
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        Object lhs = new Object();
        Object rhs = null;
        compareToBuilder.append(lhs, rhs);
        assertEquals(1, compareToBuilder.toComparison());
    }

    @Test(expected = NullPointerException.class)
    public void testAppend_NullLhsAndRhs() {
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        Object lhs = null;
        Object rhs = null;
        compareToBuilder.append(lhs, rhs);
    }

    @Test
    public void testAppend_PrimitiveTypes() {
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        int lhs = 5;
        int rhs = 10;
        compareToBuilder.append(lhs, rhs);
        assertEquals(-5, compareToBuilder.toComparison());
    }

    @Test
    public void testAppend_ArrayTypes() {
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        int[] lhs = {1, 2, 3};
        int[] rhs = {1, 2, 3};
        compareToBuilder.append(lhs, rhs);
        assertEquals(0, compareToBuilder.toComparison());
    }

    @Test
    public void testAppend_ArrayTypes_DifferentSizes() {
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        int[] lhs = {1, 2, 3};
        int[] rhs = {1, 2};
        compareToBuilder.append(lhs, rhs);
        assertEquals(-1, compareToBuilder.toComparison());
    }

    @Test
    public void testAppend_ArrayTypes_DifferentElements() {
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        int[] lhs = {1, 2, 3};
        int[] rhs = {1, 3, 2};
        compareToBuilder.append(lhs, rhs);
        assertEquals(-1, compareToBuilder.toComparison());
    }

    @Test
    public void testAppend_ArrayTypes_CustomComparator() {
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        int[] lhs = {1, 2, 3};
        int[] rhs = {3, 2, 1};
        Arrays.sort(lhs);
        Arrays.sort(rhs);
        Comparator<int[]> comparator = (a, b) -> Integer.compare(Arrays.stream(a).mapToInt(i -> i).sum(), Arrays.stream(b).mapToInt(i -> i).sum());
        compareToBuilder.append(lhs, rhs, comparator);
        assertEquals(1, compareToBuilder.toComparison());
    }

    @Test
    public void testAppend_CustomObjects() {
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        MyClass lhs = new MyClass(1, "test");
        MyClass rhs = new MyClass(2, "test2");
        compareToBuilder.append(lhs, rhs);
        assertEquals(-1, compareToBuilder.toComparison());
    }

    @Test
    public void testAppend_CustomObjects_DifferentClasses() {
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        MyClass lhs = new MyClass(1, "test");
        String rhs = "test2";
        compareToBuilder.append(lhs, rhs);
        assertNotEquals(0, compareToBuilder.toComparison());
    }

    @Test
    public void testAppend_CustomObjects_DifferentFields() {
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        MyClass lhs = new MyClass(1, "test1");
        MyClass rhs = new MyClass(1, "test2");
        Field field1 = MyClass.class.getDeclaredField("field1");
        Field field2 = MyClass.class.getDeclaredField("field2");
        compareToBuilder.append(lhs, rhs, null);
        try {
            compareToBuilder.append(lhs, rhs, new String[]{"field1"});
            assertEquals(0, compareToBuilder.toComparison());
        } catch (NoSuchFieldException e) {
            // Field does not exist in the test case for demonstration purposes
        }
        try {
            compareToBuilder.append(lhs, rhs, new String[]{"field2"});
            assertEquals(-1, compareToBuilder.toComparison());
        } catch (NoSuchFieldException e) {
            // Field does not exist in the test case for demonstration purposes
        }
    }

    @Test
    public void testAppend_CustomObjects_Transients() {
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        MyClass lhs = new MyClass(1, "test1");
        MyClass rhs = new MyClass(1, "test2");
        Field transientField1 = MyClass.class.getDeclaredField("transientField1");
        Field transientField2 = MyClass.class.getDeclaredField("transientField2");
        AccessibleObject.setAccessible(transientField1, true);
        AccessibleObject.setAccessible(transientField2, true);
        compareToBuilder.append(lhs, rhs, null, MyClass.class, new String[0]);
        assertEquals(0, compareToBuilder.toComparison());
    }

    @Test
    public void testAppend_CustomObjects_Transients_Excluded() {
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        MyClass lhs = new MyClass(1, "test1");
        MyClass rhs = new MyClass(1, "test2");
        Field transientField1 = MyClass.class.getDeclaredField("transientField1");
        Field transientField2 = MyClass.class.getDeclaredField("transientField2");
        AccessibleObject.setAccessible(transientField1, true);
        AccessibleObject.setAccessible(transientField2, true);
        compareToBuilder.append(lhs, rhs, null, MyClass.class, new String[]{"transientField1", "transientField2"});
        assertEquals(0, compareToBuilder.toComparison());
    }

    private static class MyClass {
        private int field1;
        private String field2;
        private int transientField1;
        private String transientField2;

        public MyClass(int field1, String field2) {
            this.field1 = field1;
            this.field2 = field2;
        }

        public int getField1() {
            return field1;
        }

        public String getField2() {
            return field2;
        }
    }
}