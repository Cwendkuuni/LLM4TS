package org.apache.commons.lang3.builder;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.Builder;
import org.apache.commons.lang3.CompareToBuilder;
import org.apache.commons.lang3.reflect.ReflectionToStringBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Arrays;

public class CompareToBuilderTest {

    @Test
    public void testReflectionCompare() {
        Object lhs = new Object() {
            int i = 1;
            long l = 2;
            short s = 3;
            char c = 'a';
            boolean b = true;
            String str = "test";
        };
        Object rhs = new Object() {
            int i = 1;
            long l = 2;
            short s = 3;
            char c = 'a';
            boolean b = true;
            String str = "test";
        };

        int result = CompareToBuilder.reflectionCompare(lhs, rhs);
        Assert.assertEquals(0, result);

        lhs = null;
        rhs = new Object();
        try {
            result = CompareToBuilder.reflectionCompare(lhs, rhs);
            Assert.fail();
        } catch (NullPointerException e) {
            // Expected
        }

        lhs = new Object();
        rhs = null;
        try {
            result = CompareToBuilder.reflectionCompare(lhs, rhs);
            Assert.fail();
        } catch (NullPointerException e) {
            // Expected
        }

        lhs = new Object();
        rhs = new Object();
        Class<?> reflectUpToClass = Object.class;
        String[] excludeFields = {"i"};
        result = CompareToBuilder.reflectionCompare(lhs, rhs, reflectUpToClass, excludeFields);
        Assert.assertEquals(0, result);

        lhs = new Object();
        rhs = new Object();
        result = CompareToBuilder.reflectionCompare(lhs, rhs, true, ReflectionToStringBuilder.class, new String[0]);
        Assert.assertEquals(0, result);

        lhs = new Object();
        rhs = new Object();
        result = CompareToBuilder.reflectionCompare(lhs, rhs, true, ReflectionToStringBuilder.class, new String[]{"i"});
        Assert.assertEquals(0, result);

        lhs = new Object() {
            int i = 1;
            long l = 2;
            short s = 3;
            char c = 'a';
            boolean b = true;
            String str = "test";
        };
        rhs = new Object() {
            int i = 2;
            long l = 2;
            short s = 3;
            char c = 'a';
            boolean b = true;
            String str = "test";
        };
        result = CompareToBuilder.reflectionCompare(lhs, rhs);
        Assert.assertEquals(-1, result);

        lhs = new Object() {
            int i = 1;
            long l = 2;
            short s = 3;
            char c = 'a';
            boolean b = true;
            String str = "test";
        };
        rhs = new Object() {
            int i = 1;
            long l = 1;
            short s = 3;
            char c = 'a';
            boolean b = true;
            String str = "test";
        };
        result = CompareToBuilder.reflectionCompare(lhs, rhs);
        Assert.assertEquals(1, result);
    }

