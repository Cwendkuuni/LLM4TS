package com.fasterxml.jackson.core.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonPointer;

import org.junit.Test;

/**
 * Test class for JsonPointer class.
 */
public class JsonPointerTest {

    /**
     * Test for empty pointer construction.
     */
    @Test
    public void testEmptyPointer() {
        JsonPointer emptyPointer = JsonPointer.EMPTY;
        assertFalse(emptyPointer.matches());
        assertNull(emptyPointer.getMatchingProperty());
        assertFalse(emptyPointer.mayMatchProperty());
        assertFalse(emptyPointer.mayMatchElement());
        assertEquals("", emptyPointer.toString());
    }

    /**
     * Test for constructing pointer from a single segment.
     */
    @Test
    public void testSingleSegmentPointer() {
        JsonPointer singleSegmentPointer = new JsonPointer("/test");
        assertTrue(singleSegmentPointer.matches());
        assertEquals("test", singleSegmentPointer.getMatchingProperty());
        assertTrue(singleSegmentPointer.mayMatchProperty());
        assertFalse(singleSegmentPointer.mayMatchElement());
        assertEquals("/test", singleSegmentPointer.toString());
    }

    /**
     * Test for constructing pointer from multiple segments.
     */
    @Test
    public void testMultipleSegmentsPointer() {
        JsonPointer multipleSegmentsPointer = new JsonPointer("/test/property/0");
        assertFalse(multipleSegmentsPointer.matches());
        assertEquals("/property/0", multipleSegmentsPointer.tail().toString());
        assertEquals("test", multipleSegmentsPointer.getMatchingProperty());
        assertTrue(multipleSegmentsPointer.mayMatchProperty());
        assertTrue(multipleSegmentsPointer.mayMatchElement());
        assertEquals(0, multipleSegmentsPointer.getMatchingIndex());
        assertEquals("/test/property", multipleSegmentsPointer.toString());
    }

    /**
     * Test for compiling an invalid JSON Pointer.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPointer() {
        JsonPointer.compile("test");
    }

    /**
     * Test for factory method fromSegment.
     */
    // TODO: Implement the fromSegment factory method and add test cases

    /**
     * Test for matches method.
     */
    @Test
    public void testMatches() {
        JsonPointer pointer = new JsonPointer("/test/property/0");
        assertTrue(pointer.matches());
        point = new JsonPointer("/test/property/1");
        assertFalse(pointer.matches());
    }

    /**
     * Test for getMatchingProperty method.
     */
    @Test
    public void testGetMatchingProperty() {
        JsonPointer pointer = new JsonPointer("/test/property/0");
        assertEquals("property", pointer.getMatchingProperty());
        pointer = new JsonPointer("/test/property");
        assertEquals("property", pointer.getMatchingProperty());
    }

    /**
     * Test for mayMatchProperty method.
     */
    @Test
    public void testMayMatchProperty() {
        JsonPointer pointer = new JsonPointer("/test/property/0");
        assertTrue(pointer.mayMatchProperty());
        pointer = new JsonPointer("/test/property");
        assertTrue(pointer.mayMatchProperty());
        pointer = JsonPointer.EMPTY;
        assertFalse(pointer.mayMatchProperty());
    }

    /**
     * Test for mayMatchElement method.
     */
    @Test
    public void testMayMatchElement() {
        JsonPointer pointer = new JsonPointer("/test/property/0");
        assertTrue(pointer.mayMatchElement());
        pointer = new JsonPointer("/test/property");
        assertFalse(pointer.mayMatchElement());
        pointer = JsonPointer.EMPTY;
        assertFalse(pointer.mayMatchElement());
    }

    /**
     * Test for matchProperty method.
     */
    @Test
    public void testMatchProperty() {
        JsonPointer pointer = new JsonPointer("/test/property/0");
        JsonPointer matchedPointer = pointer.matchProperty("property");
        assertEquals(pointer.toString(), matchedPointer.toString());
        pointer = new JsonPointer("/test/property");
        matchedPointer = pointer.matchProperty("property");
        assertEquals(pointer.toString(), matchedPointer.toString());
        matchedPointer = pointer.matchProperty("test");
        assertNull(matchedPointer);
    }

    /**
     * Test for matchElement method.
     */
    @Test
    public void testMatchElement() {
        JsonPointer pointer = new JsonPointer("/test/property/0");
        JsonPointer matchedPointer = pointer.matchElement(0);
        assertEquals(pointer.toString(), matchedPointer.toString());
        pointer = new JsonPointer("/test/property/1");
        matchedPointer = pointer.matchElement(0);
        assertNull(matchedPointer);
    }

    /**
     * Test for tail method.
     */
    @Test
    public void testTail() {
        JsonPointer pointer = new JsonPointer("/test/property/0");
        assertEquals(new JsonPointer("/test/property"), pointer.tail());
    }

    /**
     * Test for toString method.
     */
    @Test
    public void testToString() {
        JsonPointer pointer = new JsonPointer("/test/property/0");
        assertEquals("/test/property/0", pointer.toString());
        pointer = new JsonPointer("/test/property");
        assertEquals("/test/property", pointer.toString());
        pointer = JsonPointer.EMPTY;
        assertEquals("", pointer.toString());
    }

    /**
     * Test for hashCode and equals methods.
     */
    @Test
    public void testHashCodeAndEquals() {
        JsonPointer pointer1 = new JsonPointer("/test/property/0");
        JsonPointer pointer2 = new JsonPointer("/test/property/0");
        JsonPointer pointer3 = new JsonPointer("/test/property");
        JsonPointer pointer4 = new JsonPointer("/test/property/1");
        JsonPointer pointer5 = JsonPointer.EMPTY;
        assertEquals(pointer1.hashCode(), pointer2.hashCode());
        assertTrue(pointer1.equals(pointer2));
        assertFalse(pointer1.equals(pointer3));
        assertFalse(pointer1.equals(pointer4));
        assertFalse(pointer1.equals(pointer5));
        assertEquals(pointer5.hashCode(), pointer5.hashCode());
        assertTrue(pointer5.equals(pointer5));
    }
}