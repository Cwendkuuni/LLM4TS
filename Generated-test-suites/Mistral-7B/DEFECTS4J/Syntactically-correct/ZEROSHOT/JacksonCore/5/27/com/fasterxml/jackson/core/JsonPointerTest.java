package com.fasterxml.jackson.core.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.core.JsonPointer;

import org.junit.Test;

/**
 * Test class for {@link JsonPointer} class.
 */
public class JsonPointerTest {

    /**
     * Test constructor for empty JsonPointer instance.
     */
    @Test
    public void testEmptyJsonPointer() {
        JsonPointer jsonPointer = JsonPointer.EMPTY;
        assertTrue(jsonPointer.matches());
        assertNull(jsonPointer.getMatchingProperty());
        assertFalse(jsonPointer.mayMatchProperty());
        assertFalse(jsonPointer.mayMatchElement());
        assertEquals("", jsonPointer.toString());
    }

    /**
     * Test constructor for non-empty JsonPointer instance.
     */
    @Test
    public void testNonEmptyJsonPointer() {
        String fullString = "/path/to/node1/property1";
        String segment = "property1";
        JsonPointer next = new JsonPointer("/path/to/node1/property2", "", EMPTY);

        JsonPointer jsonPointer = new JsonPointer(fullString, segment, next);

        assertFalse(jsonPointer.matches());
        assertEquals("property1", jsonPointer.getMatchingProperty());
        assertTrue(jsonPointer.mayMatchProperty());
        assertFalse(jsonPointer.mayMatchElement());
        assertEquals(fullString, jsonPointer.toString());

        assertEquals(next, jsonPointer.tail());
        assertEquals("/path/to/node1/property2", next.toString());
    }

    /**
     * Test factory method {@link JsonPointer#compile(String)}.
     */
    @Test
    public void testJsonPointerCompile() {
        String jsonPointerInput = "/path/to/node1/property1";

        JsonPointer jsonPointer = JsonPointer.compile(jsonPointerInput);

        assertFalse(jsonPointer.matches());
        assertEquals("property1", jsonPointer.getMatchingProperty());
        assertTrue(jsonPointer.mayMatchProperty());
        assertFalse(jsonPointer.mayMatchElement());
        assertEquals(jsonPointerInput, jsonPointer.toString());
    }

    /**
     * Test factory method {@link JsonPointer#valueOf(String)}.
     */
    @Test
    public void testJsonPointerValueOf() {
        String jsonPointerInput = "/path/to/node1/property1";

        JsonPointer jsonPointer = JsonPointer.valueOf(jsonPointerInput);

        assertFalse(jsonPointer.matches());
        assertEquals("property1", jsonPointer.getMatchingProperty());
        assertTrue(jsonPointer.mayMatchProperty());
        assertFalse(jsonPointer.mayMatchElement());
        assertEquals(jsonPointerInput, jsonPointer.toString());
    }

    /**
     * Test method {@link JsonPointer#matches()}.
     */
    @Test
    public void testJsonPointerMatches() {
        String jsonPointerInput = "/path/to/node1/property1";

        JsonPointer jsonPointer = JsonPointer.compile(jsonPointerInput);
        JsonPointer emptyJsonPointer = JsonPointer.EMPTY;

        assertTrue(jsonPointer.matches());
        assertFalse(emptyJsonPointer.matches());
        assertFalse(jsonPointer.equals(emptyJsonPointer));
    }

    /**
     * Test method {@link JsonPointer#getMatchingProperty()}.
     */
    @Test
    public void testJsonPointerGetMatchingProperty() {
        String jsonPointerInput = "/path/to/node1/property1";

        JsonPointer jsonPointer = JsonPointer.compile(jsonPointerInput);

        assertEquals("property1", jsonPointer.getMatchingProperty());
        assertNull(JsonPointer.EMPTY.getMatchingProperty());
    }

    /**
     * Test method {@link JsonPointer#mayMatchProperty()}.
     */
    @Test
    public void testJsonPointerMayMatchProperty() {
        String jsonPointerInput = "/path/to/node1/property1";

        JsonPointer jsonPointer = JsonPointer.compile(jsonPointerInput);

        assertTrue(jsonPointer.mayMatchProperty());
        assertFalse(JsonPointer.EMPTY.mayMatchProperty());
    }

