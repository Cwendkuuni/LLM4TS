import com.google.gson.internal.UnsafeAllocator;
import org.junit.Test;
import org.junit.Assert;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

public class UnsafeAllocatorTest {

  private UnsafeAllocator allocator = UnsafeAllocator.create();

  @Test
  public void testNewInstanceWithObjectClass() {
    Object instance = allocator.newInstance(Object.class);
    Assert.assertNotNull(instance);
  }

  @Test
  public void testNewInstanceWithCustomClass() {
    String className = "java.lang.String";
    try {
      Class<?> customClass = Class.forName(className);
      Object instance = allocator.newInstance(customClass);
      Assert.assertNotNull(instance);
      Assert.assertEquals("", (String) instance);
    } catch (ClassNotFoundException e) {
      Assert.fail("Failed to load class: " + e.getMessage());
    }
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstanceWithInterfaceClass() {
    Class<?> interfaceClass = String.class.getInterfaces()[0];
    allocator.newInstance(interfaceClass);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstanceWithAbstractClass() {
    Class<?> abstractClass = AbstractClassTest.class;
    allocator.newInstance(abstractClass);
  }

  @Test
  public void testNewInstanceWithExceptionClass() {
    try {
      Class<?> exceptionClass = Exception.class;
      allocator.newInstance(exceptionClass);
      Assert.fail("Expected UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      // Expected
    }
  }

  @Test(expected = IllegalAccessException.class)
  public void testNewInstanceWithPrivateClass() {
    Class<?> privateClass = PrivateClassTest.class;
    allocator.newInstance(privateClass);
  }

  @Test
  public void testNewInstanceWithNullClass() {
    allocator.newInstance(null);
  }

  @Test
  public void testNewInstanceWithArrayClass() {
    Class<?> arrayClass = int[].class;
    try {
      Object instance = allocator.newInstance(arrayClass);
      Assert.assertArrayEquals(new int[0], (int[]) instance);
    } catch (Exception e) {
      Assert.fail("Unexpected exception: " + e.getMessage());
    }
  }

  @Test
  public void testNewInstanceWithGenericArrayClass() {
    Class<?> genericArrayClass = Number[].class;
    try {
      Object instance = allocator.newInstance(genericArrayClass);
      Assert.assertArrayEquals(new Number[0], (Number[]) instance);
    } catch (Exception e) {
      Assert.fail("Unexpected exception: " + e.getMessage());
    }
  }

  static class AbstractClassTest {
  }

  static class PrivateClassTest {
    private PrivateClassTest() {
    }
  }

}