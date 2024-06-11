import org.junit.Test;
import static org.junit.Assert.*;

public class PrimitivesTest {

    @Test
    public void testPrimitiveTypeOf() {
        Class<Integer> intClass = Integer.class;
        Class<Integer> expected = Integer.TYPE;
        Class<Integer> actual = Primitives.primitiveTypeOf(intClass);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPrimitiveTypeOfWithNonPrimitiveClass() {
        Class<String> stringClass = String.class;
        Primitives.primitiveTypeOf(stringClass);
    }

    @Test
    public void testIsPrimitiveWrapper() {
        Class<Boolean> booleanWrapperClass = Boolean.class;
        boolean expected = true;
        boolean actual = Primitives.isPrimitiveWrapper(booleanWrapperClass);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsPrimitiveWrapperForNonWrapperClass() {
        Class<Integer> integerClass = Integer.class;
        boolean expected = false;
        boolean actual = Primitives.isPrimitiveWrapper(integerClass);
        assertEquals(expected, actual);
    }

    @Test
    public void testPrimitiveWrapperOf() {
        Class<Boolean> booleanClass = Boolean.class;
        Boolean expected = false;
        Boolean actual = Primitives.primitiveWrapperOf(booleanClass);
        assertEquals(expected, actual);
    }

    @Test
    public void testPrimitiveWrapperOfForNull() {
        assertNull(Primitives.primitiveWrapperOf(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPrimitiveWrapperOfForNonPrimitiveClass() {
        Class<Integer> integerClass = Integer.class;
        Primitives.primitiveWrapperOf(integerClass);
    }

    @Test
    public void testEdgeCaseForWrapperTypes() {
        Class<Byte> byteClass = Byte.class;
        Byte maxByteValue = Byte.MAX_VALUE;
        Byte expected = maxByteValue;
        Byte actual = (Byte) Primitives.primitiveWrapperOf(byteClass);
        assertEquals(expected, actual);
    }

    @Test
    public void testEdgeCaseForPrimitiveTypes() {
        Class<Double> doubleClass = Double.class;
        Double maxDoubleValue = Double.MAX_VALUE;
        Double expected = maxDoubleValue;
        Double actual = Primitives.primitiveValueOrNullFor(doubleClass);
        assertEquals(expected, actual);
    }
}
//