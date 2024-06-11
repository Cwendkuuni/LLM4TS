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
        assertFalse(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.tail());
        assertEquals("", pointer.toString());
    }

    @Test
    public void testBasicPointer() {
        String input = "/foo/bar/1";
        JsonPointer pointer = JsonPointer.compile(input);
        assertTrue(pointer.matches());
        assertEquals("foo", pointer.getMatchingProperty());
        assertFalse(pointer.mayMatchElement());
        assertEquals(pointer, pointer.tail());
        assertEquals("/foo/bar", pointer.toString());

        assertEquals(pointer.matchProperty("/foo"), pointer);
        assertEquals(pointer.matchProperty("/bar"), null);
        assertEquals(pointer.matchProperty("/baz"), null);

        assertEquals(pointer.matchElement(0), null);
        assertEquals(pointer.matchElement(1), null);
        assertEquals(pointer.matchElement(1), null); // This should pass due to the immutability

        JsonPointer tail = pointer.tail();
        assertFalse(tail.matches());
        assertEquals("/bar", tail.getMatchingProperty());
        assertTrue(tail.mayMatchElement());
        assertEquals(tail, tail.tail());
        assertEquals("/1", tail.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInput() {
        JsonPointer.compile("invalid");
    }

    @Test
    public void testEmptySegment() {
        JsonPointer pointer = JsonPointer.compile("/foo//bar");
        assertEquals("/foo/bar", pointer.toString());
        assertEquals(pointer.matchProperty("/foo"), pointer);
        assertEquals(pointer.matchProperty("/bar"), null);
    }

    @Test
    public void testEmptyArraySegment() {
        JsonPointer pointer = JsonPointer.compile("/foo/[]");
        assertEquals("/foo", pointer.toString());
        assertTrue(pointer.matches());
        assertFalse(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.tail());
    }

    @Test
    public void testIndexSegment() {
        String inputs[] = { "/foo/0", "/foo/-1", "/foo/1", "/foo/2" };
        for (String input : inputs) {
            JsonPointer pointer = JsonPointer.compile(input);
            assertEquals(input, pointer.toString());
            assertEquals(pointer.matchElement(0), null);
            assertEquals(pointer.matchElement(Integer.parseInt(input.substring(input.lastIndexOf("/") + 1))), null);
            JsonPointer tail = pointer.tail();
            if (tail != null) {
                assertEquals(tail.getMatchingIndex(), Integer.parseInt(input.substring(input.lastIndexOf("/") + 1)) - 1);
            }
        }
    }

    @Test
    public void testNestedPointer() {
        String input = "/a/b/c/d/0";
        JsonPointer pointer = JsonPointer.compile(input);
        assertEquals("/a/b/c/d", pointer.toString());
        assertEquals(pointer.matchProperty("/a"), pointer);
        assertEquals(pointer.matchProperty("/b"), null);
        assertEquals(pointer.matchProperty("/c"), null);
        assertEquals(pointer.matchProperty("/d"), null);
        assertFalse(pointer.mayMatchElement());
        assertEquals(pointer.matchElement(0), null);
        JsonPointer tail = pointer.tail();
        assertEquals("/d", tail.toString());
        assertEquals(tail.getMatchingIndex(), -1);
        assertEquals(tail.matchProperty("/d"), tail);
    }

    @Test
    public void testTraversal() {
        JsonPointer a = JsonPointer.compile("/a");
        JsonPointer b = JsonPointer.compile("/a/b");
        JsonPointer c = JsonPointer.compile("/a/b/c");
        JsonPointer d = JsonPointer.compile("/a/b/c/d");

        assertEquals(b, a.matchProperty("/b"));
        assertEquals(c, b.matchProperty("/c"));
        assertEquals(d, c.matchProperty("/d"));
    }

    @Test
    public void testQuotedSegments() {
        String input = "/a/b/~\"2\"/c";
        JsonPointer pointer = JsonPointer.compile(input);
        assertEquals("/a/b/2/c", pointer.toString());
        assertEquals(pointer.matchProperty("/a"), pointer);
        assertEquals(pointer.matchProperty("/b"), null);
        assertEquals(pointer.matchProperty("/c"), null);
        assertEquals(pointer.matchElement(1), null);
    }
}