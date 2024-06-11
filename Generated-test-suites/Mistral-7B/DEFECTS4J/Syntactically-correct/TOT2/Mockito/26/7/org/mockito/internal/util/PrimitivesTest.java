import org.junit.Test;
import org.junit.Assert.*;

public class PrimitivesTest {

    @Test
    public void testPrimitiveTypeOf() {
        Class<Boolean> booleanClass = Boolean.class;
        Class<Integer> integerClass = Integer.class;

        Class<Boolean> expectedBooleanType = Boolean.TYPE;
        Class<Integer> expectedIntegerType = Integer.TYPE;

        Class<Boolean> actualBooleanType = Primitives.primitiveTypeOf(booleanClass);
        Class<Integer> actualIntegerType = Primitives.primitiveTypeOf(integerClass);

        assertEquals(expectedBooleanType, actualBooleanType);
        assertEquals(expectedIntegerType, actualIntegerType);
    }
}