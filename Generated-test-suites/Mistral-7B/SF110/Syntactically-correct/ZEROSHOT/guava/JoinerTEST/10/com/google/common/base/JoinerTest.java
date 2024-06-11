package com.google.common.base;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;

@SuppressWarnings("unchecked")
public class JoinerTest {

 @BeforeClass
 public static void setup() {
 System.setProperty("google.base.skipTestOnNull", "false");
 }

 @AfterClass
 public static void teardown() {
 System.clearProperty("google.base.skipTestOnNull");
 }

 @Test
 public void testJoinerOnString() {
 Joiner joiner = Joiner.on(",");
 String expected = "one,two,three";
 List<String> list = Arrays.asList("one", "two", "three");
 String actual = joiner.join(list);
 assertEquals(expected, actual);
 }

 @Test
 public void testJoinerOnChar() {
 Joiner joiner = Joiner.on('-');
 String expected = "one-two-three";
 List<String> list = Arrays.asList("one", "two", "three");
 String actual = joiner.join(list);
 assertEquals(expected, actual);
 }

 @Test
 public void testAppendToIterable() throws IOException {
 StringBuilder appendable = new StringBuilder();
 Joiner joiner = Joiner.on(":");
 joiner.appendTo(appendable, Arrays.asList("one", "two", "three"));
 String expected = "one:two:three";
 String actual = appendable.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testAppendToObjectArray() throws IOException {
 StringBuilder appendable = new StringBuilder();
 Joiner joiner = Joiner.on(":");
 joiner.appendTo(appendable, new Object[]{"one", "two", "three"});
 String expected = "one:two:three";
 String actual = appendable.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testAppendToIterator() throws IOException {
 StringBuilder appendable = new StringBuilder();
 Joiner joiner = Joiner.on(":");
 Iterator<String> iterator = Arrays.asList("one", "two", "three").iterator();
 joiner.appendTo(appendable, iterator);
 String expected = "one:two:three";
 String actual = appendable.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testJoinIterable() {
 String expected = "one,two,three";
 List<String> list = Arrays.asList("one", "two", "three");
 Joiner joiner = Joiner.on(",");
 String actual = joiner.join(list);
 assertEquals(expected, actual);
 }

 @Test
 public void testJoinObjectArray() {
 String expected = "one,two,three";
 String[] array = {"one", "two", "three"};
 Joiner joiner = Joiner.on(",");
 String actual = joiner.join(array);
 assertEquals(expected, actual);
 }

 @Test
 public void testJoinObjectNull() {
 Joiner joiner = Joiner.on(",");
 String expected = "null";
 String actual = joiner.join((Object[])new Object[]{"one", null, "three"});
 assertEquals(expected, actual);
 }

 @Test
 public void testUseForNull() {
 Joiner joiner = Joiner.on(",");
 Joiner joinerNull = joiner.useForNull("NULL");
 String expected = "one,NULL,three";
 String[] array = {"one", null, "three"};
 String actual = joinerNull.join(array);
 assertEquals(expected, actual);
 }

 @Test
 public void testSkipNulls() {
 Joiner joiner = Joiner.on(",");
 Joiner joinerNull = joiner.skipNulls();
 String expected = "one,three";
 String[] array = {"one", null, "three"};
 String actual = joinerNull.join(array);
 assertEquals(expected, actual);
 }

 @Test
 public void testMapJoiner() {
 Map<String, String> map = new HashMap<>();
 map.put("one", "oneValue");
 map.put("two", "twoValue");
 map.put("three", "threeValue");
 MapJoiner mapJoiner = Joiner.on(",").withKeyValueSeparator("=");
 String expected = "one=oneValue,two=twoValue,three=threeValue";
 String actual = mapJoiner.join(map);
 assertEquals(expected, actual);
 }
}