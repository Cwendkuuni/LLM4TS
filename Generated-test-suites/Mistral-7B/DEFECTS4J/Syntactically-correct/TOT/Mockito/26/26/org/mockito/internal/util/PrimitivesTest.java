import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.internal.util.Primitives;

public class PrimitivesTest {
    @Test
    public void testPrimitiveTypeOf() {
        Class<Integer> integerClass = Integer.class;
        Class<Integer> expectedIntegerType = Integer.TYPE;
        Class<Integer> actualIntegerType = Primitives.primitiveTypeOf(integerClass);
        assertSame(expectedIntegerType, actualIntegerType);
    }

    @Test
    public void testIsPrimitiveWrapperWithBoolean() {
        Class<Boolean> booleanClass = Boolean.class;
        boolean expectedResult = true;
        boolean actualResult = Primitives.isPrimitiveWrapper(booleanClass);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testIsPrimitiveWrapperWithInteger() {
        Class<Integer> integerClass = Integer.class;
        boolean expectedResult = false;
        boolean actualResult = Primitives.isPrimitiveWrapper(integerClass);
        assertEquals(expectedResult, actualResult);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsPrimitiveWrapperWithString() {
        Class<String> stringClass = String.class;
        Primitives.isPrimitiveWrapper(stringClass);
    }

    @Test
    public void testPrimitiveWrapperOfWithBoolean() {
        Class<Boolean> booleanClass = Boolean.class;
        Boolean expectedValue = Boolean.FALSE;
        Boolean actualValue = Primitives.primitiveWrapperOf(booleanClass);
        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testPrimitiveWrapperOfWithInteger() {
        Class<Integer> integerClass = Integer.class;
        Integer expectedValue = 0;
        Integer actualValue = Primitives.primitiveWrapperOf(integerClass);
        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testPrimitiveValueOrNullForWithBoolean() {
        Class<Boolean> booleanClass = Boolean.class;
        Boolean expectedValue = false;
        Boolean actualValue = Primitives.primitiveValueOrNullFor(booleanClass);
        assertSame(expectedValue, actualValue);
    }

    @Test
    public void testPrimitiveValueOrNullForWithInteger() {
        Class<Integer> integerClass = Integer.class;
        Integer expectedValue = 0;
        Integer actualValue = Primitives.primitiveValueOrNullFor(integerClass);
        assertSame(expectedValue, actualValue);
    }

    @Test
    public void testPrimitiveValueOrNullForWithNull() {
        Class<?> nullClass = null;
        Object expectedValue = null;
        Object actualValue = Primitives.primitiveValueOrNullFor(nullClass);
        assertSame(expectedValue, actualValue);
    }

    @Test
    public void testStaticInitialization() {
        Map<Class<?>, Class<?>> primitiveTypes = Primitives.primitiveTypes;
        Map<Class<?>, Object> wrapperReturnValues = Primitives.wrapperReturnValues;
        Map<Class<?>, Object> primitiveValues = Primitives.primitiveValues;

        for (Map.Entry<Class<?>, Class<?>> entry : primitiveTypes.entrySet()) {
            Class<?> key = entry.getKey();
            Class<?> value = entry.getValue();
            assertEquals(value, Primitives.primitiveTypeOf(key));
        }

        for (Map.Entry<Class<?>, Object> entry : wrapperReturnValues.entrySet()) {
            Class<?> key = entry.getKey();
            Object value = entry.getValue();
            assertEquals(value, Primitives.primitiveWrapperOf(key).booleanValue());
        }

        for (Map.Entry<Class<?>, Object> entry : primitiveValues.entrySet()) {
            Class<?> key = entry.getKey();
            Object value = entry.getValue();
            assertEquals(value, Primitives.primitiveValueOrNullFor(key));
        }
    }
}