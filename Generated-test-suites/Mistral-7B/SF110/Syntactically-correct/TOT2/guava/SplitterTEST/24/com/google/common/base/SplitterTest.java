import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import com.google.common.base.Splitter;

public class SplitterTest {
    @Test
    public void testSplitWithCharSeparator() {
        Splitter splitter = Splitter.on('-');
        String input = "this-is-a-test-string";
        Iterable<String> output = splitter.split(input);
        String[] expectedOutput = {"this", "is", "a", "test", "string"};
        assertEquals(expectedOutput.length, output.size());
        String[] actualOutput = new String[output.size()];
        Iterator<String> iter = output.iterator();
        for (int i = 0; i < actualOutput.length; i++) {
            actualOutput[i] = iter.next();
        }
        assertArrayEquals(expectedOutput, actualOutput);
    }
}