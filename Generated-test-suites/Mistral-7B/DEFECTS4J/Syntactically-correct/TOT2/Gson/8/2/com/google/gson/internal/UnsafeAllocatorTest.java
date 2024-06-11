import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class UnsafeAllocatorTest {

  private UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();

  @Test
  public void testNewInstance() throws Exception {
    Object obj = unsafeAllocator.newInstance(String.class);
    // You can add assertions here to check the functionality of the newInstance method
  }
}