package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class SplitterTest {

 // Test for on(char separator)
 @Test
 public void testSplitterOnCharSeparator() {
 String input = "one,two,three,four,five";
 Splitter splitter = Splitter.on(',').trimResults();
 List<String> result = splitter.splitToList(input);
 List<String> expected = Arrays.asList("one", "two", "three", "four", "five");
 assertEquals(expected, result);
 }

 @Test
 public void testSplitterOnCharSeparator_EmptyString() {
 String input = "";
 Splitter splitter = Splitter.on(',').omitEmptyStrings();
 List<String> result = splitter.splitToList(input);
 assertTrue(result.isEmpty());
 }

 @Test
 public void testSplitterOnCharSeparator_NullInput() {
 String input = null;
 Splitter splitter = Splitter.on(',').omitEmptyStrings();
 List<String> result = splitter.splitToList(input);
 assertNull(result);
 }

 // Test for on(CharMatcher separatorMatcher)
 @Test
 public void testSplitterOnCharMatcher() {
 String input = "apples,oranges,apples,bananas";
 Splitter splitter = Splitter.on(CharMatcher.is(','));
 List<String> result = splitter.splitToList(input);
 List<String> expected = Arrays.asList("apples", "oranges", "apples", "bananas");
 assertEquals(expected, result);
 }

 @Test
 public void testSplitterOnCharMatcher_EmptyString() {
 String input = "";
 Splitter splitter = Splitter.on(CharMatcher.is(',')).omitEmptyStrings();
 List<String> result = splitter.splitToList(input);
 assertTrue(result.isEmpty());
 }

 @Test
 public void testSplitterOnCharMatcher_NullInput() {
 String input = null;
 Splitter splitter = Splitter.on(CharMatcher.is(',')).omitEmptyStrings();
 List<String> result = splitter.splitToList(input);
 assertNull(result);
 }

 @Test
 public void testSplitterOnCharMatcher_SeparatorNotPresent() {
 String input = "applesorangesapplesbananas";
 Splitter splitter = Splitter.on(CharMatcher.is(','));
 List<String> result = splitter.splitToList(input);
 List<String> expected = Arrays.asList("applesorangesapplesbananas");
 assertEquals(expected, result);
 }

 // Test for on(String separator)
 @Test
 public void testSplitterOnStringSeparator() {
 String input = "one:two:three:four:five";
 Splitter splitter = Splitter.on(":").trimResults();
 List<String> result = splitter.splitToList(input);
 List<String> expected = Arrays.asList("one", "two", "three", "four", "five");
 assertEquals(expected, result);
 }

 @Test
 public void testSplitterOnStringSeparator_EmptyString() {
 String input = "";
 Splitter splitter = Splitter.on(":").omitEmptyStrings();
 List<String> result = splitter.splitToList(input);
 assertTrue(result.isEmpty());
 }

 @Test
 public void testSplitterOnStringSeparator_NullInput() {
 String input = null;
 Splitter splitter = Splitter.on(":").omitEmptyStrings();
 List<String> result = splitter.splitToList(input);
 assertNull(result);
 }

 // Test for on(Pattern separatorPattern)
 @Test
 public void testSplitterOnPatternSeparator() {
 String input = "apple|banana|orange|apple|grape";
 Pattern separatorPattern = Pattern.compile("\\|");
 Splitter splitter = Splitter.onPattern(separatorPattern).trimResults();
 List<String> result = splitter.splitToList(input);
 List<String> expected = Arrays.asList("apple", "banana", "orange", "apple", "grape");
 assertEquals(expected, result);
 }

 @Test
 public void testSplitterOnPatternSeparator_EmptyString() {
 String input = "";
 Pattern separatorPattern = Pattern.compile("\\|");
 Splitter splitter = Splitter.onPattern(separatorPattern).omitEmptyStrings();
 List<String> result = splitter.splitToList(input);
 assertTrue(result.isEmpty());
 }

 @Test
 public void testSplitterOnPatternSeparator_NullInput() {
 String input = null;
 Pattern separatorPattern = Pattern.compile("\\|");
 Splitter splitter = Splitter.onPattern(separatorPattern).omitEmptyStrings();
 List<String> result = splitter.splitToList(input);
 assertNull(result);
 }

 @Test
 public void testSplitterOnPatternSeparator_SeparatorNotPresent() {
 String input = "applebananaorangeapplegrape";
 Pattern separatorPattern = Pattern.compile("\\|");
 Splitter splitter = Splitter.onPattern(separatorPattern);
 List<String> result = splitter.splitToList(input);
 List<String> expected = Arrays.asList("applebananaorangeapplegrape");
 assertEquals(expected, result);
 }

 // Test for fixedLength(int length)
 @Test
 public void testSplitterFixedLength() {
 String input = "applebananaorangeapplegrape";
 Splitter splitter = Splitter.fixedLength(3);
 List<String> result = splitter.splitToList(input);
 List<String> expected = Arrays.asList("app", "ban", "or", "app", "grape");
 assertEquals(expected, result);
 }

 @Test
 public void testSplitterFixedLength_EmptyString() {
 String input = "";
 Splitter splitter = Splitter.fixedLength(3).omitEmptyStrings();
 List<String> result = splitter.splitToList(input);
 assertTrue(result.isEmpty());
 }

 @Test
 public void testSplitterFixedLength_NullInput() {
 String input = null;
 Splitter splitter = Splitter.fixedLength(3).omitEmptyStrings();
 List<String> result = splitter.splitToList(input);
 assertNull(result);
 }

 // Test for omitEmptyStrings()
 @Test
 public void testSplitterOmitEmptyStrings() {
 String input = "one, ,two, three, ,four,five";
 Splitter splitter = Splitter.on(',').omitEmptyStrings();
 List<String> result = splitter.splitToList(input);
 List<String> expected = Arrays.asList("one", "two", "three", "four", "five");
 assertEquals(expected, result);
 }

 @Test
 public void testSplitterOmitEmptyStrings_EmptyString() {
 String input = "";
 Splitter splitter = Splitter.on(',').omitEmptyStrings();
 List<String> result = splitter.splitToList(input);
 assertTrue(result.isEmpty());
 }

 @Test
 public void testSplitterOmitEmptyStrings_NullInput() {
 String input = null;
 Splitter splitter = Splitter.on(',').omitEmptyStrings();
 List<String> result = splitter.splitToList(input);
 assertNull(result);
 }

 // Test for limit(int limit)
 @Test
 public void testSplitterLimit() {
 String input = "one,two,three,four,five,six,seven,eight,nine,ten";
 Splitter splitter = Splitter.on(',').limit(5);
 List<String> result = new ArrayList<String>(splitter.splitToList(input));
 List<String> expected = Arrays.asList("one", "two", "three", "four", "five");
 assertEquals(expected, result);
 }

 @Test
 public void testSplitterLimit_EmptyString() {
 String input = "";
 Splitter splitter = Splitter.on(',').limit(5).omitEmptyStrings();
 List<String> result = splitter.splitToList(input);
 assertTrue(result.isEmpty());
 }

 @Test
 public void testSplitterLimit_NullInput() {
 String input = null;
 Splitter splitter = Splitter.on(',').limit(5).omitEmptyStrings();
 List<String> result = splitter.splitToList(input);
 assertNull(result);
 }

 @Test
 public void testSplitterLimit_LimitReached() {
 String input = "one,two,three,four,five,six,seven,eight,nine,ten,eleven,twelve";
 Splitter splitter = Splitter.on(',').limit(5);
 List<String> result = new ArrayList<String>(splitter.splitToList(input));
 List<String> expected = Arrays.asList("one", "two", "three", "four", "five");
 assertEquals(expected, result);
 }

 @Test
 public void testSplitterLimit_LimitNotReached() {
 String input = "one,two,three,four,five,six,seven,eight,nine,ten,eleven,twelve";
 Splitter splitter = Splitter.on(',').limit(8);
 List<String> result = new ArrayList<String>(splitter.splitToList(input));
 List<String> expected = Arrays.asList("one", "two", "three", "four", "five", "six", "seven", "eight");
 assertEquals(expected, result);
 }

 // Test for trimResults()
 @Test
 public void testSplitterTrimResults() {
 String input = " one,two two,three three,four four,five five";
 Splitter splitter = Splitter.on(',').trimResults();
 List<String> result = splitter.splitToList(input);
 List<String> expected = Arrays.asList("one", "two", "three", "four", "five");
 assertEquals(expected, result);
 }

 @Test
 public void testSplitterTrimResults_EmptyString() {
 String input = "";
 Splitter splitter = Splitter.on(',').trimResults().omitEmptyStrings();
 List<String> result = splitter.splitToList(input);
 assertTrue(result.isEmpty());
 }

 @Test
 public void testSplitterTrimResults_NullInput() {
 String input = null;
 Splitter splitter = Splitter.on(',').trimResults().omitEmptyStrings();
 List<String> result = splitter.splitToList(input);
 assertNull(result);
 }

 // Test for withKeyValueSeparator(String separator)
 @Test
 public void testSplitterWithKeyValueSeparator() {
 String input = "name=John,age=30,city=New York";
 Splitter splitter = Splitter.on(',').withKeyValueSeparator("=");
 Map<String, String> result = splitter.split(input);
 Map<String, String> expected = new LinkedHashMap<String, String>() {{
 put("name", "John");
 put("age", "30");
 put("city", "New York");
 }};
 assertEquals(expected, result);
 }

 @Test
 public void testSplitterWithKeyValueSeparator_EmptyString() {
 String input = "";
 Splitter splitter = Splitter.on(',').withKeyValueSeparator("=");
 Map<String, String> result = splitter.split(input);
 assertTrue(result.isEmpty());
 }

 @Test
 public void testSplitterWithKeyValueSeparator_NullInput() {
 String input = null;
 Splitter splitter = Splitter.on(',').withKeyValueSeparator("=");
 Map<String, String> result = splitter.split(input);
 assertNull(result);
 }

 @Test
 public void testSplitterWithKeyValueSeparator_InvalidEntry() {
 String input = "name=John,age,city=New York";
 Splitter splitter = Splitter.on(',').withKeyValueSeparator("=");
 try {
 Map<String, String> result = splitter.split(input);
 fail("Invalid entry, expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 String message = e.getMessage();
 String expectedMessage = "Chunk [age] is not a valid entry";
 assertThat(message, equalTo(expectedMessage));
 }
 }

}