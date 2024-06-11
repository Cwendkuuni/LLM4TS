package com.fasterxml.jackson.core.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonPointer;

import org.junit.Test;

/**
 * Test class for {@link JsonPointer} class.
 */
public class JsonPointerTest {

    /**
     * Test for the empty JsonPointer constructor.
     */
    @Test
    public void testEmptyJsonPointer() {
        JsonPointer jsonPointer = JsonPointer.EMPTY;
        assertTrue(jsonPointer.matches());
        assertEquals("", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertNull(jsonPointer.tail());
        assertEquals("", jsonPointer.toString());
    }

    /**
     * Test for the JsonPointer constructor with a full string.
     */
    @Test
    public void testJsonPointerWithFullString() {
        String input = "/foo/bar/1";
        JsonPointer jsonPointer = JsonPointer.compile(input);
        assertFalse(jsonPointer.matches());
        assertEquals("foo", jsonPointer.getMatchingProperty());
        assertEquals(1, jsonPointer.getMatchingIndex());
        assertNotNull(jsonPointer.tail());
        assertEquals("/bar", jsonPointer.tail().getMatchingProperty());
        assertEquals(-1, jsonPointer.tail().getMatchingIndex());
        assertEquals(input, jsonPointer.toString());
    }

    /**
     * Test for the JsonPointer constructor with an empty segment list.
     */
    @Test
    public void testJsonPointerWithEmptySegmentList() {
        String input = "";
        JsonPointer jsonPointer = JsonPointer.compile(input);
        assertTrue(jsonPointer.matches());
        assertEquals("", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertNull(jsonPointer.tail());
        assertEquals("", jsonPointer.toString());
    }

    /**
     * Test for the JsonPointer factory method with a null input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testJsonPointerWithNullInput() {
        JsonPointer.compile(null);
    }

    /**
     * Test for the JsonPointer factory method with an input that does not start with a slash.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testJsonPointerWithInvalidInput() {
        String input = "foo";
        JsonPointer.compile(input);
    }

    /**
     * Test for the JsonPointer factory method with a valid input.
     */
    @Test
    public void testJsonPointerWithValidInput() {
        String input = "/foo/bar/1";
        JsonPointer jsonPointer = JsonPointer.compile(input);
        assertFalse(jsonPointer.matches());
        assertEquals("foo", jsonPointer.getMatchingProperty());
        assertEquals(1, jsonPointer.getMatchingIndex());
        assertNotNull(jsonPointer.tail());
        assertEquals("/bar", jsonPointer.tail().getMatchingProperty());
        assertEquals(-1, jsonPointer.tail().getMatchingIndex());
        assertEquals(input, jsonPointer.toString());
    }

    /**
     * Test for the JsonPointer method {@link JsonPointer#matches()}.
     */
    @Test
    public void testJsonPointerMatches() {
        String input1 = "/foo/bar/1";
        JsonPointer jsonPointer1 = JsonPointer.compile(input1);
        String input2 = "/foo/bar/1";
        JsonPointer jsonPointer2 = JsonPointer.compile(input2);
        String input3 = "/foo/bar/2";
        JsonPointer jsonPointer3 = JsonPointer.compile(input3);

        assertTrue(jsonPointer1.matches());
        assertTrue(jsonPointer2.matches());
        assertFalse(jsonPointer3.matches());
    }

    /**
     * Test for the JsonPointer method {@link JsonPointer#getMatchingProperty()}.
     */
    @Test
    public void testJsonPointerGetMatchingProperty() {
        String input1 = "/foo/bar/1";
        JsonPointer jsonPointer1 = JsonPointer.compile(input1);
        String input2 = "/foo/qux/1";
        JsonPointer jsonPointer2 = JsonPointer.compile(input2);

        assertEquals("foo", jsonPointer1.getMatchingProperty());
        assertEquals("qux", jsonPointer2.getMatchingProperty());
        assertNull(JsonPointer.EMPTY.getMatchingProperty());
    }

    /**
     * Test for the JsonPointer method {@link JsonPointer#getMatchingIndex()}.
     */
    @Test
    public void testJsonPointerGetMatchingIndex() {
        String input1 = "/foo/bar/1";
        JsonPointer jsonPointer1 = JsonPointer.compile(input1);
        String input2 = "/foo/bar/2";
        JsonPointer jsonPointer2 = JsonPointer.compile(input2);
        String input3 = "/foo/bar";
        JsonPointer jsonPointer3 = JsonPointer.compile(input3);

        assertEquals(1, jsonPointer1.getMatchingIndex());
        assertEquals(-1, jsonPointer2.getMatchingIndex());
        assertNull(jsonPointer3.getMatchingIndex());
    }

    /**
     * Test for the JsonPointer method {@link JsonPointer#mayMatchProperty()}.
     */
    @Test
    public void testJsonPointerMayMatchProperty() {
        String input1 = "/foo/bar/1";
        JsonPointer jsonPointer1 = JsonPointer.compile(input1);
        String input2 = "/foo";
        JsonPointer jsonPointer2 = JsonPointer.compile(input2);

        assertTrue(jsonPointer1.mayMatchProperty());
        assertTrue(jsonPointer2.mayMatchProperty());
        assertFalse(JsonPointer.EMPTY.mayMatchProperty());
    }

    /**
     * Test for the JsonPointer method {@link JsonPointer#mayMatchElement()}.
     */
    @Test
    public void testJsonPointerMayMatchElement() {
        String input1 = "/foo/bar/1";
        JsonPointer jsonPointer1 = JsonPointer.compile(input1);
        String input2 = "/foo/bar";
        JsonPointer jsonPointer2 = JsonPointer.compile(input2);

        assertTrue(jsonPointer1.mayMatchElement());
        assertFalse(jsonPointer2.mayMatchElement());
        assertFalse(JsonPointer.EMPTY.mayMatchElement());
    }

    /**
     * Test for the JsonPointer method {@link JsonPointer#matchProperty(String)}.
     */
    @Test
    public void testJsonPointerMatchProperty() {
        String input1 = "/foo/bar/1";
        JsonPointer jsonPointer1 = JsonPointer.compile(input1);
        String input2 = "/foo/bar/1";
        JsonPointer jsonPointer2 = JsonPointer.compile(input2);
        String input3 = "/qux/bar/1";
        JsonPointer jsonPointer3 = JsonPointer.compile(input3);

        assertEquals(jsonPointer2, jsonPointer1.matchProperty("/foo/bar"));
        assertNull(jsonPointer1.matchProperty("/qux/bar"));
        assertNull(jsonPointer1.matchProperty("/baz"));
        assertNull(JsonPointer.EMPTY.matchProperty("/"));
    }

    /**
     * Test for the JsonPointer method {@link JsonPointer#matchElement(int)}.
     */
    @Test
    public void testJsonPointerMatchElement() {
        String input1 = "/foo/bar/1";
        JsonPointer jsonPointer1 = JsonPointer.compile(input1);
        String input2 = "/foo/bar/2";
        JsonPointer jsonPointer2 = JsonPointer.compile(input2);
        String input3 = "/foo/qux/1";
        JsonPointer jsonPointer3 = JsonPointer.compile(input3);

        assertEquals(jsonPointer2, jsonPointer1.matchElement(2));
        assertNull(jsonPointer1.matchElement(3));
        assertNull(jsonPointer1.matchElement(-1));
        assertNull(JsonPointer.EMPTY.matchElement(0));
    }

    /**
     * Test for the JsonPointer method {@link JsonPointer#tail()}.
     */
    @Test
    public void testJsonPointerTail() {
        String input1 = "/foo/bar/1";
        JsonPointer jsonPointer1 = JsonPointer.compile(input1);
        String input2 = "/foo/bar/2";
        JsonPointer jsonPointer2 = JsonPointer.compile(input2);

        assertEquals(jsonPointer2, jsonPointer1.tail());
        assertNull(JsonPointer.EMPTY.tail());
    }

    /**
     * Test for the JsonPointer method {@link JsonPointer#toString()}.
     */
    @Test
    public void testJsonPointerToString() {
        String input1 = "/foo/bar/1";
        JsonPointer jsonPointer1 = JsonPointer.compile(input1);
        String input2 = "/foo/qux/1";
        JsonPointer jsonPointer2 = JsonPointer.compile(input2);
        String input3 = "";
        JsonPointer jsonPointer3 = JsonPointer.EMPTY;

        assertEquals(input1, jsonPointer1.toString());
        assertEquals(input2, jsonPointer2.toString());
        assertEquals("", jsonPointer3.toString());
    }

    /**
     * Test for the JsonPointer method {@link JsonPointer#equals(Object)}.
     */
    @Test
    public void testJsonPointerEquals() {
        String input1 = "/foo/bar/1";
        JsonPointer jsonPointer1 = JsonPointer.compile(input1);
        String input2 = "/foo/bar/1";
        JsonPointer jsonPointer2 = JsonPointer.compile(input2);
        String input3 = "/foo/qux/1";
        JsonPointer jsonPointer3 = JsonPointer.compile(input3);
        String input4 = "/foo/bar/1";
        JsonPointer jsonPointer4 = JsonPointer.compile(input4);
        Object obj1 = new Object();

        assertTrue(jsonPointer1.equals(jsonPointer1));
        assertTrue(jsonPointer1.equals(jsonPointer4));
        assertFalse(jsonPointer1.equals(obj1));
        assertFalse(jsonPointer1.equals(jsonPointer2));
        assertFalse(jsonPointer1.equals(jsonPointer3));
    }

    /**
     * Test for the JsonPointer method {@link JsonPointer#hashCode()}.
     */
    @Test
    public void testJsonPointerHashCode() {
        String input1 = "/foo/bar/1";
        JsonPointer jsonPointer1 = JsonPointer.compile(input1);
        String input2 = "/foo/bar/1";
        JsonPointer jsonPointer2 = JsonPointer.compile(input2);
        String input3 = "/foo/qux/1";
        JsonPointer jsonPointer3 = JsonPointer.compile(input3);

        int hashCode1 = jsonPointer1.hashCode();
        int hashCode2 = jsonPointer2.hashCode();
        int hashCode3 = jsonPointer3.hashCode();

        assertEquals(hashCode1, hashCode1);
        assertEquals(hashCode1, hashCode4);
        assertNotEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
    }
}