import static org.junit.Assert.*;
import static com.google.common.base.Splitter.*;

public class SplitterTest {
    @Test
    public void testOnCharSeparator() {
        CharSequence input = "apple,banana,orange";
        Splitter splitter = on(',').split(input);
        List<String> expected = Arrays.asList("apple", "banana", "orange");
        List<String> result = Splitter.asList(splitter);
        assertEquals(expected, result);
    }
}