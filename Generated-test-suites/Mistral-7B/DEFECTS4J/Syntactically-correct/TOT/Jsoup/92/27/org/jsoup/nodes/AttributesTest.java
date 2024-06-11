package org.jsoup.nodes.test;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;

public class AttributesTest {

    @Test
    public void testBasicAttributeOperations() {
        Attributes attributes = new Attributes();
        attributes.put("id", "test-id");
        attributes.put("class", "test-class");
        attributes.put("style", "color: red;");

        Assert.assertEquals("test-id", attributes.get("id"));
        Assert.assertEquals("test-class", attributes.get("class"));
        Assert.assertEquals("color: red;", attributes.get("style"));

        Assert.assertTrue(attributes.hasKey("id"));
        Assert.assertTrue(attributes.hasKey("class"));
        Assert.assertTrue(attributes.hasKey("style"));

        Assert.assertFalse(attributes.hasKey("nonexistent"));

        attributes.remove("class");
        Assert.assertFalse(attributes.hasKey("class"));
    }

    @Test
    public void testCaseInsensitiveAttributeOperations() {
        Attributes attributes = new Attributes();
        attributes.put("ID", "test-id");
        attributes.put("CLASS", "test-class");
        attributes.put("STYLE", "color: red;");

        Assert.assertEquals("test-id", attributes.getIgnoreCase("ID"));
        Assert.assertEquals("test-class", attributes.getIgnoreCase("CLASS"));
        Assert.assertEquals("color: red;", attributes.getIgnoreCase("STYLE"));

        Assert.assertTrue(attributes.hasKeyIgnoreCase("ID"));
        Assert.assertTrue(attributes.hasKeyIgnoreCase("CLASS"));
        Assert.assertTrue(attributes.hasKeyIgnoreCase("STYLE"));

        attributes.removeIgnoreCase("ID");
        Assert.assertFalse(attributes.hasKeyIgnoreCase("ID"));
    }

    @Test
    public void testBooleanAttributeOperations() {
        Attributes attributes = new Attributes();
        attributes.put("checked", true);
        attributes.put("disabled", false);

        Assert.assertTrue(attributes.get("checked") == null);
        Assert.assertFalse(attributes.hasKey("checked"));

        attributes.put("checked", false);
        Assert.assertFalse(attributes.get("checked"));
        Assert.assertFalse(attributes.hasKey("checked"));

        attributes.put("checked", true);
        Assert.assertNull(attributes.get("checked"));
        Assert.assertTrue(attributes.hasKey("checked"));

        attributes.put("disabled", true);
        Assert.assertNull(attributes.get("disabled"));
        Assert.assertTrue(attributes.hasKey("disabled"));

        attributes.remove("checked");
        Assert.assertFalse(attributes.hasKey("checked"));

        attributes.remove("disabled");
        Assert.assertFalse(attributes.hasKey("disabled"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullKeyAttributeAddition() {
        Attributes attributes = new Attributes();
        attributes.put(null, "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullValueAttributeAddition() {
        Attributes attributes = new Attributes();
        attributes.put("key", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicateAttributeAddition() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value1");
        attributes.put("key", "value2");
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveOutOfBoundsAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        attributes.remove(1);
    }
}