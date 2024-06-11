package com.google.common.base;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class SplitterTest {

    @Test
    public void testSplitOnCharacter() {
        Splitter splitter = Splitter.on('-');
        String input = "--hello-world---";
        List<String> expectedOutput = Arrays.asList("hello", "world");
        List<String> output = splitter.splitToList(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testSplitOnEmptyString() {
        Splitter splitter = Splitter.on("");
        String input = "";
        List<String> expectedOutput = Arrays.asList("");
        List<String> output = splitter.splitToList(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testSplitOnString() {
        Splitter splitter = Splitter.on(":");
        String input = "key1:value1:key2:value2";
        Map<String, String> expectedOutput = new LinkedHashMap<String, String>() {{
            put("key1", "value1");
            put("key2", "value2");
        }};
        Map<String, String> output = ((MapSplitter) splitter.withKeyValueSeparator(":")).split(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testSplitOnPattern() {
        String separatorPattern = "[\\s,]+";
        Splitter splitter = Splitter.onPattern(separatorPattern);
        String input = "foo bar baz qux, quux quux, foo bar baz";
        List<String> expectedOutput = Arrays.asList("foo", "bar", "baz", "qux,", "quux", "quux,", "foo", "bar", "baz");
        List<String> output = splitter.splitToList(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testSplitOnPatternWithEmptyString() {
        String separatorPattern = "[\\s,]+";
        Splitter splitter = Splitter.onPattern(separatorPattern);
        String input = "";
        List<String> expectedOutput = Arrays.asList("");
        List<String> output = splitter.splitToList(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testSplitOnPatternWithEmptyInput() {
        String separatorPattern = "[\\s,]+";
        Splitter splitter = Splitter.onPattern(separatorPattern);
        String input = null;
        List<String> expectedOutput = Collections.emptyList();
        List<String> output = splitter.splitToList(input);
        assertEquals(expectedOutput, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSplitOnPatternWithEmptyPattern() {
        String separatorPattern = "";
        Splitter splitter = Splitter.onPattern(separatorPattern);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSplitOnPatternWithNullPattern() {
        Splitter splitter = Splitter.onPattern(null);
    }

    @Test
    public void testSplitOnFixedLength() {
        String input = "0123456789";
        Splitter splitter = Splitter.fixedLength(3);
        List<String> expectedOutput = Arrays.asList("012", "345", "567", "89");
        List<String> output = splitter.splitToList(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testSplitOnFixedLengthWithEmptyString() {
        String input = "";
        Splitter splitter = Splitter.fixedLength(3);
        List<String> expectedOutput = Arrays.asList("");
        List<String> output = splitter.splitToList(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testSplitOnFixedLengthWithNullInput() {
        String input = null;
        Splitter splitter = Splitter.fixedLength(3);
        List<String> expectedOutput = Collections.emptyList();
        List<String> output = splitter.splitToList(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testSplitWithOmittedEmptyStrings() {
        Splitter splitter = Splitter.on('-').omitEmptyStrings();
        String input = "--foo---bar--";
        List<String> expectedOutput = Arrays.asList("foo", "bar");
        List<String> output = splitter.splitToList(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testSplitWithLimit() {
        Splitter splitter = Splitter.on('-').limit(3);
        String input = "--foo---bar--baz--qux--quux--";
        List<String> expectedOutput = Arrays.asList("foo", "bar", "baz");
        List<String> output = splitter.splitToList(input);
        assertEquals(expectedOutput, output);
    }
}