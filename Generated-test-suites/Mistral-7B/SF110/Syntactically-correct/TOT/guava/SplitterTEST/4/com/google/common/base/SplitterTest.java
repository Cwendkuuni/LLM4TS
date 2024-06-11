package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SplitterTest {

 @Test
 public void testOnCharSeparator() {
 Splitter splitter = Splitter.on('-');
 List<String> result = new ArrayList<String>();
 result = splitter.split("a-b-c-d-e");
 assertEquals(Arrays.asList("a", "b", "c", "d", "e"), result);
 }

 @Test
 public void testOnStringSeparator() {
 Splitter splitter = Splitter.on(":");
 List<String> result = new ArrayList<String>();
 result = splitter.split("a:b:c:d:e");
 assertEquals(Arrays.asList("a", "b", "c", "d", "e"), result);
 }

 @Test
 public void testOnPatternSeparator() {
 Pattern separatorPattern = Pattern.compile(":");
 Splitter splitter = Splitter.on(separatorPattern);
 List<String> result = new ArrayList<String>();
 result = splitter.split("a:b:c:d:e");
 assertEquals(Arrays.asList("a", "b", "c", "d", "e"), result);
 }

 @Test(expected = NullPointerException.class)
 public void testOnNullSeparator() {
 Splitter splitter = null;
 List<String> result = new ArrayList<String>();
 try {
 result = splitter.split("a:b:c:d:e");
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testOnEmptySequence() {
 Splitter splitter = Splitter.on('-');
 List<String> result = new ArrayList<String>();
 result = splitter.split("");
 assertEmpty(result);
 }

 @Test
 public void testOnSingleCharacterSequence() {
 Splitter splitter = Splitter.on('-');
 List<String> result = new ArrayList<String>();
 result = splitter.split("a");
 assertContains("a", result);
 }

 @Test
 public void testOnMultipleCharacterSequenceWithOneSeparator() {
 Splitter splitter = Splitter.on('-');
 List<String> result = new ArrayList<String>();
 result = splitter.split("a--b");
 assertEquals(Arrays.asList("a", "", "b"), result);
 }

 @Test
 public void testOnMultipleCharacterSequenceWithMultipleSeparators() {
 Splitter splitter = Splitter.on('-');
 List<String> result = new ArrayList<String>();
 result = splitter.split("a--b--c--d");
 assertEquals(Arrays.asList("a", "", "b", "", "c", "", "d"), result);
 }

 @Test
 public void testOnPatternWithEmptyStringAsSeparator() {
 try {
 Splitter splitter = Splitter.onPattern("");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOnPatternWithEmptySequence() {
 Splitter splitter = Splitter.onPattern(":");
 List<String> result = new ArrayList<String>();
 try {
 result = splitter.split("");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testOnPatternWithSingleCharacterSequence() {
 Splitter splitter = Splitter.onPattern(":");
 List<String> result = new ArrayList<String>();
 result = splitter.split("a:b:c:d:e");
 assertEquals(Arrays.asList("a", "b", "c", "d", "e"), result);
 }

 @Test
 public void testOnPatternWithMultipleCharacterSequence() {
 Splitter splitter = Splitter.onPattern(":");
 List<String> result = new ArrayList<String>();
 result = splitter.split("a::b::c::d::e");
 assertEquals(Arrays.asList("a", "", "b", "", "c", "", "d", "", "e"), result);
 }

 @Test
 public void testLimit() {
 Splitter splitter = Splitter.on('-').limit(3);
 List<String> result = new ArrayList<String>();
 result = splitter.split("a----b----c----d----e----f----g----h----i----j----k----l----m----n----o----p----q----r----s--");
 assertEquals(Arrays.asList("a", "b", "c"), result);
 }

 @Test
 public void testOmittingEmptyStrings() {
 Splitter splitter = Splitter.on('-').omitEmptyStrings();
 List<String> result = new ArrayList<String>();
 result = splitter.split("a--b--c-- --d--e--f--");
 assertEquals(Arrays.asList("a", "b", "c", "d", "e", "f"), result);
 }

 @Test
 public void testTrimming() {
 Splitter splitter = Splitter.on('-').trimResults();
 List<String> result = new ArrayList<String>();
 result = splitter.split(" a--b--  c--d--e");
 assertEquals(Arrays.asList("a", "b", "c", "d", "e"), result);
 }

 @Test
 public void testTrimmingWithCustomTrimmer() {
 Splitter splitter = Splitter.on('-').trimResults(Pattern.compile("\\s+"));
 List<String> result = new ArrayList<String>();
 result = splitter.split(" a--b--  c--d--e");
 assertEquals(Arrays.asList("a", "b", "c", "d", "e"), result);
 }

 @Test
 public void testSplittingMapEntry() {
 Splitter outerSplitter = Splitter.on(':');
 Splitter innerSplitter = Splitter.on('=');
 Map<String, String> map = outerSplitter.withKeyValueSeparator(innerSplitter).split("key1=value1:key2=value2:key3=value3");
 assertEquals(Arrays.asList(Arrays.asList("key1", "value1"), Arrays.asList("key2", "value2"), Arrays.asList("key3", "value3")), map.entrySet());
 }

 @Test
 public void testSplittingEmptyMapEntry() {
 Splitter outerSplitter = Splitter.on(':');
 Splitter innerSplitter = Splitter.on('=');
 Map<String, String> map = outerSplitter.withKeyValueSeparator(innerSplitter).split("key1=:value1:");
 assertEquals(Arrays.asList(Arrays.asList("key1", ""), Arrays.asList("", "value1")), map.entrySet());
 }

 @Test
 public void testSplittingMapEntryWithEmptyMap() {
 Splitter outerSplitter = Splitter.on(':');
 Splitter innerSplitter = Splitter.on('=');
 Map<String, String> map = outerSplitter.withKeyValueSeparator(innerSplitter).split("");
 assertEquals(Collections.emptyMap(), map);
 }
}