import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import com.google.common.base.Splitter;

public class SplitterTest {
    @Test
    public void testOnCharSeparator() {
        Splitter splitter = Splitter.on('-');
        CharSequence input = "apple-banana-orange";
        Iterable<String> output = splitter.split(input);
        assertNotNull(output);
        List<String> expectedOutput = Arrays.asList("apple", "banana", "orange");
        assertEquals(expectedOutput, output);
    }
}