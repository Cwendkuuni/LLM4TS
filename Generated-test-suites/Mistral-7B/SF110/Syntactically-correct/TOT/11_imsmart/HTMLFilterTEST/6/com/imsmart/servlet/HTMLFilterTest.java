import org.junit.Test;
import static org.junit.Assert.*;//for assertions

public class HTMLFilterTest {
    // Test case for filter method with null input
    @Test
    public void testFilterNullInput() {
        String result = HTMLFilter.filter(null);
        assertNull(result);
    }

    // Test case for filter method with empty string input
    @Test
    public void testFilterEmptyInput() {
        String result = HTMLFilter.filter("");
        assertEquals("", result);
    }

    // Test case for filter method with valid input
    @Test
    public void testFilterValidInput() {
        String message = "<html><head><title>Test</title></head><body><p>Hello World!</p></body></html>";
        String result = HTMLFilter.filter(message);
        String expectedResult = "&lt;html&gt;&lt;head&gt;&lt;title&gt;Test&lt;/title&gt;&lt;body&gt;&lt;p&gt;Hello World!&lt;/p&gt;&lt;/body&gt;&lt;/html&gt;";
        assertEquals(expectedResult, result);
    }
}