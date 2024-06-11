package com.fasterxml.jackson.core.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonPointer;

import org.junit.Test;

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
        assertEquals(0, jsonPointer.hashCode());
        assertTrue(jsonPointer.equals(JsonPointer.EMPTY));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInput1() {
        JsonPointer.compile("abc");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInput2() {
        JsonPointer.compile("//");
    }

    @Test
    public void testSimplePointer() {
        JsonPointer jsonPointer = JsonPointer.compile("/abc");
        assertFalse(jsonPointer.matches());
        assertEquals("abc", jsonPointer.getMatchingProperty());
        assertTrue(jsonPointer.mayMatchProperty());
        assertFalse(jsonPointer.mayMatchElement());
        assertNull(jsonPointer.tail());
        assertEquals("/abc", jsonPointer.toString());
        int hashCode = jsonPointer.hashCode();
        JsonPointer jsonPointer1 = JsonPointer.compile("/abc");
        assertTrue(jsonPointer.equals(jsonPointer1));
        assertEquals(hashCode, jsonPointer1.hashCode());
    }

    @Test
    public void testPointerWithIndex() {
        JsonPointer jsonPointer = JsonPointer.compile("/list/1");
        assertFalse(jsonPointer.matches());
        assertEquals("list", jsonPointer.getMatchingProperty());
        assertFalse(jsonPointer.mayMatchProperty());
        assertTrue(jsonPointer.mayMatchElement());
        assertEquals(1, jsonPointer.getMatchingIndex());
        assertNull(jsonPointer.tail());
        assertEquals("/list/1", jsonPointer.toString());
        int hashCode = jsonPointer.hashCode();
        JsonPointer jsonPointer1 = JsonPointer.compile("/list/1");
        assertTrue(jsonPointer.equals(jsonPointer1));
        assertEquals(hashCode, jsonPointer1.hashCode());
    }

    @Test
    public void testPointerWithPropertyAndIndex() {
        JsonPointer jsonPointer = JsonPointer.compile("/obj/prop/0");
        assertFalse(jsonPointer.matches());
        assertEquals("prop", jsonPointer.getMatchingProperty());
        assertFalse(jsonPointer.mayMatchProperty());
        assertTrue(jsonPointer.mayMatchElement());
        assertEquals(0, jsonPointer.getMatchingIndex());
        assertNull(jsonPointer.tail());
        assertEquals("/obj/prop/0", jsonPointer.toString());
        int hashCode = jsonPointer.hashCode();
        JsonPointer jsonPointer1 = JsonPointer.compile("/obj/prop/0");
        assertTrue(jsonPointer.equals(jsonPointer1));
        assertEquals(hashCode, jsonPointer1.hashCode());
    }

    @Test
    public void testPointerWithMultipleSegments() {
        JsonPointer jsonPointer = JsonPointer.compile("/list/0/obj/prop");
        assertFalse(jsonPointer.matches());
        assertEquals("prop", jsonPointer.getMatchingProperty());
        assertFalse(jsonPointer.mayMatchProperty());
        assertFalse(jsonPointer.mayMatchElement());
        assertNull(jsonPointer.tail());
        assertEquals("/list/0/obj/prop", jsonPointer.toString());
        int hashCode = jsonPointer.hashCode();
        JsonPointer jsonPointer1 = JsonPointer.compile("/list/0/obj/prop");
        assertTrue(jsonPointer.equals(jsonPointer1));
        assertEquals(hashCode, jsonPointer1.hashCode());
    }

    @Test
    public void testPointerWithEscapedCharacter() {
        JsonPointer jsonPointer = JsonPointer.compile("/list/~1");
        assertFalse(jsonPointer.matches());
        assertEquals("list", jsonPointer.getMatchingProperty());
        assertFalse(jsonPointer.mayMatchProperty());
        assertTrue(jsonPointer.mayMatchElement());
        assertEquals(1, jsonPointer.getMatchingIndex());
        assertNull(jsonPointer.tail());
        assertEquals("/list/1", jsonPointer.toString());
        int hashCode = jsonPointer.hashCode();
        JsonPointer jsonPointer1 = JsonPointer.compile("/list/~1");
        assertTrue(jsonPointer.equals(jsonPointer1));
        assertEquals(hashCode, jsonPointer1.hashCode());
    }

    @Test
    public void testPointerWithQuotedSegment() {
        JsonPointer jsonPointer = JsonPointer.compile("/list/~0/prop");
        assertFalse(jsonPointer.matches());
        assertEquals("prop", jsonPointer.getMatchingProperty());
        assertFalse(jsonPointer.mayMatchProperty());
        assertFalse(jsonPointer.mayMatchElement());
        assertNull(jsonPointer.tail());
        assertEquals("/list/0/prop", jsonPointer.toString());
        int hashCode = jsonPointer.hashCode();
        JsonPointer jsonPointer1 = JsonPointer.compile("/list/~0/prop");
        assertTrue(jsonPointer.equals(jsonPointer1));
        assertEquals(hashCode, jsonPointer1.hashCode());
    }
}