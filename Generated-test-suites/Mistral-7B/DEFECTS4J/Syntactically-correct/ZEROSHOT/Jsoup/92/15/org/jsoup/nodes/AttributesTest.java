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
        Assert.assertFalse(attributes.hasKey("key"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("KEY"));
    }

    @Test
    public void testSingleAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        Assert.assertEquals(1, attributes.size());
        Assert.assertTrue(attributes.hasKey("key"));
        Assert.assertTrue(attributes.hasKeyIgnoreCase("key"));
        Assert.assertEquals("value", attributes.get("key"));
        Assert.assertEquals("value", attributes.getIgnoreCase("key"));
        Assert.assertNull(attributes.get("otherKey"));
        Assert.assertNull(attributes.getIgnoreCase("otherKey"));
    }

    @Test
    public void testMultipleAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        Assert.assertEquals(2, attributes.size());
        Assert.assertTrue(attributes.hasKey("key1"));
        Assert.assertTrue(attributes.hasKey("key2"));
        Assert.assertTrue(attributes.hasKeyIgnoreCase("key1"));
        Assert.assertTrue(attributes.hasKeyIgnoreCase("key2"));
        Assert.assertEquals("value1", attributes.get("key1"));
        Assert.assertEquals("value1", attributes.getIgnoreCase("key1"));
        Assert.assertEquals("value2", attributes.get("key2"));
        Assert.assertEquals("value2", attributes.getIgnoreCase("key2"));
        Assert.assertNull(attributes.get("otherKey"));
        Assert.assertNull(attributes.getIgnoreCase("otherKey"));
    }

    @Test
    public void testBooleanAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("key", true);
        Assert.assertEquals(1, attributes.size());
        Assert.assertTrue(attributes.hasKey("key"));
        Assert.assertTrue(attributes.hasKeyIgnoreCase("key"));
        Assert.assertNull(attributes.get("key"));
        Assert.assertEquals(EmptyString, attributes.getIgnoreCase("key"));

        attributes.put("key", false);
        Assert.assertEquals(1, attributes.size());
        Assert.assertFalse(attributes.hasKey("key"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("key"));
        Assert.assertNull(attributes.get("key"));
        Assert.assertEquals(EmptyString, attributes.getIgnoreCase("key"));
    }

    @Test
    public void testRemoveAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        Assert.assertEquals(2, attributes.size());
        attributes.remove("key1");
        Assert.assertEquals(1, attributes.size());
        Assert.assertFalse(attributes.hasKey("key1"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("key1"));
        Assert.assertEquals("value2", attributes.get("key2"));
        Assert.assertEquals("value2", attributes.getIgnoreCase("key2"));
    }

    @Test
    public void testRemoveIgnoreCaseAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("Key2", "value2");
        Assert.assertEquals(2, attributes.size());
        attributes.removeIgnoreCase("key1");
        Assert.assertEquals(1, attributes.size());
        Assert.assertFalse(attributes.hasKey("key1"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("key1"));
        Assert.assertEquals("value2", attributes.get("Key2"));
        Assert.assertEquals("value2", attributes.getIgnoreCase("key2"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullKey() {
        Attributes attributes = new Attributes();
        attributes.put(null, "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullValue() {
        Attributes attributes = new Attributes();
        attributes.put("key", null);
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        Attributes attributes2 = new Attributes();
        attributes2.put("key2", "value2");
        attributes1.addAll(attributes2);
        Assert.assertEquals(2, attributes1.size());
        Assert.assertTrue(attributes1.hasKey("key1"));
        Assert.assertTrue(attributes1.hasKey("key2"));
        Assert.assertEquals("value1", attributes1.get("key1"));
        Assert.assertEquals("value1", attributes1.getIgnoreCase("key1"));
        Assert.assertEquals("value2", attributes1.get("key2"));
        Assert.assertEquals("value2", attributes1.getIgnoreCase("key2"));
    }

    @Test
    public void testPutIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("KEY", "VALUE");
        Assert.assertEquals(1, attributes.size());
        Assert.assertTrue(attributes.hasKey("KEY"));
        Assert.assertTrue(attributes.hasKeyIgnoreCase("KEY"));
        Assert.assertEquals("VALUE", attributes.get("KEY"));
        Assert.assertEquals("VALUE", attributes.getIgnoreCase("KEY"));
    }

    @Test
    public void testGetIgnoreCaseWithEmptyValue() {
        Attributes attributes = new Attributes();
        attributes.put("KEY", "");
        Assert.assertEquals(1, attributes.size());
        Assert.assertTrue(attributes.hasKey("KEY"));
        Assert.assertTrue(attributes.hasKeyIgnoreCase("KEY"));
        Assert.assertEquals(EmptyString, attributes.get("KEY"));
        Assert.assertEquals(EmptyString, attributes.getIgnoreCase("KEY"));
    }

    @Test
    public void testPutAndGetWithDataPrefix() {
        Attributes attributes = new Attributes();
        attributes.put("data-key", "value");
        Assert.assertEquals(1, attributes.size());
        Assert.assertTrue(attributes.hasKey("data-key"));
        Assert.assertTrue(attributes.hasKeyIgnoreCase("data-key"));
        Assert.assertEquals("value", attributes.get("data-key"));
        Assert.assertEquals("value", attributes.getIgnoreCase("data-key"));
        Assert.assertEquals("value", attributes.get("data-Key"));
        Assert.assertEquals("value", attributes.getIgnoreCase("data-Key"));
        Assert.assertEquals("value", attributes.get("data-KEY"));
        Assert.assertEquals("value", attributes.getIgnoreCase("data-KEY"));

        String dataKey = "data-key-test";
        attributes.put(dataKey, "testValue");
        Assert.assertEquals(2, attributes.size());
        Assert.assertTrue(attributes.hasKey(dataKey));
        Assert.assertTrue(attributes.hasKeyIgnoreCase(dataKey));
        Assert.assertEquals("testValue", attributes.get(dataKey));
        Assert.assertEquals("testValue", attributes.getIgnoreCase(dataKey));
        Assert.assertEquals("testValue", attributes.get("Data-key-test"));
        Assert.assertEquals("testValue", attributes.getIgnoreCase("Data-key-test"));
        Assert.assertEquals("testValue", attributes.get("DATA-KEY-TEST"));
        Assert.assertEquals("testValue", attributes.getIgnoreCase("DATA-KEY-TEST"));
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");
        Map<String, String> dataset = attributes.dataset();
        Assert.assertEquals(2, dataset.size());
        Assert.assertTrue(dataset.containsKey("data-key1"));
        Assert.assertTrue(dataset.containsKey("data-key2"));
        Assert.assertEquals("value1", dataset.get("data-key1"));
        Assert.assertEquals("value1", dataset.getOrDefault("Data-key1", EmptyString));
        Assert.assertEquals("value2", dataset.get("data-key2"));
        Assert.assertEquals("value2", dataset.getOrDefault("Data-key2", EmptyString));

        Assert.assertFalse(dataset.containsKey("key1"));
        Assert.assertFalse(dataset.containsKey("key2"));
        Assert.assertNull(dataset.get("key1"));
        Assert.assertNull(dataset.get("key2"));
    }

    @Test
    public void testHtml() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        StringBuilder expected = new StringBuilder();
        expected.append(" key=\"value\"");
        String html = attributes.html();
        Assert.assertEquals(expected.toString(), html);

        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        expected.setLength(0);
        expected.append(" key=\"value\" key1=\"value1\" key2=\"value2\"");
        html = attributes.html();
        Assert.assertEquals(expected.toString(), html);

        attributes.normalize();
        expected.setLength(0);
        expected.append(" key=\"value\" key1=\"value1\" key2=\"value2\"");
        html = attributes.html();
        Assert.assertEquals(expected.toString(), html);

        attributes.normalize();
        attributes.remove("key");
        expected.setLength(0);
        expected.append(" key1=\"value1\" key2=\"value2\"");
        html = attributes.html();
        Assert.assertEquals(expected.toString(), html);
    }
}