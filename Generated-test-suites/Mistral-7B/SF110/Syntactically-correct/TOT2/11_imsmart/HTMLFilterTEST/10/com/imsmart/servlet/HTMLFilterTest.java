import static org.junit.Assert.*;

public class HTMLFilterTest {
    @Test
    public void testFilter() {
        String input = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        String expectedOutput = "&lt;html&gt;&lt;head&gt;&lt;title&gt;Test&lt;/title&gt;&lt;/head&gt;&lt;body&gt;&lt;h1&gt;Hello World!&lt;/h1&gt;&lt;/body&gt;&lt;/html&gt;";
        String output = HTMLFilter.filter(input);
        assertEquals(expectedOutput, output);
    }
}