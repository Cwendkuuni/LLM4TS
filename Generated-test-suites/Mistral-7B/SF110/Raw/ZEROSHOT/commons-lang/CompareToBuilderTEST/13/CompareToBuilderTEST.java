import org.apache.commons.lang3.*;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.*;
import java.util.*;

public class CompareToBuilderTest {

    @Test
    public void testReflectionCompare() throws Exception {
        Object lhs = new Object() {
            int i = 1;
            int j = 2;
        };
        Object rhs = new Object() {
            int i = 2;
            int j = 1;
        };
        int comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
        Assert.assertEquals(-1, comparison);

        lhs = new Object() {
            int i = 1;
            int j = 1;
        };
        rhs = new Object() {
            int i = 2;
            int j = 2;
        };
        comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
        Assert.assertEquals(0, comparison);

        lhs = new Object() {
            int i = null;
            int j = 1;
        };
        Assert.assertThrows(NullPointerException.class, () -> CompareToBuilder.reflectionCompare(lhs, rhs));

        lhs = new Object() {
            int i = 1;
            int j = null;
        };
        Assert.assertThrows(NullPointerException.class, () -> CompareToBuilder.reflectionCompare(lhs, rhs));

        lhs = new Object() {
            int i = 1;
            int j = 1;
        };
        Comparable<Object> comparable = (Comparable<Object>) lhs;
        rhs = new Object() {
            int i = 1;
            int j = 1;
        };
        comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
        Assert.assertEquals(0, comparison);

        lhs = new int[]{1, 2, 3};
        rhs = new int[]{1, 2, 3};
        comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
        Assert.assertEquals(0, comparison);

        lhs = new int[]{1, 2, 3};
        rhs = new int[]{1, 2, 4};
        comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
        Assert.assertEquals(-1, comparison);

        lhs = new int[]{-1, 2, 3};
        rhs = new int[]{1, 2, 3};
        comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
        Assert.assertEquals(1, comparison);

        lhs = new int[]{-1, 2, 3};
        rhs = new int[]{-1, 2, 3};
        comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
        Assert.assertEquals(0, comparison);

        lhs = new int[]{-1, 2, null};
        rhs = new int[]{1, 2, 3};
        comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
        Assert.assertEquals(-1, comparison);

        lhs = new int[]{-1, 2, 3};
        rhs = new int[]{-1, 2, null};
        comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
        Assert.assertEquals(0, comparison);

        lhs = new int[]{-1, null, 3};
        rhs = new int[]{1, 2, 3};
        comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
        Assert.assertEquals(-1, comparison);

        lhs = new int[]{-1, 2, 3};
        rhs = new int[]{1, null, 3};
        comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
        Assert.assertEquals(-1, comparison);

        lhs = new int[]{-1, 2, 3};
        rhs = new int[]{1, 2, 3};
        Comparator<int[]> comparator = new Comparator<int[]>() {
            @Override
            public int compare(int[] lhs, int[] rhs) {
                return Integer.compare(lhs[0], rhs[0]);
            }
        };
        comparison = CompareToBuilder.reflectionCompare(lhs, rhs, comparator);
        Assert.assertEquals(0, comparison);
    }

    @Test
    public void testAppend() throws Exception {
        Object lhs = new Object() {
            int i = 1;
            int j = 2;
        };
        Object rhs = new Object() {
            int i = 2;
            int j = 1;
        };

        CompareToBuilder builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        int comparison = builder.toComparison();
        Assert.assertEquals(-1, comparison);

        lhs = new Object() {
            int i = 1;
            int j = 1;
        };
        rhs = new Object() {
            int i = 2;
            int j = 2;
        };
        builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        comparison = builder.toComparison();
        Assert.assertEquals(0, comparison);

        lhs = new Object() {
            int i = null;
            int j = 1;
        };
        builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        Assert.assertThrows(NullPointerException.class, () -> builder.toComparison());

        lhs = new Object() {
            int i = 1;
            int j = null;
        };
        builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        Assert.assertThrows(NullPointerException.class, () -> builder.toComparison());

        lhs = new Object() {
            int i = 1;
            int j = 1;
        };
        Comparable<Object> comparable = (Comparable<Object>) lhs;
        rhs = new Object() {
            int i = 1;
            int j = 1;
        };
        builder = new CompareToBuilder();
        builder.append(lhs, rhs);
        comparison = builder.toComparison();
        Assert.assertEquals(0, comparison);

        int[] lhsArray = {1, 2, 3};
        int[] rhsArray = {1, 2, 3};
        builder = new CompareToBuilder();
        builder.append(lhsArray, rhsArray);
        comparison = builder.toComparison();
        Assert.assertEquals(0, comparison);

        lhsArray = new int[]{1, 2, 3};
        rhsArray = new int[]{1, 2, 4};
        builder = new CompareToBuilder();
        builder.append(lhsArray, rhsArray);
        comparison = builder.toComparison();
        Assert.assertEquals(-1, comparison);

        lhsArray = new int[]{-1, 2, 3};
        rhsArray = new int[]{1, 2, 3};
        builder = new CompareToBuilder();
        builder.append(lhsArray, rhsArray);
        comparison = builder.toComparison();
        Assert.assertEquals(1, comparison);

        lhsArray = new int[]{-1, 2, 3};
        rhsArray = new int[]{-1, 2, 3};
        builder = new CompareToBuilder();
        builder.append(lhsArray, rhsArray);
        comparison = builder.toComparison();
        Assert.assertEquals(0, comparison);

        lhsArray = new int[]{-1, 2, null};
        rhsArray = new int[]{1, 2, 3};
        builder = new CompareToBuilder();
        builder.append(lhsArray, rhsArray);
        comparison = builder.toComparison();
        Assert.assertEquals(-1, comparison);

        lhsArray = new int[]{-1, 2, 3};
        rhsArray = new int[]{1, null, 3};
        builder = new CompareToBuilder();
        builder.append(lhsArray, rhsArray);
        comparison = builder.toComparison();
        Assert.assertEquals(-1, comparison);

        lhsArray = new int[]{-1, 2, 3};
        rhsArray = new int[]{1, 2, 3};
        Comparator<int[]> comparator = new Comparator<int[]>() {
            @Override
            public int compare(int[] lhs, int[] rhs) {
                return Integer.compare(lhs[0], rhs[0]);
            }
        };
        builder = new CompareToBuilder();
        builder.append(lhsArray, rhsArray, comparator);
        comparison = builder.toComparison();
        Assert.assertEquals(0, comparison);
    }

    @Test
    public void testAppendSuper() throws Exception {
        Object lhs = new Object() {
            int i = 1;
        };
        Object rhs = new Object() {
            int i = 2;
            int j = 1;
        };

        CompareToBuilder builder = new CompareToBuilder();
        builder.appendSuper(1);
        builder.append(lhs, rhs);
        int comparison = builder.toComparison();
        Assert.assertEquals(1, comparison);

        lhs = new Object() {
            int i = 1;
            int j = 1;
        };
        rhs = new Object() {
            int i = 2;
            int j = 2;
        };
        builder = new CompareToBuilder();
        builder.appendSuper(0);
        builder.append(lhs, rhs);
        comparison = builder.toComparison();
        Assert.assertEquals(0, comparison);
    }
}