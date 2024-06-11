package org.jsoup.nodes.test;

import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.BooleanAttribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.XML;
import org.jsoup.parser.Parser;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class AttributesTest {

    @Test
    public void testGet() {
        Element element = new Element("div");
        element.put("class", "test");
        Attributes attributes = element.attributes();
        assertEquals("test", attributes.get("class"));
        assertEquals("", attributes.get("nonexistent"));
    }

    @Test
    public void testGetIgnoreCase() {
        Element element = new Element("div");
        element.put("class", "Test");
        Attributes attributes = element.attributes();
        assertEquals("Test", attributes.getIgnoreCase("class"));
        assertEquals("", attributes.getIgnoreCase("nonexistent"));
    }

    @Test
    public void testPut() {
        Element element = new Element("div");
        Attributes attributes = element.attributes();
        attributes.put("class", "test");
        assertEquals("test", attributes.get("class"));
        assertFalse(attributes.hasKey("nonexistent"));
        attributes.put("nonexistent", "");
        assertTrue(attributes.hasKey("nonexistent"));
        assertEquals("", attributes.get("nonexistent"));
    }

    @Test
    public void testPutBoolean() {
        Element element = new Element("div");
        Attributes attributes = element.attributes();
        attributes.put("class", "test");
        attributes.put("data-test", true);
        assertEquals("test", attributes.get("class"));
        assertTrue(attributes.hasKey("data-test"));
        assertNull(attributes.get("data-test").getValue());
        assertSame(BooleanAttribute.class, attributes.get("data-test").getClass());
    }

    @Test
    public void testPutReplace() {
        Element element = new Element("div");
        element.put("class", "test");
        Attributes attributes = element.attributes();
        attributes.put("class", "newtest");
        assertEquals("newtest", attributes.get("class"));
        assertFalse(attributes.hasKey("test"));
    }

    @Test
    public void testPutReplaceIgnoreCase() {
        Element element = new Element("div");
        element.put("class", "test");
        Attributes attributes = element.attributes();
        attributes.put("Class", "newtest");
        assertEquals("newtest", attributes.get("class"));
        assertFalse(attributes.hasKey("test"));
    }

    @Test
    public void testRemove() {
        Element element = new Element("div");
        element.put("class", "test");
        Attributes attributes = element.attributes();
        attributes.remove("class");
        assertFalse(attributes.hasKey("class"));
        assertEquals("", attributes.get("class"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        Element element = new Element("div");
        element.put("class", "test");
        Attributes attributes = element.attributes();
        attributes.removeIgnoreCase("class");
        assertFalse(attributes.hasKey("class"));
        assertEquals("", attributes.get("class"));
    }

    @Test
    public void testHasKey() {
        Element element = new Element("div");
        Attributes attributes = element.attributes();
        assertFalse(attributes.hasKey("nonexistent"));
        element.put("class", "test");
        assertTrue(attributes.hasKey("class"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Element element = new Element("div");
        Attributes attributes = element.attributes();
        assertFalse(attributes.hasKeyIgnoreCase("CLASS"));
        element.put("class", "test");
        assertTrue(attributes.hasKeyIgnoreCase("class"));
    }

    @Test
    public void testSize() {
        Element element = new Element("div");
        Attributes attributes = element.attributes();
        assertEquals(0, attributes.size());
        element.put("class", "test");
        assertEquals(1, attributes.size());
        element.put("data-test", true);
        assertEquals(2, attributes.size());
    }

    @Test
    public void testAddAll() {
        Element element1 = new Element("div");
        Attributes attributes1 = element1.attributes();
        element1.put("class", "test1");
        Element element2 = new Element("div");
        Attributes attributes2 = element2.attributes();
        element2.put("class", "test2");
        attributes1.addAll(attributes2);
        assertEquals(2, attributes1.size());
        assertEquals("test1 test2", attributes1.toString());
    }

    @Test
    public void testIterator() {
        Element element = new Element("div");
        element.put("class1", "test1");
        element.put("class2", "test2");
        Attributes attributes = element.attributes();
        List<Attribute> list = attributes.asList();
        assertEquals(2, list.size());
        assertEquals("test1", list.get(0).getValue());
        assertEquals("test2", list.get(1).getValue());
    }

    @Test
    public void testAsList() {
        Element element = new Element("div");
        element.put("class1", "test1");
        element.put("class2", "test2");
        Attributes attributes = element.attributes();
        List<Attribute> list = attributes.asList();
        assertEquals(2, list.size());
        assertEquals("test1", list.get(0).getValue());
        assertEquals("test2", list.get(1).getValue());
        // Test unmodifiable list
        try {
            list.set(0, new Attribute("newkey", "newvalue"));
            fail("List is unmodifiable.");
        } catch (UnsupportedOperationException e) {
        }
    }

    @Test
    public void testDataset() {
        Element element = new Element("div");
        element.put("data-test1", "value1");
        element.put("data-test2", "value2");
        Attributes attributes = element.attributes();
        Map<String, String> dataset = attributes.dataset();
        assertEquals(2, dataset.size());
        assertEquals("value1", dataset.get("test1"));
        assertEquals("value2", dataset.get("test2"));
    }

    @Test
    public void testHtml() throws IOException {
        Element element = new Element("div");
        element.put("id", "test");
        element.put("class", "test");
        Attributes attributes = element.attributes();
        StringBuilder accum = new StringBuilder();
        Document document = new Document();
        attributes.html(accum, document.outputSettings());
        assertEquals("<div id=\"test\" class=\"test\"></div>", accum.toString());
    }

    @Test
    public void testEquals() {
        Element element1 = new Element("div");
        element1.put("class", "test1");
        Element element2 = new Element("div");
        element2.put("class", "test1");
        Attributes attributes1 = element1.attributes();
        Attributes attributes2 = element2.attributes();
        assertTrue(attributes1.equals(attributes2));
        assertFalse(attributes1.equals(null));
        assertFalse(attributes1.equals(new Object()));
        Attributes attributes3 = new Attributes();
        assertFalse(attributes1.equals(attributes3));
    }

    @Test
    public void testHashCode() {
        Element element = new Element("div");
        Attributes attributes1 = element.attributes();
        attributes1.put("class", "test1");
        Attributes attributes2 = element.attributes();
        attributes2.put("class", "test2");
        assertEquals(attributes1.hashCode(), attributes1.hashCode());
        assertNotEquals(attributes1.hashCode(), attributes2.hashCode());
    }

    @Test
    public void testClone() {
        Element element = new Element("div");
        Attributes attributes = element.attributes();
        Attributes clonedAttributes = attributes.clone();
        assertNotSame(attributes, clonedAttributes);
        assertEquals(attributes.size(), clonedAttributes.size());
        for (Map.Entry<String, Attribute> entry : attributes.entrySet()) {
            Attribute attribute = entry.getValue();
            Attribute clonedAttribute = clonedAttributes.get(entry.getKey());
            assertEquals(attribute.getKey(), clonedAttribute.getKey());
            assertEquals(attribute.getValue(), clonedAttribute.getValue());
            assertEquals(attribute.getClass(), clonedAttribute.getClass());
        }
    }

    static class InvalidKeyException extends Exception {}

    @Test(expected = InvalidKeyException.class)
    public void testInvalidPut() {
        Element element = new Element("div");
        Attributes attributes = element.attributes();
        attributes.put(null, "");
        fail("put(null, null) should not be allowed.");
    }

    @Test(expected = InvalidKeyException.class)
    public void testInvalidPutWithValue() {
        Element element = new Element("div");
        Attributes attributes = element.attributes();
        attributes.put("", "");
        fail("put(\"\", \") should not be allowed.");
    }

    @Test(expected = InvalidKeyException.class)
    public void testInvalidRemove() {
        Element element = new Element("div");
        Attributes attributes = element.attributes();
        attributes.remove(null);
        fail("remove(null) should not be allowed.");
    }

    @Test(expected = InvalidKeyException.class)
    public void testInvalidRemoveIgnoreCase() {
        Element element = new Element("div");
        Attributes attributes = element.attributes();
        attributes.removeIgnoreCase(null);
        fail("removeIgnoreCase(null) should not be allowed.");
    }

    @Test(expected = InvalidKeyException.class)
    public void testInvalidHasKey() {
        Element element = new Element("div");
        Attributes attributes = element.attributes();
        assertFalse(attributes.hasKey(null));
    }

    @Test(expected = InvalidKeyException.class)
    public void testInvalidHasKeyIgnoreCase() {
        Element element = new Element("div");
        Attributes attributes = element.attributes();
        assertFalse(attributes.hasKeyIgnoreCase(null));
    }

    @Test
    public void testDataKey() {
        String key = "test";
        String dataKey = Attributes.dataKey(key);
        assertEquals("data-test", dataKey);
    }
}