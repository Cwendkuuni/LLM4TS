package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AttributesTest {

    @Test
    public void testGetAndPut() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        Assert.assertEquals("value1", attributes.get("key1"));
        Assert.assertEquals(1, attributes.size());

        attributes.put("key2", "value2");
        Assert.assertEquals("value2", attributes.get("key2"));
        Assert.assertEquals(2, attributes.size());

        attributes.put("key1", "value3");
        Assert.assertEquals("value3", attributes.get("key1"));
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key1", "Value1");
        Assert.assertEquals("Value1", attributes.getIgnoreCase("key1"));
        Assert.assertEquals(1, attributes.size());
    }

    @Test(expected = NullPointerException.class)
    public void testNullKey() {
        Attributes attributes = new Attributes();
        attributes.put(null, "value");
    }

    @Test(expected = NullPointerException.class)
    public void testNullValue() {
        Attributes attributes = new Attributes();
        attributes.put("key", null);
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("key", true);
        Assert.assertEquals(1, attributes.size());
        Assert.assertNull(attributes.get("key"));

        attributes.put("key", false);
        Assert.assertEquals(1, attributes.size());
        Assert.assertNull(attributes.get("key"));

        attributes.remove("key");
        Assert.assertEquals(0, attributes.size());
    }

    @Test
    public void testPutIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("Key", "Value");
        Assert.assertEquals("Value", attributes.getIgnoreCase("key"));
        Assert.assertEquals(1, attributes.size());

        attributes.putIgnoreCase("Key2", "Value2");
        Assert.assertEquals("Value2", attributes.getIgnoreCase("key2"));
        Assert.assertEquals(2, attributes.size());
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        Assert.assertEquals(2, attributes.size());

        attributes.remove("key1");
        Assert.assertEquals("value2", attributes.get("key2"));
        Assert.assertEquals(1, attributes.size());

        attributes.remove("key2");
        Assert.assertEquals(0, attributes.size());
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key1", "Value1");
        attributes.put("Key2", "Value2");
        Assert.assertEquals(2, attributes.size());

        attributes.removeIgnoreCase("key1");
        Assert.assertEquals("Value2", attributes.getIgnoreCase("key2"));
        Assert.assertEquals(1, attributes.size());

        attributes.removeIgnoreCase("Key2");
        Assert.assertEquals(0, attributes.size());
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        Assert.assertFalse(attributes.hasKey("key"));

        attributes.put("key", "value");
        Assert.assertTrue(attributes.hasKey("key"));

        attributes.remove("key");
        Assert.assertFalse(attributes.hasKey("key"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        Assert.assertFalse(attributes.hasKeyIgnoreCase("key"));

        attributes.put("Key", "Value");
        Assert.assertTrue(attributes.hasKeyIgnoreCase("key"));

        attributes.remove("Key");
        Assert.assertFalse(attributes.hasKeyIgnoreCase("key"));
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        Assert.assertEquals(0, attributes.size());

        attributes.put("key1", "value1");
        Assert.assertEquals(1, attributes.size());

        attributes.put("key2", "value2");
        Assert.assertEquals(2, attributes.size());

        attributes.remove("key1");
        Assert.assertEquals(1, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");

        Attributes attributes2 = new Attributes();
        attributes2.put("key3", "value3");
        attributes2.put("key4", "value4");

        Attributes attributes3 = new Attributes();
        attributes3.addAll(attributes1);
        attributes3.addAll(attributes2);

        Map<String, String> expected = new HashMap<>();
        expected.put("key1", "value1");
        expected.put("key2", "value2");
        expected.put("key3", "value3");
        expected.put("key4", "value4");

        for (Map.Entry<String, String> entry : expected.entrySet()) {
            Assert.assertEquals(entry.getKey(), attributes3.get(entry.getKey()));
        }
    }

    @Test
    public void testAsList() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        List<Attribute> list = attributes.asList();
        Assert.assertEquals(2, list.size());

        for (Attribute attribute : list) {
            Assert.assertEquals(attribute.getKey(), attributes.get(attribute.getKey()));
            Assert.assertEquals(attribute.getValue(), attributes.get(attribute.getKey()));
        }
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "data-value1");
        attributes.put("data-key2", "data-value2");

        Map<String, String> dataset = attributes.dataset();
        Assert.assertEquals(2, dataset.size());

        for (Map.Entry<String, String> entry : dataset.entrySet()) {
            Assert.assertEquals(entry.getKey(), attributes.getIgnoreCase(entry.getKey()));
            Assert.assertEquals(entry.getValue(), attributes.getIgnoreCase(entry.getKey()));
        }

        Assert.assertEquals("data-value1", attributes.get("data-key1"));
        Assert.assertEquals("data-value2", attributes.get("data-key2"));
    }
}