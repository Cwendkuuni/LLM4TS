import static org.junit.Assert.*;
import com.google.common.base.Splitter;

public class SplitterTest {
    @Test
    public void testOnCharSeparator() {
        Splitter splitter = Splitter.on('-');
        String input = "one-two-three-four";
        List<String> expected = Arrays.asList("one", "two", "three", "four");
        List<String> actual = new ArrayList<>();
        for (String string : splitter.split(input)) {
            actual.add(string);
        }
        assertEquals(expected, actual);
    }
}