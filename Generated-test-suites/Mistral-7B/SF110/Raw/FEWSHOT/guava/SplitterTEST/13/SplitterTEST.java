package com.google.common.base;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.CharMatcher;
import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SplitterTest {

 @Test
 public void testSplitterCreation() {
 Splitter splitter = Splitter.on(' ');
 Splitter splitterWithPattern = Splitter.onPattern("\\s+");
 Splitter fixedLengthSplitter = Splitter.fixedLength(3);
 Splitter patternWithCharMatcherSplitter = Splitter.on(Pattern.compile("(\\w+)=([^\\s]+)"));
 Splitter patternWithCharMatcherAndTrimmerSplitter = Splitter.on(Pattern.compile("(\\w+)=([^\\s]+)")).trimResults(CharMatcher.whitespace());
 }

 @Test
 public void testBasicSplit() {
 String testString = "one two three four five";
 Splitter splitter = Splitter.on(' ');
 List<String> expectedResult = Arrays.asList("one", "two", "three", "four", "five");
 List<String> result = new ArrayList<String>();
 for (String value : splitter.split(testString)) {
 result.add(value);
 }
 assertEquals(expectedResult, result);
 }

 @Test
 public void testBasicSplitWithTrimmer() {
 String testString = " one two three four five ";
 Splitter splitter = Splitter.on(' ').trimResults(CharMatcher.whitespace());
 List<String> expectedResult = Arrays.asList("one", "two", "three", "four", "five");
 List<String> result = new ArrayList<String>();
 for (String value : splitter.split(testString)) {
 result.add(value);
 }
 assertEquals(expectedResult, result);
 }

 @Test
 public void testSplitWithLimit() {
 String testString = "one two three four five six seven eight nine ten eleven twelve thirteen fourteen fifteen sixteen seventeen eighteen nineteen twenty";
 Splitter splitter = Splitter.on(' ').limit(7);
 List<String> expectedResult = Arrays.asList("one", "two", "three", "four", "five", "six", "seven");
 List<String> result = new ArrayList<String>();
 for (String value : splitter.split(testString)) {
 result.add(value);
 }
 assertEquals(expectedResult, result);
 }

 @Test
 public void testSplitWithOmitEmptyStrings() {
 String testString = "one two three four five six seven eight nine ten eleven twelve thirteen fourteen fifteen sixteen seventeen eighteen nineteen twenty";
 Splitter splitter = Splitter.on(' ').omitEmptyStrings();
 List<String> expectedResult = Arrays.asList("one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen", "twenty");
 List<String> result = new ArrayList<String>();
 for (String value : splitter.split(testString)) {
 result.add(value);
 }
 Assert.assertEquals(expectedResult.size(), result.size());
 for (int i = 0; i < expectedResult.size(); ++i) {
 assertEquals(expectedResult.get(i), result.get(i));
 }
 }

 @Test
 public void testSplitWithCustomStrategy() {
 String testString = "one=two=three=four=five";
 Splitter splitter = Splitter.onPattern("(\\w+)=([^\\s]+)");
 List<String[]> expectedResult = Arrays.asList(new String[]{"one", "two"}, new String[]{"three", "four"}, new String[]{"five", null});
 List<String[]> result = new ArrayList<String[]>();
 for (Object obj : splitter.splitToList(testString)) {
 result.add((String[])obj);
 }
 for (int i = 0; i < expectedResult.size(); ++i) {
 assertEquals(expectedResult.get(i)[0], result.get(i)[0]);
 assertEquals(expectedResult.get(i)[1], result.get(i)[1]);
 }
 }

 @Test
 public void testSplitWithCustomStrategyAndTrimmer() { // Add test case for Splitter.onPattern("(\\w+)=([^\\s]+)").trimResults(CharMatcher.whitespace())
 String testString = " one=two=three=four=five ";
 Splitter splitter = Splitter.onPattern("(\\w+)=([^\\s]+)").trimResults(CharMatcher.whitespace());
 List<String[]> expectedResult = Arrays.asList(new String[]{"one", "two"}, new String[]{"three", "four"}, new String[]{"five", null});
 List<String[]> result = new ArrayList<String[]>();
 for (Object obj : splitter.splitToList(testString)) {
 result.add((String[])obj);
 }
 for (int i = 0; i < expectedResult.size(); ++i) {
 assertEquals(expectedResult.get(i)[0], result.get(i)[0]);
 assertEquals(expectedResult.get(i)[1], result.get(i)[1]);
 }
 }

 @Test(expected = NullPointerException.class)
 public void testSplitterWithNullSequence() {
 Splitter splitter = Splitter.on(' ');
 splitter.split(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSplitterWithEmptySeparator() {
 Splitter splitter = Splitter.on('');
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSplitterWithNullSeparatorPattern() {
 Splitter splitter = Splitter.onPattern(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSplitterWithEmptySeparatorPattern() {
 Splitter splitter = Splitter.onPattern("");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSplitterWithInvalidSeparatorPattern() {
 Splitter splitter = Splitter.onPattern("\\\\s+");
 }

 @Test
 public void testSplitterWithMapSplitter() {
 Splitter keyValueSplitter = Splitter.on('=').limit(1);
 Splitter outerSplitter = Splitter.on(' ');
 Map<String, String> expectedMap = new LinkedHashMap<String, String>() {{
 put("one", "two");
 put("three", "four");
 put("five", "six");
 put("seven", "eight");
 put("nine", "ten");
 put("eleven", "twelve");
 put("thirteen", "fourteen");
 put("fifteen", "sixteen");
 put("seventeen", "eighteen");
 put("nineteen", "twenty");
 }};
 Map<String, String> resultMap = new LinkedHashMap<String, String>();
 for (Object obj : outerSplitter.withKeyValueSeparator(keyValueSplitter).split(testString)) {
 String[] entry = (String[])obj;
 resultMap.put(entry[0], entry[1]);
 }
 assertEquals(expectedMap, resultMap);
 }
}