package com.google.common.base;

import com.google.common.base.Joiner;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

public class JoinerTest {

 @Test
 public void testStringSeparator() {
 Joiner joiner = Joiner.on(",");
 String[] parts = {"apple", "banana", "cherry"};
 String expected = "apple,banana,cherry";
 StringBuilder stringBuilder = new StringBuilder();
 try {
 joiner.appendTo(stringBuilder, Arrays.asList(parts));
 } catch (IOException e) {
 throw new RuntimeException(e);
 }
 String actual = stringBuilder.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testCharSeparator() {
 Joiner joiner = Joiner.on('/');
 String[] parts = {"apple", "banana", "cherry"};
 String expected = "apple/banana/cherry";
 StringBuilder stringBuilder = new StringBuilder();
 try {
 joiner.appendTo(stringBuilder, Arrays.asList(parts));
 } catch (IOException e) {
 throw new RuntimeException(e);
 }
 String actual = stringBuilder.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testAppendToEmptyIterable() {
 Joiner joiner = Joiner.on(" ");
 String expected = "";
 StringBuilder stringBuilder = new StringBuilder();
 try {
 joiner.appendTo(stringBuilder, Collections.emptyList());
 } catch (IOException e) {
 throw new RuntimeException(e);
 }
 String actual = stringBuilder.toString();
 assertEquals(expected, actual);
 }

 @Test(expected = NullPointerException.class)
 public void testNullIterator() {
 Joiner joiner = Joiner.on(" ");
 String[] parts = {};
 Iterator<?> iterator = null;
 StringBuilder stringBuilder = new StringBuilder();
 try {
 joiner.appendTo(stringBuilder, iterator, Arrays.asList(parts));
 } catch (IOException e) {
 fail("IOException should not be thrown");
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullSeparator() {
 Joiner joiner = new Joiner("");
 joiner.appendTo(new StringBuilder(), Arrays.asList("apple", "banana"), (String)null);
 }

 @Test
 public void testJoin() {
 Joiner joiner = Joiner.on(" ");
 String[] parts = {"apple", "banana", "cherry"};
 String expected = "apple banana cherry";
 String actual = joiner.join(Arrays.asList(parts));
 assertEquals(expected, actual);
 }

 @Test
 public void testUseForNull() {
 Joiner joiner = Joiner.on(" ").useForNull("null");
 String[] parts = {"apple", null, "cherry"};
 String expected = "apple null cherry";
 String actual = joiner.join(Arrays.asList(parts));
 assertEquals(expected, actual);
 }

 @Test
 public void testSkipNulls() {
 Joiner joiner = Joiner.on(" ").skipNulls();
 String[] parts = {"apple", null, "cherry", null, "orange"};
 String expected = "apple cherry orange";
 String actual = joiner.join(Arrays.asList(parts));
 assertEquals(expected, actual);
 }

 @Test
 public void testMapJoiner() {
 Map<String, String> map = new HashMap<>();
 map.put("apple", "red");
 map.put("banana", "yellow");
 map.put("cherry", null);
 Joiner joiner = Joiner.on("=").withKeyValueSeparator(":");
 MapJoiner mapJoiner = joiner.withKeyValueSeparator(":");
 String expected = "apple:red=banana:yellow";
 StringBuilder stringBuilder = new StringBuilder();
 try {
 mapJoiner.appendTo(stringBuilder, map);
 } catch (IOException e) {
 throw new RuntimeException(e);
 }
 String actual = stringBuilder.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testMapIterator() {
 Map<String, String> map = new HashMap<>();
 map.put("apple", "red");
 map.put("banana", null);
 map.put("cherry", "green");
 Joiner joiner = Joiner.on("=").withKeyValueSeparator(":");
 MapJoiner mapJoiner = joiner.withKeyValueSeparator(":");
 StringBuilder stringBuilder = new StringBuilder();
 try {
 mapJoiner.appendTo(stringBuilder, map.entrySet().iterator());
 } catch (IOException e) {
 throw new RuntimeException(e);
 }
 String expected = "apple:red=banana:null=cherry:green";
 String actual = stringBuilder.toString();
 assertEquals(expected, actual);
 }
}