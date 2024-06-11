package com.google.common.base;

import com.google.common.base.Joiner;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class JoinerTest {

 private Joiner stringJoiner;
 private Joiner charJoiner;

 @Before
 public void setUp() {
 stringJoiner = Joiner.on(",");
 charJoiner = Joiner.on(',');
 }

 @Test
 public void testAppendToIterableString() throws IOException {
 String expected = "apple,banana,cherry";
 StringBuilder actual = new StringBuilder();
 stringJoiner.appendTo(actual, Arrays.asList("apple", "banana", "cherry"));
 assertEquals(expected, actual.toString());
 }

 @Test
 public void testAppendToIterableChar() throws IOException {
 StringBuilder actual = new StringBuilder();
 charJoiner.appendTo(actual, Arrays.asList('a', 'b', 'c').iterator());
 assertEquals("abc", actual.toString());
 }

 @Test
 public void testAppendToObjectArrayString() throws IOException {
 String[] parts = {"apple", "banana", "cherry"};
 StringBuilder actual = new StringBuilder();
 stringJoiner.appendTo(actual, parts);
 assertEquals("apple,banana,cherry", actual.toString());
 }

 @Test
 public void testAppendToObjectArrayChar() throws IOException {
 char[] parts = {'a', 'b', 'c'};
 StringBuilder actual = new StringBuilder();
 charJoiner.appendTo(actual, parts);
 assertEquals("abc", actual.toString());
 }

 @Test
 public void testAppendToIterableNull() throws IOException {
 String expected = "null,null,null";
 StringBuilder actual = new StringBuilder();
 stringJoiner.appendTo(actual, Arrays.asList((Object)null, (Object)null, (Object)null));
 assertEquals(expected, actual.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAppendToNullIterable() {
 Joiner joiner = Joiner.on(" ");
 String expectedErrorMessage = "parts cannot be null or empty";
 try {
 joiner.appendTo(new StringBuilder(), null);
 } catch (IllegalArgumentException e) {
 assertEquals(expectedErrorMessage, e.getMessage());
 }
 }

 @Test(expected = IOException.class)
 public void testAppendToEmptyIterable() throws IOException {
 Joiner joiner = Joiner.on(" ");
 StringBuilder actual = new StringBuilder();
 try {
 joiner.appendTo(actual, Collections.emptyIterator());
 fail();
 } catch (IOException e) {
 // Expected
 }
 }

 @Test(expected = IOException.class)
 public void testAppendToEmptyObjectArray() throws IOException {
 Joiner joiner = Joiner.on(" ");
 StringBuilder actual = new StringBuilder();
 try {
 joiner.appendTo(actual, new Object[0]);
 fail();
 } catch (IOException e) {
 // Expected
 }
 }

 @Test
 public void testJoinIterableString() {
 String expected = "apple,banana,cherry";
 String actual = stringJoiner.join(Arrays.asList("apple", "banana", "cherry"));
 assertEquals(expected, actual);
 }

 @Test
 public void testJoinIterableChar() {
 String expected = "abc";
 String actual = charJoiner.join(Arrays.asList('a', 'b', 'c').iterator());
 assertEquals(expected, actual);
 }

 @Test
 public void testJoinObjectArrayString() {
 String[] parts = {"apple", "banana", "cherry"};
 String actual = stringJoiner.join(parts);
 assertEquals("apple,banana,cherry", actual);
 }

 @Test
 public void testJoinObjectArrayChar() {
 char[] parts = {'a', 'b', 'c'};
 String actual = charJoiner.join(parts);
 assertEquals("abc", actual);
 }

 @Test
 public void testJoinIterableNull() {
 String expected = "null,null,null";
 String actual = stringJoiner.join(Arrays.asList((Object)null, (Object)null, (Object)null));
 assertEquals(expected, actual);
 }

 @Test
 public void testJoinEmptyIterable() {
 String actual = stringJoiner.join(Collections.emptyIterator());
 assertEquals("", actual);
 }

 @Test
 public void testJoinEmptyObjectArray() {
 String actual = stringJoiner.join(new Object[0]);
 assertEquals("", actual);
 }

 @Test
 public void testJoinUseForNullString() {
 String nullText = "NULL";
 Joiner joiner = Joiner.on(",").useForNull(nullText);
 String[] parts = {"apple", null, "cherry"};
 String actual = joiner.join(parts);
 assertEquals("apple," + nullText + ",cherry", actual);
 }

 @Test
 public void testJoinSkipNullsString() {
 Joiner joiner = Joiner.on(",").skipNulls();
 String[] parts = {"apple", null, "cherry"};
 String actual = joiner.join(parts);
 assertEquals("apple,cherry", actual);
 }

 @Test
 public void testJoinUseForNullMap() {
 String nullText = "NULL";
 Joiner joiner = Joiner.on(",").useForNull(nullText);
 Map<String, String> map = new HashMap<>();
 map.put("key1", "value1");
 map.put("key2", null);
 map.put("key3", "value3");
 String actual = joiner.join(map.entrySet());
 assertEquals("key1=value1," + nullText + ",key3=value3", actual);
 }

 @Test
 public void testJoinMapKeyValueSeparatorString() {
 String keyValueSeparator = "=";
 Joiner joiner = Joiner.on(",").withKeyValueSeparator(keyValueSeparator);
 Map<String, String> map = new HashMap<>();
 map.put("key1", "value1");
 map.put("key2", null);
 map.put("key3", "value3");
 String actual = joiner.join(map.entrySet());
 assertEquals("key1=value1," + keyValueSeparator + "key2=" + nullText + "," + keyValueSeparator + "key3=value3", actual);
 }
}