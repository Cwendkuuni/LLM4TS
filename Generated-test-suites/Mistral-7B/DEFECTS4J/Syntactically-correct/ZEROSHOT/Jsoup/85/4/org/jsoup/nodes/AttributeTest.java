package org.jsoup.nodes.test;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.helper.Validate;

import java.util.Arrays;
import java.util.Map;

public class AttributeTest {

    @Test
    public void testAttributeCreation() {
        // Test valid creation
        Attribute attribute = new Attribute("key", "value");
        Assert.assertEquals("key", attribute.getKey());
        Assert.assertEquals("value", attribute.getValue());

        // Test creation with parent
        Attributes attributes = new Attributes();
        Attribute attributeWithParent = new Attribute("key", "value", attributes);
        Assert.assertEquals(attributes, attributeWithParent.getParent());
    }

    @Test(expected = NullPointerException.class)
    public void testNullKey() {
        new Attribute(null, "value");
    }

    @Test(expected = NullPointerException.class)
    public void testNullValue() {
        new Attribute("key", null);
    }

    @Test
    public void testKeySetter() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("newKey");
        Assert.assertEquals("newKey", attribute.getKey());
    }

    @Test
    public void testValueSetter() {
        Attribute attribute = new Attribute("key", "value");
        String newValue = "newValue";
        String oldValue = attribute.setValue(newValue);
        Assert.assertEquals(newValue, attribute.getValue());
        Assert.assertEquals("value", oldValue);
    }

    @Test
    public void testHtmlMethod() throws Exception {
        Attribute attribute = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        attribute.html(sb, null);
        String result = StringUtil.releaseBuilder(sb);

        String expected = "key=\"value\"";
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testIsBooleanAttribute() {
        Attribute attribute = new Attribute("booleanAttribute", null);
        Assert.assertTrue(attribute.isBooleanAttribute());
        Attribute attribute2 = new Attribute("key", "value");
        Assert.assertFalse(attribute2.isBooleanAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        Attribute attribute = new Attribute("booleanAttribute", "");
        Assert.assertTrue(attribute.shouldCollapseAttribute(outputSettings));
        Attribute attribute2 = new Attribute("key", "value");
        Assert.assertFalse(attribute2.shouldCollapseAttribute(outputSettings));

        outputSettings.syntax(Document.OutputSettings.Syntax.xml);
        attribute = new Attribute("booleanAttribute", "");
        Assert.assertFalse(attribute.shouldCollapseAttribute(outputSettings));
        attribute2 = new Attribute("key", "value");
        Assert.assertTrue(attribute2.shouldCollapseAttribute(outputSettings));
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute attribute = Attribute.createFromEncoded("key", "key=\"value\"");
        Assert.assertEquals("key", attribute.getKey());
        Assert.assertEquals("value", attribute.getValue());
    }

    @Test
    public void testIsDataAttribute() {
        Attribute attribute = new Attribute("key", "value");
        Assert.assertFalse(attribute.isDataAttribute());
        Attribute dataAttribute = new Attribute(Attributes.dataPrefix + "key", "value");
        Assert.assertTrue(dataAttribute.isDataAttribute());
    }
}