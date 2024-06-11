package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class SplitterTest {

 @Test
 public void testOnCharSeparator() {
 CharSequence input = "hello-world-hello";
 Splitter splitter = Splitter.on('-');
 List<String> expectedOutput = Arrays.asList("hello", "world", "hello");
 List<String> output = splitter.split(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testOnCharMatcherSeparator() {
 CharSequence input = "100-200-300";
 CharMatcher separatorMatcher = CharMatcher.is('-');
 Splitter splitter = Splitter.on(separatorMatcher);
 List<String> expectedOutput = Arrays.asList("100", "200", "300");
 List<String> output = splitter.split(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testOnStringSeparator() {
 CharSequence input = "apple banana apple";
 Splitter splitter = Splitter.on(" ");
 List<String> expectedOutput = Arrays.asList("apple", "banana", "apple");
 List<String> output = splitter.split(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testOnPatternSeparator() {
 CharSequence input = "apple:banana:apple";
 Pattern separatorPattern = Pattern.compile(":");
 Splitter splitter = Splitter.on(separatorPattern);
 List<String> expectedOutput = Arrays.asList("apple", "banana", "apple");
 List<String> output = splitter.split(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testOnEmptyString() {
 CharSequence input = "";
 Splitter splitter = Splitter.onPattern("\\s+");
 List<String> expectedOutput = Arrays.asList();
 List<String> output = splitter.split(input);
 assertEquals(expectedOutput, output);
 }

 @Test(expected = NullPointerException.class)
 public void testNullInput() {
 Splitter splitter = Splitter.on('-');
 splitter.split(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptySeparator() {
 Splitter splitter = Splitter.on("");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullSeparatorPattern() {
 Pattern separatorPattern = null;
 Splitter splitter = Splitter.onPattern(separatorPattern);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidRegexSeparatorPattern() {
 Pattern separatorPattern = Pattern.compile("[\\];");
 Splitter splitter = Splitter.onPattern(separatorPattern);
 }

 @Test(expected = IllegalStateException.class)
 public void testLimitReached() {
 CharSequence input = "123456789012345678901234567890";
 Splitter splitter = Splitter.onPattern("\\s+").limit(5);
 List<String> expectedOutput = Arrays.asList("12345", "67890");
 List<String> output = splitter.split(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testOmitEmptyStrings() {
 CharSequence input = "apple  : banana :  apple";
 Splitter splitter = Splitter.onPattern("\\s+").omitEmptyStrings();
 List<String> expectedOutput = Arrays.asList("apple", "banana", "apple");
 List<String> output = splitter.split(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testFixedLengthSplitter() {
 CharSequence input = "1234567890123456789012345678901234567890";
 Splitter splitter = Splitter.fixedLength(4);
 List<String> expectedOutput = Arrays.asList("1234", "5678", "9012", "3456", "7890", "1234", "5678", "9012", "3456", "7890");
 List<String> output = splitter.split(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testCustomMapSplitterWithKeyValueSeparator() {
 CharSequence input = "key1=value1&key2=value2&key3=value3";
 Splitter keyValueSplitter = Splitter.on("=");
 Map<String, String> expectedOutput = new LinkedHashMap<String, String>() {{
 put("key1", "value1");put("key2", "value2");put("key3", "value3");
 }};
 Map<String, String> output = new MapSplitter(Splitter.on('&'), keyValueSplitter).split(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testCustomMapSplitterWithInvalidEntry() {
 CharSequence input = "key1=value1&invalidEntry";
 Splitter keyValueSplitter = Splitter.on("=");
 Map<String, String> expectedOutput = new LinkedHashMap<String, String>() {{
 put("key1", "value1");
 }};
 Map<String, String> output = new MapSplitter(Splitter.on('&'), keyValueSplitter).split(input);
 assertEquals(expectedOutput, output);
 }
}