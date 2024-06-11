package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

import static org.jsoup.nodes.Attributes.dataPrefix;

public class AttributesTest {

    @Test
    public void testEmptyAttributes() {
        Attributes attributes = new Attributes();
        Assert.assertEquals(0, attributes.size());
        Assert.assertFalse(attributes.hasKey("key"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("key"));
        Assert.assertNull(attributes.get("key"));
        Assert.assertNull(attributes.getIgnoreCase("key"));
    }

    @Test
    public void testBasicAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        Assert.assertEquals(1, attributes.size());
        Assert.assertTrue(attributes.hasKey("key"));
        Assert.assertTrue(attributes.hasKeyIgnoreCase("key"));
        Assert.assertEquals("value", attributes.get("key"));
        Assert.assertEquals("value", attributes.getIgnoreCase("key"));
    }

    @Test
    public void testCaseInsensitiveAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("KEY", "value");
        Assert.assertEquals(1, attributes.size());
        Assert.assertTrue(attributes.hasKey("KEY"));
        Assert.assertTrue(attributes.hasKeyIgnoreCase("key"));
        Assert.assertEquals("value", attributes.get("KEY"));
        Assert.assertEquals("value", attributes.getIgnoreCase("key"));
    }

    @Test
    public void testPutSameKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value1");
        attributes.put("key", "value2");
        Assert.assertEquals(1, attributes.size());
        Assert.assertEquals("value2", attributes.get("key"));
        Assert.assertEquals("value2", attributes.getIgnoreCase("key"));
    }

    @Test
    public void testPutSameKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value1");
        attributes.put("KEY", "value2");
        Assert.assertEquals(1, attributes.size());
        Assert.assertEquals("value2", attributes.get("key"));
        Assert.assertEquals("value2", attributes.getIgnoreCase("key"));
    }

    @Test
    public void testBooleanAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("key", true);
        Assert.assertEquals(1, attributes.size());
        Assert.assertTrue(attributes.hasKey("key"));
        Assert.assertFalse(attributes.get("key").isEmpty());
        Assert.assertNull(attributes.getIgnoreCase("key"));
    }

    @Test
    public void testBooleanAttributeIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("KEY", true);
        Assert.assertEquals(1, attributes.size());
        Assert.assertTrue(attributes.hasKey("KEY"));
        Assert.assertFalse(attributes.get("KEY").isEmpty());
        Assert.assertNull(attributes.getIgnoreCase("key"));
    }

    @Test
    public void testRemoveKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        attributes.remove("key");
        Assert.assertEquals(0, attributes.size());
        Assert.assertFalse(attributes.hasKey("key"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("key"));
        Assert.assertNull(attributes.get("key"));
        Assert.assertNull(attributes.getIgnoreCase("key"));
    }

    @Test
    public void testRemoveKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("KEY", "value");
        attributes.removeIgnoreCase("key");
        Assert.assertEquals(0, attributes.size());
        Assert.assertFalse(attributes.hasKey("KEY"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("key"));
        Assert.assertNull(attributes.get("KEY"));
        Assert.assertNull(attributes.getIgnoreCase("key"));
    }

    @Test
    public void testRemoveNonExistentKey() {
        Attributes attributes = new Attributes();
        attributes.remove("key");
        Assert.assertEquals(0, attributes.size());
        Assert.assertFalse(attributes.hasKey("key"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("key"));
        Assert.assertNull(attributes.get("key"));
        Assert.assertNull(attributes.getIgnoreCase("key"));
    }

    @Test
    public void testRemoveNonExistentKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.removeIgnoreCase("key");
        Assert.assertEquals(0, attributes.size());
        Assert.assertFalse(attributes.hasKey("key"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("key"));
        Assert.assertNull(attributes.get("key"));
        Assert.assertNull(attributes.getIgnoreCase("key"));
    }

    @Test
    public void testPutAndGetDataAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("data-key", "data-value");
        Assert.assertEquals(1, attributes.size());
        Assert.assertTrue(attributes.hasKey("data-key"));
        Assert.assertTrue(attributes.hasKeyIgnoreCase("data-key"));
        Assert.assertEquals("data-value", attributes.get("data-key"));
        Assert.assertEquals("data-value", attributes.getIgnoreCase("data-key"));
        Assert.assertEquals("data-key", attributes.dataset().get("key"));
        Assert.assertEquals("data-value", attributes.dataset().get("key"));
    }

    @Test
    public void testPutAndGetMultipleDataAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "data-value1");
        attributes.put("data-key2", "data-value2");
        Assert.assertEquals(2, attributes.size());
        Assert.assertTrue(attributes.hasKey("data-key1"));
        Assert.assertTrue(attributes.hasKeyIgnoreCase("data-key1"));
        Assert.assertEquals("data-value1", attributes.get("data-key1"));
        Assert.assertEquals("data-value1", attributes.getIgnoreCase("data-key1"));
        Assert.assertTrue(attributes.hasKey("data-key2"));
        Assert.assertTrue(attributes.hasKeyIgnoreCase("data-key2"));
        Assert.assertEquals("data-value2", attributes.get("data-key2"));
        Assert.assertEquals("data-value2", attributes.getIgnoreCase("data-key2"));
        Assert.assertEquals(2, attributes.dataset().size());
        Assert.assertEquals("data-value1", attributes.dataset().get("data-key1"));
        Assert.assertEquals("data-value2", attributes.dataset().get("data-key2"));
    }

    @Test
    public void testPutAndGetDataAttributesWithSameKey() {
        Attributes attributes = new Attributes();
        attributes.put("data-key", "data-value1");
        attributes.put("data-key", "data-value2");
        Assert.assertEquals(1, attributes.size());
        Assert.assertEquals("data-value2", attributes.get("data-key"));
        Assert.assertEquals("data-value2", attributes.getIgnoreCase("data-key"));
        Assert.assertEquals(1, attributes.dataset().size());
        Assert.assertEquals("data-value2", attributes.dataset().get("data-key"));
    }

    @Test
    public void testPutAndGetDataAttributesWithSameKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("data-KEY", "data-value1");
        attributes.put("data-key", "data-value2");
        Assert.assertEquals(1, attributes.size());
        Assert.assertEquals("data-value2", attributes.get("data-key"));
        Assert.assertEquals("data-value2", attributes.getIgnoreCase("data-key"));
        Assert.assertEquals(1, attributes.dataset().size());
        Assert.assertEquals("data-value2", attributes.dataset().get("data-key"));
    }

    @Test
    public void testRemoveDataAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("data-key", "data-value");
        attributes.remove("data-key");
        Assert.assertEquals(0, attributes.size());
        Assert.assertFalse(attributes.hasKey("data-key"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("data-key"));
        Assert.assertNull(attributes.get("data-key"));
        Assert.assertNull(attributes.getIgnoreCase("data-key"));
        Assert.assertEquals(0, attributes.dataset().size());
        Assert.assertFalse(attributes.dataset().containsKey("data-key"));
    }

    @Test
    public void testRemoveDataAttributeIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("data-KEY", "data-value");
        attributes.removeIgnoreCase("data-key");
        Assert.assertEquals(0, attributes.size());
        Assert.assertFalse(attributes.hasKey("data-key"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("data-key"));
        Assert.assertNull(attributes.get("data-key"));
        Assert.assertNull(attributes.getIgnoreCase("data-key"));
        Assert.assertEquals(0, attributes.dataset().size());
        Assert.assertFalse(attributes.dataset().containsKey("data-key"));
    }

    @Test
    public void testClone() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        Attributes clonedAttributes = attributes.clone();
        Assert.assertEquals(1, attributes.size());
        Assert.assertEquals(1, clonedAttributes.size());
        Assert.assertArrayEquals(attributes.keys, clonedAttributes.keys);
        Assert.assertArrayEquals(attributes.vals, clonedAttributes.vals);
    }

    @Test
    public void testNormalize() {
        Attributes attributes = new Attributes();
        attributes.put("KEY", "value");
        attributes.normalize();
        Assert.assertEquals(1, attributes.size());
        Assert.assertEquals("key", attributes.get("key"));
        Assert.assertEquals("value", attributes.get("key"));
    }
}