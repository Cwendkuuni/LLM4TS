package com.fasterxml.jackson.core.test;

import com.fasterxml.jackson.core.JsonPointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonPointerTest {

 @Test
 public void testEmptyPointer() {
 JsonPointer jsonPointer = JsonPointer.EMPTY;
 assertTrue(jsonPointer.matches());
 assertNull(jsonPointer.getMatchingProperty());
 assertFalse(jsonPointer.mayMatchProperty());
 assertFalse(jsonPointer.mayMatchElement());
 assertNull(jsonPointer.tail());
 assertEquals("", jsonPointer.toString());
 }

 @Test
 public void testSimplePointer() {
 JsonPointer jsonPointer = JsonPointer.compile("/foo");
 assertTrue(jsonPointer.matches());
 assertEquals("foo", jsonPointer.getMatchingProperty());
 assertFalse(jsonPointer.mayMatchProperty());
 assertFalse(jsonPointer.mayMatchElement());
 assertNull(jsonPointer.tail());
 assertEquals("/foo", jsonPointer.toString());
 }

 @Test
 public void testPointerWithProperty() {
 JsonPointer jsonPointer = JsonPointer.compile("/foo/bar");
 assertFalse(jsonPointer.matches());
 assertEquals("bar", jsonPointer.getMatchingProperty());
 assertFalse(jsonPointer.mayMatchProperty());
 assertFalse(jsonPointer.mayMatchElement());
 assertNotNull(jsonPointer.tail());
 assertEquals("/foo", jsonPointer.toString());
 }

 @Test
 public void testPointerWithIndex() {
 JsonPointer jsonPointer = JsonPointer.compile("/foo/0");
 assertFalse(jsonPointer.matches());
 assertEquals("", jsonPointer.getMatchingProperty());
 assertFalse(jsonPointer.mayMatchProperty());
 assertTrue(jsonPointer.mayMatchElement());
 assertNull(jsonPointer.tail());
 assertEquals("/foo/0", jsonPointer.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidPointer() {
 JsonPointer jsonPointer = JsonPointer.compile("~foo");
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testChainOfPointers() {
 JsonPointer jsonPointer1 = JsonPointer.compile("/foo/bar");
 JsonPointer jsonPointer2 = JsonPointer.compile("/baz/qux");
 JsonPointer jsonPointer3 = jsonPointer1.matchProperty("bar").matchProperty("baz").matchElement(0);
 assertEquals(jsonPointer2, jsonPointer3);
 assertFalse(jsonPointer1.equals(jsonPointer2));
 }

 @Test
 public void testHashCodeAndEquals() {
 JsonPointer jsonPointer1 = JsonPointer.compile("/foo/bar");
 JsonPointer jsonPointer2 = JsonPointer.compile("/foo/bar");
 JsonPointer jsonPointer3 = JsonPointer.compile("/baz/qux");
 assertEquals(jsonPointer1, jsonPointer1);
 assertEquals(jsonPointer1, jsonPointer2);
 assertFalse(jsonPointer1.equals(jsonPointer3));
 int hashCode1 = jsonPointer1.hashCode();
 int hashCode2 = jsonPointer1.hashCode();
 int hashCode3 = jsonPointer3.hashCode();
 assertEquals(hashCode1, hashCode2);
 assertNotEquals(hashCode1, hashCode3);
 }
}