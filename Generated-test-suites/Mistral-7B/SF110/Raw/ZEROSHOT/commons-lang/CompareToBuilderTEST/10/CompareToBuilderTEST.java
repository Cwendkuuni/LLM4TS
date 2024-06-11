package org.apache.commons.lang3;

import static org.junit.Assert.*;

import org.apache.commons.lang3.*;
import org.junit.Test;

import java.lang.reflect.*;
import java.util.*;

public class CompareToBuilderTest {

    @Test
    public void testReflectionCompare() {
        Object obj1 = new Object() {
            int value = 1;
        };
        Object obj2 = new Object() {
            int value = 2;
        };
        int comparison = CompareToBuilder.reflectionCompare(obj1, obj2);
        assertEquals(-1, comparison);

        comparison = CompareToBuilder.reflectionCompare(obj2, obj1);
        assertEquals(1, comparison);

        comparison = CompareToBuilder.reflectionCompare(obj1, obj1);
        assertEquals(0, comparison);

        comparison = CompareToBuilder.reflectionCompare(null, obj1);
        assertNull(obj1);
        assertEquals(-1, comparison);

        comparison = CompareToBuilder.reflectionCompare(obj1, null);
        assertNull(obj1);
        assertEquals(1, comparison);

        comparison = CompareToBuilder.reflectionCompare(obj1, obj1, true);
        assertEquals(0, comparison);

        comparison = CompareToBuilder.reflectionCompare(obj1, obj2, "value");
        assertEquals(-1, comparison);

        comparison = CompareToBuilder.reflectionCompare(obj1, obj2, new String[]{"value"});
        assertEquals(-1, comparison);

        comparison = CompareToBuilder.reflectionCompare(obj1, obj2, false, Object.class, new String[]{"value"});
        assertEquals(0, comparison);

        comparison = CompareToBuilder.reflectionCompare(obj1, obj2, false, Object.class, null);
        assertEquals(0, comparison);

        comparison = CompareToBuilder.reflectionCompare(obj1, obj2, false, String.class, new String[]{"value"});
        assertEquals(0, comparison);
    }

    @Test
    public void testAppend() {
        Object obj1 = new Object() {
            int value = 1;
        };
        Object obj2 = new Object() {
            int value = 2;
        };

        CompareToBuilder builder = new CompareToBuilder();
        builder.append(obj1, obj2);
        assertEquals(-1, builder.toComparison());

        builder = new CompareToBuilder();
        builder.append(obj2, obj1);
        assertEquals(1, builder.toComparison());

        builder = new CompareToBuilder();
        builder.append(obj1, obj1);
        assertEquals(0, builder.toComparison());

        builder = new CompareToBuilder();
        builder.append(null, obj1);
        assertNull(obj1);
        assertEquals(-1, builder.toComparison());

        builder = new CompareToBuilder();
        builder.append(obj1, null);
        assertNull(obj1);
        assertEquals(1, builder.toComparison());

        builder = new CompareToBuilder();
        builder.appendSuper(1);
        assertEquals(1, builder.toComparison());

        int[] intArray1 = {1, 2};
        int[] intArray2 = {2, 1};
        builder = new CompareToBuilder();
        builder.append(intArray1, intArray2);
        assertEquals(-1, builder.toComparison());

        builder = new CompareToBuilder();
        builder.append(intArray2, intArray1);
        assertEquals(1, builder.toComparison());

        builder = new CompareToBuilder();
        builder.append(intArray1, intArray1);
        assertEquals(0, builder.toComparison());

        builder = new CompareToBuilder();
        builder.append(null, intArray1);
        assertNull(intArray1);
        assertEquals(-1, builder.toComparison());

        builder = new CompareToBuilder();
        builder.append(intArray1, null);
        assertNull(intArray1);
        assertEquals(1, builder.toComparison());

        char[] charArray1 = {'a', 'b'};
        char[] charArray2 = {'b', 'a'};
        builder = new CompareToBuilder();
        builder.append(charArray1, charArray2);
        assertEquals(-1, builder.toComparison());

        builder = new CompareToBuilder();
        builder.append(charArray2, charArray1);
        assertEquals(1, builder.toComparison());

        builder = new CompareToBuilder();
        builder.append(charArray1, charArray1);
        assertEquals(0, builder.toComparison());

        builder = new CompareToBuilder();
        builder.append(null, charArray1);
        assertNull(charArray1);
        assertEquals(-1, builder.toComparison());

        builder = new CompareToBuilder();
        builder.append(charArray1, null);
        assertNull(charArray1);
        assertEquals(1, builder.toComparison());
    }

    @Test(expected = NullPointerException.class)
    public void testNullPointerException() {
        Object obj1 = null;
        Object obj2 = new Object() {
            int value = 1;
        };

        CompareToBuilder builder = new CompareToBuilder();
        builder.append(obj1, obj2);
    }

    @Test(expected = ClassCastException.class)
    public void testClassCastException() {
        Object obj1 = new Object() {
            int value = 1;
        };
        String obj2 = "test";

        CompareToBuilder builder = new CompareToBuilder();
        builder.append(obj1, obj2);
    }

}