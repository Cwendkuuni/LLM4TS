import org.junit.Test;
import com.google.common.base.Splitter;
import static org.junit.Assert.*;

public class SplitterTest {
    @Test
    public void testOnCharSeparator() {
        Splitter splitter = Splitter.on('-');
        Iterable<String> result = splitter.split("test-one-test-two-test-three");
        List<String> expectedList = Arrays.asList("test", "one", "test", "two", "test", "three");
        assertEquals(expectedList, result);
    }
}