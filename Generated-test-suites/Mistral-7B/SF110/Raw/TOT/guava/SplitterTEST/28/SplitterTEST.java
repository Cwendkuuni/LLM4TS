package com.google.common.base;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class SplitterTest {

 private Splitter commaSplitter;
 private Splitter whitespaceSplitter;
 private Splitter patternSplitter;
 private Splitter separatorPatternSplitter;
 private Splitter fixedLengthSplitter;

 @Before
 public void setup() {
 commaSplitter = Splitter.on(',');
 whitespaceSplitter = Splitter.on(Character.isWhitespace());
 patternSplitter = Splitter.on("[a-z]+");
 separatorPatternSplitter = Splitter.onPattern("\\s+|[a-z]+");
 fixedLengthSplitter = Splitter.fixedLength(3);
 }

 @Test
 public void testCommaSplitter() {
 List<String> result = Lists.newArrayList(commaSplitter.split("one,two,three,four"));
 assertEquals(Arrays.asList("one", "two", "three", "four"), result);
 }

 @Test
 public void testCommaSplitterEmptyString() {
 List<String> result = Lists.newArrayList(commaSplitter.split(""));
 assertTrue(result.isEmpty());
 }

 @Test
 public void testCommaSplitterNull() {
 List<String> result = Lists.newArrayList(commaSplitter.split(null));
 assertTrue(result.isEmpty());
 }

 @Test
 public void testWhitespaceSplitter() {
 List<String> result = Lists.newArrayList(whitespaceSplitter.split("one two three four"));
 assertEquals(Arrays.asList("one", "two", "three", "four"), result);
 }

 @Test
 public void testWhitespaceSplitterEmptyString() {
 List<String> result = Lists.newArrayList(whitespaceSplitter.split(""));
 assertTrue(result.isEmpty());
 }

 @Test
 public void testWhitespaceSplitterNull() {
 List<String> result = Lists.newArrayList(whitespaceSplitter.split(null));
 assertTrue(result.isEmpty());
 }

 @Test
 public void testPatternSplitter() {
 List<String> result = Lists.newArrayList(patternSplitter.split("apple-green-orange"));
 assertEquals(Arrays.asList("apple", "green", "orange"), result);
 }

 @Test
 public void testPatternSplitterEmptyString() {
 List<String> result = Lists.newArrayList(patternSplitter.split(""));
 assertTrue(result.isEmpty());
 }

 @Test
 public void testPatternSplitterNull() {
 List<String> result = Lists.newArrayList(patternSplitter.split(null));
 assertTrue(result.isEmpty());
 }

 @Test
 public void testSeparatorPatternSplitter() {
 Map<String, String> result = separatorPatternSplitter.withKeyValueSeparator(":").split("key1:value1:key2:value2");
 assertEquals(Maps.newLinkedHashMap(Arrays.asList(new AbstractMap.SimpleEntry<>("key1", "value1"), new AbstractMap.SimpleEntry<>("key2", "value2"))), result);
 }

 @Test
 public void testSeparatorPatternSplitterEmptyString() {
 Map<String, String> result = separatorPatternSplitter.withKeyValueSeparator(":").split("");
 assertTrue(result.isEmpty());
 }

 @Test
 public void testSeparatorPatternSplitterNull() {
 Map<String, String> result = separatorPatternSplitter.withKeyValueSeparator(":").split(null);
 assertTrue(result.isEmpty());
 }

 @Test
 public void testFixedLengthSplitter() {
 List<String> result = Lists.newArrayList(fixedLengthSplitter.split("abcdefghijklmnopqrstuvwxyz"));
 assertEquals(Arrays.asList("abc", "def", "ghi", "jkl", "mno", "pqr", "stu", "vwxy"), result);
 }

 @Test
 public void testFixedLengthSplitterEmptyString() {
 List<String> result = Lists.newArrayList(fixedLengthSplitter.split(""));
 assertTrue(result.isEmpty());
 }

 @Test
 public void testFixedLengthSplitterNull() {
 List<String> result = Lists.newArrayList(fixedLengthSplitter.split(null));
 assertTrue(result.isEmpty());
 }

 @Test(expected = NullPointerException.class)
 public void testNullInputThrowsException() {
 commaSplitter.split(null);
 }
}