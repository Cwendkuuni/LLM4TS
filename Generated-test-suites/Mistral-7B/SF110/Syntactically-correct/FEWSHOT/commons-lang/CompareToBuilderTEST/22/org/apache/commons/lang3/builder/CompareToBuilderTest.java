package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class CompareToBuilderTest {

    @Test
    public void testReflectionCompare() {
        Object lhs = new Object() {
            int i = 1;
        };
        Object rhs = new Object() {
            int i = 2;
        };
        int result = CompareToBuilder.reflectionCompare(lhs, rhs);
        assertEquals(-1, result);

        lhs = new Object();
        rhs = new Object();
        result = CompareToBuilder.reflectionCompare(lhs, rhs);
        assertEquals(0, result);

        lhs = new Object() {
            int i = 2;
        };
        result = CompareToBuilder.reflectionCompare(lhs, rhs);
        assertEquals(1, result);
    }

    @Test
    public void testReflectionCompareTransients() {
        class TestClass {
            @SuppressWarnings("unused")
            private boolean transientField;
            private int nonTransientField = 1;

            public TestClass(int nonTransientField) {
                this.nonTransientField = nonTransientField;
            }

            @Override
            public String toString() {
                return "TestClass{" + "nonTransientField=" + nonTransientField + '}';
            }
        }

        TestClass lhs = new TestClass(1);
        TestClass rhs = new TestClass(2);
        int result = CompareToBuilder.reflectionCompare(lhs, rhs, true);
        assertEquals(-1, result);

        result = CompareToBuilder.reflectionCompare(lhs, rhs, true, TestClass.class, new String[0]);
        assertEquals(-1, result);
    }

    @Test
    public void testReflectionCompareExcludeFields() {
        class TestClass {
            private int excludedField = 1;
            private int nonExcludedField = 1;

            public TestClass(int excludedField, int nonExcludedField) {
                this.excludedField = excludedField;
                this.nonExcludedField = nonExcludedField;
            }

            @Override
            public String toString() {
                return "TestClass{" + "excludedField=" + excludedField + ", nonExcludedField=" + nonExcludedField + '}';
            }
        }

        TestClass lhs = new TestClass(1, 1);
        TestClass rhs = new TestClass(1, 2);
        int result = CompareToBuilder.reflectionCompare(lhs, rhs, new String[]{"excludedField"});
        assertEquals(0, result);

        result = CompareToBuilder.reflectionCompare(lhs, rhs, TestClass.class, new String[0]);
        assertEquals(0, result);

        result = CompareToBuilder.reflectionCompare(lhs, rhs, TestClass.class, new String[]{"excludedField"});
        assertEquals(0, result);

        result = CompareToBuilder.reflectionCompare(lhs, rhs, TestClass.class, new String[]{"nonExcludedField"});
        assertEquals(-1, result);
    }

    @Test
    public void testReflectionCompareReflectUpToClass() {
        class TestSuperClass {
            int superField = 1;
        }

        class TestClass extends TestSuperClass {
            int field = 1;

            @Override
            public String toString() {
                return "TestClass{" + "superField=" + superField + ", field=" + field + '}';
            }
        }

        TestClass lhs = new TestClass();
        TestClass rhs = new TestClass();
        int result = CompareToBuilder.reflectionCompare(lhs, rhs, TestClass.class, TestSuperClass.class, new String[0]);
        assertEquals(0, result);

        lhs.superField = 2;
        result = CompareToBuilder.reflectionCompare(lhs, rhs, TestClass.class, TestSuperClass.class, new String[0]);
        assertEquals(-1, result);

        rhs.superField = 3;
        result = CompareToBuilder.reflectionCompare(lhs, rhs, TestClass.class, TestSuperClass.class, new String[0]);
        assertEquals(1, result);
    }
}