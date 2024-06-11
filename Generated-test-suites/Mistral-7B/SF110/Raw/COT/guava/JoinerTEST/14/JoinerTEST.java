package com.example.junit; // replace with your test package name if necessary

import static org.junit.Assert.*;
import java.util.*;

import com.google.common.base.Joiner;
import com.google.common.base.MapJoiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class JoinerTest {

 private Joiner joinerWithComma;
 private Joiner joinerWithEmptyStringSeparator;
 private Joiner joinerWithCustomSeparator;
 private MapJoiner mapJoinerWithComma;
 private MapJoiner mapJoinerWithCustomSeparator;

 @BeforeClass
 public static void setupJoinerInstances() {
 joinerWithComma = Joiner.on(",");
 joinerWithEmptyStringSeparator = Joiner.on("");
 joinerWithCustomSeparator = Joiner.on("|");
 mapJoinerWithComma = new Joiner("").withKeyValueSeparator(",");
 mapJoinerWithCustomSeparator = new Joiner("").withKeyValueSeparator(":");
 }

 @Test
 public void testOnMethodWithStringSeparator() {
 Joiner joiner = Joiner.on(".");
 assertEquals("", joiner.toString(null));
 assertEquals("one", joiner.toString(new Object(){}));
 assertEquals("one.two.three", joiner.join(Arrays.asList(new Object[]{"one", "two", "three"})));
 assertEquals("one", joiner.join(Arrays.asList(new Object[]{"one"})));
 assertEquals("", joiner.join(Collections.emptyList()));
 }

 @Test
 public void testOnMethodWithCharSeparator() {
 Joiner joiner = Joiner.on(';');
 assertEquals("", joiner.toString(null));
 assertEquals("one", joiner.toString(new Object(){}));
 assertEquals("one;two;three", joiner.join(Arrays.asList(new Object[]{"one", "two", "three"})));
 assertEquals("one", joiner.join(Arrays.asList(new Object[]{"one"})));
 assertEquals("", joiner.join(Collections.emptyList()));
 }

 @Test
 public void testAppendToMethodWithIterable() {
 StringBuilder appendable = new StringBuilder();
 Joiner joiner = joinerWithComma;
 joiner.appendTo(appendable, Arrays.asList(new Object[]{"one", "two", "three"}));
 assertEquals("one,two,three", appendable.toString());
 }

 @Test
 public void testAppendToMethodWithIterator() {
 StringBuilder appendable = new StringBuilder();
 Joiner joiner = joinerWithComma;
 Iterator<Object> iterator = Arrays.asList(new Object[]{"one", "two", "three"}).iterator();
 joiner.appendTo(appendable, iterator);
 assertEquals("one,two,three", appendable.toString());
 }

 @Test
 public void testAppendToMethodWithObjectArray() {
 StringBuilder appendable = new StringBuilder();
 Joiner joiner = joinerWithComma;
 joiner.appendTo(appendable, new Object[]{"one", "two", "three"});
 assertEquals("one,two,three", appendable.toString());
 }

 @Test
 public void testAppendToMethodWithNullFirstAndSecondAndEmptyArray() {
 StringBuilder appendable = new StringBuilder();
 Joiner joiner = joinerWithComma;
 joiner.appendTo(appendable, null, null, new Object[0]);
 assertEquals("", appendable.toString());
 }

 @Test
 public void testAppendToMethodWithNullFirstAndSecondButNotEmptyArray() {
 StringBuilder appendable = new StringBuilder();
 Joiner joiner = joinerWithComma;
 joiner.appendTo(appendable, null, "two", new Object[]{"three", "four"});
 assertEquals("two,three,four", appendable.toString());
 }

 @Test
 public void testAppendToMethodWithNullIterable() {
 StringBuilder appendable = new StringBuilder();
 Joiner joiner = joinerWithComma;
 joiner.appendTo(appendable, null);
 assertEquals("", appendable.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAppendToMethodWithNullAppendable() {
 Joiner joiner = joinerWithComma;
 joiner.appendTo(null, Arrays.asList(new Object[]{"one", "two", "three"}));
 }

 @Test
 public void testJoinMethodWithIterable() {
 assertEquals("one,two,three", joinerWithComma.join(Arrays.asList(new Object[]{"one", "two", "three"})));
 }

 @Test
 public void testJoinMethodWithIterator() {
 assertEquals("one,two,three", joinerWithComma.join(Arrays.asList(new Object[]{"one", "two", "three"}).iterator()));
 }

 @Test
 public void testJoinMethodWithObjectArray() {
 assertEquals("one,two,three", joinerWithComma.join(new Object[]{"one", "two", "three"}));
 }

 @Test
 public void testJoinMethodWithNull() {
 assertEquals("", joinerWithComma.join(null));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJoinMethodWithEmptyIterable() {
 assertEquals("", joinerWithComma.join(Collections.emptyList()));
 }

 @Test
 public void testUseForNullMethod() {
 Joiner joiner = joinerWithComma.useForNull("NA");
 assertEquals("NA,one,NA,two,NA,three,NA", joiner.join(Arrays.asList(new Object[]{"one", "two", "three"})));
 }

 @Test
 public void testSkipNullsMethod() {
 Joiner joiner = joinerWithComma.skipNulls();
 assertEquals("one,two,three", joiner.join(Arrays.asList(new Object[]{"one", null, "two", "three"})));
 }

 @Test
 public void testWithKeyValueSeparatorMethod() {
 Map<String, String> map = Maps.newHashMap();
 map.put("key1", "value1");
 map.put("key2", null);
 map.put("key3", "value3");

 assertEquals("key1=" + joinerWithComma.toString("value1") + ",key2=,key3=" + joinerWithComma.toString("value3"), mapJoinerWithComma.join(map));
 }

 @Test
 public void testWithKeyValueSeparatorMethodCustomSeparator() {
 Map<String, String> map = Maps.newHashMap();
 map.put("key1", "value1");
 map.put("key2", null);
 map.put("key3", "value3");

 assertEquals("key1=" + joinerWithCustomSeparator.toString("value1") + ":" + joinerWithCustomSeparator.toString("value3"), mapJoinerWithCustomSeparator.join(map));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testWithKeyValueSeparatorMethodNullMap() {
 mapJoinerWithComma.join(null);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testUseForNullAndSkipNullsTogether() {
 Joiner joiner = joinerWithComma.useForNull("NA").skipNulls();
 joiner.appendTo(new StringBuilder(), Arrays.asList(new Object[]{"one", null, "two", null}));
 fail("UseForNull and SkipNulls can't be used together");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testWithKeyValueSeparatorAndSkipNullsTogether() {
 Joiner joiner = mapJoinerWithComma.withKeyValueSeparator(",").skipNulls();
 joiner.appendTo(new StringBuilder(), Maps.newHashMap(Collections.singletonMap(null, "value")).entrySet());
 fail("WithKeyValueSeparator and SkipNulls can't be used together");
 }
}