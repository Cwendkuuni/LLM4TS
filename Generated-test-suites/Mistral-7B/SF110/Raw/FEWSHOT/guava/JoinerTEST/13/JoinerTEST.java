package com.google.common.base;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;

public class JoinerTest {

 @Test
 public void testJoinerOnString() {
 Joiner joiner = Joiner.on(":");
 List<String> list = Arrays.asList("foo", "bar", "baz");
 String result = joiner.join(list);
 assertEquals("foo:bar:baz", result);
 }

 @Test
 public void testJoinerOnChar() {
 Joiner joiner = Joiner.on(':');
 List<String> list = Arrays.asList("foo", "bar", "baz");
 String result = joiner.join(list);
 assertEquals("foo:bar:baz", result);
 }

 @Test
 public void testAppendToIterator() throws IOException {
 StringBuilder builder = new StringBuilder();
 Joiner joiner = Joiner.on(":");
 List<String> list = Arrays.asList("foo", "bar", "baz");
 joiner.appendTo(builder, list.iterator());
 assertEquals("foo:bar:baz", builder.toString());
 }

 @Test
 public void testAppendToObjectArray() throws IOException {
 StringBuilder builder = new StringBuilder();
 Joiner joiner = Joiner.on(":");
 joiner.appendTo(builder, new Object[]{"foo", "bar", "baz"});
 assertEquals("foo:bar:baz", builder.toString());
 }

 @Test
 public void testAppendToIterable() throws IOException {
 StringBuilder builder = new StringBuilder();
 Joiner joiner = Joiner.on(":");
 List<String> list = Arrays.asList("foo", "bar", "baz");
 joiner.appendTo(builder, list);
 assertEquals("foo:bar:baz", builder.toString());
 }

 @Test
 public void testJoinIterable() {
 Joiner joiner = Joiner.on(":");
 List<String> list = Arrays.asList("foo", "bar", "baz");
 String result = joiner.join(list);
 assertEquals("foo:bar:baz", result);
 }

 @Test
 public void testJoinObjectArray() {
 Joiner joiner = Joiner.on(":");
 Object[] objArray = {"foo", "bar", "baz"};
 String result = joiner.join(objArray);
 assertEquals("foo:bar:baz", result);
 }

 @Test
 public void testUseForNull() {
 Joiner joiner = Joiner.on(":");
 String result = joiner.useForNull("NA").join(Arrays.asList("foo", null, "baz"));
 assertEquals("foo:NA:baz", result);
 }

 @Test
 public void testSkipNulls() {
 Joiner joiner = Joiner.on(":");
 String result = joiner.skipNulls().join(Arrays.asList("foo", null, "bar", "baz"));
 assertEquals("foo:bar:baz", result);
 }

 @Test
 public void testMapJoinerOnString() {
 Joiner joiner = Joiner.on(":");
 Map<String, String> map = new HashMap<>();
 map.put("key1", "value1");
 map.put("key2", "value2");
 map.put("key3", "value3");
 MapJoiner mapJoiner = joiner.withKeyValueSeparator("=");
 String result = mapJoiner.join(map.entrySet());
 assertEquals("key1=value1:key2=value2:key3=value3", result);
 }

 @Test
 public void testMapJoinerOnChar() {
 Joiner joiner = Joiner.on(":");
 Map<String, String> map = new HashMap<>();
 map.put("key1", "value1");
 map.put("key2", "value2");
 map.put("key3", "value3");
 MapJoiner mapJoiner = joiner.withKeyValueSeparator('=').useForNull("NA");
 String result = mapJoiner.join(map.entrySet());
 assertEquals("key1=value1:key2=value2:key3=NA", result);
 }
}