package com.fasterxml.jackson.core.test;

 import static org.junit.Assert.assertEquals;
 import static org.junit.Assert.assertNull;
 import static org.junit.Assert.assertSame;

 import com.fasterxml.jackson.core.JsonPointer;

 import org.junit.Test;

 /**
 * JUnit test file for JsonPointer class.
 */
 public class JsonPointerTest {

 @Test
 public void testEmptyPointer() {
 JsonPointer empty = JsonPointer.EMPTY;
 assertEquals("", empty.toString());
 assertEquals(-1, empty.getMatchingIndex());
 assertNull(empty.getMatchingProperty());
 assertNull(empty.tail());
 }

 @Test
 public void testBasicPointerConstruction() {
 String pointerString = "/a/b/c";
 JsonPointer pointer = JsonPointer.compile(pointerString);
 assertEquals(pointerString, pointer.toString());
 assertNull(pointer.tail());
 assertEquals("a", pointer.getMatchingProperty());
 assertEquals(-1, pointer.getMatchingIndex());
 }

 @Test
 public void testBasicPointerMatches() {
 String pointerString = "/a/b/c";
 JsonPointer pointer = JsonPointer.compile(pointerString);
 JsonPointer subPointer = JsonPointer.compile("/a/b");
 assertEquals(pointer, subPointer.tail());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidPointer() {
 String pointerString = "a/b";
 JsonPointer pointer = null;
 try {
 pointer = JsonPointer.compile(pointerString);
 } catch (IllegalArgumentException e) {
 pointer = e.getCause();
 }
 assertEquals("Invalid input: JSON Pointer expression must start with '/': \"a/b\"", pointer.getMessage());
 }

 @Test
 public void testMatchingProperty() {
 String pointerString = "/a/b/c";
 JsonPointer pointer = JsonPointer.compile(pointerString);
 assertEquals("b", pointer.matchProperty("b").getMatchingProperty());
 assertEquals("c", pointer.matchProperty("c").getMatchingProperty());
 assertNull(pointer.matchProperty("d").getMatchingProperty());
 }

 @Test
 public void testMatchingElement() {
 String pointerString = "/a/0/c";
 JsonPointer pointer = JsonPointer.compile(pointerString);
 assertEquals(0, pointer.getMatchingIndex());
 assertEquals("c", pointer.matchElement(0).getMatchingProperty());
 assertNull(pointer.matchElement(1));
 }

 @Test
 public void testCompositePointerConstruction() {
 String pointerString = "/a/0/b/1";
 JsonPointer pointer = JsonPointer.compile(pointerString);
 JsonPointer subPointer1 = JsonPointer.compile("/a/0");
 JsonPointer subPointer2 = JsonPointer.compile("/b/1");
 assertEquals(pointer, subPointer1.tail().tail().tail().tail());
 assertSame(subPointer1, pointer.tail().tail());
 assertSame(subPointer2, pointer.tail().tail().tail());
 }

 @Test
 public void testCompositePointerMatches() {
 String pointerString = "/a/0/b/1";
 JsonPointer pointer = JsonPointer.compile(pointerString);
 JsonPointer subPointer1 = JsonPointer.compile("/a/0");
 JsonPointer subPointer2 = JsonPointer.compile("/b/1");
 assertEquals(pointer, subPointer1.tail().tail().tail());
 assertEquals(subPointer1, pointer.tail().tail());
 assertEquals(subPointer2, pointer.tail().tail().tail());
 }
 }