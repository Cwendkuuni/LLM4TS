package com.google.common.base;

import com.google.common.base.Joiner;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class JoinerTest {

 @Test
 public void testJoinStringSeparator() {
 String separator = ",";
 Joiner joiner = Joiner.on(separator);
 String[] parts = {"apple", "banana", "cherry"};
 String expected = "apple,banana,cherry";
 String result = joiner.join(Arrays.asList(parts));
 assertEquals(expected, result);
 }

 @Test
 public void testJoinCharSeparator() {
 char separator = '-';
 Joiner joiner = Joiner.on(separator);
 String[] parts = {"apple", "banana", "cherry"};
 String expected = "apple-banana-cherry";
 String result = joiner.join(Arrays.asList(parts));
 assertEquals(expected, result);
 }

 @Test
 public void testAppendToStringBuilder() {
 Joiner joiner = Joiner.on(",");
 StringBuilder sb = new StringBuilder();
 Iterable<String> parts = Arrays.asList("apple", "banana", "cherry");
 joiner.appendTo(sb, parts);
 String expected = "apple,banana,cherry";
 String result = sb.toString();
 assertEquals(expected, result);
 }

 @Test
 public void testAppendToByteArrayOutputStream() {
 Joiner joiner = Joiner.on(",");
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 Iterable<String> parts = Arrays.asList("apple", "banana", "cherry");
 joiner.appendTo(baos, parts);
 String expected = "apple,banana,cherry";
 String result = new String(baos.toByteArray(), 0, baos.size());
 assertEquals(expected, result);
 }

 @Test(expected = NullPointerException.class)
 public void testNullSeparator() {
 Joiner joiner = null;
 Iterable<String> parts = Arrays.asList("apple", "banana", "cherry");
 assertThrows(NullPointerException.class, () -> joiner.join(parts));
 }

 @Test(expected = NullPointerException.class)
 public void testNullParts() {
 Joiner joiner = Joiner.on(",");
 assertThrows(NullPointerException.class, () -> joiner.join(null));
 }

 @Test(expected = IOException.class)
 public void testIOException() throws IOException {
 Joiner joiner = Joiner.on(",");
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 Iterator<String> parts = Arrays.asList("apple", "banana", "cherry").iterator();
 joiner.appendTo(baos, parts);
 baos.close();
 assertThrows(IOException.class, () -> joiner.appendTo(baos, Arrays.asList("apple", "banana", "cherry").iterator()));
 }

 @Test(expected = NoSuchElementException.class)
 public void testEmptyIterator() {
 Joiner joiner = Joiner.on(",");
 StringBuilder sb = new StringBuilder();
 Iterator<?> parts = Collections.emptyIterator();
 joiner.appendTo(sb, parts);
 String expected = "";
 String result = sb.toString();
 assertEquals(expected, result);
 }

 @Test
 public void testUseForNull() {
 Joiner joiner = Joiner.on(",");
 String nullText = "NULL";
 Joiner joinerWithNullText = joiner.useForNull(nullText);
 String[] parts = {"apple", null, "cherry"};
 String expected = "apple," + nullText + ",cherry";
 String result = joinerWithNullText.join(Arrays.asList(parts));
 assertEquals(expected, result);
 }

 @Test
 public void testSkipNulls() {
 Joiner joiner = Joiner.on(",");
 String[] parts = {"apple", null, "banana", null, "cherry"};
 String expected = "apple,banana,cherry";
 String result = joiner.skipNulls().join(Arrays.asList(parts));
 assertEquals(expected, result);
 }

 @Test
 public void testMapJoiner() {
 Joiner joiner = Joiner.on(",");
 Map<String, String> map = new HashMap<>();
 map.put("apple", "red");
 map.put("banana", "yellow");
 map.put("cherry", "green");
 MapJoiner mapJoiner = joiner.withKeyValueSeparator(":");
 String expected = "apple:red,banana:yellow,cherry:green";
 String result = mapJoiner.join(map.entrySet());
 assertEquals(expected, result);
 }

 @Test
 public void testMapJoinerUseForNull() {
 Joiner joiner = Joiner.on(",");
 Map<String, String> map = new HashMap<>();
 map.put("apple", null);
 map.put("banana", "yellow");
 map.put("cherry", null);
 MapJoiner mapJoiner = joiner.withKeyValueSeparator(":");
 String nullText = "NULL";
 MapJoiner mapJoinerWithNullText = mapJoiner.useForNull(nullText);
 String expected = "apple:" + nullText + "," + "banana:yellow," + "cherry:" + nullText;
 String result = mapJoinerWithNullText.join(map.entrySet());
 assertEquals(expected, result);
 }
}