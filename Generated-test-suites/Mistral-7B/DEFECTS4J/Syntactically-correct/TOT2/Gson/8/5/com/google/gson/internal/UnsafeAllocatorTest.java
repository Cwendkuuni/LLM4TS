import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import com.google.gson.internal.UnsafeAllocator;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(JUnit4.class)
public class UnsafeAllocatorTest {

  @Test
  public void testNewInstance() {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    String expected = "Hello World";
    String result = allocator.newInstance(String.class);
    assertEquals(expected, result);
  }

  // Test for edge case: Null Class Argument
  @Test(expected = NullPointerException.class)
  public void testNewInstanceWithNullClassArgument() {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    allocator.newInstance(null);
  }

  // Test for edge case: Class Argument not instantiable
  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstanceWithUninstantiableClass() {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    final Class<UnsafeAllocator> unsafeAllocatorClass = UnsafeAllocator.class;
    allocator = allocator.newInstance(unsafeAllocatorClass);
    allocator.newInstance(UnsafeAllocator.class);
  }

  // Test for exception: Reflection Exceptions (IllegalAccessException, InvocationTargetException)
  @Test(expected = Exception.class)
  public void testNewInstanceWithReflectionExceptions() {
    UnsafeAllocator allocator = UnsafeAllocator.create();
    Class<UnsafeAllocator> unsafeAllocatorClass = UnsafeAllocator.class;
    ReflectiveOperationException reflectiveOperationException = new ReflectiveOperationException("Reflection Exception");
    doThrow(reflectiveOperationException)
        .when(allocator)
        .newInstance(eq(unsafeAllocatorClass));
    try {
      allocator.newInstance(unsafeAllocatorClass);
    } catch (Exception e) {
      assertEquals(reflectiveOperationException, e);
    }
  }
}
//