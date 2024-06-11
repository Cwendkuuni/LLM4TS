package com.google.common.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Test;

public class SplitterTest {

 @Test
 public void testSplitterCreation() {
 Splitter splitter = Splitter.on('-');
 assertEquals(CharMatcher.is('-'), splitter.trimmer());
 assertEquals(Strategy.class, splitter.strategy().getClass());
 assertEquals(false, splitter.omitEmptyStrings());
 assertEquals(Integer.MAX_VALUE, splitter.limit());
 }

 @Test
 public void testSplitterCreationWithStrategy() {
 Splitter splitter = new Splitter(new Strategy() {
 @Override
 public Iterator<String> iterator(Splitter splitter, CharSequence toSplit) {
 throw new UnsupportedOperationException();
 }
 }, false, CharMatcher.NONE, Integer.MAX_VALUE);
 assertEquals(Strategy.class, splitter.strategy().getClass());
 }

 @Test
 public void testIterableSplit() {
 CharSequence sequence = "one-two-three-four-five";
 List<String> result = Splitter.on('-').split(sequence);
 List<String> expected = Arrays.asList("one", "two", "three", "four", "five");
 assertEquals(expected, result);
 }

 @Test
 public void testIterableSplitWithLimit() {
 CharSequence sequence = "one-two-three-four-five-six-seven-eight-nine";
 List<String> result = Splitter.on('-').limit(3).split(sequence);
 List<String> expected = Arrays.asList("one-two-three", "four-five-six");
 assertEquals(expected, result);
 }

 @Test
 public void testIterableSplitWithEmptySequence() {
 List<String> result = Splitter.on('-').split(CharSequences.emptyString());
 assertTrue(result.isEmpty());
 }

 @Test
 public void testIterableSplitWithNullSequence() {
 List<String> result = Splitter.on('-').split((CharSequence) null);
 assertNull(result);
 }

 @Test
 public void testIterableSplitWithOmittedEmptyStrings() {
 CharSequence sequence = "one- -three-four-five";
 List<String> result = Splitter.on('-').omitEmptyStrings().split(sequence);
 List<String> expected = Arrays.asList("one", "three", "four", "five");
 assertEquals(expected, result);
 }

 @Test
 public void testIterableSplitWithTrimmedEmptyStrings() {
 CharSequence sequence = "one\t-two-\tthree";
 List<String> result = Splitter.on('\t').trimResults().split(sequence);
 List<String> expected = Arrays.asList("one", "two", "three");
 assertEquals(expected, result);
 }

 @Test
 public void testIterableSplitWithKeyValueSeparator() {
 CharSequence sequence = "key1=value1-key2=value2";
 Map<String, String> result = Splitter.on('=').withKeyValueSeparator(Splitter.on('-')).split(sequence);
 Map<String, String> expected = new LinkedHashMap<String, String>() {{
 put("key1", "value1");
 put("key2", "value2");
 }};
 assertEquals(expected, result);
 }

 @Test
 public void testIterableSplitWithPattern() {
 CharSequence sequence = "one.two.three.four";
 List<String> result = Splitter.onPattern("\\.").split(sequence);
 List<String> expected = Arrays.asList("one", "two", "three", "four");
 assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPatternCannotMatchEmptyString() {
 CharSequence sequence = CharSequences.emptyString();
 Splitter.onPattern("").split(sequence);
 }

 @Test
 public void testSplittingIteratorNext() {
 Splitter splitter = Splitter.on('-');
 CharSequence sequence = "one-two-three-four";
 SplittingIterator iterator = splitter.splittingIterator(sequence);
 String next = iterator.next();
 assertEquals("one", next);
 next = iterator.next();
 assertEquals("two", next);
 next = iterator.next();
 assertEquals("three", next);
 next = iterator.next();
 assertEquals("four", next);
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testSplittingIteratorLimit() {
 Splitter splitter = Splitter.on('-').limit(2);
 CharSequence sequence = "one-two-three-four";
 SplittingIterator iterator = splitter.splittingIterator(sequence);
 String next1 = iterator.next();
 assertEquals("one-two", next1);
 String next2 = iterator.next();
 assertEquals("three", next2);
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testSplittingIteratorTrimmedEmptyStrings() {
 Splitter splitter = Splitter.on('-').trimResults();
 CharSequence sequence = "one\t-two-\tthree";
 SplittingIterator iterator = splitter.splittingIterator(sequence);
 String next = iterator.next();
 assertEquals("one", next);
 next = iterator.next();
 assertEquals("two", next);
 next = iterator.next();
 assertEquals("three", next);
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testSplittingIteratorEmptySequence() {
 Splitter splitter = Splitter.on('-');
 SplittingIterator iterator = splitter.splittingIterator(CharSequences.emptyString());
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testSplittingIteratorNullSequence() {
 Splitter splitter = Splitter.on('-');
 SplittingIterator iterator = splitter.splittingIterator((CharSequence) null);
 assertNull(iterator);
 }

 @Test
 public void testMapSplitter() {
 CharSequence sequence = "key1=value1-key2=value2";
 MapSplitter mapSplitter = Splitter.on('=').withKeyValueSeparator(Splitter.on('-'));
 Map<String, String> result = mapSplitter.split(sequence);
 Map<String, String> expected = new LinkedHashMap<String, String>() {{
 put("key1", "value1");
 put("key2", "value2");
 }};
 assertEquals(expected, result);
 }

 @Test
 public void testMapSplitterEmptySequence() {
 MapSplitter mapSplitter = Splitter.on('=').withKeyValueSeparator(Splitter.on('-'));
 Map<String, String> result = mapSplitter.split(CharSequences.emptyString());
 assertTrue(result.isEmpty());
 }
}