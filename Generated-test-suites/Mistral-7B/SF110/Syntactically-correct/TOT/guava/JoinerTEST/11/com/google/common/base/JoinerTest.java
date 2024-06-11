package com.google.common.base;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

@RunWith(JUnit4.class)
public class JoinerTest {

    @Test
    public void testDefaultJoiner() {
        Joiner joiner = Joiner.on(" ");
        List<String> parts = Lists.newArrayList("apple", "banana", "cherry");
        StringBuilder expected = new StringBuilder();
        expected.append("apple ");
        expected.append("banana ");
        expected.append("cherry");

        StringBuilder result = joiner.appendTo(new StringBuilder(), parts);
        assertEquals(expected.toString(), result.toString());

        String joined = joiner.join(parts);
        assertEquals(expected.toString(), joined);
    }

    @Test
    public void testJoinerWithCharSeparator() {
        Joiner joiner = Joiner.on('-');
        List<Integer> parts = Lists.newArrayList(1, 2, 3);
        StringBuilder expected = new StringBuilder();
        expected.append("1-");
        expected.append("2-");
        expected.append("3");

        StringBuilder result = joiner.appendTo(new StringBuilder(), parts);
        assertEquals(expected.toString(), result.toString());

        String joined = joiner.join(parts);
        assertEquals(expected.toString(), joined);
    }

    @Test
    public void testJoinerWithNullElements() {
        Joiner joiner = Joiner.on(",");
        List<String> parts = Lists.newArrayList("apple", null, "banana");
        StringBuilder expected = new StringBuilder();
        expected.append("apple,");
        expected.append("null,");
        expected.append("banana");

        StringBuilder result = joiner.appendTo(new StringBuilder(), parts);
        assertEquals(expected.toString(), result.toString());

        String joined = joiner.join(parts);
        assertEquals(expected.toString(), joined);
    }

    @Test(expected = NullPointerException.class)
    public void testNullJoiner() {
        Joiner joiner = null;
        StringBuilder appendable = new StringBuilder();
        Iterable<?> parts = Arrays.asList("apple", "banana", "cherry");
        joiner.appendTo(appendable, parts);
    }

    @Test(expected = NullPointerException.class)
    public void testNullAppendable() {
        Joiner joiner = Joiner.on(",");
        String separator = null;
        joiner = joiner.useForNull(separator);
        Iterable<?> parts = Arrays.asList("apple", "banana", "cherry");
        joiner.appendTo(null, parts);
    }

    @Test(expected = NullPointerException.class)
    public void testNullParts() {
        Joiner joiner = Joiner.on(",");
        StringBuilder appendable = new StringBuilder();
        joiner.appendTo(appendable, null);
    }

    @Test
    public void testJoinerWithEmptyCollection() {
        Joiner joiner = Joiner.on(",");
        StringBuilder expected = new StringBuilder();
        String joined = joiner.join(Collections.emptyList());
        assertEquals(expected.toString(), joined);
    }
}