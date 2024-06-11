import org.junit.Test;
import static org.junit.Assert.*;
import com.google.common.base.Splitter;

public class SplitterTest {
    @Test
    public void testOnCharSeparator() {
        Splitter splitter = Splitter.on('-');
        String input = "test-1-test-2-test-3";
        Iterable<String> result = splitter.split(input);
        List<String> expected = Arrays.asList("test", "1", "test", "2", "test", "3");
        assertEquals(expected, result);
    }
}