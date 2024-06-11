import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;
import org.junit.Assert.*;

public class IOUtilsTest {
    @Test
    public void testCopyMethod() throws IOException {
        // Given: InputStream and OutputStream
        ByteArrayInputStream input = new ByteArrayInputStream("Test data".getBytes());
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        // When: Calling copy method with default buffer size
        long result = IOUtils.copy(input, output);

        // Then: Checking the result and the content of OutputStream
        assertEquals(input.available(), result);
        assertEquals("Test data", output.toString());

        // Given: InputStream and OutputStream with a custom buffer size
        input = new ByteArrayInputStream("Test data".getBytes());
        output = new ByteArrayOutputStream(1024);

        // When: Calling copy method with a custom buffer size
        result = IOUtils.copy(input, output, 1024);

        // Then: Checking the result and the content of OutputStream
        assertEquals(input.available(), result);
        assertEquals("Test data", output.toString());
    }
}