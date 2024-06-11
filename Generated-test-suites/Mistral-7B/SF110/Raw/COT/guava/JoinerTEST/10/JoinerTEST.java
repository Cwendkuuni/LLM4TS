package com.example.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Joiner;
import com.google.common.base.MapJoiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterators;

public class JoinerTest {

 private Joiner joiner;
 private MapJoiner mapJoiner;

 @Before
 public void setUp() {
 joiner = Joiner.on(":");
 mapJoiner = joiner.withKeyValueSeparator(" = ");
 }

 @Test
 public void testOnString() {
 Joiner joiner = Joiner.on(":");
 assertNotNull(joiner);
 }

 @Test
 public void testOnChar() {
 Joiner joiner = Joiner.on(':');
 assertNotNull(joiner);
 }

 @Test
 public void testAppenderWithIterable() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 Joiner joiner = Joiner.on(":");
 Iterable<String> parts = Arrays.asList("Hello", "World");
 joiner.appendTo(outputStream, parts);
 assertEquals("Hello:World", outputStream.toString());
 }

 @Test
 public void testAppenderWithIterator() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 Joiner joiner = Joiner.on(":");
 Iterator<String> parts = Arrays.asList("Hello", "World").iterator();
 joiner.appendTo(outputStream, parts);
 assertEquals("Hello:World", outputStream.toString());
 }

 @Test
 public void testAppenderWithObjectArray() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 Joiner joiner = Joiner.on(":");
 Object[] parts = new Object[]{"Hello", "World"};
 joiner.appendTo(outputStream, parts);
 assertEquals("Hello:World", outputStream.toString());
 }

 @Test
 public void testAppenderWithNullFirst() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 Joiner joiner = Joiner.on(":");
 Object[] parts = new Object[]{null, "World"};
 joiner.appendTo(outputStream, parts);
 assertEquals("null:World", outputStream.toString());
 }

 @Test
 public void testAppenderWithNullSecond() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 Joiner joiner = Joiner.on(":");
 Object[] parts = new Object[]{"Hello", null};
 joiner.appendTo(outputStream, parts);
 assertEquals("Hello:", outputStream.toString());
 }

 @Test
 public void testAppenderWithNullAndBoth() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 Joiner joiner = Joiner.on(":");
 Object[] parts = new Object[]{null, null};
 joiner.appendTo(outputStream, parts);
 assertEquals("null:", outputStream.toString());
 }

 @Test(expected = NullPointerException.class)
 public void testAppenderWithNullIterator() {
 Joiner joiner = Joiner.on(":");
 joiner.appendTo(new StringBuilder(), null);
 }

 @Test(expected = IOException.class)
 public void testAppenderWithIOException() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream() {
 @Override
 public void write(int b) throws IOException {
 throw new IOException("Test IOException");
 }
 };
 Joiner joiner = Joiner.on(":");
 joiner.appendTo(outputStream, Arrays.asList("Hello", "World").iterator());
 }

 @Test
 public void testJoinWithIterable() {
 String result = joiner.join(Arrays.asList("Hello", "World"));
 assertEquals("Hello:World", result);
 }

 @Test
 public void testJoinWithIterator() {
 String result = joiner.join(Arrays.asList("Hello", "World").iterator());
 assertEquals("Hello:World", result);
 }

 @Test
 public void testJoinWithObjectArray() {
 String result = joiner.join(new Object[]{"Hello", "World"});
 assertEquals("Hello:World", result);
 }

 @Test
 public void testJoinWithNullFirst() {
 String result = joiner.join(new Object[]{null, "World"});
 assertEquals("null:World", result);
 }

 @Test
 public void testJoinWithNullSecond() {
 String result = joiner.join(new Object[]{"Hello", null});
 assertEquals("Hello:", result);
 }

 @Test
 public void testJoinWithNullAndBoth() {
 String result = joiner.join(new Object[]{null, null});
 assertEquals("null:", result);
 }

 @Test
 public void testUseForNull() {
 Joiner joiner = joiner.useForNull("NULL");
 Iterator<Object> parts = Iterators.asIterable(new Object[]{null, "World"}).iterator();
 StringBuilder builder = new StringBuilder();
 joiner.appendTo(builder, parts);
 assertEquals("NULL:World", builder.toString());
 }

 @Test
 public void testJoinWithMap() {
 Map<String, String> map = new HashMap<>();
 map.put("Key1", "Value1");
 map.put("Key2", "Value2");
 String result = mapJoiner.join(map);
 assertEquals("Key1 = Value1:Key2 = Value2", result);
 }

 @Test
 public void testJoinWithEmptyMap() {
 Map<String, String> map = new HashMap<>();
 String result = mapJoiner.join(map);
 assertEquals("", result);
 }

 @Test
 public void testJoinWithNullMap() {
 assertThrows(NullPointerException.class, () -> mapJoiner.join(null));
 }

 @Test
 public void testJoinWithNullValue() {
 Map<String, Object> map = new HashMap<>();
 map.put("Key", null);
 String result = mapJoiner.join(map);
 assertEquals("Key = :", result);
 }

 @Test
 public void testIterableWithNull() {
 Iterable<Object> parts = () -> new Object[]{null};
 String result = joiner.join(parts);
 assertEquals("null", result);
 }

 @Test(expected = NoSuchElementException.class)
 public void testIteratorWithEmpty() {
 Iterator<Object> parts = Iterators.emptyIterator();
 String result = joiner.join(parts);
 }

 @Test(expected = NoSuchElementException.class)
 public void testIteratorWithNoSuchElement() {
 Iterator<Object> parts = Iterators.singletonIterator(new Object());
 parts.next();
 parts.remove();
 String result = joiner.join(parts);
 }

 @Test(expected = NullPointerException.class)
 public void testUseForNullWithNull() {
 Joiner joiner = joiner.useForNull(null);
 Iterator<Object> parts = Iterators.singletonIterator(null);
 StringBuilder builder = new StringBuilder();
 joiner.appendTo(builder, parts);
 }
}