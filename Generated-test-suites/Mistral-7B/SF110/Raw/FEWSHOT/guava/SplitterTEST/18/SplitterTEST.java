package org.apache.commons.cli; // Replace with the actual package name of the Splitter class if it is different.

import static org.junit.Assert.*;
import org.junit.Test;

public class SplitterTest {

    @Test
    public void testSplitSimple() {
        Splitter splitter = Splitter.on('-');
        String input = "item1-item2-item3";
        List<String> result = splitter.splitToList(input);
        assertEquals(Arrays.asList("item1", "item2", "item3"), result);
    }

    @Test
    public void testSplitEmptyString() {
        Splitter splitter = Splitter.on('-');
        List<String> result = splitter.splitToList("");
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testSplitNullString() {
        Splitter splitter = Splitter.on('-');
        List<String> result = splitter.splitToList(null);
        assertNull(result);
    }

    @Test
    public void testSplitWithOmittedEmptyStrings() {
        Splitter splitter = Splitter.on('-').omitEmptyStrings();
        String input = "item1--item2--item3--";
        List<String> result = splitter.splitToList(input);
        assertEquals(Arrays.asList("item1", "item2", "item3"), result);
    }

    @Test
    public void testSplitWithLimit() {
        Splitter splitter = Splitter.on('-').limit(2);
        String input = "item1-item2-item3-item4-item5";
        List<String> result = splitter.splitToList(input);
        assertEquals(Arrays.asList("item1", "item2"), result);
    }

    @Test
    public void testSplitWithCustomSeparator() {
        Splitter splitter = Splitter.onPattern("\\s+");
        String input = "item1 item2 item3";
        List<String> result = splitter.splitToList(input);
        assertEquals(Arrays.asList("item1", "item2", "item3"), result);
    }

    @Test
    public void testSplitWithCustomSeparatorEmptyString() {
        Splitter splitter = Splitter.onPattern("\\s+");
        List<String> result = splitter.splitToList("");
        assertEquals(Collections.emptyList(), result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSplitWithInvalidPattern() {
        Splitter splitter = Splitter.onPattern("[a-z]+"); // This pattern matches a sequence of one or more lowercase letters, which is not a valid separator.
        String input = "item1,item2,item3";
        List<String> result = splitter.splitToList(input);
    }

    @Test
    public void testSplitWithMapKeyValueSeparator() {
        Splitter keySplitter = Splitter.on(':');
        Splitter valueSplitter = Splitter.fixedLength(2);
        MapSplitter mapSplitter = Splitter.onPattern("\\s+").withKeyValueSeparator(keySplitter, valueSplitter);
        String input = "key1:value1:key2:value2";
        Map<String, String> result = mapSplitter.split(input);
        assertEquals(Arrays.asList("key1", "value1"), result.keySet().spliterator().collect(Collectors.toList()));
        assertEquals(Arrays.asList("value1"), result.values().spliterator().collect(Collectors.toList()));
    }

    @Test
    public void testSplitWithEmptyMapEntry() {
        Splitter keySplitter = Splitter.on(':');
        Splitter valueSplitter = Splitter.fixedLength(2);
        MapSplitter mapSplitter = Splitter.onPattern("\\s+").withKeyValueSeparator(keySplitter, valueSplitter);
        String input = "key1:value1::key2:value2:"; // The third ':' is an empty entry.
        Map<String, String> result = mapSplitter.split(input);
        assertEquals(Arrays.asList("key1", "value1"), result.keySet().spliterator().collect(Collectors.toList()));
        assertEquals(Arrays.asList("value1"), result.values().spliterator().collect(Collectors.toList()));
        assertTrue(result.isEmpty());
    }

}