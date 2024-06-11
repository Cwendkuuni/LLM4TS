package com.google.common.base;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.StrictMath;
import com.google.common.collect.LinkedHashMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.UnmodifiableMap;
import com.google.common.primitives.Chars;
import com.google.common.primitives.Ints;
import com.google.common.util.CharMatcher;
import com.google.common.util.Preconditions;

@GwtCompatible(emulated = true)
public class SplitterTest {

    @Test
    public void testSplitterCreation() {
        Splitter splitter = Splitter.on('-');
        assertEquals(CharMatcher.is('-'), splitter.trimmer);
        assertEquals(false, splitter.omitEmptyStrings);
        assertEquals(Splitter.Strategy.class, splitter.strategy.getClass());
        assertEquals(Integer.MAX_VALUE, splitter.limit);

        Splitter stringSplitter = Splitter.on("--");
        assertEquals(CharMatcher.NONE, stringSplitter.trimmer);
        assertEquals(false, stringSplitter.omitEmptyStrings);
        assertEquals(Splitter.Strategy.class, stringSplitter.strategy.getClass());
        assertEquals(Integer.MAX_VALUE, stringSplitter.limit);

        Splitter patternSplitter = Splitter.onPattern("\\s+");
        assertEquals(CharMatcher.NONE, patternSplitter.trimmer);
        assertEquals(false, patternSplitter.omitEmptyStrings);
        assertEquals(SplitterPatternStrategy.class, patternSplitter.strategy.getClass());
    }

    @Test
    public void testOn() {
        Splitter splitter = Splitter.on('-');
        Iterator<String> iterator = splitter.split("--foo--bar--baz--").iterator();
        List<String> expected = Lists.newArrayList("foo", "bar", "baz");
        List<String> actual = Lists.newArrayList();

        while (iterator.hasNext()) {
            actual.add(iterator.next());
        }

        assertEquals(expected, actual);
    }

    @Test
    public void testOnWithTrimming() {
        Splitter trimmedSplitter = Splitter.on('-').trimResults();
        Iterator<String> iterator = trimmedSplitter.split("--foo--bar--baz--").iterator();
        List<String> expected = Lists.newArrayList("foo", "bar", "baz");
        List<String> actual = Lists.newArrayList();

        while (iterator.hasNext()) {
            actual.add(iterator.next());
        }

        assertEquals(expected, actual);
    }

    @Test
    public void testOnWithEmptyStringsOmit() {
        Splitter splitter = Splitter.on('-').omitEmptyStrings();
        Iterator<String> iterator = splitter.split("--foo--bar-----baz--").iterator();
        List<String> expected = Lists.newArrayList("foo", "bar", "baz");
        List<String> actual = Lists.newArrayList();

        while (iterator.hasNext()) {
            actual.add(iterator.next());
        }

        assertEquals(expected, actual);
    }

    @Test
    public void testOnWithLimit() {
        Splitter limitSplitter = Splitter.on('-').limit(2);
        Iterator<String> iterator = limitSplitter.split("--foo--bar--baz--").iterator();
        List<String> expected = Lists.newArrayList("foo", "bar");
        List<String> actual = Lists.newArrayList();

        while (iterator.hasNext()) {
            actual.add(iterator.next());
        }

        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOnWithInvalidSeparator() {
        Splitter splitter = Splitter.on(null);
    }

    @Test
    public void testOnPattern() {
        Splitter patternSplitter = Splitter.onPattern("\\s+");
        Iterator<String> iterator = patternSplitter.split("foo bar baz qux quux").iterator();
        List<String> expected = Lists.newArrayList("foo", "bar", "baz", "qux", "quux");
        List<String> actual = Lists.newArrayList();

        while (iterator.hasNext()) {
            actual.add(iterator.next());
        }

        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOnPatternWithEmptyPattern() {
        Splitter splitter = Splitter.onPattern("");
    }

    @Test(expected = IllegalStateException.class)
    public void testOnPatternWithMatchingEmptyString() {
        Splitter patternSplitter = Splitter.onPattern("\\s+");
        Iterator<String> iterator = patternSplitter.split("foo\n").iterator();
        iterator.next();
        String emptyString = iterator.next();

        assertTrue(emptyString.isEmpty());

        iterator.next(); // This should throw an exception
    }

    @Test
    public void testSplitToList() {
        Splitter splitter = Splitter.on('-');
        List<String> expected = Lists.newArrayList("foo", "bar", "baz");
        List<String> actual = splitter.splitToList("--foo--bar--baz--");

        assertEquals(expected, actual);
    }

    @Test
    public void testSplitToListWithTrimming() {
        Splitter trimmedSplitter = Splitter.on('-').trimResults();
        List<String> expected = Lists.newArrayList("foo", "bar", "baz");
        List<String> actual = trimmedSplitter.splitToList("--foo--bar--baz--");

        assertEquals(expected, actual);
    }

    @Test
    public void testSplitToListWithEmptyStringsOmit() {
        Splitter splitter = Splitter.on('-').omitEmptyStrings();
        List<String> expected = Lists.newArrayList("foo", "bar", "baz");
        List<String> actual = splitter.splitToList("--foo--bar-----baz--");

        assertEquals(expected, actual);
    }

    @Test
    public void testSplitToListWithLimit() {
        Splitter limitSplitter = Splitter.on('-').limit(2);
        List<String> expected = Lists.newArrayList("foo", "bar");
        List<String> actual = limitSplitter.splitToList("--foo--bar--baz--");

        assertEquals(expected, actual);
    }

    @Test
    public void testMapSplitter() {
        Splitter keyValueSplitter = Splitter.on(":");
        Map<String, String> expected = Maps.newLinkedHashMap();
        expected.put("foo:bar", "bar");
        expected.put("baz:qux", "qux");

        Map<String, String> actual = Splitter.MapSplitter.withKeyValueSeparator(Splitter.on(":")).split(
                "foo:bar:baz:qux").map(entry -> String.join("=", Arrays.asList(entry[0], entry[1]))).collect(Collectors.toMap(String::substringBefore, String::substringAfter));

        assertEquals(expected, actual);
    }

    @Test
    public void testMapSplitterWithEmptyEntries() {
        Splitter keyValueSplitter = Splitter.on(":");
        Map<String, String> expected = Maps.newLinkedHashMap();
        expected.put("foo:bar", "bar");
        expected.put("baz:", null);
        expected.put(":qux", "qux");

        Map<String, String> actual = Splitter.MapSplitter.withKeyValueSeparator(Splitter.on(":")).split(
                "foo:bar:baz::qux").map(entry -> String.join("=", Arrays.asList(entry[0], entry[1]))).collect(Collectors.toMap(String::substringBefore, String::substringAfter));

        assertEquals(expected, actual);
    }

}