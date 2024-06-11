import static org.junit.Assert.*;
import com.google.common.base.Splitter;

public class SplitterTest {
    @Test
    public void testOnCharMatcher() {
        Splitter splitter = Splitter.on(CharMatcher.is('-'));
        String input = "apple-banana-orange-grape";
        List<String> expected = Arrays.asList("apple", "banana", "orange", "grape");
        List<String> result = splitter.splitToList(input);
        assertEquals(expected, result);
    }
}