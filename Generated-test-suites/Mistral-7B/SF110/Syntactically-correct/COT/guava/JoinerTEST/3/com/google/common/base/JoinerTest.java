package com.google.common.base;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;
import org.junit.annotations.*;

@RunWith(Parameterized.class)
public class JoinerTest<T> {

 private Joiner joiner;
 private String separator;

 @Parameters(name = "{index}: Joiner with separator '{0}'")
 public static Iterable<Object[]> data() {
 return Arrays.asList(new Object[][] {
 {"", new Joiner("")},
 {"-", new Joiner("-")},
 {"|", new Joiner("|")}
 });
 }

 @Parameters(name = "{index}: Joiner with character separator '{0}'")
 @RunWith(value = Parameterized.class)
 public JoinerTest(String separator) {
 this.separator = separator;
 joiner = Joiner.on(separator);
 }

 @Test
 public void testAppendTo_Iterable() throws IOException {
 Iterable<String> list = Arrays.asList("one", "two", "three");
 StringBuilder expected = new StringBuilder();
 expected.append("one").append(separator).append("two").append(separator).append("three");
 StringBuilder actual = new StringBuilder();
 joiner.appendTo(actual, list);
 assertEquals(expected.toString(), actual.toString());
 }

 @Test(expected = IOException.class)
 public void testAppendTo_Iterable_IOException() throws IOException {
 StringWriter writer = new StringWriter();
 Joiner joiner = new Joiner("") {
 @Override
 public <A extends Appendable> A appendTo(A appendable, Iterator<?> parts) throws IOException {
 throw new IOException();
 }
 };
 Iterable<String> list = Arrays.asList("one", "two", "three");
 joiner.appendTo(writer, list);
 }

 @Test
 public void testAppendTo_ObjectArray() throws IOException {
 Object[] array = new Object[] {"one", "two", "three"};
 StringBuilder expected = new StringBuilder();
 expected.append("one").append(separator).append("two").append(separator).append("three");
 StringBuilder actual = new StringBuilder();
 joiner.appendTo(actual, array);
 assertEquals(expected.toString(), actual.toString());
 }

 @Test(expected = IOException.class)
 public void testAppendTo_ObjectArray_IOException() throws IOException {
 StringWriter writer = new StringWriter();
 Joiner joiner = new Joiner("") {
 @Override
 public <A extends Appendable> A appendTo(A appendable, Object[] parts) throws IOException {
 throw new IOException();
 }
 };
 Object[] array = new Object[] {"one", "two", "three"};
 joiner.appendTo(writer, array);
 }

 @Test
 public void testAppendTo_NullFirst_Null() throws IOException {
 StringBuilder actual = new StringBuilder();
 joiner.appendTo(actual, (Iterable<?>)null, (Object)null, "two", "three");
 assertEquals("", actual.toString());
 }

 @Test
 public void testAppendTo_NullFirst_String() throws IOException {
 StringBuilder actual = new StringBuilder();
 joiner.appendTo(actual, (Iterable<?>)null, "two", "three");
 assertEquals("", actual.toString());
 }

 @Test
 public void testAppendTo_NullMid_String() throws IOException {
 StringBuilder actual = new StringBuilder();
 joiner.appendTo(actual, "one", (Object)null, "three");
 assertEquals("one" + separator + "three", actual.toString());
 }

 @Test
 public void testAppendTo_NullLast_String() throws IOException {
 StringBuilder actual = new StringBuilder();
 joiner.appendTo(actual, "one", "two", (Object)null);
 assertEquals("one" + separator + "two", actual.toString());
 }

 @Test
 public void testAppendTo_NullFirst_Char() throws IOException {
 StringBuilder actual = new StringBuilder();
 joiner.appendTo(actual, (Iterable<?>)null, (char)0, "two", "three");
 assertEquals("", actual.toString());
 }

 @Test
 public void testAppendTo_NullFirst_CharSeparator() throws IOException {
 StringBuilder actual = new StringBuilder();
 joiner.appendTo(actual, (Iterable<?>)null, (char)0, "two", "three");
 assertEquals("", actual.toString());
 }

 @Test
 public void testAppendTo_NullMid_Char() throws IOException {
 StringBuilder actual = new StringBuilder();
 joiner.appendTo(actual, "one", (char)0, "three");
 assertEquals("one" + separator + "three", actual.toString());
 }

 @Test
 public void testAppendTo_NullLast_Char() throws IOException {
 StringBuilder actual = new StringBuilder();
 joiner.appendTo(actual, "one", "two", (char)0);
 assertEquals("one" + separator + "two", actual.toString());
 }

 @Test
 public void testJoin_Iterable() {
 String result = joiner.join(Arrays.asList("one", "two", "three"));
 assertEquals("one" + separator + "two" + separator + "three", result);
 }

 @Test
 public void testJoin_ObjectArray() {
 String result = joiner.join(new Object[] {"one", "two", "three"});
 assertEquals("one" + separator + "two" + separator + "three", result);
 }

 @Test
 public void testJoin_NullFirst() {
 String result = joiner.join(new Object[] {null, "two", "three"});
 assertEquals("", result);
 }

 @Test
 public void testJoin_NullMid() {
 String result = joiner.join(new Object[] {"one", null, "three"});
 assertEquals("one" + separator + "three", result);
 }

 @Test
 public void testJoin_NullLast() {
 String result = joiner.join(new Object[] {"one", "two", null});
 assertEquals("one" + separator + "two", result);
 }

 @Test
 public void testJoin_EmptyList() {
 String result = joiner.join(Collections.emptyList());
 assertEquals("", result);
 }

 @Test
 public void testJoin_EmptyArray() {
 String result = joiner.join(new Object[0]);
 assertEquals("", result);
 }

 @Test
 public void testUseForNull() {
 Joiner joiner = Joiner.on("").useForNull("");
 String result = joiner.join(Arrays.asList("one", null, "three"));
 assertEquals("one" + separator + "" + separator + "three", result);
 }

 @Test
 public void testUseForNull_CharSeparator() {
 Joiner joiner = Joiner.on("").useForNull("");
 String result = joiner.join(Arrays.asList("one", null, "three"));
 assertEquals("one" + separator + "" + separator + "three", result);
 }

 @Test
 public void testSkipNulls() {
 Joiner joiner = Joiner.on("").skipNulls();
 String result = joiner.join(Arrays.asList("one", null, "two", null, "three"));
 assertEquals("one" + separator + "two" + separator + "three", result);
 }

 @Test
 public void testWithKeyValueSeparator() {
 Joiner joiner = Joiner.on("=").withKeyValueSeparator("=");
 Map<String, String> map = new HashMap<>();
 map.put("key1", "value1");
 map.put("key2", "value2");
 map.put("key3", "value3");
 String result = joiner.join(map.entrySet());
 assertEquals("key1=" + separator + "value1" + separator + "key2=" + separator + "value2" + separator + "key3=" + separator + "value3", result);
 }
}