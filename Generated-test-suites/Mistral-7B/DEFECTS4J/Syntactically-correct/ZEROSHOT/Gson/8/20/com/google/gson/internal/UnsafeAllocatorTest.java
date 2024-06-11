import com.google.gson.internal.UnsafeAllocator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;

@RunWith(JUnit4.class)
public class UnsafeAllocatorTest {

  private UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();

  @Test
  public void testNewInstanceWithObjectClass() throws Exception {
    Object instance = unsafeAllocator.newInstance(Object.class);
    assertInstanceOf(Object.class, instance);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstanceWithInterfaceClass() throws Exception {
    unsafeAllocator.newInstance(Runnable.class);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstanceWithAbstractClass() throws Exception {
    unsafeAllocator.newInstance(AbstractClassTest.class);
  }

  @Test
  public void testNewInstanceWithCustomClass() throws Exception {
    Class<CustomClassTest> customClass = CustomClassTest.class;
    CustomClassTest instance = (CustomClassTest) unsafeAllocator.newInstance(customClass);
    assertInstanceOf(CustomClassTest.class, instance);
  }

  @Test(expected = NullPointerException.class)
  public void testNewInstanceWithNullClass() throws Exception {
    unsafeAllocator.newInstance(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNewInstanceWithNonPublicClass() throws Exception {
    Class<?> nonPublicClass = Class.forName("com.google.gson.internal.PrivateClassTest");
    unsafeAllocator.newInstance(nonPublicClass);
  }

  @Test
  public void testNewInstanceWithArrayClass() throws Exception {
    int[] intArray = (int[]) unsafeAllocator.newInstance(int[].class);
    assertNull(intArray);
  }

  @Test
  public void testNewInstanceWithPrimitiveClass() throws Exception {
    byte byteValue = (byte) unsafeAllocator.newInstance(Byte.TYPE);
    assertInstanceOf(Byte.class, Byte.TYPE);
    assertInstanceOf(Byte.class, byteValue);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNewInstanceWithArrayWithElementTypeNull() throws Exception {
    unsafeAllocator.newInstance(int[].class);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNewInstanceWithArrayWithElementTypeNotSupport() throws Exception {
    unsafeAllocator.newInstance(String[].class);
  }

  @Test(expected = InstantiationException.class)
  public void testNewInstanceWithThrowingInstantiationExceptionClass() throws Exception {
    unsafeAllocator.newInstance(ThrowingInstantiationExceptionTest.class);
  }

  @Test
  public void testNewInstanceWithThrowingExceptionClass() throws Exception {
    ThrowingExceptionTest throwingExceptionTest = (ThrowingExceptionTest) unsafeAllocator.newInstance(ThrowingExceptionTest.class);
    assertInstanceOf(ThrowingExceptionTest.class, throwingExceptionTest);
  }

  @Test
  public void testNewInstanceWithThrowingRuntimeExceptionClass() throws Exception {
    ThrowingRuntimeExceptionTest throwingRuntimeExceptionTest = (ThrowingRuntimeExceptionTest) unsafeAllocator.newInstance(ThrowingRuntimeExceptionTest.class);
    assertInstanceOf(ThrowingRuntimeExceptionTest.class, throwingRuntimeExceptionTest);
  }

  @Test
  public void testNewInstanceWithThrowingCheckedExceptionClass() throws Exception {
    ThrowingCheckedExceptionTest throwingCheckedExceptionTest = (ThrowingCheckedExceptionTest) unsafeAllocator.newInstance(ThrowingCheckedExceptionTest.class);
    assertInstanceOf(ThrowingCheckedExceptionTest.class, throwingCheckedExceptionTest);
  }

  @Test(expected = InvocationTargetException.class)
  public void testNewInstanceWithClassThrowingException() throws Exception {
    ThrowingClassExceptionTest throwingClassExceptionTest = (ThrowingClassExceptionTest) unsafeAllocator.newInstance(ThrowingClassExceptionTest.class);
    assertInstanceOf(ThrowingClassExceptionTest.class, throwingClassExceptionTest);

    Throwable cause = throwingClassExceptionTest.getCause();
    assertInstanceOf(RuntimeException.class, cause);
  }

  @Test(expected = UndeclaredThrowableException.class)
  public void testNewInstanceWithClassHidingException() throws Exception {
    ThrowingClassHidingExceptionTest throwingClassHidingExceptionTest = (ThrowingClassHidingExceptionTest) unsafeAllocator.newInstance(ThrowingClassHidingExceptionTest.class);
    assertInstanceOf(ThrowingClassHidingExceptionTest.class, throwingClassHidingExceptionTest);

    Throwable cause = throwingClassHidingExceptionTest.getCause();
    assertInstanceOf(RuntimeException.class, cause);
  }

  static class CustomClassTest {
    // empty class
  }

  static class AbstractClassTest {
    public abstract void test();
  }

  static class PrivateClassTest {
    // private class
  }

  static class ThrowingInstantiationExceptionTest {
    private ThrowingInstantiationExceptionTest() throws InstantiationException {
      throw new InstantiationException();
    }
  }

  static class ThrowingExceptionTest {
    private ThrowingExceptionTest() throws Exception {
      throw new Exception();
    }
  }

  static class ThrowingRuntimeExceptionTest {
    private ThrowingRuntimeExceptionTest() throws RuntimeException {
      throw new RuntimeException();
    }
  }

  static class ThrowingCheckedExceptionTest extends Exception {
    public ThrowingCheckedExceptionTest() throws Exception {
      super();
    }
  }

  static class ThrowingClassExceptionTest extends Exception {
    public ThrowingClassExceptionTest() throws ClassNotFoundException {
      super("ClassNotFoundException");
    }
  }

  static class ThrowingClassHidingExceptionTest extends RuntimeException {
    public ThrowingClassHidingExceptionTest(Throwable cause) {
      super("RuntimeException", cause);
    }
  }
}