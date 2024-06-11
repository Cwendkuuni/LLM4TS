package org.mockito.internal.util.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.util.Primitives;

public class PrimitivesTest {

    @Test
    public void testPrimitiveTypeOf() {
        Class<Integer> intClass = Integer.class;
        Class<Integer> expected = Integer.TYPE;
        Class<Integer> actual = Primitives.primitiveTypeOf(intClass);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsPrimitiveWrapper() {
        Class<Boolean> booleanWrapperClass = Boolean.class;
        boolean expected = true;
        boolean actual = Primitives.isPrimitiveWrapper(booleanWrapperClass);
        assertEquals(expected, actual);
    }

    @Test
    public void testPrimitiveWrapperOf() {
        Class<Boolean> booleanWrapperClass = Boolean.class;
        Boolean expected = false;
        Object actual = Primitives.primitiveWrapperOf(booleanWrapperClass);
        assertEquals(expected, actual);
    }

    @Test
    public void testPrimitiveValueOrNullFor() {
        Class<Boolean> booleanClass = Boolean.class;
        Boolean expected = false;
        Object actual = Primitives.primitiveValueOrNullFor(booleanClass);
        assertSame(expected, actual);
    }

    @Test(expected = NullPointerException.class)
    public void testPrimitiveTypeOfNullInput() {
        Class<Object> nullClass = null;
        Assert.assertThrows(NullPointerException.class, () -> Primitives.primitiveTypeOf(nullClass));
    }

    @Test
    public void testPrimitiveTypeOfNonPrimitiveType() {
        Class<String> stringClass = String.class;
        Class<?> expected = String.class;
        Class<?> actual = Primitives.primitiveTypeOf(stringClass);
        assertEquals(expected, actual);
    }

    @Test(expected = NullPointerException.class)
    public void testIsPrimitiveWrapperNullInput() {
        Class<?> nullType = null;
        Assert.assertThrows(NullPointerException.class, () -> Primitives.isPrimitiveWrapper(nullType));
    }

    @Test
    public void testIsPrimitiveWrapperNonWrapperType() {
        Class<Integer> integerClass = Integer.class;
        boolean expected = false;
        boolean actual = Primitives.isPrimitiveWrapper(integerClass);
        assertEquals(expected, actual);
    }

    @Test(expected = NullPointerException.class)
    public void testPrimitiveWrapperOfNullInput() {
        Class<Object> nullType = null;
        Assert.assertThrows(NullPointerException.class, () -> Primitives.primitiveWrapperOf(nullType));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPrimitiveWrapperOfWrongType() {
        Class<String> stringType = String.class;
        Assert.assertThrows(IllegalArgumentException.class, () -> Primitives.primitiveWrapperOf(stringType));
    }

    @Test(expected = NullPointerException.class)
    public void testPrimitiveValueOrNullForNullInput() {
        Class<Object> nullType = null;
        Assert.assertThrows(NullPointerException.class, () -> Primitives.primitiveValueOrNullFor(nullType));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPrimitiveValueOrNullForWrongType() {
        Class<String> stringType = String.class;
        Object expected = null;
        Object actual = Primitives.primitiveValueOrNullFor(stringType);
        assertNull(actual);
    }
}