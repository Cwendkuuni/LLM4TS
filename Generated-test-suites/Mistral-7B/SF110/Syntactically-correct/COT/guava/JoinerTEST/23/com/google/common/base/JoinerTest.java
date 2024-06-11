package com.example.junit;

import static org.junit.Assert.*;

import java.util.*;

import com.google.common.base.*;

@SuppressWarnings("unchecked")
public class JoinerTest {

 private Joiner joiner;
 private StringBuilder stringBuilder;
 private String expectedJoin;

 @Before
 public void setUp() {
 joiner = Joiner.on(":");
 }

 @Test
 public void testAppendToIterable() throws IOException {
 stringBuilder = new StringBuilder();
 Iterable<String> iterable = Arrays.asList("test1", "test2", "test3");
 joiner.appendTo(stringBuilder, iterable);
 expectedJoin = "test1:test2:test3";
 assertEquals(expectedJoin, stringBuilder.toString());
 }

 @Test
 public void testAppendToObjectArray() throws IOException {
 stringBuilder = new StringBuilder();
 Object[] parts = new Object[]{"test1", "test2", "test3"};
 joiner.appendTo(stringBuilder, parts);
 expectedJoin = "test1:test2:test3";
 assertEquals(expectedJoin, stringBuilder.toString());
 }

 @Test
 public void testAppendToIterableWithFirstAndSecond() throws IOException {
 stringBuilder = new StringBuilder();
 Iterator<String> iterator = Arrays.asList("test1", "test2", "test3").iterator();
 if (iterator.hasNext()) {
 String first = iterator.next();
 String second = iterator.next();
 joiner.appendTo(stringBuilder, first, second, "test3");
 }
 expectedJoin = "test1:test2:test3";
 assertEquals(expectedJoin, stringBuilder.toString());
 }

 @Test
 public void testAppendToIteratorWithFirstAndSecond() throws IOException {
 stringBuilder = new StringBuilder();
 Iterator<String> iterator = Arrays.asList("test1", "test2", "test3").iterator();
 if (iterator.hasNext()) {
 String first = iterator.next();
 String second = iterator.next();
 joiner.appendTo(stringBuilder, first, second, "test3");
 }
 expectedJoin = "test1:test2:test3";
 assertEquals(expectedJoin, stringBuilder.toString());
 }

 @Test
 public void testJoinIterable() {
 expectedJoin = "test1:test2:test3";
 assertEquals(expectedJoin, joiner.join(Arrays.asList("test1", "test2", "test3")));
 }

 @Test
 public void testJoinObjectArray() {
 expectedJoin = "test1:test2:test3";
 assertEquals(expectedJoin, joiner.join(new Object[]{"test1", "test2", "test3"}));
 }

 @Test
 public void testJoinWithFirstAndSecond() {
 expectedJoin = "test1:test2:test3";
 assertEquals(expectedJoin, joiner.join("test1", "test2", "test3"));
 }

 @Test
 public void testUseForNull() {
 joiner = joiner.useForNull("NULL");
 expectedJoin = "NULL:test1:test2:test3";
 Iterable<String> iterable = Arrays.asList("test1", "test2", "test3");
 StringBuilder stringBuilder = new StringBuilder();
 joiner.appendTo(stringBuilder, iterable);
 assertEquals(expectedJoin, stringBuilder.toString());
 }

 @Test
 public void testSkipNulls() throws IOException {
 joiner = joiner.skipNulls();
 stringBuilder = new StringBuilder();
 Iterator<Object> iterator = Arrays.asList(null, "test1", null, "test2", null).iterator();
 joiner.appendTo(stringBuilder, iterator);
 expectedJoin = "test1:test2";
 assertEquals(expectedJoin, stringBuilder.toString());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testUseForNullAndSkipNulls() {
 joiner = joiner.useForNull("NULL");
 joiner = joiner.skipNulls();
 joiner.appendTo(new StringBuilder(), new Object[]{"test1", null, "test2", null, "test3"});
 fail("Use for null and skip nulls should not be supported");
 }

 @Test
 public void testWithKeyValueSeparator() {
 Map<String, String> map = new HashMap<>();
 map.put("key1", "value1");
 map.put("key2", "value2");
 MapJoiner mapJoiner = joiner.withKeyValueSeparator(":");
 expectedJoin = "key1:value1:key2:value2";
 assertEquals(expectedJoin, mapJoiner.join(map));
 }

 @Test
 public void testAppendToMap() throws IOException {
 stringBuilder = new StringBuilder();
 Map<String, String> map = new HashMap<>();
 map.put("key1", "value1");
 map.put("key2", "value2");
 mapJoiner.appendTo(stringBuilder, map);
 expectedJoin = "key1:value1:key2:value2";
 assertEquals(expectedJoin, stringBuilder.toString());
 }

 @Test
 public void testJoinMap() {
 expectedJoin = "key1:value1:key2:value2";
 assertEquals(expectedJoin, mapJoiner.join(map));
 }

 @Test
 public void testJoinMapEntrySet() {
 Map<String, String> map = new HashMap<>();
 map.put("key1", "value1");
 map.put("key2", "value2");
 MapJoiner mapJoiner = joiner.withKeyValueSeparator(":");
 expectedJoin = "key1:value1:key2:value2";
 assertEquals(expectedJoin, mapJoiner.join(map.entrySet()));
 }

 @Test
 public void testAppendToMapEntryIterator() throws IOException {
 stringBuilder = new StringBuilder();
 Map<String, String> map = new HashMap<>();
 map.put("key1", "value1");
 map.put("key2", "value2");
 MapJoiner mapJoiner = joiner.withKeyValueSeparator(":");
 Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
 mapJoiner.appendTo(stringBuilder, iterator);
 expectedJoin = "key1:value1:key2:value2";
 assertEquals(expectedJoin, stringBuilder.toString());
 }

 @Test
 public void testJoinMapEntryIteratorThrowsException() throws IOException {
 stringBuilder = new StringBuilder();
 Map<String, String> map = new HashMap<>();
 map.put("key1", "value1");
 map.put("key2", "value2");
 MapJoiner mapJoiner = joiner.withKeyValueSeparator(":");
 Map<String, String> mapCopy = new HashMap<>(map);
 Iterator<Map.Entry<String, String>> iterator = mapCopy.entrySet().iterator();
 mapJoiner.appendTo(stringBuilder, iterator);
 fail("appendTo method should throw an IOException when called with an Iterator");
 }

 @Test
 public void testJoinMapEntrySet() {
 expectedJoin = "key1:value1:key2:value2";
 assertEquals(expectedJoin, mapJoiner.join(map.entrySet()));
 }

}