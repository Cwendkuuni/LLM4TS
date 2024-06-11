package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.regex.Pattern;

public class SplitterTest {

 @Test
 public void testOnCharSeparator() {
 Splitter splitter = Splitter.on('-');
 String input = "key1-value1-key2-value2-key3-value3";
 Iterable<String> result = splitter.split(input);
 List<String> expectedResult = Arrays.asList("key1", "value1", "key2", "value2", "key3", "value3");
 assertIterableEquals(expectedResult, result);
 }

 @Test
 public void testOnCharMatcherSeparator() {
 Splitter splitter = Splitter.on(CharMatcher.is('-'));
 String input = "key1--value1-key2--value2-key3--value3";
 Iterable<String> result = splitter.split(input);
 List<String> expectedResult = Arrays.asList("key1", "value1", "key2", "value2", "key3", "value3");
 assertIterableEquals(expectedResult, result);
 }

 @Test
 public void testOnStringSeparator() {
 Splitter splitter = Splitter.on("-");
 String input = "key1=value1&key2=value2&key3=value3";
 Iterable<String> result = splitter.split(input);
 List<String> expectedResult = Arrays.asList("key1=value1", "key2=value2", "key3=value3");
 assertIterableEquals(expectedResult, result);
 }

 @Test
 public void testOnPatternSeparator() {
 Splitter splitter = Splitter.onPattern("\\s+");
 String input = "key1 value1   \tvalue2   key2 value2";
 Iterable<String> result = splitter.split(input);
 List<String> expectedResult = Arrays.asList("key1", "value1", "key2", "value2");
 assertIterableEquals(expectedResult, result);
 }

 @Test
 public void testOnEmptyStringSeparator() {
 Splitter splitter = Splitter.on(" ");
 String input = "key1 value1 key2 value2 key3 value3";
 Iterable<String> result = splitter.split(input);
 List<String> expectedResult = Arrays.asList("key1", "", "value1", "", "key2", "", "value2", "", "key3", "", "value3");
 assertIterableEquals(expectedResult, result);
 }

 @Test(expected = NullPointerException.class)
 public void testNullInput() {
 Splitter splitter = Splitter.on('-');
 splitter.split(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptySeparator() {
 Splitter splitter = Splitter.on("");
 try {
 splitter.split("test");
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidPattern() {
 Pattern invalidPattern = Pattern.compile(".*");
 Splitter splitter = Splitter.on(invalidPattern);
 try {
 splitter.split("test");
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testSplitWithLimit() {
 Splitter splitter = Splitter.on('-').limit(2);
 String input = "key1-value1-key2-value2-key3-value3-key4-value4-key5-value5";
 Iterable<String> result = splitter.split(input);
 List<String> expectedResult = Arrays.asList("key1-value1", "key2-value2");
 assertIterableEquals(expectedResult, result);
 }
}