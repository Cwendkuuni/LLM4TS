package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributesTest {

    private Attributes attributes;

    @Before
    public void setUp() {
        attributes = new Attributes();
        attributes.put("class", "test");
        attributes.put("id", "testId");
        attributes.put("title", "Test Title");
        attributes.put("data-customData", "customDataValue");
    }

    @Test
    public void testGet() {
        assertEquals("test", attributes.get("class"));
        assertEquals("testId", attributes.get("id"));
        assertEquals("Test Title", attributes.get("title"));
        assertEquals("", attributes.get("nonExistentKey"));
        assertEquals(null, attributes.get("booleanAttribute"));

        // Test case insensitive get
        assertEquals("Test Title", attributes.getIgnoreCase("Title"));
    }

    @Test
    public void testPut() {
        attributes.put("class", "newClass");
        assertEquals("newClass", attributes.get("class"));

        // Test put with existing key
        attributes.put("class", "newClass2");
        assertEquals("newClass2", attributes.get("class"));

        // Test put with case insensitive key
        attributes.putIgnoreCase("Class", "testClass");
        assertEquals("testClass", attributes.get("Class"));

        // Test put boolean attribute
        attributes.put("booleanAttribute", true);
        assertNull(attributes.get("booleanAttribute"));
        assertTrue(attributes.hasKey("booleanAttribute"));
        assertFalse(attributes.hasKey("BooleanAttribute"));

        // Test put an Attribute object
        Attribute attribute = new Attribute("data-customData2", "newCustomDataValue");
        attributes.put(attribute);
        assertEquals("newCustomDataValue", attributes.get("data-customData2"));
    }

    @Test
    public void testSize() {
        assertEquals(4, attributes.size());
    }

    @Test
    public void testRemove() {
        attributes.remove("class");
        assertEquals(3, attributes.size());
        assertFalse(attributes.hasKey("class"));

        // Test remove with case insensitive key
        attributes.removeIgnoreCase("Class");
        assertEquals(3, attributes.size());
        assertFalse(attributes.hasKey("class"));
        assertFalse(attributes.hasKey("Class"));
    }

    @Test
    public void testHasKey() {
        assertTrue(attributes.hasKey("class"));
        assertTrue(attributes.hasKey("id"));
        assertTrue(attributes.hasKey("title"));
        assertTrue(attributes.hasKey("data-customData"));
        assertFalse(attributes.hasKey("nonExistentKey"));
        assertFalse(attributes.hasKey("BooleanAttribute"));
        assertFalse(attributes.hasKeyIgnoreCase("NonExistentKey"));
        assertFalse(attributes.hasKeyIgnoreCase("BOOLEANATTRIBUTE"));
    }

    @Test
    public void testAddAll() {
        Attributes newAttributes = new Attributes();
        newAttributes.put("newKey", "newValue");

        attributes.addAll(newAttributes);
        assertEquals(5, attributes.size());
        assertTrue(attributes.hasKey("newKey"));
        assertEquals("newValue", attributes.get("newKey"));
    }

    @Test
    public void testClone() {
        Attributes clonedAttributes = attributes.clone();
        assertEquals(attributes.size(), clonedAttributes.size());

        for (String key : attributes.asList()) {
            assertEquals(attributes.get(key), clonedAttributes.get(key));
        }
    }

    @Test
    public void testNormalize() {
        attributes.put("CLASS", "test");
        attributes.normalize();
        assertEquals("test", attributes.get("class"));
        assertEquals(1, attributes.size());
    }
}