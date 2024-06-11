package com.google.common.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class SplitterTest {

    @Test
    public void testOnCharSeparator() {
        Splitter splitter = Splitter.on('-');
        List<String> result = splitter.split("one-two-three-four");
        assertEquals(Arrays.asList("one", "two", "three", "four"), result);
    }

    @Test
    public void testOnCharMatcherSeparator() {
        Splitter splitter = Splitter.on(CharMatcher.is('-'));
        List<String> result = splitter.split("one--two--three--four");
        assertEquals(Arrays.asList("one", "", "two", "", "three", "", "four"), result);
    }

    @Test
    public void testOnEmptyStringSeparator() {
        Splitter splitter = Splitter.on("");
        List<String> result = splitter.split("one,two,three");
        assertEquals(Arrays.asList("one", "", "two", "", "three"), result);
    }

    @Test(expected = NullPointerException.class)
    public void testOnNullSeparator() {
        Splitter.on(null);
    }

    @Test
    public void testOnPatternSeparator() {
        Splitter splitter = Splitter.onPattern("\\s+");
        List<String> result = splitter.split("one   two   three   four");
        assertEquals(Arrays.asList("one", "two", "three", "four"), result);
    }

    @Test
    public void testOnPatternInvalid() {
        try {
            Splitter.onPattern(".*");
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testFixedLengthSeparator() {
        Splitter splitter = Splitter.fixedLength(2);
        List<String> result = splitter.split("onetwothreefourfive");
        assertEquals(Arrays.asList("onetwo", "threesfour", "fifth"), result);
    }

    @Test
    public void testLimitSeparator() {
        Splitter splitter = Splitter.on('-').limit(2);
        List<String> result = splitter.split("one-two-three-four-five-six-seven");
        assertEquals(Arrays.asList("one-two", "three-four"), result);
    }

    @Test
    public void testOmitEmptyStrings() {
        Splitter splitter = Splitter.on('-').omitEmptyStrings();
        List<String> result = splitter.split("one--two--three--");
        assertEquals(Arrays.asList("one", "two", "three"), result);
    }

    @Test
    public void testTrimResults() {
        Splitter splitter = Splitter.on('-').trimResults();
        List<String> result = splitter.split("one--two--three--");
        assertEquals(Arrays.asList("one", "two", "three"), result);
    }

    @Test
    public void testTrimResultsWithWhitespace() {
        Splitter splitter = Splitter.on('-').trimResults(CharMatcher.WHITESPACE);
        List<String> result = splitter.split(" one--two-- three-- ");
        assertEquals(Arrays.asList("one", "two", "three"), result);
    }

    @Test
    public void testWithKeyValueSeparator() {
        Splitter keyValueSplitter = Splitter.on(":");
        Map<String, String> map = Splitter.new MapSplitter(Splitter.on('-'), keyValueSplitter).split("key1:value1:key2:value2:key3:value3");
        assertEquals(Arrays.asList("key1", "value1", "key2", "value2", "key3", "value3"), Arrays.asList(map.keySet().toArray()));
        assertEquals(Arrays.asList("value1", "value2", "value3"), Arrays.asList(map.values().toArray()));
    }

    @Test
    public void testWithInvalidEntry() {
        Map<String, String> map = Splitter.new MapSplitter(Splitter.on('-'), Splitter.on(':')).split("key1:value1:key2:value2:key3");
        assertEquals(2, map.size());
        try {
            map = Splitter.new MapSplitter(Splitter.on('-'), Splitter.on(':')).split("key1:value1:key2:value2:key3:");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}