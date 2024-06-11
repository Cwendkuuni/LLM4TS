package org.jsoup.nodes;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attributes;

public class AttributesTest {

    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertEquals("value", attributes.get("key"));
        assertEquals("", attributes.get("unknown"));
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "Value");
        assertEquals("Value", attributes.getIgnoreCase("key"));
        assertEquals("", attributes.getIgnoreCase("Unknown"));
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertEquals("value", attributes.get("key"));
        attributes.put("key", "newValue");
        assertEquals("newValue", attributes.get("key"));
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("key", true);
        assertTrue(attributes.hasKey("key"));
        assertFalse(attributes.get("key").isEmpty());
        assertFalse(attributes.hasKey("key") && attributes.get("key").isEmpty());
        assertFalse(attributes.hasKey("key") && attributes.get("key").isDataAttribute());
        assertFalse(attributes.hasKeyIgnoreCase("key") && attributes.getIgnoreCase("key").isEmpty());
        assertFalse(attributes.hasKeyIgnoreCase("key") && attributes.getIgnoreCase("key").isDataAttribute());
    }

    @Test
    public void testPutAttribute() {
        Attributes attributes = new Attributes();
        attributes.put(new Attribute("key", "value"));
        assertEquals("value", attributes.get("key"));
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        attributes.remove("key");
        assertEquals("", attributes.get("key"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "Value");
        attributes.removeIgnoreCase("key");
        assertEquals("", attributes.getIgnoreCase("key"));
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertTrue(attributes.hasKey("key"));
        assertFalse(attributes.hasKey("unknown"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "Value");
        assertTrue(attributes.hasKeyIgnoreCase("key"));
        assertFalse(attributes.hasKeyIgnoreCase("Unknown"));
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assertEquals(2, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        Attributes attributes2 = new Attributes();
        attributes2.put("key2", "value2");
        attributes1.addAll(attributes2);
        assertEquals(2, attributes1.size());
        assertEquals("value1", attributes1.get("key1"));
        assertEquals("value2", attributes1.get("key2"));
    }

    @Test
    public void testIterator() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        List<Attribute> asList = attributes.asList();
        Iterator<Attribute> iterator = attributes.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            Attribute next = iterator.next();
            assertEquals(next, asList.get(i++));
        }
    }

    @Test
    public void testAsList() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        List<Attribute> asList = attributes.asList();
        assertEquals(2, asList.size());
        assertEquals("value1", asList.get(0).getValue());
        assertEquals("value2", asList.get(1).getValue());
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-key", "value");
        Map<String, String> dataset = attributes.dataset();
        assertEquals("value", dataset.get("key"));
    }

    @Test
    public void testHtml() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String html = attributes.html();
        String expected = " key=\"value\"";
        assertEquals(expected, html);
    }

    @Test
    public void testToString() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String toString = attributes.toString();
        String expected = attributes.html();
        assertEquals(expected, toString);
    }

    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");
        Attributes attributes3 = new Attributes();
        attributes3.put("key2", "value2");
        assertTrue(attributes1.equals(attributes1));
        assertTrue(attributes1.equals(attributes2));
        assertFalse(attributes1.equals(attributes3));
    }

    @Test
    public void testHashCode() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");
        Attributes attributes3 = new Attributes();
        attributes3.put("key2", "value2");
        int hashCode1 = attributes1.hashCode();
        int hashCode2 = attributes2.hashCode();
        int hashCode3 = attributes3.hashCode();
        assertEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
    }

    @Test
    public void testClone() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        Attributes clonedAttributes = attributes.clone();
        assertEquals("value", clonedAttributes.get("key"));
    }
}