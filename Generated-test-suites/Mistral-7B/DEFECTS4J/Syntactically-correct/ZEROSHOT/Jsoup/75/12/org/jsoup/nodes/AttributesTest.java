package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Map;

import static org.jsoup.nodes.Attributes.dataPrefix;
import static org.jsoup.nodes.Attributes.InitialCapacity;
import static org.jsoup.nodes.Attributes.GrowthFactor;

public class AttributesTest {

    @Test
    public void testInitialization() {
        Attributes attributes = new Attributes();
        Assert.assertEquals(InitialCapacity, attributes.keys.length);
        Assert.assertEquals(InitialCapacity, attributes.vals.length);
        Assert.assertEquals(0, attributes.size);
    }

    @Test
    public void testAddAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        Assert.assertEquals(1, attributes.size());
        Assert.assertEquals("value1", attributes.get("key1"));
        Assert.assertEquals("value1", attributes.getIgnoreCase("key1"));
    }

    @Test
    public void testAddMultipleAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        Assert.assertEquals(2, attributes.size());
        Assert.assertEquals("value1", attributes.get("key1"));
        Assert.assertEquals("value2", attributes.get("key2"));
        Assert.assertEquals("value1", attributes.getIgnoreCase("key1"));
        Assert.assertEquals("value2", attributes.getIgnoreCase("key2"));
    }

    @Test
    public void testAddDuplicateAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key1", "value2");
        Assert.assertEquals(1, attributes.size());
        Assert.assertEquals("value2", attributes.get("key1"));
        Assert.assertEquals("value2", attributes.getIgnoreCase("key1"));
    }

    @Test
    public void testReplaceAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key1", "value2");
        Assert.assertEquals("value2", attributes.get("key1"));
        Assert.assertEquals("value2", attributes.getIgnoreCase("key1"));
    }

    @Test
    public void testRemoveAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.remove("key1");
        Assert.assertEquals(1, attributes.size());
        Assert.assertNull(attributes.get("key1"));
        Assert.assertNull(attributes.getIgnoreCase("key1"));
        Assert.assertEquals("value2", attributes.get("key2"));
        Assert.assertEquals("value2", attributes.getIgnoreCase("key2"));
    }

    @Test
    public void testRemoveMultipleAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.remove("key1");
        attributes.remove("key2");
        Assert.assertEquals(0, attributes.size());
        Assert.assertNull(attributes.get("key1"));
        Assert.assertNull(attributes.getIgnoreCase("key1"));
        Assert.assertNull(attributes.get("key2"));
        Assert.assertNull(attributes.getIgnoreCase("key2"));
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        Assert.assertFalse(attributes.hasKey("key1"));
        attributes.put("key1", "value1");
        Assert.assertTrue(attributes.hasKey("key1"));
        Assert.assertTrue(attributes.hasKeyIgnoreCase("key1"));
    }

    @Test
    public void testPutBooleanAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("key1", true);
        Assert.assertEquals(1, attributes.size());
        Assert.assertNull(attributes.get("key1"));
        Assert.assertFalse(attributes.hasKey("key1"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("key1"));
        Assert.assertTrue(attributes.hasKey("key1") && attributes.get("key1") == null);
        Assert.assertTrue(attributes.hasKeyIgnoreCase("key1") && attributes.get("key1") == null);
        Assert.assertFalse(attributes.hasKey("key2"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("key2"));
    }

    @Test
    public void testPutAndGetDataAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");
        Assert.assertEquals("value1", attributes.get("data-key1"));
        Assert.assertEquals("value1", attributes.getIgnoreCase("data-key1"));
        Assert.assertEquals("value1", attributes.get("data-key1"));
        Assert.assertEquals("value1", attributes.getIgnoreCase("Data-key1"));
        Assert.assertEquals("value1", attributes.get("Data-key1"));
        Map<String, String> dataset = attributes.dataset();
        Assert.assertEquals("value1", dataset.get("key1"));
        Assert.assertEquals("value1", dataset.getOrDefault("data-key1", ""));
        Assert.assertEquals("value1", dataset.getOrDefault("Data-key1", ""));
    }

    @Test
    public void testCloneAttributes() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");
        Attributes attributes2 = attributes1.clone();
        Assert.assertEquals(attributes1.size(), attributes2.size());
        Assert.assertEquals("value1", attributes1.get("key1"));
        Assert.assertEquals("value1", attributes1.getIgnoreCase("key1"));
        Assert.assertEquals("value1", attributes2.get("key1"));
        Assert.assertEquals("value1", attributes2.getIgnoreCase("key1"));
        Assert.assertEquals("value2", attributes1.get("key2"));
        Assert.assertEquals("value2", attributes1.getIgnoreCase("key2"));
        Assert.assertEquals("value2", attributes2.get("key2"));
        Assert.assertEquals("value2", attributes2.getIgnoreCase("key2"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullKey() {
        Attributes attributes = new Attributes();
        attributes.put(null, "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullValue() {
        Attributes attributes = new Attributes();
        attributes.put("key", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddEmptyStringKey() {
        Attributes attributes = new Attributes();
        attributes.put("", "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddEmptyStringValue() {
        Attributes attributes = new Attributes();
        attributes.put("key", "");
    }
}