package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AttributesTest {

    @Test
    public void testBasicUsage() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Assert.assertEquals("value1", attributes.get("key1"));
        Assert.assertEquals("value2", attributes.get("key2"));
        Assert.assertNull(attributes.get("nonExistentKey"));

        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("key1", "value1");
        expectedMap.put("key2", "value2");

        Assert.assertEquals(expectedMap, attributes.asMap());
    }

    @Test
    public void testSizeAndIterator() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Assert.assertEquals(2, attributes.size());

        int i = 0;
        for (Attribute attribute : attributes) {
            Assert.assertEquals(String.valueOf(i), attribute.getKey());
            Assert.assertEquals(String.valueOf(i + 1), attribute.getValue());
            i++;
        }
    }

    @Test
    public void testCaseInsensitivity() {
        Attributes attributes = new Attributes();
        attributes.put("Key1", "value1");
        attributes.put("Key2", "value2");

        Assert.assertEquals("value1", attributes.getIgnoreCase("key1"));
        Assert.assertEquals("value2", attributes.getIgnoreCase("Key2"));
        Assert.assertNull(attributes.getIgnoreCase("nonExistentKey"));
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

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicateKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        attributes.put("key", "otherValue");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyKey() {
        Attributes attributes = new Attributes();
        attributes.put("", "value");
    }

    @Test
    public void testBooleanAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("booleanKey", true);
        Assert.assertTrue(attributes.hasKey("booleanKey"));
        Assert.assertFalse(attributes.get("booleanKey").isEmpty());
        Assert.assertNull(attributes.get("booleanKey").getClass().isInstance(new Boolean("false")));
    }

    @Test
    public void testPutAttribute() {
        Attributes attributes = new Attributes();
        Attribute attribute = new Attribute("key", "value");
        attributes.put(attribute);

        Assert.assertEquals("value", attributes.get("key"));
        Assert.assertNotNull(attribute.parent);
        Assert.assertEquals(attributes, attribute.parent);
    }

    @Test
    public void testPutIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("Key1", "value1");
        Assert.assertEquals("value1", attributes.getIgnoreCase("key1"));
        Assert.assertEquals("value1", attributes.getIgnoreCase("Key1"));
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        attributes.remove("key1");

        Assert.assertEquals("value2", attributes.get("key2"));
        Assert.assertNull(attributes.get("key1"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key1", "value1");
        attributes.put("Key2", "value2");

        attributes.removeIgnoreCase("key1");

        Assert.assertEquals("value2", attributes.get("Key2"));
        Assert.assertNull(attributes.get("Key1"));
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Assert.assertTrue(attributes.hasKey("key1"));
        Assert.assertTrue(attributes.hasKey("key2"));
        Assert.assertFalse(attributes.hasKey("nonExistentKey"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("Key1", "value1");
        attributes.put("Key2", "value2");

        Assert.assertTrue(attributes.hasKeyIgnoreCase("key1"));
        Assert.assertTrue(attributes.hasKeyIgnoreCase("Key1"));
        Assert.assertTrue(attributes.hasKeyIgnoreCase("KEY1"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("nonExistentKey"));
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");

        Map<String, String> dataset = attributes.dataset();

        Assert.assertEquals(2, dataset.size());
        Assert.assertEquals("value1", dataset.get("data-key1"));
        Assert.assertEquals("value2", dataset.get("data-key2"));
    }

    @Test
    public void testHtml() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        StringBuilder expectedHtml = new StringBuilder();
        expectedHtml.append(" key1=\"value1\" key2=\"value2\"");

        Assert.assertEquals(expectedHtml.toString(), attributes.html());
    }
}