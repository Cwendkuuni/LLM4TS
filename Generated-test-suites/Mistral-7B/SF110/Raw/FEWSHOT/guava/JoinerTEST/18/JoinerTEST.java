package com.google.common.base;

import static com.google.common.base.Joiner.UseForNull.nullText;
import static org.junit.Assert.*;

import com.google.common.base.Joiner;
import com.google.common.base.Joiner.MapJoiner;
import com.google.common.base.Joiner.UseForNull;
import com.google.common.collect.ImmutableMap;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.junit.Test;

public class JoinerTest {

 @Test
 public void testJoinerCreation() {
 Joiner joiner = Joiner.on(":");
 UseForNull nullJoiner = joiner.useForNull("NULL");
 assertEquals(":", joiner.separator);
 assertEquals("NULL", nullJoiner.getUseForNull());
 }

 @Test
 public void testJoinStringArray() {
 Joiner joiner = Joiner.on(":");
 StringBuilder result = new StringBuilder();
 String[] parts = {"abc", "def", "ghi"};
 joiner.appendTo(result, parts);
 assertEquals("abc:def:ghi", result.toString());
 }

 @Test
 public void testJoinObjects() {
 Joiner joiner = Joiner.on(":");
 StringBuilder result = new StringBuilder();
 Object[] parts = {"abc", 1, 3.0};
 joiner.appendTo(result, parts);
 assertEquals("abc:1:3.0", result.toString());
 }

 @Test
 public void testJoinIterable() {
 Joiner joiner = Joiner.on(":");
 StringBuilder result = new StringBuilder();
 List<String> parts = Arrays.asList("abc", "def", "ghi");
 joiner.appendTo(result, parts);
 assertEquals("abc:def:ghi", result.toString());
 }

 @Test
 public void testJoinMapEntrySet() {
 Joiner joiner = Joiner.on(":");
 Map<String, String> map = ImmutableMap.of("A", "1", "B", "2");
 StringBuilder result = new StringBuilder();
 joiner.appendTo(result, map.entrySet());
 assertEquals("A:1:B:2", result.toString());
 }

 @Test
 public void testJoinMapEntrySetWithKeyValueSeparator() {
 Joiner joiner = Joiner.on(":").withKeyValueSeparator("=");
 Map<String, String> map = ImmutableMap.of("A", "1", "B", "2");
 StringBuilder result = new StringBuilder();
 joiner.appendTo(result, map.entrySet());
 assertEquals("A=1:B=2", result.toString());
 }

 @Test
 public void testJoinNullObjects() {
 Joiner joiner = Joiner.on(":");
 Joiner nullJoiner = joiner.useForNull("NULL");
 StringBuilder result = new StringBuilder();
 Object[] parts = {"abc", null, "def"};
 nullJoiner.appendTo(result, parts);
 assertEquals("abc:NULL:def", result.toString());
 }

 @Test
 public void testJoinNullIterable() {
 Joiner joiner = Joiner.on(":");
 Joiner nullJoiner = joiner.useForNull("NULL");
 StringBuilder result = new StringBuilder();
 Iterator<Object> parts = Arrays.asList(null, "def", "ghi").iterator();
 nullJoiner.appendTo(result, parts);
 assertEquals("NULL:def:ghi", result.toString());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testJoinNullAndNonNullObjects() {
 Joiner joiner = Joiner.on(":");
 Joiner nullJoiner = joiner.useForNull("NULL");
 StringBuilder result = new StringBuilder();
 Object[] parts = {"abc", null, "def"};
 nullJoiner.appendTo(result, parts);
 nullJoiner.skipNulls(); // UnsupportedOperationException
 }

 @Test
 public void testJoinNullAndNonNullIterable() {
 Joiner joiner = Joiner.on(":");
 Joiner nullJoiner = joiner.useForNull("NULL");
 StringBuilder result = new StringBuilder();
 Iterator<Object> parts = Arrays.asList(null, "def", "ghi").iterator();
 nullJoiner.appendTo(result, parts);
 assertEquals("def:ghi", result.toString());
 }

}