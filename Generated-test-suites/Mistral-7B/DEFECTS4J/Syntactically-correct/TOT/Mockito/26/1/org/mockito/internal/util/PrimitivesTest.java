
package org.junit.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.util.Primitives;

public class PrimitivesTest {

    @Test
    public void testPrimitiveTypeOf() {
        Expert1.testPrimitiveTypeOf();
    }

    @Test
    public void testIsPrimitiveWrapper() {
        Expert2.testIsPrimitiveWrapper();
    }

    @Test
    public void testPrimitiveWrapperOf() {
        Expert3.testPrimitiveWrapperOf();
        Expert3.testPrimitiveValueOrNullFor();
    }
}
