package com.google.common.base;

import com.google.common.base.Joiner;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class JoinerTest {

 @Test
 public void testAppendToWithIterable() throws IOException {
 Joiner joiner = Joiner.on(",");
 StringBuilder expected = new StringBuilder();
 expected.append("apple").append(joiner.separator).append("banana").append(joiner.separator).append("orange");
 StringBuilder actual = new StringBuilder();
 joiner.appendTo(actual, Arrays.asList("apple", "banana", "orange"));
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testAppendToWithIterator() throws IOException {
 Joiner joiner = Joiner.on(",");
 StringBuilder expected = new StringBuilder();
 expected.append("apple").append(joiner.separator).append("banana").append(joiner.separator).append("orange");
 StringBuilder actual = new StringBuilder();
 Iterator<String> iterator = Arrays.asList("apple", "banana", "orange").iterator();
 joiner.appendTo(actual, iterator);
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testAppendToWithObjectArray() throws IOException {
 Joiner joiner = Joiner.on(",");
 StringBuilder expected = new StringBuilder();
 expected.append("apple").append(joiner.separator).append("banana").append(joiner.separator).append("orange");
 StringBuilder actual = new StringBuilder();
 joiner.appendTo(actual, new Object[]{"apple", "banana", "orange"});
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testAppendToWithNullAndOtherObjects() throws IOException {
 Joiner joiner = Joiner.on(",");
 StringBuilder expected = new StringBuilder();
 expected.append("null").append(joiner.separator).append("apple").append(joiner.separator).append("banana").append(joiner.separator).append("orange");
 StringBuilder actual = new StringBuilder();
 joiner.appendTo(actual, new Object[]{null, "apple", "banana", "orange"});
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testAppendToWithEmptyIterable() throws IOException {
 Joiner joiner = Joiner.on(",");
 StringBuilder actual = new StringBuilder();
 joiner.appendTo(actual, Collections.emptyIterator());
 assertEquals("", actual.toString());
 }

 @Test(expected = IOException.class)
 public void testAppendToWithNullIterable() throws IOException {
 Joiner joiner = Joiner.on(",");
 joiner.appendTo(null, Collections.emptyIterator());
 }

 @Test
 public void testJoinWithIterable() {
 Joiner joiner = Joiner.on(",");
 List<String> list = Arrays.asList("apple", "banana", "orange");
 assertEquals("apple," + joiner.separator + "banana," + joiner.separator + "orange", joiner.join(list));
 }

 @Test
 public void testJoinWithArray() {
 Joiner joiner = Joiner.on(",");
 String[] array = new String[]{"apple", "banana", "orange"};
 assertEquals("apple," + joiner.separator + "banana," + joiner.separator + "orange", joiner.join(array));
 }

 @Test
 public void testJoinWithNullAndOtherObjects() {
 Joiner joiner = Joiner.on(",");
 Object[] array = new Object[]{null, "apple", "banana", "orange"};
 assertEquals("null," + joiner.separator + "apple," + joiner.separator + "banana," + joiner.separator + "orange", joiner.join(array));
 }

 @Test
 public void testJoinWithEmptyIterable() {
 Joiner joiner = Joiner.on(",");
 Iterator<String> emptyIterator = Collections.emptyIterator();
 assertEquals("", joiner.join(emptyIterator));
 }

 @Test(expected = NullPointerException.class)
 public void testJoinWithNullIterable() {
 Joiner joiner = Joiner.on(",");
 joiner.join(null);
 }

 @Test
 public void testUseForNull() {
 Joiner joiner = Joiner.on(",");
 Joiner joinerWithNullText = joiner.useForNull("NA");
 assertEquals("NA,", joinerWithNullText.toString(null));
 }

 @Test
 public void testSkipNulls() throws IOException {
 Joiner joiner = Joiner.on(",");
 StringBuilder expected = new StringBuilder();
 expected.append("apple").append(joiner.separator).append("banana").append(joiner.separator).append("orange");
 StringBuilder actual = new StringBuilder();
 joiner.appendTo(actual, Arrays.asList(null, "apple", null, "banana", "orange", null).iterator());
 Joiner joinerSkippingNulls = joiner.skipNulls();
 joinerSkippingNulls.appendTo(actual, Arrays.asList(null, "apple", null, "banana", "orange", null).iterator());
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testMapJoiner() {
 Joiner joiner = Joiner.on(",");
 Map<String, String> map = new HashMap<>();
 map.put("Apple", "Fruit");
 map.put("Banana", "Fruit");
 map.put("Orange", "Fruit");
 MapJoiner mapJoiner = joiner.withKeyValueSeparator(" => ");
 StringBuilder expected = new StringBuilder();
 expected.append("Apple => Fruit").append(joiner.separator).append("Banana => Fruit").append(joiner.separator).append("Orange => Fruit");
 StringBuilder actual = new StringBuilder();
 mapJoiner.appendTo(actual, map.entrySet());
 assertEquals(expected.toString(), actual.toString());
 }
}