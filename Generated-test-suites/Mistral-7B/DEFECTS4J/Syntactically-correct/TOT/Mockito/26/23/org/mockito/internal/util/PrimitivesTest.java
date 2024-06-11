import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.util.Primitives;

public class PrimitivesTest {
    @Test
    public void testPrimitiveTypeOf() {
        Class<Integer> intClass = Integer.class;
        Class<Integer> intType = Primitives.primitiveTypeOf(intClass);
        assertTrue(intType.equals(Integer.TYPE));

        Class<String> stringClass = String.class;
        Class<String> stringType = Primitives.primitiveTypeOf(stringClass);
        assertNull(stringType);
    }
}