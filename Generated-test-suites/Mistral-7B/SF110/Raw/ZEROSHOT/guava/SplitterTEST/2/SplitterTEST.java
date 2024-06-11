package com.example.junit;

import static org.junit.Assert.*;
import com.google.common.base.Splitter;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SplitterTest {

    @Test
    public void testOnCharSeparator() {
        Splitter splitter = Splitter.on('-');
        List<String> result = splitter.split("test-1, test-2, test-3");
        assertEquals(Arrays.asList("test-1", "test-2", "test-3"), result);
    }

    @Test
    public void testOnStringSeparator() {
        Splitter splitter = Splitter.on("-");
        List<String> result = splitter.split("test-1, test-2, test-3");
        assertEquals(Arrays.asList("test-1", "test-2", "test-3"), result);
    }

    @Test
    public void testOnPatternSeparator() {
        Pattern pattern = Pattern.compile("\\s+");
        Splitter splitter = Splitter.onPattern(pattern);
        List<String> result = splitter.split("test 1 test 2 test 3");
        assertEquals(Arrays.asList("test", "1", "test", "2", "test", "3"), result);
    }

    @Test
    public void testFixedLengthSplitter() {
        Splitter splitter = Splitter.fixedLength(3);
        List<String> result = splitter.split("123abc456def");
        assertEquals(Arrays.asList("123", "abc", "456", "def"), result);
    }

    @Test
    public void testOnEmptyStringSeparator() {
        Splitter splitter = Splitter.on(" ");
        List<String> result = splitter.split("test test test");
        assertEquals(Arrays.asList("test", "test", "test"), result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullSequence() {
        Splitter splitter = Splitter.on('-');
        splitter.split(null);
    }

    @Test
    public void testWithKeyValueSeparator() {
        String input = "name=John,age=30,city=New York";
        Splitter splitter = Splitter.on("=").omitEmptyStrings().limit(2);
        MapSplitter mapSplitter = splitter.withKeyValueSeparator(Splitter.on("="));
        Map<String, String> result = mapSplitter.split(input);
        assertEquals(Collections.singletonMap("name", "John"), result);
    }
}