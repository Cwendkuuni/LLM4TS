import org.junit.Test;
import org.junit.Assert.*;
import com.google.gson.internal.UnsafeAllocator;

public class UnsafeAllocatorTest {

  @Test
  public void testNewInstance_Object() {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    Object obj = null;
    try {
      obj = allocator.newInstance(Object.class);
      assertNotNull(obj);
    } catch (Exception e) {
      e.printStackTrace();
      fail("Failed to allocate instance of Object using UnsafeAllocator");
    }
  }

  @Test
  public void testNewInstance_String() {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    String str = null;
    try {
      str = (String) allocator.newInstance(String.class);
      assertNotNull(str);
      str.intern(); // test string interning
      assertEquals("", str);
    } catch (Exception e) {
      e.printStackTrace();
      fail("Failed to allocate instance of String using UnsafeAllocator");
    }
  }

  @Test
  public void testNewInstance_Array() {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    int[] arr = null;
    try {
      arr = (int[]) allocator.newInstance(int[].class);
      assertNotNull(arr);
      arr[0] = 42;
      assertEquals(42, arr[0]);
    } catch (Exception e) {
      e.printStackTrace();
      fail("Failed to allocate instance of int[] using UnsafeAllocator");
    }
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstance_Interface() {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    Runnable runnable = null;
    try {
      runnable = (Runnable) allocator.newInstance(Runnable.class);
      fail("Expected UnsupportedOperationException when trying to allocate interface instance");
    } catch (Exception e) {
      // expected
    }
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstance_AbstractClass() {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    AbstractClass abstractClass = null;
    try {
      abstractClass = (AbstractClass) allocator.newInstance(AbstractClass.class);
      fail("Expected UnsupportedOperationException when trying to allocate abstract class instance");
    } catch (Exception e) {
      // expected
    }
  }
}