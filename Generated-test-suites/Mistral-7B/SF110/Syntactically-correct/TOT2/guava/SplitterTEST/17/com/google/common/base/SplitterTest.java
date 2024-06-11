import static org.junit.Assert.*;
import com.google.common.base.Splitter;

public class SplitterTest {

    @Test
    public void testOnCharSeparator() {
        Splitter splitter = Splitter.on('-');
        String input = "apple-banana-orange";
        List<String> expected = Arrays.asList("apple", "banana", "orange");
        List<String> result = splitter.splitToList(input);
        assertEquals(expected, result);
    }

    // Add edge cases and exception handling tests here
}