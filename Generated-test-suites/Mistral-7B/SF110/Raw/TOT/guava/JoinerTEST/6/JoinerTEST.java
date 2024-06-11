package com.google.common.base;

import com.google.common.base.Joiner;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class JoinerTest {

 @Test
 public void testStringJoiner() {
 Joiner joiner = Joiner.on("|");
 String expected = "apple|banana|orange";
 String result = joiner.join(Arrays.asList("apple", "banana", "orange"));
 assertEquals(expected, result);
 }

 @Test
 public void testCharJoiner() {
 Joiner joiner = Joiner.on('-');
 String expected = "apple-banana-orange";
 String result = joiner.join(Arrays.asList("apple", "banana", "orange"));
 assertEquals(expected, result);
 }

 @Test
 public void testEmptyIterable() {
 Joiner joiner = Joiner.on(":");
 String expected = "";
 String result = joiner.join(Collections.emptyList());
 assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullSeparator() {
 Joiner joiner = Joiner.on((String)null);
 }

 @Test
 public void testAppendTo() throws IOException {
 StringBuilder stringBuilder = new StringBuilder();
 Joiner joiner = Joiner.on(",");
 joiner.appendTo(stringBuilder, Arrays.asList("apple", "banana", "orange"));
 String expected = "apple,banana,orange";
 String result = stringBuilder.toString();
 assertEquals(expected, result);
 }

 @Test
 public void testAppendToNulls() throws IOException {
 StringBuilder stringBuilder = new StringBuilder();
 Joiner joiner = Joiner.on(",");
 joiner.appendTo(stringBuilder, Arrays.asList("apple", null, "orange"));
 String expected = "apple,null,orange";
 String result = stringBuilder.toString();
 assertEquals(expected, result);
 }

 @Test
 public void testUseForNull() {
 Joiner joiner = Joiner.on(",").useForNull("NA");
 String expected = "apple,NA,orange";
 String result = joiner.join(Arrays.asList("apple", null, "orange"));
 assertEquals(expected, result);
 }

 @Test
 public void testSkipNulls() {
 Joiner joiner = Joiner.on(",").skipNulls();
 String expected = "apple,orange";
 String result = joiner.join(Arrays.asList("apple", null, "orange"));
 assertEquals(expected, result);
 }

 @Test
 public void testMapJoiner() {
 Map<String, String> map = new HashMap<>();
 map.put("apple", "fruit");
 map.put("banana", "yellow");
 map.put("orange", "citrus");
 Joiner mapJoiner = Joiner.on(":").withKeyValueSeparator("=");
 String expected = "apple=fruit:banana=yellow:orange=citrus";
 String result = mapJoiner.join(map);
 assertEquals(expected, result);
 }
}