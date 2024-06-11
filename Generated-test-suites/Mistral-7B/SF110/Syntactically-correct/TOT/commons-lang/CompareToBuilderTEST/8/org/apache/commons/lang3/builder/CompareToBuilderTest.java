package org.apache.commons.lang3.builder;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class CompareToBuilderTest {

    @Test
    public void testReflectionCompare() {
        Object lhs = new Object() {};
        Object rhs = new Object() {};
        int result = CompareToBuilder.reflectionCompare(lhs, rhs);
        Assert.assertEquals(0, result);

        lhs = new Object() {
            int value = 5;
        };
        rhs = new Object() {
            int value = 5;
        };
        result = CompareToBuilder.reflectionCompare(lhs, rhs);
        Assert.assertEquals(0, result);

        lhs = new Object() {
            int value = 5;
        };
        rhs = new Object() {
            int value = 6;
        };
        result = CompareToBuilder.reflectionCompare(lhs, rhs);
        Assert.assertEquals(-1, result);

        lhs = new Object() {
            int value = 6;
        };
        result = CompareToBuilder.reflectionCompare(lhs, rhs);
        Assert.assertEquals(1, result);

        lhs = null;
        rhs = new Object() {
            int value = 5;
        };
        try {
            result = CompareToBuilder.reflectionCompare(lhs, rhs);
            Assert.fail();
        } catch (NullPointerException e) {
            // Expected
        }

        lhs = new Object() {
            int value = 5;
        };
        rhs = null;
        try {
            result = CompareToBuilder.reflectionCompare(lhs, rhs);
            Assert.fail();
        } catch (NullPointerException e) {
            // Expected
        }

        lhs = new String("Hello");
        rhs = new String("Hello");
        result = CompareToBuilder.reflectionCompare(lhs, rhs);
        Assert.assertEquals(0, result);

        lhs = new String("Hello");
        rhs = new String("World");
        result = CompareToBuilder.reflectionCompare(lhs, rhs);
        Assert.assertEquals(-1, result);

        lhs = new String("World");
        result = CompareToBuilder.reflectionCompare(lhs, rhs);
        Assert.assertEquals(1, result);
    }

    @Test
    public void testReflectionCompareExcludeFields() {
        Object lhs = new TestClass() {
            int field1 = 5;
            int field2 = 6;
        };
        Object rhs = new TestClass() {
            int field1 = 5;
            int field2 = 7;
        };
        int result = CompareToBuilder.reflectionCompare(lhs, rhs, Arrays.asList("field2"));
        Assert.assertEquals(0, result);

        lhs = new TestClass() {
            int field1 = 5;
            int field2 = 6;
        };
        rhs = new TestClass() {
            int field1 = 5;
            int field2 = 7;
            int field3 = 8;
        };
        result = CompareToBuilder.reflectionCompare(lhs, rhs, Arrays.asList("field2", "field3"));
        Assert.assertEquals(0, result);

        lhs = new TestClass() {
            int field1 = 5;
            int field2 = 6;
        };
        rhs = new TestClass() {
            int field1 = 5;
            int field2 = 7;
            int field3 = 8;
        };
        try {
            result = CompareToBuilder.reflectionCompare(lhs, rhs, Arrays.asList("field4"));
            Assert.fail();
        } catch (ArrayIndexOutOfBoundsException e) {
            // Expected
        }
    }

    @Test
    public void testReflectionCompareCompareTransients() {
        class TestTransientClass {
            @SuppressWarnings("unused")
            private transient int transientField;
        }

        Object lhs = new TestTransientClass() {};
        Object rhs = new TestTransientClass() {};
        int result = CompareToBuilder.reflectionCompare(lhs, rhs, true);
        Assert.assertEquals(0, result);

        lhs = new TestTransientClass() {};
        rhs = new TestTransientClass() {
            int transientField = 5;
        };
        result = CompareToBuilder.reflectionCompare(lhs, rhs, true);
        Assert.assertEquals(-1, result);

        lhs = new TestTransientClass() {
            int transientField = 5;
        };
        result = CompareToBuilder.reflectionCompare(lhs, rhs, true);
        Assert.assertEquals(1, result);

        lhs = new TestTransientClass() {};
        rhs = null;
        try {
            result = CompareToBuilder.reflectionCompare(lhs, rhs, true);
            Assert.fail();
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testReflectionCompareSuperCompareTo() {
        class TestSuperClass {
            int superField = 5;
        }

        class TestSubClass extends TestSuperClass {
            int subField = 6;
        }

        Object lhs = new TestSubClass() {};
        Object rhs = new TestSubClass() {};
        int result = CompareToBuilder.reflectionCompare(lhs, rhs);
        Assert.assertEquals(0, result);

        lhs = new TestSubClass() {};
        rhs = new TestSubClass() {
            int superField = 4;
        };
        result = CompareToBuilder.reflectionCompare(lhs, rhs);
        Assert.assertEquals(-1, result);

        lhs = new TestSubClass() {
            int superField = 6;
        };
        result = CompareToBuilder.reflectionCompare(lhs, rhs);
        Assert.assertEquals(1, result);

        lhs = new TestSubClass() {};
        rhs = null;
        try {
            result = CompareToBuilder.reflectionCompare(lhs, rhs);
            Assert.fail();
        } catch (NullPointerException e) {
            // Expected
        }

        lhs = new TestSubClass() {};
        result = CompareToBuilder.appendSuper((CompareToBuilder)null, 4);
        Assert.assertNull(lhs);

        lhs = new TestSubClass() {};
        CompareToBuilder builder = new CompareToBuilder();
        result = builder.appendSuper(lhs, 4);
        Assert.assertEquals(4, result.toComparison());
    }

    @Test
    public void testAppend() {
        Object lhs = new Object[] {1, 2, 3};
        Object rhs = new Object[] {1, 2, 3};
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        int result = builder.toComparison();
        Assert.assertEquals(0, result);

        lhs = new Object[] {1, 2, 3};
        rhs = new Object[] {1, 2, 4};
        builder.append(lhs, rhs);
        result = builder.toComparison();
        Assert.assertEquals(-1, result);

        lhs = new Object[] {1, 2, 3};
        rhs = new Object[] {1, 2, 3, 4};
        try {
            builder.append(lhs, rhs);
            Assert.fail();
        } catch (ArrayStoreException e) {
            // Expected
        }

        lhs = new Object[] {1, 2, 3};
        rhs = new Object[] {1, null, 3};
        try {
            builder.append(lhs, rhs);
            Assert.fail();
        } catch (ArrayStoreException e) {
            // Expected
        }

        lhs = new Object[] {1, 2, 3};
        rhs = null;
        try {
            builder.append(lhs, rhs);
            Assert.fail();
        } catch (NullPointerException e) {
            // Expected
        }

        lhs = null;
        rhs = new Object[] {1, 2, 3};
        try {
            builder.append(lhs, rhs);
            Assert.fail();
        } catch (NullPointerException e) {
            // Expected
        }

        lhs = new int[] {1, 2, 3};
        rhs = new int[] {1, 2, 3};
        builder.append(lhs, rhs);
        result = builder.toComparison();
        Assert.assertEquals(0, result);

        lhs = new int[] {1, 2, 3};
        rhs = new int[] {1, 2, 4};
        builder.append(lhs, rhs);
        result = builder.toComparison();
        Assert.assertEquals(-1, result);

        lhs = new int[] {1, 2, 3};
        rhs = new int[] {1, 2, 3, 4};
        try {
            builder.append(lhs, rhs);
            Assert.fail();
        } catch (ArrayStoreException e) {
            // Expected
        }

        lhs = new int[] {1, 2, 3};
        rhs = new int[] {1, null, 3};
        try {
            builder.append(lhs, rhs);
            Assert.fail();
        } catch (ArrayStoreException e) {
            // Expected
        }

        lhs = new int[] {1, 2, 3};
        rhs = null;
        try {
            builder.append(lhs, rhs);
            Assert.fail();
        } catch (NullPointerException e) {
            // Expected
        }

        lhs = null;
        rhs = new int[] {1, 2, 3};
        try {
            builder.append(lhs, rhs);
            Assert.fail();
        } catch (NullPointerException e) {
            // Expected
        }

        lhs = new String[] {"Hello", "World"};
        rhs = new String[] {"Hello", "World"};
        builder.append(lhs, rhs);
        result = builder.toComparison();
        Assert.assertEquals(0, result);

        lhs = new String[] {"Hello", "World"};
        rhs = new String[] {"Hello", "World", "!"};
        builder.append(lhs, rhs);
        result = builder.toComparison();
        Assert.assertEquals(-1, result);

        lhs = new String[] {"Hello", "World"};
        rhs = new String[] {"Hello", null, "World"};
        try {
            builder.append(lhs, rhs);
            Assert.fail();
        } catch (ArrayStoreException e) {
            // Expected
        }

        lhs = new String[] {"Hello", "World"};
        rhs = null;
        try {
            builder.append(lhs, rhs);
            Assert.fail();
        } catch (NullPointerException e) {
            // Expected
        }

        lhs = null;
        rhs = new String[] {"Hello", "World"};
        try {
            builder.append(lhs, rhs);
            Assert.fail();
        } catch (NullPointerException e) {
            // Expected
        }

        lhs = new Comparable[] {new TestClass(), new TestClass() {}};
        rhs = new Comparable[] {new TestClass(), new TestClass() {}};
        builder.append(lhs, rhs);
        result = builder.toComparison();
        Assert.assertEquals(0, result);

        lhs = new Comparable[] {new TestClass(), new TestClass() {}};
        rhs = new Comparable[] {new TestClass(), new TestClass() {}};
        rhs[0] = new TestClass();
        builder.append(lhs, rhs);
        result = builder.toComparison();
        Assert.assertEquals(-1, result);

        lhs = new Comparable[] {new TestClass(), new TestClass() {}};
        rhs = new Comparable[] {new TestClass(), new TestClass() {}};
        rhs[1] = new TestClass();
        builder.append(lhs, rhs);
        result = builder.toComparison();
        Assert.assertEquals(1, result);

        lhs = new Comparable[] {new TestClass(), new TestClass() {}};
        rhs = new Comparable[] {null, new TestClass() {}};
        try {
            builder.append(lhs, rhs);
            Assert.fail();
        } catch (NullPointerException e) {
            // Expected
        }

        lhs = new Comparable[] {null, new TestClass() {}};
        rhs = new Comparable[] {new TestClass(), new TestClass() {}};
        try {
            builder.append(lhs, rhs);
            Assert.fail();
        } catch (NullPointerException e) {
            // Expected
        }

        lhs = new Comparable[] {null, new TestClass() {}};
        rhs = null;
        try {
            builder.append(lhs, rhs);
            Assert.fail();
        } catch (NullPointerException e) {
            // Expected
        }

        lhs = null;
        rhs = new Comparable[] {new TestClass(), new TestClass() {}};
        try {
            builder.append(lhs, rhs);
            Assert.fail();
        } catch (NullPointerException e) {
            // Expected
        }

        lhs = new Comparable[] {new TestClass(), new TestClass() {}};
        rhs = new Comparable[] {new TestClass(), new TestClass() {}};
        Comparator<Comparable> comparator = (l, r) -> l.hashCode() - r.hashCode();
        builder.append(lhs, rhs, comparator);
        result = builder.toComparison();
        Assert.assertEquals(0, result);

        lhs = new Comparable[] {new TestClass(), new TestClass() {}};
        rhs = new Comparable[] {new TestClass(), new TestClass() {}};
        Comparator<Comparable> comparator2 = (l, r) -> l.hashCode() - r.hashCode();
        rhs[0] = new TestClass();
        builder.append(lhs, rhs, comparator2);
        result = builder.toComparison();
        Assert.assertEquals(-1, result);

        lhs = new Comparable[] {new TestClass(), new TestClass() {}};
        rhs = new Comparable[] {new TestClass(), new TestClass() {}};
        Comparator<Comparable> comparator3 = (l, r) -> l.hashCode() - r.hashCode();
        rhs[1] = new TestClass();
        builder.append(lhs, rhs, comparator3);
        result = builder.toComparison();
        Assert.assertEquals(1, result);

        lhs = new Comparable[] {new TestClass(), new TestClass() {}};
        rhs = new Comparable[] {new TestClass(), new TestClass() {}};
        rhs[0] = null;
        try {
            builder.append(lhs, rhs, comparator);
            Assert.fail();
        } catch (NullPointerException e) {
            // Expected
        }

        lhs = new Comparable[] {null, new TestClass() {}};
        rhs = new Comparable[] {new TestClass(), new TestClass() {}};
        try {
            builder.append(lhs, rhs, comparator);
            Assert.fail();
        } catch (NullPointerException e) {
            // Expected
        }

        lhs = new Comparable[] {null, new TestClass() {}};
        rhs = null;
        try {
            builder.append(lhs, rhs, comparator);
            Assert.fail();
        } catch (NullPointerException e) {
            // Expected
        }

        lhs = null;
        rhs = new Comparable[] {new TestClass(), new TestClass() {}};
        try {
            builder.append(lhs, rhs, comparator);
            Assert.fail();
        } catch (NullPointerException e) {
            // Expected
        }
    }

    private static class TestClass implements Comparable<TestClass> {
        @Override
        public int compareTo(TestClass o) {
            return 0;
        }
    }
}