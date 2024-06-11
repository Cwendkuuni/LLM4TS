import org.junit.Test;
import org.junit.Assert.*;

public class PrimitivesTest {

    @Test
    public void testPrimitiveTypeOf() {
        Class<Integer> intClass = Integer.class;
        Class<Integer> expectedIntType = Integer.TYPE;
        Class<Integer> actualIntType = Primitives.primitiveTypeOf(intClass);
        assertEquals(expectedIntType, actualIntType);
    }

    @Test(expected = NullPointerException.class)
    public void testPrimitiveTypeOfNullClass() {
        Class<?> nullClass = null;
        Primitives.primitiveTypeOf(nullClass);
    }

    @Test
    public void testPrimitiveTypeOfNonPrimitive() {
        Class<String> stringClass = String.class;
        Class<?> expectedType = null;
        Class<?> actualType = Primitives.primitiveTypeOf(stringClass);
        assertNull(actualType);
    }
}