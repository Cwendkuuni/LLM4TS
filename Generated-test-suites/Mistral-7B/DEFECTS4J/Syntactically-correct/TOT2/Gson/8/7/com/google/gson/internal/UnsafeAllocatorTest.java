import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.JUnit4;
import com.google.gson.internal.UnsafeAllocator;

@RunWith(JUnit4.class)
public class UnsafeAllocatorTest {

    @Test
    public void testNewInstance() throws Exception {
        UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
        String testClass = String.class;
        String instance = unsafeAllocator.newInstance(testClass);
        // Add assertions here to verify the instance is correctly created
    }
}