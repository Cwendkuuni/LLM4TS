package org.apache.commons.cli; // Assuming you want to test the Splitter class from the given package "com.google.common.base"

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.junit.Assert.*;
import com.google.common.base.*;
import org.junit.Test;

public class SplitterTest {

    @Test
    public void testSplitterBasicUsage() {
        CharMatcher separatorMatcher = CharMatcher.is('-');
        Splitter splitter = Splitter.on(separatorMatcher);
        String text = "item1-item2-item3";

        Iterable<String> splitText = splitter.split(text);
        List<String> splitList = splitText.asList();

        assertEquals(3, splitList.size());
        assertEquals("item1", splitList.get(0));
        assertEquals("item2", splitList.get(1));
        assertEquals("item3", splitList.get(2));
    }

    @Test
    public void testSplitterWithEmptyStrings() {
        CharMatcher separatorMatcher = CharMatcher.is('-');
        Splitter splitter = Splitter.on(separatorMatcher).omitEmptyStrings();
        String text = "item1-item2-";

        Iterable<String> splitText = splitter.split(text);
        List<String> splitList = splitText.asList();

        assertEquals(2, splitList.size());
        assertEquals("item1", splitList.get(0));
        assertEquals("item2", splitList.get(1));

        text = "item1--item2--";

        splitText = splitter.split(text);
        splitList = splitText.asList();

        assertEquals(2, splitList.size());
        assertEquals("item1", splitList.get(0));
        assertEquals("item2", splitList.get(1));
    }

    @Test
    public void testSplitterWithLimit() {
        CharMatcher separatorMatcher = CharMatcher.is('-');
        Splitter splitter = Splitter.on(separatorMatcher).limit(2);
        String text = "item1-item2-item3-item4";

        Iterable<String> splitText = splitter.split(text);
        List<String> splitList = splitText.asList();

        assertEquals(3, splitList.size());
        assertEquals("item1-item2", splitList.get(0));
        assertEquals("item3", splitList.get(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSplitterNullText() {
        CharMatcher separatorMatcher = CharMatcher.is('-');
        Splitter splitter = Splitter.on(separatorMatcher);
        String text = null;

        Iterable<String> splitText = splitter.split(text);
    }

    @Test
    public void testSplitterEmptyText() {
        CharMatcher separatorMatcher = CharMatcher.is('-');
        Splitter splitter = Splitter.on(separatorMatcher);
        String text = "";

        Iterable<String> splitText = splitter.split(text);
        List<String> splitList = splitText.asList();

        assertEquals(0, splitList.size());
    }

    @Test
    public void testSplitterWithCustomSeparator() {
        String separator = ":";
        Splitter splitter = Splitter.onPattern(separator);
        String text = "item1:item2:item3";

        Iterable<String> splitText = splitter.split(text);
        List<String> splitList = splitText.asList();

        assertEquals(3, splitList.size());
        assertEquals("item1", splitList.get(0));
        assertEquals("item2", splitList.get(1));
        assertEquals("item3", splitList.get(2));
    }

    @Test
    public void testSplitterWithFixedLengthSeparator() {
        Splitter splitter = Splitter.fixedLength(2);
        String text = "item1item2item3item4";

        Iterable<String> splitText = splitter.split(text);
        List<String> splitList = splitText.asList();

        assertEquals(4, splitList.size());
        assertEquals("item1item", splitList.get(0));
        assertEquals("item2", splitList.get(1));
        assertEquals("item3item", splitList.get(2));
        assertEquals("item4", splitList.get(3));
    }

    @Test
    public void testMapSplitter() {
        String separator = ":";
        Splitter outerSplitter = Splitter.onPattern(separator);
        Splitter entrySplitter = Splitter.onPattern("=");

        String text = "key1:value1=value2:key2:value3=value4";

        MapSplitter mapSplitter = outerSplitter.withKeyValueSeparator(entrySplitter);
        Map<String, String> map = mapSplitter.split(text);

        assertEquals(2, map.size());
        assertEquals("key1", map.get("key1"));
        assertEquals("value1=value2", map.get("key1").split("=")[0]);
        assertEquals("value2", map.get("key1").split("=")[1]);

        assertEquals("key2", map.get("key2"));
        assertEquals("value3=value4", map.get("key2").split("=")[0]);
        assertEquals("value3", map.get("key2").split("=")[1]);
    }
}