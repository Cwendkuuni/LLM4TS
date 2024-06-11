package com.google.common.base;

import com.google.common.base.Joiner;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JoinerTest {

 private Joiner separatorJoiner;
 private Joiner commaJoiner;
 private Joiner tabJoiner;

 @Before
 public void setup() {
 separatorJoiner = Joiner.on("|");
 commaJoiner = Joiner.on(",");
 tabJoiner = Joiner.on("\t");
 }

 @Test
 public void testAppendToIterable() throws IOException {
 Iterable<String> simpleList = Arrays.asList("apple", "banana", "cherry");
 StringBuilder expected = new StringBuilder();
 expected.append("apple|banana|cherry");
 assertEquals(expected.toString(), separatorJoiner.appendTo(new StringBuilder(), simpleList).toString());
 }

 @Test
 public void testAppendToIterator() throws IOException {
 Iterator<String> iterator = Arrays.asList("apple", "banana", "cherry").iterator();
 StringBuilder expected = new StringBuilder();
 expected.append("apple|banana|cherry");
 assertEquals(expected.toString(), separatorJoiner.appendTo(new StringBuilder(), iterator).toString());
 }

 @Test
 public void testAppendToArray() throws IOException {
 String[] simpleArray = {"apple", "banana", "cherry"};
 StringBuilder expected = new StringBuilder();
 expected.append("apple|banana|cherry");
 assertEquals(expected.toString(), separatorJoiner.appendTo(new StringBuilder(), simpleArray).toString());
 }

 @Test
 public void testAppendToIterableWithNulls() throws IOException {
 Iterable<String> simpleList = Arrays.asList("apple", null, "cherry");
 StringBuilder expected = new StringBuilder();
 expected.append("apple|null|cherry");
 Joiner joiner = separatorJoiner.useForNull("null");
 assertEquals(expected.toString(), joiner.appendTo(new StringBuilder(), simpleList).toString());
 }

 @Test
 public void testAppendToIteratorWithNulls() throws IOException {
 Iterator<String> iterator = Arrays.asList("apple", null, "cherry").iterator();
 StringBuilder expected = new StringBuilder();
 expected.append("apple|null|cherry");
 Joiner joiner = separatorJoiner.useForNull("null");
 assertEquals(expected.toString(), joiner.appendTo(new StringBuilder(), iterator).toString());
 }

 @Test
 public void testAppendToArrayWithNulls() throws IOException {
 String[] simpleArray = {"apple", null, "cherry"};
 StringBuilder expected = new StringBuilder();
 expected.append("apple|null|cherry");
 Joiner joiner = separatorJoiner.useForNull("null");
 assertEquals(expected.toString(), joiner.appendTo(new StringBuilder(), simpleArray).toString());
 }

 @Test
 public void testAppendToIterableWithSkippedNulls() throws IOException {
 Iterable<String> simpleList = Arrays.asList(null, "apple", null, "cherry");
 StringBuilder expected = new StringBuilder();
 expected.append("apple|cherry");
 Joiner joiner = separatorJoiner.skipNulls();
 assertEquals(expected.toString(), joiner.appendTo(new StringBuilder(), simpleList).toString());
 }

 @Test
 public void testAppendToIteratorWithSkippedNulls() throws IOException {
 Iterator<String> iterator = Arrays.asList(null, "apple", null, "cherry").iterator();
 StringBuilder expected = new StringBuilder();
 expected.append("apple|cherry");
 Joiner joiner = separatorJoiner.skipNulls();
 assertEquals(expected.toString(), joiner.appendTo(new StringBuilder(), iterator).toString());
 }

 @Test
 public void testAppendToArrayWithSkippedNulls() throws IOException {
 String[] simpleArray = {null, "apple", null, "cherry"};
 StringBuilder expected = new StringBuilder();
 expected.append("apple|cherry");
 Joiner joiner = separatorJoiner.skipNulls();
 assertEquals(expected.toString(), joiner.appendTo(new StringBuilder(), simpleArray).toString());
 }

 @Test
 public void testJoinIterable() {
 Iterable<String> simpleList = Arrays.asList("apple", "banana", "cherry");
 assertEquals("apple|banana|cherry", separatorJoiner.join(simpleList));
 }

 @Test
 public void testJoinIterator() {
 Iterator<String> iterator = Arrays.asList("apple", "banana", "cherry").iterator();
 assertEquals("apple|banana|cherry", separatorJoiner.join(iterator));
 }

 @Test
 public void testJoinArray() {
 String[] simpleArray = {"apple", "banana", "cherry"};
 assertEquals("apple|banana|cherry", separatorJoiner.join(simpleArray));
 }

 @Test
 public void testJoinIterableWithNulls() {
 Iterable<String> simpleList = Arrays.asList("apple", null, "cherry");
 assertEquals("apple|null|cherry", separatorJoiner.useForNull(null).join(simpleList));
 }

 @Test
 public void testJoinIteratorWithNulls() {
 Iterator<String> iterator = Arrays.asList("apple", null, "cherry").iterator();
 assertEquals("apple|null|cherry", separatorJoiner.useForNull(null).join(iterator));
 }

 @Test
 public void testJoinArrayWithNulls() {
 String[] simpleArray = {"apple", null, "cherry"};
 assertEquals("apple|null|cherry", separatorJoiner.useForNull(null).join(simpleArray));
 }

 @Test
 public void testJoinIterableWithSkippedNulls() {
 Iterable<String> simpleList = Arrays.asList(null, "apple", null, "cherry");
 assertEquals("apple|cherry", separatorJoiner.skipNulls().join(simpleList));
 }

 @Test
 public void testJoinIteratorWithSkippedNulls() {
 Iterator<String> iterator = Arrays.asList(null, "apple", null, "cherry").iterator();
 assertEquals("apple|cherry", separatorJoiner.skipNulls().join(iterator));
 }

 @Test
 public void testJoinArrayWithSkippedNulls() {
 String[] simpleArray = {null, "apple", null, "cherry"};
 assertEquals("apple|cherry", separatorJoiner.skipNulls().join(simpleArray));
 }

 @Test
 public void testJoinMapIterableEntrySet() {
 Map<String, String> map = new HashMap<>();
 map.put("key1", "value1");
 map.put("key2", "value2");
 map.put("key3", "value3");
 Iterable<Map.Entry<String, String>> entrySet = map.entrySet();
 assertEquals("key1=value1|key2=value2|key3=value3", separatorJoiner.withKeyValueSeparator("=").join(entrySet));
 }

 @Test
 public void testJoinMapIteratorEntrySet() {
 Map<String, String> map = new HashMap<>();
 map.put("key1", "value1");
 map.put("key2", "value2");
 map.put("key3", "value3");
 Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
 assertEquals("key1=value1|key2=value2|key3=value3", separatorJoiner.withKeyValueSeparator("=").join(iterator));
 }

 @Test
 public void testJoinMapArrayEntrySet() {
 Map<String, String> map = new HashMap<>();
 map.put("key1", "value1");
 map.put("key2", "value2");
 map.put("key3", "value3");
 Map.Entry<String, String>[] entrySet = map.entrySet().toArray(new Map.Entry[map.size()]);
 assertEquals("key1=value1|key2=value2|key3=value3", separatorJoiner.withKeyValueSeparator("=").join(Arrays.asList(entrySet)));
 }

 @Test
 public void testJoinMapWithNullValues() {
 Map<String, String> map = new HashMap<>();
 map.put("key1", "value1");
 map.put("key2", null);
 map.put("key3", "value3");
 Iterable<Map.Entry<String, String>> entrySet = map.entrySet();
 Joiner joiner = separatorJoiner.useForNull("null");
 assertEquals("key1=value1|key2=null|key3=value3", joiner.withKeyValueSeparator("=").join(entrySet));
 }

 @Test
 public void testJoinMapWithSkippedNullValues() {
 Map<String, String> map = new HashMap<>();
 map.put("key1", "value1");
 map.put("key2", null);
 map.put("key3", "value3");
 Iterable<Map.Entry<String, String>> entrySet = map.entrySet();
 Joiner joiner = separatorJoiner.skipNulls();
 assertEquals("key1=value1|key3=value3", joiner.withKeyValueSeparator("=").join(entrySet));
 }

}