package org.jsoup.nodes;

import org.junit.Test;
import org.junit.Assert;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;

import java.util.Arrays;
import java.util.Map;

public class AttributeTest {

    @Test
    public void testAttributeCreationUnencodedKeyValue() {
        Attribute attribute = new Attribute("key", "value");
        Assert.assertEquals("key", attribute.getKey());
        Assert.assertEquals("value", attribute.getValue());
    }

    @Test
    public void testAttributeCreationUnencodedKeyValueParent() {
        Attributes attributes = new Attributes();
        Attribute attribute = new Attribute("key", "value", attributes);
        Assert.assertEquals("key", attribute.getKey());
        Assert.assertEquals("value", attribute.getValue());
        Assert.assertEquals(attribute, attributes.get("key"));
    }

    @Test(expected = NullPointerException.class)
    public void testAttributeCreationNullKey() {
        new Attribute(null, "value");
    }

    @Test(expected = NullPointerException.class)
    public void testAttributeCreationNullValue() {
        new Attribute("key", null);
    }

    @Test
    public void testAttributeKeySetter() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("newKey");
        Assert.assertEquals("newKey", attribute.getKey());
    }

    @Test(expected = NullPointerException.class)
    public void testAttributeKeySetterNullKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey(null);
    }

    @Test
    public void testAttributeValueSetter() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setValue("newValue");
        Assert.assertEquals("newValue", attribute.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testAttributeValueSetterNullValue() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setValue(null);
    }

    @Test
    public void testAttributeEquals() {
        Attribute attribute1 = new Attribute("key1", "value1");
        Attribute attribute2 = new Attribute("key1", "value1");
        Attribute attribute3 = new Attribute("key2", "value2");

        Assert.assertEquals(attribute1, attribute1);
        Assert.assertEquals(attribute1, attribute2);
        Assert.assertNotEquals(attribute1, attribute3);
    }

    @Test
    public void testShouldCollapseAttribute() {
        Attribute attribute = new Attribute("key", "");
        Assert.assertTrue(attribute.shouldCollapseAttribute(null));

        Attribute booleanAttribute = new Attribute("checked", null);
        Assert.assertTrue(booleanAttribute.shouldCollapseAttribute(null));

        Attribute nonBooleanAttribute = new Attribute("key", "value");
        Assert.assertFalse(nonBooleanAttribute.shouldCollapseAttribute(null));
    }

    @Test
    public void testIsBooleanAttribute() {
        Attribute attribute = new Attribute("key", null);
        Assert.assertTrue(attribute.isBooleanAttribute());

        Attribute nonBooleanAttribute = new Attribute("key", "value");
        Assert.assertFalse(nonBooleanAttribute.isBooleanAttribute());
    }

    @Test
    public void testIsBooleanAttributeWithEncodedKey() {
        Attribute attribute = Attribute.createFromEncoded("data-key", null);
        Assert.assertTrue(attribute.isBooleanAttribute());

        Attribute nonBooleanAttribute = new Attribute("key", "value");
        Assert.assertFalse(nonBooleanAttribute.isBooleanAttribute());
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute attribute = Attribute.createFromEncoded("data-key", null);
        Assert.assertEquals("data-key", attribute.getKey());
        Assert.assertNull(attribute.getValue());
    }
}