package com.fasterxml.jackson.core.test;

import com.fasterxml.jackson.core.JsonPointer;
import org.junit.Test;
import static org.junit.Assert.*;

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
 public void testBasicPointerCreation() {
 JsonPointer pointer = JsonPointer.compile("/test/property");
 assertTrue(pointer.matches());
 assertEquals("property", pointer.getMatchingProperty());
 assertFalse(pointer.mayMatchProperty());
 assertFalse(pointer.mayMatchElement());
 assertEquals("/test/property", pointer.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidPointerCreation() {
 JsonPointer pointer = JsonPointer.compile("test/property");
 fail("Expected IllegalArgumentException but got: " + pointer);
 }

 @Test
 public void testPropertyMatching() {
 JsonPointer pointer = JsonPointer.compile("/test/property");
 JsonPointer propertyPointer = new JsonPointer("/test/property");
 JsonPointer elementPointer = new JsonPointer("/test/0");
 JsonPointer emptyElementPointer = new JsonPointer("/test/");
 JsonPointer nullPointer = JsonPointer.EMPTY;

 assertTrue(pointer.matches());
 assertSame(propertyPointer, pointer.matchProperty("property"));
 assertNull(pointer.matchProperty("anotherProperty"));
 assertNull(pointer.matchElement(1));
 assertNull(pointer.tail());

 assertNull(nullPointer.matchProperty("property"));
 assertNull(nullPointer.matchElement(1));

 assertSame(propertyPointer, elementPointer.matchProperty("property"));
 assertNull(elementPointer.matchProperty("anotherProperty"));
 assertNull(elementPointer.matchElement(0));
 assertNull(elementPointer.tail());

 assertNull(emptyElementPointer.matchProperty("property"));
 assertNull(emptyElementPointer.matchElement(1));
 assertNull(emptyElementPointer.tail());
 }

 @Test
 public void testElementMatching() {
 JsonPointer pointer = JsonPointer.compile("/test/1");
 JsonPointer propertyPointer = new JsonPointer("/test/property");
 JsonPointer elementPointer = new JsonPointer("/test/1");
 JsonPointer emptyElementPointer = new JsonPointer("/test/");
 JsonPointer nullPointer = JsonPointer.EMPTY;

 assertTrue(pointer.matches());
 assertNull(pointer.getMatchingProperty());
 assertEquals(1, pointer.getMatchingIndex());
 assertFalse(pointer.mayMatchProperty());
 assertFalse(pointer.mayMatchElement());
 assertEquals("/test/1", pointer.toString());

 assertNull(nullPointer.matchElement(1));
 assertNull(nullPointer.matchProperty("property"));

 assertNull(propertyPointer.matchElement(1));
 assertSame(elementPointer, propertyPointer.matchElement(1));
 assertNull(propertyPointer.matchProperty("anotherProperty"));
 assertNull(propertyPointer.tail());

 assertNull(emptyElementPointer.matchElement(1));
 assertNull(emptyElementPointer.matchProperty("property"));
 assertNull(emptyElementPointer.tail());

 assertEquals(1, pointer.getMatchingIndex());
 }

 @Test
 public void testTailMatching() {
 JsonPointer pointer = JsonPointer.compile("/test/property/anotherProperty");
 JsonPointer propertyPointer = new JsonPointer("/test/property");
 JsonPointer anotherPropertyPointer = new JsonPointer("/test/property/anotherProperty");

 assertTrue(pointer.matches());
 assertNull(pointer.getMatchingProperty());
 assertFalse(pointer.mayMatchProperty());
 assertFalse(pointer.mayMatchElement());
 assertEquals("/test/property/anotherProperty", pointer.toString());

 assertNull(propertyPointer.matchElement(1));
 assertSame(anotherPropertyPointer, propertyPointer.tail());
 assertNull(propertyPointer.matchProperty("anotherProperty"));
 assertNull(propertyPointer.tail());

 assertNull(pointer.matchProperty("anotherProperty"));
 assertSame(anotherPropertyPointer, pointer.tail());
 }
}