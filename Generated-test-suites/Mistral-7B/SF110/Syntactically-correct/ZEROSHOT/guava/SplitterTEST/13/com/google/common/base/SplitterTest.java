package com.example.junit;

import org.junit.Test;
import static org.junit.Assert.*;
import com.google.common.base.Splitter;
import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class SplitterTest {

 @Test
 public void testOnCharSeparator() {
 Splitter splitter = Splitter.on(':');
 Iterable<String> result = splitter.split("key1:value1,key2:value2");
 List<String> list = Lists.newArrayList(result);
 assertEquals(2, list.size());
 assertEquals("key1", list.get(0));
 assertEquals("value1", list.get(1));
 }

 @Test
 public void testOnCharMatcherSeparator() {
 CharMatcher separatorMatcher = CharMatcher.is(':');
 Splitter splitter = Splitter.on(separatorMatcher);
 Iterable<String> result = splitter.split("key1:value1,key2:value2");
 List<String> list = Lists.newArrayList(result);
 assertEquals(2, list.size());
 assertEquals("key1", list.get(0));
 assertEquals("value1", list.get(1));
 }

 @Test
 public void testOnStringSeparator() {
 Splitter splitter = Splitter.on(":");
 Iterable<String> result = splitter.split("key1:value1,key2:value2");
 List<String> list = Lists.newArrayList(result);
 assertEquals(2, list.size());
 assertEquals("key1", list.get(0));
 assertEquals("value1", list.get(1));
 }

 @Test
 public void testOnPatternSeparator() {
 String separatorPattern = ":";
 Splitter splitter = Splitter.onPattern(separatorPattern);
 Iterable<String> result = splitter.split("key1:value1,key2:value2");
 List<String> list = Lists.newArrayList(result);
 assertEquals(2, list.size());
 assertEquals("key1", list.get(0));
 assertEquals("value1", list.get(1));
 }

 @Test
 public void testFixedLengthSplitter() {
 Splitter splitter = Splitter.fixedLength(3);
 Iterable<String> result = splitter.split("1234567890");
 List<String> list = Lists.newArrayList(result);
 assertEquals(4, list.size());
 assertEquals("123", list.get(0));
 assertEquals("456", list.get(1));
 assertEquals("567", list.get(2));
 assertEquals("890", list.get(3));
 }

 @Test
 public void testOmitEmptyStrings() {
 Splitter splitter = Splitter.on(":").omitEmptyStrings();
 Iterable<String> result = splitter.split("key1:value1:,key2:,:value3:");
 List<String> list = Lists.newArrayList(result);
 assertEquals(2, list.size());
 assertEquals("key1:value1", list.get(0));
 assertEquals("key2:value3", list.get(1));
 }

 @Test
 public void testLimit() {
 Splitter splitter = Splitter.on(":").limit(2);
 Iterable<String> result = splitter.split("key1:value1:key2:value2:key3:value3:key4:value4");
 List<String> list = Lists.newArrayList(result);
 assertEquals(3, list.size());
 assertEquals("key1:value1", list.get(0));
 assertEquals("key2:value2", list.get(1));
 assertEquals("key3:value3", list.get(2));
 }

 @Test
 public void testTrimResults() {
 Splitter splitter = Splitter.on(":").trimResults();
 Iterable<String> result = splitter.split(" key1:value1 key2:value2 ");
 List<String> list = Lists.newArrayList(result);
 assertEquals(2, list.size());
 assertEquals("key1:value1", list.get(0));
 assertEquals("key2:value2", list.get(1));
 }

 @Test
 public void testTrimResultsWithCustomTrimmer() {
 Splitter splitter = Splitter.on(":").trimResults(CharMatcher.is(' '));
 Iterable<String> result = splitter.split(" key1:value1 key2:value2 ");
 List<String> list = Lists.newArrayList(result);
 assertEquals(2, list.size());
 assertEquals("key1:value1", list.get(0));
 assertEquals("key2:value2", list.get(1));
 }

 @Test
 public void testMapSplitter() {
 Splitter keySplitter = Splitter.on(":").omitEmptyStrings();
 Splitter valueSplitter = Splitter.on("=").omitEmptyStrings();
 MapSplitter mapSplitter = new MapSplitter(keySplitter, valueSplitter);
 Iterable<Map.Entry<String, String>> result = mapSplitter.split("key1:value1=value1.1,key2:value2=value2.2,key3:value3=value3.3");
 List<Map.Entry<String, String>> list = Lists.newArrayList(result);
 assertEquals(3, list.size());
 assertEquals("key1", list.get(0).getKey());
 assertEquals("value1.1", list.get(0).getValue());
 assertEquals("key2", list.get(1).getKey());
 assertEquals("value2.2", list.get(1).getValue());
 assertEquals("key3", list.get(2).getKey());
 assertEquals("value3.3", list.get(2).getValue());
 }
}