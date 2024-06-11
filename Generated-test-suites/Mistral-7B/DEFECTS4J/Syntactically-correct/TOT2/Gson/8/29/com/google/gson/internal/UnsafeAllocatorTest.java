import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.gson.internal.UnsafeAllocator;

@RunWith(JUnit4.class)
public class UnsafeAllocatorTest {

  private UnsafeAllocator allocator = UnsafeAllocator.create();

  @Test
  public void testNewInstance() {
    String stringInstance = allocator.newInstance(String.class);
    assertNotNull(stringInstance);
  }
}