import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.util.Primitives;

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
        Object booleanWrapper = Primitives.primitiveWrapperOf(Boolean.class);
        assertTrue(booleanWrapper instanceof Boolean);
        assertEquals(false, ((Boolean) booleanWrapper).booleanValue());

        Object charWrapper = Primitives.primitiveWrapperOf(Character.class);
        assertTrue(charWrapper instanceof Character);
        assertEquals('\u0000', ((Character) charWrapper).charValue());

        Object byteWrapper = Primitives.primitiveWrapperOf(Byte.class);
        assertTrue(byteWrapper instanceof Byte);
        assertEquals((byte) 0, ((Byte) byteWrapper).byteValue());

        Object shortWrapper = Primitives.primitiveWrapperOf(Short.class);
        assertTrue(shortWrapper instanceof Short);
        assertEquals((short) 0, ((Short) shortWrapper).shortValue());

        Object intWrapper = Primitives.primitiveWrapperOf(Integer.class);
        assertTrue(intWrapper instanceof Integer);
        assertEquals(0, ((Integer) intWrapper).intValue());

        Object longWrapper = Primitives.primitiveWrapperOf(Long.class);
        assertTrue(longWrapper instanceof Long);
        assertEquals(0L, ((Long) longWrapper).longValue());

        Object floatWrapper = Primitives.primitiveWrapperOf(Float.class);
        assertTrue(floatWrapper instanceof Float);
        assertEquals(0F, ((Float) floatWrapper).floatValue());

        Object doubleWrapper = Primitives.primitiveWrapperOf(Double.class);
        assertTrue(doubleWrapper instanceof Double);
        assertEquals(0D, ((Double) doubleWrapper).doubleValue());
    }

    @Test
    public void testPrimitiveValueOrNullFor() {
        assertTrue(Primitives.primitiveValueOrNullFor(boolean.class) == false);
        assertTrue(Primitives.primitiveValueOrNullFor(char.class) == '\u0000');
        assertTrue(Primitives.primitiveValueOrNullFor(byte.class) == (byte) 0);
        assertTrue(Primitives.primitiveValueOrNullFor(short.class) == (short) 0);
        assertTrue(Primitives.primitiveValueOrNullFor(int.class) == 0);
        assertTrue(Primitives.primitiveValueOrNullFor(long.class) == 0L);
        assertTrue(Primitives.primitiveValueOrNullFor(float.class) == 0F);
        assertTrue(Primitives.primitiveValueOrNullFor(double.class) == 0D);
    }
}