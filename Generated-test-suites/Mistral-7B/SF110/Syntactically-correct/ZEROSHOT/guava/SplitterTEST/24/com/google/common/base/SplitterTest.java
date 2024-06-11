package com.example.junit;

import static org.junit.Assert.*;
import com.google.common.base.Splitter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SplitterTest {

 @org.junit.Test
 public void testOnChar() {
 Splitter splitter = Splitter.on('-');
 List<String> result = splitter.split("this-is-a-test-string");
 assertEquals(Arrays.asList("this", "is", "a", "test", "string"), result);
 }

 @org.junit.Test
 public void testOnCharWithTrimmer() {
 Splitter splitter = Splitter.on('-').trimResults();
 List<String> result = splitter.split("this-is-a-test-string");
 assertEquals(Arrays.asList("this", "is", "a", "test", "string"), result);
 assertEquals(Arrays.asList("this", "is", "a", "test", "string"), splitter.split(" this is a test string "));
 }

 @org.junit.Test
 public void testOnString() {
 Splitter splitter = Splitter.on("-");
 List<String> result = splitter.split("this-is-a-test-string");
 assertEquals(Arrays.asList("this", "is", "a", "test", "string"), result);
 }

 @org.junit.Test
 public void testOnPattern() {
 Splitter splitter = Splitter.onPattern("[\\s]+");
 List<String> result = splitter.split("this is a test string");
 assertEquals(Arrays.asList("this", "is", "a", "test", "string"), result);
 }

 @org.junit.Test
 public void testOnPatternWithEmptyString() {
 Splitter splitter = Splitter.onPattern("[\\s]+").omitEmptyStrings();
 List<String> result = splitter.split("this is a test string");
 assertEquals(Arrays.asList("this", "is", "a", "test", "string"), result);
 List<String> result2 = splitter.split(" this is a test string ");
 assertEquals(Arrays.asList("this", "is", "a", "test", "string"), result2);
 List<String> result3 = splitter.split(" this is a test string ");
 assertEquals(Arrays.asList("this", "is", "a", "test", "string"), result3);
 }

 @org.junit.Test
 public void testFixedLength() {
 Splitter splitter = Splitter.fixedLength(3);
 List<String> result = splitter.split("abcdefghijklmnopqrstuvwxyz");
 assertEquals(Arrays.asList("abc", "def", "ghi", "jkl", "mno", "pqr", "stu", "vwxy", "z"), result);
 }

 @org.junit.Test
 public void testLimit() {
 Splitter splitter = Splitter.on('-').limit(5);
 List<String> result = splitter.split("this-is-a-test-string-this-is-a-test-string-this-is-a-test-string-this-is-a-test-string-this-is-a-test-string");
 assertEquals(Arrays.asList("this-is-a-test-string", "this-is-a-test-string", "this-is-a-test-string", "this-is-a-test-string", "this-is-a-test-string"), result);
 }

 @org.junit.Test
 public void testMapSplitter() {
 Splitter outerSplitter = Splitter.on(":");
 Splitter innerSplitter = Splitter.on("=");
 MapSplitter mapSplitter = outerSplitter.withKeyValueSeparator(innerSplitter);
 String testString = "key1:value1=value1.1:key2:value2=value2.2:key3:value3=value3.3";
 Map<String, String> result = mapSplitter.split(testString);
 assertEquals(Collections.unmodifiableMap(Map.of("key1", "value1.1", "key2", "value2.2", "key3", "value3.3")), result);
 }
}