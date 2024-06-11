package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Pattern;

public class SplitterTest {

 @Test
 public void testOnCharSeparator() {
 CharSequence sequence = "hello world, this is a test, of the splitter";
 Splitter splitter = Splitter.on(' ').split(sequence);
 List<String> expected = Arrays.asList("hello", "world", "this", "is", "a", "test", "of", "the", "splitter");
 List<String> result = new ArrayList<>();
 for (String s : splitter) {
 result.add(s);
 }
 assertEquals(expected, result);
 }

 @Test
 public void testOnStringSeparator() {
 CharSequence sequence = "hello world, this is a test, of the splitter";
 Splitter splitter = Splitter.on(",").split(sequence);
 List<String> expected = Arrays.asList("hello world", "this is a test", "of the splitter");
 List<String> result = new ArrayList<>();
 for (String s : splitter) {
 result.add(s);
 }
 assertEquals(expected, result);
 }

 @Test
 public void testOnPatternSeparator() {
 CharSequence sequence = "hello world, this is a test, of the splitter";
 Pattern pattern = Pattern.compile("\\s+");
 Splitter splitter = Splitter.onPattern(pattern).split(sequence);
 List<String> expected = Arrays.asList("hello", "world", "this", "is", "a", "test", "of", "the", "splitter");
 List<String> result = new ArrayList<>();
 for (String s : splitter) {
 result.add(s);
 }
 assertEquals(expected, result);
 }

 @Test
 public void testOnCustomPatternSeparator() {
 CharSequence sequence = "h1: hello world, h2: this is a test, h3: of the splitter";
 Splitter splitter = Splitter.onPattern(":\\s*").split(sequence);
 Map<String, String> expected = new LinkedHashMap<>();
 expected.put("h1", "hello world");
 expected.put("h2", "this is a test");
 expected.put("h3", "of the splitter");
 Map<String, String> result = splitter.split(sequence).splitToMap();
 assertEquals(expected, result);
 }

 @Test(expected = NullPointerException.class)
 public void testNullInput() {
 Splitter.on(Pattern.compile("")).split(null);
 }

 @Test(expected = IllegalStateException.class)
 public void testInvalidStrategy() {
 Splitter splitter = new Splitter(new Strategy() {
 @Override
 public Iterator<String> iterator(final Splitter p0, final CharSequence p1) {
 throw new IllegalStateException("Invalid strategy");
 }
 });
 Splitter.SplitterIterator iter = splitter.splittingIterator("test");
 while (iter.hasNext()) {
 iter.next();
 }
 fail("Test should have thrown an exception");
 }

 @Test
 public void testLimit() {
 CharSequence sequence = "123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456";
 Splitter splitter = Splitter.on(' ').limit(5).split(sequence);
 List<String> expected = Arrays.asList("12345", "56789", "01234", "56789", "01234");
 List<String> result = new ArrayList<>();
 for (String s : splitter) {
 result.add(s);
 }
 assertEquals(expected, result);
 }

 @Test
 public void testOmitEmptyStrings() {
 CharSequence sequence = "1, ,2,3, ,,4,5";
 Splitter splitter = Splitter.on(',').omitEmptyStrings().split(sequence);
 List<String> expected = Arrays.asList("1", "2", "3", "4", "5");
 List<String> result = new ArrayList<>();
 for (String s : splitter) {
 result.add(s);
 }
 assertEquals(expected, result);
 }
}