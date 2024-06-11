package org.apache.commons.cli; // Assuming you want to test the Splitter class from the given package instead of com.google.common.base

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SplitterTest {

    @Test
    public void testSplitterOnSingleCharacter() {
        Splitter splitter = Splitter.on('-');
        List<String> result = splitter.split("test-test-test");
        assertEquals(Arrays.asList("test", "test", "test"), result);
    }

    @Test
    public void testSplitterOnMultipleCharacter() {
        Splitter splitter = Splitter.on("-_");
        List<String> result = splitter.split("test_-test_-test_-test");
        assertEquals(Arrays.asList("test", "", "test", "", "test"), result);
    }

    @Test
    public void testSplitterOnEmptyString() {
        Splitter splitter = Splitter.on(" ");
        List<String> result = splitter.split(" test ");
        assertEquals(Arrays.asList("", "", "", ""), result);
    }

    @Test
    public void testSplitterOnEmptySequence() {
        Splitter splitter = Splitter.on(" ");
        List<String> result = splitter.split(Character.MIN_VALUE, 0);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testSplitterOnSingleCharacterWithTrim() {
        Splitter splitter = Splitter.on('-').trimResults();
        List<String> result = splitter.split("test--test--test");
        assertEquals(Arrays.asList("test", "test", "test"), result);
    }

    @Test
    public void testSplitterOnMultipleCharacterWithTrim() {
        Splitter splitter = Splitter.on("-_").trimResults();
        List<String> result = splitter.split("test_-test_-test_-test");
        assertEquals(Arrays.asList("test", "", "test", "", "test"), result);
    }

    @Test
    public void testSplitterOnEmptyStringWithTrim() {
        Splitter splitter = Splitter.on(" ").trimResults();
        List<String> result = splitter.split(" test ");
        assertEquals(Arrays.asList("", "", "", ""), result);
    }

    @Test
    public void testSplitterOnEmptySequenceWithTrim() {
        Splitter splitter = Splitter.on(" ").trimResults();
        List<String> result = splitter.split(Character.MIN_VALUE, 0);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testSplitterOnPattern() {
        Pattern pattern = Pattern.compile("\\s+");
        Splitter splitter = Splitter.onPattern(pattern);
        List<String> result = splitter.split("test test test");
        assertEquals(Arrays.asList("test", "test", "test"), result);
    }

    @Test
    public void testSplitterOnFixedLength() {
        Splitter splitter = Splitter.fixedLength(2);
        List<String> result = splitter.split("1234567890");
        assertEquals(Arrays.asList("12", "34", "56", "78", "90"), result);
    }

    @Test
    public void testSplitterOnEmptyStringWithFixedLength() {
        Splitter splitter = Splitter.fixedLength(2);
        List<String> result = splitter.split("1");
        assertTrue(result.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSplitterOnNegativeLength() {
        Splitter splitter = Splitter.fixedLength(-1);
    }

    @Test
    public void testSplitterOnOmittingEmptyStrings() {
        Splitter splitter = Splitter.on(" ").omitEmptyStrings();
        List<String> result = splitter.split(" test test test ");
        assertEquals(Arrays.asList("test", "test", "test"), result);
    }

    @Test
    public void testSplitterOnLimit() {
        Splitter splitter = Splitter.on(" ").limit(3);
        List<String> result = splitter.split("test test test test test test test test test");
        assertEquals(Arrays.asList("test", "test", "test test test"), result);
    }

    @Test
    public void testMapSplitter() {
        String input = "key1=value1 key2=value2 key3=value3";
        Splitter keyValueSplitter = Splitter.on("=").omitEmptyStrings();
        Map<String, String> map = new Splitter.MapSplitter(Splitter.on(" "), keyValueSplitter).split(input);
        assertEquals(3, map.size());
        assertEquals("key1", map.keySet().iterator().next());
        assertEquals("value1", map.get("key1"));
        assertEquals("key2", map.keySet().iterator().next());
        assertEquals("value2", map.get("key2"));
        assertEquals("key3", map.keySet().iterator().next());
        assertEquals("value3", map.get("key3"));
    }

    @Test
    public void testMapSplitterWithEmptyValues() {
        String input = "key1= value2= key3=";
        Splitter keyValueSplitter = Splitter.on("=").omitEmptyStrings();
        Map<String, String> map = new Splitter.MapSplitter(Splitter.on(" "), keyValueSplitter).split(input);
        assertEquals(3, map.size());
        assertEquals("key1", map.keySet().iterator().next());
        assertNull(map.get("key1"));
        assertEquals("key2", map.keySet().iterator().next());
        assertNull(map.get("key2"));
        assertEquals("key3", map.keySet().iterator().next());
        assertNull(map.get("key3"));
    }
}