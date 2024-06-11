import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.util.Primitives;

public class PrimitivesTest {
    @Test
    public void testPrimitiveTypeOf() {
        Class<Integer> intClass = Integer.class;
        Class<Integer> intType = Primitives.primitiveTypeOf(intClass);
        Assert.assertEquals(Integer.TYPE, intType);

        Class<String> stringClass = String.class;
        Class<String> stringType = Primitives.primitiveTypeOf(stringClass);
        Assert.assertNull(stringType);

        Class<Double> doubleClass = Double.class;
        Class<Double> doubleType = Primitives.primitiveTypeOf(doubleClass);
        Assert.assertEquals(Double.TYPE, doubleType);
    }

    @Test
    public void testIsPrimitiveWrapper() {
        Assert.assertTrue(Primitives.isPrimitiveWrapper(Boolean.class));
        Assert.assertFalse(Primitives.isPrimitiveWrapper(String.class));
    }

    @Test
    public void testPrimitiveWrapperOf() {
        Boolean booleanWrapper = Primitives.primitiveWrapperOf(Boolean.class);
        Assert.assertFalse(booleanWrapper);

        Byte byteWrapper = Primitives.primitiveWrapperOf(Byte.class);
        Assert.assertEquals((byte) 0, byteWrapper.byteValue());
    }

    @Test
    public void testPrimitiveValueOrNullFor() {
        Boolean booleanValue = Primitives.primitiveValueOrNullFor(Boolean.class);
        Assert.assertNull(booleanValue);

        Byte byteValue = Primitives.primitiveValueOrNullFor(Byte.class);
        Assert.assertEquals((byte) 0, byteValue);

        Short shortValue = Primitives.primitiveValueOrNullFor(Short.class);
        Assert.assertEquals((short) 0, shortValue);

        Integer intValue = Primitives.primitiveValueOrNullFor(Integer.class);
        Assert.assertEquals(0, intValue.intValue());

        Long longValue = Primitives.primitiveValueOrNullFor(Long.class);
        Assert.assertEquals(0L, longValue);

        Float floatValue = Primitives.primitiveValueOrNullFor(Float.class);
        Assert.assertEquals(0F, floatValue);

        Double doubleValue = Primitives.primitiveValueOrNullFor(Double.class);
        Assert.assertEquals(0D, doubleValue);
    }

    @Test
    public void testMismatchedTypes() {
        Assert.assertNull(Primitives.primitiveTypeOf(String.class));
        Assert.assertNull(Primitives.isPrimitiveWrapper(String.class));
        Assert.assertNull(Primitives.primitiveWrapperOf(String.class));
        Assert.assertNull(Primitives.primitiveValueOrNullFor(String.class));
    }
}