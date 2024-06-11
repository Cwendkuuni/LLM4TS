package com.example.junit4;

import org.junit.Test;
import static org.junit.Assert.*;
import com.google.common.base.Splitter;
import java.util.regex.Pattern;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class SplitterTest {

    @Test
    public void testOnCharSeparator() {
        // Test Basic Functionality
        CharSequence input = "HelloWorldHelloWorld";
        Splitter splitter = Splitter.on(' ');
        Iterable<String> output = splitter.split(input);
        String[] expected = {"Hello", "World", "Hello", "World"};
        assertEquals(Arrays.asList(expected), Iterables.toList(output));

        // Test Edge Case: Separator-only CharSequence
        input = "---";
        assertIterableEquals(Arrays.asList(""), splitter.split(input));

        // Test Exception Handling: null CharSequence
        assertNull(splitter.split(null));
    }

    @Test
    public void testOnStringSeparator() {
        // Test Basic Functionality
        CharSequence input = "HelloWorld:HelloWorld:HelloWorld";
        Splitter splitter = Splitter.on(":");
        Iterable<String> output = splitter.split(input);
        String[] expected = {"HelloWorld", "HelloWorld", "HelloWorld"};
        assertEquals(Arrays.asList(expected), Iterables.toList(output));

        // Test Edge Case: Separator-only CharSequence
        input = "----";
        assertIterableEquals(Arrays.asList(""), splitter.split(input));

        // Test Exception Handling: null CharSequence
        assertNull(splitter.split(null));
    }

    @Test
    public void testOnRegexSeparatorPattern() {
        // Test Basic Functionality
        CharSequence input = "AppleBananaOrangeAppleBananaOrange";
        Pattern separatorPattern = Pattern.compile("(?i)orange");
        Splitter splitter = Splitter.on(separatorPattern);
        Iterable<String> output = splitter.split(input);
        String[] expected = {"Apple", "Banana", "Apple", "Banana"};
        assertEquals(Arrays.asList(expected), Iterables.toList(output));

        // Test Edge Case: No Matches CharSequence
        input = "AppleAppleApple";
        assertIterableEquals(Arrays.asList(""), splitter.split(input));

        // Test Exception Handling: null CharSequence
        assertNull(splitter.split(null));

        // Test Exception Handling: null Separator Pattern
        assertNull(Splitter.on(null).split(input));
    }

    // ... (Add more test cases for other methods if necessary)

    private static <T> void assertIterableEquals(Iterable<T> expected, Iterable<T> actual) {
        assertEquals(expected.spliterator().getExactSizeIfKnown(), actual.spliterator().getExactSizeIfKnown());
        for (T e : expected) {
            assertTrue("Expected: " + e, actual.contains(e));
        }
    }
}