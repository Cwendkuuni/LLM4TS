package com.google.common.base;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SplitterTest {

 private Splitter separatorMatcherSplitter;
 private Splitter separatorPatternSplitter;
 private Splitter separatorStringSplitter;
 private Splitter fixedLengthSplitter;

 @Before
 public void setup() {
 separatorMatcherSplitter = Splitter.on(CharMatcher.is('.'));
 separatorPatternSplitter = Splitter.onPattern("\\.");
 separatorStringSplitter = Splitter.on(".");
 fixedLengthSplitter = Splitter.fixedLength(3);
 }

 @Test
 public void testSeparatorMatcher() {
 String input = "a.b.c.d.e";
 List<String> expected = Arrays.asList("a", "b", "c", "d", "e");
 List<String> result = separatorMatcherSplitter.split(input);
 assertEquals(expected, result);
 }

 @Test
 public void testSeparatorPattern() {
 String input = "a.b.c.d.e";
 Pattern pattern = Pattern.compile("\\.");
 List<String> expected = Arrays.asList("a", "b", "c", "d", "e");
 List<String> result = separatorPatternSplitter.split(input);
 assertEquals(expected, result);
 }

 @Test
 public void testSeparatorString() {
 String input = "a.b.c.d.e";
 List<String> expected = Arrays.asList("a", "b", "c", "d", "e");
 List<String> result = separatorStringSplitter.split(input);
 assertEquals(expected, result);
 }

 @Test
 public void testFixedLength() {
 String input = "abcdefghijklmnopqrstuvwxyz";
 List<String> expected = Arrays.asList("abc", "def", "ghi", "jkl", "mno", "pqr", "stu", "wxy", "z");
 List<String> result = fixedLengthSplitter.split(input);
 assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullInput() {
 Splitter splitter = Splitter.on(".");
 char[] input = null;
 try {
 splitter.split(input);
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testOutOfBounds() {
 String input = "abc";
 Splitter splitter = Splitter.fixedLength(2);
 try {
 splitter.split(input);
 } catch (IndexOutOfBoundsException e) {
 // Expected
 }
 }

 @Test
 public void testEmptyString() {
 String input = "";
 List<String> expected = Collections.emptyList();
 List<String> result = separatorMatcherSplitter.split(input);
 assertEquals(expected, result);
 }

 @Test
 public void testEmptyStringOmit() {
 Splitter splitter = separatorMatcherSplitter.omitEmptyStrings();
 String input = "";
 List<String> result = splitter.split(input);
 assertTrue(result.isEmpty());
 }

 @Test
 public void testLimit() {
 String input = "123456789012345678901234567890";
 List<String> expected = Arrays.asList("123", "456", "789", "012", "345", "678", "901", "234", "567", "890");
 Splitter splitter = Splitter.fixedLength(3);
 List<String> result = splitter.limit(11).split(input);
 assertEquals(expected, result);
 }

 @Test
 public void testMapSplitter() {
 String input = "key1=value1,key2=value2,key3=value3";
 Map<String, String> expected = new HashMap<>();
 expected.put("key1", "value1");
 expected.put("key2", "value2");
 expected.put("key3", "value3");
 Map<String, String> result = separatorPatternSplitter.withKeyValueSeparator(",").split(input);
 assertEquals(expected, result);
 }
}