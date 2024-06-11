package com.fasterxml.jackson.core.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonPointer;

import org.junit.Test;

public class JsonPointerTest {

 @Test
 public void testEmptyPointer() {
 JsonPointer emptyPointer = JsonPointer.EMPTY;
 assertTrue(emptyPointer.matches());
 assertNull(emptyPointer.getMatchingProperty());
 assertEquals(-1, emptyPointer.getMatchingIndex());
 assertFalse(emptyPointer.mayMatchProperty());
 assertFalse(emptyPointer.mayMatchElement());
 assertEquals("", emptyPointer.toString());
 }

 @Test
 public void testBasicPointerConstruction() {
 String input = "/foo/bar/baz";
 JsonPointer pointer = JsonPointer.compile(input);
 assertTrue(pointer.matches());
 assertEquals("foo", pointer.getMatchingProperty());
 assertEquals(-1, pointer.getMatchingIndex());
 assertFalse(pointer.mayMatchProperty());
 assertFalse(pointer.mayMatchElement());
 assertEquals(input, pointer.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidInput() {
 String input = "foo";
 JsonPointer pointer = null;
 try {
 pointer = JsonPointer.compile(input);
 } catch (IllegalArgumentException e) {
 pointer = e;
 }
 assertNotNull(pointer);
 assertEquals("Invalid input: JSON Pointer expression must start with '/': \"foo\"", pointer.getMessage());
 }

 @Test
 public void testMatchProperty() {
 String input = "/foo/bar/baz";
 JsonPointer pointer = JsonPointer.compile(input);
 JsonPointer matchProperty = pointer.matchProperty("bar");
 assertTrue(matchProperty.matches());
 assertEquals("bar", matchProperty.getMatchingProperty());
 assertEquals(-1, matchProperty.getMatchingIndex());
 assertFalse(matchProperty.mayMatchProperty());
 assertFalse(matchProperty.mayMatchElement());
 assertEquals("/foo/bar", matchProperty.toString());
 }

 @Test
 public void testMatchElement() {
 String input = "[1,2,3]/1";
 JsonPointer pointer = JsonPointer.compile(input);
 JsonPointer matchElement = pointer.matchElement(1);
 assertTrue(matchElement.matches());
 assertEquals("", matchElement.getMatchingProperty());
 assertEquals(1, matchElement.getMatchingIndex());
 assertFalse(matchElement.mayMatchProperty());
 assertFalse(matchElement.mayMatchElement());
 assertEquals("[1]", matchElement.toString());
 }

 @Test
 public void testTail() {
 String input = "/foo/bar/baz";
 JsonPointer pointer = JsonPointer.compile(input);
 JsonPointer tail = pointer.tail();
 assertEquals("/bar/baz", tail.toString());
 }

 @Test
 public void testToString() {
 String input = "/foo/bar/baz";
 JsonPointer pointer = JsonPointer.compile(input);
 assertEquals(input, pointer.toString());
 }

 @Test
 public void testEquals() {
 String input1 = "/foo/bar/baz";
 String input2 = "/foo/bar/baz";
 JsonPointer pointer1 = JsonPointer.compile(input1);
 JsonPointer pointer2 = JsonPointer.compile(input2);
 assertTrue(pointer1.equals(pointer2));
 }

 @Test
 public void testHashCode() {
 String input1 = "/foo/bar/baz";
 String input2 = "/foo/bar/qux";
 JsonPointer pointer1 = JsonPointer.compile(input1);
 JsonPointer pointer2 = JsonPointer.compile(input2);
 int hashCode1 = pointer1.hashCode();
 int hashCode2 = pointer2.hashCode();
 assertNotEquals(hashCode1, hashCode2);
 }
}