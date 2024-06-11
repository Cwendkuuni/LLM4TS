package com.google.common.base;

import com.google.common.base.Joiner;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class JoinerTest {

 @Test
 public void testSimpleJoinString() {
 Joiner joiner = Joiner.on(":");
 List<String> list = Arrays.asList("apple", "banana", "cherry");
 String result = joiner.join(list);
 assertEquals("apple:banana:cherry", result);
 }

 @Test
 public void testJoinEmptyList() {
 Joiner joiner = Joiner.on(",");
 List<String> list = Collections.emptyList();
 String result = joiner.join(list);
 assertEquals("", result);
 }

 @Test(expected = NullPointerException.class)
 public void testNullIterator() {
 Joiner joiner = Joiner.on(",");
 Iterator<String> iterator = null;
 joiner.appendTo(new StringBuilder(), iterator);
 }

 @Test
 public void testJoinIterableNullsIncluded() {
 Joiner joiner = Joiner.on(",");
 List<Object> list = Arrays.asList("apple", null, "cherry");
 String result = joiner.join(list);
 assertEquals("apple,null,cherry", result);
 }

 @Test
 public void testJoinIterableNullsSkipped() {
 Joiner joiner = Joiner.on(",").skipNulls();
 List<Object> list = Arrays.asList("apple", null, "cherry");
 String result = joiner.join(list);
 assertEquals("apple,cherry", result);
 }

 @Test
 public void testJoinObjectArray() {
 Joiner joiner = Joiner.on(",");
 Object[] array = {"apple", null, "cherry"};
 String result = joiner.join(Arrays.asList(array));
 assertEquals("apple,null,cherry", result);
 }

 @Test
 public void testJoinMapEntries() {
 Joiner joiner = Joiner.on(":");
 Map<String, String> map = new HashMap<>();
 map.put("apple", "red");
 map.put("banana", "yellow");
 map.put("cherry", null);
 String result = new Joiner(joiner).withKeyValueSeparator(":").join(map.entrySet());
 assertEquals("apple:red:banana:yellow", result);
 }

 @Test
 public void testJoinMapEntriesNullValuesSkipped() {
 Joiner joiner = Joiner.on(":").useForNull("NA");
 Map<String, String> map = new HashMap<>();
 map.put("apple", "red");
 map.put("banana", null);
 map.put("cherry", "green");
 String result = new Joiner(joiner).withKeyValueSeparator(":").join(map.entrySet());
 assertEquals("apple:red:cherry:green", result);
 }

 @Test
 public void testJoinMapEntriesNullKeysSkipped() {
 Joiner joiner = Joiner.on(":").skipNulls();
 Map<String, String> map = new HashMap<>();
 map.put(null, "red");
 map.put("apple", "green");
 map.put("banana", null);
 String result = new Joiner(joiner).withKeyValueSeparator(":").join(map.entrySet());
 assertEquals("apple:green", result);
 }
}