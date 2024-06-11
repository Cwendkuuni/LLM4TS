package com.google.common.base;

import com.google.common.base.Joiner;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class JoinerTest {

 private Joiner joiner;

 @Before
 public void setUp() {
 joiner = Joiner.on(",");
 }

 @Test
 public void testAppendToIterable() throws IOException {
 Iterable<String> iterable = Arrays.asList("test1", "test2", "test3");
 StringBuilder expected = new StringBuilder();
 expected.append("test1,");
 expected.append("test2,");
 expected.append("test3");

 StringBuilder actual = new StringBuilder();
 joiner.appendTo(actual, iterable);
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testAppendToIterableWithEmptyList() throws IOException {
 Iterable<String> iterable = Collections.emptyList();
 StringBuilder actual = new StringBuilder();
 joiner.appendTo(actual, iterable);
 assertEquals("", actual.toString());
 }

 @Test(expected = IOException.class)
 public void testAppendToIterableWithNullIterator() throws IOException {
 Iterator<String> nullIterator = null;
 StringBuilder actual = new StringBuilder();
 joiner.appendTo(actual, nullIterator);
 fail("IOException was not thrown");
 }

 @Test
 public void testAppendToObjectArray() throws IOException {
 Object[] objectArray = new Object[]{"test1", "test2", "test3"};
 StringBuilder expected = new StringBuilder();
 expected.append("test1,");
 expected.append("test2,");
 expected.append("test3");

 StringBuilder actual = new StringBuilder();
 joiner.appendTo(actual, objectArray);
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testAppendToNullFirstObjectAndNonNullSecondObject() throws IOException {
 Object[] objectArray = new Object[]{null, "test2", "test3"};
 StringBuilder expected = new StringBuilder();
 expected.append("null,");
 expected.append("test2,");
 expected.append("test3");

 StringBuilder actual = new StringBuilder();
 joiner.appendTo(actual, objectArray);
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testAppendToObjectArrayWithEmptyArray() throws IOException {
 Object[] emptyArray = {};
 StringBuilder actual = new StringBuilder();
 joiner.appendTo(actual, emptyArray);
 assertEquals("", actual.toString());
 }

 @Test
 public void testAppendToMap() {
 Map<String, String> map = new LinkedHashMap<>();
 map.put("key1", "value1");
 map.put("key2", "value2");
 map.put("key3", "value3");

 StringBuilder expected = new StringBuilder();
 expected.append("key1=value1,");
 expected.append("key2=value2,");
 expected.append("key3=value3");

 StringBuilder actual = new StringBuilder();
 joiner.appendTo(actual, map.entrySet());
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testJoinIterable() {
 Iterable<String> iterable = Arrays.asList("test1", "test2", "test3");
 String expected = "test1,test2,test3";
 String actual = joiner.join(iterable);
 assertEquals(expected, actual);
 }

 @Test
 public void testJoinIterableWithEmptyList() {
 Iterable<String> iterable = Collections.emptyList();
 String actual = joiner.join(iterable);
 assertEquals("", actual);
 }

 @Test
 public void testJoinObjectArray() {
 Object[] objectArray = new Object[]{"test1", "test2", "test3"};
 String expected = "test1,test2,test3";
 String actual = joiner.join(objectArray);
 assertEquals(expected, actual);
 }

 @Test
 public void testJoinNullFirstObjectAndNonNullSecondObject() {
 Object[] objectArray = new Object[]{null, "test2", "test3"};
 String expected = "null,test2,test3";
 String actual = joiner.join(objectArray);
 assertEquals(expected, actual);
 }

 @Test
 public void testJoinMap() {
 Map<String, String> map = new LinkedHashMap<>();
 map.put("key1", "value1");
 map.put("key2", "value2");
 map.put("key3", "value3");String expected = "key1=value1,key2=value2,key3=value3";
 String actual = joiner.join(map);
 assertEquals(expected, actual);
 }

 @Test
 public void testUseForNull() {
 joiner = joiner.useForNull("NULL");
 Object[] objectArray = new Object[]{null, "test2", "test3"};
 String expected = "NULL,test2,test3";
 String actual = joiner.join(objectArray);
 assertEquals(expected, actual);
 }

 @Test
 public void testSkipNulls() {
 joiner = joiner.skipNulls();
 Object[] objectArray = new Object[]{null, "test2", null, "test4"};
 String expected = "test2,test4";
 String actual = joiner.join(objectArray);
 assertEquals(expected, actual);
 }
}