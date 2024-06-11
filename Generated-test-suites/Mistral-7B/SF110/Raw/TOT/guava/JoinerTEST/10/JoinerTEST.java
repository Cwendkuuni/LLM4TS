package com.google.common.base;

import com.google.common.base.Joiner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class JoinerTest {

 @Test
 public void testStringJoiner() {
 Joiner joiner = Joiner.on(",");
 String result = joiner.join(Arrays.asList("apple", "banana", "cherry"));
 assertEquals("apple,banana,cherry", result);
 }

 @Test
 public void testCharJoiner() {
 Joiner joiner = Joiner.on('/');
 String result = joiner.join(Arrays.asList("apple", "banana", "cherry"));
 assertEquals("apple/banana/cherry", result);
 }

 @Test
 public void testNullJoiner() {
 Joiner joiner = Joiner.on("%");
 String result = joiner.join(Arrays.asList("apple", null, "cherry"));
 assertEquals("apple%null%cherry", result);
 }

 @Test
 public void testEmptyIterable() {
 Joiner joiner = Joiner.on("|");
 String result = joiner.join(Collections.emptyList());
 assertEquals("", result);
 }

 @Test(expected = NullPointerException.class)
 public void testNullJoinerParts() {
 Joiner joiner = Joiner.on("@");
 joiner.appendTo(null, Arrays.asList("apple", "banana", "cherry"));
 }

 @Test(expected = NullPointerException.class)
 public void testNullJoinerIterator() {
 Joiner joiner = Joiner.on("$");
 joiner.appendTo(new StringBuilder(), null);
 }

 @Test(expected = NullPointerException.class)
 public void testNullJoinerObjectArray() {
 Joiner joiner = Joiner.on("^");
 joiner.appendTo(new StringBuilder(), (Iterable<?>)null);
 }

 @Test
 public void testMapJoiner() {
 Joiner joiner = Joiner.on(":");
 Map<String, String> map = new HashMap<>();
 map.put("apple", "fruit");
 map.put("banana", "yellow");
 map.put("cherry", "red");
 MapJoiner mapJoiner = joiner.withKeyValueSeparator(" => ");
 String result = mapJoiner.join(map.entrySet());
 assertEquals("apple => fruit:banana => yellow:cherry => red", result);
 }

 @Test
 public void testNullMapJoinerMap() {
 Joiner joiner = Joiner.on("*");
 MapJoiner mapJoiner = joiner.withKeyValueSeparator(" => ");
 String result = mapJoiner.join((Map<?, ?>)null);
 assertNull(result);
 }

 @Test
 public void testMapJoinerNullKey() {
 Joiner joiner = Joiner.on("+");
 MapJoiner mapJoiner = joiner.withKeyValueSeparator(" => ");
 Map<String, String> map = new HashMap<>();
 map.put(null, "value");
 String result = mapJoiner.join(map.entrySet());
 assertEquals("", result);
 }

 @Test
 public void testMapJoinerNullValue() {
 Joiner joiner = Joiner.on("#");
 MapJoiner mapJoiner = joiner.withKeyValueSeparator(" => ");
 Map<String, String> map = new HashMap<>();
 map.put("key", null);
 String result = mapJoiner.join(map.entrySet());
 assertEquals("key => ", result);
 }
}