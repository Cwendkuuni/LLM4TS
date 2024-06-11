package com.google.common.base;

import com.google.common.base.Joiner;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

public class JoinerTest {

 @Test
 public void testStringJoiner() {
 Joiner joiner = Joiner.on(",");
 String result = joiner.join(Arrays.asList("apple", "banana", "orange"));
 assertEquals("apple,banana,orange", result);
 }

 @Test(expected = NullPointerException.class)
 public void testNullJoiner() {
 Joiner joiner = null;
 joiner.join(Arrays.asList("apple", "banana", "orange"));
 }

 @Test
 public void testEmptyJoiner() {
 Joiner joiner = Joiner.on(",");
 String result = joiner.join(Collections.emptyList());
 assertEquals("", result);
 }

 @Test
 public void testJoinNullPart() {
 Joiner joiner = Joiner.on(",");
 String result = joiner.join(Arrays.asList("apple", null, "orange"));
 assertEquals("apple,null,orange", result);
 }

 @Test
 public void testJoinerWithCustomSeparator() {
 Joiner joiner = Joiner.on("*");
 String result = joiner.join(Arrays.asList("apple", "banana", "orange"));
 assertEquals("apple*banana*orange", result);
 }

 @Test
 public void testAppendTo() throws IOException {
 StringBuilder sb = new StringBuilder();
 Joiner joiner = Joiner.on(",");
 joiner.appendTo(sb, Arrays.asList("apple", "banana", "orange"));
 assertEquals("apple,banana,orange", sb.toString());
 }

 @Test
 public void testAppendToWithNullPart() throws IOException {
 StringBuilder sb = new StringBuilder();
 Joiner joiner = Joiner.on(",");
 joiner.appendTo(sb, Arrays.asList("apple", null, "orange"));
 assertEquals("apple,null,orange", sb.toString());
 }

 @Test
 public void testMapJoiner() {
 Joiner joiner = Joiner.on(":");
 Map<String, String> map = new HashMap<>();
 map.put("apple", "red");
 map.put("banana", "yellow");
 map.put("orange", "orange");
 String result = joiner.withKeyValueSeparator("=").join(map.entrySet());
 assertEquals("apple=red:banana=yellow:orange=orange", result);
 }

 @Test
 public void testMapJoinerWithNullValue() {
 Joiner joiner = Joiner.on(":");
 Map<String, String> map = new HashMap<>();
 map.put("apple", "red");
 map.put("banana", null);
 map.put("orange", "orange");
 String result = joiner.withKeyValueSeparator("=").join(map.entrySet());
 assertEquals("apple=red:banana=null:orange=orange", result);
 }

 @Test
 public void testMapJoinerWithCustomSeparator() {
 Joiner joiner = Joiner.on(":");
 Map<String, String> map = new HashMap<>();
 map.put("apple", "red");
 map.put("banana", "yellow");
 map.put("orange", "orange");
 String result = joiner.join(map.entrySet()).substring(0, result.length() - 1);
 assertEquals("apple:red:banana:yellow:orange", result);
 }

 @Test
 public void testAppendToMapJoiner() throws IOException {
 StringBuilder sb = new StringBuilder();
 Joiner joiner = Joiner.on(":");
 Joiner mapJoiner = joiner.withKeyValueSeparator("=");
 mapJoiner.appendTo(sb, map);
 assertEquals("apple=red:banana=null:orange=orange", sb.toString());
 }
}