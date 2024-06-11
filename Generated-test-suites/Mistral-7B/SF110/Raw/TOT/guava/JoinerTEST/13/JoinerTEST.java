package com.google.common.base;

import com.google.common.base.Joiner;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class JoinerTest {

 @Test
 public void testStringJoiner() throws Exception {
 Joiner joiner = Joiner.on(",");
 String join = joiner.join(Arrays.asList("apple", "banana", "cherry"));
 assertEquals("apple,banana,cherry", join);
 }

 @Test
 public void testCharJoiner() throws Exception {
 Joiner joiner = Joiner.on('-');
 StringBuilder stringBuilder = new StringBuilder();
 joiner.appendTo(stringBuilder, Arrays.asList("apple", "banana", "cherry"));
 assertEquals("apple-banana-cherry", stringBuilder.toString());
 }

 @Test
 public void testNullJoiner() throws Exception {
 Joiner joiner = Joiner.on(",");
 Joiner joinerNull = joiner.useForNull("NA");
 String join = joinerNull.join(Collections.singletonList(null));
 assertEquals("NA", join);
 }

 @Test
 public void testMapJoiner() throws Exception {
 Joiner joiner = Joiner.on(",");
 Map<String, String> map = new ConcurrentHashMap<>();
 map.put("apple", "fruit");
 map.put("banana", "yellow");
 map.put("cherry", "red");
 MapJoiner mapJoiner = joiner.withKeyValueSeparator(": ");
 StringBuilder stringBuilder = new StringBuilder();
 mapJoiner.appendTo(stringBuilder, map.entrySet());
 assertEquals("apple: fruit, banana: yellow, cherry: red", stringBuilder.toString());
 }

 @Test(expected = IOException.class)
 public void testAppendToWithNullIterable() throws Exception {
 Joiner joiner = Joiner.on(",");
 joiner.appendTo(new StringBuilder(), null);
 fail("IOException expected but not thrown");
 }

 @Test(expected = NullPointerException.class)
 public void testToStringWithNullObject() throws Exception {
 Joiner joiner = Joiner.on(",");
 joiner.toString(null);
 fail("NullPointerException expected but not thrown");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testUseForNullAndSkipNulls() throws Exception {
 Joiner joiner = Joiner.on(",");
 Joiner joinerNull = joiner.skipNulls();
 Joiner joinerUseForNull = joinerNull.useForNull("NA");
 Object[] objects = new Object[]{ null, "apple", null, "banana", null };
 StringBuilder stringBuilder = new StringBuilder();
 joinerUseForNull.appendTo(stringBuilder, objects);
 assertEquals("NA,apple,NA,banana,NA", stringBuilder.toString());
 }
}