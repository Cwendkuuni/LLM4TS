package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class SplitterTest {

 @Test
 public void testOnCharSeparator() {
 Splitter splitter = Splitter.on('-');
 List<String> result = splitter.split("test-string");
 assertEquals(Arrays.asList("test", "string"), result);
 }

 @Test
 public void testOnStringSeparator() {
 Splitter splitter = Splitter.on(":");
 List<String> result = splitter.split("test:string");
 assertEquals(Arrays.asList("test", "string"), result);
 }

 @Test
 public void testOnPatternSeparator() {
 Pattern pattern = Pattern.compile("\\s+");
 Splitter splitter = Splitter.onPattern(pattern);
 List<String> result = splitter.split("test string");
 assertEquals(Arrays.asList("test", "string"), result);
 }

 @Test
 public void testFixedLengthSplitter() {
 Splitter splitter = Splitter.fixedLength(2);
 List<String> result = splitter.split("teststringteststring");
 assertEquals(Arrays.asList("teststring", "teststring"), result);
 }

 @Test
 public void testSplitWithEmptyStrings() {
 Splitter splitter = Splitter.on(' ');
 List<String> result = splitter.split("test string");
 assertEquals(Arrays.asList("test", "", "string"), result);
 }

 @Test
 public void testSplitWithOmittedEmptyStrings() {
 Splitter splitter = Splitter.on(' ').omitEmptyStrings();
 List<String> result = splitter.split("test string");
 assertEquals(Arrays.asList("test", "string"), result);
 }

 @Test
 public void testSplitWithLimit() {
 Splitter splitter = Splitter.on(' ').limit(2);
 List<String> result = splitter.split("test string test string test string");
 assertEquals(Arrays.asList("test string", "test string"), result);
 }

 @Test
 public void testSplitWithCustomTrimmer() {
 Splitter splitter = Splitter.onPattern("\\s+").trimResults(Pattern.compile("^[a-z]"));
 List<String> result = splitter.split(" testString test string");
 assertEquals(Arrays.asList("testString", "test string"), result);
 }

 @Test
 public void testMapSplitter() {
 Pattern pattern = Pattern.compile(":");
 Splitter outerSplitter = Splitter.onPattern(pattern);
 Splitter keyValueSplitter = Splitter.on('=');
 MapSplitter mapSplitter = outerSplitter.withKeyValueSeparator(keyValueSplitter);
 String input = "test:name=John:age=30:city=New York";
 Map<String, String> result = mapSplitter.split(input);
 assertEquals(Arrays.asList("name=John", "age=30", "city=New York"), result.values());
 assertEquals(Arrays.asList("name", "age", "city"), result.keySet());
 }
}