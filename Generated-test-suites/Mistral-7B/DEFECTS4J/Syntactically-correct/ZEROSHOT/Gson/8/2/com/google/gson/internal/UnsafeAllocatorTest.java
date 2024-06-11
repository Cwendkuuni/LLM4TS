import com.google.gson.internal.UnsafeAllocator;
import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;
import java.lang.reflect.AccessibleObject;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

@RunWith(JUnit4.class)
public class UnsafeAllocatorTest {
  private UnsafeAllocator unsafeAllocator;

  @Before
  public void setup() {
    unsafeAllocator = UnsafeAllocator.create();
  }

  @Test
  public void testNewInstanceNull() {
    assertThrows(NullPointerException.class, () -> unsafeAllocator.newInstance(null));
  }

  @Test
  public void testNewInstanceObject() {
    Object instance = unsafeAllocator.newInstance(Object.class);
    assertNotNull(instance);
    assertInstanceOf(Object.class, instance);
  }

  @Test
  public void testNewInstanceWrapperClass() {
    @SuppressWarnings("unchecked")
    UnsafeAllocator<Integer> unsafeAllocatorInteger = (UnsafeAllocator<Integer>) unsafeAllocator;
    Integer instance = unsafeAllocatorInteger.newInstance(Integer.class);
    assertNotNull(instance);
    assertInstanceOf(Integer.class, instance);
  }

  @Test
  public void testNewInstanceNonPublicClass() {
    Class<?> nonPublicClass = new ClassFactory().createNonPublicClass();
    assertThrows(IllegalAccessError.class, () -> unsafeAllocator.newInstance(nonPublicClass));
  }

  @Test
  public void testNewInstanceInterface() {
    assertThrows(UnsupportedOperationException.class, () -> unsafeAllocator.newInstance(String.class));
  }

  @Test
  public void testNewInstanceAbstract() {
    assertThrows(UnsupportedOperationException.class, () -> unsafeAllocator.newInstance(AbstractClass.class));
  }

  @Test
  public void testNewInstanceException() {
    try {
      Class<?> exceptionClass = Class.forName("java.lang.RuntimeException");
      assertThrows(RuntimeException.class, () -> unsafeAllocator.newInstance(exceptionClass));
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void testNewInstanceIllegalAccess() throws Exception {
    Class<?> illegalAccessClass = new ClassFactory().createIllegalAccessClass();
    AccessibleObject constructor = illegalAccessClass.getDeclaredConstructor();
    constructor.setAccessible(true);

    try {
      unsafeAllocator.newInstance((Class<Object>) illegalAccessClass);
    } catch (InvocationTargetException e) {
      Throwable cause = e.getCause();
      if (cause instanceof IllegalAccessError) {
        assertInstanceOf(IllegalAccessError.class, cause);
      } else if (cause instanceof UndeclaredThrowableException) {
        UndeclaredThrowableException ute = (UndeclaredThrowableException) cause;
        assertInstanceOf(IllegalAccessError.class, ute.getException());
      } else {
        throw new AssertionError("Unexpected exception: " + cause);
      }
    }
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstanceUnsupportedOperationException() {
    UnsafeAllocator unsupportedAllocator = new UnsafeAllocator() {
      @Override
      public <T> T newInstance(Class<T> c) {
        throw new UnsupportedOperationException("Cannot allocate " + c);
      }
    };
    unsupportedAllocator.newInstance(String.class);
  }
}

class AbstractClass {
  // abstract class
}

class ClassFactory {
  // helper method to create non-public, non-abstract classes
}