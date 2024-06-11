package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class SplitterTest {

 @Test
 public void testOnCharSeparator() {
 String input = "apple-banana-orange-grape";
 Splitter splitter = Splitter.on('-');
 List<String> expected = Arrays.asList("apple", "banana", "orange", "grape");
 List<String> result = new ArrayList<>();
 for (String s : splitter.split(input)) {
 result.add(s);
 }
 assertEquals(expected, result);
 }

 @Test
 public void testOnStringSeparator() {
 String input = "apple:banana:orange:grape";
 Splitter splitter = Splitter.on(":");
 List<String> expected = Arrays.asList("apple", "banana", "orange", "grape");
 List<String> result = new ArrayList<>();
 for (String s : splitter.split(input)) {
 result.add(s);
 }
 assertEquals(expected, result);
 }

 @Test
 public void testOnPatternSeparator() {
 String input = "apple|banana|orange|grape";
 Pattern separatorPattern = Pattern.compile("|");
 Splitter splitter = Splitter.onPattern(separatorPattern);
 List<String> expected = Arrays.asList("apple", "banana", "orange", "grape");
 List<String> result = new ArrayList<>();
 for (String s : splitter.split(input)) {
 result.add(s);
 }
 assertEquals(expected, result);
 }

 @Test
 public void testOnEmptyStringSeparator() {
 String input = "apple";
 Splitter splitter = Splitter.on("");
 List<String> expected = Arrays.asList("apple");
 List<String> result = new ArrayList<>();
 for (String s : splitter.split(input)) {
 result.add(s);
 }
 assertEquals(expected, result);
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
 splitter.split("apple");
 } catch (IllegalArgumentException e) {
 assertEquals("The separator may not be the empty string.", e.getMessage());
 }
 }

 @Test(expected = IllegalStateException.class)
 public void testLimitReached() {
 String input = "apple-banana-orange-grape-apple-banana-orange-grape";
 Splitter splitter = Splitter.on('-').limit(5);
 List<String> expected = Arrays.asList("apple", "banana", "orange", "grape", "apple");
 List<String> result = new ArrayList<>();
 for (String s : splitter.split(input)) {
 result.add(s);
 }
 assertEquals(expected, result);
 }
}