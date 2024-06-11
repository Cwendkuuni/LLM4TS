package org.jsoup.nodes.test;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;

public class AttributeTest {

    @Test
    public void testAttributeCreation() {
        Attribute attribute = new Attribute("key", "value");
        Assert.assertEquals("key", attribute.getKey());
        Assert.assertEquals("value", attribute.getValue());

        Attribute attributeWithParent = new Attribute("key", "value", new Attributes());
        Assert.assertEquals("key", attributeWithParent.getKey());
        Assert.assertEquals("value", attributeWithParent.getValue());
        Assert.assertNull(attributeWithParent.getParent());
    }

    @Test
    public void testAttributeKeySetting() {
        Attribute attribute = new Attribute("oldKey", "value");
        attribute.setKey("newKey");
        Assert.assertEquals("newKey", attribute.getKey());
        Assert.assertEquals("value", attribute.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAttributeKeySettingNull() {
        Attribute attribute = new Attribute("oldKey", "value");
        attribute.setKey(null);
    }

    @Test
    public void testAttributeValueSetting() {
        Attribute attribute = new Attribute("key", "oldValue");
        attribute.setValue("newValue");
        Assert.assertEquals("key", attribute.getKey());
        Assert.assertEquals("newValue", attribute.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAttributeValueSettingNull() {
        Attribute attribute = new Attribute("key", "oldValue");
        attribute.setValue(null);
    }

    @Test
    public void testAttributeHtml() throws IOException {
        Attribute attribute = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        attribute.html(sb, new Document("").outputSettings());
        String expectedHtml = "key=\"value\"";
        Assert.assertEquals(expectedHtml, StringUtil.releaseBuilder(sb));
    }

    @Test
    public void testShouldCollapseAttribute() throws IOException {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        Attribute attribute = new Attribute("boolAttribute", "");
        Assert.assertTrue(attribute.shouldCollapseAttribute(outputSettings));

        attribute = new Attribute("boolAttribute", "false");
        Assert.assertFalse(attribute.shouldCollapseAttribute(outputSettings));

        outputSettings.syntax(Document.OutputSettings.Syntax.xml);
        attribute = new Attribute("boolAttribute", "false");
        Assert.assertTrue(attribute.shouldCollapseAttribute(outputSettings));
    }

    @Test
    public void testIsBooleanAttribute() {
        Attribute attribute = new Attribute("boolAttribute", "");
        Assert.assertTrue(attribute.isBooleanAttribute());

        attribute = new Attribute("key", "value");
        Assert.assertFalse(attribute.isBooleanAttribute());
    }
}