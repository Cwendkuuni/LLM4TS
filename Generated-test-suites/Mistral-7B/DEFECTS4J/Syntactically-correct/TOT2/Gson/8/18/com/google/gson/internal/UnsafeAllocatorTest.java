import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.gson.internal.UnsafeAllocator;

@RunWith(JUnit4.class)
public class UnsafeAllocatorTest {

  @Test
  public void testNewInstance() {
    UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
    String expected = "Test String";
    String instance = unsafeAllocator.newInstance(String.class);
    org.junit.Assert.assertEquals(expected, instance);
  }
}