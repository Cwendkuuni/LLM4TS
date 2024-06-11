package com.example.junit;

import static org.junit.Assert.*;

import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.LinkedHashMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SplitterTest {

 private Splitter createSplitter(char separator) {
 return Splitter.on(separator);
 }

 private Splitter createSplitter(CharMatcher separatorMatcher) {
 return Splitter.on(separatorMatcher);
 }

 private Splitter createSplitter(String separator) {
 return Splitter.on(separator);
 }

 private Splitter createSplitter(Pattern separatorPattern) {
 return Splitter.on(separatorPattern);
 }

 private Splitter createSplitter(int length) {
 return Splitter.fixedLength(length);
 }

 @Test
 public void testOnChar() {
 final Splitter splitter = createSplitter('-');
 final List<String> result = Lists.newArrayList(splitter.split("test-test-test"));
 assertEquals(Arrays.asList("test", "test", "test"), result);
 }

 @Test
 public void testOnCharMatcher() {
 final CharMatcher separatorMatcher = CharMatcher.is('-');
 final Splitter splitter = createSplitter(separatorMatcher);
 final List<String> result = Lists.newArrayList(splitter.split("test--test--test"));
 assertEquals(Arrays.asList("test", "", "test"), result);
 }

 @Test
 public void testOnString() {
 final Splitter splitter = createSplitter(":");
 final List<String> result = Lists.newArrayList(splitter.split("test:test:test"));
 assertEquals(Arrays.asList("test", "test", "test"), result);
 }

 @Test
 public void testOnPattern() {
 final Pattern separatorPattern = Pattern.compile(":");
 final Splitter splitter = createSplitter(separatorPattern);
 final List<String> result = Lists.newArrayList(splitter.split("test:test:test"));
 assertEquals(Arrays.asList("test", "test", "test"), result);
 }

 @Test
 public void testOnFixedLength() {
 final Splitter splitter = createSplitter(2);
 final List<String> result = Lists.newArrayList(splitter.split("1234567890"));
 assertEquals(Arrays.asList("12", "34", "56", "78", "90"), result);
 }

 @Test
 public void testOmitEmptyStrings() {
 final Splitter splitter = createSplitter('-');
 final List<String> result = Lists.newArrayList(splitter.split("test--test"));
 assertEquals(Arrays.asList("test", "test"), result);
 }

 @Test
 public void testLimit() {
 final Splitter splitter = createSplitter('-');
 final List<String> result = Lists.newArrayList(splitter.limit(3).split("test----------------test"));
 assertEquals(Arrays.asList("test", "test", "test----------------test"), result);
 }

 @Test
 public void testTrimResults() {
 final Splitter splitter = createSplitter('-');
 final List<String> result = Lists.newArrayList(splitter.trimResults().split(" test-test test "));
 assertEquals(Arrays.asList("test", "test"), result);
 }

 @Test
 public void testTrimResultsWithCustomTrimmer() {
 final Splitter splitter = createSplitter(CharMatcher.is(' '));
 final List<String> result = Lists.newArrayList(splitter.trimResults(CharMatcher.is(':')).split(" test:test : test "));
 assertEquals(Arrays.asList("test", "test"), result);
 }

 @Test
 public void testMapSplitter() {
 final Splitter outerSplitter = createSplitter(':');
 final Splitter entrySplitter = createSplitter('-');
 final Map<String, String> result = outerSplitter.withKeyValueSeparator(entrySplitter).split("key1:value1-key2:value2-key3:value3");
 assertEquals(Maps.newLinkedHashMap(Arrays.asList(Arrays.asList("key1", "value1"), Arrays.asList("key2", "value2"), Arrays.asList("key3", "value3"))), result);
 }

 @Test(expected = NullPointerException.class)
 public void testNullSequence() {
 final Splitter splitter = createSplitter(':');
 splitter.split(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptySeparator() {
 final Splitter splitter = createSplitter("");
 splitter.split("test");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPatternThatMatchesEmptyString() {
 final Pattern separatorPattern = Pattern.compile("");
 final Splitter splitter = createSplitter(separatorPattern);
splitter.split("test");
 }
}