import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class HTMLFilterTest {
    @Test
    public void testFilter() {
        String input = "<html><head><title>Test</title></head><body>Hello World</body></html>";
        String expectedOutput = "&lt;html&gt;&lt;head&gt;&lt;title&gt;Test&lt;/title&gt;&lt;/head&gt;&lt;body&gt;Hello World&lt;/body&gt;&lt;/html&gt;";
        String output = HTMLFilter.filter(input);
        assertEquals(expectedOutput, output);
    }
}