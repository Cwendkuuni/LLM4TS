package com.fasterxml.jackson.core.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonPointer;

import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testEmptyPointer() {
        JsonPointer pointer = JsonPointer.EMPTY;
        assertTrue(pointer.matches());
        assertEquals("", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertNull(pointer.tail());
        assertEquals("", pointer.toString());
    }

    @Test
    public void testSimplePointer() {
        JsonPointer pointer = JsonPointer.compile("/test/property");
        assertFalse(pointer.matches());
        assertEquals("test", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertNotNull(pointer.tail());
        assertEquals("/test", pointer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPointer() {
        JsonPointer.compile("test");
    }

    @Test
    public void testMultipleSegmentsPointer() {
        JsonPointer pointer = JsonPointer.compile("/test/property1/property2");
        assertFalse(pointer.matches());
        assertEquals("property1", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertNotNull(pointer.tail());
        assertEquals("/test/property1", pointer.toString());
    }

    @Test
    public void testMatchesProperty() {
        JsonPointer pointer = JsonPointer.compile("/test/property1/property2");
        JsonPointer match = new JsonPointer("/test/property1");
        JsonPointer expectedMatch = pointer.matchProperty("property2");
        assertNull(expectedMatch);
        match = pointer.matchProperty("property1");
        assertEquals(pointer.tail(), match);
    }

    @Test
    public void testMatchesElement() {
        JsonPointer pointer = JsonPointer.compile("/test/0/property2");
        JsonPointer match = new JsonPointer("/test/0");
        JsonPointer expectedMatch = pointer.matchElement(1);
        assertNull(expectedMatch);
        match = pointer.matchElement(0);
        assertEquals(pointer.tail(), match);
    }

    @Test
    public void testMatchLastSegment() {
        JsonPointer pointer = JsonPointer.compile("/test/0/property2/0");
        JsonPointer match = new JsonPointer("/test/0/property2");
        JsonPointer expectedMatch = pointer.tail();
        assertEquals(expectedMatch, match);
    }

    @Test
    public void testTail() {
        JsonPointer pointer = JsonPointer.compile("/test/property1/property2");
        JsonPointer tail = pointer.tail();
        assertEquals(JsonPointer.compile("/test/property1"), tail);
    }

    @Test
    public void testEquals() {
        JsonPointer pointer1 = JsonPointer.compile("/test/property1/property2");
        JsonPointer pointer2 = new JsonPointer("/test/property1/property2", null, pointer1);
        JsonPointer pointer3 = JsonPointer.compile("/test/property1/property2");
        JsonPointer pointer4 = JsonPointer.compile("/test/property1");

        assertEquals(pointer1, pointer2);
        assertEquals(pointer1, pointer3);
        assertNotEquals(pointer1, pointer4);
    }

    @Test
    public void testHashCode() {
        JsonPointer pointer1 = JsonPointer.compile("/test/property1/property2");
        JsonPointer pointer2 = new JsonPointer("/test/property1/property2", null, pointer1);
        JsonPointer pointer3 = JsonPointer.compile("/test/property1/property2");
        JsonPointer pointer4 = JsonPointer.compile("/test/property1");

        int hashCode1 = pointer1.hashCode();
        int hashCode2 = pointer2.hashCode();
        int hashCode3 = pointer3.hashCode();
        int hashCode4 = pointer4.hashCode();

        assertEquals(hashCode1, hashCode2);
        assertEquals(hashCode1, hashCode3);
        assertNotEquals(hashCode1, hashCode4);
    }
}