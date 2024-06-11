package com.google.common.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Splitter.MapSplitter;
import com.google.common.base.Splitter.SplittingIterator;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SplitterTest {

    @Test
    public void testOnChar() {
        Splitter splitter = Splitter.on('-');
        List<String> split = splitter.split("a--b--c");
        assertEquals(Arrays.asList("a", "", "b", "", "c"), split);
    }

    @Test
    public void testOnCharEmpty() {
        Splitter splitter = Splitter.on('-');
        List<String> split = splitter.split("");
        assertTrue(Collections.isEmpty(split));
    }

    @Test
    public void testOnCharNull() {
        try {
            Splitter splitter = Splitter.on((char) 0);
            List<String> split = splitter.split("test");
            fail();
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testOnPattern() {
        Splitter splitter = Splitter.onPattern("[\\s]+");
        List<String> split = splitter.split("a  b  c");
        assertEquals(Arrays.asList("a", "b", "c"), split);
    }

    @Test
    public void testOnPatternEmpty() {
        Splitter splitter = Splitter.onPattern("[\\s]+");
        List<String> split = splitter.split("");
        assertTrue(Collections.isEmpty(split));
    }

    @Test
    public void testOnPatternNull() {
        try {
            Splitter splitter = Splitter.onPattern(null);
            List<String> split = splitter.split("test");
            fail();
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testOnRegex() {
        Splitter splitter = Splitter.on("[\\s]+");
        List<String> split = splitter.split("a b c");
        assertEquals(Arrays.asList("a", "b", "c"), split);
    }

    @Test
    public void testOnRegexEmpty() {
        Splitter splitter = Splitter.on("[\\s]+");
        List<String> split = splitter.split("");
        assertTrue(Collections.isEmpty(split));
    }

    @Test
    public void testOnRegexNull() {
        try {
            Splitter splitter = Splitter.on(null);
            List<String> split = splitter.split("test");
            fail();
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testFixedLength() {
        Splitter splitter = Splitter.fixedLength(3);
        List<String> split = splitter.split("12345");
        assertEquals(Arrays.asList("123", "45"), split);
    }

    @Test
    public void testFixedLengthEmpty() {
        Splitter splitter = Splitter.fixedLength(3);
        List<String> split = splitter.split("");
        assertTrue(Collections.isEmpty(split));
    }

    @Test
    public void testFixedLengthNull() {
        try {
            Splitter splitter = Splitter.fixedLength(0);
            List<String> split = splitter.split("test");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testOmitEmptyStrings() {
        Splitter splitter = Splitter.on('-').omitEmptyStrings();
        List<String> split = splitter.split("a--b--c--");
        assertEquals(Arrays.asList("a", "b", "c"), split);
    }

    @Test
    public void testOmitEmptyStringsEmpty() {
        Splitter splitter = Splitter.on('-').omitEmptyStrings();
        List<String> split = splitter.split("");
        assertTrue(Collections.isEmpty(split));
    }

    @Test
    public void testLimit() {
        Splitter splitter = Splitter.on('-').limit(2);
        List<String> split = splitter.split("a--b--c--d--e--");
        assertEquals(Arrays.asList("a", "b"), split);
    }

    @Test
    public void testLimitEmpty() {
        Splitter splitter = Splitter.on('-').limit(2);
        List<String> split = splitter.split("");
        assertTrue(Collections.isEmpty(split));
    }

    @Test
    public void testLimitNegative() {
        Splitter splitter = Splitter.on('-').limit(-1);
        List<String> split = splitter.split("a--b--c--d--e--");
        assertEquals(Arrays.asList("a", "b", "c", "d", "e"), split);
    }

    @Test
    public void testTrimResults() {
        Splitter splitter = Splitter.on('-').trimResults();
        List<String> split = splitter.split("a--b--c--");
        assertEquals(Arrays.asList("a", "b", "c"), split);
    }

    @Test
    public void testTrimResultsEmpty() {
        Splitter splitter = Splitter.on('-').trimResults();
        List<String> split = splitter.split("");
        assertTrue(Collections.isEmpty(split));
    }

    @Test
    public void testWithKeyValueSeparator() {
        Map<String, String> map = Splitter.on(':').withKeyValueSeparator(Splitter.on('=')).split("key1=value1:key2=value2:key3=value3");
        assertEquals(Maps.newHashMap(Arrays.asList(new AbstractMap.SimpleEntry<>("key1", "value1"), new AbstractMap.SimpleEntry<>("key2", "value2"), new AbstractMap.SimpleEntry<>("key3", "value3"))), map);
    }

    @Test
    public void testWithKeyValueSeparatorEmpty() {
        Map<String, String> map = Splitter.on(':').withKeyValueSeparator(Splitter.on('=')).split("");
        assertTrue(Collections.isEmpty(map));
    }

    @Test
    public void testWithKeyValueSeparatorNull() {
        try {
            Map<String, String> map = Splitter.on(':').withKeyValueSeparator(null).split("key1=value1:key2=value2:key3=value3");
            fail();
        } catch (NullPointerException e) {
            // expected
        }
    }
}