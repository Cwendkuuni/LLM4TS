package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.helper.Validate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AttributesTest {

    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String result = attributes.get("key");
        Assert.assertEquals("value", result);
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "value");
        String result = attributes.getIgnoreCase("key");
        Assert.assertEquals("value", result);
    }

    @Test
    public void testGetWhenNull() {
        Attributes attributes = new Attributes();
        String result = attributes.get("key");
        Assert.assertEquals("", result);
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String result = attributes.get("key");
        Assert.assertEquals("value", result);
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("key", true);
        boolean result = attributes.hasKey("key");
        Assert.assertTrue(result);
    }

    @Test
    public void testPutWhenExists() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value1");
        attributes.put("key", "value2");
        String result = attributes.get("key");
        Assert.assertEquals("value2", result);
    }

    @Test
    public void testPutWhenNullAttributes() {
        Attributes attributes = new Attributes();
        attributes.put(null, "value");
        Assert.fail("Should not allow null keys.");
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        attributes.remove("key");
        String result = attributes.get("key");
        Assert.assertEquals("", result);
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "value");
        attributes.removeIgnoreCase("key");
        String result = attributes.get("Key");
        Assert.assertEquals("value", result);
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        boolean result = attributes.hasKey("key");
        Assert.assertTrue(result);
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key", "value");
        boolean result = attributes.hasKeyIgnoreCase("key");
        Assert.assertTrue(result);
    }

    @Test
    public void testHasKeyWhenNull() {
        Attributes attributes = new Attributes();
        boolean result = attributes.hasKey("key");
        Assert.assertFalse(result);
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        int result = attributes.size();
        Assert.assertEquals(2, result);
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        Attributes attributes2 = new Attributes();
        attributes2.put("key2", "value2");
        attributes1.addAll(attributes2);
        int size1 = attributes1.size();
        int size2 = attributes2.size();
        Assert.assertEquals(size1 + size2, attributes1.size());
        String result1 = attributes1.get("key1");
        String result2 = attributes1.get("key2");
        Assert.assertEquals("value1", result1);
        Assert.assertEquals("value2", result2);
    }

    @Test
    public void testIterator() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        List<String> keys = new ArrayList<>();
        for (String key : attributes) {
            keys.add(key);
        }
        List<String> expected = new ArrayList<>();
        expected.add("key1");
        expected.add("key2");
        Assert.assertEquals(expected, keys);
    }

    @Test
    public void testAsList() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        List<Attribute> attributesList = attributes.asList();
        List<String> keys = new ArrayList<>();
        for (Attribute attribute : attributesList) {
            keys.add(attribute.getKey());
        }
        List<String> expected = new ArrayList<>();
        expected.add("key1");
        expected.add("key2");
        Assert.assertEquals(expected, keys);
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-key", "value");
        Map<String, String> result = attributes.dataset();
        Assert.assertEquals("value", result.get("data-key"));
    }

    @Test
    public void testHtml() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String result = attributes.html();
        Assert.assertEquals(" key=\"value\"", result);
    }

    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        attributes1.put("key1", "value1");
        attributes2.put("key1", "value1");
        Assert.assertTrue(attributes1.equals(attributes2));
    }

    @Test
    public void testHashCode() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        attributes1.put("key", "value");
        attributes2.put("key", "value");
        Assert.assertEquals(attributes1.hashCode(), attributes2.hashCode());
    }

    @Test
    public void testClone() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        Attributes clonedAttributes = attributes.clone();
        String result = clonedAttributes.get("key");
        Assert.assertEquals("value", result);
    }
}