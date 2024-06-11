package com.fasterxml.jackson.core.test;

import com.fasterxml.jackson.core.JsonPointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonPointerTest {

    @Test
    public void testEmptyPointer() {
        JsonPointer pointer = JsonPointer.EMPTY;
        assertTrue(pointer.matches());
        assertNull(pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertFalse(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.tail());
        assertEquals("", pointer.toString());
    }

    @Test
    public void testSimplePointer() {
        String input = "/test/property";
        JsonPointer pointer = JsonPointer.compile(input);
        assertTrue(pointer.matches());
        assertEquals("property", pointer.getMatchingProperty());
        assertFalse(pointer.mayMatchElement());
        assertEquals(0, pointer.tail().getMatchingIndex());
        assertEquals("/test/property", pointer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInput() {
        String input = "test/property";
        JsonPointer pointer = JsonPointer.compile(input);
    }

    @Test
    public void testIndexedPointer() {
        String input = "/test/0";
        JsonPointer pointer = JsonPointer.compile(input);
        assertTrue(pointer.matches());
        assertNull(pointer.getMatchingProperty());
        assertEquals(0, pointer.getMatchingIndex());
        assertFalse(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.tail());
        assertEquals("/test/0", pointer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeIndex() {
        String input = "/test/-1";
        JsonPointer pointer = JsonPointer.compile(input);
    }

    @Test
    public void testPointerWithTrailingSlash() {
        String input = "/test/";
        JsonPointer pointer = JsonPointer.compile(input);
        assertTrue(pointer.matches());
        assertNull(pointer.getMatchingProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.tail());
        assertEquals("/test/", pointer.toString());
    }

    @Test
    public void testPointerWithMultipleSegments() {
        String input = "/test/property1/property2/property3";
        JsonPointer pointer = JsonPointer.compile(input);
        assertFalse(pointer.matches());
        assertEquals("property1", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertEquals("/test/property1", pointer.tail().toString());
    }

    @Test
    public void testPointerWithEmptySegment() {
        String input = "/test//property";
        JsonPointer pointer = JsonPointer.compile(input);
        assertTrue(pointer.matches());
        assertEquals("property", pointer.getMatchingProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.tail());
        assertEquals("/test/property", pointer.toString());
    }

    @Test
    public void testPointerWithQuotedSegment() {
        String input = "/test/property~1/property";
        JsonPointer pointer = JsonPointer.compile(input);
        assertFalse(pointer.matches());
        assertEquals("property~1", pointer.getMatchingProperty());
        assertFalse(pointer.mayMatchElement());
        assertEquals(1, pointer.tail().getMatchingIndex());
        assertEquals("/test/property~1", pointer.toString());
    }
}