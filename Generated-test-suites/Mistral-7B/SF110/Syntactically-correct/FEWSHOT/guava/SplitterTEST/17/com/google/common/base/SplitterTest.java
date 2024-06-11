package com.google.common.base;

import static org.junit.Assert.*;
import org.junit.Test;

public class SplitterTest {

    @Test
    public void testSplitterOnChar() {
        Splitter splitter = Splitter.on('-');
        Iterable<String> result = splitter.split("test-1,test-2,test-3");
        List<String> expected = Arrays.asList("test-1", "test-2", "test-3");
        assertEquals(expected, result);
    }

    @Test
    public void testSplitterOnEmptyString() {
        Splitter splitter = Splitter.on("");
        Iterable<String> result = splitter.split("test1,test2,test3");
        List<String> expected = Arrays.asList("test1", "", "test2", "", "test3");
        assertEquals(expected, result);
    }

    @Test
    public void testSplitterOnPattern() {
        String separatorPattern = "(?i)\\b[a-z]\\+\\b";
        Splitter splitter = Splitter.onPattern(separatorPattern);
        Iterable<String> result = splitter.split("foo_1A_bar_2B_qux_3C");
        List<String> expected = Arrays.asList("foo", "bar", "qux");
        assertEquals(expected, result);
    }

    @Test
    public void testSplitterOnFixedLength() {
        Splitter splitter = Splitter.fixedLength(3);
        Iterable<String> result = splitter.split("test123,test456,test789");
        List<String> expected = Arrays.asList("test123", "test456", "test789");
        assertEquals(expected, result);
    }

    @Test
    public void testSplitterOnStrategy() {
        Strategy customStrategy = (Splitter splitter, CharSequence charSequence) -> {
            Matcher matcher = Pattern.compile("(?i)\\b[a-z]+\\b").matcher(charSequence);
            return new SplittingIterator(splitter, charSequence) {
                @Override
                public int separatorStart(final int start) {
                    return matcher.find(start) ? matcher.start() : -1;
                }

                @Override
                public int separatorEnd(final int separatorPosition) {
                    return matcher.end();
                }
            };
        };
        Splitter splitter = new Splitter(customStrategy);
        Iterable<String> result = splitter.split("fooBar1Baz2Qux3FooBar");
        List<String> expected = Arrays.asList("foo", "Bar", "1", "Baz", "2", "Qux", "3", "FooBar");
        assertEquals(expected, result);
    }

    @Test
    public void testMapSplitter() {
        Splitter keySplitter = Splitter.on("-");
        Splitter valueSplitter = Splitter.on(":");
        MapSplitter mapSplitter = Splitter.withKeyValueSeparator(keySplitter).withKeyValueSeparator(valueSplitter);
        Iterable<Map.Entry<String, String>> result = mapSplitter.split("key1:value1-value2,key2:value3-value4");
        List<Map.Entry<String, String>> expected = Arrays.asList(
                Map.entry("key1", "value1"),
                Map.entry("key2", "value3")
        );
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSplitterNullSequence() {
        Splitter splitter = Splitter.on('-');
        splitter.split(null);
    }
}