    @Test
    public void testBuilderAndComparison() {
        Comparable<Integer> comparable = 1;
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(comparable, 1);
        int result = builder.build();
        Assert.assertEquals(0, result);

        comparable = 2;
        builder = new CompareToBuilder();
        builder.append(comparable, 1);
        result = builder.build();
        Assert.assertEquals(-1, result);

        comparable = 1;
        builder = new CompareToBuilder();
        builder.append(1, comparable);
        result = builder.build();
        Assert.assertEquals(0, result);

        Object lhs = new Object() {
            int i = 1;
            long l = 2;
            short s = 3;
            char c = 'a';
            boolean b = true;
            String str = "test";
        };
        Object rhs = new Object() {
            int i = 1;
            long l = 2;
            short s = 3;
            char c = 'a';
            boolean b = false;
            String str = "test";
        };
        builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        result = builder.build();
        Assert.assertEquals(-1, result);

        builder = new CompareToBuilder();
        builder.append(rhs, lhs);
        result = builder.build();
        Assert.assertEquals(1, result);

        lhs = new Object() {
            int i = 1;
            long l = 2;
            short s = 3;
            char c = 'a';
            boolean b = true;
            String str = "test";
        };
        rhs = new Object() {
            int i = 1;
            long l = 2;
            short s = 3;
            char c = 'a';
            boolean b = true;
            String str = null;
        };
        try {
            builder = new CompareToBuilder();
            builder.append(lhs, rhs);
            Assert.fail();
        } catch (NullPointerException e) {
            // Expected
        }

        lhs = new Object() {
            int i = 1;
            long l = 2;
            short s = 3;
            char c = 'a';
            boolean b = true;
            String str = null;
        };
        rhs = new Object() {
            int i = 1;
            long l = 2;
            short s = 3;
            char c = 'a';
            boolean b = true;
            String str = "test";
        };
        builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        result = builder.build();
        Assert.assertEquals(-1, result);

        lhs = new Object() {
            int i = 1;
            long l = 2;
            short s = 3;
            char c = 'a';
            boolean b = true;
            String str = "test";
        };
        rhs = new Object() {
            int i = 1;
            long l = 2;
            short s = 3;
            char c = 'a';
            boolean b = true;
            String str = "test1";
        };
        builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        result = builder.build();
        Assert.assertEquals(0, result);

        lhs = new Object[]{"a", "b", "c"};
        rhs = new Object[]{"a", "b", "c"};
        builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        result = builder.build();
        Assert.assertEquals(0, result);

        lhs = new Object[]{"a", "b", "c"};
        rhs = new Object[]{"a", "b", null};
        try {
            builder = new CompareToBuilder();
            builder.append(lhs, rhs);
            Assert.fail();
        } catch (NullPointerException e) {
            // Expected
        }

        lhs = new Object[]{"a", "b", null};
        rhs = new Object[]{"a", "b", "c"};
        try {
            builder = new CompareToBuilder();
            builder.append(lhs, rhs);
            Assert.fail();
        } catch (NullPointerException e) {
            // Expected
        }

        lhs = new Object[]{"a", "b", "c", "d"};
        rhs = new Object[]{"a", "b", "c", "d"};
        builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        result = builder.build();
        Assert.assertEquals(0, result);

        lhs = new Object[]{"a", "b", "c", "d"};
        rhs = new Object[]{"a", "b", "c", null};
        try {
            builder = new CompareToBuilder();
            builder.append(lhs, rhs);
            Assert.fail();
        } catch (NullPointerException e) {
            // Expected
        }

        lhs = new Object[]{"a", "b", "c", null};
        rhs = new Object[]{"a", "b", "c", "d"};
        try {
            builder = new CompareToBuilder();
            builder.append(lhs, rhs);
            Assert.fail();
        } catch (NullPointerException e) {
            // Expected
        }

        lhs = new Object[]{"a", "b", "c", "d"};
        rhs = new Object[]{"a", "b", "c", "d"};
        Arrays.sort(lhs);
        Arrays.sort(rhs);
        builder = new CompareToBuilder();
        builder.append(lhs, rhs, Collections.reverseOrder());
        result = builder.build();
        Assert.assertEquals(-1, result);

        lhs = new int[]{1, 2, 3, 4};
        rhs = new int[]{1, 2, 3, 4};
        builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        result = builder.build();
        Assert.assertEquals(0, result);

        lhs = new int[]{1, 2, 3, 4};
        rhs = new int[]{1, 2, 3, 5};
        builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        result = builder.build();
        Assert.assertEquals(-1, result);

        lhs = new int[]{1, 2, 3, 4};
        rhs = new int[]{1, 2, 3, 4};
        Arrays.sort(lhs);
        Arrays.sort(rhs);
        builder = new CompareToBuilder();
        builder.append(lhs, rhs, Collections.reverseOrder());
        result = builder.build();
        Assert.assertEquals(1, result);

        lhs = new int[]{1, 2, 3, 4};
        rhs = new int[]{2, 3, 3, 4};
        builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        result = builder.build();
        Assert.assertEquals(-1, result);

        lhs = new int[]{1, 2, 3, 4};
        rhs = new int[]{1, 3, 3, 4};
        builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        result = builder.build();
        Assert.assertEquals(-1, result);

        lhs = new int[]{1, 2, 3, 4};
        rhs = new int[]{1, 2, 3, 5};
        builder = new CompareToBuilder();
        builder.append(lhs, rhs, new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                return o1[3] - o2[3];
            }
        });
        result = builder.build();
        Assert.assertEquals(1, result);
    }
}