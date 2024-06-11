import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import com.google.gson.internal.UnsafeAllocator;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

@RunWith(JUnit4.class)
public class UnsafeAllocatorTest {

  private UnsafeAllocator allocator = UnsafeAllocator.create();

  @Test
  public void testNewInstance() {
    try {
      Object obj = allocator.newInstance(String.class);
      assertNotNull(obj);
      assertEquals("", (String) obj);
    } catch (Exception e) {
      fail("Failed to allocate instance of String");
    }

    try {
      Object obj = allocator.newInstance(Integer.class);
      assertNotNull(obj);
      assertEquals(0, (Integer) obj);
    } catch (Exception e) {
      fail("Failed to allocate instance of Integer");
    }

    try {
      Object obj = allocator.newInstance(UnsafeAllocator.class);
      assertNotNull(obj);
      fail("Should not be able to allocate instance of UnsafeAllocator");
    } catch (Exception e) {
      assertTrue(e instanceof UnsupportedOperationException);
    }

    try {
      Object obj = allocator.newInstance(AbstractClass.class);
      fail("Should not be able to allocate instance of abstract class");
    } catch (Exception e) {
      assertTrue(e instanceof UnsupportedOperationException);
    }
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstanceUnsupported() {
    Object obj = allocator.newInstance(UnsupportedClass.class);
  }

  static class AbstractClass {
    // abstract class
  }

  static class UnsupportedClass implements Runnable {
    @Override
    public void run() {
      // do something unsupported
    }
  }
}