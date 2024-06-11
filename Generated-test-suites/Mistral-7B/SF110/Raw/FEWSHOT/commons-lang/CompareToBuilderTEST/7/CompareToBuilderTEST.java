package org.apache.commons.lang3;

import static org.junit.Assert.*;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.CompareToBuilder.CompareToBuilderImpl;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Arrays;

public class CompareToBuilderTest {

    @Test
    public void testReflectionCompare() {
        Object lhs = new Object() {
            int a = 1;
            int b = 2;
        };

        Object rhs = new Object() {
            int a = 2;
            int b = 1;
        };

        int result = CompareToBuilder.reflectionCompare(lhs, rhs);
        assertEquals(-1, result);

        lhs = new Object() {
            int a = 2;
            int b = 1;
        };

        rhs = new Object() {
            int a = 1;
            int b = 2;
        };

        result = CompareToBuilder.reflectionCompare(lhs, rhs);
        assertEquals(1, result);

        lhs = new Object() {
            int a = 1;
            int b = 1;
        };

        rhs = new Object() {
            int a = 1;
            int b = 1;
        };

        result = CompareToBuilder.reflectionCompare(lhs, rhs);
        assertEquals(0, result);
    }

    @Test
    public void testReflectionCompareWithExcludeFields() {
        Object lhs = new Object() {
            int a = 1;
            int b = 2;
            String excludeField = "exclude";
        };

        Object rhs = new Object() {
            int a = 2;
            int b = 1;
        };

        int result = CompareToBuilder.reflectionCompare(lhs, rhs, "excludeField");
        assertEquals(0, result);

        result = CompareToBuilder.reflectionCompare(lhs, rhs, "excludeField", new String[]{"excludeField"});
        assertEquals(0, result);

        result = CompareToBuilder.reflectionCompare(lhs, rhs, CompareToBuilder.REFLECTION_ANY, new String[]{"excludeField"}, new String[0]);
        assertEquals(0, result);
    }

    @Test
    public void testReflectionCompareWithTransients() {
        class TestClass {
            transient int excludeField = 0;
            int a = 1;
            int b = 2;
        }

        Object lhs = new TestClass();
        Object rhs = new TestClass();

        int result = CompareToBuilder.reflectionCompare(lhs, rhs, true, TestClass.class, new String[0]);
        assertEquals(0, result);

        result = CompareToBuilder.reflectionCompare(lhs, rhs, true, TestClass.class, new String[]{"excludeField"});
        assertEquals(0, result);
    }

    @Test
    public void testReflectionCompareWithTransientsAndExcludeFields() {
        class TestClass {
            transient int excludeField = 0;
            int a = 1;
            int b = 2;
        }

        Object lhs = new TestClass();
        Object rhs = new TestClass();

        int result = CompareToBuilder.reflectionCompare(lhs, rhs, true, TestClass.class, new String[]{"excludeField"});
        assertEquals(0, result);

        result = CompareToBuilder.reflectionCompare(lhs, rhs, CompareToBuilder.REFLECTION_ANY, new String[]{"excludeField"}, new String[0]);
        assertEquals(0, result);

        result = CompareToBuilder.reflectionCompare(lhs, rhs, true, TestClass.class, new String[0]);
        fail("Expected ComparisonException to be thrown");
    }

