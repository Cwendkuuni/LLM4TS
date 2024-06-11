package com.google.common.base;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class SplitterTest {

 private Splitter charSeparatorSplitter;
 private Splitter stringSeparatorSplitter;
 private Splitter patternSeparatorSplitter;

 @Before
 public void setup() {
 charSeparatorSplitter = Splitter.on('-');
 stringSeparatorSplitter = Splitter.on(",");
 patternSeparatorSplitter = Splitter.onPattern("\\s+");
 }

 @Test
 public void testCharSeparator() {
 String input = "apple-banana-orange";
 List<String> expected = Arrays.asList("apple", "banana", "orange");
 List<String> result = charSeparatorSplitter.split(input).splitToList();
 assertEquals(expected, result);
 }

 @Test
 public void testEmptyString() {
 String input = "";
 List<String> expected = Collections.emptyList();
 List<String> result = charSeparatorSplitter.limit(0).split(input).splitToList();
 assertEquals(expected, result);
 }

 @Test
 public void testStringSeparator() {
 String input = "apple,banana,orange";
 List<String> expected = Arrays.asList("apple", "banana", "orange");
 List<String> result = stringSeparatorSplitter.split(input).splitToList();
 assertEquals(expected, result);
 }

 @Test
 public void testEmptyStringInList() {
 String input = "apple, ,orange";
 List<String> expected = Arrays.asList("apple", "", "orange");
 List<String> result = stringSeparatorSplitter.omitEmptyStrings().split(input).splitToList();
 assertEquals(expected, result);
 }

 @Test
 public void testPatternSeparator() {
 String input = "apple   banana orange";
 List<String> expected = Arrays.asList("apple", "banana", "orange");
 List<String> result = patternSeparatorSplitter.split(input).splitToList();
 assertEquals(expected, result);
 }

 @Test(expected = NullPointerException.class)
 public void testNullInput() {
 Splitter splitter = Splitter.on('-');
 try {
 splitter.split(null);
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPatternEmptyString() {
 try {
 Splitter.onPattern("");
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPatternNotMatching() {
 try {
 Splitter.onPattern("\\d");
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }
}