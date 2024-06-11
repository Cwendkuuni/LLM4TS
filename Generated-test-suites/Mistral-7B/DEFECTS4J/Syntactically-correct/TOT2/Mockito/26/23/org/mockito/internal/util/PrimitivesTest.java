package org.mockito.internal.util.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.util.Primitives;

public class PrimitivesTest {

    @Test
    public void testPrimitiveTypeOf() {
        Assert.assertEquals(Integer.TYPE.getClass(), Primitives.primitiveTypeOf(Integer.class));
        Assert.assertEquals(Integer.class, Primitives.primitiveTypeOf(Integer.TYPE));
    }

    @Test
    public void testIsPrimitiveWrapper() {
        Assert.assertTrue(Primitives.isPrimitiveWrapper(Boolean.class));
        Assert.assertFalse(Primitives.isPrimitiveWrapper(String.class));
    }

    @Test
    public void testPrimitiveWrapperOf() {
        Object booleanWrapper = Primitives.primitiveWrapperOf(Boolean.class);
        Assert.assertFalse((Boolean) booleanWrapper);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPrimitiveWrapperOfWithNonPrimitive() {
        Primitives.primitiveWrapperOf(String.class);
    }

    @Test
    public void testPrimitiveValueOrNullFor() {
        Assert.assertEquals(false, (Boolean) Primitives.primitiveValueOrNullFor(boolean.class));
    }

    @Test
    public void testPrimitiveTypeOfWithNull() {
        Assert.assertNull(Primitives.primitiveTypeOf(null));
    }

    @Test
    public void testPrimitiveTypeOfWithNonPrimitive() {
        Assert.assertNull(Primitives.primitiveTypeOf(String.class));
    }

    @Test
    public void testIsPrimitiveWrapperWithNonPrimitive() {
        Assert.assertFalse(Primitives.isPrimitiveWrapper(String.class));
    }

    @Test
    public void testPrimitiveValueOrNullForWithNoMapping() {
        Assert.assertNull(Primitives.primitiveValueOrNullFor(Enum.class));
    }

}