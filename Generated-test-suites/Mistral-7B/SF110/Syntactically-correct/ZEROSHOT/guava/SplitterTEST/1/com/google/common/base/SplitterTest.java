package com.example.junit;

import com.google.common.base.Splitter;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class SplitterTest {

 @Test
 public void testOnChar() {
 Splitter splitter = Splitter.on('-');
 List<String> result = splitter.split("test-string");
 assertArrayEquals(new String[]{"test", "string"}, result.toArray());
 }

 @Test
 public void testOnString() {
 Splitter splitter = Splitter.on("-");
 List<String> result = splitter.split("test-string");
 assertArrayEquals(new String[]{"test", "string"}, result.toArray());
 }

 @Test
 public void testOnPattern() {
 Splitter splitter = Splitter.onPattern("\\-");
 List<String> result = splitter.split("test-string");
 assertArrayEquals(new String[]{"test", "string"}, result.toArray());
 }

 @Test
 public void testOnFixedLength() {
 Splitter splitter = Splitter.fixedLength(3);
 List<String> result = splitter.split("test123string");
 assertArrayEquals(new String[]{"test", "123", "string"}, result.toArray());
 }

 @Test
 public void testOnEmptyString() {
 Splitter splitter = Splitter.on(" ");
 List<String> result = splitter.split("test string");
 assertTrue(result.isEmpty());
 }

 @Test
 public void testLimit() {
 Splitter splitter = Splitter.on(" ");
 List<String> result = splitter.split("test test test test test test test test test test test");
 assertEquals(10, result.size());
 }

 @Test
 public void testTrimResults() {
 Splitter splitter = Splitter.on(" ");
 List<String> result = splitter.split(" test string  ");
 assertArrayEquals(new String[]{"test", "string"}, result.toArray());
 }

 @Test
 public void testTrimResultsWithCharMatcher() {
 Splitter splitter = Splitter.on(" ");
 Splitter trimmedSplitter = splitter.trimResults(CharMatcher.WHITESPACE);
 List<String> result = trimmedSplitter.split(" test string  ");
 assertArrayEquals(new String[]{"test", "string"}, result.toArray());
 }

 @Test
 public void testMapSplitter() {
 Splitter keySplitter = Splitter.on(":");
 Splitter valueSplitter = Splitter.on("=");
 MapSplitter mapSplitter = keySplitter.withKeyValueSeparator(valueSplitter);
 String testString = "key1:value1= test key2:value2=test key3:value3=";
 Map<String, String> result = mapSplitter.split(testString);
 assertEquals(3, result.size());
 assertEquals("key1", result.get("key1"));
 assertEquals("value1", result.get("value1"));
 assertEquals("key2", result.get("key2"));
 assertEquals("value2", result.get("value2"));
 assertEquals("key3", result.get("key3"));
 assertEquals("value3", result.get("value3"));
 }
}