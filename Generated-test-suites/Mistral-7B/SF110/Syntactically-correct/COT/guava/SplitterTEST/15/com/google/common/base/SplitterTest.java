package com.example.junit;

import static org.junit.Assert.*;

import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.LinkedHashMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SplitterTest {

 private CharSequence sequence;
 private Splitter splitter;

 @Before
 public void setup() {
 sequence = "the quick brown fox jumps over the lazy dog";
 splitter = Splitter.on(' ');
 }

 @Test
 public void testOn() {
 Iterable<String> result = splitter.split(sequence);
 List<String> list = new ArrayList<>(result);
 assertEquals("the", list.get(0));
 assertEquals("quick", list.get(1));
 // ... other assertions for the rest of the elements
 }

 @Test
 public void testOnCharMatcher() {
 CharMatcher charMatcher = CharMatcher.is('-');
 splitter = Splitter.on(charMatcher);
 result = splitter.split(sequence);
 list = new ArrayList<>(result);
 assertEquals("the", list.get(0));
 assertEquals("quick", list.get(1));
 assertEquals("brown", list.get(2));
 // ... other assertions for the rest of the elements where '-' is present
 }

 @Test
 public void testOnPattern() {
 String pattern = "[ ]+";
 splitter = Splitter.onPattern(pattern);
 result = splitter.split(sequence);
 list = new ArrayList<>(result);
 assertEquals("the", list.get(0));
 assertEquals("quick", list.get(1));
 assertEquals("brown", list.get(2));
 assertEquals("jumps", list.get(3));
 // ... other assertions for the rest of the elements where ' ' or '+' is present
 }

 @Test
 public void testLimit() {
 splitter = splitter.limit(3);
 result = splitter.split(sequence);
 list = new ArrayList<>(result);
 assertEquals("the", list.get(0));
 assertEquals("quick", list.get(1));
 assertEquals("brown", list.get(2));
 // ... other assertions for the first three elements
 assertFalse(result.iterator().hasNext());
 }

 @Test
 public void testTrimResults() {
 splitter = splitter.trimResults();
 result = splitter.split(sequence);
 list = new ArrayList<>(result);
 assertEquals("the", list.get(0));
 assertEquals("quick", list.get(1));
 assertEquals("brown", list.get(2));
 assertEquals("jumps", list.get(3));
 // ... other assertions for the rest of the elements with leading and trailing whitespace removed
 }

 @Test
 public void testTrimResultsWithCustomTrimmer() {
 CharMatcher trimmer = CharMatcher.is('a');
 splitter = splitter.trimResults(trimmer);
 result = splitter.split(sequence);
 list = new ArrayList<>(result);
 // ... assertions for the rest of the elements with 'a' characters removed from the beginning and end
 }

 @Test
 public void testOmitEmptyStrings() {
 splitter = splitter.omitEmptyStrings();
 result = splitter.split(sequence);
 assertEquals(10, result.size());
 // ... assertions for all elements except the empty strings
 }

 @Test(expected = NullPointerException.class)
 public void testOnNullSeparator() {
 assertThrows(NullPointerException.class, () -> Splitter.on(null));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOnEmptySeparator() {
 assertThrows(IllegalArgumentException.class, () -> Splitter.on(""));
 }

 @Test(expected = NullPointerException.class)
 public void testOnNullToSplit() {
 assertThrows(NullPointerException.class, () -> splitter.split(null));
 }

 @Test
 public void testIterableToList() {
 List<String> list = splitter.splitToList(sequence);
 assertEquals("the", list.get(0));
 // ... other assertions for the rest of the elements
 }

 @Test
 public void testMapSplitter() {
 Map<String, String> map = splitter.withKeyValueSeparator(":").split(sequence);
 assertEquals("the:quick", map.get("the"));
 assertEquals("brown:jumps", map.get("brown"));
 // ... other assertions for the rest of the elements
 }

 @Test
 public void testMapSplitterNullKeyValueSeparator() {
 assertThrows(NullPointerException.class, () -> splitter.withKeyValueSeparator(null));
 }

}