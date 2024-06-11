package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;

public class SplitterTest {
    @Test
    public void testOnCharSeparator() {
        Splitter splitter = Splitter.on('-');
        String input = "apple-banana-orange-grape";
        List<String> expectedOutput = Arrays.asList("apple", "banana", "orange", "grape");
        List<String> output = splitter.splitToList(input);
        assertEquals(expectedOutput, output);
        // Test edge cases and exception handling scenarios
        String emptyInput = "";
        try {
            List<String> outputEmpty = splitter.splitToList(emptyInput);
            fail("Expected an IllegalArgumentException for empty input string");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
        String nullInput = null;
        try {
            List<String> outputNull = splitter.splitToList(nullInput);
            fail("Expected a NullPointerException for null input CharSequence");
        } catch (NullPointerException e) {
            // Expected exception
        }
        String charSequenceWithOnlyWhiteSpaces = "   ";
        List<String> outputWhiteSpaces = splitter.splitToList(charSequenceWithOnlyWhiteSpaces);
        assertEquals(Arrays.asList(""), outputWhiteSpaces);
        String charSequenceWithOnlySeparator = "-";
        List<String> outputOnlySeparator = splitter.splitToList(charSequenceWithOnlySeparator);
        assertEquals(Collections.singletonList(""), outputOnlySeparator);
        String charSequenceWithFirstLastSeparator = "--apple--banana--";
        List<String> outputFirstLastSeparator = splitter.splitToList(charSequenceWithFirstLastSeparator);
        assertEquals(Arrays.asList("", "apple", "", "banana", "", ""), outputFirstLastSeparator);
        String charSequenceWithMiddleSeparator = "apple---banana-orange-grape";
        List<String> outputMiddleSeparator = splitter.splitToList(charSequenceWithMiddleSeparator);
        assertEquals(Arrays.asList("apple", "", "banana", "", "orange", "", "grape"), outputMiddleSeparator);
        int limit = 2;
        Splitter splitterWithLimit = Splitter.on('-').limit(limit);
        String charSequenceWithLimit = "apple-banana-orange-grape-apple-banana-orange-grape";
        List<String> outputWithLimit = splitterWithLimit.splitToList(charSequenceWithLimit);
        assertEquals(Arrays.asList("apple", "banana", "orange", "grape", "apple", "banana"), outputWithLimit);
        String charSequenceWithCombinationOfSeparatorsAndNonSeparators = "apple--banana:orange-grape";
        List<String> outputWithCombination = splitter.splitToList(charSequenceWithCombinationOfSeparatorsAndNonSeparators);
        assertEquals(Arrays.asList("apple", "banana:", "orange", "grape"), outputWithCombination);
    }

    @Test
    public void testOnStringSeparator() {
        Splitter splitter = Splitter.on("-");
        String input = "apple:banana:orange:grape";
        List<String> expectedOutput = Arrays.asList("apple", "banana", "orange", "grape");
        List<String> output = splitter.splitToList(input);
        assertEquals(expectedOutput, output);
        // Test edge cases and exception handling scenarios
        String emptyInput = "";
        try {
            List<String> outputEmpty = splitter.splitToList(emptyInput);
            fail("Expected an IllegalArgumentException for empty input string");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
        String nullInput = null;
        try {
            List<String> outputNull = splitter.splitToList(nullInput);
            fail("Expected a NullPointerException for null input CharSequence");
        } catch (NullPointerException e) {
            // Expected exception
        }
        String charSequenceWithOnlySeparator = "--";
        List<String> outputOnlySeparator = splitter.splitToList(charSequenceWithOnlySeparator);
        assertEquals(Collections.singletonList(""), outputOnlySeparator);
        String charSequenceWithFirstLastSeparator = "--apple--banana--";
        List<String> outputFirstLastSeparator = splitter.splitToList(charSequenceWithFirstLastSeparator);
        assertEquals(Arrays.asList("", "apple", "", "banana", "", ""), outputFirstLastSeparator);
        String charSequenceWithMiddleSeparator = "apple:-banana:-orange-grape";
        List<String> outputMiddleSeparator = splitter.splitToList(charSequenceWithMiddleSeparator);
        assertEquals(Arrays.asList("apple", ":", "banana", ":", "orange", ":", "grape"), outputMiddleSeparator);
        int limit = 2;
        Splitter splitterWithLimit = Splitter.on("-").limit(limit);
        String charSequenceWithLimit = "apple:banana:orange:grape:apple:banana:orange:grape";
        List<String> outputWithLimit = splitterWithLimit.splitToList(charSequenceWithLimit);
        assertEquals(Arrays.asList("apple", "banana", "orange", "grape", "apple"), outputWithLimit);
        String charSequenceWithCombinationOfSeparatorsAndNonSeparators = "apple:-banana:orange--grape";
        List<String> outputWithCombination = splitter.splitToList(charSequenceWithCombinationOfSeparatorsAndNonSeparators);
        assertEquals(Arrays.asList("apple", "--banana", "orange--grape"), outputWithCombination);
    }

    @Test
    public void testOnPatternSeparator() {
        String input = " apple\tbana\torange\tgrape ";
        Pattern separatorPattern = Pattern.compile("\\s+");
        Splitter splitter = Splitter.on(separatorPattern);
        List<String> expectedOutput = Arrays.asList("apple", "banana", "orange", "grape");
        List<String> output = splitter.splitToList(input);
        assertEquals(expectedOutput, output);
        // Test edge cases and exception handling scenarios
        String emptyInput = "";
        try {
            List<String> outputEmpty = splitter.splitToList(emptyInput);
            fail("Expected an IllegalArgumentException for empty input string");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
        String nullInput = null;
        try {
            List<String> outputNull = splitter.splitToList(nullInput);
            fail("Expected a NullPointerException for null input CharSequence");
        } catch (NullPointerException e) {
            // Expected exception
        }
        String charSequenceWithNoSeparator = "applebananaorangegrape";
        List<String> outputNoSeparator = splitter.splitToList(charSequenceWithNoSeparator);
        assertEquals(Arrays.asList("apple", "banana", "orange", "grape"), outputNoSeparator);
        int limit = 2;
        Splitter splitterWithLimit = Splitter.on(separatorPattern).limit(limit);
        String charSequenceWithLimit = "apple banana orange grape apple banana orange grape";
        List<String> outputWithLimit = splitterWithLimit.splitToList(charSequenceWithLimit);
        assertEquals(Arrays.asList("apple", "banana", "orange", "grape", "apple"), outputWithLimit);
        String charSequenceWithCombinationOfSeparatorsAndNonSeparators = "apple banana:orange--grape";
        List<String> outputWithCombination = splitter.splitToList(charSequenceWithCombinationOfSeparatorsAndNonSeparators);
        assertEquals(Arrays.asList("apple", "banana:", "orange--grape"), outputWithCombination);
        String charSequenceWithEmptySeparatorPattern = Splitter.on(Pattern.compile(""));
        try {
            List<String> outputEmptyPattern = charSequenceWithEmptySeparatorPattern.splitToList(input);
            fail("Expected an IllegalArgumentException for empty separator pattern");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
        String charSequenceWithNonMatchingSeparatorPattern = Splitter.on(Pattern.compile("[^\\s]+"));
        List<String> outputWithNonMatchingSeparatorPattern = splitter.splitToList(input);
        assertNotEquals(expectedOutput, outputWithNonMatchingSeparatorPattern);
    }
}