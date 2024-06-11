import org.junit.Test;
import org.junit.experimental.categories.Category;
import static org.junit.Assert.*;

@Category(IntegrationTests.class)
public class UnsafeAllocatorTest {

  @Test
  public void testNewInstance() {
    UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
    String stringInstance = unsafeAllocator.newInstance(String.class);
    assertNotNull(stringInstance);
  }
}