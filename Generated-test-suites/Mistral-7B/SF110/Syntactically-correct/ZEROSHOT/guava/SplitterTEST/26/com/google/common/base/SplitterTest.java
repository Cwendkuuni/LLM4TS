package com.example.junit4;

import static org.junit.Assert.*;

import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Strategies;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SplitterTest {

 @org.junit.Test
 public void testOnChar() {
 Splitter splitter = Splitter.on('-');
 List<String> result = new ArrayList<>();
 result.addAll(splitter.split("test-string-1"));
 result.addAll(splitter.split("test-string-2"));
 assertEquals(Arrays.asList("test", "string-1", "test", "string-2"), result);
 }

 @org.junit.Test
 public void testOnCharMatcher() {
 CharMatcher trimmer = CharMatcher.is(' ');
 Splitter splitter = Splitter.on(trimmer).trimResults();
 List<String> result = new ArrayList<>();
 result.addAll(splitter.split(" test string 1 "));
 result.addAll(splitter.split(" test string 2 "));
 assertEquals(Arrays.asList("test", "string", "1", "test", "string", "2"), result);
 }

 @org.junit.Test
 public void testOnString() {
 Splitter splitter = Splitter.on("-");
 List<String> result = new ArrayList<>();
 result.addAll(splitter.split("test-string-1"));
 result.addAll(splitter.split("test-string-2"));
 assertEquals(Arrays.asList("test", "string-1", "test", "string-2"), result);
 }

 @org.junit.Test
 public void testOnPattern() {
 Pattern pattern = Pattern.compile("[-]");
 Splitter splitter = Splitter.onPattern(pattern);
 List<String> result = new ArrayList<>();
 result.addAll(splitter.split("test-string-1"));
 result.addAll(splitter.split("test-string-2"));
 assertEquals(Arrays.asList("test", "string-1", "test", "string-2"), result);
 }

 @org.junit.Test
 public void testOnFixedLength() {
 Splitter splitter = Splitter.fixedLength(2);
 List<String> result = new ArrayList<>();
 result.addAll(splitter.split("teststring123"));
 result.addAll(splitter.split("teststring456"));
 assertEquals(Arrays.asList("test", "string", "12", "test", "string", "45"), result);
 }

 @org.junit.Test
 public void testOmitEmptyStrings() {
 Splitter splitter = Splitter.on('-').omitEmptyStrings();
 List<String> result = new ArrayList<>();
 result.addAll(splitter.split("test-string1-"));
 result.addAll(splitter.split("test-string2"));
 result.addAll(splitter.split("test-string3-"));
 assertEquals(Arrays.asList("test", "string1", "test", "string3"), result);
 }

 @org.junit.Test
 public void testLimit() {
 Splitter splitter = Splitter.on('-').limit(2);
 List<String> result = new ArrayList<>();
 result.addAll(splitter.split("test-string1-test-string2-test-string3-"));
 assertEquals(Arrays.asList("test", "string1", "test", "string2"), result);
 }

 @org.junit.Test
 public void testJoin() {
 Splitter splitter = Splitter.on("-");
 List<String> list1 = Arrays.asList("test", "string1", "test", "string2");
 List<String> list2 = Arrays.asList("test", "string3", "test", "string4");
 List<String> result = new ArrayList<>();
 result.addAll(splitter.split("test-string1-test-string2"));
 result.addAll(splitter.split("test-string3-test-string4"));
 assertEquals(Joiner.on("-").join(list1), splitter.join(result));
 assertEquals(Joiner.on("-").join(list2), splitter.join(result));
 }

 @org.junit.Test
 public void testMapSplitter() {
 Splitter keySplitter = Splitter.on(":");
 Splitter valueSplitter = Splitter.on("-");
 Map<String, String> result = new HashMap<>();
 result.putAll(new Splitter.MapSplitter(keySplitter, valueSplitter).split("key1:value1-key2:value2-key3:value3"));
 assertEquals(Arrays.asList("key1", "value1"), result.get("key1"));
 assertEquals(Arrays.asList("key2", "value2"), result.get("key2"));
 assertEquals(Arrays.asList("key3", "value3"), result.get("key3"));
 }
}