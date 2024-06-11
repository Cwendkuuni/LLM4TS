package com.google.common.base;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;
import org.junit.Test;

import com.google.common.base.Splitter;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

public class SplitterTest {

    @Test
    public void testSplit_onChar_emptyString() {
        Splitter splitter = Splitter.on(' ');
        CharSequence sequence = "";
        List<String> result = splitter.split(sequence);
        assertEquals(Lists.newArrayList(), result);
    }

    @Test
    public void testSplit_onChar_singleElement() {
        Splitter splitter = Splitter.on(' ');
        CharSequence sequence = "test";
        List<String> result = splitter.split(sequence);
        assertEquals(Lists.newArrayList("test"), result);
    }

    @Test
    public void testSplit_onChar_multipleElements() {
        Splitter splitter = Splitter.on(' ');
        CharSequence sequence = "this is a test";
        List<String> result = splitter.split(sequence);
        assertEquals(Lists.newArrayList("this", "is", "a", "test"), result);
    }

    @Test
    public void testSplit_onChar_omitEmptyStrings() {
        Splitter splitter = Splitter.on(' ').omitEmptyStrings();
        CharSequence sequence = "this   is   a   test";
        List<String> result = splitter.split(sequence);
        assertEquals(Lists.newArrayList("this", "is", "a", "test"), result);
    }

    @Test
    public void testSplit_onString_emptyString() {
        Splitter splitter = Splitter.on("--");
        CharSequence sequence = "";
        List<String> result = splitter.split(sequence);
        assertEquals(Lists.newArrayList(), result);
    }

    @Test
    public void testSplit_onString_singleElement() {
        Splitter splitter = Splitter.on("--");
        CharSequence sequence = "--test--";
        List<String> result = splitter.split(sequence);
        assertEquals(Lists.newArrayList("test"), result);
    }

    @Test
    public void testSplit_onString_multipleElements() {
        Splitter splitter = Splitter.on("--");
        CharSequence sequence = "--this--is--a--test--";
        List<String> result = splitter.split(sequence);
        assertEquals(Lists.newArrayList("this", "is", "a", "test"), result);
    }

    @Test
    public void testSplit_onPattern_emptyString() {
        Splitter splitter = Splitter.onPattern("\\s+");
        CharSequence sequence = "";
        List<String> result = splitter.split(sequence);
        assertEquals(Lists.newArrayList(), result);
    }

    @Test
    public void testSplit_onPattern_singleElement() {
        Splitter splitter = Splitter.onPattern("\\s+");
        CharSequence sequence = "test";
        List<String> result = splitter.split(sequence);
        assertEquals(Lists.newArrayList("test"), result);
    }

    @Test
    public void testSplit_onPattern_multipleElements() {
        Splitter splitter = Splitter.onPattern("\\s+");
        CharSequence sequence = "this is a test";
        List<String> result = splitter.split(sequence);
        assertEquals(Lists.newArrayList("this", "is", "a", "test"), result);
    }

    @Test
    public void testSplit_onPattern_omitEmptyStrings() {
        Splitter splitter = Splitter.onPattern("\\s+").omitEmptyStrings();
        CharSequence sequence = "this   is   a   test";
        List<String> result = splitter.split(sequence);
        assertEquals(Lists.newArrayList("this", "is", "a", "test"), result);
    }

    @Test
    public void testSplitToList_onChar_emptyString() {
        Splitter splitter = Splitter.on(' ');
        CharSequence sequence = "";
        List<String> result = splitter.splitToList(sequence);
        assertEquals(Lists.newArrayList(), result);
    }

    @Test
    public void testSplitToList_onChar_singleElement() {
        Splitter splitter = Splitter.on(' ');
        CharSequence sequence = "test";
        List<String> result = splitter.splitToList(sequence);
        assertEquals(Lists.newArrayList("test"), result);
    }

    @Test
    public void testSplitToList_onChar_multipleElements() {
        Splitter splitter = Splitter.on(' ');
        CharSequence sequence = "this is a test";
        List<String> result = splitter.splitToList(sequence);
        assertEquals(Lists.newArrayList("this", "is", "a", "test"), result);
    }

    @Test
    public void testSplitToList_onChar_omitEmptyStrings() {
        Splitter splitter = Splitter.on(' ').omitEmptyStrings();
        CharSequence sequence = "this   is   a   test";
        List<String> result = splitter.splitToList(sequence);
        assertEquals(Lists.newArrayList("this", "is", "a", "test"), result);
    }

    @Test
    public void testSplitToList_onString_emptyString() {
        Splitter splitter = Splitter.on("--");
        CharSequence sequence = "";
        List<String> result = splitter.splitToList(sequence);
        assertEquals(Lists.newArrayList(), result);
    }

    @Test
    public void testSplitToList_onString_singleElement() {
        Splitter splitter = Splitter.on("--");
        CharSequence sequence = "--test--";
        List<String> result = splitter.splitToList(sequence);
        assertEquals(Lists.newArrayList("test"), result);
    }

    @Test
    public void testSplitToList_onString_multipleElements() {
        Splitter splitter = Splitter.on("--");
        CharSequence sequence = "--this--is--a--test--";
        List<String> result = splitter.splitToList(sequence);
        assertEquals(Lists.newArrayList("this", "is", "a", "test"), result);
    }

    @Test
    public void testSplitToList_onPattern_emptyString() {
        Splitter splitter = Splitter.onPattern("\\s+");
        CharSequence sequence = "";
        List<String> result = splitter.splitToList(sequence);
        assertEquals(Lists.newArrayList(), result);
    }

    @Test
    public void testSplitToList_onPattern_singleElement() {
        Splitter splitter = Splitter.onPattern("\\s+");
        CharSequence sequence = "test";
        List<String> result = splitter.splitToList(sequence);
        assertEquals(Lists.newArrayList("test"), result);
    }

    @Test
    public void testSplitToList_onPattern_multipleElements() {
        Splitter splitter = Splitter.onPattern("\\s+");
        CharSequence sequence = "this is a test";
        List<String> result = splitter.splitToList(sequence);
        assertEquals(Lists.newArrayList("this", "is", "a", "test"), result);
    }

    @Test
    public void testSplitToList_onPattern_omitEmptyStrings() {
        Splitter splitter = Splitter.onPattern("\\s+").omitEmptyStrings();
        CharSequence sequence = "this   is   a   test";
        List<String> result = splitter.splitToList(sequence);
        assertEquals(Lists.newArrayList("this", "is", "a", "test"), result);
    }

    @Test
    public void testMapSplitter_singleKeyValuePair() {
        Splitter splitter = Splitter.on(':');
        Map<String, String> map = splitter.withKeyValueSeparator("=").split(new StringBuilder("key=value").toString());
        assertEquals(Maps.newLinkedHashMap(Map.of("key", "value")), map);
    }

    @Test
    public void testMapSplitter_multipleKeyValuePairs() {
        Splitter splitter = Splitter.on(':');
        Map<String, String> map = splitter.withKeyValueSeparator("=").split(new StringBuilder("key1=value1:key2=value2").toString());
        assertEquals(Maps.newLinkedHashMap(Map.of("key1", "value1", "key2", "value2")), map);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapSplitter_invalidEntry() {
        Splitter splitter = Splitter.on(':');
        Map<String, String> map = splitter.withKeyValueSeparator("=").split(new StringBuilder("key:value").toString());
        fail("Invalid entry found in map");
    }
}