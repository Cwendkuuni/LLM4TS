package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Pattern;

public class SplitterTest {

 @Test
 public void testOnCharSeparator() {
 CharSequence sequence = "abcdefghello world!";
 Splitter splitter = Splitter.on(' ');
 List<String> result = new ArrayList<String>(Arrays.asList(splitter.split(sequence)));
 List<String> expected = Arrays.asList("abcdefg", "hello", "world!");
 assertEquals(expected, result);
 }

 @Test
 public void testOnCharMatcherSeparator() {
 CharSequence sequence = "abcdefghello_world!";
 Splitter splitter = Splitter.on(CharMatcher.forRegex("_"));
 List<String> result = new ArrayList<String>(Arrays.asList(splitter.split(sequence)));
 List<String> expected = Arrays.asList("abcdefg", "hello", "world!");
 assertEquals(expected, result);
 }

 @Test
 public void testOnStringSeparator() {
 CharSequence sequence = "abcdefg:hello:world!";
 Splitter splitter = Splitter.on(":");
 List<String> result = new ArrayList<String>(Arrays.asList(splitter.split(sequence)));
 List<String> expected = Arrays.asList("abcdefg", "hello", "world!");
 assertEquals(expected, result);
 }

 @Test
 public void testOnPatternSeparator() {
 CharSequence sequence = "abcdefg---hello---world!";
 Splitter splitter = Splitter.onPattern("---");
 List<String> result = new ArrayList<String>(Arrays.asList(splitter.split(sequence)));
 List<String> expected = Arrays.asList("abcdefg", "hello", "world!");
 assertEquals(expected, result);
 }

 @Test
 public void testOnFixedLengthSeparator() {
 CharSequence sequence = "abcdefgabcdefg";
 Splitter splitter = Splitter.fixedLength(3);
 List<String> result = new ArrayList<String>(Arrays.asList(splitter.split(sequence)));
 List<String> expected = Arrays.asList("abc", "def", "abc");
 assertEquals(expected, result);
 }

 @Test
 public void testOnEmptyStringSeparator() {
 CharSequence sequence = "ab   c   d   e   f";
 Splitter splitter = Splitter.on(CharMatcher.WHITESPACE);
 List<String> result = new ArrayList<String>(Arrays.asList(splitter.split(sequence)));
 List<String> expected = Arrays.asList("a", "b", "c", "d", "e", "f");
 assertEquals(expected, result);
 }

 @Test
 public void testOnNullSequence() {
 try {
 Splitter splitter = Splitter.on(' ');
 splitter.split(null);
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testOnInvalidPatternSeparator() {
 try {
 Splitter splitter = Splitter.onPattern("(.*");
 splitter.split("abcdefg");
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testSplitToList() {
 CharSequence sequence = "abcdefghello world!";
 List<String> result = Splitter.on(' ').splitToList(sequence);
 List<String> expected = Arrays.asList("abcdefg", "hello", "world!");
 assertEquals(expected, result);
 }

 @Test
 public void testMapSplitter() {
 CharSequence sequence = "key1=value1,key2=value2:key3=value3";
 Map<String, String> expected = new LinkedHashMap<String, String>() {{
 put("key1", "value1");
 put("key2", "value2");
 put("key3", "value3");
 }};
 Map<String, String> result = Splitter.on(',').withKeyValueSeparator(Splitter.on("=")).split(sequence).split();
 assertEquals(expected, result);
 }
}