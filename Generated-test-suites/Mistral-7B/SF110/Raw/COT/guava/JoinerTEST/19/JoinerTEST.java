package com.example.junit; // replace with your package name if needed

import static org.junit.Assert.*;
import com.google.common.base.Joiner;
import com.google.common.base.MapJoiner;
import com.google.common.collect.ImmutableMap;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class JoinerTest {

    @SuppressWarnings("unchecked")
    <A extends Appendable> void testAppendTo(Joiner joiner, A appendable, Iterable<?> parts) throws IOException {
        joiner.appendTo(appendable, parts);
        assertEquals(joiner.separator().repeat(parts.size() + 1), appendable.toString());
        appendable.reset();
    }

    @Test
    public void testStringJoinerOnString() throws Exception {
        Joiner joiner = Joiner.on(",");
        StringJoiner stringJoiner = (StringJoiner) joiner;
        String expected = "apple,banana,cherry";
        StringJoiner appendable = new StringJoiner(",");
        joiner.appendTo(appendable, Arrays.asList("apple", "banana", "cherry"));
        assertEquals(expected, appendable.toString());
    }

    @Test
    public void testStringJoinerOnChar() throws Exception {
        Joiner joiner = Joiner.on('/');
        StringJoiner stringJoiner = (StringJoiner) joiner;
        String expected = "apple/banana/cherry";;
        StringJoiner appendable = new StringJoiner("/");
        joiner.appendTo(appendable, Arrays.asList("apple", "banana", "cherry"));
        assertEquals(expected, appendable.toString());
    }

    @Test
    public void testAppendToWithIterable() throws Exception {
        Joiner joiner = Joiner.on(",");
        StringJoiner appendable = new StringJoiner(",");
        joiner.appendTo(appendable, Arrays.asList("apple", "banana", "cherry"));
        testStringJoinerOnString();
    }

    @Test
    public void testAppendToWithArray() throws Exception {
        Joiner joiner = Joiner.on(",");
        StringJoiner appendable = new StringJoiner(",");
        joiner.appendTo(appendable, new Object[]{"apple", "banana", "cherry"});
        testStringJoinerOnString();
    }

    @Test
    public void testAppendToWithNullFirst() throws Exception {
        Joiner joiner = Joiner.on(",");
        StringJoiner appendable = new StringJoiner(",");
        joiner.appendTo(appendable, new Object[]{null, "banana", "cherry"});
        String expected = "null,banana,cherry";
        assertEquals(expected, appendable.toString());
    }

    @Test
    public void testAppendToWithNullSecond() throws Exception {
        Joiner joiner = Joiner.on(",");
        StringJoiner appendable = new StringJoiner(",");
        joiner.appendTo(appendable, new Object[]{"apple", null, "cherry"});
        String expected = "apple,null,cherry";
        assertEquals(expected, appendable.toString());
    }

    @Test
    public void testAppendToWithNullInList() throws Exception {
        Joiner joiner = Joiner.on(",");
        StringJoiner appendable = new StringJoiner(",");
        List<Object> parts = new ArrayList<>(Arrays.asList("apple", null, "cherry"));
        joiner.appendTo(appendable, parts);
        String expected = "apple,null,cherry";
        assertEquals(expected, appendable.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAppendToWithNullIterable() throws Exception {
        Joiner joiner = Joiner.on(",");
        StringJoiner appendable = new StringJoiner(",");
        joiner.appendTo(appendable, null);
    }

    @Test(expected = IOException.class)
    public void testAppendToWithNullAppendable() throws Exception {
        Joiner joiner = Joiner.on(",");
        joiner.appendTo(null, Arrays.asList("apple", "banana", "cherry"));
    }

    @Test
    public void testJoinWithIterable() throws Exception {
        Joiner joiner = Joiner.on(",");
        String actual = joiner.join(Arrays.asList("apple", "banana", "cherry"));
        String expected = "apple,banana,cherry";
        assertEquals(expected, actual);
    }

    @Test
    public void testJoinWithArray() throws Exception {
        Joiner joiner = Joiner.on(",");
        String actual = joiner.join(new Object[]{"apple", "banana", "cherry"});
        String expected = "apple,banana,cherry";
        assertEquals(expected, actual);
    }

    @Test
    public void testJoinWithNullFirst() throws Exception {
        Joiner joiner = Joiner.on(",");
        String actual = joiner.join(new Object[]{null, "banana", "cherry"});
        String expected = "null,banana,cherry";
        assertEquals(expected, actual);
    }

    @Test
    public void testJoinWithNullSecond() throws Exception {
        Joiner joiner = Joiner.on(",");
        String actual = joiner.join(new Object[]{"apple", null, "cherry"});
        String expected = "apple,null,cherry";
        assertEquals(expected, actual);
    }

    @Test
    public void testJoinWithNullInArray() throws Exception {
        Joiner joiner = Joiner.on(",");
        String actual = joiner.join(new Object[]{null, "banana", "cherry"});
        String expected = "null,banana,cherry";
        assertEquals(expected, actual);
    }

    @Test
    public void testUseForNull() throws Exception {
        Joiner joiner = Joiner.on(",");
        Joiner joinerWithNullText = joiner.useForNull("NA");
        String actual = joinerWithNullText.join(Arrays.asList("apple", null, "cherry"));
        String expected = "apple,NA,cherry";
        assertEquals(expected, actual);
    }

    @Test
    public void testSkipNulls() throws Exception {
        Joiner joiner = Joiner.on(",");
        Joiner joinedWithNullsSkipped = joiner.skipNulls();
        String actual = joinedWithNullsSkipped.join(Arrays.asList("apple", null, "cherry"));
        String expected = "apple,cherry";
        assertEquals(expected, actual);
    }

    @Test
    public void testMapJoiner() throws Exception {
        Joiner joiner = Joiner.on(",");
        MapJoiner mapJoiner = joiner.withKeyValueSeparator(": ");
        Map<String, String> map = ImmutableMap.of("apple", "red", null, "cherry", "banana", "yellow");
        String actual = mapJoiner.join(map);
        String expected = "apple: red,cherry: yellow,banana: yellow";
        assertEquals(expected, actual);
    }

    @Test
    public void testMapJoinerWithNullKey() throws Exception {
        Joiner joiner = Joiner.on(",");
        MapJoiner mapJoiner = joiner.withKeyValueSeparator(": ");
        Map<String, String> map = ImmutableMap.of(null, "cherry", "banana", "yellow");
        String actual = mapJoiner.join(map);
        String expected = "null: cherry,banana: yellow";
        assertEquals(expected, actual);
    }

    @Test
    public void testMapJoinerWithNullValue() throws Exception {
        Joiner joiner = Joiner.on(",");
        MapJoiner mapJoiner = joiner.withKeyValueSeparator(": ");
        Map<String, Object> map = ImmutableMap.of("apple", null, "cherry", "banana", "yellow");
        String actual = mapJoiner.join(map);
        String expected = "apple: null,cherry: ,banana: yellow";
        assertEquals(expected, actual);
    }

    @Test
    public void testMapJoinerWithEmptyMap() throws Exception {
        Joiner joiner = Joiner.on(",");
        MapJoiner mapJoiner = joiner.withKeyValueSeparator(": ");
        Map<String, String> map = Collections.emptyMap();
        String actual = mapJoiner.join(map);
        String expected = "";
        assertEquals(expected, actual);
    }
}