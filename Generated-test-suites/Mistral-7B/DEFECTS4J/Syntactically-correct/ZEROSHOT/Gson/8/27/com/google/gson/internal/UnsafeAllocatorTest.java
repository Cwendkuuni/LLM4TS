import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import com.google.gson.internal.UnsafeAllocator;
import com.google.gson.internal.UnsafeAllocator.UnsafeAllocatorImpl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;

@RunWith(JUnit4.class)
public class UnsafeAllocatorTest {

  private UnsafeAllocator allocator = UnsafeAllocator.create();

  @Test
  public void testNewInstanceObject() throws Exception {
    Object obj = allocator.newInstance(Object.class);
    assertInstanceOf(Object.class, obj);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstanceInterface() throws Exception {
    allocator.newInstance(String.class);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstanceAbstractClass() throws Exception {
    allocator.newInstance(AbstractClass.class);
  }

  @Test
  public void testNewInstanceNull() throws Exception {
    assertNull(allocator.newInstance(null));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstanceArray() throws Exception {
    allocator.newInstance(int[].class);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstanceGenericArray() throws Exception {
    allocator.newInstance(int[][].class);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstanceWithArguments() throws Exception {
    allocator.newInstance(TwoArgumentConstructor.class);
  }

  @Test
  public void testNewInstanceInnerClass() throws Exception {
    Class<?> outerClass = InnerClass.class;
    Class<?> innerClass = InnerClass.InnerClass.class;
    Object outerInstance = allocator.newInstance(outerClass);
    Object innerInstance = allocator.newInstance(innerClass);
    assertInstanceOf(outerClass, outerInstance);
    assertInstanceOf(innerClass, innerInstance);
  }

  @Test
  public void testNewInstanceThrowException() throws Exception {
    Class<?> throwableClass = Throwable.class;
    try {
      allocator.newInstance(throwableClass);
    } catch (InvocationTargetException e) {
      assertInstanceOf(Throwable.class, e.getCausedByException());
    }
  }

  @Test(expected = UndeclaredThrowableException.class)
  public void testNewInstanceWithUndeclaredThrowable() throws Exception {
    Class<?> throwableClass = new ExceptionWithUndeclaredThrowable().getClass();
    allocator.newInstance(throwableClass);
  }

  private static class AbstractClass {
  }

  private static class TwoArgumentConstructor {
  }

  private static class InnerClass {
    public static class InnerClass {
    }
  }

  private static class ExceptionWithUndeclaredThrowable extends Exception {
    private static class UndeclaredThrowableConstructor {
      ExceptionWithUndeclaredThrowable() throws RuntimeException {
        throw new RuntimeException();
      }
    }

    ExceptionWithUndeclaredThrowable() throws Exception {
      new UndeclaredThrowableConstructor().newInstance();
    }
  }

  private static class UnsafeAllocatorImpl extends UnsafeAllocator {
  }

}