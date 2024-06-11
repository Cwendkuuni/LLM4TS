package com.google.common.base;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class JoinerTest {

 private Joiner joiner;
 private StringBuilder stringBuilder;
 private Appendable appendable;
 private Iterator<String> iter;
 private Object[] objects;
 private Map<String, String> map;

 @Before
 public void setup() {
 joiner = Joiner.on(",");
 stringBuilder = new StringBuilder();
 appendable = stringBuilder;
 iter = Arrays.asList("apple", "banana", "cherry").iterator();
 objects = new Object[]{"apple", "banana", "cherry"};
 map = new HashMap<>();
 map.put("key1", "value1");
 map.put("key2", "value2");
 }

 @Test
 public void testAppendToWithIterable() throws IOException {
 joiner.appendTo(appendable, Arrays.asList("apple", "banana", "cherry"));
 assertEquals("apple,banana,cherry", appendable.toString());
 }

 @Test
 public void testAppendToWithStringSeparator() throws IOException {
 joiner.appendTo(appendable, "apple", "banana", "cherry");
 assertEquals("apple,banana,cherry", appendable.toString());
 }

 @Test
 public void testAppendToWithStringBuilder() {
 joiner.appendTo(stringBuilder, Arrays.asList("apple", "banana", "cherry"));
 assertEquals("apple,banana,cherry", stringBuilder.toString());
 }

 @Test
 public void testAppendToWithStringBuilderAndNull() {
 joiner = joiner.useForNull("NA");
 joiner.appendTo(stringBuilder, Arrays.asList(null, "banana", "cherry"));
 assertEquals("NA,banana,cherry", stringBuilder.toString());
 }

 @Test
 public void testAppendToWithStringBuilderAndSkipNulls() {
 joiner = joiner.skipNulls();
 joiner.appendTo(stringBuilder, Arrays.asList("null", "banana", "cherry", null, "orange"));
 assertEquals("banana,cherry,orange", stringBuilder.toString());
 }

 @Test
 public void testAppendToWithMap() throws IOException {
 joiner.appendTo(appendable, map.entrySet());
 assertEquals("key1=value1,key2=value2", appendable.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAppendToWithNullIterable() {
 joiner.appendTo(appendable, null);
 }

 @Test(expected = IOException.class)
 public void testAppendToWithIOException() throws IOException, InterruptedException {
 Thread.currentThread().interrupt();
 joiner.appendTo(appendable, iter);
 }

 @Test(expected = NullPointerException.class)
 public void testToStringWithNullObject() {
 joiner = joiner.useForNull("NA");
 joiner.toString(null);
 }
}