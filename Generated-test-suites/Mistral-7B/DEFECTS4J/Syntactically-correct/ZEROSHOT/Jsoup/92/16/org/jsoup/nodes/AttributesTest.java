package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AttributesTest {

    @Test
    public void testEmptyAttributes() {
        Attributes attributes = new Attributes();
        Assert.assertEquals(0, attributes.size());
    }

    @Test
    public void testSetGetAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        Assert.assertEquals("value", attributes.get("key"));
        Assert.assertEquals(1, attributes.size());
    }

    @Test(expected = NullPointerException.class)
    public void testSetNullKey() {
        Attributes attributes = new Attributes();
        attributes.put(null, "value");
    }

    @Test(expected = NullPointerException.class)
    public void testSetNullValue() {
        Attributes attributes = new Attributes();
        attributes.put("key", null);
    }

    @Test
    public void testSetMultipleAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        Assert.assertEquals("value1", attributes.get("key1"));
        Assert.assertEquals("value2", attributes.get("key2"));
        Assert.assertEquals(2, attributes.size());
    }

    @Test
    public void testSetSameKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value1");
        attributes.put("key", "value2");
        Assert.assertEquals("value2", attributes.get("key"));
        Assert.assertEquals(1, attributes.size());
    }

    @Test
    public void testSetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("key", "value");
        Assert.assertEquals("value", attributes.get("key"));
        Assert.assertEquals(1, attributes.size());
    }

    @Test
    public void testSetBooleanAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("key", true);
        Assert.assertEquals("", attributes.get("key"));
        Assert.assertEquals(1, attributes.size());
    }

    @Test
    public void testRemoveAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.remove("key1");
        Assert.assertEquals("value2", attributes.get("key2"));
        Assert.assertEquals(1, attributes.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveNonExistentKey() {
        Attributes attributes = new Attributes();
        attributes.remove("nonExistentKey");
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("Key2", "value2");
        attributes.removeIgnoreCase("key1");
        Assert.assertEquals("value2", attributes.get("Key2"));
        Assert.assertEquals(1, attributes.size());
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        Assert.assertTrue(attributes.hasKey("key"));
        Assert.assertFalse(attributes.hasKey("nonExistentKey"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        Assert.assertTrue(attributes.hasKeyIgnoreCase("key"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("NonExistentKey"));
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        Assert.assertEquals(2, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        Attributes attributes2 = new Attributes();
        attributes2.put("key2", "value2");

        Attributes result = new Attributes();
        result.addAll(attributes1);
        result.addAll(attributes2);

        Assert.assertEquals("value1", result.get("key1"));
        Assert.assertEquals("value2", result.get("key2"));
        Assert.assertEquals(2, result.size());
    }

    @Test
    public void testIterator() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        List<Map.Entry<String, String>> entries = new ArrayList<>();
        for (Map.Entry<String, String> entry : attributes) {
            entries.add(entry);
        }

        Assert.assertEquals(2, entries.size());
        Assert.assertEquals("key1", entries.get(0).getKey());
        Assert.assertEquals("value1", entries.get(0).getValue());
        Assert.assertEquals("key2", entries.get(1).getKey());
        Assert.assertEquals("value2", entries.get(1).getValue());
    }

    @Test
    public void testAsList() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        List<Attribute> list = attributes.asList();
        Assert.assertEquals(2, list.size());
        Assert.assertEquals("key1", list.get(0).getKey());
        Assert.assertEquals("value1", list.get(0).getValue());
        Assert.assertEquals("key2", list.get(1).getKey());
        Assert.assertEquals("value2", list.get(1).getValue());
    }
}