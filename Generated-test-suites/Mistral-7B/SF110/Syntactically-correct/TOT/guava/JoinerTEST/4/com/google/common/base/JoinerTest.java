package com.google.common.base;

import com.google.common.base.Joiner;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class JoinerTest {

 @Test
 public void testAppendToIterableStringSeparator() {
 Joiner joiner = Joiner.on(" ");
 StringBuilder expectedOutput = new StringBuilder();
 expectedOutput.append("test1 test2 test3");

 StringBuilder actualOutput = new StringBuilder();
 joiner.appendTo(actualOutput, Arrays.asList("test1", "test2", "test3"));
 assertEquals(expectedOutput.toString(), actualOutput.toString());
 }

 @Test(expected = NullPointerException.class)
 public void testAppendToIterableNullIterator() {
 Joiner joiner = Joiner.on(" ");
 StringBuilder actualOutput = new StringBuilder();
 joiner.appendTo(actualOutput, null);
 }

 @Test
 public void testAppendToObjectArrayStringSeparator() {
 Joiner joiner = Joiner.on(" ");
 StringBuilder expectedOutput = new StringBuilder();
 expectedOutput.append("test1 test2 test3");

 StringBuilder actualOutput = new StringBuilder();
 joiner.appendTo(actualOutput, new Object[]{"test1", "test2", "test3"});
 assertEquals(expectedOutput.toString(), actualOutput.toString());
 }

 @Test
 public void testAppendToObjectArrayEmptyArray() {
 Joiner joiner = Joiner.on(" ");
 StringBuilder actualOutput = new StringBuilder();
 joiner.appendTo(actualOutput, new Object[0]);
 assertEquals("", actualOutput.toString());
 }

 @Test(expected = NullPointerException.class)
 public void testAppendToObjectArrayNullPart() {
 Joiner joiner = Joiner.on(" ");
 StringBuilder actualOutput = new StringBuilder();
 joiner.appendTo(actualOutput, new Object[]{"test1", null, "test3"});
 }

 @Test
 public void testJoinIterable() {
 Joiner joiner = Joiner.on(" ");
 String expectedOutput = "test1 test2 test3";

 String actualOutput = joiner.join(Arrays.asList("test1", "test2", "test3"));
 assertEquals(expectedOutput, actualOutput);
 }

 @Test(expected = NullPointerException.class)
 public void testJoinNullIterable() {
 Joiner joiner = Joiner.on(" ");
 String actualOutput = joiner.join(null);
 }

 @Test
 public void testJoinObjectArray() {
 Joiner joiner = Joiner.on(" ");
 String expectedOutput = "test1 test2 test3";

 String actualOutput = joiner.join(new Object[]{"test1", "test2", "test3"});
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testJoinEmptyObjectArray() {
 Joiner joiner = Joiner.on(" ");
 String actualOutput = joiner.join(new Object[0]);
 assertEquals("", actualOutput);
 }

 @Test(expected = NullPointerException.class)
 public void testJoinNullObject() {
 Joiner joiner = Joiner.on(" ");
 String actualOutput = joiner.join(null);
 }

 @Test
 public void testUseForNull() {
 Joiner joiner = Joiner.on(" ");
 Joiner nullJoiner = joiner.useForNull("null");

 StringBuilder expectedOutput = new StringBuilder();
 expectedOutput.append("test1 null test2 null test3");

 StringBuilder actualOutput = new StringBuilder();
 nullJoiner.appendTo(actualOutput, Arrays.asList("test1", "test2", "test3"));
 assertEquals(expectedOutput.toString(), actualOutput.toString());
 }

 @Test
 public void testSkipNulls() {
 Joiner joiner = Joiner.on(" ");
 Joiner skipNullJoiner = joiner.skipNulls();

 StringBuilder expectedOutput = new StringBuilder();
 expectedOutput.append("test1 test2 test3");

 StringBuilder actualOutput = new StringBuilder();
 skipNullJoiner.appendTo(actualOutput, Arrays.asList("test1", null, "test2", null, "test3"));
 assertEquals(expectedOutput.toString(), actualOutput.toString());
 }

 @Test
 public void testMapJoiner() {
 Joiner joiner = Joiner.on(": ");
 MapJoiner mapJoiner = joiner.withKeyValueSeparator(": ");

 Map<String, String> testMap = new HashMap<>();
 testMap.put("key1", "value1");
 testMap.put("key2", "value2");
 testMap.put("key3", "value3");

 StringBuilder expectedOutput = new StringBuilder();
 expectedOutput.append("key1: value1: key2: value2: key3: value3");

 StringBuilder actualOutput = new StringBuilder();
 mapJoiner.appendTo(actualOutput, testMap.entrySet());
 assertEquals(expectedOutput.toString(), actualOutput.toString());
 }
}