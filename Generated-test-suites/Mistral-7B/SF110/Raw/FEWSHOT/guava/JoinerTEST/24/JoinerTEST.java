package com.google.common.base;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.junit.Assert.*;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Joiner;
import com.google.common.base.MapJoiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.io.IOException;
import java.util.*;

@GwtCompatible
public class JoinerTest {

    @Test
    public void testJoinerOnString() {
        Joiner joiner = Joiner.on(",");
        List<String> list = Lists.newArrayList("apple", "banana", "cherry");
        StringBuilder expected = new StringBuilder();
        expected.append("apple").append(",").append("banana").append(",").append("cherry");
        StringBuilder actual = new StringBuilder();
        try {
            joiner.appendTo(actual, list.iterator());
        } catch (IOException e) {
            fail("IOException should not be thrown.");
        }
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testJoinerOnChar() {
        Joiner joiner = Joiner.on('-');
        List<String> list = Lists.newArrayList("apple", "banana", "cherry");
        StringBuilder expected = new StringBuilder();
        expected.append("apple").append("-").append("banana").append("-").append("cherry");
        StringBuilder actual = new StringBuilder();
        try {
            joiner.appendTo(actual, list.iterator());
        } catch (IOException e) {
            fail("IOException should not be thrown.");
        }
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testAppendToWithIterable() throws IOException {
        Joiner joiner = Joiner.on(":");
        List<Integer> list = Lists.newArrayList(1, 2, 3);
        StringBuilder actual = new StringBuilder();
        joiner.appendTo(actual, list);
        StringBuilder expected = new StringBuilder();
        expected.append("1:2:3");
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testAppendToWithObjectArray() throws IOException {
        Joiner joiner = Joiner.on("+");
        String[] array = {"apple", "banana", "cherry"};
        StringBuilder actual = new StringBuilder();
        joiner.appendTo(actual, array);
        StringBuilder expected = new StringBuilder();
        expected.append("apple+banana+cherry");
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testAppendToWithSingleObject() throws IOException {
        Joiner joiner = Joiner.on("*");
        StringBuilder actual = new StringBuilder();
        joiner.appendTo(actual, "hello");
        StringBuilder expected = new StringBuilder();
        expected.append("hello");
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testAppendToWithNullFirstAndSecondAndObjects() throws IOException {
        Joiner joiner = Joiner.on("@");
        StringBuilder actual = new StringBuilder();
        joiner.appendTo(actual, null, null, "apple", "banana", "cherry");
        StringBuilder expected = new StringBuilder();
        expected.append("apple@banana@cherry");
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testAppendToWithNullFirstAndSecondAndIterable() throws IOException {
        Joiner joiner = Joiner.on("#");
        StringBuilder actual = new StringBuilder();
        joiner.appendTo(actual, Iterables.asIterable(new Object[]{"apple", null, "banana"}));
        StringBuilder expected = new StringBuilder();
        expected.append("apple#null#banana");
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testJoinWithIterable() {
        Joiner joiner = Joiner.on("/");
        List<String> list = Lists.newArrayList("apple", "banana", "cherry");
        String actual = joiner.join(list);
        String expected = "apple/banana/cherry";
        assertEquals(expected, actual);
    }

    @Test
    public void testJoinWithObjectArray() {
        Joiner joiner = Joiner.on("%");
        String[] array = {"apple", "banana", "cherry"};
        String actual = joiner.join(array);
        String expected = "apple%banana%cherry";
        assertEquals(expected, actual);
    }

    @Test
    public void testJoinWithSingleObject() {
        Joiner joiner = Joiner.on("$");
        String actual = joiner.join("hello");
        String expected = "hello";
        assertEquals(expected, actual);
    }

    @Test
    public void testJoinWithNullFirstAndSecondAndObjects() {
        Joiner joiner = Joiner.on("&");
        String actual = joiner.join(null, null, "apple", "banana", "cherry");
        String expected = "apple&banana&cherry";
        assertEquals(expected, actual);
    }

    @Test
    public void testJoinWithNullFirstAndSecondAndIterable() {
        Joiner joiner = Joiner.on("*");
        String actual = joiner.join(Iterables.asIterable(new Object[]{"apple", null, "banana"}));
        String expected = "apple*null*banana";
        assertEquals(expected, actual);
    }

    @Test
    public void testUseForNull() {
        Joiner joiner = Joiner.on("@");
        Joiner joinerWithNullText = joiner.useForNull("NULL");
        String actual = joinerWithNullText.join(Arrays.asList(null, "apple", "banana", "cherry"));
        String expected = "NULL@apple@banana@cherry";
        assertEquals(expected, actual);
    }

    @Test
    public void testSkipNulls() {
        Joiner joiner = Joiner.on("+");
        Joiner joinerSkippingNulls = joiner.skipNulls();
        String[] array = {"apple", null, "banana", null, "cherry"};
        String actual = joinerSkippingNulls.join(array);
        String expected = "apple+banana+cherry";
        assertEquals(expected, actual);
    }

    @Test
    public void testMapJoinerWithStringKeyValueSeparator() {
        Joiner joiner = Joiner.on("-");
        MapJoiner mapJoiner = joiner.withKeyValueSeparator("=");
        Map<String, Integer> map = Maps.newHashMap();
        map.put("apple", 1);
        map.put("banana", 2);
        map.put("cherry", 3);
        StringBuilder actual = new StringBuilder();
        mapJoiner.appendTo(actual, map);
        StringBuilder expected = new StringBuilder();
        expected.append("apple=-1&banana=-2&cherry=-3");
        assertEquals(expected.toString(), actual.toString());
        String joined = mapJoiner.join(map);
        assertEquals(expected.toString(), joined);
    }

    @Beta
    @Test
    public void testMapJoinerWithIterableMapEntry() throws IOException {
        Joiner joiner = Joiner.on("&");
        MapJoiner mapJoiner = joiner.withKeyValueSeparator("=");
        Map<String, Integer> map = Maps.newHashMap();
        map.put("apple", 1);
        map.put("banana", 2);
        map.put("cherry", 3);
        StringBuilder actual = new StringBuilder();
        mapJoiner.appendTo(actual, map.entrySet());
        StringBuilder expected = new StringBuilder();
        expected.append("apple=1&banana=2&cherry=3");
        assertEquals(expected.toString(), actual.toString());
    }

    @Beta
    @Test
    public void testMapJoinerWithIterableMapEntryNullText() throws IOException {
        Joiner joiner = Joiner.on("|");
        MapJoiner mapJoiner = joiner.useForNull("NULL");
        Map<String, Integer> map = Maps.newHashMap();
        map.put("apple", 1);
        map.put("banana", null);
        map.put("cherry", 3);
        StringBuilder actual = new StringBuilder();
        mapJoiner.appendTo(actual, map.entrySet());
        StringBuilder expected = new StringBuilder();
        expected.append("apple=1&NULL=null&cherry=3");
        assertEquals(expected.toString(), actual.toString());
    }
}