import org.junit.Test;
import com.google.gson.internal.UnsafeAllocator;

public class UnsafeAllocatorTest {
  private UnsafeAllocator allocator = UnsafeAllocator.create();

  @Test
  public void testNewInstance() {
    String str = allocator.newInstance(String.class);
    assert (str != null);
  }
}