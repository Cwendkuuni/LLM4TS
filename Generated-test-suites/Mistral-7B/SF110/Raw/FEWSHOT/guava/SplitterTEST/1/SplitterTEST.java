package com.google.common.base;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Joiner;
import com.google.common.base.Predicates;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.base.Splitter.MapSplitter;
import com.google.common.base.Splitter.MapSplitter.InvalidEntryException;
import com.google.common.base.Splitter.Strategy;
import com.google.common.collect.Iterators;
import com.google.common.collect.LinkedHashMap;

@GwtCompatible(emulated = true)
public class SplitterTest {

    @Beta
    @Test
    public void testSplitterWithCharSeparator() {
        final Splitter splitter = Splitter.on(':');
        final List<String> result = splitter.split("key1:value1, key2:value2");
        assertEquals(2, result.size());
        assertEquals("key1", result.get(0));
        assertEquals("value1", result.get(1));
    }

    @Beta
    @Test
    public void testSplitterWithStringSeparator() {
        final Splitter splitter = Splitter.on(":");
        final List<String> result = splitter.split("key1:value1, key2:value2");
        assertEquals(2, result.size());
        assertEquals("key1", result.get(0));
        assertEquals("value1", result.get(1));
    }

    @Beta
    @Test
    public void testSplitterWithPatternSeparator() {
        final Pattern pattern = Pattern.compile(":");
        final Splitter splitter = Splitter.on(pattern);
        final List<String> result = splitter.split("key1:value1, key2:value2");
        assertEquals(2, result.size());
        assertEquals("key1", result.get(0));
        assertEquals("value1", result.get(1));
    }

    @Beta
    @Test
    public void testSplitterWithEmptyStringAsSeparator() {
        final Splitter splitter = Splitter.on("");
        final List<String> result = splitter.split("key1, value1, key2, value2");
        assertEquals(4, result.size());
        assertEquals("key1", result.get(0));
        assertEquals("", result.get(1));
        assertEquals("value1", result.get(2));
        assertEquals("", result.get(3));
        assertEquals("key2", result.get(4));
    }

    @Beta
    @Test
    public void testSplitterWithLimit() {
        final Splitter splitter = Splitter.on(':').limit(2);
        final List<String> result = splitter.split("key1:value1, key2:value2, key3:value3");
        assertEquals(3, result.size());
        assertEquals("key1:value1", result.get(0));
        assertEquals("key2:value2", result.get(1));
    }

    @Beta
    @Test
    public void testSplitterWithOmittedEmptyStrings() {
        final Splitter splitter = Splitter.on(':').omitEmptyStrings();
        final List<String> result = splitter.split("key1:value1, , key3:value3");
        assertEquals(2, result.size());
        assertEquals("key1:value1", result.get(0));
        assertEquals("key3:value3", result.get(1));
    }

    @Beta
    @Test
    public void testSplitterWithTrimmedResults() {
        final CharMatcher trimmer = CharMatcher.WHITESPACE;
        final Splitter splitter = Splitter.on(':').trimResults(trimmer);
        final List<String> result = splitter.split(" key1:value1  ,  key2:value2 ");
        assertEquals(2, result.size());
        assertEquals("key1:value1", result.get(0));
        assertEquals("key2:value2", result.get(1));
    }

    @Beta
    @Test
    public void testMapSplitterWithKeyValueSeparator() {
        final Splitter keyValueSplitter = Splitter.on("=");
        final MapSplitter mapSplitter = Splitter.on('&').withKeyValueSeparator(keyValueSplitter);
        final String input = "name1=JohnDoe&age=30&city=London";
        final Map<String, String> result = mapSplitter.split(input);
        assertEquals(3, result.size());
        assertEquals("name1", result.keySet().iterator().next());
        assertEquals("JohnDoe", result.get("name1"));
        assertEquals("age", result.keySet().iterator().next());
        assertEquals("30", result.get("age"));
        assertEquals("city", result.keySet().iterator().next());
        assertEquals("London", result.get("city"));

        try {
            mapSplitter.split(input + "&name2=JohnDoe");
            fail("Expected InvalidEntryException.");
        } catch (InvalidEntryException e) {
            // Expected
        }
    }

    @Test
    public void testFixedLengthSplitter() {
        final Splitter splitter = Splitter.fixedLength(3);
        final List<String> result = splitter.split("abcdefghijklmnopqrstuvwxyz");
        assertEquals(8, result.size());
        assertEquals("abc", result.get(0));
        assertEquals("def", result.get(1));
        assertEquals("ghi", result.get(2));
        assertEquals("jkl", result.get(3));
        assertEquals("mno", result.get(4));
        assertEquals("pqr", result.get(5));
        assertEquals("stu", result.get(6));
        assertEquals("wxyz", result.get(7));
    }

    @Test
    public void testStrategyIteratorWithCharMatcherSeparator() {
        final CharMatcher separatorMatcher = CharMatcher.is(':');
        final Strategy strategy = new Strategy() {
            @Override
            public SplittingIterator iterator(final Splitter splitter, final CharSequence toSplit) {
                return new SplittingIterator(splitter, toSplit) {
                    @Override
                    int separatorStart(final int start) {
                        return separatorMatcher.indexIn(this.toSplit, start);
                    }

                    @Override
                    int separatorEnd(final int separatorPosition) {
                        return separatorPosition + 1;
                    }
                };
            }
        };
        final List<String> result = new Splitter(strategy).split("key1:value1, key2:value2");
        assertEquals(2, result.size());
        assertEquals("key1:value1", result.get(0));
        assertEquals("key2:value2", result.get(1));
    }

    @Test
    public void testStrategyIteratorWithRegexSeparator() {
        final Pattern separatorPattern = Pattern.compile(":");
        final Strategy strategy = new Strategy() {
            @Override
            public SplittingIterator iterator(final Splitter splitter, final CharSequence toSplit) {
                return new SplittingIterator(splitter, toSplit) {
                    final Matcher matcher = separatorPattern.matcher(toSplit);

                    @Override
                    int separatorStart(final int start) {
                        return matcher.find(start) ? matcher.start() : -1;
                    }

                    @Override
                    int separatorEnd(final int separatorPosition) {
                        return matcher.end();
                    }
                };
            }
        };
        final List<String> result = new Splitter(strategy).split("key1:value1, key2:value2");
        assertEquals(2, result.size());
        assertEquals("key1:value1", result.get(0));
        assertEquals("key2:value2", result.get(1));
    }
}