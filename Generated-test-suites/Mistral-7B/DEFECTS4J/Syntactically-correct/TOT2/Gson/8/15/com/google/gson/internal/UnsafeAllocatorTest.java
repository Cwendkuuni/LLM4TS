import static org.junit.Assert.assertNotNull;
import com.google.gson.internal.UnsafeAllocator;

public class UnsafeAllocatorTest {
  // 

  @org.junit.Test
  public void testNewInstance() {
    UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
    String str = (String) unsafeAllocator.newInstance(String.class);
    assertNotNull(str);
  }

  // 
}