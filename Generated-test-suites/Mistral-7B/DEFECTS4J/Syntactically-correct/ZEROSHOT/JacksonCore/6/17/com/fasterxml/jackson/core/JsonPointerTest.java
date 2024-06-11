package com.fasterxml.jackson.core.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonPointer;

import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testEmptyJsonPointer() {
        JsonPointer emptyJsonPointer = JsonPointer.EMPTY;
        assertTrue(emptyJsonPointer.matches());
        assertEquals("", emptyJsonPointer.getMatchingProperty());
        assertEquals(-1, emptyJsonPointer.getMatchingIndex());
        assertNull(emptyJsonPointer.tail());
        assertEquals("", emptyJsonPointer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionForInvalidInput() {
        JsonPointer jsonPointer = JsonPointer.compile("invalidInput");
    }

    @Test
    public void testJsonPointerWithSingleSegment() {
        String input = "/property1";
        JsonPointer jsonPointer = JsonPointer.compile(input);
        assertTrue(jsonPointer.matches());
        assertEquals("property1", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertNull(jsonPointer.tail());
        assertEquals(input, jsonPointer.toString());
    }

    @Test
    public void testJsonPointerWithMultipleSegments() {
        String input = "/array/0/object/property1";
        JsonPointer jsonPointer = JsonPointer.compile(input);
        assertFalse(jsonPointer.matches());
        assertEquals("object", jsonPointer.getMatchingProperty());
        assertEquals(0, jsonPointer.getMatchingIndex());
        JsonPointer tailJsonPointer = jsonPointer.tail();
        assertEquals("/array/0", tailJsonPointer.toString());
    }

    @Test
    public void testJsonPointerWithQuotedEscapedSegment() {
        String input = "/property~1/property~0";
        JsonPointer jsonPointer = JsonPointer.compile(input);
        assertTrue(jsonPointer.matches());
        assertEquals("property~1", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertNull(jsonPointer.tail());
        assertEquals(input, jsonPointer.toString());
    }

    @Test
    public void testJsonPointerWithMultipleQuotedEscapedSegments() {
        String input = "/property~1/property~0/property~1/property~0";
        JsonPointer jsonPointer = JsonPointer.compile(input);
        assertFalse(jsonPointer.matches());
        assertEquals("property~1", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        JsonPointer tailJsonPointer = jsonPointer.tail();
        assertEquals("/property~1/property~0", tailJsonPointer.toString());
    }

    @Test
    public void testJsonPointerWithQuotedEscapedSegmentsAndSlash() {
        String input = "/property~1/property~0/array/0";
        JsonPointer jsonPointer = JsonPointer.compile(input);
        assertFalse(jsonPointer.matches());
        assertEquals("property~1", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        JsonPointer tailJsonPointer = jsonPointer.tail();
        assertEquals("/property~1/array", tailJsonPointer.toString());
    }

    @Test
    public void testJsonPointerWithMultipleMatches() {
        String input = "/array/1/object/property1=value1/array/0";
        JsonPointer jsonPointer = JsonPointer.compile(input);
        JsonPointer property1Match = jsonPointer.matchProperty("property1");
        JsonPointer array0Match = jsonPointer.matchElement(0);
        assertEquals("property1", property1Match.getMatchingProperty());
        assertEquals(1, property1Match.getMatchingIndex());
        assertEquals("/array/1/object", property1Match.toString());
        assertEquals(1, jsonPointer.getMatchingIndex());
        assertEquals("value1", jsonPointer.getMatchingProperty());
        assertEquals("/array/0", array0Match.toString());
        assertEquals(0, array0Match.getMatchingIndex());
    }
}