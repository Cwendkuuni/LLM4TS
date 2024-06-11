import org.junit.Test;
import org.junit.Assert.*;

public class PrimitivesTest {
    @Test
    public void testPrimitiveTypeOf() {
        Class<Integer> intClass = Integer.class;
        Class<Integer> intType = Primitives.primitiveTypeOf(intClass);
        assertTrue(intType.isPrimitive());
        assertEquals(Integer.TYPE, intType);

        Class<String> stringClass = String.class;
        Class<String> stringType = Primitives.primitiveTypeOf(stringClass);
        assertFalse(stringType.isPrimitive());
        assertNull(stringType);

        Class<Object[]> arrayClass = Object[].class;
        Class<Object[]> arrayType = Primitives.primitiveTypeOf(arrayClass);
        assertNull(arrayType);
    }

    @Test
    public void testIsPrimitiveWrapper() {
        assertTrue(Primitives.isPrimitiveWrapper(Boolean.class));
        assertTrue(Primitives.isPrimitiveWrapper(Character.class));
        assertTrue(Primitives.isPrimitiveWrapper(Byte.class));
        assertTrue(Primitives.isPrimitiveWrapper(Short.class));
        assertTrue(Primitives.isPrimitiveWrapper(Integer.class));
        assertTrue(Primitives.isPrimitiveWrapper(Long.class));
        assertTrue(Primitives.isPrimitiveWrapper(Float.class));
        assertTrue(Primitives.isPrimitiveWrapper(Double.class));

        assertFalse(Primitives.isPrimitiveWrapper(String.class));
        assertFalse(Primitives.isPrimitiveWrapper(Object.class));
    }

    @Test
    public void testPrimitiveWrapperOf() {
        assertEquals(false, Primitives.primitiveWrapperOf(Boolean.class));
        assertEquals('\u0000', Primitives.primitiveWrapperOf(Character.class));
        assertEquals((byte) 0, Primitives.primitiveWrapperOf(Byte.class));
        assertEquals((short) 0, Primitives.primitiveWrapperOf(Short.class));
        assertEquals(0, Primitives.primitiveWrapperOf(Integer.class));
        assertEquals(0L, Primitives.primitiveWrapperOf(Long.class));
        assertEquals(0F, Primitives.primitiveWrapperOf(Float.class));
        assertEquals(0D, Primitives.primitiveWrapperOf(Double.class));
    }

    @Test
    public void testPrimitiveValueOrNullFor() {
        assertEquals(false, Primitives.primitiveValueOrNullFor(boolean.class));
        assertEquals('\u0000', Primitives.primitiveValueOrNullFor(char.class));
        assertEquals((byte) 0, Primitives.primitiveValueOrNullFor(byte.class));
        assertEquals((short) 0, Primitives.primitiveValueOrNullFor(short.class));
        assertEquals(0, Primitives.primitiveValueOrNullFor(int.class));
        assertEquals(0L, Primitives.primitiveValueOrNullFor(long.class));
        assertEquals(0F, Primitives.primitiveValueOrNullFor(float.class));
        assertEquals(0D, Primitives.primitiveValueOrNullFor(double.class));

        assertNull(Primitives.primitiveValueOrNullFor(String.class));
        assertNull(Primitives.primitiveValueOrNullFor(Object.class));
    }

    @Test(expected = NullPointerException.class)
    public void testNullArgumentForPrimitiveValueOrNullFor() {
        Primitives.primitiveValueOrNullFor(null);
    }
}