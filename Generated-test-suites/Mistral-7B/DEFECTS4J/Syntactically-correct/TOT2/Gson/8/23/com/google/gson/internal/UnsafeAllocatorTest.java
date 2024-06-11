import org.junit.Test;
import static org.junit.Assert.*;

public class UnsafeAllocatorTest {

  private UnsafeAllocator allocator;

  @Before
  public void setUp() {
    allocator = UnsafeAllocator.create();
  }

  // Test case 1: Basic functionality
  @Test
  public void testNewInstance_BasicFunctionality() {
    String result = allocator.newInstance(String.class);
    assertNotNull(result);
  }

  // Test case 2: Edge case - null class
  @Test(expected = NullPointerException.class)
  public void testNewInstance_NullClass() {
    allocator.newInstance(null);
  }

  // Test case 3: Edge case - Unsupported class
  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstance_UnsupportedClass() {
    allocator.newInstance(UnsupportedOperationException.class);
  }

  // Test case 4: Edge case - Primitive types
  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstance_PrimitiveType() {
    allocator.newInstance(int.class);
  }

  // Test case 5: Edge case - Array types
  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstance_ArrayType() {
    allocator.newInstance(int[].class);
  }

  // Test case 6: Error scenario - Exception while allocating object
  @Test(expected = Exception.class)
  public void testNewInstance_Exception() {
    allocator = new UnsafeAllocator() {
      @Override
      public <T> T newInstance(Class<T> c) throws Exception {
        throw new Exception("Test exception");
      }
    };
    allocator.newInstance(String.class);
  }

  // Test case 7: Edge case - Interface types
  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstance_InterfaceType() {
    allocator.newInstance(Runnable.class);
  }

  // Test case 8: Edge case - Abstract classes
  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstance_AbstractClass() {
    allocator.newInstance(AbstractClass.class);
  }

}

class AbstractClass {
  // abstract class
}