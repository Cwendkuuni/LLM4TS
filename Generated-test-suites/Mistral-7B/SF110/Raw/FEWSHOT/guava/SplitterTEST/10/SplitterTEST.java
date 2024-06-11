package com.google.common.base;

import static org.junit.Assert.*;
import org.junit.Test;

public class SplitterTest {

    @Test
    public void testOnChar() {
        Splitter splitter = Splitter.on('-');
        Iterable<String> result = splitter.split("a-b-c-d");
        List<String> expected = Arrays.asList("a", "b", "c", "d");
        assertEquals(expected, result);
    }

    @Test
    public void testOnString() {
        Splitter splitter = Splitter.on("--");;
        Iterable<String> result = splitter.split("foo--bar--baz");
        List<String> expected = Arrays.asList("foo", "bar", "baz");
        assertEquals(expected, result);
    }

    @Test
    public void testOnPattern() {
        Splitter splitter = Splitter.on("[\\s]+");
        Iterable<String> result = splitter.split("foo bar baz");
        List<String> expected = Arrays.asList("foo", "bar", "baz");
        assertEquals(expected, result);
    }

    @Test
    public void testOnFixedLength() {
        Splitter splitter = Splitter.fixedLength(2);
        Iterable<String> result = splitter.split("12345678");
        List<String> expected = Arrays.asList("12", "34", "56", "78");
        assertEquals(expected, result);
    }

    @Test
    public void testOmittingEmptyStrings() {
        Splitter splitter = Splitter.on("\\s+").omitEmptyStrings();
        Iterable<String> result = splitter.split("foo bar   baz");
        List<String> expected = Arrays.asList("foo", "bar", "baz");
        assertEquals(expected, result);
    }

    @Test
    public void testLimitingResults() {
        Splitter splitter = Splitter.on("-").limit(3);
        Iterable<String> result = splitter.split("a-b-c-d-e-f-g-h-i");
        List<String> expected = Arrays.asList("a-b", "c-d", "e");
        assertEquals(expected, result);
    }

    @Test
    public void testTrimmingResults() {
        Splitter splitter = Splitter.on("\\s+").trimResults();
        Iterable<String> result = splitter.split("foo bar   baz");
        List<String> expected = Arrays.asList("foo", "bar", "baz");
        List<String> actual = new ArrayList<>();
        for (String s : result) {
            actual.add(s.trim());
        }
        assertEquals(expected, actual);
    }

    @Test
    public void testWithKeyValueSeparator() {
        Splitter splitter = Splitter.on("=").limit(2).trimResults();
        Map<String, String> result = new LinkedHashMap<>();
        result.putAll(splitter.withKeyValueSeparator("k1=v1=v2=v3=k2=v4").split("k1=v1=v2=v3=k2=v4"));
        Map<String, String> expected = new LinkedHashMap<>();
        expected.put("k1", "v1");
        expected.put("k2", "v4");
        assertEquals(expected, result);
    }
}