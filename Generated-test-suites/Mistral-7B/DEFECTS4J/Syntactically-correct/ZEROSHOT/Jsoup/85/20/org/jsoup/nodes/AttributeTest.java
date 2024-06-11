package org.jsoup.nodes;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;

import java.util.Map;

public class AttributeTest {

    @Test
    public void testAttributeCreation() {
        Attribute attribute = new Attribute("key", "value");
        Assert.assertEquals("key", attribute.getKey());
        Assert.assertEquals("value", attribute.getValue());
    }

    @Test
    public void testSettingKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("newKey");
        Assert.assertEquals("newKey", attribute.getKey());
    }

    @Test(expected = NullPointerException.class)
    public void testSettingNullKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey(null);
    }

    @Test
    public void testSettingValue() {
        Attribute attribute = new Attribute("key", "value");
        String newValue = "newValue";
        String oldValue = attribute.getValue();
        attribute.setValue(newValue);
        Assert.assertEquals(newValue, attribute.getValue());
        Assert.assertEquals(oldValue, attribute.getOldValue()); // oldValue is not specified in the code, but can be assumed to be the initial value "value"
    }

    @Test(expected = NullPointerException.class)
    public void testSettingNullValue() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setValue(null);
    }

    @Test
    public void testHtmlMethod() throws IOException {
        Attribute attribute = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        attribute.html(sb, new Document("").outputSettings());
        Assert.assertEquals("key=\"value\"", StringUtil.releaseBuilder(sb));
    }

    @Test
    public void testToStringMethod() {
        Attribute attribute = new Attribute("key", "value");
        Assert.assertEquals("key=\"value\"", attribute.toString());
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute attribute = Attribute.createFromEncoded("unencodedKey", "encodedValue=\"encodedValue\"");
        Assert.assertEquals("unencodedKey", attribute.getKey());
        Assert.assertEquals("encodedValue", attribute.getValue());
    }

    @Test
    public void testIsDataAttribute() {
        Attribute attribute = new Attribute("data-key", null);
        Assert.assertTrue(attribute.isDataAttribute());

        Attribute nonDataAttribute = new Attribute("key", null);
        Assert.assertFalse(nonDataAttribute.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() throws IOException {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        Attribute booleanAttribute = new Attribute("checked", null);
        Attribute nonBooleanAttribute = new Attribute("key", "value");

        Assert.assertTrue(booleanAttribute.shouldCollapseAttribute(outputSettings));
        Assert.assertFalse(nonBooleanAttribute.shouldCollapseAttribute(outputSettings));
    }

    @Test
    public void testIsBooleanAttribute() {
        Attribute booleanAttribute = new Attribute("checked", null);
        Attribute nonBooleanAttribute = new Attribute("key", null);

        Assert.assertTrue(booleanAttribute.isBooleanAttribute());
        Assert.assertFalse(nonBooleanAttribute.isBooleanAttribute());
    }

    @Test
    public void testEqualsMethod() {
        Attribute attribute1 = new Attribute("key1", "value1");
        Attribute attribute2 = new Attribute("key1", "value1");
        Attribute attribute3 = new Attribute("key2", "value2");

        Assert.assertEquals(attribute1, attribute1);
        Assert.assertEquals(attribute1, attribute2);
        Assert.assertNotEquals(attribute1, attribute3);
    }

    @Test
    public void testHashCodeMethod() {
        Attribute attribute1 = new Attribute("key1", "value1");
        Attribute attribute2 = new Attribute("key1", "value1");
        Attribute attribute3 = new Attribute("key2", "value2");

        Assert.assertEquals(attribute1.hashCode(), attribute1.hashCode());
        Assert.assertEquals(attribute1.hashCode(), attribute2.hashCode());
        Assert.assertNotEquals(attribute1.hashCode(), attribute3.hashCode());
    }

    @Test
    public void testCloneMethod() throws CloneNotSupportedException {
        Attribute attribute = new Attribute("key", "value");
        Attribute clonedAttribute = attribute.clone();

        Assert.assertEquals(attribute, clonedAttribute);
        Assert.assertNotSame(attribute, clonedAttribute);
    }
}