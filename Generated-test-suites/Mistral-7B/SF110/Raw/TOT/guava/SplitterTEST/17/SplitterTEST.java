package com.google.common.base;

import com.google.common.base.Splitter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class SplitterTest {

 @Test
 public void testOnCharSeparator() {
 Splitter splitter = Splitter.on('-');
 String input = "a-b-c-d-e";
 Iterable<String> result = splitter.split(input);
 List<String> expected = Arrays.asList("a", "b", "c", "d", "e");
 assertEquals(expected, result);
 }

 @Test
 public void testOnEmptyStringSeparator() {
 Splitter splitter = Splitter.on("");
 String input = "a,b,c,d,e";
 Iterable<String> result = splitter.split(input);
 List<String> expected = Arrays.asList("a", "", "b", "", "c", "", "d", "", "e");
 assertEquals(expected, result);
 }

 @Test(expected = NullPointerException.class)
 public void testOnNullseparatorMatcher() {
 Splitter.on(null);
 }

 @Test
 public void testOnPatternSeparator() {
 Splitter splitter = Splitter.onPattern("\\s+");
 String input = "a b c d e";
 Iterable<String> result = splitter.split(input);
 List<String> expected = Arrays.asList("a", "b", "c", "d", "e");
 assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOnEmptyPattern() {
 Splitter.onPattern("");
 }

 @Test
 public void testOnPatternWithInvalidRegex() {
 try {
 Splitter.onPattern(".");
 fail("Should have thrown IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testFixedLengthSeparator() {
 Splitter splitter = Splitter.fixedLength(3);
 String input = "1234567890";
 Iterable<String> result = splitter.split(input);
 List<String> expected = Arrays.asList("123", "456", "789");
 assertEquals(expected, result);
 }

 @Test
 public void testLimit() {
 Splitter splitter = Splitter.on('-').limit(3);
 String input = "a-b-c-d-e-f-g-h-i-j-k-l-m-n-o-p-q-r-s-t-u-v-w-x-y-z";
 Iterable<String> result = splitter.split(input);
 List<String> expected = Arrays.asList("a-b-c", "d-e-f", "g-h-i");
 assertEquals(expected, result);
 }

 @Test
 public void testTrimEmptyStrings() {
 Splitter splitter = Splitter.on(" ").omitEmptyStrings();
 String input = " a b c d e ";
 Iterable<String> result = splitter.split(input);
 List<String> expected = Arrays.asList("a", "b", "c", "d", "e");
 assertEquals(expected, result);
 }

 @Test
 public void testTrimResults() {
 Splitter splitter = Splitter.on("-").limit(2).trimResults();
 String input = "a---b----c---d---e---f---g---h---i---j---k---l---m---n---o---p---q---r---s---t---u---v---w---x---y---z";
 Iterable<String> result = splitter.split(input);
 List<String> expected = Arrays.asList("a--b", "c--d");
 assertEquals(expected, result);
 }

 @Test
 public void testWithKeyValueSeparator() {
 Splitter keyValueSplitter = Splitter.on(":");
 Splitter splitter = Splitter.withKeyValueSeparator(keyValueSplitter);
 String input = "key1:value1:key2:value2:key3:value3";
 Map<String, String> result = splitter.split(input);
 Map<String, String> expected = new HashMap<String, String>(){{
 put("key1", "value1");
 put("key2", "value2");
 put("key3", "value3");
 }};
 assertEquals(expected, result);
 }

}