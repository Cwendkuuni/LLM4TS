import org.junit.Test;
import org.junit.Assert.*;
import static org.mockito.internal.util.Primitives.*;

public class PrimitivesTest {

    @Test
    public void testPrimitiveTypeOf() {
        Class<Integer> intClass = Integer.class;
        Class<Integer> expectedIntType = Integer.TYPE;
        Class<String> stringClass = String.class;
        Class<String> expectedStringType = String.class;

        // Test with primitive type
        Class<Integer> actualIntType = primitiveTypeOf(intClass);
        assertEquals(expectedIntType, actualIntType);

        // Test with non-primitive type
        Class<String> actualStringType = primitiveTypeOf(stringClass);
        assertEquals(expectedStringType, actualStringType);
    }

    @Test
    public void testIsPrimitiveWrapper() {
        Class<Boolean> booleanWrapperClass = Boolean.class;
        Class<Character> characterWrapperClass = Character.class;
        Class<Byte> byteWrapperClass = Byte.class;
        Class<Short> shortWrapperClass = Short.class;
        Class<Integer> integerWrapperClass = Integer.class;
        Class<Long> longWrapperClass = Long.class;
        Class<Float> floatWrapperClass = Float.class;
        Class<Double> doubleWrapperClass = Double.class;

        // Test with primitive types
        assertFalse(isPrimitiveWrapper(boolean.class));
        assertFalse(isPrimitiveWrapper(character.class));
        assertFalse(isPrimitiveWrapper(byte.class));
        assertFalse(isPrimitiveWrapper(short.class));
        assertFalse(isPrimitiveWrapper(int.class));
        assertFalse(isPrimitiveWrapper(long.class));
        assertFalse(isPrimitiveWrapper(float.class));
        assertFalse(isPrimitiveWrapper(double.class));

        // Test with wrapper classes
        assertTrue(isPrimitiveWrapper(Boolean.class));
        assertTrue(isPrimitiveWrapper(Character.class));
        assertTrue(isPrimitiveWrapper(Byte.class));
        assertTrue(isPrimitiveWrapper(Short.class));
        assertTrue(isPrimitiveWrapper(Integer.class));
        assertTrue(isPrimitiveWrapper(Long.class));
        assertTrue(isPrimitiveWrapper(Float.class));
        assertTrue(isPrimitiveWrapper(Double.class));
    }

    @Test
    public void testPrimitiveWrapperOf() {
        Class<Boolean> booleanWrapperClass = Boolean.class;
        Class<Character> characterWrapperClass = Character.class;
        Class<Byte> byteWrapperClass = Byte.class;
        Class<Short> shortWrapperClass = Short.class;
        Class<Integer> integerWrapperClass = Integer.class;
        Class<Long> longWrapperClass = Long.class;
        Class<Float> floatWrapperClass = Float.class;
        Class<Double> doubleWrapperClass = Double.class;

        // Test with primitive types
        assertEquals(false, primitiveWrapperOf(boolean.class));
        assertEquals('\u0000', primitiveWrapperOf(char.class));
        assertEquals((byte) 0, primitiveWrapperOf(byte.class));
        assertEquals((short) 0, primitiveWrapperOf(short.class));
        assertEquals(0, primitiveWrapperOf(int.class));
        assertEquals(0L, primitiveWrapperOf(long.class));
        assertEquals(0F, primitiveWrapperOf(float.class));
        assertEquals(0D, primitiveWrapperOf(double.class));

        // Test with wrapper classes
        assertEquals(false, (Boolean) primitiveWrapperOf(Boolean.class));
        assertEquals('\u0000', (Character) primitiveWrapperOf(Character.class));
        assertEquals((byte) 0, (Byte) primitiveWrapperOf(Byte.class));
        assertEquals((short) 0, (Short) primitiveWrapperOf(Short.class));
        assertEquals(0, (Integer) primitiveWrapperOf(Integer.class));
        assertEquals(0L, (Long) primitiveWrapperOf(Long.class));
        assertEquals(0F, (Float) primitiveWrapperOf(Float.class));
        assertEquals(0D, (Double) primitiveWrapperOf(Double.class));
    }
}