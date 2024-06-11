package com.fasterxml.jackson.core.test;

import com.fasterxml.jackson.core.JsonPointer;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test class for JsonPointer class.
 */
public class JsonPointerTest {

 @Test
 public void testEmptyPointer() {
 JsonPointer emptyPointer = JsonPointer.EMPTY;
 assertTrue(emptyPointer.matches());
 assertNull(emptyPointer.getMatchingProperty());
 assertFalse(emptyPointer.mayMatchProperty());
 assertFalse(emptyPointer.mayMatchElement());
 }

 @Test
 public void testCompileValidJsonPointer() {
 String input = "/a/0/b/1";
 JsonPointer jsonPointer = JsonPointer.compile(input);
 assertEquals("/a/0/b/1", jsonPointer.toString());
 assertTrue(jsonPointer.matches());
 assertEquals("b", jsonPointer.getMatchingProperty());
 assertEquals(1, jsonPointer.getMatchingIndex());
 assertTrue(jsonPointer.mayMatchProperty());
 assertFalse(jsonPointer.mayMatchElement());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCompileInvalidJsonPointer() {
 String input = "a";
 JsonPointer jsonPointer = JsonPointer.compile(input);
 // JUnit should throw IllegalArgumentException
 }

 @Test
 public void testMatchProperty() {
 JsonPointer jsonPointer = new JsonPointer("/a/0/b", "b", JsonPointer.EMPTY);
 JsonPointer matchProperty = jsonPointer.matchProperty("b");
 assertEquals(JsonPointer.EMPTY, matchProperty);
 jsonPointer = new JsonPointer("/a/0/b", "b", new JsonPointer("/c/1/d", "d", JsonPointer.EMPTY));
 matchProperty = jsonPointer.matchProperty("b");
 assertEquals(new JsonPointer("/c/1/d", "d", JsonPointer.EMPTY), matchProperty);
 }

 @Test
 public void testMatchElement() {
 JsonPointer jsonPointer = new JsonPointer("/a/0/b/1", "", JsonPointer.EMPTY);
 JsonPointer matchElement = jsonPointer.matchElement(1);
 assertEquals(JsonPointer.EMPTY, matchElement);
 jsonPointer = new JsonPointer("/a/0/b/1", "", new JsonPointer("/c/1/d/2", "", JsonPointer.EMPTY));
 matchElement = jsonPointer.matchElement(1);
 assertEquals(new JsonPointer("/c/1/d/2", "", JsonPointer.EMPTY), matchElement);
 }

 @Test
 public void testTail() {
 JsonPointer jsonPointer = new JsonPointer("/a/0/b", "b", JsonPointer.EMPTY);
 JsonPointer tail = jsonPointer.tail();
 assertEquals(JsonPointer.EMPTY, tail);
 jsonPointer = new JsonPointer("/a/0/b", "b", new JsonPointer("/c/1/d", "d", JsonPointer.EMPTY));
 tail = jsonPointer.tail();
 assertEquals(new JsonPointer("/c/1/d", "d", JsonPointer.EMPTY), tail);
 }

 @Test
 public void testToString() {
 JsonPointer jsonPointer1 = new JsonPointer("/a/0/b", "b", JsonPointer.EMPTY);
 JsonPointer jsonPointer2 = new JsonPointer("/a/0/b", "b", JsonPointer.EMPTY);
 assertEquals(jsonPointer1.toString(), jsonPointer2.toString());
 }

 @Test
 public void testHashCode() {
 JsonPointer jsonPointer1 = new JsonPointer("/a/0/b", "b", JsonPointer.EMPTY);
 JsonPointer jsonPointer2 = new JsonPointer("/a/0/b", "b", JsonPointer.EMPTY);
 assertEquals(jsonPointer1.hashCode(), jsonPointer2.hashCode());
 }

 @Test
 public void testEquals() {
 JsonPointer jsonPointer1 = new JsonPointer("/a/0/b", "b", JsonPointer.EMPTY);
 JsonPointer jsonPointer2 = new JsonPointer("/a/0/b", "b", JsonPointer.EMPTY);
 JsonPointer jsonPointer3 = new JsonPointer("/a/0/b", "c", JsonPointer.EMPTY);
 JsonPointer jsonPointer4 = new JsonPointer("/c/1/d", "d", JsonPointer.EMPTY);
 assertTrue(jsonPointer1.equals(jsonPointer1));
 assertTrue(jsonPointer1.equals(jsonPointer2));
 assertFalse(jsonPointer1.equals(jsonPointer3));
 assertFalse(jsonPointer1.equals(jsonPointer4));
 }
}