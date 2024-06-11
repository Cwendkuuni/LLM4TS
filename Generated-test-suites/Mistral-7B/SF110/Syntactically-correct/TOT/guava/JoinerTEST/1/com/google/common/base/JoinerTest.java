package com.google.common.base;

import com.google.common.base.Joiner;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JoinerTest {

 private Joiner joiner;
 private Iterable<String> iterable;

 @Before
 public void setup() {
 joiner = Joiner.on(",");
 }

 @Test
 public void testAppendToIterable() throws IOException {
 StringBuilder expected = new StringBuilder();
 expected.append("apple").append(joiner.separator).append("banana")
 .append(joiner.separator).append("orange");
 Iterable<String> parts = Arrays.asList("apple", "banana", "orange");
 StringBuilder actual = new StringBuilder();
 joiner.appendTo(actual, parts);
 assertEquals(expected.toString(), actual.toString());
 }

 @Test(expected = NullPointerException.class)
 public void testAppendToNullIterator() throws IOException {
 joiner.appendTo(new StringBuilder(), null);
 }

 @Test
 public void testAppendToObjectArray() throws IOException {
 Object[] parts = {"apple", "banana", "orange"};
 StringBuilder actual = new StringBuilder();
 joiner.appendTo(actual, parts);
 StringBuilder expected = new StringBuilder();
 expected.append("apple").append(joiner.separator).append("banana")
 .append(joiner.separator).append("orange");
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testJoinIterable() {
 String expected = "apple,banana,orange";
 Iterable<String> parts = Arrays.asList("apple", "banana", "orange");
 String actual = joiner.join(parts);
 assertEquals(expected, actual);
 }

 @Test
 public void testJoinObjectArray() {
 Object[] parts = {"apple", "banana", "orange"};
 String actual = joiner.join(parts);
 String expected = "apple,banana,orange";
 assertEquals(expected, actual);
 }

 @Test
 public void testJoinNullObjects() {
 String expected = "null,null,null";
 String first = null;
 String second = null;
 Object[] parts = {first, second};
 String actual = joiner.join(parts);
 assertEquals(expected, actual);
 }

 @Test
 public void testUseForNull() {
 joiner = joiner.useForNull("NA");
 String expected = "apple,NA,banana,NA,orange";
 String first = "apple";
 Object second = null;
 Object[] parts = {first, second, "banana", null, "orange"};
 String actual = joiner.join(parts);
 assertEquals(expected, actual);
 }

 @Test
 public void testSkipNulls() {
 joiner = joiner.skipNulls();
 String expected = "apple,banana,orange";
 String first = "apple";
 Object second = null;
 Object[] parts = {first, second, "banana", null, "orange"};
 String actual = joiner.join(parts);
 assertEquals(expected, actual);
 }

 @Test
 public void testMapJoiner() throws IOException {
 Joiner mapJoiner = Joiner.on(" => ");
 Map<String, String> map = new HashMap<>();
 map.put("apple", "fruit");
 map.put("banana", "fruit");
 map.put("orange", "citrus");
 StringBuilder expected = new StringBuilder();
 expected.append("apple => fruit").append(mapJoiner.separator)
 .append("banana => fruit").append(mapJoiner.separator)
 .append("orange => citrus");
 MapJoiner mapJoinerInstance = mapJoiner.withKeyValueSeparator(mapJoiner.separator);
 StringBuilder actual = new StringBuilder();
 mapJoinerInstance.appendTo(actual, map.entrySet());
 assertEquals(expected.toString(), actual.toString());
 }
}