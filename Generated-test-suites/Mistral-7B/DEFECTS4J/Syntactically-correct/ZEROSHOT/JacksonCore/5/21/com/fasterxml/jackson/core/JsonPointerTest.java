package com.fasterxml.jackson.core.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonPointer;

import org.junit.Test;

/**
 * Test class for JsonPointer class.
 */
public class JsonPointerTest {

    @Test
    public void testEmptyPointer() {
        JsonPointer jsonPointer = JsonPointer.EMPTY;
        assertTrue(jsonPointer.matches());
        assertEquals("", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertNull(jsonPointer.tail());
        assertEquals("", jsonPointer.toString());
    }

    @Test
    public void testParseSimplePointer() {
        JsonPointer jsonPointer = JsonPointer.compile("/a");
        assertFalse(jsonPointer.matches());
        assertEquals("a", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertNull(jsonPointer.tail());
        assertEquals("/a", jsonPointer.toString());
    }

    @Test
    public void testParseComplexPointer() {
        JsonPointer jsonPointer = JsonPointer.compile("/a/0/b/1");
        assertFalse(jsonPointer.matches());
        assertEquals("b", jsonPointer.getMatchingProperty());
        assertEquals(1, jsonPointer.getMatchingIndex());
        assertNotNull(jsonPointer.tail());
        assertEquals("/a/0/b", jsonPointer.tail().toString());
        assertEquals("/a/0/b/1", jsonPointer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidPointer() {
        JsonPointer.compile("~/a");
    }

    @Test
    public void testMatchProperty() {
        JsonPointer jsonPointer1 = JsonPointer.compile("/a/0");
        JsonPointer jsonPointer2 = JsonPointer.compile("/a/0/b");
        JsonPointer jsonPointer3 = JsonPointer.compile("/c/d");

        JsonPointer matchedJsonPointer = jsonPointer1.matchProperty("a");
        assertEquals(jsonPointer1, matchedJsonPointer);

        matchedJsonPointer = jsonPointer2.matchProperty("a");
        assertEquals(jsonPointer1, matchedJsonPointer);

        matchedJsonPointer = jsonPointer2.matchProperty("b");
        assertEquals(jsonPointer2, matchedJsonPointer);

        matchedJsonPointer = jsonPointer3.matchProperty("a");
        assertNull(matchedJsonPointer);
    }

    @Test
    public void testMatchElement() {
        JsonPointer jsonPointer1 = JsonPointer.compile("/a/0");
        JsonPointer jsonPointer2 = JsonPointer.compile("/a/1");
        JsonPointer jsonPointer3 = JsonPointer.compile("/a/2");

        JsonPointer matchedJsonPointer = jsonPointer1.matchElement(0);
        assertEquals(jsonPointer1, matchedJsonPointer);

        matchedJsonPointer = jsonPointer2.matchElement(0);
        assertNull(matchedJsonPointer);

        matchedJsonPointer = jsonPointer2.matchElement(1);
        assertEquals(jsonPointer2, matchedJsonPointer);

        matchedJsonPointer = jsonPointer3.matchElement(0);
        assertNull(matchedJsonPointer);

        matchedJsonPointer = jsonPointer3.matchElement(1);
        assertNull(matchedJsonPointer);

        matchedJsonPointer = jsonPointer3.matchElement(2);
        assertEquals(jsonPointer3, matchedJsonPointer);
    }

    @Test
    public void testTail() {
        JsonPointer jsonPointer1 = JsonPointer.compile("/a/0");
        JsonPointer jsonPointer2 = JsonPointer.compile("/a/0/b");

        assertEquals(jsonPointer2, jsonPointer1.tail());
        assertEquals("/a/0/b", jsonPointer2.toString());
    }

    @Test
    public void testHashCodeAndEquals() {
        JsonPointer jsonPointer1 = JsonPointer.compile("/a/0");
        JsonPointer jsonPointer2 = JsonPointer.compile("/a/0");
        JsonPointer jsonPointer3 = JsonPointer.compile("/a/1");

        assertEquals(jsonPointer1, jsonPointer1);
        assertEquals(jsonPointer1, jsonPointer2);
        assertNotEquals(jsonPointer1, jsonPointer3);

        int hashCode1 = jsonPointer1.hashCode();
        int hashCode2 = jsonPointer2.hashCode();
        int hashCode3 = jsonPointer3.hashCode();

        assertEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
    }
}