package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SplitterTest {

 @Test
 public void testOnCharSeparator() {
 String input = "helloWorld-isThisAtest?123";
 Splitter splitter = Splitter.on('-');
 List<String> expected = Arrays.asList("hello", "World", "isThisAtest", "123");
 List<String> actual = new ArrayList<>(splitter.split(input));
 assertEquals(expected.size(), actual.size());
 for (int i = 0; i < expected.size(); i++) {
 String expectedStr = expected.get(i);
 String actualStr = actual.get(i);
 assertEquals(expectedStr, actualStr);
 }
 }

 @Test
 public void testOnStringSeparator() {
 String input = "helloWorld-isThisAtest?123";
 Splitter splitter = Splitter.on("--");
 List<String> expected = Arrays.asList("helloWorld", "isThisAtest?123");
 List<String> actual = new ArrayList<>(splitter.split(input));
 assertEquals(expected.size(), actual.size());
 for (int i = 0; i < expected.size(); i++) {
 String expectedStr = expected.get(i);
 String actualStr = actual.get(i);
 assertEquals(expectedStr, actualStr);
 }
 }

 @Test
 public void testOnPatternSeparator() {
 String input = "helloWorld-isThisAtest?123";
 Pattern separatorPattern = Pattern.compile("-|\\s+");
 Splitter splitter = Splitter.onPattern(separatorPattern);
 List<String> expected = Arrays.asList("hello", "World", "isThisAtest", "123");
 List<String> actual = new ArrayList<>(splitter.split(input));
 assertEquals(expected.size(), actual.size());
 for (int i = 0; i < expected.size(); i++) {
 String expectedStr = expected.get(i);
 String actualStr = actual.get(i);
 assertEquals(expectedStr, actualStr);
 }
 }

 @Test
 public void testOnFixedLengthSeparator() {
 String input = "1234567890";
 Splitter splitter = Splitter.fixedLength(3);
 List<String> expected = Arrays.asList("123", "456", "789", "0");
 List<String> actual = new ArrayList<>(splitter.split(input));
 assertEquals(expected.size(), actual.size());
 for (int i = 0; i < expected.size(); i++) {
 String expectedStr = expected.get(i);
 String actualStr = actual.get(i);
 assertEquals(expectedStr, actualStr);
 }
 }

 @Test
 public void testOmitEmptyStrings() {
 String input = "hello World ,   ";
 Splitter splitter = Splitter.on(' ').omitEmptyStrings();
 List<String> expected = Arrays.asList("hello", "World");
 List<String> actual = new ArrayList<>(splitter.split(input));
 assertEquals(expected.size(), actual.size());
 for (int i = 0; i < expected.size(); i++) {
 String expectedStr = expected.get(i);
 String actualStr = actual.get(i);
 assertEquals(expectedStr, actualStr);
 }
 }

 @Test
 public void testLimit() {
 String input = "123456789012345678901234567890";
 Splitter splitter = Splitter.on(' ').limit(3);
 List<String> expected = Arrays.asList("123", "456", "567");
 List<String> actual = new ArrayList<>(splitter.split(input));
 assertEquals(expected.size(), actual.size());
 for (int i = 0; i < expected.size(); i++) {
 String expectedStr = expected.get(i);
 String actualStr = actual.get(i);
 assertEquals(expectedStr, actualStr);
 }
 }

 @Test
 public void testTrimResults() {
 String input = "  hello World  ";
 Splitter splitter = Splitter.on(' ').trimResults();
 List<String> expected = Arrays.asList("hello", "World");
 List<String> actual = new ArrayList<>(splitter.split(input));
 for (String str : actual) {
 assertTrue(str.matches("^[\\w ]+$"));
 }
 assertEquals(expected.size(), actual.size());
 for (int i = 0; i < expected.size(); i++) {
 String expectedStr = expected.get(i);
 String actualStr = actual.get(i);
 assertEquals(expectedStr, actualStr);
 }
 }

 @Test
 public void testTrimResultsWithCustomTrimmer() {
 String input = "  hello World  ";
 Splitter splitter = Splitter.on(' ').trimResults(CharMatcher.anyOf('!', '?'));
 List<String> expected = Arrays.asList("hello", "World");
 List<String> actual = new ArrayList<>(splitter.split(input));
 for (String str : actual) {
 assertTrue(str.matches("^[\\w ]+$"));
 assertTrue(str.matches("(?s).*[^!?]*$"));
 }
 assertEquals(expected.size(), actual.size());
 for (int i = 0; i < expected.size(); i++) {
 String expectedStr = expected.get(i);
 String actualStr = actual.get(i);
 assertEquals(expectedStr, actualStr);
 }
 }

 @Test
 public void testMapSplitter() {
 String input = "key1=value1,key2=value2,key3=value3";
 Splitter keySplitter = Splitter.on("=");
 Splitter valueSplitter = Splitter.on(",");
 MapSplitter mapSplitter = new MapSplitter(keySplitter, valueSplitter);
 Map<String, String> expected = new HashMap<>();
 expected.put("key1", "value1");
 expected.put("key2", "value2");
 expected.put("key3", "value3");
 Map<String, String> actual = mapSplitter.split(input);
 assertEquals(expected.size(), actual.size());
 for (Map.Entry<String, String> entry : expected.entrySet()) {
 String key = entry.getKey();
 String value = entry.getValue();
 assertTrue(actual.containsKey(key));
 assertEquals(value, actual.get(key));
 }
 }

}