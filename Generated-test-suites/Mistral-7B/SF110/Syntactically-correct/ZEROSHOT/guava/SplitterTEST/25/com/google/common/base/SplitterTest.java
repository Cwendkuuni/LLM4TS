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
 Splitter splitter = Splitter.on('-');
 List<String> result = new ArrayList<>();
 result.addAll(splitter.split("test-1,test-2,test-3"));
 assertEquals(Arrays.asList("test-1", "test-2", "test-3"), result);
 }

 @Test
 public void testOnStringSeparator() {
 Splitter splitter = Splitter.on(",");
 List<String> result = new ArrayList<>();
 result.addAll(splitter.split("test1,test2,test3"));
 assertEquals(Arrays.asList("test1", "test2", "test3"), result);
 }

 @Test
 public void testOnPatternSeparator() {
 Splitter splitter = Splitter.onPattern("\\s+");
 List<String> result = new ArrayList<>();
 result.addAll(splitter.split("test 1 test 2 test 3"));
 assertEquals(Arrays.asList("test 1", "test 2", "test 3"), result);
 }

 @Test
 public void testOnFixedLengthSeparator() {
 Splitter splitter = Splitter.fixedLength(2);
 List<String> result = new ArrayList<>();
 result.addAll(splitter.split("test12test13test14"));
 assertEquals(Arrays.asList("test12", "test13", "test14"), result);
 }

 @Test
 public void testOmmitEmptyStrings() {
 Splitter splitter = Splitter.on(",").omitEmptyStrings();
 List<String> result = new ArrayList<>();
 result.addAll(splitter.split("test1, ,test2,test3"));
 assertEquals(Arrays.asList("test1", "test2", "test3"), result);
 }

 @Test
 public void testLimit() {
 Splitter splitter = Splitter.on(",").limit(2);
 List<String> result = new ArrayList<>();
 result.addAll(splitter.split("test1,test2,test3,test4,test5"));
 assertEquals(Arrays.asList("test1", "test2"), result);
 }

 @Test
 public void testTrimResults() {
 Splitter splitter = Splitter.on(",").trimResults();
 List<String> result = new ArrayList<>();
 result.addAll(splitter.split(" test1, test2, test3 "));
 assertEquals(Arrays.asList("test1", "test2", "test3"), result);
 }

 @Test
 public void testTrimResultsWithCustomTrimmer() {
 Splitter splitter = Splitter.on(",").trimResults(Pattern.compile("\\s+"));
 List<String> result = new ArrayList<>();
 result.addAll(splitter.split(" test1, test2, test3 "));
 assertEquals(Arrays.asList("test1", "test2", "test3"), result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullSequence() {
 Splitter splitter = Splitter.on(",");
 splitter.split(null);
 }

 @Test
 public void testInvalidEntry() {
 Splitter splitter = Splitter.on(",").omitEmptyStrings();
 List<Map.Entry<String, String>> result = new ArrayList<>();
 result.addAll(splitter.withKeyValueSeparator(",").split("test=1,test=2,test=3,test=4=5"));
 assertEquals(Arrays.asList(
 new Map.Entry<>("test=1", ""),
 new Map.Entry<>("test=2", ""),
 new Map.Entry<>("test=3", ""),
 new Map.Entry<>("test=4=5", "")), result);
 }
}