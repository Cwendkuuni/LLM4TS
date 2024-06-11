package com.google.common.base;

import com.google.common.base.Joiner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.*;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class JoinerTest {

 // Test for the static factory method on(String separator)
 @Test
 public void testJoinStringSeparator() {
 Joiner joiner = Joiner.on(",");
 String result = joiner.join(Arrays.asList("apple", "banana", "orange"));
 assertEquals("apple,banana,orange", result);
 }

 // Test for the static factory method on(char separator)
 @Test
 public void testJoinCharSeparator() {
 Joiner joiner = Joiner.on('-');
 String result = joiner.join(Arrays.asList("apple", "banana", "orange"));
 assertEquals("apple-banana-orange", result);
 }

 // Test for the constructor Joiner(String separator)
 @Test
 public void testJoinerStringSeparator() {
 Joiner joiner = new Joiner(",");
 String result = joiner.join(Arrays.asList("apple", "banana", "orange"));
 assertEquals("apple,banana,orange", result);
 }

 // Test for the method appendTo(A appendable, Iterable<?> parts)
 @Test
 public void testAppendToIterable() throws IOException {
 StringBuilder appendable = new StringBuilder();
 Joiner joiner = new Joiner(",");
 joiner.appendTo(appendable, Arrays.asList("apple", "banana", "orange"));
 assertEquals("apple,banana,orange", appendable.toString());
 }

 // Test for the method appendTo(A appendable, Object[] parts)
 @Test
 public void testAppendToObjectArray() throws IOException {
 StringBuilder appendable = new StringBuilder();
 Joiner joiner = new Joiner(",");
 joiner.appendTo(appendable, new Object[]{"apple", "banana", "orange"});
 assertEquals("apple,banana,orange", appendable.toString());
 }

 // Test for the method appendTo(A appendable, Iterator<?> parts)
 @Test
 public void testAppendToIterator() throws IOException {
 StringBuilder appendable = new StringBuilder();
 Joiner joiner = new Joiner(",");
 Iterator<String> iterator = Arrays.asList("apple", "banana", "orange").iterator();
 joiner.appendTo(appendable, iterator);
 assertEquals("apple,banana,orange", appendable.toString());
 }

 // Test for the method appendTo(A appendable, Object first, Object second, Object... rest)
 @Test
 public void testAppendToObjectArrayWithVariadic() throws IOException {
 StringBuilder appendable = new StringBuilder();
 Joiner joiner = new Joiner(",");
 joiner.appendTo(appendable, "apple", "banana", "orange");
 assertEquals("apple,banana,orange", appendable.toString());
 }

 // Test for the method join(Iterable<?> parts)
 @Test
 public void testJoinIterable() {
 Joiner joiner = new Joiner(",");
 String result = joiner.join(Arrays.asList("apple", "banana", "orange"));
 assertEquals("apple,banana,orange", result);
 }

 // Test for the method join(Object[] parts)
 @Test
 public void testJoinObjectArray() {
 Joiner joiner = new Joiner(",");
 String result = joiner.join(new Object[]{"apple", "banana", "orange"});
 assertEquals("apple,banana,orange", result);
 }

 // Test for the method join(Object first, Object second, Object... rest)
 @Test
 public void testJoinObjectArrayWithVariadic() {
 Joiner joiner = new Joiner(",");
 String result = joiner.join("apple", "banana", "orange");
 assertEquals("apple,banana,orange", result);
 }

 // Test for the method useForNull(String nullText)
 @Test
 public void testUseForNull() {
 Joiner joiner = Joiner.on(",").useForNull("N/A");
 String result = joiner.join(Arrays.asList(new Object[]{"apple", null, "orange"}));
 assertEquals("apple,N/A,orange", result);
 }

 // Test for the method skipNulls()
 @Test
 public void testSkipNulls() {
 Joiner joiner = Joiner.on(",").skipNulls();
 String result = joiner.join(Arrays.asList(new Object[]{"apple", null, "orange"}));
 assertEquals("apple,orange", result);
 }

 // Test for the MapJoiner class
 @Test
 public void testMapJoiner() {
 Joiner joiner = Joiner.on(",");
 Map<String, String> map = new HashMap<>();
 map.put("apple", "red");
 map.put("banana", "yellow");
 map.put("orange", "orange");
 MapJoiner mapJoiner = joiner.withKeyValueSeparator(":");
 String result = mapJoiner.join(map);
 assertEquals("apple:red,banana:yellow,orange:orange", result);
 }

 // Test for the MapJoiner.appendTo(A appendable, Map<?, ?> map) method
 @Test
 public void testMapJoinerAppendTo() throws IOException {
 StringBuilder appendable = new StringBuilder();
 Joiner joiner = Joiner.on(",");
 MapJoiner mapJoiner = joiner.withKeyValueSeparator(":");
 Map<String, String> map = new HashMap<>();
 map.put("apple", "red");
 map.put("banana", "yellow");
 map.put("orange", "orange");
 mapJoiner.appendTo(appendable, map);
 assertEquals("apple:red,banana:yellow,orange:orange", appendable.toString());
 }

 // Test for the MapJoiner.join(Map<?, ?> map) method
 @Test
 public void testMapJoinerJoin() {
 Joiner joiner = Joiner.on(",");
 MapJoiner mapJoiner = joiner.withKeyValueSeparator(":");
 Map<String, String> map = new HashMap<>();
 map.put("apple", "red");
 map.put("banana", "yellow");
 map.put("orange", "orange");
 String result = mapJoiner.join(map);
 assertEquals("apple:red,banana:yellow,orange:orange", result);
 }
}