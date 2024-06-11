package com.fasterxml.jackson.core.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonPointer;

import org.junit.Test;

public class JsonPointerTest {

 @Test
 public void testEmptyPointer() {
 JsonPointer emptyPointer = JsonPointer.EMPTY;
 assertTrue(emptyPointer.matches());
 assertEquals("", emptyPointer.getMatchingProperty());
 assertEquals(-1, emptyPointer.getMatchingIndex());
 assertNull(emptyPointer.tail());
 assertEquals("", emptyPointer.toString());
 }

 @Test
 public void testBasicPointer() {
 String pointerString = "/test/property";
 JsonPointer pointer = JsonPointer.compile(pointerString);
 assertTrue(pointer.matches());
 assertEquals("test", pointer.getMatchingProperty());
 assertEquals(-1, pointer.getMatchingIndex());
 assertNull(pointer.tail());
 assertEquals(pointerString, pointer.toString());
 }

 @Test
 public void testNestedPointer() {
 String pointerString = "/test/subtest/property";
 JsonPointer pointer = JsonPointer.compile(pointerString);
 assertFalse(pointer.matches());
 assertEquals("subtest", pointer.getMatchingProperty());
 assertEquals(-1, pointer.getMatchingIndex());
 JsonPointer tail = pointer.tail();
 assertNotNull(tail);
 assertEquals("/test", tail.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidPointer() {
 String invalidPointerString = "~test/property";
 // Jackson's JsonPointer class should throw an IllegalArgumentException
 // for invalid pointers
 JsonPointer pointer = JsonPointer.compile(invalidPointerString);
 fail("IllegalArgumentException expected");
 }

 @Test
 public void testMatchProperty() {
 String pointerString = "/test/property";
 JsonPointer pointer = JsonPointer.compile(pointerString);
 String matchingProperty = "test";
 JsonPointer matchedPointer = pointer.matchProperty(matchingProperty);
 assertEquals(pointer, matchedPointer);
 }

 @Test
 public void testMatchElement() {
 String pointerString = "/test/0/property";
 JsonPointer pointer = JsonPointer.compile(pointerString);
 JsonPointer matchedPointer = pointer.matchElement(0);
 assertEquals(pointer, matchedPointer);
 }

 @Test
 public void testMismatchedProperty() {
 String pointerString = "/test/property";
 JsonPointer pointer = JsonPointer.compile(pointerString);
 String mismatchedProperty = "subtest";
 JsonPointer matchedPointer = pointer.matchProperty(mismatchedProperty);
 assertNull(matchedPointer);
 }

 @Test
 public void testMismatchedElement() {
 String pointerString = "/test/0/property";
 JsonPointer pointer = JsonPointer.compile(pointerString);
 int mismatchedIndex = 1;
 JsonPointer matchedPointer = pointer.matchElement(mismatchedIndex);
 assertNull(matchedPointer);
 }
}