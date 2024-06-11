import static org.junit.Assert.*;
import org.junit.Test;

public class PrimitivesTest {

  @Test
  public void testPrimitiveTypeOf() {
    Class<Integer> intClass = Integer.class;
    Class<Integer> expectedIntType = Integer.TYPE;
    Class<String> stringClass = String.class;
    Class<String> expectedStringType = String.class;

    assertEquals(expectedIntType, Primitives.primitiveTypeOf(intClass));
    assertEquals(expectedStringType, Primitives.primitiveTypeOf(stringClass));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPrimitiveTypeOfNullInput() {
    Class<?> nullClass = null;
    assertNull(Primitives.primitiveTypeOf(nullClass));
  }

  @Test
  public void testPrimitiveTypeOfInterface() {
    Class<Comparable<Integer>> comparableIntInterface = Comparable.class.cast(Integer.TYPE.getInterfaces()[0]);
    assertNull(Primitives.primitiveTypeOf(comparableIntInterface));
  }

  @Test
  public void testPrimitiveTypeOfArray() {
    int[] intArray = new int[5];
    assertNull(Primitives.primitiveTypeOf(int[].class));
  }
}