import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class HTMLFilterTest {

    @Test
    public void testFilterMethod() {
        String input = "<html><head><title>Test</title></head><body>";
        String expectedOutput = "&lt;html&gt;&lt;head&gt;&lt;title&gt;Test&lt;/title&gt;&lt;/head&gt;&lt;body&gt;";
        String output = HTMLFilter.filter(input);
        assertEquals(expectedOutput, output);
    }
}