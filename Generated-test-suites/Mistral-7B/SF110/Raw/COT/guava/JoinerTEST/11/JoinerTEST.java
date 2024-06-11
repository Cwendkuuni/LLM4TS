package com.example.junit; // replace with your test package name

import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;
import com.google.common.base.*;

@CheckReturnValue
public class JoinerTest {

 private Joiner joiner;
 private Joiner joinerWithNullText;
 private Joiner joinerWithSkipNulls;
 private MapJoiner mapJoiner;
 private Iterator<Object> iterator;
 private Iterable<Object> iterable;
 private Map<String, String> map;

 @Before
 public void setup() {
 joiner = Joiner.on(":");
 joinerWithNullText = joiner.useForNull("NULL");
 joinerWithSkipNulls = joiner.skipNulls();
 mapJoiner = joiner.withKeyValueSeparator("=>");
 map = new HashMap<>();
 map.put("key1", "value1");
 map.put("key2", null);
 map.put("key3", "value3");
 iterator = map.entrySet().iterator();
 iterable = map.entrySet();
 }

 @Test
 public void testOn() {
 Joiner joiner = Joiner.on(":");
 assertEquals(":", joiner.separator);
 }

 @Test
 public void testOnChar() {
 Joiner joiner = Joiner.on(':');
 assertEquals(":", joiner.separator);
 }

 @Test
 public void testAppendToIterable() throws IOException {
 StringBuilder expected = new StringBuilder();
 expected.append("part1:");
 expected.append(joiner.separator);
 expected.append("part2:");
 expected.append(joiner.separator);
 expected.append("part3:");
 StringBuilder actual = new StringBuilder();
 joiner.appendTo(actual, Arrays.asList("part1", "part2", "part3"));
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testAppendToIterator() throws IOException {
 StringBuilder expected = new StringBuilder();
 expected.append("part1:");
 expected.append(joiner.separator);
 expected.append("part2:");
 expected.append(joiner.separator);
 expected.append("part3:");
 StringBuilder actual = new StringBuilder();
 joiner.appendTo(actual, Arrays.asList("part1", "part2", "part3").iterator());
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testAppendToObjectArray() throws IOException {
 StringBuilder expected = new StringBuilder();
 expected.append("part1:");
 expected.append(joiner.separator);
 expected.append("part2:");
 expected.append(joiner.separator);
 expected.append("part3:");
 StringBuilder actual = new StringBuilder();
 joiner.appendTo(actual, new Object[]{"part1", "part2", "part3"});
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testAppendToObjectArrayWithNull() throws IOException {
 StringBuilder expected = new StringBuilder();
 expected.append("NULL:");
 expected.append(joiner.separator);
 expected.append("part2:");
 expected.append(joiner.separator);
 expected.append("part3:");
 StringBuilder actual = new StringBuilder();
 joinerWithNullText.appendTo(actual, new Object[]{"null", "part2", "part3"});
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testAppendToObjectArrayWithNulls() throws IOException {
 StringBuilder expected = new StringBuilder();
 expected.append("NULL:");
 expected.append(joiner.separator);
 expected.append("NULL:");
 expected.append(joiner.separator);
 expected.append("NULL:");
 StringBuilder actual = new StringBuilder();
 joinerWithSkipNulls.appendTo(actual, new Object[]{"null", null, null});
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testAppendToStringBuilder() {
 StringBuilder expected = new StringBuilder();
 expected.append("part1:");
 expected.append(joiner.separator);
 expected.append("part2:");
 StringBuilder actual = new StringBuilder();
 joiner.appendTo(actual, Arrays.asList("part1", "part2"));
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testAppendToStringBuilderWithNull() {
 StringBuilder expected = new StringBuilder();
 expected.append("NULL:");
 expected.append(joiner.separator);
 expected.append("part2:");
 StringBuilder actual = new StringBuilder();
 joinerWithNullText.appendTo(actual, Arrays.asList("null", "part2"));
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testAppendToStringBuilderWithNulls() {
 StringBuilder expected = new StringBuilder();
 expected.append("NULL:");
 expected.append(joiner.separator);
 expected.append("NULL:");
 expected.append(joiner.separator);
 expected.append("NULL:");
 StringBuilder actual = new StringBuilder();
 joinerWithSkipNulls.appendTo(actual, Arrays.asList("null", null, null));
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testJoinIterable() {
 String expected = "part1:" + joiner.separator + "part2:" + joiner.separator + "part3";
 String actual = joiner.join(Arrays.asList("part1", "part2", "part3"));
 assertEquals(expected, actual);
 }

 @Test
 public void testJoinIterator() {
 String expected = "part1:" + joiner.separator + "part2:" + joiner.separator + "part3";
 String actual = joiner.join(Arrays.asList("part1", "part2", "part3").iterator());
 assertEquals(expected, actual);
 }

 @Test
 public void testJoinObjectArray() {
 String expected = "part1:" + joiner.separator + "part2:" + joiner.separator + "part3";
 String actual = joiner.join(new Object[]{"part1", "part2", "part3"});
 assertEquals(expected, actual);
 }

 @Test
 public void testJoinObjectArrayWithNull() {
 String expected = "NULL:" + joiner.separator + "part2:" + joiner.separator + "part3";
 String actual = joinerWithNullText.join(new Object[]{"null", "part2", "part3"});
 assertEquals(expected, actual);
 }

 @Test
 public void testJoinObjectArrayWithNulls() {
 String expected = "NULL:" + joiner.separator + "NULL:" + joiner.separator + "NULL:";
 String actual = joinerWithSkipNulls.join(new Object[]{"null", null, null});
 assertEquals(expected, actual);
 }

 @Test
 public void testJoinMap() {
 String expected = "key1=>value1:" + joiner.separator + "key2=>NULL:" + joiner.separator + "key3=>value3";
 String actual = mapJoiner.join(map);
 assertEquals(expected, actual);
 }

 @Test
 public void testJoinMapEntrySet() {
 String expected = "key1=>value1:" + joiner.separator + "key2=>NULL:" + joiner.separator + "key3=>value3";
 String actual = mapJoiner.join(iterable);
 assertEquals(expected, actual);
 }

 @Test(expected = AssertionError.class)
 public void testAppendToStringBuilderWithIOException() {
 StringBuilder actual = new StringBuilder();
 joiner.appendTo(actual, Arrays.asList("part1", "part2").iterator());
 fail(); // the appendTo method should throw IOException
 }

 @Test
 public void testUseForNull() {
 Joiner joiner = Joiner.on(":");
 assertEquals(":", joiner.separator);
 Joiner joinerWithNullText = joiner.useForNull("NULL");
 assertEquals("NULL", joinerWithNullText.toString(null));
 }

 @Test
 public void testSkipNulls() {
 Joiner joiner = Joiner.on(":");
 assertEquals(":", joiner.separator);
 Joiner joinerWithSkipNulls = joiner.skipNulls();
 assertEquals("part1:", joinerWithSkipNulls.join(new Object[]{"part1", null, "part3"}));
 }

 @Test
 public void testWithKeyValueSeparator() {
 MapJoiner mapJoiner = joiner.withKeyValueSeparator("=>");
 assertNotNull(mapJoiner);
 assertEquals("=>", mapJoiner.keyValueSeparator);
 }
}