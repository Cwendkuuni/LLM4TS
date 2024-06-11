import org.junit.Test;
import com.google.gson.internal.UnsafeAllocator;

public class UnsafeAllocatorTest {
  private static final UnsafeAllocator UNSAFE_ALLOCATOR = UnsafeAllocator.create();

  @Test
  public void testNewInstance() {
    String expected = "Hello World";
    String actual = UNSAFE_ALLOCATOR.newInstance(String.class);
    org.junit.Assert.assertEquals(expected, actual);
  }
}