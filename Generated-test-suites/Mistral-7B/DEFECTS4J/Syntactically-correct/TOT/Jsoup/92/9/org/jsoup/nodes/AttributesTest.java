package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class AttributesTest {

    @Test
    public void testEmptyAttributes() {
        Attributes attributes = new Attributes();
        Assert.assertEquals(0, attributes.size());
        Assert.assertFalse(attributes.hasKey("key"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("key"));
    }

    @Test
    public void testSettingGettingAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        Assert.assertEquals("value", attributes.get("key"));
        Assert.assertEquals("value", attributes.getIgnoreCase("key"));
        Assert.assertEquals(1, attributes.size());
        Assert.assertTrue(attributes.hasKey("key"));
        Assert.assertTrue(attributes.hasKeyIgnoreCase("key"));
    }

    @Test
    public void testSettingGettingAttributesWithDuplicates() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value1");
        attributes.put("key", "value2");
        Assert.assertEquals("value2", attributes.get("key"));
        Assert.assertEquals(1, attributes.size());
        Assert.assertTrue(attributes.hasKey("key"));
        Assert.assertTrue(attributes.hasKeyIgnoreCase("key"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSettingNullKey() {
        Attributes attributes = new Attributes();
        attributes.put(null, "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSettingNullValue() {
        Attributes attributes = new Attributes();
        attributes.put("key", null);
    }

    @Test
    public void testSettingBooleanAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("key", true);
        Assert.assertTrue(attributes.getBoolean("key"));
        Assert.assertFalse(attributes.getBoolean("key")); // boolean attributes are treated as null if not set
        Assert.assertEquals(0, attributes.size());
    }

    @Test
    public void testSettingAndGettingAttributesWithCaseInsensitiveKey() {
        Attributes attributes = new Attributes();
        attributes.put("KEY", "value");
        Assert.assertEquals("value", attributes.getIgnoreCase("key"));
        Assert.assertEquals("value", attributes.getIgnoreCase("KEY"));
    }

    @Test
    public void testSettingAndAddingAttributes() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        Attributes attributes2 = new Attributes();
        attributes2.put("key2", "value2");
        Attributes attributes3 = new Attributes();
        attributes3.addAll(attributes1);
        attributes3.addAll(attributes2);
        Assert.assertEquals(2, attributes3.size());
        Assert.assertTrue(attributes3.hasKey("key1"));
        Assert.assertTrue(attributes3.hasKey("key2"));
        Assert.assertTrue(attributes3.hasKeyIgnoreCase("key1"));
        Assert.assertTrue(attributes3.hasKeyIgnoreCase("key2"));
        Assert.assertEquals("value1", attributes3.get("key1"));
        Assert.assertEquals("value2", attributes3.get("key2"));
        Assert.assertEquals("value1", attributes3.getIgnoreCase("key1"));
        Assert.assertEquals("value2", attributes3.getIgnoreCase("key2"));
    }

    @Test
    public void testSettingAndRemovingAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        Assert.assertEquals("value1", attributes.get("key1"));
        attributes.remove("key1");
        Assert.assertFalse(attributes.hasKey("key1"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("key1"));
    }

    @Test
    public void testSettingAndRemovingAttributesWithCaseInsensitiveKey() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        Assert.assertEquals("value1", attributes.getIgnoreCase("key1"));
        attributes.removeIgnoreCase("key1");
        Assert.assertFalse(attributes.hasKey("key1"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("key1"));
    }

    @Test
    public void testSettingAndRemovingAttributesWithDuplicates() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value1");
        attributes.put("key", "value2");
        attributes.remove("key");
        Assert.assertEquals(0, attributes.size());
        Assert.assertFalse(attributes.hasKey("key"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("key"));
    }

    @Test
    public void testSettingAndRemovingBooleanAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("key", true);
        attributes.remove("key");
        Assert.assertEquals(0, attributes.size());
        Assert.assertFalse(attributes.hasKey("key"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("key"));
    }
}