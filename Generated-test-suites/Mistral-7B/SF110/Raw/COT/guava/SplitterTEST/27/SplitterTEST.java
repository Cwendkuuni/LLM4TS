package com.example.junit; // replace with your test package name

import static org.junit.Assert.*;

import com.google.common.base.Splitter;
import com.google.common.base.Joiner;
import com.google.common.base.CharMatcher;
import com.google.common.collect.LinkedHashMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SplitterTest {

    @SuppressWarnings("unchecked")
    private static <T> Iterator<T> createMockIterator(final T[] array) {
        return new Iterator<T>() {
            int index = 0;

            @Override
            public boolean hasNext() {
                return index < array.length;
            }

            @Override
            public T next() {
                return array[index++];
            }
        };
    }

    @SuppressWarnings("unchecked")
    private static <T> Iterable<T> createMockIterable(final T[] array) {
        return new Iterable<T>() {
            @Override
            public Iterator<T> iterator() {
                return (Iterator<T>) createMockIterator(array);
            }
        };
    }

    @Test
    public void testOnCharSeparator() {
        CharMatcher separatorMatcher = CharMatcher.is('-');
        Splitter splitter = Splitter.on(separatorMatcher);

        CharSequence sequence = "test-this-out-test";
        Iterable<String> result = splitter.split(sequence);
        List<String> expected = Arrays.asList("test", "this", "out", "test");
        assertEquals(expected, result);
    }

    @Test
    public void testOnStringSeparator() {
        Splitter splitter = Splitter.on("/");

        CharSequence sequence = "test/this/out/test";
        Iterable<String> result = splitter.split(sequence);
        List<String> expected = Arrays.asList("test", "this", "out", "test");
        assertEquals(expected, result);
    }

    @Test
    public void testOnPatternSeparator() {
        Pattern separatorPattern = Pattern.compile("\\s+");
        Splitter splitter = Splitter.on(separatorPattern);

        CharSequence sequence = "test this out test";
        Iterable<String> result = splitter.split(sequence);
        List<String> expected = Arrays.asList("test", "this", "out", "test");
        assertEquals(expected, result);
    }

    @Test(expected = NullPointerException.class)
    public void testOnNullSeparator() {
        Splitter.on(null);
    }

    @Test
    public void testOnEmptyStringSeparator() {
        Splitter splitter = Splitter.on("");

        CharSequence sequence = "";
        Iterable<String> result = splitter.split(sequence);
        List<String> expected = Collections.emptyList();
        assertEquals(expected, result);
    }

    @Test
    public void testFixedLengthSplitter() {
        Splitter splitter = Splitter.fixedLength(2);

        CharSequence sequence = "test123test456test789";
        Iterable<String> result = splitter.split(sequence);
        List<String> expected = Arrays.asList("test1", "test2", "test3", "test4", "test5", "test6", "test7", "test8", "test9");
        assertEquals(expected, result);
    }

    @Test
    public void testLimitSplitter() {
        Splitter splitter = Splitter.on('/').limit(3);

        CharSequence sequence = "test/this/out/test/one/more/test";
        Iterable<String> result = splitter.split(sequence);
        List<String> expected = Arrays.asList("test", "this", "out", "test");
        assertEquals(expected, result);
    }

    @Test
    public void testOmitEmptyStringsSplitter() {
        Splitter splitter = Splitter.on("/").omitEmptyStrings();

        CharSequence sequence = "test/this/out/test//";
        Iterable<String> result = splitter.split(sequence);
        List<String> expected = Arrays.asList("test", "this", "out", "test");
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeLengthLimitSplitter() {
        Splitter splitter = Splitter.on("/").limit(-1);
    }

    @Test
    public void testMapSplitter() {
        Splitter keySplitter = Splitter.on(":");
        Splitter valueSplitter = Splitter.on("=");

        Splitter mapSplitter = Splitter.withKeyValueSeparator(keySplitter).withKeyValueSeparator(valueSplitter);

        CharSequence sequence = "key1:value1=value2:key2:value3=value4";
        Map<String, String> result = mapSplitter.split(sequence);
        Map<String, String> expected = new LinkedHashMap<String, String>() {{
            put("key1", "value1");
            put("key2", "value3");
        }};
        assertEquals(expected, result);
    }

    @Test(expected = NullPointerException.class)
    public void testNullSequence() {
        Splitter splitter = Splitter.on("/");
        splitter.split(null);
    }
}