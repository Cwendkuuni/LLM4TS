package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class AttributesTest {
    private Attributes attributes;

    @Before
    public void setUp() {
        attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.put("data-customKey1", "customValue1");
    }

    @Test
    public void testGet() {
        assertEquals("value1", attributes.get("key1"));
        assertEquals("", attributes.get("nonExistentKey"));
        assertEquals("value2", attributes.get("key2"));
    }

    @Test
    public void testGetIgnoreCase() {
        assertEquals("value1", attributes.getIgnoreCase("key1"));
        assertEquals("value2", attributes.getIgnoreCase("key2"));
        assertEquals("", attributes.getIgnoreCase("nonExistentKey"));
    }

    @Test
    public void testPut() {
        attributes.put("key3", "value3");
        assertEquals("value3", attributes.get("key3"));
    }

    @Test
    public void testPutBoolean() {
        attributes.put("key4", false);
        assertTrue(attributes.hasKey("key4"));
        assertEquals("", attributes.get("key4"));
        attributes.put("key4", true);
        assertFalse(attributes.hasKey("key4"));
        assertTrue(attributes.hasKeyIgnoreCase("key4"));
    }

    @Test
    public void testRemove() {
        attributes.remove("key1");
        assertEquals("", attributes.get("key1"));
        assertFalse(attributes.hasKey("key1"));
        assertFalse(attributes.hasKeyIgnoreCase("key1"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        attributes.removeIgnoreCase("key1");
        assertEquals("", attributes.get("key1"));
        assertFalse(attributes.hasKey("key1"));
        assertFalse(attributes.hasKeyIgnoreCase("key1"));
        assertTrue(attributes.hasKeyIgnoreCase("KEY1"));
    }

    @Test
    public void testHasKey() {
        assertTrue(attributes.hasKey("key1"));
        assertFalse(attributes.hasKey("key3"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        assertTrue(attributes.hasKeyIgnoreCase("key1"));
        assertTrue(attributes.hasKeyIgnoreCase("Key1"));
        assertTrue(attributes.hasKeyIgnoreCase("key2"));
        assertFalse(attributes.hasKeyIgnoreCase("key3"));
    }

    @Test
    public void testSize() {
        assertEquals(3, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes attributes2 = new Attributes();
        attributes2.put("key5", "value5");
        attributes2.put("key6", "value6");
        attributes.addAll(attributes2);
        assertEquals("value5", attributes.get("key5"));
        assertEquals("value6", attributes.get("key6"));
        assertEquals(5, attributes.size());
    }

    @Test
    public void testIterator() {
        List<Attribute> attributesList = attributes.asList();
        Iterator<Attribute> iterator = attributesList.iterator();
        List<String> keys = new LinkedList<>();
        while (iterator.hasNext()) {
            Attribute attribute = iterator.next();
            keys.add(attribute.getKey());
        }
        List<String> expectedKeys = new LinkedList<>(List.of("key1", "key2", "key4", "data-customKey1"));
        assertEquals(expectedKeys, keys);
    }

    @Test
    public void testDataset() {
        Map<String, String> dataset = attributes.dataset();
        assertEquals(1, dataset.size());
        assertEquals("customValue1", dataset.get("data-customKey1"));
    }

    @Test
    public void testHtml() {
        StringBuilder expectedHtml = new StringBuilder();
        expectedHtml.append(" key1=\"value1\" key2=\"value2\" data-customKey1=\"customValue1\"");
        String html = attributes.html();
        assertEquals(expectedHtml.toString(), html);
    }

    @Test
    public void testEquals() {
        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");
        attributes2.put("key2", "value2");
        assertTrue(attributes.equals(attributes));
        assertTrue(attributes.equals(attributes2));
        Attributes attributes3 = new Attributes();
        attributes3.put("key1", "value2");
        assertFalse(attributes.equals(attributes3));
    }

    @Test
    public void testHashCode() {
        int attributesHashCode = attributes.hashCode();
        Attributes attributesCopy = (Attributes) attributes.clone();
        assertEquals(attributesHashCode, attributesCopy.hashCode());
    }
}