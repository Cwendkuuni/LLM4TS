import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import com.google.gson.internal.UnsafeAllocator;
import com.google.gson.internal.UnsafeAllocator.UnsafeAllocatorImpl;
import java.lang.reflect.InvocationTargetException;

@RunWith(JUnit4.class)
public class UnsafeAllocatorTest {

  private UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();

  @Test
  public void testNewInstance_NormalCase() {
    try {
      Class<String> stringClass = String.class;
      String instance = unsafeAllocator.newInstance(stringClass);
      assertNotNull(instance);
      assertEquals("", instance);
    } catch (Exception e) {
      fail("Failed to allocate instance of String class: " + e.getMessage());
    }
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstance_InterfaceClass() {
    try {
      Class<Comparable<Integer>> comparableClass = Comparable.class.asSubclass(Integer.class);
      unsafeAllocator.newInstance(comparableClass);
      fail("Expected UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      // Expected
    }
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstance_AbstractClass() {
    try {
      Class<AbstractClass> abstractClass = AbstractClass.class;
      unsafeAllocator.newInstance(abstractClass);
      fail("Expected UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      // Expected
    }
  }

  @Test
  public void testNewInstance_ExceptionThrown() {
    try {
      Class<RuntimeException> runtimeExceptionClass = RuntimeException.class;
      unsafeAllocator = new UnsafeAllocatorImpl() {
        @Override
        public <T> T newInstance(Class<T> c) throws Exception {
          throw new InstantiationException("Cannot instantiate " + c);
        }
      };
      expectThrows(InstantiationException.class, () -> unsafeAllocator.newInstance(runtimeExceptionClass));
    } catch (InstantiationException e) {
      // Expected
    }
  }

  private static void expectThrows(Class<? extends Throwable> expectedExceptionType, Runnable runnable) {
    try {
      runnable.run();
      fail("Expected " + expectedExceptionType.getName() + " to be thrown");
    } catch (Throwable e) {
      if (expectedExceptionType.isInstance(e)) {
        // Expected
      } else {
        fail("Unexpected exception: " + e.getClass().getName());
      }
    }
  }

  private static void assertNotNull(Object object) {
    if (object == null) {
      fail("Expected object to not be null");
    }
  }

  private static void assertEquals(String expectedValue, Object object) {
    String actualValue = object == null ? null : object.toString();
    if (!expectedValue.equals(actualValue)) {
      fail("Expected: <" + expectedValue + ">, but was: <" + actualValue + ">");
    }
  }

  static class AbstractClass {
  }

  static interface Comparable<T> {
  }
}