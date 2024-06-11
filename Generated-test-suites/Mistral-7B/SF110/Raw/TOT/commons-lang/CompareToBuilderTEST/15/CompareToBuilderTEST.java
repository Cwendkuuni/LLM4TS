package org.apache.commons.lang3.builder;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.reflect.ReflectionToStringBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class CompareToBuilderTest {

    private Integer lhs;
    private Integer rhs;
    private int expectedComparison;

    public CompareToBuilderTest(Integer lhs, Integer rhs, int expectedComparison) {
        this.lhs = lhs;
        this.rhs = rhs;
        this.expectedComparison = expectedComparison;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {null, null, 0},
                {1, 1, 0},
                {1, null, -1},
                {null, 1, 1},
                {Integer.MIN_VALUE, Integer.MIN_VALUE, 0},
                {Integer.MIN_VALUE, -1, -1},
                {Integer.MAX_VALUE, Integer.MAX_VALUE, 0},
                {Integer.MAX_VALUE, 1, 1},
                {1, new Integer(1), 0},
                {new Integer(1), 1, 0},
                {new Integer(1), new Integer(2), -1},
                {new Integer(2), new Integer(1), 1},
                {new int[]{1, 1, 1}, new int[]{1, 1, 1}, 0},
                {new int[]{1, 1, 1}, new int[]{1, 1, 2}, -1},
                {new int[]{1, 1, 1}, null, -1},
                {null, new int[]{1, 1, 1}, 1},
                {new double[]{1.0, 1.0, 1.0}, new double[]{1.0, 1.0, 1.0}, 0},
                {new double[]{1.0, 1.0, 1.0}, new double[]{1.0, 1.0, 2.0}, -1},
                {new double[]{1.0, 1.0, 1.0}, null, -1},
                {null, new double[]{1.0, 1.0, 1.0}, 1},
                {new boolean[]{true, true, true}, new boolean[]{true, true, true}, 0},
                {new boolean[]{true, true, true}, new boolean[]{false, false, false}, -1},
                {new boolean[]{true, true, true}, null, -1},
                {null, new boolean[]{true, true, true}, 1},
                {new String[]{"a", "a", "a"}, new String[]{"a", "a", "a"}, 0},
                {new String[]{"a", "a", "a"}, new String[]{"a", "a", "b"}, -1},
                {new String[]{"a", "a", "a"}, null, -1},
                {null, new String[]{"a", "a", "a"}, 1},
        });
    }

    @Before
    public void setUp() {
    }

    @Test
    public void testReflectionCompare() {
        int comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
        assertEquals(expectedComparison, comparison);
    }

    @Test
    public void testReflectionCompareWithExcludeFields() {
        int comparison = CompareToBuilder.reflectionCompare(lhs, rhs, Arrays.asList("field1", "field2"));
        assertEquals(expectedComparison, comparison);
    }

    @Test(expected = NullPointerException.class)
    public void testReflectionCompareWithNullLhs() {
        CompareToBuilder.reflectionCompare(null, rhs);
    }

    @Test(expected = NullPointerException.class)
    public void testReflectionCompareWithNullRhs() {
        CompareToBuilder.reflectionCompare(lhs, null);
    }

    @Test(expected = ClassCastException.class)
    public void testReflectionCompareWithDifferentTypes() {
        CompareToBuilder.reflectionCompare(new Integer(1), new String("1"));
    }

    @Test(expected = ClassCastException.class)
    public void testReflectionCompareWithDifferentSuperClasses() {
        CompareToBuilder.reflectionCompare(new Object(), new Object());
    }
}