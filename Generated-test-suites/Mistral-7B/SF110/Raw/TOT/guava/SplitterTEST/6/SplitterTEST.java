package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;;

public class SplitterTest {

 @Test
 public void testOnCharSeparator() {
 String input = "hello-world-hello";
 Splitter splitter = Splitter.on('-');
 List<String> result = splitter.split(input);
 assertEquals(Arrays.asList("hello", "world", "hello"), result);
 }

 @Test
 public void testOnCharSeparator_emptyString() {
 String input = "";
 Splitter splitter = Splitter.on('-');
 List<String> result = splitter.split(input);
 assertEquals(Collections.emptyList(), result);
 }

 @Test
 public void testOnCharSeparator_omitEmptyStrings() {
 String input = "hello-world-hello-";
 Splitter splitter = Splitter.on('-').omitEmptyStrings();
 List<String> result = splitter.split(input);
 assertEquals(Arrays.asList("hello", "world"), result);
 }

 @Test
 public void testOnPatternSeparator() {
 String input = "hello|world|hello";
 Pattern pattern = Pattern.compile("\\|");
 Splitter splitter = Splitter.onPattern(pattern);
 List<String> result = splitter.split(input);
 assertEquals(Arrays.asList("hello", "world", "hello"), result);
 }

 @Test(expected = NullPointerException.class)
 public void testOnPatternSeparator_nullPattern() {
 String input = "hello|world|hello";
 Splitter splitter = Splitter.onPattern((Pattern) null);
 }

 @Test
 public void testOnStringSeparator() {
 String input = "hello|world|hello";
 Splitter splitter = Splitter.on("|");
 List<String> result = splitter.split(input);
 assertEquals(Arrays.asList("hello", "world", "hello"), result);
 }

 @Test
 public void testOnStringSeparator_emptyString() {
 String input = "";
 Splitter splitter = Splitter.on("|");
 List<String> result = splitter.split(input);
 assertEquals(Collections.emptyList(), result);
 }

 @Test
 public void testFixedLength() {
 String input = "1234567890";
 Splitter splitter = Splitter.fixedLength(3);
 List<String> result = splitter.split(input);
 assertEquals(Arrays.asList("123", "456", "789"), result);
 }

 @Test
 public void testFixedLength_lengthLessThanOne() {
 String input = "1234567890";
 Splitter splitter = Splitter.fixedLength(0);
 List<String> result = splitter.split(input);
 assertEquals(Collections.emptyList(), result);
 }

 @Test
 public void testLimit() {
 String input = "1234567890";
 Splitter splitter = Splitter.on(' ').limit(2);
 List<String> result = splitter.split(input);
 assertEquals(Arrays.asList("123", "456"), result);
 }

 @Test
 public void testTrimResults() {
 String input = " hello world hello ";
 Splitter splitter = Splitter.on(' ').trimResults();
 List<String> result = splitter.splitToList(input);
 assertEquals(Arrays.asList("hello", "world", "hello"), result);
 }

 @Test
 public void testTrimResults_nullTrimmer() {
 String input = " hello world hello ";
 Splitter splitter = Splitter.on(' ').trimResults((CharMatcher) null);
 List<String> result = splitter.splitToList(input);
 assertEquals(Arrays.asList("hello", "world", "hello"), result);
 }

 @Test
 public void testMapSplitter() {
 String input = "key1=value1&key2=value2&key3=value3";
 Splitter keyValueSplitter = Splitter.on("=").omitEmptyStrings();
 Map<String, String> result = new Splitter(Splitter.on('&')).withKeyValueSeparator(keyValueSplitter).split(input);
 assertEquals(Arrays.asList(Arrays.asList("key1", "value1"), Arrays.asList("key2", "value2"), Arrays.asList("key3", "value3")), result.entrySet());
 }

 @Test
 public void testMapSplitter_trailingAmpersand() {
 String input = "key1=value1&key2=value2&key3=value3&";
 Splitter keyValueSplitter = Splitter.on("=").omitEmptyStrings();
 Map<String, String> result = new Splitter(Splitter.on('&')).withKeyValueSeparator(keyValueSplitter).split(input);
 assertEquals(Arrays.asList(Arrays.asList("key1", "value1"), Arrays.asList("key2", "value2"), Arrays.asList("key3", "value3")), result.entrySet());
 }

}