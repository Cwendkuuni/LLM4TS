package com.fasterxml.jackson.core.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonPointer;

import org.junit.Test;

/**
 * Test class for JsonPointer
 */
public class JsonPointerTest {

    /**
     * Test for empty pointer
     */
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

    /**
     * Test for pointer with single segment
     */
    @Test
    public void testSingleSegmentPointer() {
        String input = "/test";
        JsonPointer pointer = JsonPointer.compile(input);
        assertFalse(pointer.matches());
        assertEquals("test", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.tail());
        assertEquals("/test", pointer.toString());
    }

    /**
     * Test for pointer with multiple segments
     */
    @Test
    public void testMultipleSegmentsPointer() {
        String input = "/test/property1/property2";
        JsonPointer pointer = JsonPointer.compile(input);
        assertFalse(pointer.matches());
        assertEquals("property1", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        JsonPointer tail = pointer.tail();
        assertEquals("/property2", tail.toString());
        assertEquals(1, pointer.getMatchingProperty().length());
        assertEquals(2, tail.getMatchingProperty().length());
    }

    /**
     * Test for pointer with index
     */
    @Test
    public void testIndexPointer() {
        String input = "/test/0";
        JsonPointer pointer = JsonPointer.compile(input);
        assertFalse(pointer.matches());
        assertEquals("test", pointer.getMatchingProperty());
        assertEquals(0, pointer.getMatchingIndex());
        assertTrue(pointer.mayMatchProperty());
        assertTrue(pointer.mayMatchElement());
        assertNull(pointer.tail());
        assertEquals("/test/0", pointer.toString());
    }

    /**
     * Test for pointer with invalid input
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInputPointer() {
        String input = "test";
        JsonPointer.compile(input);
    }

    /**
     * Test for pointer with empty segment
     */
    @Test(expected = IllegalArgumentException.class)
    public void testEmptySegmentPointer() {
        String input = "/test/";
        JsonPointer.compile(input);
    }

    /**
     * Test for pointer with no slash
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNoSlashPointer() {
        String input = "test";
        JsonPointer.compile(input);
    }
}