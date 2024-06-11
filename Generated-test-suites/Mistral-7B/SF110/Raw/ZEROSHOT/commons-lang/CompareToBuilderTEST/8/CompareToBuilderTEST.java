package org.apache.commons.lang3.builder;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Arrays;

public class CompareToBuilderTest {

    @Test
    public void testReflectionCompare() {
        Object obj1 = new Object() {
            int field1 = 1;
            int field2 = 2;
        };
        Object obj2 = new Object() {
            int field1 = 1;
            int field2 = 3;
        };

        int comparison = CompareToBuilder.reflectionCompare(obj1, obj2);
        Assert.assertEquals(-1, comparison);

        obj1 = new Object() {
            int field1 = 1;
            int field2 = 3;
        };
        comparison = CompareToBuilder.reflectionCompare(obj1, obj2);
        Assert.assertEquals(1, comparison);

        obj1 = new Object() {
            int field1 = 1;
            int field2 = 2;
        };
        Object obj3 = new Object() {
            int field1 = 2;
            int field2 = 2;
        };
        comparison = CompareToBuilder.reflectionCompare(obj1, obj3);
        Assert.assertEquals(0, comparison);

        obj1 = new Object() {
            int field1 = null;
            int field2 = 2;
        };
        comparison = CompareToBuilder.reflectionCompare(obj1, obj2);
        Assert.assertEquals(-1, comparison);

        obj1 = new Object() {
            int field1 = 2;
            int field2 = null;
        };
        comparison = CompareToBuilder.reflectionCompare(obj1, obj2);
        Assert.assertEquals(1, comparison);
    }

    @Test
    public void testReflectionCompareWithTransients() {
        class TestObject {
            @SuppressWarnings("unused")
            transient int transientField = 1;
            int nonTransientField = 1;
        }

        TestObject obj1 = new TestObject();
        TestObject obj2 = new TestObject();

        obj1.nonTransientField = 2;
        obj2.nonTransientField = 3;

        int comparison = CompareToBuilder.reflectionCompare(obj1, obj2, true);
        Assert.assertEquals(-1, comparison);

        obj1.nonTransientField = 3;
        comparison = CompareToBuilder.reflectionCompare(obj1, obj2, true);
        Assert.assertEquals(1, comparison);
    }

    @Test
    public void testReflectionCompareWithExcludedFields() {
        class TestObject {
            int excludedField = 1;
            int field1 = 1;
            int field2 = 2;
        }

        TestObject obj1 = new TestObject();
        TestObject obj2 = new TestObject();

        obj1.field1 = 2;
        obj2.field1 = 3;

        int comparison = CompareToBuilder.reflectionCompare(obj1, obj2, Arrays.asList("excludedField"));
        Assert.assertEquals(-1, comparison);

        obj1.field1 = 3;
        comparison = CompareToBuilder.reflectionCompare(obj1, obj2, Arrays.asList("excludedField"));
        Assert.assertEquals(1, comparison);

        obj1.excludedField = 1;
        obj2.excludedField = 2;

        comparison = CompareToBuilder.reflectionCompare(obj1, obj2, Arrays.asList("excludedField"));
        Assert.assertEquals(0, comparison);
    }

    @Test
    public void testBuild() {
        CompareToBuilder builder = new CompareToBuilder();

        builder.append(1, 2);
        Assert.assertEquals(-1, builder.build());

        builder.append(2, 1);
        Assert.assertEquals(1, builder.build());

        builder.append(1, 1);
        Assert.assertEquals(0, builder.build());

        builder.append("Hello", "World");
        Assert.assertEquals(-1, builder.build());

        builder.append("World", "Hello");
        Assert.assertEquals(1, builder.build());
    }

    @Test
    public void testAppendSuper() {
        Object superClass = Object.class;
        Object subClass1 = new Object() {}.getClass();
        Object subClass2 = new Object() {}.getClass();

        CompareToBuilder builder = new CompareToBuilder();

        builder.appendSuper(CompareToBuilder.reflectionCompare(superClass, subClass1));
        Assert.assertEquals(-1, builder.build());

        builder.appendSuper(CompareToBuilder.reflectionCompare(subClass1, subClass2));
        Assert.assertEquals(1, builder.build());
    }

