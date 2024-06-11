package com.google.common.base.test;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class JoinerTest {

 @Test
 public void testStringJoiner() {
 Joiner joiner = Joiner.on(":");
 List<String> list = Lists.newArrayList("apple", "banana", "cherry");
 StringBuilder expected = new StringBuilder();
 expected.append("apple:banana:cherry");
 StringBuilder actual = new StringBuilder();
 try {
 joiner.appendTo(actual, list);
 } catch (IOException e) {
 throw new RuntimeException(e);
 }
 assertEquals(expected.toString(), actual.toString());
 }

 @Test(expected = NullPointerException.class)
 public void testNullIterator() {
 Joiner joiner = Joiner.on(":");
 Iterator<?> nullIterator = null;
 try {
 joiner.appendTo(new StringBuilder(), nullIterator);
 } catch (IOException e) {
 // expected
 }
 }

 @Test
 public void testEmptyIterable() {
 Joiner joiner = Joiner.on(":");
 StringBuilder expected = new StringBuilder();
 StringBuilder actual = new StringBuilder();
 try {
 joiner.appendTo(actual, Collections.emptyList());
 } catch (IOException e) {
 throw new RuntimeException(e);
 }
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testCharJoiner() {
 Joiner joiner = Joiner.on('/');
 List<String> list = Lists.newArrayList("apple", "banana", "cherry");
 StringBuilder expected = new StringBuilder();
 expected.append("a").append(joiner.separator).append("p").append(joiner.separator).append("p").append(joiner.separator).append("l").append(joiner.separator).append("e").append(joiner.separator).append(".").append(joiner.separator).append("a").append(joiner.separator).append("p").append(joiner.separator).append("p").append(joiner.separator).append("c").append(joiner.separator).append("h").append(joiner.separator).append("e").append(joiner.separator).append("r").append(joiner.separator).append("r");
 StringBuilder actual = new StringBuilder();
 try {
 joiner.appendTo(actual, list);
 } catch (IOException e) {
 throw new RuntimeException(e);
 }
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testJoinObjectArray() {
 Joiner joiner = Joiner.on(":");
 Object[] objArray = new Object[]{1, "two", 3.5f, "four"};
 StringBuilder expected = new StringBuilder();
 expected.append(1).append(joiner.separator).append("two").append(joiner.separator).append(3.5f).append(joiner.separator).append("four");
 StringBuilder actual = new StringBuilder();
 try {
 joiner.appendTo(actual, objArray);
 } catch (IOException e) {
 throw new RuntimeException(e);
 }
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testJoinMapEntry() {
 Joiner joiner = Joiner.on("\t");
 Map<String, String> map = new HashMap<>();
 map.put("key1", "value1");
 map.put("key2", "value2");
 MapJoiner mapJoiner = joiner.withKeyValueSeparator("\t");
 StringBuilder expected = new StringBuilder();
 expected.append("key1\tvalue1\nkey2\tvalue2\n");
 StringBuilder actual = new StringBuilder();
 try {
 mapJoiner.appendTo(actual, map.entrySet());
 } catch (IOException e) {
 throw new RuntimeException(e);
 }
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testUseForNull() {
 Joiner joiner = Joiner.on(":");
 Joiner useForNullJoiner = joiner.useForNull("null");
 List<Object> list = Arrays.asList(null, "one", "two", null, "five");
 StringBuilder expected = new StringBuilder();
 expected.append("null:one:two:null:five");
 StringBuilder actual = new StringBuilder();
 try {
 useForNullJoiner.appendTo(actual, list);
 } catch (IOException e) {
 throw new RuntimeException(e);
 }
 assertEquals(expected.toString(), actual.toString());
 }
}