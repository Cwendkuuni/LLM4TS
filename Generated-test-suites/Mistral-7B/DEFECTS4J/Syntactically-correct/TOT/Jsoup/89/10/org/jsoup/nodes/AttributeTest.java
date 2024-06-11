package org.jsoup.nodes.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;

import java.util.Arrays;

public class AttributeTest {

    @Test
    public void testConstructorWithKeyAndValue() {
        Attribute attribute = new Attribute("key", "value");
        Assertions.assertEquals("key", attribute.getKey());
        Assertions.assertEquals("value", attribute.getValue());
    }

    @Test
    public void testConstructorWithKeyValueAndParent() {
        Attributes parentAttributes = new Attributes();
        Attribute attribute = new Attribute("key", "value", parentAttributes);
        Assertions.assertEquals("key", attribute.getKey());
        Assertions.assertEquals("value", attribute.getValue());
        Assertions.assertEquals(attribute, parentAttributes.get("key"));
    }

    @Test
    public void testSetKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("newKey");
        Assertions.assertEquals("newKey", attribute.getKey());
    }

    @Test
    public void testSetKeyValidation() {
        Attribute attribute = new Attribute("key", "value");
        Assertions.assertThrows(IllegalArgumentException.class, () -> attribute.setKey(null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> attribute.setKey(""));
    }

    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setValue("newValue");
        Assertions.assertEquals("newValue", attribute.getValue());
    }

    @Test
    public void testSetValueValidation() {
        Attribute attribute = new Attribute("key", "value");
        Assertions.assertThrows(IllegalArgumentException.class, () -> attribute.setValue(null));
    }

    @Test
    public void testHtml() throws IOException {
        Attribute attribute = new Attribute("key", "value");
        StringBuilder expected = StringUtil.borrowBuilder();
        attribute.html(expected, new Document("").outputSettings());
        String actual = attribute.html();
        Assertions.assertEquals(expected.toString(), actual);
    }

    @Test
    public void testIsDataAttribute() {
        Attribute attribute = new Attribute("key", "value");
        Assertions.assertFalse(attribute.isDataAttribute());

        Attribute dataAttribute = new Attribute(Attributes.dataPrefix + "test", "value");
        Assertions.assertTrue(dataAttribute.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.syntax(Document.OutputSettings.Syntax.html);

        Attribute attribute = new Attribute("key", "");
        Assertions.assertTrue(attribute.shouldCollapseAttribute(outputSettings));

        Attribute booleanAttribute = new Attribute("checked", "");
        Assertions.assertTrue(booleanAttribute.shouldCollapseAttribute(outputSettings));

        Attribute nonBooleanAttribute = new Attribute("key", "value");
        Assertions.assertFalse(nonBooleanAttribute.shouldCollapseAttribute(outputSettings));
    }

    @Test
    public void testIsBooleanAttribute() {
        Attribute attribute = new Attribute("key", "value");
        Assertions.assertFalse(attribute.isBooleanAttribute());

        Attribute booleanAttribute = new Attribute("checked", null);
        Assertions.assertTrue(booleanAttribute.isBooleanAttribute());

        Attribute booleanAttributeWithValue = new Attribute("checked", "");
        Assertions.assertTrue(booleanAttributeWithValue.isBooleanAttribute());

        Attribute nonBooleanAttribute = new Attribute("key", "value");
        Assertions.assertFalse(nonBooleanAttribute.isBooleanAttribute());
    }

    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("key1", "value1");
        Attribute attribute2 = new Attribute("key2", "value2");
        Attribute attribute3 = new Attribute("key1", "value1");

        Assertions.assertFalse(attribute1.equals(attribute2));
        Assertions.assertTrue(attribute1.equals(attribute3));
    }

    @Test
    public void testHashCode() {
        Attribute attribute1 = new Attribute("key1", "value1");
        Attribute attribute2 = new Attribute("key2", "value2");
        Attribute attribute3 = new Attribute("key1", "value1");

        Assertions.assertNotEquals(attribute1.hashCode(), attribute2.hashCode());
        Assertions.assertEquals(attribute1.hashCode(), attribute3.hashCode());
    }

    @Test
    public void testClone() {
        Attribute attribute = new Attribute("key", "value");
        Attribute clonedAttribute = attribute.clone();

        Assertions.assertEquals(attribute.getKey(), clonedAttribute.getKey());
        Assertions.assertEquals(attribute.getValue(), clonedAttribute.getValue());
    }
}