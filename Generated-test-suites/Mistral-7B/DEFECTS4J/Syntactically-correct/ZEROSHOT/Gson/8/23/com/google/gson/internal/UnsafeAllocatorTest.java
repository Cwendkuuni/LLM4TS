import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import com.google.gson.internal.UnsafeAllocator;
import com.google.gson.internal.UnsafeAllocator.UnsafeAllocatorImpl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNotNull;

@RunWith(JUnit4.class)
public class UnsafeAllocatorTest {

  @Test
  public void testNewInstanceWithObjectClass() throws Exception {
    UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
    Object object = unsafeAllocator.newInstance(Object.class);
    assertNotNull(object);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstanceWithAbstractClass() throws Exception {
    UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
    try {
      unsafeAllocator.newInstance(AbstractClass.class);
    } catch (InvocationTargetException e) {
      throw e.getTargetException();
    } catch (UndeclaredThrowableException e) {
      throw e.getThrowable();
    }
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstanceWithInterface() throws Exception {
    UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
    try {
      unsafeAllocator.newInstance(Interface.class);
    } catch (InvocationTargetException e) {
      throw e.getTargetException();
    } catch (UndeclaredThrowableException e) {
      throw e.getThrowable();
    }
  }

  @Test
  public void testNewInstanceWithCustomClass() throws Exception {
    UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
    CustomClass customClass = unsafeAllocator.newInstance(CustomClass.class);
    assertInstanceOf(CustomClass.class, customClass);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstanceWithNullClass() throws Exception {
    UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
    try {
      unsafeAllocator.newInstance(null);
    } catch (InvocationTargetException e) {
      throw e.getTargetException();
    } catch (UndeclaredThrowableException e) {
      throw e.getThrowable();
    }
  }

  private static class AbstractClass {
  }

  private static class Interface implements Runnable {
  }

  private static class CustomClass {
  }
}

class UnsafeAllocatorImpl extends UnsafeAllocator {
  @Override
  public <T> T newInstance(Class<T> c) throws Exception {
    // Your implementation goes here
  }
}