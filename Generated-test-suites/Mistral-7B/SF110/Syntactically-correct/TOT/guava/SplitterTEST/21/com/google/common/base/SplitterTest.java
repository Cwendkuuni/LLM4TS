package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SplitterTest {

 @Test
 public void testOnCharSeparator() {
 CharSequence input = "one,two,three,four";
 Splitter splitter = Splitter.on(',').trimResults();
 List<String> result = splitter.split(input);
 assertEquals(Arrays.asList("one", "two", "three", "four"), result);
 }

 @Test
 public void testOnCharMatcherSeparator() {
 CharSequence input = "one--two--three--four";
 Splitter splitter = Splitter.on(CharMatcher.forRegex("--"));
 List<String> result = splitter.split(input);
 assertEquals(Arrays.asList("one", "two", "three", "four"), result);
 }

 @Test
 public void testOnPatternSeparator() {
 CharSequence input = "apples,oranges,bananas";
 Pattern separatorPattern = Pattern.compile(",");
 Splitter splitter = Splitter.onPattern(separatorPattern);
 List<String> result = splitter.split(input);
 assertEquals(Arrays.asList("apples", "oranges", "bananas"), result);
 }

 @Test
 public void testOnEmptyStringSeparator() {
 CharSequence input = "apple orange banana";
 Splitter splitter = Splitter.on(" ");
 List<String> result = splitter.split(input);
 assertEquals(Arrays.asList("apple", "orange", "banana"), result);
 }

 @Test
 public void testOnFixedLengthSeparator() {
 CharSequence input = "xxxxapplexxxxorangexxxxbananaxxxx";
 Splitter splitter = Splitter.fixedLength(4);
 List<String> result = splitter.split(input);
 assertEquals(Arrays.asList("apple", "orange", "banana"), result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOnNullSeparator() {
 // This test case should throw an IllegalArgumentException
 Splitter splitter = Splitter.on(null);
 }

 @Test
 public void testTrimming() {
 CharSequence input = " one two three four ";
 Splitter splitter = Splitter.on(' ').trimResults();
 List<String> result = splitter.split(input);
 assertEquals(Arrays.asList("one", "two", "three", "four"), result);
 }

 @Test
 public void testLimiting() {
 CharSequence input = "one,two,three,four,five,six,seven,eight,nine,ten";
 Splitter splitter = Splitter.on(',').limit(5);
 List<String> result = splitter.split(input);
 assertEquals(Arrays.asList("one", "two", "three", "four", "five"), result);
 }

 @Test
 public void testOmittingEmptyStrings() {
 CharSequence input = "one, ,three,four";
 Splitter splitter = Splitter.on(',').omitEmptyStrings();
 List<String> result = splitter.split(input);
 assertEquals(Arrays.asList("one", "three", "four"), result);
 }

 @Test
 public void testMapSplitter() {
 CharSequence input = "key1=value1,key2=value2,key3=value3";
 Splitter keyValueSplitter = Splitter.on(Pattern.compile("="));
 Map<String, String> result = new Splitter(Splitter.on(','), keyValueSplitter).withKeyValueSeparator().split(input);
 assertEquals(Arrays.asList("key1=value1", "key2=value2", "key3=value3"), result.keySet());
 assertEquals(Arrays.asList("value1", "value2", "value3"), result.values());
 }

 @Test
 public void testErrorHandling() {
 CharSequence input = "";
 Splitter splitter = Splitter.onPattern("");
 List<String> result = splitter.split(input);
 fail("Expected IllegalArgumentException but got " + result);
 }

}