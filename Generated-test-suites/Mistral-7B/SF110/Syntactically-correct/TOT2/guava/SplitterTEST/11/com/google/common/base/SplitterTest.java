import static org.junit.Assert.*;
import com.google.common.base.Splitter;

public class SplitterTest {
    // Test starts here
    

    @Test
    public void testSplitterOnCharSeparator() {
        Splitter splitter = Splitter.on('-');
        String input = "apple-banana-cherry-grape";
        List<String> expectedOutput = Arrays.asList("apple", "banana", "cherry", "grape");
        List<String> output = new ArrayList<>();
        for (String element : splitter.split(input)) {
            output.add(element);
        }
        assertEquals(expectedOutput, output);
    }

    @Test(expected = NullPointerException.class)
    public void testNullCharSequence() {
        Splitter splitter = Splitter.on('-');
        assertNull(splitter.split(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyCharSequence() {
        Splitter splitter = Splitter.on('-');
        assertNull(splitter.split(Character.MIN_VALUE + ""));
    }

    @Test
    public void testEmptyInputWithOmitEmptyStrings() {
        Splitter splitter = Splitter.on('-').omitEmptyStrings();
        String input = "";
        List<String> expectedOutput = Collections.emptyList();
        List<String> output = new ArrayList<>();
        for (String element : splitter.split(input)) {
            output.add(element);
        }
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testEmptyInputWithoutOmitEmptyStrings() {
        Splitter splitter = Splitter.on('-');
        String input = "";
        List<String> expectedOutput = Arrays.asList("");
        List<String> output = new ArrayList<>();
        for (String element : splitter.split(input)) {
            output.add(element);
        }
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testSplitterOnCharSeparatorWithLimit() {
        Splitter splitter = Splitter.on('-').limit(3);
        String input = "apple-banana-cherry-grape-pineapple-orange-mango";
        List<String> expectedOutput = Arrays.asList("apple", "banana", "cherry");
        List<String> output = new ArrayList<>();
        for (String element : splitter.split(input)) {
            output.add(element);
        }
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testSplitterOnCharSeparatorWithTrimResults() {
        Splitter splitter = Splitter.on('-').trimResults();
        String input = " apple-banana- cherry  grape ";
        List<String> expectedOutput = Arrays.asList("apple", "banana", "cherry", "grape");
        List<String> output = new ArrayList<>();
        for (String element : splitter.split(input)) {
            output.add(element);
        }
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testSplitterOnCharSeparatorWithEmptyInputAndTrimResults() {
        Splitter splitter = Splitter.on('-').trimResults();
        String input = "";
        List<String> expectedOutput = Collections.emptyList();
        List<String> output = new ArrayList<>();
        for (String element : splitter.split(input)) {
            output.add(element);
        }
        assertEquals(expectedOutput, output);
    }

    
}