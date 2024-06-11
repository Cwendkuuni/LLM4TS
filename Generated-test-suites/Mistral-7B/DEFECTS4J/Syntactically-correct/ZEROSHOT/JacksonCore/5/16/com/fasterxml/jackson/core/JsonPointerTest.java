package com.fasterxml.jackson.core.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.core.JsonPointer;

import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testEmptyPointer() {
        JsonPointer pointer = JsonPointer.EMPTY;
        assertTrue(pointer.matches());
        assertFalse(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.getMatchingProperty());
        assertNull(pointer.getMatchingIndex());
        assertEquals("", pointer.toString());
    }

    @Test
    public void testBasicPointer() {
        JsonPointer pointer = JsonPointer.compile("/foo");
        assertTrue(pointer.matches());
        assertFalse(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.getMatchingProperty());
        assertNull(pointer.getMatchingIndex());
        assertEquals("/foo", pointer.toString());

        JsonPointer propertyPointer = JsonPointer.compile("/foo/bar");
        assertFalse(propertyPointer.matches());
        assertTrue(propertyPointer.mayMatchProperty());
        assertFalse(propertyPointer.mayMatchElement());
        assertEquals("foo", propertyPointer.getMatchingProperty());
        assertNull(propertyPointer.getMatchingIndex());
        assertEquals("/foo/bar", propertyPointer.toString());
    }

    @Test
    public void testIndexPointer() {
        JsonPointer pointer = JsonPointer.compile("/foo/0");
        assertTrue(pointer.matches());
        assertFalse(pointer.mayMatchProperty());
        assertTrue(pointer.mayMatchElement());
        assertEquals("foo", pointer.getMatchingProperty());
        assertEquals(0, pointer.getMatchingIndex());
        assertEquals("/foo/0", pointer.toString());

        JsonPointer tailPointer = pointer.tail();
        assertFalse(tailPointer.matches());
        assertNull(tailPointer.getMatchingProperty());
        assertNull(tailPointer.getMatchingIndex());
        assertEquals("/", tailPointer.toString());
    }

    @Test
    public void testMultipleSegmentsPointer() {
        JsonPointer pointer = JsonPointer.compile("/foo/bar/baz/quux");
        assertFalse(pointer.matches());
        assertFalse(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertEquals("foo", pointer.getMatchingProperty());
        assertNull(pointer.getMatchingIndex());
        assertEquals("/foo/bar/baz/quux", pointer.toString());

        JsonPointer property1Pointer = pointer.matchProperty("bar");
        assertSame(property1Pointer, JsonPointer.compile("/foo/bar"));
        JsonPointer property2Pointer = property1Pointer.matchProperty("baz");
        assertSame(property2Pointer, JsonPointer.compile("/foo/bar/baz"));
        JsonPointer property3Pointer = property2Pointer.matchProperty("quux");
        assertSame(property3Pointer, JsonPointer.compile("/foo/bar/baz/quux"));
        assertNull(property3Pointer.tail());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPointer() {
        JsonPointer.compile("~/foo");
    }
}