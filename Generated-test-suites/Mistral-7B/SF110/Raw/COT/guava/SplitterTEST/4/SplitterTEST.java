package com.example.splitter; // Replace "com.example" with your package name

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SplitterTest {

    private Splitter stringSeparator;
    private Splitter charSeparator;
    private Splitter regexSeparator;
    private Splitter fixedLengthSeparator;
    private Splitter mapSeparator;

    @Before
    public void setUp() {
        stringSeparator = Splitter.on(":");
        charSeparator = Splitter.on('#');
        regexSeparator = Splitter.onPattern("\\s+");
        fixedLengthSeparator = Splitter.fixedLength(2);
        mapSeparator = Splitter.on("=");
    }

    @Test
    public void testStringSeparator_simple() {
        List<String> result = stringSeparator.split("a:b:c:d");
        List<String> expected = Lists.newArrayList("a", "b", "c", "d");
        assertArrayEquals(expected.toArray(), result.toArray());
    }

    @Test
    public void testStringSeparator_emptyString() {
        List<String> result = stringSeparator.split("");
        assertTrue(result.isEmpty());
    }

    @Test
    public void testCharSeparator_simple() {
        List<String> result = charSeparator.split("a#b#c#d");
        List<String> expected = Lists.newArrayList("a", "", "b", "", "c", "", "d");
        assertArrayEquals(expected.toArray(), result.toArray());
    }

    @Test
    public void testCharSeparator_singleCharacter() {
        List<String> result = charSeparator.split("a#");
        List<String> expected = Lists.newArrayList("a");
        assertArrayEquals(expected.toArray(), result.toArray());
    }

    @Test
    public void testRegexSeparator_simple() {
        List<String> result = regexSeparator.split("a  b  c  d");
        List<String> expected = Lists.newArrayList("a", "b", "c", "d");
        assertArrayEquals(expected.toArray(), result.toArray());
    }

    @Test
    public void testRegexSeparator_emptyString() {
        List<String> result = regexSeparator.split("");
        assertTrue(result.isEmpty());
    }

    @Test
    public void testFixedLengthSeparator_simple() {
        List<String> result = fixedLengthSeparator.split("abcdabcdabcd");
        List<String> expected = Lists.newArrayList("ab", "cd");
        assertArrayEquals(expected.toArray(), result.toArray());
    }

    @Test
    public void testFixedLengthSeparator_singleCharacter() {
        List<String> result = fixedLengthSeparator.split("a");
        List<String> expected = Lists.newArrayList("a");
        assertArrayEquals(expected.toArray(), result.toArray());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFixedLengthSeparator_negativeLength() {
        fixedLengthSeparator.limit(-1);
    }

    @Test
    public void testMapSeparator_simple() {
        Map<String, String> result = mapSeparator.split("key1=value1&key2=value2");
        Map<String, String> expected = Maps.newLinkedHashMap();
        expected.put("key1", "value1");
        expected.put("key2", "value2");
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapSeparator_invalidEntry() {
        Map<String, String> result = mapSeparator.split("key1=value1&");
        assertNull(result);
    }
}