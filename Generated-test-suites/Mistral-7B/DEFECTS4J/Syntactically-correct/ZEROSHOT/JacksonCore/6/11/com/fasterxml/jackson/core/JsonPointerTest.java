package com.fasterxml.jackson.core.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonPointer;

import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testEmptyPointer() {
        JsonPointer pointer = JsonPointer.EMPTY;
        assertTrue(pointer.matches());
        assertEquals("", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertNull(pointer.tail());
        assertEquals("", pointer.toString());
    }

    @Test
    public void testSimplePointer() {
        JsonPointer pointer = JsonPointer.compile("/test");
        assertFalse(pointer.matches());
        assertEquals("test", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertNull(pointer.tail());
        assertEquals("/test", pointer.toString());
    }

    @Test
    public void testPointerWithProperty() {
        JsonPointer pointer = JsonPointer.compile("/test/property");
        assertFalse(pointer.matches());
        assertEquals("property", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertNotNull(pointer.tail());
        assertEquals("/test/property", pointer.toString());
    }

    @Test
    public void testPointerWithIndex() {
        JsonPointer pointer = JsonPointer.compile("/test/0");
        assertFalse(pointer.matches());
        assertEquals("", pointer.getMatchingProperty());
        assertEquals(0, pointer.getMatchingIndex());
        assertNotNull(pointer.tail());
        assertEquals("/test/0", pointer.toString());
    }

    @Test
    public void testPointerWithMultipleSegments() {
        JsonPointer pointer = JsonPointer.compile("/test/property1/property2/0");
        assertFalse(pointer.matches());
        assertEquals("property2", pointer.getMatchingProperty());
        assertEquals(0, pointer.getMatchingIndex());
        assertNotNull(pointer.tail());
        assertEquals("/test/property1/property2", pointer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPointer() {
        JsonPointer.compile("test");
    }

    @Test
    public void testMatchProperty() {
        JsonPointer pointer = JsonPointer.compile("/test/property");
        JsonPointer matchingPointer = new JsonPointer("/test", "property", null);
        JsonPointer nextPointer = new JsonPointer("/test/property/value");
        JsonPointer tailPointer = new JsonPointer("/test/property/value/anotherProperty");

        JsonPointer matchedPointer = pointer.matchProperty("property");
        assertEquals(nextPointer, matchedPointer);
        assertNull(pointer.matchProperty("anotherProperty"));

        assertEquals(pointer, matchedPointer.tail());
        assertEquals("/test/property", matchedPointer.toString());
    }

    @Test
    public void testMatchElement() {
        JsonPointer pointer = JsonPointer.compile("/test/0");
        JsonPointer matchingPointer = new JsonPointer("/test", "", null);
        JsonPointer nextPointer = new JsonPointer("/test/0/value");
        JsonPointer tailPointer = new JsonPointer("/test/1");

        JsonPointer matchedPointer = pointer.matchElement(0);
        assertEquals(nextPointer, matchedPointer);
        assertNull(pointer.matchElement(1));

        assertEquals(pointer, matchedPointer.tail());
        assertEquals("/test/0", matchedPointer.toString());
    }
}