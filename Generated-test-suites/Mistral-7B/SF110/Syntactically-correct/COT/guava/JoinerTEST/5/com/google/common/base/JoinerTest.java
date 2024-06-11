package com.example.junit; // replace with your package name if necessary

import static org.junit.Assert.*;

import com.google.common.base.Joiner;
import com.google.common.base.MapJoiner;
import com.google.common.collect.Arrays;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import org.junit.Test;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class JoinerTest {

    @Test
    public void testJoinerOnString() {
        Joiner joiner = Joiner.on(",");
        String result = joiner.join(Arrays.asList("apple", "banana", "cherry"));
        assertEquals("apple,banana,cherry", result);
    }

    @Test
    public void testJoinerOnCharacter() {
        Joiner joiner = Joiner.on('-');
        String result = joiner.join(Arrays.asList("apple", "banana", "cherry"));
        assertEquals("apple-banana-cherry", result);
    }

    @Test
    public void testAppendToIterable() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringWriter stringWriter = new StringWriter();
        joiner.appendTo(stringWriter, Arrays.asList("apple", "banana", "cherry"));
        assertEquals("apple,banana,cherry", stringWriter.toString());
    }

    @Test
    public void testAppendToObjectArray() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringWriter stringWriter = new StringWriter();
        joiner.appendTo(stringWriter, new Object[]{"apple", "banana", "cherry"});
        assertEquals("apple,banana,cherry", stringWriter.toString());
    }

    @Test
    public void testAppendToIterableWithNull() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringWriter stringWriter = new StringWriter();
        joiner.appendTo(stringWriter, Arrays.asList("apple", null, "cherry"));
        assertEquals("apple,null,cherry", stringWriter.toString());
    }

    @Test(expected = IOException.class)
    public void testAppendToIterableWithNullIO() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringWriter stringWriter = null;
        joiner.appendTo(stringWriter, Arrays.asList("apple", null, "cherry"));
    }

    @Test
    public void testJoinObjectArray() {
        Joiner joiner = Joiner.on(",");
        String result = joiner.join(new Object[]{"apple", "banana", "cherry"});
        assertEquals("apple,banana,cherry", result);
    }

    @Test
    public void testJoinWithNull() {
        Joiner joiner = Joiner.on(",");
        String result = joiner.join(new Object[]{null, "banana", "cherry"});
        assertEquals("null,banana,cherry", result);
    }

    @Test
    public void testJoinMap() {
        Joiner joiner = Joiner.on(",");
        Map<String, String> map = ImmutableMap.of("apple", "red", "banana", "yellow", "cherry", "green");
        String result = joiner.join(map);
        assertEquals("apple,red,banana,yellow,cherry,green", result);
    }

    @Test
    public void testJoinMapEntrySet() {
        Joiner joiner = Joiner.on(",");
        Map<String, String> map = ImmutableMap.of("apple", "red", "banana", "yellow", "cherry", "green");
        String result = joiner.join(map.entrySet());
        assertEquals("apple=red,banana=yellow,cherry=green", result);
    }

    @Test
    public void testMapJoinerAppendToMap() throws IOException {
        Joiner joiner = Joiner.on(",");
        MapJoiner mapJoiner = joiner.withKeyValueSeparator("=");
        StringWriter stringWriter = new StringWriter();
        mapJoiner.appendTo(stringWriter, ImmutableMap.of("apple", "red", "banana", "yellow", "cherry", "green"));
        assertEquals("apple=red,banana=yellow,cherry=green", stringWriter.toString());
    }

    @Test
    public void testMapJoinerJoinMap() {
        Joiner joiner = Joiner.on(",");
        MapJoiner mapJoiner = joiner.withKeyValueSeparator("=");
        String result = mapJoiner.join(ImmutableMap.of("apple", "red", "banana", "yellow", "cherry", "green"));
        assertEquals("apple=red,banana=yellow,cherry=green", result);
    }

    @Test
    public void testMapJoinerJoinMapEntrySet() {
        Joiner joiner = Joiner.on(",");
        MapJoiner mapJoiner = joiner.withKeyValueSeparator("=");
        String result = mapJoiner.join(mapJoiner.appendTo(new StringBuilder(), mapJoiner.join
                (ImmutableMap.of("apple", "red", "banana", "yellow", "cherry", "green"))).toString().split("\\Z")[0].split("="));
        assertEquals("apple=red,banana=yellow,cherry=green", result);
    }
}