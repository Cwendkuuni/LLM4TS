package com.google.common.base;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import org.junit.Test;
import static org.junit.Assert.*;

public class SplitterTest {

 @Test
 public void testOnChar() {
 Splitter splitter = Splitter.on('-');
 Iterable<String> result = splitter.split("test-string");
 List<String> list = splitter.splitToList("test-string");

 assertEquals("test", result.iterator().next());
 assertEquals("string", result.iterator().next());
 assertArrayEquals(new String[]{"test", "string"}, list.toArray());
 }

 @Test
 public void testOnChar_omitEmptyStrings() {
 Splitter splitter = Splitter.on('-').omitEmptyStrings();
 Iterable<String> result = splitter.split("test--string");
 List<String> list = splitter.splitToList("test--string");

 assertEquals("test", result.iterator().next());
 assertEquals("string", result.iterator().next());
 assertArrayEquals(new String[]{"test", "string"}, list.toArray());
 }

 @Test(expected = NullPointerException.class)
 public void testOnChar_nullInput() {
 Splitter splitter = Splitter.on('-');
 splitter.split(null);
 }

 @Test
 public void testOnPattern() {
 Splitter splitter = Splitter.onPattern("\\.");
 Iterable<String> result = splitter.split("test.string.another.test");
 List<String> list = splitter.splitToList("test.string.another.test");

 assertEquals("test", result.iterator().next());
 assertEquals("string", result.iterator().next());
 assertEquals("another", result.iterator().next());
 assertEquals("test", result.iterator().next());
 assertArrayEquals(new String[]{"test", "string", "another", "test"}, list.toArray());
 }

 @Test
 public void testOnPattern_emptyString() {
 Splitter splitter = Splitter.onPattern("\\.");
 Iterable<String> result = splitter.split(".");
 List<String> list = splitter.splitToList(".");

 assertEquals("", result.iterator().next());
 assertArrayEquals(new String[]{""}, list.toArray());
 }

 @Test
 public void testOnPattern_nullInput() {
 Splitter splitter = Splitter.onPattern("\\.");
 splitter.split(null);
 }

 @Test
 public void testOnRegex() {
 Splitter splitter = Splitter.on("[\\s]+");
 Iterable<String> result = splitter.split("test string test string test string");
 List<String> list = splitter.splitToList("test string test string test string");

 assertEquals("test", result.iterator().next());
 assertEquals("string", result.iterator().next());
 assertEquals("test", result.iterator().next());
 assertEquals("string", result.iterator().next());
 assertArrayEquals(new String[]{"test", "string", "test", "string"}, list.toArray());
 }

 @Test
 public void testOnRegex_emptyString() {
 Splitter splitter = Splitter.on("[\\s]+");
 Iterable<String> result = splitter.split("");
 List<String> list = splitter.splitToList("");

 assertEquals(Collections.emptyList(), result);
 assertArrayEquals(new String[]{}, list.toArray());
 }

 @Test
 public void testOnRegex_nullInput() {
 Splitter splitter = Splitter.on("[\\s]+");
 splitter.split(null);
 }

 @Test
 public void testFixedLength() {
 Splitter splitter = Splitter.fixedLength(2);
 Iterable<String> result = splitter.split("teststringteststringteststring");
 List<String> list = splitter.splitToList("teststringteststringteststring");

 assertEquals("tes", result.iterator().next());
 assertEquals("tstr", result.iterator().next());
 assertEquals("test", result.iterator().next());
 assertEquals("string", result.iterator().next());
 assertArrayEquals(new String[]{"tes", "tstr", "test", "string"}, list.toArray());
 }

 @Test
 public void testFixedLength_emptyString() {
 Splitter splitter = Splitter.fixedLength(2);
 Iterable<String> result = splitter.split("");
 List<String> list = splitter.splitToList("");

 assertEquals(Collections.emptyList(), result);
 assertArrayEquals(new String[]{}, list.toArray());
 }

 @Test
 public void testFixedLength_nullInput() {
 Splitter splitter = Splitter.fixedLength(2);
 splitter.split(null);
 }

 @Test
 public void testTrimResults() {
 Splitter splitter = Splitter.on(" ").trimResults();
 Iterable<String> result = splitter.split("test string test string test string");
 List<String> list = splitter.splitToList("test string test string test string");

 assertEquals("test", result.iterator().next());
 assertEquals("string", result.iterator().next());
 assertEquals("test", result.iterator().next());
 assertEquals("string", result.iterator().next());
 assertArrayEquals(new String[]{"test", "string", "test", "string"}, list.toArray());
 }

 @Test
 public void testTrimResults_nullInput() {
 Splitter splitter = Splitter.on(" ").trimResults();
 splitter.split(null);
 }

 @Test
 public void testTrimResultsWithCharMatcher() {
 Splitter splitter = Splitter.on(" ").trimResults(CharMatcher.WHITESPACE);
 Iterable<String> result = splitter.split("test string test string test string");
 List<String> list = splitter.splitToList("test string test string test string");

 assertEquals("test", result.iterator().next());
 assertEquals("string", result.iterator().next());
 assertEquals("test", result.iterator().next());
 assertEquals("string", result.iterator().next());
 assertArrayEquals(new String[]{"test", "string", "test", "string"}, list.toArray());
 }

 @Test
 public void testWithKeyValueSeparator() {
 Splitter splitter = Splitter.on("=").withKeyValueSeparator(Splitter.on("="));
 Map<String, String> map = splitter.split("key1=value1&key2=value2&key3=value3");
 assertEquals("key1", map.get("key1"));
 assertEquals("value1", map.get("key1"));
 assertEquals("key2", map.get("key2"));
 assertEquals("value2", map.get("key2"));
 assertEquals("key3", map.get("key3"));
 assertEquals("value3", map.get("key3"));
 }

 @Test
 public void testWithKeyValueSeparator_emptyString() {
 Splitter splitter = Splitter.on("=").withKeyValueSeparator(Splitter.on("="));
 Map<String, String> map = splitter.split("");
 assertEquals(Collections.emptyMap(), map);
 }

 @Test
 public void testWithKeyValueSeparator_nullInput() {
 Splitter splitter = Splitter.on("=").withKeyValueSeparator(Splitter.on("="));
 splitter.split(null);
 }
}