    @Test
    public void testAppendArrays() {
        Object[] array1 = new Object[]{1, 2, 3};
        Object[] array2 = new Object[]{1, 2, 3};
        Object[] array3 = new Object[]{1, 2, 4};

        CompareToBuilder builder = new CompareToBuilder();

        builder.append(array1, array2);
        Assert.assertEquals(0, builder.build());

        builder.append(array1, array3);
        Assert.assertEquals(-1, builder.build());

        builder.append(array3, array2);
        Assert.assertEquals(1, builder.build());

        builder.append(new int[]{1, 2, 3}, new int[]{1, 2, 3});
        Assert.assertEquals(0, builder.build());

        builder.append(new int[]{1, 2, 3}, new int[]{1, 2, 4});
        Assert.assertEquals(-1, builder.build());

        builder.append(new int[]{1, 2, 4}, new int[]{1, 2, 3});
        Assert.assertEquals(1, builder.build());
    }

    @Test
    public void testAppendComparableObjects() {
        String str1 = "Hello";
        String str2 = "World";

        CompareToBuilder builder = new CompareToBuilder();

        builder.append(str1, str2);
        Assert.assertEquals(-1, builder.build());

        builder.append(str2, str1);
        Assert.assertEquals(1, builder.build());

        Comparable<String> strComparable1 = (Comparable<String>) str1;
        Comparable<String> strComparable2 = (Comparable<String>) str2;

        builder.append(strComparable1, strComparable2);
        Assert.assertEquals(-1, builder.build());

        builder.append(strComparable2, strComparable1);
        Assert.assertEquals(1, builder.build());
    }

    @Test
    public void testAppendPrimitiveTypes() {
        CompareToBuilder builder = new CompareToBuilder();

        builder.append((short) 1, (short) 2);
        Assert.assertEquals(-1, builder.build());

        builder.append((short) 2, (short) 1);
        Assert.assertEquals(1, builder.build());

        builder.append((byte) 1, (byte) 2);
        Assert.assertEquals(-1, builder.build());

        builder.append((byte) 2, (byte) 1);
        Assert.assertEquals(1, builder.build());

        builder.append((char) 'a', (char) 'b');
        Assert.assertEquals(-1, builder.build());

        builder.append((char) 'b', (char) 'a');
        Assert.assertEquals(1, builder.build());

        builder.append((int) 1, (int) 2);
        Assert.assertEquals(-1, builder.build());

        builder.append((int) 2, (int) 1);
        Assert.assertEquals(1, builder.build());

        builder.append((long) 1, (long) 2);
        Assert.assertEquals(-1, builder.build());

        builder.append((long) 2, (long) 1);
        Assert.assertEquals(1, builder.build());

        builder.append((float) 1.0f, (float) 2.0f);
        Assert.assertEquals(-1, builder.build());

        builder.append((float) 2.0f, (float) 1.0f);
        Assert.assertEquals(1, builder.build());

        builder.append((double) 1.0, (double) 2.0);
        Assert.assertEquals(-1, builder.build());

        builder.append((double) 2.0, (double) 1.0);
        Assert.assertEquals(1, builder.build());

        builder.append((boolean) false, (boolean) true);
        Assert.assertEquals(-1, builder.build());

        builder.append((boolean) true, (boolean) false);
        Assert.assertEquals(1, builder.build());
    }

    @Test
    public void testAppendCustomComparator() {
        int[] array1 = {1, 2, 3};
        int[] array2 = {3, 2, 1};

        Comparator<int[]> comparator = (Comparator<int[]>) (lhs, rhs) -> {
            if (lhs == rhs) {
                return 0;
            }
            if (lhs == null || rhs == null) {
                throw new NullPointerException();
            }
            if (lhs.length != rhs.length) {
                return Arrays.toString(lhs).compareTo(Arrays.toString(rhs));
            }
            for (int i = 0; i < lhs.length && this.comparison == 0; ++i) {
                this.append(lhs[i], rhs[i]);
            }
            return this.comparison;
        };

        CompareToBuilder builder = new CompareToBuilder();

        builder.append(array1, array2, comparator);
        Assert.assertEquals(-1, builder.build());

        int[] array3 = Arrays.copyOf(array1, array1.length + 1);
        Arrays.fill(array3, Integer.MAX_VALUE);

        builder.append(array1, array3, comparator);
        Assert.assertEquals(-1, builder.build());

        builder.append(array3, array1, comparator);
        Assert.assertEquals(1, builder.build());
    }
}