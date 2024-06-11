import static org.junit.Assert.*;
import com.google.common.base.Splitter;

public class SplitterTest {

    @Test
    public void testOnCharSeparator() {
        String input = "apple-banana-cherry";
        Splitter splitter = Splitter.on('-').split(input);
        List<String> expected = Arrays.asList("apple", "banana", "cherry");
        List<String> actual = new ArrayList<String>(splitter);
        assertEquals(expected, actual);
    }
}