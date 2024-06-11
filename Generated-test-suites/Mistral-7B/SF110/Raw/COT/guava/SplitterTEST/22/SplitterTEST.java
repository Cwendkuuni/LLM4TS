package com.example.junit; // replace with your test package name

import com.google.common.base.Splitter;
import org.junit.Test;
import static org.junit.Assert.*;

public class SplitterTest {

    @Test
    public void testOnCharSeparator() {
        Splitter splitter = Splitter.on('-');
        CharSequence sequence = "a-b-c-d-e";
        Iterable<String> result = splitter.split(sequence);
        List<String> expected = new ArrayList<>();
        expected.add("a");
        expected.add("b");
        expected.add("c");
        expected.add("d");
        expected.add("e");
        assertEquals(expected, result);
    }

    @Test
    public void testOnCharMatcherSeparator() {
        Splitter splitter = Splitter.on(CharMatcher.is('_'));
        CharSequence sequence = "a_b_c_d_e";
        Iterable<String> result = splitter.split(sequence);
        List<String> expected = new ArrayList<>();
        expected.add("a");
        expected.add("b");
        expected.add("c");
        expected.add("d");
        expected.add("e");
        assertEquals(expected, result);
    }

    @Test
    public void testOnStringSeparator() {
        Splitter splitter = Splitter.on(":");
        CharSequence sequence = "a:b:c:d:e";
        Iterable<String> result = splitter.split(sequence);
        List<String> expected = new ArrayList<>();
        expected.add("a");
        expected.add("b");
        expected.add("c");
        expected.add("d");
        expected.add("e");
        assertEquals(expected, result);
    }

    @Test
    public void testOnPatternSeparator() {
        Splitter splitter = Splitter.on(Pattern.compile("\\:"));
        CharSequence sequence = "a:b:c:d:e";
        Iterable<String> result = splitter.split(sequence);
        List<String> expected = new ArrayList<>();
        expected.add("a");
        expected.add("b");
        expected.add("c");
        expected.add("d");
        expected.add("e");
        assertEquals(expected, result);
    }

    @Test
    public void testFixedLengthSplitter() {
        Splitter splitter = Splitter.fixedLength(3);
        CharSequence sequence = "abcdefghijklmnopqrstuvwxyz";
        Iterable<String> result = splitter.split(sequence);
        List<String> expected = new ArrayList<>();
        expected.add("abc");
        expected.add("def");
        expected.add("ghi");
        expected.add("jkl");
        expected.add("mno");
        expected.add("pqr");
        expected.add("stu");
        expected.add("vwxy");
        expected.add("z");
        assertEquals(expected, result);
    }

    @Test
    public void testSplitWithKeyValueSeparator() {
        Splitter keyValueSplitter = Splitter.on("=");
        Splitter outerSplitter = Splitter.on('&');
        MapSplitter mapSplitter = outerSplitter.withKeyValueSeparator(keyValueSplitter);
        CharSequence sequence = "key1=value1&key2=value2&key3=value3";
        Map<String, String> result = mapSplitter.split(sequence);
        assertEquals(3, result.size());
        assertEquals("key1", result.keySet().iterator().next());
        assertEquals("value1", result.get("key1"));
        assertEquals("key2", result.keySet().iterator().next());
        assertEquals("value2", result.get("key2"));
        assertEquals("key3", result.keySet().iterator().next());
        assertEquals("value3", result.get("key3"));
    }

    @Test(expected = NullPointerException.class)
    public void testNullSequence() {
        Splitter splitter = Splitter.on('-');
        splitter.split(null);
    }
}