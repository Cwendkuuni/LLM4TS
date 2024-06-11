package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.jsoup.helper.Validate;

import java.util.ArrayList;
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

        String value = attributes.get("key");
        assertEquals("value", value);

        value = attributes.get("empty");
        assertEquals("", value);
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "Value");

        String value = attributes.getIgnoreCase("key");
        assertEquals("Value", value);

        value = attributes.getIgnoreCase("KEY");
        assertEquals("Value", value);

        value = attributes.getIgnoreCase("empty");
        assertEquals("", value);
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();

        attributes.put("key", "value");
        assertEquals("value", attributes.get("key"));

        attributes.put("key", "newValue");
        assertEquals("newValue", attributes.get("key"));

        attributes.put("anotherKey", "anotherValue");
        assertEquals("anotherValue", attributes.get("anotherKey"));

        attributes.put(null, "nullValue");
        assertEquals("", attributes.get(""));
        assertEquals("nullValue", attributes.get(null));
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();

        attributes.put("key", true);
        assertTrue(attributes.hasKey("key"));
        assertFalse(attributes.hasKey("key") && attributes.size() > 1);

        attributes.put("key", false);
        assertFalse(attributes.hasKey("key"));
    }

    @Test
    public void testPutAttribute() {
        Attributes attributes = new Attributes();
        Attribute attribute = new Attribute("key", "value");

        attributes.put(attribute);
        assertEquals("value", attributes.get("key"));
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        attributes.remove("key");
        assertEquals("", attributes.get("key"));
        assertFalse(attributes.hasKey("key"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "Value");

        attributes.removeIgnoreCase("key");
        assertEquals("", attributes.get("Key"));
        assertFalse(attributes.hasKey("Key"));
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");

        assertTrue(attributes.hasKey("key"));
        assertFalse(attributes.hasKey("empty"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "Value");

        assertTrue(attributes.hasKeyIgnoreCase("key"));
        assertTrue(attributes.hasKeyIgnoreCase("Key"));
        assertFalse(attributes.hasKeyIgnoreCase("empty"));
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
        attributes2.put("key3", "value3");

        attributes1.addAll(attributes2);

        assertEquals(3, attributes1.size());
        assertTrue(attributes1.hasKey("key1"));
        assertTrue(attributes1.hasKey("key2"));
        assertTrue(attributes1.hasKey("key3"));
    }

    @Test
    public void testIterator() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        List<Attribute> list = new ArrayList<>();
        for (Attribute attribute : attributes) {
            list.add(attribute);
        }

        assertEquals(2, list.size());
        assertTrue(list.get(0).hasKey("key1"));
        assertTrue(list.get(1).hasKey("key2"));
    }

    @Test
    public void testAsList() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        List<Attribute> list = attributes.asList();

        assertEquals(2, list.size());
        assertTrue(list.get(0).hasKey("key1"));
        assertTrue(list.get(1).hasKey("key2"));
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");
        attributes.put("key2", "value2");

        Map<String, String> dataset = attributes.dataset();

        assertEquals(1, dataset.size());
        assertTrue(dataset.containsKey("data-key1"));
        assertEquals("value1", dataset.get("data-key1"));
    }

    @Test
    public void testHtml() throws IOException {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        StringBuilder expected = new StringBuilder();
        expected.append(" key1=\"value1\" key2=\"value2\"");

        assertEquals(expected.toString(), attributes.html());
    }

    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");

        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");
        attributes2.put("key2", "value2");

        Attributes attributes3 = new Attributes();
        attributes3.put("key1", "value1");
        attributes3.put("key3", "value3");

        Attributes attributes4 = new Attributes();
        attributes4.put("key2", "value2");
        attributes4.put("key3", "value3");

        assertTrue(attributes1.equals(attributes1));
        assertTrue(attributes1.equals(attributes2));

        assertFalse(attributes1.equals(attributes3));
        assertFalse(attributes1.equals(attributes4));

        assertFalse(attributes1.equals(null));
        assertFalse(attributes1.equals(new Object()));
    }

    @Test
    public void testClone() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Attributes clone = attributes.clone();

        assertEquals(attributes.size(), clone.size());
        assertTrue(attributes.equals(clone));
    }
}