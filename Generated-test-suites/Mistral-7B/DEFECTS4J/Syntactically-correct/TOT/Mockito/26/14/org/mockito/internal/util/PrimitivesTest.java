package org.mockito.internal.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class PrimitivesTest {

    @Test
    public void testPrimitiveTypeOf() {
        assertEquals(Integer.TYPE, Primitives.primitiveTypeOf(Integer.class));
        assertEquals(Boolean.TYPE, Primitives.primitiveTypeOf(Boolean.class));
        assertEquals(Character.TYPE, Primitives.primitiveTypeOf(Character.class));
        assertEquals(String.class, Primitives.primitiveTypeOf(String.class));
    }

    @Test
    public void testIsPrimitiveWrapper() {
        assertTrue(Primitives.isPrimitiveWrapper(Boolean.class));
        assertTrue(Primitives.isPrimitiveWrapper(Character.class));
        assertFalse(Primitives.isPrimitiveWrapper(String.class));
    }

    @Test
    public void testPrimitiveWrapperOf() {
        assertEquals(false, Primitives.primitiveWrapperOf(Boolean.class));
        assertEquals('\u0000', Primitives.primitiveWrapperOf(Character.class));
        assertEquals(0, Primitives.primitiveWrapperOf(Integer.class).intValue());
        assertEquals(0L, Primitives.primitiveWrapperOf(Long.class).longValue());
        assertEquals(0F, Primitives.primitiveWrapperOf(Float.class).floatValue());
        assertEquals(0D, Primitives.primitiveWrapperOf(Double.class).doubleValue());
    }

    @Test
    public void testPrimitiveValueOrNullFor() {
        assertEquals(false, Primitives.primitiveValueOrNullFor(boolean.class));
        assertEquals('\u0000', Primitives.primitiveValueOrNullFor(char.class));
        assertEquals(0, Primitives.primitiveValueOrNullFor(int.class));
        assertEquals(0L, Primitives.primitiveValueOrNullFor(long.class));
        assertEquals(0F, Primitives.primitiveValueOrNullFor(float.class));
        assertEquals(0D, Primitives.primitiveValueOrNullFor(double.class));
    }
}