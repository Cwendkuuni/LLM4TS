package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class SplitterTest {

 @Test
 public void testOnCharSeparator() {
 String input = "one,two,three,four";
 Splitter splitter = Splitter.on(',').trimResults();
 List<String> result = splitter.split(input);
 List<String> expected = Arrays.asList("one", "two", "three", "four");
 assertEquals(expected, result);
 }

 @Test
 public void testOnStringSeparator() {
 String input = "one,two,three,four";
 Splitter splitter = Splitter.on(",");
 List<String> result = splitter.split(input);
 List<String> expected = Arrays.asList("one", "two", "three", "four");
 assertEquals(expected, result);
 }

 @Test
 public void testOnPatternSeparator() {
 String input = "one-two-three-four";
 Splitter splitter = Splitter.onPattern("\\-");
 List<String> result = new ArrayList<String>();
 result.addAll(splitter.splitToList(input));
 List<String> expected = Arrays.asList("one", "two", "three", "four");
 assertEquals(expected, result);
 }

 @Test(expected = NullPointerException.class)
 public void testNullInput() {
 Splitter splitter = Splitter.on(',');
 try {
 splitter.split(null);
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyInput() {
 String input = "";
 Splitter splitter = Splitter.on(',');
 try {
 splitter.split(input);
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testLimitReached() {
 String input = "onetwothreefourfive";
 Splitter splitter = Splitter.on(',').limit(2);
 List<String> result = new ArrayList<String>();
 result.addAll(splitter.splitToList(input));
 List<String> expected = Arrays.asList("one", "two");
 assertEquals(expected, result);
 }

 @Test
 public void testOmitEmptyStrings() {
 String input = "one,,three,four";
 Splitter splitter = Splitter.on(',').omitEmptyStrings();
 List<String> result = new ArrayList<String>();
 result.addAll(splitter.splitToList(input));
 List<String> expected = Arrays.asList("one", "three", "four");
 assertEquals(expected, result);
 }

 @Test
 public void testKeyValueSplitter() {
 String input = "key1=value1,key2=value2,key3=value3";
 Splitter keyValueSplitter = Splitter.on("=").limit(2);
 Splitter outerSplitter = Splitter.on(",");
 MapSplitter mapSplitter = outerSplitter.withKeyValueSeparator(keyValueSplitter);
 Map<String, String> result = mapSplitter.split(input);
 Map<String, String> expected = new LinkedHashMap<String, String>() {{
 put("key1", "value1");
 put("key2", "value2");
 put("key3", "value3");
 }};
 assertEquals(expected, result);
 }
}