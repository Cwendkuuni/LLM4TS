package com.google.common.base;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import org.junit.Test;
import static org.junit.Assert.*;

public class JoinerTest {

 @Test
 public void testStringJoiner() {
 Joiner joiner = Joiner.on(",");
 String result = joiner.join(new String[]{"apple", "banana", "cherry"});
 assertEquals("apple, banana, cherry", result);
 }

 @Test
 public void testCharJoiner() {
 Joiner joiner = Joiner.on('/');
 String result = joiner.join(new String[]{"apple", "banana", "cherry"});
 assertEquals("apple/banana/cherry", result);
 }

 @Test
 public void testAppendToIterable() throws IOException {
 StringBuilder sb = new StringBuilder();
 Joiner joiner = Joiner.on(":");
 joiner.appendTo(sb, Arrays.asList("apple", "banana", "cherry"));
 assertEquals("apple:banana:cherry", sb.toString());
 }

 @Test
 public void testAppendToObjectArray() throws IOException {
 StringBuilder sb = new StringBuilder();
 Joiner joiner = Joiner.on(":");
 joiner.appendTo(sb, new Object[]{"apple", "banana", "cherry"});
 assertEquals("apple:banana:cherry", sb.toString());
 }

 @Test
 public void testAppendToIterator() throws IOException {
 StringBuilder sb = new StringBuilder();
 Joiner joiner = Joiner.on(":");
 Iterator<String> it = Arrays.asList("apple", "banana", "cherry").iterator();
 joiner.appendTo(sb, it);
 assertEquals("apple:banana:cherry", sb.toString());
 }

 @Test
 public void testAppendToMap() throws IOException {
 Map<String, String> map = ImmutableMap.of("key1", "value1", "key2", "value2");
 StringBuilder sb = new StringBuilder();
 Joiner joiner = Joiner.on(":");
 joiner.appendTo(sb, map.entrySet());
 assertEquals("key1:value1:key2:value2", sb.toString());
 }

 @Test
 public void testUseForNull() {
 Joiner joiner = Joiner.on(",").useForNull("N/A");
 String result = joiner.join(new String[]{"apple", null, "cherry"});
 assertEquals("apple, N/A, cherry", result);
 }

 @Test
 public void testSkipNulls() {
 Joiner joiner = Joiner.on(",").skipNulls();
 String result = joiner.join(new String[]{"apple", null, "cherry"});
 assertEquals("apple, cherry", result);
 }
}