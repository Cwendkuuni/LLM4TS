package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class AttributesTest {

    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertEquals("value", attributes.get("key"));
        assertEquals("", attributes.get("nonexistentkey"));
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertEquals("value", attributes.getIgnoreCase("key"));
        assertEquals("", attributes.getIgnoreCase("NONEEXISTENTKEY"));
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertEquals("value", attributes.get("key"));

        Attributes attributes2 = new Attributes();
        attributes2.put("key", "othervalue");
        assertEquals("othervalue", attributes2.get("key"));

        attributes.put("key", "newvalue");
        assertEquals("newvalue", attributes.get("key"));
        assertNotSame(attributes.get("key"), attributes2.get("key"));
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        assertFalse(attributes.hasKey("key"));
        attributes.put("key", true);
        assertTrue(attributes.hasKey("key"));
        assertNull(attributes.get("key"));
        assertFalse(attributes.hasKey("nonexistentkey"));
    }

    @Test
    public void testPutAttribute() {
        Attributes attributes = new Attributes();
        attributes.put(new Attribute("key", "value"));
        assertEquals("value", attributes.get("key"));

        Attributes attributes2 = new Attributes();
        attributes2.put(new Attribute("key", "othervalue"));
        assertEquals("othervalue", attributes2.get("key"));

        attributes.put(new Attribute("key", "newvalue"));
        assertEquals("newvalue", attributes.get("key"));
        assertNotSame(attributes.get("key"), attributes2.get("key"));
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        assertFalse(attributes.hasKey("key"));
        attributes.put("key", "value");
        assertTrue(attributes.hasKey("key"));
        assertEquals("value", attributes.get("key"));
        attributes.remove("key");
        assertFalse(attributes.hasKey("key"));
        assertNull(attributes.get("key"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        assertFalse(attributes.hasKey("key"));
        attributes.put("key", "value");
        assertTrue(attributes.hasKey("key"));
        assertEquals("value", attributes.get("key"));
        attributes.removeIgnoreCase("Key");
        assertFalse(attributes.hasKey("key"));
        assertNull(attributes.get("key"));
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        assertFalse(attributes.hasKey("key"));
        attributes.put("key", "value");
        assertTrue(attributes.hasKey("key"));
        assertFalse(attributes.hasKey("nonexistentkey"));
        assertFalse(attributes.hasKeyIgnoreCase("nonexistentkey"));
        assertFalse(attributes.hasKeyIgnoreCase("KEY"));
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        assertEquals(0, attributes.size());
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
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put(new Attribute("data-key1", "value1"));
        attributes.put(new Attribute("data-key2", "value2"));
        Map<String, String> dataset = attributes.dataset();
        assertEquals(2, dataset.size());
        assertEquals("value1", dataset.get("data-key1"));
        assertEquals("value2", dataset.get("data-key2"));
        assertNull(dataset.get("key1"));
    }

    @Test
    public void testHtml() {
        Attributes attributes = new Attributes();
        StringBuilder expected = new StringBuilder();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        try {
            attributes.html(expected, new Document.OutputSettings());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
        assertEquals("<key1>value1</key1><key2>value2</key2>", expected.toString());
    }

    @Test
    public void testClone() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        Attributes clone = attributes.clone();
        assertEquals("value1", clone.get("key1"));
        assertEquals(1, clone.size());
        attributes.put("key2", "value2");
        assertEquals(1, attributes.size());
        assertNotSame(attributes, clone);
    }

    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        Attributes attributes3 = new Attributes();
        attributes1.put("key1", "value1");
        attributes2.put("key1", "value1");
        attributes3.put("key2", "value2");
        assertTrue(attributes1.equals(attributes1));
        assertTrue(attributes1.equals(attributes2));
        assertFalse(attributes1.equals(attributes3));
        assertFalse(attributes1.equals(null));
        assertFalse(attributes1.equals(""));
    }

    @Test
    public void testHashCode() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        Attributes attributes3 = new Attributes();
        attributes1.put("key1", "value1");
        attributes2.put("key1", "value1");
        attributes3.put("key2", "value2");
        assertEquals(attributes1.hashCode(), attributes1.hashCode());
        assertEquals(attributes1.hashCode(), attributes2.hashCode());
        assertNotEquals(attributes1.hashCode(), attributes3.hashCode());
    }

    @Test
    public void testIterator() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        List<Attribute> expected = Arrays.asList(new Attribute("key1", "value1"), new Attribute("key2", "value2"));
        List<Attribute> actual = new ArrayList<>(attributes.asList());
        assertEquals(expected, actual);
    }

    @Test
    public void testAsList() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        List<Attribute> expected = Arrays.asList(new Attribute("key1", "value1"), new Attribute("key2", "value2"));
        List<Attribute> actual = attributes.asList();
        assertEquals(expected, actual);
    }

    @Test(expected = SerializationException.class)
    public void testHtmlSerializationException() {
        Attributes attributes = new Attributes();
        StringBuilder expected = new StringBuilder();
        try {
            attributes.html(expected, new Document.OutputSettings());
        } catch (IOException e) {
            throw new SerializationException(e);
        }
        fail("SerializationException should be thrown");
    }
}