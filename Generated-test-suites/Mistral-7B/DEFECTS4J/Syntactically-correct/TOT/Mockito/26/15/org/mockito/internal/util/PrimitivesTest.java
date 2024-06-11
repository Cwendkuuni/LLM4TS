// PrimitivesTest.java
package org.junit.platform.commons.util;

import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.util.Primitives;

public class PrimitivesTest {
    @Test
    public void testPrimitiveTypeOf() {
        Class<Integer> intClass = Integer.class;
        Class<Integer> expectedIntType = Integer.TYPE;
        Class<Integer> actualIntType = Primitives.primitiveTypeOf(intClass);
        assertEquals(expectedIntType, actualIntType);

        Class<String> stringClass = String.class;
        Class<?> actualStringType = Primitives.primitiveTypeOf(stringClass);
        assertNull(actualStringType);
    }

    @Test
    public void testIsPrimitiveWrapper() {
        Class<Boolean> booleanWrapperClass = Boolean.class;
        boolean expectedResult = true;
        boolean actualResult = Primitives.isPrimitiveWrapper(booleanWrapperClass);
        assertEquals(expectedResult, actualResult);

        Class<String> stringClass = String.class;
        actualResult = Primitives.isPrimitiveWrapper(stringClass);
        expectedResult = false;
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testPrimitiveWrapperOf() {
        Class<Boolean> booleanClass = Boolean.class;
        Boolean expectedBoolean = false;
        Boolean actualBoolean = Primitives.primitiveWrapperOf(booleanClass);
        assertEquals(expectedBoolean, actualBoolean);

        Class<String> stringClass = String.class;
        assertNull(Primitives.primitiveWrapperOf(stringClass));
    }

    @Test
    public void testPrimitiveValueOrNullFor() {
        Class<Boolean> booleanClass = Boolean.class;
        Boolean expectedBoolean = false;
        Boolean actualBoolean = Primitives.primitiveValueOrNullFor(booleanClass);
        assertEquals(expectedBoolean, actualBoolean);

        Class<Integer> intClass = Integer.class;
        assertNull(Primitives.primitiveValueOrNullFor(intClass));
    }

    @Test
    public void testMisconfiguredPrimitiveTypesMap() {
        // Replace the contents of the primitiveTypes map with incorrect values
        Map<Class<?>, Class<?>> primitiveTypes = new HashMap<>();
        primitiveTypes.put(Boolean.class, Integer.TYPE);

        Primitives primitives = new Primitives();
        Class<Boolean> booleanType = Boolean.TYPE;
        Class<Boolean> booleanWrapper = Boolean.class;

        // test primitiveTypeOf() with misconfigured primitiveTypes map
        Class<Boolean> actualBooleanType = primitives.primitiveTypeOf(booleanWrapper);
        assertNotEquals(booleanType, actualBooleanType);

        // test isPrimitiveWrapper() with misconfigured primitiveTypes map
        boolean actualIsWrapper = primitives.isPrimitiveWrapper(booleanWrapper);
        assertFalse(actualIsWrapper);
    }

    @Test
    public void testMisconfiguredWrapperReturnValuesMap() {
        // Replace the contents of the wrapperReturnValues map with incorrect values
        Map<Class<?>, Object> wrapperReturnValues = new HashMap<>();
        wrapperReturnValues.put(Boolean.class, 123);

        Primitives primitives = new Primitives();
        Class<Boolean> booleanType = Boolean.TYPE;

        // test primitiveWrapperOf() with misconfigured wrapperReturnValues map
        Object actualBoolean = primitives.primitiveWrapperOf(booleanType);
        assertNotEquals(expectedBoolean, actualBoolean);

        // test primitiveValueOrNullFor() with misconfigured wrapperReturnValues map
        Object actualNull = primitives.primitiveValueOrNullFor(booleanType);
        assertNull(actualNull);
    }
}