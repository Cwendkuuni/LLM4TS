package com.google.common.base;

import com.google.common.base.Joiner;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class JoinerTest {

 private Joiner separatorJoiner;
 private Joiner commaJoiner;
 private Joiner subJoiner;

 @Before
 public void setUp() {
 separatorJoiner = Joiner.on("/");
 commaJoiner = Joiner.on(",");
 subJoiner = Joiner.on("--");
 }

 @Test
 public void testAppendToIterable() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 Joiner joiner = separatorJoiner;
 Iterable<String> list = Arrays.asList("apple", "banana", "cherry");
 joiner.appendTo(outputStream, list);
 String expected = "apple/banana/cherry";
 assertEquals(expected, outputStream.toString());
 }

 @Test
 public void testAppendToIterator() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 Joiner joiner = separatorJoiner;
 Iterator<String> iterator = Arrays.asList("apple", "banana", "cherry").iterator();
 joiner.appendTo(outputStream, iterator);
 String expected = "apple/banana/cherry";
 assertEquals(expected, outputStream.toString());
 }

 @Test
 public void testAppendToArray() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 Joiner joiner = separatorJoiner;
 Object[] array = new Object[] {"apple", "banana", "cherry"};
 joiner.appendTo(outputStream, array);
 String expected = "apple/banana/cherry";
 assertEquals(expected, outputStream.toString());
 }

 @Test
 public void testAppendToSingleObject() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 Joiner joiner = separatorJoiner;
 String str = "apple";
 joiner.appendTo(outputStream, Arrays.asList(str));
 String expected = "apple";
 assertEquals(expected, outputStream.toString());
 }

 @Test
 public void testAppendToEmptyIterable() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 Joiner joiner = separatorJoiner;
 Iterable<String> list = Collections.emptyList();
 joiner.appendTo(outputStream, list);
 String expected = "";
 assertEquals(expected, outputStream.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAppendToNullIterable() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 Joiner joiner = separatorJoiner;
 Iterable<String> list = null;
 joiner.appendTo(outputStream, list);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAppendToNullIterator() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 Joiner joiner = separatorJoiner;
 Iterator<String> iterator = null;
 joiner.appendTo(outputStream, iterator);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAppendToNullObject() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 Joiner joiner = separatorJoiner;
 Object obj = null;
 joiner.appendTo(outputStream, Arrays.asList(obj));
 }

 @Test
 public void testAppendToUseForNull() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 Joiner joiner = separatorJoiner.useForNull("NULL");
 Object[] array = new Object[] {"apple", null, "cherry"};
 joiner.appendTo(outputStream, array);
 String expected = "apple/NULL/cherry";
 assertEquals(expected, outputStream.toString());
 }

 @Test
 public void testAppendToSkipNulls() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 Joiner joiner = separatorJoiner.skipNulls();
 Object[] array = new Object[] {"apple", null, "cherry"};
 joiner.appendTo(outputStream, array);
 String expected = "apple/cherry";
 assertEquals(expected, outputStream.toString());
 }

 @Test
 public void testJoinIterable() {
 String joined = separatorJoiner.join(Arrays.asList("apple", "banana", "cherry"));
 String expected = "apple/banana/cherry";
 assertEquals(expected, joined);
 }

 @Test
 public void testJoinIterator() {
 String joined = separatorJoiner.join(Arrays.asList("apple", "banana", "cherry").iterator());
 String expected = "apple/banana/cherry";
 assertEquals(expected, joined);
 }

 @Test
 public void testJoinArray() {
 String joined = separatorJoiner.join(new Object[] {"apple", "banana", "cherry"});
 String expected = "apple/banana/cherry";
 assertEquals(expected, joined);
 }

 @Test
 public void testJoinSingleObject() {
 String joined = separatorJoiner.join("apple");
 String expected = "apple";
 assertEquals(expected, joined);
 }

 @Test
 public void testJoinEmptyIterable() {
 String joined = separatorJoiner.join(Collections.emptyList());
 String expected = "";
 assertEquals(expected, joined);
 }

 @Test
 public void testJoinNullIterator() {
 String joined = separatorJoiner.join(null);
 String expected = "";
 assertEquals(expected, joined);
 }

 @Test
 public void testJoinNullObject() {
 String joined = separatorJoiner.join(null);
 String expected = "";
 assertEquals(expected, joined);
 }

 @Test
 public void testJoinUseForNull() {
 String joined = separatorJoiner.useForNull(null).join(Arrays.asList("apple", null, "cherry"));
 String expected = "apple/NULL/cherry";
 assertEquals(expected, joined);
 }

 @Test
 public void testJoinSkipNulls() {
 String joined = separatorJoiner.skipNulls().join(Arrays.asList("apple", null, "cherry"));
 String expected = "apple/cherry";
 assertEquals(expected, joined);
 }

 @Test
 public void testJoinWithKeyValueSeparator() {
 Map<String, String> map = new HashMap<>();
 map.put("key1", "value1");
 map.put("key2", "value2");
 MapJoiner mapJoiner = separatorJoiner.withKeyValueSeparator(":");
 String joined = mapJoiner.join(map);
 String expected = "key1:value1/key2:value2";
 assertEquals(expected, joined);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJoinWithNullMap() {
 MapJoiner mapJoiner = separatorJoiner.withKeyValueSeparator(":");
 String joined = mapJoiner.join(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJoinWithNullMapEntry() {
 Map<String, String> map = new HashMap<>();
 map.put("key1", "value1");
 map.put(null, "value2");
 MapJoiner mapJoiner = separatorJoiner.withKeyValueSeparator(":");
 String joined = mapJoiner.join(map);
 }

 @Test
 public void testJoinWithEmptyMap() {
 MapJoiner mapJoiner = separatorJoiner.withKeyValueSeparator(":");
 String joined = mapJoiner.join(Collections.emptyMap());
 String expected = "";
 assertEquals(expected, joined);
 }

 @Test
 public void testAppendToMapJoiner() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 MapJoiner mapJoiner = separatorJoiner.withKeyValueSeparator(":");
 Map<String, String> map = new HashMap<>();
 map.put("key1", "value1");
 map.put("key2", "value2");
 mapJoiner.appendTo(outputStream, map);
 String expected = "key1:value1/key2:value2";
 assertEquals(expected, outputStream.toString());
 }

 @Test
 public void testJoinMapJoiner() {
 String joined = separatorJoiner.withKeyValueSeparator(":").join(Collections.singletonMap("key", "value"));
 String expected = "key:value";
 assertEquals(expected, joined);
 }
}