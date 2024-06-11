import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class SplitterTest {

    @Test
    public void testSplitWithCharSeparator() {
        String input = "apple:banana:orange:grape";
        Splitter splitter = Splitter.on(':').split(input);
        List<String> expected = Arrays.asList("apple", "banana", "orange", "grape");
        List<String> actual = new ArrayList<>(splitter);
        assertEquals(expected, actual);
    }

    @Test
    public void testEmptyStringWithCharSeparator() {
        Splitter splitter = Splitter.on(':').split("");
        assertNull(splitter.split("").next());
    }

    @Test(expected = NullPointerException.class)
    public void testNullInputWithCharSeparator() {
        Splitter splitter = Splitter.on(':');
        splitter.split(null);
    }
}