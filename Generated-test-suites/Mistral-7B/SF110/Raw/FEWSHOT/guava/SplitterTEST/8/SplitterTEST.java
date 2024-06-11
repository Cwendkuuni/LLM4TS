package com.google.common.base;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class SplitterTest {

    @Test
    public void testSplitterCreation() {
        Splitter splitter = Splitter.on('-');
        Splitter splitterWithPattern = Splitter.onPattern("--|-");
        Splitter splitterWithCharMatcher = Splitter.on(CharMatcher.is('_'));
        Splitter splitterWithFixedLength = Splitter.fixedLength(3);

        assertEquals(Splitter.Strategy.class, splitter.getStrategy().getClass());
        assertEquals(Splitter.Strategy.class, splitterWithPattern.getStrategy().getClass());
        assertEquals(Splitter.Strategy.class, splitterWithCharMatcher.getStrategy().getClass());
        assertEquals(Splitter.FixedLengthStrategy.class, splitterWithFixedLength.getStrategy().getClass());
    }

    @Test
    public void testIteratorNext() {
        Splitter splitter = Splitter.on('-');
        CharSequence testSequence = "a-b--c-d-e--f";

        Iterator<String> iterator = splitter.iterator(splitter, testSequence);

        assertEquals("a", iterator.next());
        assertEquals("b", iterator.next());
        assertEquals("c", iterator.next());
        assertEquals("d", iterator.next());
        assertEquals("e", iterator.next());
        assertEquals("f", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorHasNext() {
        Splitter splitter = Splitter.on('-');
        CharSequence testSequence = "a--b--c--d--e--";

        Iterator<String> iterator = splitter.iterator(splitter, testSequence);

        assertTrue(iterator.hasNext());
        assertTrue(iterator.hasNext());
        assertTrue(iterator.hasNext());
        assertTrue(iterator.hasNext());
        assertTrue(iterator.hasNext());
        assertTrue(iterator.hasNext());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testEmptySequence() {
        Splitter splitter = Splitter.on('-');
        CharSequence testSequence = "";

        Iterator<String> iterator = splitter.iterator(splitter, testSequence);

        assertFalse(iterator.hasNext());
    }

    @Test
    public void testNullSequence() {
        Splitter splitter = Splitter.on('-');

        assertNull(splitter.iterator(splitter, null));
    }

    @Test
    public void testSplitterWithCharMatcher() {
        Splitter splitter = Splitter.on(CharMatcher.is('_'));
        CharSequence testSequence = "_foo__bar__baz";

        Iterator<String> iterator = splitter.iterator(splitter, testSequence);

        assertEquals("foo", iterator.next());
        assertEquals("bar", iterator.next());
        assertEquals("baz", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testSplitterWithPattern() {
        Splitter splitter = Splitter.onPattern("--|-");
        CharSequence testSequence = "--foo--bar--baz--";

        Iterator<String> iterator = splitter.iterator(splitter, testSequence);

        assertEquals("foo", iterator.next());
        assertEquals("bar", iterator.next());
        assertEquals("baz", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testSplitterWithFixedLength() {
        Splitter splitter = Splitter.fixedLength(3);
        CharSequence testSequence = "12345678";

        Iterator<String> iterator = splitter.iterator(splitter, testSequence);

        assertEquals("123", iterator.next());
        assertEquals("456", iterator.next());
        assertEquals("78", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testSplitterWithEmptyStrings() {
        Splitter splitter = Splitter.on('-').omitEmptyStrings();
        CharSequence testSequence = "a-b--c-d---e";

        Iterator<String> iterator = splitter.iterator(splitter, testSequence);

        assertEquals("a", iterator.next());
        assertEquals("b", iterator.next());
        assertEquals("c", iterator.next());
        assertEquals("d", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testSplitterWithLimit() {
        Splitter splitter = Splitter.on('-').limit(3);
        CharSequence testSequence = "a-b--c-d---e";

        Iterator<String> iterator = splitter.iterator(splitter, testSequence);

        assertEquals("a", iterator.next());
        assertEquals("b", iterator.next());
        assertEquals("c", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testSplitterWithTrimming() {
        Splitter splitter = Splitter.on('-').trimResults();
        CharSequence testSequence = " a--b--  c--d--  e ";

        List<String> result = new ArrayList<String>();
        for (String s : splitter.split(testSequence)) {
            result.add(s);
        }

        assertArrayEquals(new String[]{"a", "b", "c", "d"}, result.toArray());
    }

    @Test
    public void testSplitterWithKeyValueSeparator() {
        Splitter keyValueSplitter = Splitter.on('=').limit(2);
        Splitter splitter = Splitter.on('-').withKeyValueSeparator(keyValueSplitter);
        CharSequence testSequence = "key1=value1-key2=value2-key3=value3";

        Map<String, String> result = splitter.split(testSequence);

        assertEquals(3, result.size());
        assertEquals("key1", result.keySet().iterator().next());
        assertEquals("value1", result.get("key1"));
        assertEquals("key2", result.keySet().iterator().next());
        assertEquals("value2", result.get("key2"));
        assertEquals("key3", result.keySet().iterator().next());
        assertEquals("value3", result.get("key3"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSplitterWithInvalidPattern() {
        Splitter splitter = Splitter.onPattern("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSplitterWithEmptySequence() {
        Splitter splitter = Splitter.on('-');
        splitter.split("");
    }
}