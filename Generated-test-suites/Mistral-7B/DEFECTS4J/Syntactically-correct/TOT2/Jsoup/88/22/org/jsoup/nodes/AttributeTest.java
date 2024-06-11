package org.junit.test;

import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeTest {

    @Test
    public void testAttributeConstruction() {
        String key = "testKey";
        String value = "testValue";
        Attribute attribute = new Attribute(key, value);
        assertEquals(key, attribute.getKey());
        assertEquals(value, attribute.getValue());
    }

    @Test
    public void testSetKey() {
        String key = "testKey";
        String newKey = "newTestKey";
        Attribute attribute = new Attribute(key, "testValue");
        attribute.setKey(newKey);
        assertEquals(newKey, attribute.getKey());

        try {
            attribute.setKey(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }

        try {
            attribute.setKey("");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        Attributes parent = null; // Assuming Attributes is a valid test object
        try {
            attribute.setKey("testKey");
            attribute.setParent(parent); // Assume parent is a method
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testToString() {
        Attribute attribute = new Attribute("testKey", "testValue");
        String expected = "testKey=\"testValue\"";
        assertEquals(expected, attribute.toString());
    }

    // Add other test cases here...
}