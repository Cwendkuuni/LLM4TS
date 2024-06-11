import static org.junit.Assert.assertEquals;

public class HTMLFilterTest {
    @Test
    public void testFilterNormalCase() {
        String input = "<html>Hello World<head></head></html>";
        String expectedOutput = "&lt;html&gt;Hello World&lt;head&gt;&lt;head&gt;&lt;/head&gt;&lt;/html&gt;";
        String output = HTMLFilter.filter(input);
        assertEquals(expectedOutput, output);
    }
}