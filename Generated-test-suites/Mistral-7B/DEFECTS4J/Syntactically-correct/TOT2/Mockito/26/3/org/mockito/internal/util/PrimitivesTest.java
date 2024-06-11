import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.util.Primitives;

public class PrimitivesTest {

    @Test
    public void testPrimitiveTypeOf() {
        Class<Boolean> booleanType = Boolean.class;
        Class<Integer> integerType = Integer.class;

        Class<Boolean> expectedBoolean = Boolean.TYPE;
        Class<Integer> expectedInteger = Integer.TYPE;

        Class<Boolean> resultBoolean = Primitives.primitiveTypeOf(booleanType);
        Class<Integer> resultInteger = Primitives.primitiveTypeOf(integerType);

        assertEquals(expectedBoolean, resultBoolean);
        assertEquals(expectedInteger, resultInteger);
    }
}