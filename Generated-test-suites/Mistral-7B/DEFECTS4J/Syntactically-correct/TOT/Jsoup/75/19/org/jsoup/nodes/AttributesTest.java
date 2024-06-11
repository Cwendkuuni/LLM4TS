package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class AttributesTest {

    @Test
    public void testGetAndPut() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        assertEquals("value1", attributes.get("key1"));
        attributes.put("key1", "value2");
        assertEquals("value2", attributes.get("key1"));
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        assertEquals("value1", attributes.getIgnoreCase("key1"));
        attributes.put("key2", "value2");
        assertEquals("value2", attributes.getIgnoreCase("key2"));
        assertEquals("value1", attributes.getIgnoreCase("Key1"));
    }

    @Test
    public void testPutIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.putIgnoreCase("Key1", "value2");
        assertEquals("value2", attributes.getIgnoreCase("key1"));
        assertEquals("value2", attributes.getIgnoreCase("Key1"));
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("key1", true);
        assertFalse(attributes.get("key1").equals("true"));
        assertEquals(null, attributes.get("key1"));
        assertFalse(attributes.hasKey("key1"));

        attributes.put("key1", false);
        assertTrue(attributes.hasKeyIgnoreCase("key1"));
        assertEquals(null, attributes.get("key1"));
        assertFalse(attributes.hasKey("key1"));
    }

    @Test
    public void testPutBooleanSetFalse() {
        Attributes attributes = new Attributes();
        attributes.put("key1", true);
        attributes.put("key1", false);
        assertFalse(attributes.hasKey("key1"));
    }

    @Test(expected = NullPointerException.class)
    public void testNullKey() {
        Attributes attributes = new Attributes();
        attributes.put(null, "value");
    }

    @Test(expected = NullPointerException.class)
    public void testNullValue() {
        Attributes attributes = new Attributes();
        attributes.put("key", null);
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        assertTrue(attributes.hasKey("key1"));
        attributes.remove("key1");
        assertFalse(attributes.hasKey("key1"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.removeIgnoreCase("key1");
        assertFalse(attributes.hasKey("key1"));
        assertTrue(attributes.hasKey("key2"));
    }

    @Test
    public void testSizeAndAddAll() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");

        attributes2.put("key2", "value3");
        attributes2.put("key3", "value3");

        attributes1.addAll(attributes2);
        assertEquals(3, attributes1.size());
        assertEquals("value3", attributes1.get("key2"));
        assertEquals("value3", attributes1.get("key3"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIterableRemove() {
        Attributes attributes = new Attributes();
        Iterator<Attribute> iterator = attributes.iterator();
        iterator.next();
        iterator.remove();
    }

    @Test(expected = NullPointerException.class)
    public void testNullAttributes() {
        Attributes attributes = null;
        attributes.get("key");
    }
}