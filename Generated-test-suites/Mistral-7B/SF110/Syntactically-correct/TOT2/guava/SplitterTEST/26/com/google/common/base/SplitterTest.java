import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class SplitterTest {
    // 

    @Test
    public void testOnCharSeparator() {
        Splitter splitter = Splitter.on('-');
        CharSequence testSequence = "test1-test2-test3";
        Iterable<String> result = splitter.split(testSequence);
        List<String> expectedResult = Arrays.asList("test1", "test2", "test3");
        assertEquals(expectedResult, result);
    }

    // 

    // Edge case: empty string
    @Test(expected = IllegalArgumentException.class)
    public void testOnCharSeparatorEmptyString() {
        Splitter splitter = Splitter.on('-');
        CharSequence testSequence = "";
        Splitter.split(splitter, testSequence);
    }

    // Error scenario: null input
    @Test(expected = NullPointerException.class)
    public void testOnCharSeparatorNullInput() {
        Splitter splitter = Splitter.on('-');
        CharSequence testSequence = null;
        Splitter.split(splitter, testSequence);
    }
}