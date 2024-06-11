import org.junit.Test;
import org.junit.Assert.*;

public class PrimitivesTest {

    @Test
    public void testPrimitiveTypeOf() {
        Class<Integer> intClass = Integer.class;
        Class<Integer> expectedIntType = Integer.TYPE;
        Class<String> stringClass = String.class;
        Class<?> actualIntType = Primitives.primitiveTypeOf(intClass);
        Assert.assertEquals(expectedIntType, actualIntType);
        Assert.assertNotEquals(String.class, actualIntType);
        Class<?> actualStringType = Primitives.primitiveTypeOf(stringClass);
        Assert.assertNull(actualStringType);
    }

    @Test
    public void testIsPrimitiveWrapper_BooleanWrapper() {
        Class<Boolean> booleanWrapperClass = Boolean.class;
        boolean expectedResult = true;
        boolean actualResult = Primitives.isPrimitiveWrapper(booleanWrapperClass);
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testIsPrimitiveWrapper_StringClass() {
        Class<String> stringClass = String.class;
        boolean expectedResult = false;
        boolean actualResult = Primitives.isPrimitiveWrapper(stringClass);
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testPrimitiveWrapperOf_Boolean() {
        Class<Boolean> booleanClass = Boolean.class;
        Boolean expectedResult = false;
        Boolean actualResult = Primitives.primitiveWrapperOf(booleanClass);
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testPrimitiveWrapperOf_Integer() {
        Class<Integer> integerClass = Integer.class;
        Integer expectedResult = 0;
        Integer actualResult = Primitives.primitiveWrapperOf(integerClass);
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test(expected = NullPointerException.class)
    public void testPrimitiveTypeOf_EmptyClass() {
        Class<?> emptyClass = null;
        Class<?> actualClassType = Primitives.primitiveTypeOf(emptyClass);
        Assert.fail("Expected NullPointerException, but got: " + actualClassType);
    }

    @Test
    public void testIsPrimitiveWrapper_NonPrimitiveClass() {
        Class<String> stringClass = String.class;
        boolean expectedResult = false;
        boolean actualResult = Primitives.isPrimitiveWrapper(stringClass);
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testWrapperTypes() {
        Map<Class<?>, Class<?>> expectedWrapperTypes = new HashMap<>();
        expectedWrapperTypes.put(Boolean.class, Boolean.TYPE);
        expectedWrapperTypes.put(Character.class, Character.TYPE);
        expectedWrapperTypes.put(Byte.class, Byte.TYPE);
        expectedWrapperTypes.put(Short.class, Short.TYPE);
        expectedWrapperTypes.put(Integer.class, Integer.TYPE);
        expectedWrapperTypes.put(Long.class, Long.TYPE);
        expectedWrapperTypes.put(Float.class, Float.TYPE);
        expectedWrapperTypes.put(Double.class, Double.TYPE);

        Map<Class<?>, Class<?>> actualWrapperTypes = Primitives.wrapperTypes;
        Assert.assertEquals(expectedWrapperTypes, actualWrapperTypes);
    }
}