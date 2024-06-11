import org.junit.Test;
import org.junit.Assert.*;

public class PrimitivesTest {

    @Test
    public void testPrimitiveTypeOf() {
        Class<Integer> integerClass = Integer.class;
        Class<Integer> expectedType = Integer.TYPE;
        Class<Integer> result = Primitives.primitiveTypeOf(integerClass);
        assertEquals(expectedType, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPrimitiveTypeOfWithNullInput() {
        assertNull(Primitives.primitiveTypeOf(null));
    }
}