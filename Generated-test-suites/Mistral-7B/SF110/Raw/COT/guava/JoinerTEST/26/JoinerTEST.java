package com.example.junit; // replace with your package name

import static org.junit.Assert.*;

import java.util.*;
import com.google.common.base.*;

public class JoinerTest {

 private Joiner joinerWithStringSeparator;
 private Joiner joinerWithCharSeparator;
 private Joiner joinerWithNullText;
 private MapJoiner mapJoiner;

 @Before
 public void setUp() {
 joinerWithStringSeparator = Joiner.on("|");
 joinerWithCharSeparator = Joiner.on('-');
 joinerWithNullText = joinerWithStringSeparator.useForNull("N/A");
 mapJoiner = joinerWithStringSeparator.withKeyValueSeparator(":");
 }

 @Test
 public void testJoinIterableString() throws IOException {
 List<String> list = Arrays.asList("apple", "banana", "cherry");
 StringBuilder expected = new StringBuilder("apple|banana|cherry");
 StringBuilder actual = joinerWithStringSeparator.appendTo(new StringBuilder(), list);
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testJoinIterableChar() throws IOException {
 char[] arr = {'a', 'b', 'c'};
 StringBuilder expected = new StringBuilder("abc");
 StringBuilder actual = joinerWithCharSeparator.appendTo(new StringBuilder(), Arrays.asList(arr));
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testJoinObjectArrayString() throws IOException {
 Object[] arr = {"apple", "banana", "cherry"};
 StringBuilder expected = new StringBuilder("apple|banana|cherry");
 StringBuilder actual = joinerWithStringSeparator.appendTo(new StringBuilder(), arr);
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testJoinObjectArrayChar() throws IOException {
 Object[] arr = {'a', 'b', 'c'};
 StringBuilder expected = new StringBuilder("abc");
 StringBuilder actual = joinerWithCharSeparator.appendTo(new StringBuilder(), arr);
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testJoinObjectNull() throws IOException {
 Object[] arr = {"apple", null, "cherry"};
 StringBuilder expected = new StringBuilder("apple|N/A|cherry");
 StringBuilder actual = joinerWithNullText.appendTo(new StringBuilder(), arr);
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testJoinMap() throws IOException {
 Map<String, String> map = new HashMap<>();
 map.put("key1", "value1");
 map.put("key2", "value2");
 StringBuilder expected = new StringBuilder("key1:value1|key2:value2");
 StringBuilder actual = mapJoiner.appendTo(new StringBuilder(), map);
 assertEquals(expected.toString(), actual.toString());
 }

 @Test(expected = IOException.class)
 public void testIOExceptionInAppendTo() throws IOException {
 StringBuilder appendable = new StringBuilder();
 Iterator<String> parts = Arrays.asList("apple", "banana", "cherry").iterator();
 joinerWithStringSeparator.appendTo(appendable, parts);
 fail("IOException should be thrown in appendTo method");
 }

 @Test(expected = NullPointerException.class)
 public void testNullPointerExceptionInAppendTo() throws IOException {
 StringBuilder appendable = null;
 Iterator<String> parts = Arrays.asList("apple", "banana", "cherry").iterator();
 joinerWithStringSeparator.appendTo(appendable, parts);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIllegalArgumentExceptionInJoinIterableNull() {
 Iterator<String> parts = null;
 joinerWithStringSeparator.join(parts);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIllegalArgumentExceptionInJoinObjectArrayNull() {
 Object[] parts = {"apple", null, "cherry"};
 joinerWithStringSeparator.join(parts);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIllegalArgumentExceptionInJoinMapNull() {
 Map<String, String> map = null;
 mapJoiner.join(map);
 }

}