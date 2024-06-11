package org.jsoup.nodes;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.helper.Validate;
import java.util.LinkedHashMap;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Collections;

public class AttributesTest {

    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        String result = attributes.get("key1");
        assertEquals("value1", result);
        result = attributes.get("key2");
        assertEquals("", result);
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
       attributes.put("key1", "value1");
        String result = attributes.getIgnoreCase("key1");
        assertEquals("value1", result);
        result = attributes.getIgnoreCase("Key1");
        assertEquals("value1", result);
        result = attributes.getIgnoreCase("KEY1");
        assertEquals("value1", result);
        result = attributes.getIgnoreCase("key2");
        assertEquals("", result);
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        String result = attributes.get("key1");
        assertEquals("value1", result);
        attributes.put("key1", "value2");
        result = attributes.get("key1");
        assertEquals("value2", result);
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("key1", true);
        assertTrue(attributes.hasKey("key1"));
        assertFalse(attributes.hasKey("key2"));
        attributes.put("key1", false);
        assertFalse(attributes.hasKey("key1"));
    }

    @Test
    public void testPutAttribute() {
        Attributes attributes = new Attributes();
        attributes.put(new Attribute("key1", "value1"));
        String result = attributes.get("key1");
        assertEquals("value1", result);
        attributes.put(new Attribute("key1", "value2"));
        result = attributes.get("key1");
        assertEquals("value2", result);
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.remove("key1");
        String result = attributes.get("key1");
        assertEquals("", result);
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.removeIgnoreCase("key1");
        String result = attributes.get("key1");
        assertEquals("", result);
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        assertTrue(attributes.hasKey("key1"));
        assertTrue(attributes.hasKeyIgnoreCase("key1"));
        assertFalse(attributes.hasKey("key2"));
        assertFalse(attributes.hasKeyIgnoreCase("key2"));
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        assertEquals(1, attributes.size());
        attributes.put("key2", "value2");
        assertEquals(2, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        attributes1.put("key1", "value1");
        attributes2.put("key2", "value2");
        attributes1.addAll(attributes2);
        assertEquals(2, attributes1.size());
        String result1 = attributes1.get("key1");
        assertEquals("value1", result1);
        String result2 = attributes1.get("key2");
        assertEquals("value2", result2);
    }

    @Test
    public void testIterator() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        List<Attribute> attributesList = attributes.asList();
        Iterator<Attribute> iterator = attributes.iterator();
        for (Attribute attribute : attributesList) {
            Attribute next = iterator.next();
            assertEquals(attribute.getKey(), next.getKey());
            assertEquals(attribute.getValue(), next.getValue());
        }
    }

    @Test
    public void testAsList() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        List<Attribute> attributesList = attributes.asList();
        assertEquals(2, attributesList.size());
        Attribute firstAttribute = attributesList.get(0);
        Attribute secondAttribute = attributesList.get(1);
        assertEquals(firstAttribute.getKey(), secondAttribute.getKey());
        assertEquals(firstAttribute.getValue(), secondAttribute.getValue());
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put(new Attribute("data-key1", "data-value1"));
        Map<String, String> result = attributes.dataset();
        assertEquals("data-value1", result.get("key1"));
        assertTrue(result.containsKey("key1"));
        assertTrue(result.isEmpty());
    }

    @Test
    public void testHtml() throws IOException {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        StringBuilder expected = new StringBuilder();
        expected.append(" key1=\"value1\"");
        StringBuilder result = new StringBuilder();
        attributes.html(result, null);
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        Attributes attributes3 = new Attributes();
        attributes1.put("key1", "value1");
        attributes2.put("key1", "value1");
        attributes3.put("key2", "value2");
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
        assertEquals(attributes1.hashCode(), attributes2.hashCode());
        assertNotEquals(attributes1.hashCode(), attributes3.hashCode());
    }

    @Test
    public void testClone() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        Attributes clonedAttributes = attributes.clone();
        assertEquals(attributes.size(), clonedAttributes.size());
        assertTrue(attributes.equals(clonedAttributes));
    }
}