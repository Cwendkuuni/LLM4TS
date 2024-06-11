
import com.google.gson.internal.UnsafeAllocator;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnsafeAllocatorTest {

  @Test
  public void testCreateUnsafeAllocator() {
    UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
    assertNotNull(unsafeAllocator);
  }

  @Test
  public void testNewInstance() {
    UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
    String stringInstance = unsafeAllocator.newInstance(String.class);
    assertNotNull(stringInstance);
  }

  @Test
  public void testNewInstanceWithInterface() {
    UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
    Assert.throwsException(UnsupportedOperationException.class, () -> {
      unsafeAllocator.newInstance(Object.class);
    });
  }

  @Test
  public void testNewInstanceWithAbstractClass() {
    UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
    Assert.throwsException(UnsupportedOperationException.class, () -> {
      unsafeAllocator.newInstance(AbstractClass.class);
    });
  }
}

abstract class AbstractClass {
}
