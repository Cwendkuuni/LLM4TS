package org.apache.commons.cli; // Replace with the actual package name if necessary

import static org.junit.Assert.*;
import org.junit.Test;

public class SplitterTest {

    @Test
    public void testSplitterWithDefaultSettings() {
        Splitter splitter = Splitter.on(' ');
        String input = "This is a test string to be split using the Splitter class.";
        List<String> expectedOutput = Arrays.asList("This", "is", "a", "test", "string", "to", "be", "split", "using", "the", "Splitter", "class.");
        List<String> output = splitter.splitToList(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testSplitterWithCustomSeparator() {
        Splitter splitter = Splitter.onPattern("\\|");
        String input = "item1|item2|item3|item4|item5";
        List<String> expectedOutput = Arrays.asList("item1", "item2", "item3", "item4", "item5");
        List<String> output = splitter.splitToList(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testSplitterWithCustomPattern() {
        Splitter splitter = Splitter.onPattern("\\[([^]]*)]");
        String input = "[item1][item2][item3][item4]";
        Map<String, String> expectedOutput = new LinkedHashMap<>();
        expectedOutput.put("item1", "");
        expectedOutput.put("item2", "");
        expectedOutput.put("item3", "");
        expectedOutput.put("item4", "");
        Map<String, String> output = splitter.withKeyValueSeparator().split(input).getMap();
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testSplitterWithCustomTrimmer() {
        Splitter splitter = Splitter.on(' ').trimResults(CharMatcher.is('a'));
        String input = "Apple is a fruit. Banana is also a fruit.";
        List<String> expectedOutput = Arrays.asList("Apple is a fruit.", "Banana is also a fruit.");
        List<String> output = splitter.splitToList(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testSplitterWithLimit() {
        Splitter splitter = Splitter.on(' ').limit(3);
        String input = "This is a test string to be split using the Splitter class with a limit of 3.";
        List<String> expectedOutput = Arrays.asList("This", "is", "a");
        List<String> output = splitter.splitToList(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testSplitterWithOmitEmptyStrings() {
        Splitter splitter = Splitter.on(' ').omitEmptyStrings();
        String input = "This is a test string to be split using the Splitter class with omitted empty strings.";
        List<String> expectedOutput = Arrays.asList("This", "is", "a", "test", "string", "to", "be", "split", "using", "the", "Splitter", "class");
        List<String> output = splitter.splitToList(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testSplitterWithCustomStrategy() {
        Splitter splitter = new Splitter(new Strategy() {
            @Override
            public SplittingIterator iterator(Splitter p0, CharSequence p1) {
                return new SplittingIterator(p0, p1) {
                    @Override
                    int separatorStart(int p0) {
                        return p0;
                    }

                    @Override
                    int separatorEnd(int p0) {
                        return p0 + 3;
                    }
                };
            }
        }, false, CharMatcher.NONE, 5);

        String input = "This is a test string to be split using a custom strategy.";
        List<String> expectedOutput = Arrays.asList("This", "is", "a", "test", "string", "to", "be", "split", "using", "a", "custom");
        List<String> output = splitter.splitToList(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testSplitterWithInvalidInput() {
        Splitter splitter = Splitter.on(' ');
        String input = "";
        Map<String, String> expectedOutput = Collections.emptyMap();
        Map<String, String> output = splitter.withKeyValueSeparator().split(input).getMap();
        assertEquals(expectedOutput, output);
    }
}