package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class AttributesTest {

    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");

        assertEquals("value1", attributes.get("key1"));
        assertEquals("", attributes.get("key2"));
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");

        assertEquals("value1", attributes.getIgnoreCase("key1"));
        assertEquals("", attributes.getIgnoreCase("key2"));
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");

        assertEquals("value1", attributes.get("key1"));

        attributes.put("key1", "value2");
        assertEquals("value2", attributes.get("key1"));

        attributes.put("key2", "value2");
        assertEquals("value2", attributes.get("key2"));
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("key1", true);

        assertFalse(attributes.hasKey("key1"));
        assertTrue(attributes.hasKeyIgnoreCase("key1"));
        assertNull(attributes.get("key1"));
        assertNull(attributes.getIgnoreCase("key1"));

        attributes.put("key1", false);
        assertFalse(attributes.hasKey("key1"));
        assertFalse(attributes.hasKeyIgnoreCase("key1"));
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");

        assertEquals("value1", attributes.get("key1"));
        attributes.remove("key1");
        assertEquals("", attributes.get("key1"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");

        assertEquals("value1", attributes.get("key1"));
        attributes.removeIgnoreCase("key1");
        assertEquals("", attributes.get("key1"));
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");

        assertTrue(attributes.hasKey("key1"));
        assertFalse(attributes.hasKeyIgnoreCase("key2"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");

        assertTrue(attributes.hasKeyIgnoreCase("key1"));
        assertTrue(attributes.hasKeyIgnoreCase("Key1"));
        assertFalse(attributes.hasKeyIgnoreCase("key2"));
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
        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");
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

        List<String> keys = new ArrayList<>();
        for (Attribute attribute : attributes) {
            keys.add(attribute.getKey());
        }

        List<String> expectedKeys = Arrays.asList("key1", "key2");
        assertEquals(expectedKeys, keys);
    }

    @Test
    public void testAsList() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        List<Attribute> list = attributes.asList();
        assertEquals(2, list.size());
        assertEquals("value1", list.get(0).getValue());
        assertEquals("value2", list.get(1).getValue());
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");
        attributes.put("key2", "value2");

        Map<String, String> dataset = attributes.dataset();
        assertEquals(1, dataset.size());
        assertEquals("value1", dataset.get("data-key1"));
    }

    @Test
    public void testHtml() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");

        StringBuilder expectedHtml = new StringBuilder(" key1=\"value1\"");
        StringBuilder actualHtml = new StringBuilder();
        try {
            attributes.html(actualHtml, null);
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertEquals(expectedHtml.toString(), actualHtml.toString());
    }

    @Test
    public void testToString() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");

        StringBuilder expectedHtml = new StringBuilder(" key1=\"value1\"");
        assertEquals(expectedHtml.toString(), attributes.toString());
    }

    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        Attributes attributes3 = new Attributes();

        attributes1.put("key1", "value1");
        attributes2.put("key1", "value1");
        attributes3.put("key2", "value1");

        assertTrue(attributes1.equals(attributes1));
        assertTrue(attributes1.equals(attributes2));
        assertFalse(attributes1.equals(attributes3));
    }

    @Test
    public void testClone() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");

        Attributes clonedAttributes = attributes.clone();

        assertEquals(attributes.size(), clonedAttributes.size());
        assertEquals("value1", clonedAttributes.get("key1"));
    }
}