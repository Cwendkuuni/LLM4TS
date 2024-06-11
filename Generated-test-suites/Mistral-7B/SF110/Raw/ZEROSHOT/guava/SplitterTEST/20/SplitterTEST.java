package com.example.junit;

import static org.junit.Assert.*;
import static com.google.common.base.Splitter.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class SplitterTest {

    @Test
    public void testOnChar() {
        Splitter splitter = on('-');
        List<String> result = new ArrayList<>(Arrays.asList("one", "two", "three-four", "five-six"));
        List<String> expected = splitter.split("one-two-three-four-five-six");
        assertEquals(result, expected);
    }

    @Test
    public void testOnCharMatcher() {
        Splitter splitter = on(Character.isWhitespace());
        List<String> result = new ArrayList<>(Arrays.asList("one", "two", "three four", "five six"));
        List<String> expected = splitter.split("one two three four five six");
        assertEquals(result, expected);
    }

    @Test
    public void testOnString() {
        Splitter splitter = on(":");
        List<String> result = new ArrayList<>(Arrays.asList("one:two:three:four:five:six"));
        List<String> expected = splitter.split("one:two:three:four:five:six");
        assertEquals(result, expected);
    }

    @Test
    public void testOnPattern() {
        Splitter splitter = on("\\s+");
        List<String> result = new ArrayList<>(Arrays.asList("one", "two", "three four", "five six"));
        List<String> expected = splitter.split("one two three four five six");
        assertEquals(result, expected);
    }

    @Test
    public void testOnPatternWithEmptyString() {
        Splitter splitter = onPattern("\\s+|-");
        List<String> result = new ArrayList<>(Arrays.asList("one", "two", "", "three four", "five six"));
        List<String> expected = splitter.split("one two - three four five six");
        assertEquals(result, expected);
    }

    @Test
    public void testOnPatternWithEmptyInput() {
        Splitter splitter = onPattern("\\s+|-");
        List<String> result = new ArrayList<>();
        List<String> expected = splitter.split("");
        assertEquals(result, expected);
    }

    @Test
    public void testOnFixedLength() {
        Splitter splitter = fixedLength(2);
        List<String> result = new ArrayList<>(Arrays.asList("one-two", "three-four", "five-six"));
        List<String> expected = splitter.split("onetwothreefourfivesix");
        assertEquals(result, expected);
    }

    @Test
    public void testOmitEmptyStrings() {
        Splitter splitter = on(":").omitEmptyStrings();
        List<String> result = new ArrayList<>(Arrays.asList("one:two", "three:four:five:six"));
        List<String> expected = splitter.split("one:two: :three:four:five:six:");
        assertEquals(result, expected);
    }

    @Test
    public void testLimit() {
        Splitter splitter = on(":").limit(3);
        List<String> result = new ArrayList<>(Arrays.asList("one:two", "three:four", "five:six"));
        List<String> expected = splitter.split("one:two:three:four:five:six");
        assertEquals(result, expected);
    }

    @Test
    public void testMapSplitter() {
        List<String> result = new ArrayList<>();
        Map<String, String> expected = new LinkedHashMap<>();
        expected.put("key1", "value1");
        expected.put("key2", "value2");

        Splitter keyValueSplitter = on(":");
        MapSplitter mapSplitter = withKeyValueSeparator(keyValueSplitter);
        CharSequence sequence = "key1:value1:key2:value2";
        Map<String, String> actual = mapSplitter.split(sequence);

        assertEquals(expected, actual);
        for (String key : actual.keySet()) {
            List<String> keyParts = Splitter.on(":").split(key);
            assertEquals(1, keyParts.size());
            assertEquals(key, keyParts.get(0));
        }

        for (String value : actual.values()) {
            List<String> valueParts = Splitter.on(":").split(value);
            assertEquals(1, valueParts.size());
            assertEquals(value, valueParts.get(0));
        }
    }
}