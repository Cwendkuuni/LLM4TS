package com.google.common.base;

import com.google.common.base.Joiner;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class JoinerTest {

 @Test
 public void testAppendingIterableToStringBuilder() {
 StringJoiner joiner = Joiner.on(" ");
 StringBuilder stringBuilder = new StringBuilder();
 List<String> list = Arrays.asList("apple", "banana", "cherry");
 joiner.appendTo(stringBuilder, list.iterator());
 String expected = "apple banana cherry";
 assertEquals(expected, stringBuilder.toString());
 }

 @Test
 public void testAppendingNullFirstElementToStringBuilder() {
 StringJoiner joiner = Joiner.on(" ");
 StringBuilder stringBuilder = new StringBuilder();
 joiner.appendTo(stringBuilder, null, "banana", "cherry");
 String expected = "banana cherry";
 assertEquals(expected, stringBuilder.toString());
 }

 @Test
 public void testAppendingNullElementToStringBuilder() {
 StringJoiner joiner = Joiner.on(" ");
 StringBuilder stringBuilder = new StringBuilder();
 List<String> list = Arrays.asList("apple", null, "cherry");
 joiner.appendTo(stringBuilder, list.iterator());
 String expected = "apple cherry";
 assertEquals(expected, stringBuilder.toString());
 }

 @Test(expected = IOException.class)
 public void testAppendingNullIteratorToStringBuilder() {
 StringJoiner joiner = Joiner.on(" ");
 StringBuilder stringBuilder = new StringBuilder();
 joiner.appendTo(stringBuilder, null);
 fail("Should throw IOException");
 }

 @Test
 public void testAppendingObjectArrayToStringBuilder() {
 StringJoiner joiner = Joiner.on(" ");
 StringBuilder stringBuilder = new StringBuilder();
 joiner.appendTo(stringBuilder, new Object[]{"apple", "banana", "cherry"});
 String expected = "apple banana cherry";
 assertEquals(expected, stringBuilder.toString());
 }

 @Test
 public void testAppendingNullFirstObjectToStringBuilder() {
 StringJoiner joiner = Joiner.on(" ");
 StringBuilder stringBuilder = new StringBuilder();
 joiner.appendTo(stringBuilder, null, "banana", "cherry");
 String expected = "banana cherry";
 assertEquals(expected, stringBuilder.toString());
 }

 @Test
 public void testAppendingNullObjectToStringBuilder() {
 StringJoiner joiner = Joiner.on(" ");
 StringBuilder stringBuilder = new StringBuilder();
 Object[] objects = {"apple", null, "cherry"};
 joiner.appendTo(stringBuilder, objects);
 String expected = "apple cherry";
 assertEquals(expected, stringBuilder.toString());
 }

 @Test
 public void testAppendingStringBuilderToStringBuilder() {
 StringJoiner joiner = Joiner.on(" ");
 StringBuilder stringBuilder1 = new StringBuilder("apple");
 StringBuilder stringBuilder2 = new StringBuilder("banana cherry");
 StringBuilder stringBuilder3 = new StringBuilder();
 joiner.appendTo(stringBuilder3, stringBuilder1, stringBuilder2.iterator());
 String expected = "apple banana cherry";
 assertEquals(expected, stringBuilder3.toString());
 }

 @Test
 public void testAppendingNullStringBuilderToStringBuilder() {
 StringJoiner joiner = Joiner.on(" ");
 StringBuilder stringBuilder1 = new StringBuilder("apple");
 StringBuilder stringBuilder2 = null;
 StringBuilder stringBuilder3 = new StringBuilder();
 joiner.appendTo(stringBuilder3, stringBuilder1, stringBuilder2.iterator());
 String expected = "apple";
 assertEquals(expected, stringBuilder3.toString());
 }

 @Test
 public void testAppendingNullIteratorToStringBuilderWithNulls() {
 StringJoiner joiner = Joiner.on(" ");
 StringBuilder stringBuilder = new StringBuilder();
 joiner.skipNulls().appendTo(stringBuilder, Arrays.asList(null, "banana", null).iterator());
 String expected = "banana";
 assertEquals(expected, stringBuilder.toString());
 }

 @Test
 public void testAppendingIterableToString() {
 StringJoiner joiner = Joiner.on(" ");
 String expected = "apple banana cherry";
 assertEquals(expected, joiner.join(Arrays.asList("apple", "banana", "cherry")));
 }

 @Test
 public void testAppendingNullFirstElementToString() {
 StringJoiner joiner = Joiner.on(" ");
 String expected = "banana cherry";
 assertEquals(expected, joiner.join(Arrays.asList(null, "banana", "cherry")));
 }

 @Test
 public void testAppendingNullElementToString() {
 StringJoiner joiner = Joiner.on(" ");
 String expected = "apple cherry";
 assertEquals(expected, joiner.join(Arrays.asList("apple", null, "cherry")));
 }

 @Test
 public void testAppendingObjectArrayToString() {
 StringJoiner joiner = Joiner.on(" ");
 String expected = "apple banana cherry";
 assertEquals(expected, joiner.join(new Object[]{"apple", "banana", "cherry"}));
 }

 @Test
 public void testAppendingNullFirstObjectToString() {
 StringJoiner joiner = Joiner.on(" ");
 String expected = "banana cherry";
 assertEquals(expected, joiner.join(new Object[]{null, "banana", "cherry"}));
 }

 @Test
 public void testAppendingNullObjectToString() {
 StringJoiner joiner = Joiner.on(" ");
 String expected = "apple cherry";
 assertEquals(expected, joiner.join(new Object[]{"apple", null, "cherry"}));
 }

 @Test
 public void testJoiningMapEntrySetToString() {
 StringJoiner joiner = Joiner.on("=");
 Map<String, Integer> map = new HashMap<>();
 map.put("one", 1);
 map.put("two", 2);
 map.put("three", 3);
 String expected = "one=1 two=2 three=3";
 assertEquals(expected, joiner.join(map.entrySet()));
 }

 @Test
 public void testJoiningMapToString() {
 StringJoiner joiner = Joiner.on("=");
 Map<String, Integer> map = new HashMap<>();
 map.put("one", 1);
 map.put("two", 2);
 map.put("three", 3);
 String expected = "one=1, two=2, three=3";
 assertEquals(expected, joiner.join(map));
 }

 @Test
 public void testJoiningEmptyMapToString() {
 StringJoiner joiner = Joiner.on("=");
 Map<String, Integer> map = Collections.emptyMap();
 String expected = "";
 assertEquals(expected, joiner.join(map));
 }
}