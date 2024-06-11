package com.google.common.base;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.junit.Assert.*;

import java.util.*;
import org.junit.Test;

public class JoinerTest {

 @Test
 public void testJoiner_constructor() {
 Joiner joiner = Joiner.on(",");
 assertEquals(",", joiner.separator());
 }

 @Test
 public void testJoiner_iterator() throws IOException {
 StringBuilder builder = new StringBuilder();
 Joiner joiner = Joiner.on(",");
 Iterable<String> parts = Arrays.asList("apple", "banana", "cherry");
 joiner.appendTo(builder, parts);
 assertEquals("apple,banana,cherry", builder.toString());
 }

 @Test
 public void testJoiner_objectArray() throws IOException {
 StringBuilder builder = new StringBuilder();
 Joiner joiner = Joiner.on(",");
 Object[] parts = {"apple", "banana", "cherry"};
 joiner.appendTo(builder, parts);
 assertEquals("apple,banana,cherry", builder.toString());
 }

 @Test
 public void testJoiner_useForNull() throws IOException {
 StringBuilder builder = new StringBuilder();
 Joiner joiner = Joiner.on(",").useForNull("NA");
 Object[] parts = {"apple", null, "cherry"};
 joiner.appendTo(builder, parts);
 assertEquals("apple,NA,cherry", builder.toString());
 }

 @Test
 public void testJoiner_skipNulls() throws IOException {
 StringBuilder builder = new StringBuilder();
 Joiner joiner = Joiner.on(",").skipNulls();
 Object[] parts = {"apple", null, "cherry"};
 joiner.appendTo(builder, parts);
 assertEquals("apple,cherry", builder.toString());
 }

 @Test
 public void testJoiner_map() throws IOException {
 Map<String, String> map = new HashMap<>();
 map.put("a", "apple");
 map.put("b", null);
 map.put("c", "cherry");
 Joiner joiner = Joiner.on(",").useForNull("NA");
 StringBuilder builder = new StringBuilder();
 joiner.appendTo(builder, map.entrySet());
 assertEquals("a=apple,b=NA,c=cherry", builder.toString());
 }

 @Test
 public void testJoiner_MapJoiner() throws IOException {
 Map<String, String> map = new HashMap<>();
 map.put("a", "apple");
 map.put("b", null);
 map.put("c", "cherry");
 Joiner joiner = Joiner.on(",");
 MapJoiner mapJoiner = joiner.withKeyValueSeparator("=");
 StringBuilder builder = new StringBuilder();
 joiner.appendTo(builder, mapJoiner.appendTo(new StringBuilder(), map.entrySet()));
 assertEquals("a=apple,b=NA,c=cherry", builder.toString());
 }
}