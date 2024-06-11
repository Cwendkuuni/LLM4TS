package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
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
        attributes.put("key", "value");
        String result = attributes.getIgnoreCase("Key");
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
        result = attributes.getIgnoreCase("key").equals("true");
        Assert.assertTrue(result);
        result = attributes.get("key").equals("");
        Assert.assertFalse(result);
    }

    @Test
    public void testPutWithExistingKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value1");
        attributes.put("key", "value2");
        String result = attributes.get("key");
        Assert.assertEquals("value2", result);
    }

    @Test
    public void testPutWithMultipleValues() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value1");
        attributes.put("key", "value2");
        String[] results = attributes.getIgnoreCase("key").split("\\s+");
        Assert.assertArrayEquals(new String[]{"value1", "value2"}, results);
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        Assert.assertFalse(attributes.hasKey("key"));
        attributes.put("key", "value");
        Assert.assertTrue(attributes.hasKey("key"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("KEY"));
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        Assert.assertEquals(0, attributes.size());
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
        attributes1.addAll(attributes2);
        String[] results = attributes1.getIgnoreCase("key1", "key2").split("\\s+");
        Assert.assertArrayEquals(new String[]{"value1", "value2"}, results);
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.remove("key1");
        Assert.assertFalse(attributes.hasKey("key1"));
        Assert.assertEquals(1, attributes.size());
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.removeIgnoreCase("key1");
        Assert.assertFalse(attributes.hasKey("key1"));
        Assert.assertEquals(1, attributes.size());
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        Map<String, String> result = attributes.dataset();
        Assert.assertEquals("value", result.get("key"));
    }

    @Test
    public void testHtml() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        String result = attributes.html();
        String expected = " key=\"value\"";
        Assert.assertEquals(expected, result);
    }
}