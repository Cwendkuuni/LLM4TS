package com.example.splittertest;

import static org.junit.Assert.*;

import java.util.*;
import com.google.common.base.*;

public class SplitterTest {

 @ org.junit.Test
 public void testOnCharSeparator() {
 Splitter splitter = Splitter.on('-');
 List<String> result = splitter.split("test-1, test-2, test-3");
 assertEquals(3, result.size());
 assertEquals("test-1", result.get(0));
 assertEquals("test-2", result.get(1));
 assertEquals("test-3", result.get(2));
 }

 @ org.junit.Test
 public void testOnStringSeparator() {
 Splitter splitter = Splitter.on(":");
 List<String> result = splitter.split("key1:value1, key2:value2, key3:value3");
 assertEquals(3, result.size());
 assertEquals("key1:value1", result.get(0));
 assertEquals("key2:value2", result.get(1));
 assertEquals("key3:value3", result.get(2));
 }

 @ org.junit.Test
 public void testOnPatternSeparator() {
 Pattern pattern = Pattern.compile("\\s+");
 Splitter splitter = Splitter.onPattern(pattern);
 List<String> result = splitter.split("test 1 test 2 test 3");
 assertEquals(3, result.size());
 assertEquals("test 1", result.get(0));
 assertEquals("test 2", result.get(1));
 assertEquals("test 3", result.get(2));
 }

 @ org.junit.Test
 public void testFixedLengthSplitter() {
 Splitter splitter = Splitter.fixedLength(4);
 List<String> result = splitter.split("test1234test5678test91011");
 assertEquals(3, result.size());
 assertEquals("test1234", result.get(0));
 assertEquals("test5678", result.get(1));
 assertEquals("test91011", result.get(2));
 }

 @ org.junit.Test
 public void testOmitEmptyStrings() {
 Splitter splitter = Splitter.on('-').omitEmptyStrings();
 List<String> result = splitter.split("test- , test- , test-");
 assertEquals(2, result.size());
 assertEquals("test-", result.get(0));
 assertEquals("test", result.get(1));
 }

 @ org.junit.Test
 public void testLimit() {
 Splitter splitter = Splitter.on(':').limit(2);
 List<String> result = new ArrayList<String>();
 result.addAll(splitter.split("key1:value1:key2:value2:key3:value3:key4:value4"));
 assertEquals(2, result.size());
 assertEquals("key1:value1:key2:value2", result.get(0));
 assertEquals("key3:value3", result.get(1));
 }

 @ org.junit.Test
 public void testMapSplitter() {
 Splitter keySplitter = Splitter.on(":");
 Splitter valueSplitter = Splitter.on("_");
 Map<String, String> result = keyValueSplitter.splitMap("key1:value1_1,key2:value2_2,key3:value3_3");
 assertEquals(3, result.size());
 assertEquals("key1", result.keySet().iterator().next());
 assertEquals("value1_1", result.get("key1"));
 assertEquals("key2", result.keySet().iterator().next());
 assertEquals("value2_2", result.get("key2"));
 assertEquals("key3", result.keySet().iterator().next());
 assertEquals("value3_3", result.get("key3"));
 }
}