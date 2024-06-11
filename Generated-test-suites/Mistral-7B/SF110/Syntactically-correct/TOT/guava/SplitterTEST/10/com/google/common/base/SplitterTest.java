package com.google.common.base;

import com.google.common.base.Splitter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class SplitterTest {

 @Test
 public void testOnCharSeparator() {
 Splitter splitter = Splitter.on('-');
 Iterable<String> result = splitter.split("test-one-two-three");
 List<String> listResult = new ArrayList<>(result);
 assertEquals(3, listResult.size());
 assertEquals("test", listResult.get(0));
 assertEquals("one", listResult.get(1));
 assertEquals("two", listResult.get(2));
 assertEquals("three", listResult.get(2)); // check for duplicate handling
 }

 @Test
 public void testOnStringSeparator() {
 Splitter splitter = Splitter.on("-");
 Iterable<String> result = splitter.split("test-one-two-three");
 List<String> listResult = new ArrayList<>(result);
 assertEquals(3, listResult.size());
 assertEquals("test", listResult.get(0));
 assertEquals("one", listResult.get(1));
 assertEquals("two", listResult.get(2));
 assertEquals("three", listResult.get(2)); // check for duplicate handling
 }

 @Test
 public void testOnPatternSeparator() {
 Splitter splitter = Splitter.onPattern("\\-");
 Iterable<String> result = splitter.split("test-one-two-three");
 List<String> listResult = new ArrayList<>(result);
 assertEquals(3, listResult.size());
 assertEquals("test", listResult.get(0));
 assertEquals("one", listResult.get(1));
 assertEquals("two", listResult.get(2));
 assertEquals("three", listResult.get(2)); // check for duplicate handling
 }

 @Test
 public void testOnEmptyStringSeparator() {
 Splitter splitter = Splitter.on("");
 Iterable<String> result = splitter.split("test");
 List<String> listResult = new ArrayList<>(result);
 assertEquals(1, listResult.size());
 assertEquals("test", listResult.get(0));
 }

 @Test
 public void testOnEmptySeparator() {
 Splitter splitter = Splitter.onPattern("\\s+");
 Iterable<String> result = splitter.split(" test ");
 List<String> listResult = new ArrayList<>(result);
 assertEquals(1, listResult.size());
 assertEquals(" test ", listResult.get(0));
 }

 @Test(expected = NullPointerException.class)
 public void testOnNullSeparator() {
 Splitter splitter = null;
 Iterable<String> result = splitter.split("test");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOnEmptySeparatorPattern() {
 Splitter splitter = Splitter.onPattern("");
 Iterable<String> result = splitter.split("test");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOnInvalidPatternSeparator() {
 Splitter splitter = Splitter.onPattern("\\W+");
 Iterable<String> result = splitter.split("test");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOnNullToSplit() {
 Splitter splitter = Splitter.on('-');
 Iterable<String> result = splitter.split(null);
 }

 @Test
 public void testLimit() {
 Splitter splitter = Splitter.on('-').limit(2);
 Iterable<String> result = splitter.split("test-one-two-three-four-five");
 List<String> listResult = new ArrayList<>(result);
 assertEquals(3, listResult.size());
 assertEquals("test", listResult.get(0));
 assertEquals("one-two", listResult.get(1));
 assertNull(listResult.get(2));
 }

 @Test
 public void testTrim() {
 Splitter splitter = Splitter.on('-').limit(2).trimResults();
 Iterable<String> result = splitter.split(" test-one-two-three-four-five ");
 List<String> listResult = new ArrayList<>(result);
 assertEquals(3, listResult.size());
 assertEquals("test", listResult.get(0));
 assertEquals("one-two", listResult.get(1));
 assertNull(listResult.get(2));
 }

 @Test
 public void testTrimAndLimit() {
 Splitter splitter = Splitter.on('-').limit(2).trimResults(CharMatcher.WHITESPACE);
 Iterable<String> result = splitter.split(" test-one-two-three-four-five ");
 List<String> listResult = new ArrayList<>(result);
 assertEquals(3, listResult.size());
 assertEquals("test", listResult.get(0));
 assertEquals("one-two", listResult.get(1));
 assertNull(listResult.get(2));
 }

 @Test
 public void testKeyValueSeparator() {
 Splitter splitter = Splitter.on("=").limit(2).trimResults();
 Map<String, String> mapResult = splitter.splitToMap("key1=value1&key2=value2&key3=value3");
 assertEquals(3, mapResult.size());
 assertEquals("key1", mapResult.get("key1"));
 assertEquals("value1", mapResult.get("value1"));
 assertEquals("key2", mapResult.get("key2"));
 assertEquals("value2", mapResult.get("value2"));
 assertEquals("key3", mapResult.get("key3"));
 assertEquals("value3", mapResult.get("value3"));
 }
}