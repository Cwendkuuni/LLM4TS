import static org.junit.Assert.*;
import com.google.common.base.Splitter;

public class SplitterTest {
    @Test
    public void testOnCharSeparator() {
        String input = "apple-banana-orange-grape";
        Splitter splitter = Splitter.on('-');
        Iterable<String> result = splitter.split(input);
        List<String> expected = Arrays.asList("apple", "banana", "orange", "grape");
        assertEquals(expected, result);
    }
}