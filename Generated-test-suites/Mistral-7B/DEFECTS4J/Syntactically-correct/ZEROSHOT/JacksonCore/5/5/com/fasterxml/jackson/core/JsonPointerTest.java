package com.fasterxml.jackson.core.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonPointer;

import org.junit.Test;

public class JsonPointerTest {

 private static JsonPointer createPointer(String input) {
 try {
 return JsonPointer.compile(input);
 } catch (IllegalArgumentException e) {
 fail("Failed to create pointer from input: " + input);
 }
 return null;
 }

 @Test
 public void testEmptyPointer() {
 JsonPointer pointer = JsonPointer.EMPTY;
 assertTrue(pointer.matches());
 assertNull(pointer.getMatchingProperty());
 assertFalse(pointer.mayMatchProperty());
 assertFalse(pointer.mayMatchElement());
 assertNull(pointer.tail());
 assertEquals("", pointer.toString());
 }

 @Test
 public void testBasicPointer() {
 String input = "/foo/bar/baz";
 JsonPointer pointer = createPointer(input);
 assertFalse(pointer.matches());
 assertEquals("foo", pointer.getMatchingProperty());
 assertEquals(0, pointer.getMatchingIndex());
 assertNull(pointer.tail());
 assertEquals(input, pointer.toString());
 }

 @Test
 public void testBasicPointerWithMatchingElement() {
 String input = "/foo/0/bar";
 JsonPointer pointer = createPointer(input);
 assertFalse(pointer.matches());
 assertEquals("foo", pointer.getMatchingProperty());
 assertEquals(0, pointer.getMatchingIndex());
 assertNull(pointer.tail());
 assertEquals(input, pointer.toString());
 }

 @Test
 public void testBasicPointerWithTrailingSlash() {
 String input = "/foo/bar/";
 JsonPointer pointer = createPointer(input);
 assertFalse(pointer.matches());
 assertEquals("bar", pointer.getMatchingProperty());
 assertEquals(-1, pointer.getMatchingIndex());
 assertNull(pointer.tail());
 assertEquals(input, pointer.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidInput() {
 String input = "~/foo";
 createPointer(input);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidInputWithSlash() {
 String input = "/~/foo";
 createPointer(input);
 }

 @Test
 public void testParseQuotedTail() {
 String input = "~1~2/quux";
 JsonPointer pointer = createPointer(input);
 assertFalse(pointer.matches());
 assertEquals("quux", pointer.getMatchingProperty());
 assertEquals(-1, pointer.getMatchingIndex());
 assertNull(pointer.tail());
 assertEquals(input, pointer.toString());
 }

 @Test
 public void testParseWithQuotedCharacter() {
 String input = "~/foo/bar/~1/baz";
 JsonPointer pointer = createPointer(input);
 assertFalse(pointer.matches());
 assertEquals("foo", pointer.getMatchingProperty());
 assertEquals(-1, pointer.getMatchingIndex());
 assertNull(pointer.tail());
 assertEquals(input, pointer.toString());
 }

 @Test
 public void testParseWithQuotedCharacterInPosition() {
 String input = "~0/foo/bar/~1/baz";
 JsonPointer pointer = createPointer(input);
 assertFalse(pointer.matches());
 assertEquals("foo", pointer.getMatchingProperty());
 assertEquals(0, pointer.getMatchingIndex());
 assertNull(pointer.tail());
 assertEquals(input, pointer.toString());
 }

 @Test
 public void testParseWithDoubleQuotes() {
 String input = "~/foo/bar/\"quux\"";
 JsonPointer pointer = createPointer(input);
 assertFalse(pointer.matches());
 assertEquals("quux", pointer.getMatchingProperty());
 assertEquals(-1, pointer.getMatchingIndex());
 assertNull(pointer.tail());
 assertEquals(input, pointer.toString());
 }

 @Test
 public void testParseWithDoubleQuotesInPosition() {
 String input = "~0/foo/bar/\"quux\"";
 JsonPointer pointer = createPointer(input);
 assertFalse(pointer.matches());
 assertEquals("quux", pointer.getMatchingProperty());
 assertEquals(0, pointer.getMatchingIndex());
 assertNull(pointer.tail());
 assertEquals(input, pointer.toString());
 }

 @Test
 public void testParseWithSlashInPosition() {
 String input = "~0/foo/bar//baz";
 JsonPointer pointer = createPointer(input);
 assertFalse(pointer.matches());
 assertEquals("baz", pointer.getMatchingProperty());
 assertEquals(-1, pointer.getMatchingIndex());
 assertNull(pointer.tail());
 assertEquals(input, pointer.toString());
 }

 @Test
 public void testParseComplexPointer() {
 String input = "/foo/0/bar[1]/qux[2]/baz[3]/quux[-1]";
 JsonPointer pointer = createPointer(input);
 assertFalse(pointer.matches());
 assertEquals("quux", pointer.getMatchingProperty());
 assertEquals(-1, pointer.getMatchingIndex());
 JsonPointer tail = pointer.tail();
 assertFalse(tail.matches());
 assertEquals("baz", tail.getMatchingProperty());
 assertEquals(3, tail.getMatchingIndex());
 assertNull(tail.tail());
 assertEquals(input, pointer.toString());
 }

 @Test
 public void testParseComplexPointerWithEmptySegment() {
 String input = "/foo/0/bar[]/qux[2]/baz[3]/quux[-1]";
 JsonPointer pointer = createPointer(input);
 assertFalse(pointer.matches());
 assertEquals("quux", pointer.getMatchingProperty());
 assertEquals(-1, pointer.getMatchingIndex());
 JsonPointer tail = pointer.tail();
 assertFalse(tail.matches());
 assertEquals("baz", tail.getMatchingProperty());
 assertEquals(3, tail.getMatchingIndex());
 JsonPointer nextTail = tail.tail();
 assertNull(nextTail);
 assertEquals(input, pointer.toString());
 }

 @Test
 public void testParseComplexPointerWithEmptySegmentAtEnd() {
 String input = "/foo/0/bar[]/qux[2]/baz[3]";
 JsonPointer pointer = createPointer(input);
 assertFalse(pointer.matches());
 assertEquals("baz", pointer.getMatchingProperty());
 assertEquals(3, pointer.getMatchingIndex());
 JsonPointer tail = pointer.tail();
 assertNull(tail);
 assertEquals(input, pointer.toString());
 }
}