    @Test
    public void testReflectionCompareWithCustomComparator() {
        class TestClass {
            int a = 1;
            int b = 2;
        }

        Comparator<Object> comparator = new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                TestClass cast1 = (TestClass) o1;
                TestClass cast2 = (TestClass) o2;
                return cast1.a - cast2.a;
            }
        };

        Object lhs = new TestClass();
        Object rhs = new TestClass();

        int result = CompareToBuilder.reflectionCompare(lhs, rhs, comparator);
        assertEquals(-1, result);

        lhs = new TestClass();
        rhs = new TestClass();
        ((TestClass) lhs).a = 2;
        ((TestClass) rhs).a = 1;

        result = CompareToBuilder.reflectionCompare(lhs, rhs, comparator);
        assertEquals(1, result);
    }

    @Test
    public void testAppendSuper() {
        CompareToBuilder builder = new CompareToBuilder();
        CompareToBuilder superBuilder = new CompareToBuilder();
        superBuilder.appendSuper(5);
        builder.appendSuper(superBuilder.toComparison());

        int result = builder.toComparison();
        assertEquals(5, result);
    }

    @Test
    public void testAppend() {
        CompareToBuilder builder = new CompareToBuilder();

        Object lhs = new Object() {
            int a = 1;
            int b = 2;
        };

        Object rhs = new Object() {
            int a = 2;
            int b = 1;
        };

        builder.append(lhs, rhs);

        int result = builder.toComparison();
        assertEquals(-1, result);

        lhs = new Object() {
            int a = 2;
            int b = 1;
        };

        rhs = new Object() {
            int a = 1;
            int b = 2;
        };

        builder = new CompareToBuilder();
        builder.append(lhs, rhs);

        result = builder.toComparison();
        assertEquals(1, result);

        lhs = new Object() {
            int a = 1;
            int b = 1;
        };

        rhs = new Object() {
            int a = 1;
            int b = 1;
        };

        builder = new CompareToBuilder();
        builder.append(lhs, rhs);

        result = builder.toComparison();
        assertEquals(0, result);
    }

    @Test
    public void testAppendWithArrays() {
        int[] lhs = {1, 2, 3};
        int[] rhs = {2, 1, 3};

        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);

        int result = builder.toComparison();
        assertEquals(-1, result);

        lhs = new int[]{2, 1, 3};
        rhs = new int[]{1, 2, 3};

        builder = new CompareToBuilder();
        builder.append(lhs, rhs);

        result = builder.toComparison();
        assertEquals(1, result);

        lhs = new int[]{1, 1, 1};
        rhs = new int[]{1, 1, 1};

        builder = new CompareToBuilder();
        builder.append(lhs, rhs);

        result = builder.toComparison();
        assertEquals(0, result);
    }

    @Test
    public void testAppendWithArraysAndComparator() {
        int[] lhs = {1, 2, 3};
        int[] rhs = {2, 1, 3};

        CompareToBuilder builder = new CompareToBuilder();
        Comparator<Object> comparator = new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                int[] cast1 = (int[]) o1;
                int[] cast2 = (int[]) o2;
                return Arrays.compare(cast1, cast2);
            }
        };

        builder.append(lhs, rhs, comparator);

        int result = builder.toComparison();
        assertEquals(-1, result);

        lhs = new int[]{2, 1, 3};
        rhs = new int[]{1, 2, 3};

        builder = new CompareToBuilder();
        builder.append(lhs, rhs, comparator);

        result = builder.toComparison();
        assertEquals(1, result);
    }

    @Test
    public void testAppendWithCustomObjectComparator() {
        class TestObject {
            int a;

            TestObject(int a) {
                this.a = a;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                TestObject testObject = (TestObject) o;
                return a == testObject.a;
            }

            @Override
            public int hashCode() {
                return a;
            }
        }

        TestObject lhs = new TestObject(1);
        TestObject rhs = new TestObject(2);

        CompareToBuilder builder = new CompareToBuilder();
        Comparator<Object> comparator = new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                TestObject cast1 = (TestObject) o1;
                TestObject cast2 = (TestObject) o2;
                return Integer.compare(cast1.a, cast2.a);
            }
        };

        builder.append(lhs, rhs, comparator);

        int result = builder.toComparison();
        assertEquals(-1, result);

        lhs = new TestObject(2);
        rhs = new TestObject(1);

        builder = new CompareToBuilder();
        builder.append(lhs, rhs, comparator);

        result = builder.toComparison();
        assertEquals(1, result);
    }

    @Test
    public void testAppendWithNestedObjects() {
        class OuterClass {
            int a;
            InnerClass inner;

            OuterClass(int a) {
                this.a = a;
                this.inner = new InnerClass();
            }
        }

        class InnerClass {
            int b;

            InnerClass() {
                this.b = 1;
            }
        }

        OuterClass lhs = new OuterClass(1);
        OuterClass rhs = new OuterClass(2);

        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);

        int result = builder.toComparison();
        assertEquals(-1, result);

        lhs = new OuterClass(2);
        rhs = new OuterClass(1);

        builder = new CompareToBuilder();
        builder.append(lhs, rhs);

        result = builder.toComparison();
        assertEquals(1, result);
    }

    @Test
    public void testAppendWithNestedObjectsAndComparator() {
        class OuterClass {
            int a;
            InnerClass inner;

            OuterClass(int a) {
                this.a = a;
                this.inner = new InnerClass();
            }
        }

        class InnerClass {
            int b;

            InnerClass() {
                this.b = 1;
            }
        }

        OuterClass lhs = new OuterClass(1);
        OuterClass rhs = new OuterClass(2);

        CompareToBuilder builder = new CompareToBuilder();
        Comparator<Object> comparator = new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                OuterClass cast1 = (OuterClass) o1;
                OuterClass cast2 = (OuterClass) o2;
                int result = Integer.compare(cast1.a, cast2.a);
                if (result == 0) {
                    result = cast1.inner.hashCode() - cast2.inner.hashCode();
                }
                return result;
            }
        };

        builder.append(lhs, rhs, comparator);

        int result = builder.toComparison();
        assertEquals(-1, result);

        lhs = new OuterClass(2);
        rhs = new OuterClass(1);

        builder = new CompareToBuilder();
        builder.append(lhs, rhs, comparator);

        result = builder.toComparison();
        assertEquals(1, result);
    }

    @Test
    public void testAppendWithReflection() throws NoSuchFieldException, IllegalAccessException {
        class TestClass {
            int a;
            int b;

            TestClass() {
            }

            TestClass(int a, int b) {
                this.a = a;
                this.b = b;
            }
        }

        TestClass lhs = new TestClass(1, 2);
        TestClass rhs = new TestClass(2, 1);

        CompareToBuilder builder = new CompareToBuilder();
        Field aField = TestClass.class.getDeclaredField("a");
        aField.setAccessible(true);
        Field bField = TestClass.class.getDeclaredField("b");
        bField.setAccessible(true);

        builder.append(lhs, rhs, aField, bField);

        int result = builder.toComparison();
        assertEquals(-1, result);

        lhs = new TestClass(2, 1);
        rhs = new TestClass(1, 2);

        builder = new CompareToBuilder();
        builder.append(lhs, rhs, aField, bField);

        result = builder.toComparison();
        assertEquals(1, result);
    }

    @Test
    public void testAppendWithReflectionAndComparator() throws NoSuchFieldException, IllegalAccessException {
        class TestClass {
            int a;
            int b;

            TestClass() {
            }

            TestClass(int a, int b) {
                this.a = a;
                this.b = b;
            }
        }

        TestClass lhs = new TestClass(1, 2);
        TestClass rhs = new TestClass(2, 1);

        CompareToBuilder builder = new CompareToBuilder();
        Field aField = TestClass.class.getDeclaredField("a");
        aField.setAccessible(true);
        Field bField = TestClass.class.getDeclaredField("b");
        bField.setAccessible(true);
        Comparator<Object> comparator = new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                TestClass cast1 = (TestClass) o1;
                TestClass cast2 = (TestClass) o2;
                int result = aField.getInt(cast1) - aField.getInt(cast2);
                if (result == 0) {
                    result = bField.getInt(cast1) - bField.getInt(cast2);
                }
                return result;
            }
        };

        builder.append(lhs, rhs, aField, bField, comparator);

        int result = builder.toComparison();
        assertEquals(-1, result);

        lhs = new TestClass(2, 1);
        rhs = new TestClass(1, 2);

        builder = new CompareToBuilder();
        builder.append(lhs, rhs, aField, bField, comparator);

        result = builder.toComparison();
        assertEquals(1, result);
    }
}