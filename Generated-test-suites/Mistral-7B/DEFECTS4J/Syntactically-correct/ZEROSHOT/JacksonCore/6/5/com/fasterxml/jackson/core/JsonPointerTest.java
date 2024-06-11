package com.fasterxml.jackson.core.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonPointer;

import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testEmptyJsonPointer() {
        JsonPointer emptyPointer = JsonPointer.EMPTY;
        assertTrue(emptyPointer.matches());
        assertNull(emptyPointer.getMatchingProperty());
        assertEquals(-1, emptyPointer.getMatchingIndex());
        assertFalse(emptyPointer.mayMatchProperty());
        assertFalse(emptyPointer.mayMatchElement());
        assertNull(emptyPointer.tail());
        assertEquals("", emptyPointer.toString());
    }

    @Test
    public void testBasicJsonPointerConstruction() {
        String input = "/foo/bar/baz";
        JsonPointer pointer = JsonPointer.compile(input);
        assertEquals(input, pointer.toString());
        assertTrue(pointer.matches());
        assertEquals("foo", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertFalse(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        JsonPointer tail = pointer.tail();
        assertNotNull(tail);
        assertFalse(tail.matches());
        assertEquals("bar", tail.getMatchingProperty());
        assertEquals(-1, tail.getMatchingIndex());
        assertFalse(tail.mayMatchProperty());
        assertFalse(tail.mayMatchElement());
        JsonPointer tailTail = tail.tail();
        assertNotNull(tailTail);
        assertEquals("baz", tailTail.getMatchingProperty());
        assertEquals(-1, tailTail.getMatchingIndex());
        assertFalse(tailTail.mayMatchProperty());
        assertFalse(tailTail.mayMatchElement());
        assertNull(tailTail.tail());
    }

    @Test
    public void testBasicJsonPointerMatching() {
        String input = "/foo/bar/baz";
        JsonPointer pointer = JsonPointer.compile(input);
        assertTrue(pointer.matches());
        assertFalse(pointer.matchProperty(null));
        assertFalse(pointer.matchProperty(""));
        assertFalse(pointer.matchProperty("quux"));
        assertTrue(pointer.matchProperty("foo"));
        JsonPointer matched = pointer.matchProperty("foo");
        assertTrue(matched.matches());
        assertEquals("bar", matched.getMatchingProperty());
        assertFalse(matched.mayMatchProperty());
        assertFalse(matched.mayMatchElement());
        assertNull(matched.tail());
        assertFalse(pointer.matchElement(-1));
        assertFalse(pointer.matchElement(0));
        assertTrue(pointer.matchElement(2));
        JsonPointer matchedElement = pointer.matchElement(2);
        assertTrue(matchedElement.matches());
        assertFalse(matchedElement.mayMatchProperty());
        assertFalse(matchedElement.mayMatchElement());
        assertNull(matchedElement.tail());
        assertEquals("/foo/bar/baz", pointer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidJsonPointerInput() {
        String input = "quux";
        JsonPointer pointer = JsonPointer.compile(input);
        fail("Expected IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidJsonPointerInputWithTrailingSlash() {
        String input = "/quux/";
        JsonPointer pointer = JsonPointer.compile(input);
        fail("Expected IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidJsonPointerInputWithLeadingSlash() {
        String input = "/";
        JsonPointer pointer = JsonPointer.compile(input);
        fail("Expected IllegalArgumentException");
    }

    @Test
    public void testJsonPointerHashCodeAndEquals() {
        String input1 = "/foo/bar/baz";
        String input2 = "/foo/bar/baz";
        JsonPointer pointer1 = JsonPointer.compile(input1);
        JsonPointer pointer2 = JsonPointer.compile(input2);
        assertEquals(pointer1, pointer2);
        JsonPointer pointer3 = new JsonPointer("/foo/bar/baz", "quux", null);
        assertNotEquals(pointer1, pointer3);
        JsonPointer pointer4 = new JsonPointer("/foo/bar/baz", "bar", null);
        assertNotEquals(pointer1, pointer4);
        JsonPointer pointer5 = new JsonPointer("/foo/quux/baz", "bar", null);
        assertNotEquals(pointer1, pointer5);
        JsonPointer pointer6 = new JsonPointer("/foo/bar/quux", "bar", null);
        assertNotEquals(pointer1, pointer6);
        JsonPointer pointer7 = new JsonPointer("/foo/bar/baz", "bar", pointer1);
        assertEquals(pointer1, pointer7);
        JsonPointer pointer8 = new JsonPointer("/foo/bar/baz", "bar", pointer2);
        assertEquals(pointer1, pointer8);
    }

    @Test
    public void testJsonPointerToString() {
        String input = "/foo/bar/baz";
        JsonPointer pointer = JsonPointer.compile(input);
        assertEquals(input, pointer.toString());
    }
}