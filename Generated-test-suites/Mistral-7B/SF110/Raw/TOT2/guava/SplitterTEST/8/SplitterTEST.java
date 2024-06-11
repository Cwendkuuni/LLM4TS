import static org.junit.Assert.*;
import com.google.common.base.Splitter;

public class SplitterTest {
    // Test Start
    @Test
    public void testOnChar() {
        Splitter splitter = Splitter.on(' ');
        CharSequence sequence = "Hello World";
        Iterable<String> result = splitter.split(sequence);
        List<String> expected = Arrays.asList("Hello", "World");
        assertEquals(expected, result);
    }

    // Test Edge Cases
    @Test
    public void testEmptyString() {
        Splitter splitter = Splitter.on(' ');
        CharSequence sequence = "";
        Iterable<String> result = splitter.split(sequence);
        List<String> expected = Collections.emptyList();
        assertEquals(expected, result);
    }

    @Test
    public void testNullSequence() {
        Splitter splitter = Splitter.on(' ');
        CharSequence sequence = null;
        try {
            splitter.split(sequence);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    // Test Exception Scenarios
    @Test(expected = NullPointerException.class)
    public void testNullSplitter() {
        Splitter splitter = null;
        CharSequence sequence = "Test";
        splitter.split(sequence);
    }

    // Test End
    
}