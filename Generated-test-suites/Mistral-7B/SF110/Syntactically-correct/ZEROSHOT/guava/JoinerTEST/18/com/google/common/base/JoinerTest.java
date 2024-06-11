package com.google.common.base;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

@SuppressWarnings("unchecked")
public class JoinerTest {

 private static final Joiner JOINER = Joiner.on('-');

 @Test
 public void testJoinerCreation() {
 Joiner joiner = Joiner.on('-');
 Joiner joinerWithIterable = joiner.appendTo(new StringBuilder(), Arrays.asList("A", "B", "C"));
 Joiner joinerWithObject = joiner.appendTo(new StringBuilder(), "A", "B", "C");
 Joiner joinerWithNullText = joiner.useForNull("NA");
 Joiner joinerWithSkipNulls = joiner.skipNulls();
 Joiner joinerWithMap = joiner.withKeyValueSeparator(", ");

 assertNotNull(joiner);
 assertNotNull(joinerWithIterable);
 assertNotNull(joinerWithObject);
 assertNotNull(joinerWithNullText);
 assertNotNull(joinerWithSkipNulls);
 assertNotNull(joinerWithMap);
 }

 @Test
 public void testToString() {
 Object obj1 = new Object() {
 @Override
 public String toString() {
 return "Object1";
 }
 };
 Object obj2 = new Object() {
 @Override
 public String toString() {
 return "Object2";
 }
 };
 Object obj3 = new Object() {
 @Override
 public String toString() {
 return "Object3";
 }
 };
 Object objNull = null;

 StringBuilder builder = new StringBuilder();

 Joiner joiner = Joiner.on('-');

 assertEquals("Object1", joiner.toString(obj1));
 assertEquals("Object2", joiner.toString(obj2));
 assertEquals("", joiner.toString(objNull));

 joiner.appendTo(builder, Arrays.asList(obj1, obj2, obj3));
 assertEquals("Object1-Object2-Object3", builder.toString());

 joiner.appendTo(builder, Iterators.asList(obj1, obj2, obj3).iterator());
 assertEquals("Object1-Object2-Object3", builder.toString());

 joiner.appendTo(builder, new Object[] { obj1, obj2, obj3 });
 assertEquals("Object1-Object2-Object3", builder.toString());

 joiner.appendTo(builder, obj1, obj2, obj3);
 assertEquals("Object1-Object2-Object3", builder.toString());
 }

 @Test
 public void testAppendTo() throws IOException {
 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 StringJoiner stringJoiner = new StringJoiner("-");

 Joiner joiner = Joiner.on('-');

 joiner.appendTo(byteStream, Arrays.asList("A", "B", "C"));
 assertEquals("-A-B-C", byteStream.toString());

 joiner.appendTo(byteStream, Iterators.asList(Collections.singletonMap("A", "1"), Collections.singletonMap("B", "2"), Collections.singletonMap("C", "3")).entrySet().iterator());
 assertEquals("-A-1-B-2-C-3", byteStream.toString());

 joiner.appendTo(byteStream, new String[] { "A", "B", "C" });
 assertEquals("-A-B-C", byteStream.toString());

 joiner.appendTo(byteStream, new Object[] { "A", "B", "C" });
 assertEquals("-A-B-C", byteStream.toString());

 joiner.appendTo(byteStream, "A", "B", "C");
 assertEquals("-A-B-C", byteStream.toString());
 }

 @Test
 public void testUseForNull() {
 Joiner joiner = Joiner.on('-');
 Joiner joinerWithNullText = joiner.useForNull("NA");

 assertEquals("NA", joinerWithNullText.toString(null));
 assertEquals("Object1", joinerWithNullText.toString(new Object() {
 @Override
 public String toString() {
 return "Object1";
 }
 }));
 }

 @Test
 public void testSkipNulls() {
 Joiner joiner = Joiner.on('-');
 Joiner joinerWithSkipNulls = joiner.skipNulls();

 StringBuilder builder = new StringBuilder();
 joinerWithSkipNulls.appendTo(builder, Arrays.asList("A", null, "B", null, "C"));
 assertEquals("A-B-C", builder.toString());
 }

 @Test
 public void testMapJoiner() {
 Joiner joiner = Joiner.on('-');
 MapJoiner mapJoiner = joiner.withKeyValueSeparator(", ");

 Map<String, String> map = new HashMap<>();
 map.put("A", "1");
 map.put("B", null);
 map.put("C", "3");

 StringBuilder builder = new StringBuilder();
 mapJoiner.appendTo(builder, map);
 assertEquals("A, 1-B, null-C, 3", builder.toString());
 }

}