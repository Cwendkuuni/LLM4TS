package com.google.common.base;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Splitter.MapSplitter;
import com.google.common.base.Splitter.MapSplitter.InvalidEntryException;
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
    public void testOn() {
        Splitter splitter = Splitter.on('-');
        CharSequence testString = "item1-item2-item3";
        Iterator<String> iterator = splitter.split(testString).iterator();
        List<String> expectedResult = Lists.newArrayList("item1", "item2", "item3");
        List<String> result = new ArrayList<>();
        while (iterator.hasNext()) {
            result.add(iterator.next());
        }
        assertArrayEquals(expectedResult.toArray(), result.toArray());
    }

    @Test
    public void testOnWithEmptyString() {
        Splitter splitter = Splitter.on("--");
        CharSequence testString = "item1--item2--item3";
        Iterator<String> iterator = splitter.split(testString).iterator();
        List<String> expectedResult = Lists.newArrayList("item1", "", "item2", "", "item3");
        List<String> result = new ArrayList<>();
        while (iterator.hasNext()) {
            result.add(iterator.next());
        }
        assertArrayEquals(expectedResult.toArray(), result.toArray());
    }

    @Test
    public void testOnWithCustomSeparatorMatcher() {
        Splitter splitter = Splitter.on(Pattern.compile("--"));
        CharSequence testString = "item1--item2--item3";
        Iterator<String> iterator = splitter.split(testString).iterator();
        List<String> expectedResult = Lists.newArrayList("item1", "", "item2", "", "item3");
        List<String> result = new ArrayList<>();
        while (iterator.hasNext()) {
            result.add(iterator.next());
        }
        assertArrayEquals(expectedResult.toArray(), result.toArray());
    }

    @Test
    public void testOnWithCustomSeparatorPattern() {
        Splitter splitter = Splitter.onPattern("--");
        CharSequence testString = "item1--item2--item3";
        Iterator<String> iterator = splitter.split(testString).iterator();
        List<String> expectedResult = Lists.newArrayList("item1", "", "item2", "", "item3");
        List<String> result = new ArrayList<>();
        while (iterator.hasNext()) {
            result.add(iterator.next());
        }
        assertArrayEquals(expectedResult.toArray(), result.toArray());
    }

    @Test
    public void testOnWithFixedLengthSeparator() {
        Splitter splitter = Splitter.fixedLength(3);
        CharSequence testString = "item123item234item345";
        Iterator<String> iterator = splitter.split(testString).iterator();
        List<String> expectedResult = Lists.newArrayList("item123", "item234", "item345");
        List<String> result = new ArrayList<>();
        while (iterator.hasNext()) {
            result.add(iterator.next());
        }
        assertArrayEquals(expectedResult.toArray(), result.toArray());
    }

    @Test
    public void testOnWithEmptyInputSequence() {
        Splitter splitter = Splitter.on('-');
        CharSequence testString = "";
        Iterator<String> iterator = splitter.split(testString).iterator();
        assertFalse(iterator.hasNext());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOnWithNullSeparator() {
        Splitter.on((CharSequence) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOnWithEmptySeparator() {
        Splitter.on("");
    }

    @Test
    public void testOmitEmptyStrings() {
        Splitter splitter = Splitter.on('-').omitEmptyStrings();
        CharSequence testString = "item1--item2--item3--";
        Iterator<String> iterator = splitter.split(testString).iterator();
        List<String> expectedResult = Lists.newArrayList("item1", "item2", "item3");
        List<String> result = new ArrayList<>();
        while (iterator.hasNext()) {
            result.add(iterator.next());
        }
        assertArrayEquals(expectedResult.toArray(), result.toArray());
    }

    @Test
    public void testLimit() {
        Splitter splitter = Splitter.on('-').limit(2);
        CharSequence testString = "item1--item2--item3--item4--item5";
        Iterator<String> iterator = splitter.split(testString).iterator();
        List<String> expectedResult = Lists.newArrayList("item1--item2", "item3");
        List<String> result = new ArrayList<>();
        while (iterator.hasNext()) {
            result.add(iterator.next());
        }
        assertArrayEquals(expectedResult.toArray(), result.toArray());
    }

    @Test
    public void testTrimResults() {
        Splitter splitter = Splitter.on('-').trimResults();
        CharSequence testString = " item1--item2--item3 ";
        Iterator<String> iterator = splitter.split(testString).iterator();
        List<String> expectedResult = Lists.newArrayList("item1", "item2", "item3");
        List<String> result = new ArrayList<>();
        while (iterator.hasNext()) {
            result.add(iterator.next());
        }
        assertArrayEquals(expectedResult.toArray(), result.toArray());
    }

    @Test
    public void testTrimResultsWithCustomTrimmer() {
        Splitter splitter = Splitter.on('-').trimResults(CharMatcher.WHITESPACE);
        CharSequence testString = "\t item1--item2--item3 \n ";
        Iterator<String> iterator = splitter.split(testString).iterator();
        List<String> expectedResult = Lists.newArrayList("item1", "item2", "item3");
        List<String> result = new ArrayList<>();
        while (iterator.hasNext()) {
            result.add(iterator.next());
        }
        assertArrayEquals(expectedResult.toArray(), result.toArray());
    }

    @Test
    public void testMapSplitter() {
        Splitter keySplitter = Splitter.on('=');
        Splitter valueSplitter = Splitter.on(',');
        MapSplitter mapSplitter = Splitter.withKeyValueSeparator("=").limit(1).trimResults().withKeyValueSeparator(keySplitter).withValueSeparator(valueSplitter);
        CharSequence testString = "key1=value1,key2=value2,key3=value3";
        Map<String, String> expectedResult = Maps.newLinkedHashMap();
        expectedResult.put("key1", "value1");
        expectedResult.put("key2", "value2");
        expectedResult.put("key3", "value3");
        Map<String, String> result = mapSplitter.split(testString);
        assertArrayEquals(expectedResult.keySet().toArray(), result.keySet().toArray());
        assertArrayEquals(expectedResult.values().toArray(), result.values().toArray());
    }

    @Test(expected = InvalidEntryException.class)
    public void testMapSplitterInvalidEntry() {
        Splitter keySplitter = Splitter.on('=');
        Splitter valueSplitter = Splitter.on(',');
        MapSplitter mapSplitter = Splitter.withKeyValueSeparator("=").limit(1).trimResults().withKeyValueSeparator(keySplitter).withValueSeparator(valueSplitter);
        CharSequence testString = "key1=value1,key2=value2,key3=";
        mapSplitter.split(testString);
        fail("Expected InvalidEntryException");
    }
}