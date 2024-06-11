package com.google.common.base;

import com.google.common.base.Joiner;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.StringWriter;
import java.util.*;

public class JoinerTest {

 private Joiner stringJoiner;
 private Joiner charJoiner;

 @Before
 public void setUp() {
 stringJoiner = Joiner.on(",");
 charJoiner = Joiner.on('-');
 }

 @Test
 public void testAppendToIterableString() throws IOException {
 StringWriter stringWriter = new StringWriter();
 stringJoiner.appendTo(stringWriter, Arrays.asList("apple", "banana", "cherry"));
 assertEquals("apple,banana,cherry", stringWriter.toString());
 }

 @Test
 public void testAppendToIterableChar() throws IOException {
 StringWriter stringWriter = new StringWriter();
 charJoiner.appendTo(stringWriter, Arrays.asList('a', 'b', 'c'));
 assertEquals("a-b-c", stringWriter.toString());
 }

 @Test
 public void testAppendToObjectArray() throws IOException {
 StringWriter stringWriter = new StringWriter();
 stringJoiner.appendTo(stringWriter, new Object[]{"apple", "banana", "cherry"});
 assertEquals("apple,banana,cherry", stringWriter.toString());
 }

 @Test
 public void testAppendToObjectArrayCharSeparator() throws IOException {
 StringWriter stringWriter = new StringWriter();
 charJoiner.appendTo(stringWriter, new Object[]{'a', 'b', 'c'});
 assertEquals("a-b-c", stringWriter.toString());
 }

 @Test
 public void testAppendToObjectArrayNull() throws IOException {
 StringWriter stringWriter = new StringWriter();
 stringJoiner.appendTo(stringWriter, new Object[]{null, "banana", "cherry"});
 assertEquals("null,banana,cherry", stringWriter.toString());
 }

 @Test
 public void testAppendToObjectArrayNullSeparator() throws IOException {
 StringWriter stringWriter = new StringWriter();
 charJoiner.appendTo(stringWriter, new Object[]{null, "banana", "cherry"});
 assertEquals("nullbananacherry", stringWriter.toString());
 }

 @Test
 public void testAppendToIteratorString() throws IOException {
 StringWriter stringWriter = new StringWriter();
 Iterator<String> iterator = Arrays.asList("apple", "banana", "cherry").iterator();
 stringJoiner.appendTo(stringWriter, iterator);
 assertEquals("apple,banana,cherry", stringWriter.toString());
 }

 @Test
 public void testAppendToIteratorChar() throws IOException {
 StringWriter stringWriter = new StringWriter();
 Iterator<Character> iterator = Arrays.asList('a', 'b', 'c').iterator();
 charJoiner.appendTo(stringWriter, iterator);
 assertEquals("a-b-c", stringWriter.toString());
 }

 @Test
 public void testAppendToObjectNullFirst() throws IOException {
 StringWriter stringWriter = new StringWriter();
 stringJoiner.appendTo(stringWriter, null, "banana", "cherry");
 assertEquals("banana,cherry", stringWriter.toString());
 }

 @Test
 public void testAppendToObjectNullSecond() throws IOException {
 StringWriter stringWriter = new StringWriter();
 stringJoiner.appendTo(stringWriter, "apple", null, "cherry");
 assertEquals("apple,cherry", stringWriter.toString());
 }

 @Test
 public void testAppendToObjectNullThird() throws IOException {
 StringWriter stringWriter = new StringWriter();
 stringJoiner.appendTo(stringWriter, "apple", "banana", null);
 assertEquals("apple,banana", stringWriter.toString());
 }

 @Test(expected = IOException.class)
 public void testAppendToThrowsIOException() throws IOException {
 StringWriter stringWriter = new StringWriter();stringJoiner.appendTo(stringWriter, Arrays.asList("apple", "banana", "cherry"), null);
 fail("IOException expected but not thrown");
 }

 @Test(expected = NullPointerException.class)
 public void testToStringNullIterator() {
 try {
 stringJoiner.toString(null);
 fail("NullPointerException expected but not thrown");
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testJoinIterableString() {
 assertEquals("apple,banana,cherry", stringJoiner.join(Arrays.asList("apple", "banana", "cherry")));
 }

 @Test
 public void testJoinIterableChar() {
 assertEquals("a-b-c", charJoiner.join(Arrays.asList('a', 'b', 'c')));
 }

 @Test
 public void testJoinObjectArrayStringSeparator() {
 assertEquals("apple,banana,cherry", stringJoiner.join(new Object[]{"apple", "banana", "cherry"}));
 }

 @Test
 public void testJoinObjectArrayCharSeparator() {
 assertEquals("a-b-c", charJoiner.join(new Object[]{'a', 'b', 'c'}));
 }

 @Test
 public void testJoinObjectArrayNull() {
 assertEquals("null,banana,cherry", stringJoiner.join(new Object[]{null, "banana", "cherry"}));
 }

 @Test
 public void testJoinObjectArrayNullSeparator() {
 assertEquals("nullbananacherry", stringJoiner.join(new Object[]{null, "banana", "cherry"}));
 }

 @Test
 public void testJoinIteratorString() {
 assertEquals("apple,banana,cherry", stringJoiner.join(Arrays.asList("apple", "banana", "cherry").iterator()));
 }

 @Test
 public void testJoinIteratorChar() {
 assertEquals("a-b-c", charJoiner.join(Arrays.asList('a', 'b', 'c').iterator()));
 }

 @Test
 public void testJoinObjectArrayNullFirst() {
 assertEquals("banana,cherry", stringJoiner.join(new Object[]{null, "banana", "cherry"}));
 }

 @Test
 public void testJoinObjectArrayNullSecond() {
 assertEquals("apple,cherry", stringJoiner.join(new Object[]{"apple", null, "cherry"}));
 }

 @Test
 public void testJoinObjectArrayNullThird() {
 assertEquals("apple,banana", stringJoiner.join(new Object[]{"apple", "banana", null}));
 }

 @Test
 public void testUseForNull() {
 Joiner joiner = stringJoiner.useForNull("NA");
 assertEquals("apple,NA,banana,NA,cherry", joiner.join(Arrays.asList("apple", null, "banana", null, "cherry")));
 }

 @Test
 public void testSkipNulls() {
 Joiner joiner = stringJoiner.skipNulls();
 assertEquals("apple,banana,cherry", joiner.join(Arrays.asList("apple", null, "banana", null, "cherry")));
 }
}