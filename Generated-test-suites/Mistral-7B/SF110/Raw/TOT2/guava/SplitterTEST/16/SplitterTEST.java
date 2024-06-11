import org.junit.Test;
import static org.junit.Assert.*;

public class SplitterTest {
    @Test
    public void testOnCharSeparator() {
        Splitter splitter = Splitter.on('-');
        Iterable<String> result = splitter.split("test-string-1-test-string-2-test-string-3");
        List<String> expected = Arrays.asList("test-string-1", "test-string-2", "test-string-3");
        assertEquals(expected, result);
    }

    @Test(expected = NullPointerException.class)
    public void testOnCharSeparatorWithNullInput() {
        Splitter splitter = Splitter.on('-');
        splitter.split(null);
    }
}