package com.google.common.base;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.google.common.base.Joiner;
import com.google.common.base.MapJoiner;
import com.google.common.collect.Maps;

public class JoinerTest {

 // Testing the Joiner's constructor
 @Test
 public void testJoinerConstruction() {
 Joiner joiner = Joiner.on(":");
 assertNotNull(joiner);
 }

 // Testing appendTo method with Iterable
 @Test
 public void testAppendToIterable() throws IOException {
 StringBuilder stringBuilder = new StringBuilder();
 Joiner joiner = Joiner.on(",");
 List<String> list = Arrays.asList("apple", "banana", "cherry");
 joiner.appendTo(stringBuilder, list);
 assertEquals("apple,banana,cherry", stringBuilder.toString());
 }

 // Testing appendTo method with Object[]
 @Test
 public void testAppendToObjectArray() throws IOException {
 StringBuilder stringBuilder = new StringBuilder();
 Joiner joiner = Joiner.on("-");
 joiner.appendTo(stringBuilder, new Object[]{"apple", "banana", "cherry"});
 assertEquals("apple-banana-cherry", stringBuilder.toString());
 }

 // Testing appendTo method with Iterator
 @Test
 public void testAppendToIterator() throws IOException {
 StringBuilder stringBuilder = new StringBuilder();
 Joiner joiner = Joiner.on("|");
 Iterator<String> iterator = Arrays.asList("apple", "banana", "cherry").iterator();
 joiner.appendTo(stringBuilder, iterator);
 assertEquals("apple|banana|cherry", stringBuilder.toString());
 }

 // Testing appendTo method with null objects
 @Test
 public void testAppendToNullObjects() throws IOException {
 StringBuilder stringBuilder = new StringBuilder();
 Joiner joiner = Joiner.on(";");
 joiner.appendTo(stringBuilder, new Object[]{"apple", null, "cherry"});
 assertEquals("apple;null;cherry", stringBuilder.toString());
 }

 // Testing join method with Iterable
 @Test
 public void testJoinIterable() {
 String joinString = Joiner.on(",").join(Arrays.asList("apple", "banana", "cherry"));
 assertEquals("apple,banana,cherry", joinString);
 }

 // Testing join method with Object[]
 @Test
 public void testJoinObjectArray() {
 String joinString = Joiner.on("-").join(new Object[]{"apple", "banana", "cherry"});
 assertEquals("apple-banana-cherry", joinString);
 }

 // Testing join method with Iterator
 @Test
 public void testJoinIterator() {
 String joinString = Joiner.on("|").join(Arrays.asList("apple", "banana", "cherry").iterator());
 assertEquals("apple|banana|cherry", joinString);
 }

 // Testing useForNull method
 @Test
 public void testUseForNull() {
 Joiner joiner = Joiner.on(",").useForNull("N/A");
 String[] nullArray = {null, null, "cherry"};
 StringBuilder stringBuilder = new StringBuilder();
 joiner.appendTo(stringBuilder, nullArray);
 assertEquals("N/A,N/A,cherry", stringBuilder.toString());
 }

 // Testing skipNulls method
 @Test
 public void testSkipNulls() {
 Joiner joiner = Joiner.on(",").skipNulls();
 String[] array = {"apple", null, "banana", null, "cherry"};
 StringBuilder stringBuilder = new StringBuilder();
 joiner.appendTo(stringBuilder, array);
 assertEquals("apple,banana", stringBuilder.toString());
 }

 // Testing MapJoiner
 @Test
 public void testMapJoiner() {
 Map<String, String> map = Maps.newHashMap();
 map.put("name", "John");
 map.put("age", "25");
 map.put("city", "New York");
 MapJoiner mapJoiner = Joiner.on("=").withKeyValueSeparator(":");
 StringBuilder stringBuilder = new StringBuilder();
 mapJoiner.appendTo(stringBuilder, map);
 assertEquals("name:John=age:25=city:NewYork", stringBuilder.toString());
 }

 // Testing MapJoiner with useForNull method
 @Test
 public void testMapJoinerUseForNull() {
 Map<String, String> map = Maps.newHashMap();
 map.put("name", "John");
 map.put("age", null);
 map.put("city", "New York");
 MapJoiner mapJoiner = Joiner.on("=").withKeyValueSeparator(":").useForNull("N/A");
 StringBuilder stringBuilder = new StringBuilder();
 mapJoiner.appendTo(stringBuilder, map);
 assertEquals("name:John=age:N/A=city:NewYork", stringBuilder.toString());
 }

 // Testing MapJoiner with skipNulls method
 @Test
 public void testMapJoinerSkipNulls() {
 Map<String, String> map = Maps.newHashMap();
 map.put("name", "John");
 map.put("age", null);
 map.put("city", "New York");
 MapJoiner mapJoiner = Joiner.on("=").withKeyValueSeparator(":").skipNulls();
 StringBuilder stringBuilder = new StringBuilder();
 mapJoiner.appendTo(stringBuilder, map);
 assertEquals("name:John", stringBuilder.toString());
 }
}