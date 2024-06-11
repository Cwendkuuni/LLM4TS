package org.jsoup.nodes;

import org.junit.Test;
import org.junit.Assert.*;

import static org.jsoup.nodes.Attributes.*;

public class AttributesTest {

    @Test
    public void testBasicUsage() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertEquals("value", attributes.get("key"));
        assertEquals(1, attributes.size());
        attributes.put("key2", "value2");
        assertEquals("value2", attributes.get("key2"));
        assertEquals(2, attributes.size());
        assertEquals(false, attributes.hasKey("nonexistent"));
        assertEquals("", attributes.get("key").toLowerCase());
    }

    @Test
    public void testCaseInsensitiveGet() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertEquals("value", attributes.getIgnoreCase("key"));
        assertEquals("value", attributes.getIgnoreCase("KEY"));
        assertEquals("", attributes.getIgnoreCase("nonexistent"));
    }

    @Test
    public void testPutWithSameKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value1");
        attributes.put("key", "value2");
        assertEquals("value2", attributes.get("key"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullKey() {
        Attributes attributes = new Attributes();
        attributes.put(null, "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullValue() {
        Attributes attributes = new Attributes();
        attributes.put("key", null);
    }

    @Test
    public void testBooleanAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("checked", true);
        assertTrue(attributes.hasKey("checked"));
        assertEquals("checked", attributes.getIgnoreCase("checked"));
        assertFalse(attributes.get("checked").isEmpty());
        attributes.put("checked", false);
        assertFalse(attributes.hasKey("checked"));
        assertFalse(attributes.getIgnoreCase("checked").isEmpty());
        assertNull(attributes.get("checked"));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveByIndex() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.remove(1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveByNameWithNull() {
        Attributes attributes = new Attributes();
        attributes.remove(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveByNameWithNonexistent() {
        Attributes attributes = new Attributes();
        attributes.remove("nonexistent");
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.remove("key1");
        assertFalse(attributes.hasKey("key1"));
        assertFalse(attributes.getIgnoreCase("key1").isEmpty());
        assertNull(attributes.get("key1"));
        assertEquals(1, attributes.size());
    }

    @Test
    public void testGetEmpty() {
        Attributes attributes = new Attributes();
        assertEquals("", attributes.get("nonexistent"));
        assertEquals("", attributes.getIgnoreCase("nonexistent"));
    }

    @Test
    public void testClone() {
        Attributes originalAttributes = new Attributes();
        originalAttributes.put("key1", "value1");
        originalAttributes.put("key2", "value2");
        Attributes clonedAttributes = originalAttributes.clone();
        assertEquals(originalAttributes, clonedAttributes);
        assertEquals(originalAttributes.get("key1"), clonedAttributes.get("key1"));
        assertEquals(originalAttributes.get("key2"), clonedAttributes.get("key2"));
    }
}