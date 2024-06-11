package com.fasterxml.jackson.core.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonPointer;

import org.junit.Test;

/**
 * Test class for {@link JsonPointer} class.
 */
public class JsonPointerTest {

    /**
     * Test constructor with empty string input.
     */
    @Test
    public void testEmptyConstructor() {
        JsonPointer pointer = new JsonPointer();
        assertNull(pointer._nextSegment);
        assertEquals("", pointer._asString);
        assertTrue(pointer.matches());
    }

    /**
     * Test constructor with valid input.
     */
    @Test
    public void testValidConstructor() {
        String input = "/test/property1/2";
        JsonPointer pointer = new JsonPointer(input, "property1", null);
        assertEquals("/test/property1", pointer._asString);
        assertNotNull(pointer._nextSegment);
        assertEquals("/2", pointer._nextSegment._asString);
        assertFalse(pointer.matches());
        assertEquals("property1", pointer.getMatchingProperty());
        assertEquals(2, pointer.getMatchingIndex());
    }

    /**
     * Test compile factory method with empty string input.
     */
    @Test
    public void testEmptyCompile() {
        JsonPointer pointer = JsonPointer.compile("");
        assertNotNull(pointer);
        assertTrue(pointer.matches());
    }

    /**
     * Test compile factory method with valid input.
     */
    @Test
    public void testValidCompile() {
        String input = "/test/property1/2";
        JsonPointer pointer = JsonPointer.compile(input);
        assertEquals("/test/property1/2", pointer._asString);
        assertNotNull(pointer._nextSegment);
        assertEquals("/2", pointer._nextSegment._asString);
        assertFalse(pointer.matches());
        assertEquals("property1", pointer.getMatchingProperty());
        assertEquals(2, pointer.getMatchingIndex());
    }

    /**
     * Test compile factory method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCompile() {
        String input = "test";
        JsonPointer.compile(input);
    }

    /**
     * Test matches method.
     */
    @Test
    public void testMatches() {
        String input = "/test/property1/2";
        JsonPointer pointer = JsonPointer.compile(input);

        JsonPointer emptyPointer = new JsonPointer();
        JsonPointer propertyPointer = new JsonPointer("/test/property1");
        JsonPointer indexPointer = new JsonPointer("/test/property1/1");
        JsonPointer tailPointer = pointer.tail();

        assertTrue(emptyPointer.matches());
        assertFalse(pointer.matches());
        assertFalse(propertyPointer.matches());
        assertFalse(indexPointer.matches());
        assertTrue(tailPointer.matches());
    }

    /**
     * Test getMatchingProperty method.
     */
    @Test
    public void testGetMatchingProperty() {
        String input = "/test/property1/2";
        JsonPointer pointer = JsonPointer.compile(input);

        assertEquals("property1", pointer.getMatchingProperty());
        JsonPointer emptyPointer = new JsonPointer();
        assertEquals("", emptyPointer.getMatchingProperty());
    }

    /**
     * Test getMatchingIndex method.
     */
    @Test
    public void testGetMatchingIndex() {
        String input = "/test/property1/2";
        JsonPointer pointer = JsonPointer.compile(input);

        assertEquals(2, pointer.getMatchingIndex());
        JsonPointer emptyPointer = new JsonPointer();
        assertEquals(-1, emptyPointer.getMatchingIndex());
    }

    /**
     * Test mayMatchProperty method.
     */
    @Test
    public void testMayMatchProperty() {
        String input = "/test/property1/2";
        JsonPointer pointer = JsonPointer.compile(input);

        assertTrue(pointer.mayMatchProperty());
        JsonPointer emptyPointer = new JsonPointer();
        assertFalse(emptyPointer.mayMatchProperty());
    }

    /**
     * Test mayMatchElement method.
     */
    @Test
    public void testMayMatchElement() {
        String input = "/test/property1/2";
        JsonPointer pointer = JsonPointer.compile(input);

        assertTrue(pointer.mayMatchElement());
        JsonPointer emptyPointer = new JsonPointer();
        assertFalse(emptyPointer.mayMatchElement());
    }

    /**
     * Test matchProperty method.
     */
    @Test
    public void testMatchProperty() {
        String input = "/test/property1";
        JsonPointer pointer = JsonPointer.compile(input);

        JsonPointer propertyMatch = new JsonPointer("/test/property1");
        JsonPointer arrayMatch = new JsonPointer("/test/array[1]/property1");

        assertEquals(propertyMatch, pointer.matchProperty("property1"));
        assertNull(pointer.matchProperty("property2"));
        assertNull(pointer.matchProperty(""));
        assertNull(pointer.matchProperty(null));
        assertNull(pointer.matchProperty("property1").matchProperty("property2"));
        assertEquals(arrayMatch, pointer.matchProperty("array[1]/property1"));
    }

    /**
     * Test matchElement method.
     */
    @Test
    public void testMatchElement() {
        String input = "/test/array/1";
        JsonPointer pointer = JsonPointer.compile(input);

        JsonPointer arrayMatch = new JsonPointer("/test/array/1");
        JsonPointer emptyArrayMatch = new JsonPointer("/test/array");
        JsonPointer nonMatch = new JsonPointer("/test/array/3");

        assertEquals(arrayMatch, pointer.matchElement(1));
        assertNull(pointer.matchElement(-1));
        assertNull(pointer.matchElement(0));
        assertNull(pointer.matchElement(2));
        assertNull(pointer.matchElement(null));
        assertNull(pointer.matchElement(""));
        assertEquals(emptyArrayMatch, pointer.matchElement(0));
        assertNull(nonMatch);
    }

    /**
     * Test tail method.
     */
    @Test
    public void testTail() {
        String input = "/test/property1/2";
        JsonPointer pointer = JsonPointer.compile(input);

        JsonPointer tailPointer = pointer.tail();
        assertEquals("/2", tailPointer._asString);
    }

    /**
     * Test equals method.
     */
    @Test
    public void testEquals() {
        String input1 = "/test/property1/2";
        String input2 = "/test/property1/2";
        String input3 = "/test/property1/3";

        JsonPointer pointer1 = JsonPointer.compile(input1);
        JsonPointer pointer2 = JsonPointer.compile(input2);
        JsonPointer pointer3 = JsonPointer.compile(input3);

        JsonPointer emptyPointer = new JsonPointer();

        assertTrue(pointer1.equals(pointer1));
        assertTrue(pointer1.equals(pointer2));
        assertFalse(pointer1.equals(pointer3));
        assertFalse(pointer1.equals(emptyPointer));
        assertFalse(pointer1.equals(new Object()));
    }

    /**
     * Test hashCode method.
     */
    @Test
    public void testHashCode() {
        String input = "/test/property1/2";
        JsonPointer pointer = JsonPointer.compile(input);

        String input2 = "/test/property1/2";
        JsonPointer pointer2 = JsonPointer.compile(input2);
        String input3 = "/test/property1/3";

        JsonPointer pointer3 = JsonPointer.compile(input3);
        JsonPointer emptyPointer = new JsonPointer();

        assertEquals(pointer.hashCode(), pointer.hashCode());
        assertEquals(pointer.hashCode(), pointer2.hashCode());
        assertNotEquals(pointer.hashCode(), pointer3.hashCode());
        assertNotEquals(pointer.hashCode(), emptyPointer.hashCode());
    }
}