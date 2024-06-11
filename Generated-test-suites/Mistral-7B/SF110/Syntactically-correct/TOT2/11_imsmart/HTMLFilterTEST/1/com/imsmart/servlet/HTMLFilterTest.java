import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class HTMLFilterTest {
    @Test
    public void testFilter() {
        String input = "<html><head></head><body><h1>Hello World</h1></body></html>";
        String expectedOutput = "&lt;html&gt;&lt;head&gt;&lt;!--&gt; &lt;!--&gt;&lt;/head&gt;&lt;body&gt;&lt;h1&gt;Hello World&lt;h1&gt;&lt;/body&gt;&lt;/html&gt;";
        String output = HTMLFilter.filter(input);
        assertEquals(expectedOutput, output);
    }
}