    /**
     * Test method {@link JsonPointer#mayMatchElement()}.
     */
    @Test
    public void testJsonPointerMayMatchElement() {
        String jsonPointerInput = "/path/to/node1/property1/0";

        JsonPointer jsonPointer = JsonPointer.compile(jsonPointerInput);

        assertTrue(jsonPointer.mayMatchElement());
        assertFalse(JsonPointer.EMPTY.mayMatchElement());
    }

    /**
     * Test method {@link JsonPointer#matchProperty(String)}.
     */
    @Test
    public void testJsonPointerMatchProperty() {
        String jsonPointerInput1 = "/path/to/node1/property1";
        String jsonPointerInput2 = "/path/to/node1/property2";

        JsonPointer jsonPointer1 = JsonPointer.compile(jsonPointerInput1);
        JsonPointer jsonPointer2 = JsonPointer.compile(jsonPointerInput2);

        JsonPointer matchedJsonPointer = jsonPointer1.matchProperty("property1");
        assertEquals(jsonPointer1, matchedJsonPointer);
        assertNull(jsonPointer1.matchProperty("property2"));
        assertNull(jsonPointer2.matchProperty("property1"));
    }

    /**
     * Test method {@link JsonPointer#matchElement(int)}.
     */
    @Test
    public void testJsonPointerMatchElement() {
        String jsonPointerInput = "/path/to/node1/property1/0";

        JsonPointer jsonPointer = JsonPointer.compile(jsonPointerInput);

        JsonPointer matchedJsonPointer = jsonPointer.matchElement(0);
        assertEquals(jsonPointer, matchedJsonPointer);
        assertNull(jsonPointer.matchElement(-1));
    }

    /**
     * Test method {@link JsonPointer#toString()}.
     */
    @Test
    public void testJsonPointerToString() {
        String jsonPointerInput1 = "/path/to/node1/property1";
        String jsonPointerInput2 = "/path/to/node1/property1/0";

        JsonPointer jsonPointer1 = JsonPointer.compile(jsonPointerInput1);
        JsonPointer jsonPointer2 = JsonPointer.compile(jsonPointerInput2);

        assertEquals(jsonPointerInput1, jsonPointer1.toString());
        assertEquals(jsonPointerInput2, jsonPointer2.toString());
    }

    /**
     * Test method {@link JsonPointer#hashCode()}.
     */
    @Test
    public void testJsonPointerHashCode() {
        String jsonPointerInput1 = "/path/to/node1/property1";
        String jsonPointerInput2 = "/path/to/node1/property1";
        String jsonPointerInput3 = "/path/to/node1/property2";

        JsonPointer jsonPointer1 = JsonPointer.compile(jsonPointerInput1);
        JsonPointer jsonPointer2 = JsonPointer.compile(jsonPointerInput2);
        JsonPointer jsonPointer3 = JsonPointer.compile(jsonPointerInput3);

        assertEquals(jsonPointerInput1.hashCode(), jsonPointer1.hashCode());
        assertEquals(jsonPointerInput2.hashCode(), jsonPointer2.hashCode());
        assertNotEquals(jsonPointer1.hashCode(), jsonPointer2.hashCode());
        assertNotEquals(jsonPointer1.hashCode(), jsonPointer3.hashCode());
    }

    /**
     * Test method {@link JsonPointer#equals(Object)}.
     */
    @Test
    public void testJsonPointerEquals() {
        String jsonPointerInput1 = "/path/to/node1/property1";
        String jsonPointerInput2 = "/path/to/node1/property1";
        String jsonPointerInput3 = "/path/to/node1/property2";

        JsonPointer jsonPointer1 = JsonPointer.compile(jsonPointerInput1);
        JsonPointer jsonPointer2 = JsonPointer.compile(jsonPointerInput2);
        JsonPointer jsonPointer3 = JsonPointer.compile(jsonPointerInput3);

        assertTrue(jsonPointer1.equals(jsonPointer1));
        assertTrue(jsonPointer1.equals(jsonPointer2));
        assertFalse(jsonPointer1.equals(jsonPointer3));
        assertFalse(jsonPointer1.equals(new Object()));
    }
}