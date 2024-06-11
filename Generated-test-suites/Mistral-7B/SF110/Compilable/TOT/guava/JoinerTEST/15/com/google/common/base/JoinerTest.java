package com.google.common.base;

import com.google.common.base.Joiner;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JoinerTest {

 @Test
 public void testJoinIterableStringSeparator() {
 Joiner joiner = Joiner.on(",");
 String result = joiner.join(Arrays.asList("apple", "banana", "cherry"));
 assertEquals("apple,banana,cherry", result);
 }

 @Test
 public void testJoinIterableCharSeparator() {
 Joiner joiner = Joiner.on("-");
 String result = joiner.join(Arrays.asList("apple", "banana", "cherry"));
 assertEquals("apple-banana-cherry", result);
 }

 @Test
 public void testJoinObjectArrayStringSeparator() {
 Joiner joiner = Joiner.on(",");
 String result = joiner.join(new Object[]{"apple", "banana", "cherry"});
 assertEquals("apple,banana,cherry", result);
 }

 @Test
 public void testJoinObjectArrayCharSeparator() {
 Joiner joiner = Joiner.on("-");
 String result = joiner.join(new Object[]{"apple", "banana", "cherry"});
 assertEquals("apple-banana-cherry", result);
 }

 @Test
 public void testJoinIteratorEmpty() {
 Joiner joiner = Joiner.on(",");
 StringBuilder sb = new StringBuilder();
 joiner.appendTo(sb, Collections.emptyIterator());
 assertEquals("", sb.toString());
 }

 @Test
 public void testJoinIteratorSingle() {
 Joiner joiner = Joiner.on(",");
 StringBuilder sb = new StringBuilder();
 joiner.appendTo(sb, Arrays.asList("apple").iterator());
 assertEquals("apple", sb.toString());
 }

 @Test
 public void testJoinIteratorMultiple() {
 Joiner joiner = Joiner.on(",");
 StringBuilder sb = new StringBuilder();
 joiner.appendTo(sb, Arrays.asList("apple", "banana", "cherry").iterator());
 assertEquals("apple,banana,cherry", sb.toString());
 }

 @Test
 public void testJoinIteratorNulls() {
 Joiner joiner = Joiner.on(",");
 StringBuilder sb = new StringBuilder();
 joiner.appendTo(sb, Arrays.asList(null, "apple", null, "cherry").iterator());
 assertEquals("null,apple,null", sb.toString());
 }

 @Test
 public void testJoinMapEntryStringKeyValueSeparator() {
 Joiner joiner = Joiner.on("=");
 Map<String, String> map = new HashMap<>();
 map.put("key1", "value1");
 map.put("key2", "value2");
 String result = joiner.join(map.entrySet());
 assertEquals("key1=value1=key2=value2", result);
 }

 @Test
 public void testJoinMapEntryCharKeyValueSeparator() {
 Joiner joiner = Joiner.on("-");
 Map<String, String> map = new HashMap<>();
 map.put("key1", "value1");
 map.put("key2", "value2");
 String result = joiner.join(map.entrySet());
 assertEquals("key1-value1-key2-value2", result);
 }
}