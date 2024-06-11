package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class SplitterTest {

 @Test
 public void testOnCharSeparator() {
 String input = "a-b-c-d-e";
 Splitter splitter = Splitter.on('-');
 List<String> output = splitter.split(input);
 assertEquals(Arrays.asList("a", "b", "c", "d", "e"), output);
 }

 @Test
 public void testOnStringSeparator() {
 String input = "apple:banana:orange:grape";
 Splitter splitter = Splitter.on(":");
 List<String> output = splitter.split(input);
 assertEquals(Arrays.asList("apple", "banana", "orange", "grape"), output);
 }

 @Test
 public void testOnPatternSeparator() {
 String input = "apple|banana|orange|grape";
 Pattern separatorPattern = Pattern.compile("\\|");
 Splitter splitter = Splitter.on(separatorPattern);
 List<String> output = splitter.split(input);
 assertEquals(Arrays.asList("apple", "banana", "orange", "grape"), output);
 }

 @Test
 public void testOnEmptyStringSeparator() {
 String input = "apple   banana   orange   grape";
 Splitter splitter = Splitter.on("   ");
 List<String> output = splitter.split(input);
 assertEquals(Arrays.asList("apple", "", "banana", "", "orange", "", "grape"), output);
 }

 @Test(expected = NullPointerException.class)
 public void testOnNullInput() {
 Splitter splitter = Splitter.on('-');
 splitter.split(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOnEmptySeparator() {
 String input = "apple";
 Splitter splitter = Splitter.on("");
 try {
 List<String> output = splitter.split(input);
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOnEmptyInput() {
 String input = "";
 Splitter splitter = Splitter.on('-');
 try {
 List<String> output = splitter.split(input);
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testLimit() {
 String input = "a-b-c-d-e-f-g-h-i-j-k-l-m-n-o-p-q-r-s-t-u-v-w-x-y-z";
 Splitter splitter = Splitter.on('-').limit(5);
 List<String> output = splitter.split(input);
 assertEquals(Arrays.asList("a-b-c-d-e", "f-g-h-i-j"), output);
 }

 @Test
 public void testOmitEmptyStrings() {
 String input = "apple:  :orange:grape:";
 Splitter splitter = Splitter.on(":").omitEmptyStrings();
 List<String> output = splitter.split(input);
 assertEquals(Arrays.asList("apple", "orange", "grape"), output);
 }

 @Test
 public void testTrimResults() {
 String input = "apple   : banana   : orange   : grape";
 Splitter splitter = Splitter.on(":").trimResults();
 List<String> output = splitter.splitToList(input);
 assertEquals(Arrays.asList("apple", "banana", "orange", "grape"), output);
 }

 @Test
 public void testTrimResultsWithWhiteSpace() {
 String input = "apple   : banana   : orange   : grape";
 Splitter splitter = Splitter.on(":").trimResults(CharMatcher.WHITESPACE);
 List<String> output = splitter.splitToList(input);
 assertEquals(Arrays.asList("apple", "banana", "orange", "grape"), output);
 }
}