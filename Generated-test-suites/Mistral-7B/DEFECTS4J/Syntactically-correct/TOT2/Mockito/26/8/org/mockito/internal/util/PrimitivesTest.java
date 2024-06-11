
import org.junit.Test;
import org.junit.Assert.*;

public class PrimitivesTest {

    @Test
    public void testPrimitiveTypeOf() {
        // Test case from expert 1
    }

    @Test
    public void testIsPrimitiveWrapper() {
        // Test case from expert 1
    }

    @Test
    public void testPrimitiveWrapperOf() {
        // Test case from expert 1
    }

    @Test
    public void testPrimitiveValueOrNullFor() {
        // Test case from expert 2
    }

    @Test(expected = NullPointerException.class)
    public void testNullInputForPrimitiveTypeOf() {
        Class<?> nullClass = null;
        try {
            Primitives.primitiveTypeOf(nullClass);
        } catch (NullPointerException e) {
            // Expected null input test case
        }
    }

    @Test(expected = ClassCastException.class)
    public void testNonPrimitiveTypeForPrimitiveTypeOf() {
        Class<String> stringType = String.class;
        try {
            Primitives.primitiveTypeOf(stringType);
        } catch (ClassCastException e) {
            // Test for non-primitive type as input
        }
    }

    @Test(expected = ClassCastException.class)
    public void testCustomWrapperClassForPrimitiveTypeOf() {
        Class<MyBooleanWrapper> customBooleanWrapper = MyBooleanWrapper.class;
        try {
            Primitives.primitiveTypeOf(customBooleanWrapper);
        } catch (ClassCastException e) {
            // Test for custom wrapper class as input
        }
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testOutOfBoundsIntegerForPrimitiveValueOrNullFor() {
        Class<Integer> intType = Integer.TYPE;
        try {
            Primitives.primitiveValueOrNullFor((Class<?>) (intType + 1));
        } catch (ArrayIndexOutOfBoundsException e) {
            // Test for out-of-bounds primitive type as input
        }
    }

    @Test
    public void testEmptyMappings() {
        Primitives emptyPrimitives = new Primitives() {
            @Override
            private static Map<Class<?>, Class<?>> wrapperTypes = new HashMap<>();
            @Override
            private static Map<Class<?>, Class<?>> primitiveTypes = new HashMap<>();
            @Override
            private static Map<Class<?>, Object> wrapperReturnValues = new HashMap<>();
            @Override
            private static Map<Class<?>, Object> primitiveValues = new HashMap<>();
        };

        Class<Integer> intType = Integer.TYPE;
        Class<Integer> intWrapperType = Integer.class;

        try {
            emptyPrimitives.primitiveTypeOf(intType);
            fail("Expected IllegalArgumentException for empty mappings");
        } catch (IllegalArgumentException e) {
            // Test for empty mappings
        }

        try {
            emptyPrimitives.isPrimitiveWrapper(intWrapperType);
            fail("Expected IllegalArgumentException for empty mappings");
        } catch (IllegalArgumentException e) {
            // Test for empty mappings
        }

        try {
            emptyPrimitives.primitiveWrapperOf(intWrapperType);
            fail("Expected NullPointerException for empty mappings");
        } catch (NullPointerException e) {
            // Test for empty mappings
        }

        try {
            emptyPrimitives.primitiveValueOrNullFor(intType);
            fail("Expected NullPointerException for empty mappings");
        } catch (NullPointerException e) {
            // Test for empty mappings
        }
    }
}

class MyBooleanWrapper {
    boolean value;
}
