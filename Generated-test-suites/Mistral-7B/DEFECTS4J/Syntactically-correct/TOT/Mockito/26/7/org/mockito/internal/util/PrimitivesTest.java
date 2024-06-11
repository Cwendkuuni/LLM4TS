// PrimitivesTest.java - Test for primitiveTypeOf method

import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.util.Primitives;

public class PrimitivesTest {
   @Test
   public void testPrimitiveTypeOf() {
      Class<Integer> intClass = Integer.class;
      Class<Integer> intType = Primitives.primitiveTypeOf(intClass);
      assertTrue(Integer.TYPE.isAssignableFrom(intType));

      Class<String> stringClass = String.class;
      Class<String> stringType = Primitives.primitiveTypeOf(stringClass);
      assertNull(stringType);
   }
}