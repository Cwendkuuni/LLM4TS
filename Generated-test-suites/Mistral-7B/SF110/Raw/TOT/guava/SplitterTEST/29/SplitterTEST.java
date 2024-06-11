package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class SplitterTest {

 @Test
 public void testCharSeparator() {
 Splitter charSplitter = Splitter.on('-');
 List<String> result = charSplitter.split("test-one-two-three");
 assertEquals(Arrays.asList("test", "one", "two", "three"), result);
 }

 @Test
 public void testEmptyCharSeparator() {
 Splitter emptyCharSplitter = Splitter.on('');
 List<String> result = emptyCharSplitter.split("test");
 assertEquals(Arrays.asList("test"), result);
 }

 @Test
 public void testNullSequence() {
 assertNull(Splitter.on('-').split(null));
 }

 @Test
 public void testInvalidCharSeparator() {
 try {
 Splitter invalidCharSplitter = Splitter.on((char)0);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testStringSeparator() {
 Splitter stringSplitter = Splitter.on(":");
 List<String> result = stringSplitter.split("key:value:anotherKey:value");
 assertEquals(Arrays.asList("key", "value", "anotherKey", "value"), result);
 }

 @Test
 public void testEmptyStringSeparator() {
 Splitter emptyStringSplitter = Splitter.on("");
 List<String> result = emptyStringSplitter.split("test");
 assertEquals(Arrays.asList("test"), result);
 }

 @Test
 public void testNullSeparator() {
 try {
 Splitter nullSeparatorSplitter = Splitter.on(null);
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testInvalidPattern() {
 try {
 Splitter invalidPatternSplitter = Splitter.onPattern("\\.");
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testPatternSeparator() {
 Pattern pattern = Pattern.compile(":");
 Splitter patternSplitter = Splitter.onPattern(pattern);
 List<String> result = patternSplitter.split("key:value:anotherKey:value");
 assertEquals(Arrays.asList("key", "value", "anotherKey", "value"), result);
 }

 @Test
 public void testFixedLengthSeparator() {
 Splitter fixedLengthSplitter = Splitter.fixedLength(3);
 List<String> result = fixedLengthSplitter.split("abcdefghijklmnopqrstuvwxyz");
 assertEquals(Arrays.asList("abc", "def", "ghi", "jkl", "mno", "pqr", "stu", "vwxy", "z"), result);
 }

 @Test
 public void testLimit() {
 Splitter limitSplitter = Splitter.on('-').limit(5);
 List<String> result = limitSplitter.split("test-one-two-three-four-five-six-seven-eight");
 assertEquals(Arrays.asList("test-one", "two-three", "four-five"), result);
 }

 @Test
 public void testTrimResults() {
 Splitter trimResultsSplitter = Splitter.on('-').trimResults();
 List<String> result = trimResultsSplitter.split(" test-one test-two test-three ");
 assertEquals(Arrays.asList("test-one", "test-two", "test-three"), result);
 }

 @Test
 public void testMapSplitter() {
 Splitter keyValueSplitter = Splitter.on("=").limit(2);
 Map<String, String> map = new Splitter.MapSplitter(Splitter.on('-'), keyValueSplitter).split("key1=value1-key2=value2-key3=value3");
 assertEquals(Arrays.asList(Arrays.asList("key1", "value1"), Arrays.asList("key2", "value2"), Arrays.asList("key3", "value3")), map.entrySet());
 }

}