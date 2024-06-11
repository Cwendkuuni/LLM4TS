package com.google.common.base;

import com.google.common.base.Joiner;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

public class JoinerTest {

 @Test
 public void testJoinIterableStringSeparator() {
 Joiner joiner = Joiner.on(",");
 String result = joiner.join(Arrays.asList("apple", "banana", "cherry"));
 assertEquals("apple,banana,cherry", result);
 }

 @Test
 public void testJoinIterableCharSeparator() {
 Joiner joiner = Joiner.on('-');
 String result = joiner.join(Arrays.asList("apple", "banana", "cherry"));
 assertEquals("apple-banana-cherry", result);
 }

 @Test
 public void testJoinStringArray() {
 String[] array = {"apple", "banana", "cherry"};
 Joiner joiner = Joiner.on(",");
 String result = joiner.join(array);
 assertEquals("apple,banana,cherry", result);
 }

 @Test
 public void testJoinNullElements() {
 String[] array = {"apple", null, "cherry"};
 Joiner joiner = Joiner.on(",");
 String result = joiner.join(array);
 assertEquals("apple,null,cherry", result);
 }

 @Test
 public void testJoinIterableEmpty() {
 Joiner joiner = Joiner.on(":");
 String result = joiner.join(Collections.emptyList());
 assertEquals("", result);
 }

 @Test
 public void testJoinNullIterable() {
 Joiner joiner = Joiner.on(":");
 String result = joiner.join(null);
 assertNull(result);
 }

 @Test(expected = IOException.class)
 public void testThrowIOException() throws IOException {
 Joiner joiner = new Joiner("") {
 @Override
 public <A extends Appendable> A appendTo(final A appendable, final Iterable<?> parts) throws IOException {
 throw new IOException();
 }
 };
 String result = joiner.join(Arrays.asList("apple", "banana", "cherry"));
 fail("IOException should be thrown");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testUseForNullAndSkipNulls() {
 Joiner joiner = Joiner.on(",");
 joiner = joiner.useForNull("NA");
 joiner = joiner.skipNulls();
 String result = joiner.join(new String[]{"apple", null, "banana", "cherry"});
 assertEquals("apple,NA,banana,cherry", result);
 }

 @Test
 public void testMapJoiner() {
 Map<String, Integer> map = new HashMap<>();
 map.put("apple", 1);
 map.put("banana", 2);
 map.put("cherry", 3);
 Joiner joiner = Joiner.on(",");
 MapJoiner mapJoiner = joiner.withKeyValueSeparator(":");
 String result = mapJoiner.join(map);
 assertEquals("apple:1,banana:2,cherry:3", result);
 }

}