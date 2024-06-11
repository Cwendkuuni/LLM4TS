import static org.junit.Assert.*;
import com.google.common.base.Splitter;

public class SplitterTest {
    @Test
    public void testSplitWithCharSeparator() {
        Splitter splitter = Splitter.on('-');
        String input = "apple-banana-orange";
        List<String> expectedOutput = Arrays.asList("apple", "banana", "orange");
        List<String> actualOutput = splitter.splitToList(input);
        assertEquals(expectedOutput, actualOutput);
    }
}