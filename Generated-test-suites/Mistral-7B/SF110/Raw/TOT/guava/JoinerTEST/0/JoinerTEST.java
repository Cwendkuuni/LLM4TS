package com.google.common.base;

import com.google.common.base.Joiner;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class JoinerTest {

 @Test
 public void testStringJoinerWithStringSeparator() {
 Joiner joiner = Joiner.on(":");
 String result = joiner.join(Arrays.asList("apple", "banana", "cherry"));
 assertEquals("apple:banana:cherry", result);
 }

 @Test
 public void testStringJoinerWithCharSeparator() {
 Joiner joiner = Joiner.on('-');
 String result = joiner.join(Arrays.asList("apple", "banana", "cherry"));
 assertEquals("apple-banana-cherry", result);
 }

 @Test
 public void testAppendToStringBuilderWithIterable() throws IOException {
 Joiner joiner = Joiner.on(":");
 StringBuilder builder = new StringBuilder();
 joiner.appendTo(builder, Arrays.asList("apple", "banana", "cherry"));
 assertEquals("apple:banana:cherry", builder.toString());
 }

 @Test
 public void testAppendToStringBuilderWithString() throws IOException {
 Joiner joiner = Joiner.on(":");
 StringBuilder builder = new StringBuilder();
 joiner.appendTo(builder, "apple");
 assertEquals("apple", builder.toString());
 }

 @Test
 public void testAppendToStringBuilderWithNull() throws IOException {
 Joiner joiner = Joiner.on(":");
 StringBuilder builder = new StringBuilder();
 joiner.appendTo(builder, (Iterable<?>) Collections.singletonList(null));
 assertEquals("", builder.toString());
 }

 @Test
 public void testAppendToStringBuilderWithObjectArray() throws IOException {
 Joiner joiner = Joiner.on(":");
 StringBuilder builder = new StringBuilder();
 joiner.appendTo(builder, new Object[]{"apple", "banana", "cherry"});
 assertEquals("apple:banana:cherry", builder.toString());
 }

 @Test
 public void testAppendToStringBuilderWithNullAndObjectArray() throws IOException {
 Joiner joiner = Joiner.on(":");
 StringBuilder builder = new StringBuilder();
 joiner.appendTo(builder, new Object[]{null, "apple", "banana", "cherry"});
 assertEquals("", builder.toString());
 }

 @Test
 public void testJoinWithString() {
 Joiner joiner = Joiner.on(":");
 String result = joiner.join(Arrays.asList("apple", "banana", "cherry"));
 assertEquals("apple:banana:cherry", result);
 }

 @Test
 public void testJoinWithChar() {
 Joiner joiner = Joiner.on('-');
 String result = joiner.join(Arrays.asList("apple", "banana", "cherry"));
 assertEquals("apple-banana-cherry", result);
 }

 @Test
 public void testJoinWithIterable() {
 Joiner joiner = Joiner.on(":");
 String result = joiner.join(Arrays.asList("apple", "banana", "cherry"));
 assertEquals("apple:banana:cherry", result);
 }

 @Test
 public void testJoinWithObjectArray() {
 Joiner joiner = Joiner.on(":");
 String result = joiner.join(new Object[]{"apple", "banana", "cherry"});
 assertEquals("apple:banana:cherry", result);
 }

 @Test
 public void testUseForNull() {
 Joiner joiner = Joiner.on(":");
 Joiner nullJoiner = joiner.useForNull("NULL");
 String result = nullJoiner.join(Arrays.asList("apple", null, "cherry"));
 assertEquals("apple:NULL:cherry", result);
 }

 @Test
 public void testSkipNulls() {
 Joiner joiner = Joiner.on(":");
 Joiner skipNullJoiner = joiner.skipNulls();
 String result = skipNullJoiner.join(Arrays.asList("apple", null, "cherry"));
 assertEquals("apple:cherry", result);
 }

 @Test
 public void testWithKeyValueSeparator() {
 Joiner joiner = Joiner.on(":");
 MapJoiner mapJoiner = joiner.withKeyValueSeparator("=");
 Map<String, String> map = new HashMap<>();
 map.put("key1", "value1");
 map.put("key2", null);
 map.put("key3", "value3");Map<String,String> map = new HashMap<>();
 map.put("key1", "value1");
 map.put("key2", null);
 map.put("key3", "value3");
 String result = mapJoiner.join(map.entrySet());
 assertEquals("key1=value1:key3=value3", result);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testWithKeyValueSeparatorWithUseForNull() {
 Joiner joiner = Joiner.on(":");
 Joiner skipNullJoiner = joiner.skipNulls();
 Joiner mapJoiner = skipNullJoiner.withKeyValueSeparator("=");
 Map<String, String> map = new HashMap<>();
 map.put("key1", null);
 map.put("key2", "value2");
 map.put("key3", null);
 map.put("key4", "value4");
 mapJoiner.join(map.entrySet());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testWithKeyValueSeparatorWithUseForNullAndSkipNulls() {
 Joiner joiner = Joiner.on(":");
 Joiner nullJoiner = joiner.useForNull("NULL");
 Joiner skipNullJoiner = joiner.skipNulls();
 Joiner mapJoiner = skipNullJoiner.withKeyValueSeparator("=");
 Map<String, String> map = new HashMap<>();
 map.put("key1", null);
 map.put("key2", "value2");
 map.put("key3", null);
 map.put("key4", "value4");
 mapJoiner.join(map.entrySet());
 }
}