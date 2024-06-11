package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;

import java.util.Arrays;

public class AttributeTest {

    @org.junit.Test
    public void testAttributeCreation() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());

        Attribute attributeWithParent = new Attribute("key", "value", new Attributes());
        assertEquals("key", attributeWithParent.getKey());
        assertEquals("value", attributeWithParent.getValue());
        assertNull(attributeWithParent.getParent());
    }

    @org.junit.Test
    public void testSettingKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
    }

    @org.junit.Test
    public void testSettingValue() {
        Attribute attribute = new Attribute("key", "value");
        String newValue = "newValue";
        String oldValue = attribute.getValue();
        attribute.setValue(newValue);
        assertEquals(newValue, attribute.getValue());
        assertEquals(oldValue, attribute.getOldValue());
    }

    @org.junit.Test
    public void testHtmlMethod() throws IOException {
        Attribute attribute = new Attribute("key", "value");
        StringBuilder accumulator = StringUtil.borrowBuilder();
        attribute.html(accumulator, new Document("").outputSettings());
        String expectedHtml = "key=\"value\"";
        assertEquals(expectedHtml, StringUtil.releaseBuilder(accumulator));
    }

    @org.junit.Test
    public void testCreateFromEncoded() {
        Attribute attribute = Attribute.createFromEncoded("key", "value%3D%3D");
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
    }

    @org.junit.Test
    public void testIsDataAttribute() {
        Attribute attribute = new Attribute("data-key", "value");
        assertTrue(attribute.isDataAttribute());

        Attribute attributeWithoutPrefix = new Attribute("key", "value");
        assertFalse(attributeWithoutPrefix.isDataAttribute());
    }

    @org.junit.Test
    public void testShouldCollapseAttribute() {
        Document.OutputSettings settings = new Document.OutputSettings();
        Attribute attribute = new Attribute("key", "value");
        assertTrue(attribute.shouldCollapseAttribute(settings));
        assertTrue(attribute.shouldCollapseAttribute("", settings));
        assertTrue(attribute.shouldCollapseAttribute(null, settings));

        Attribute booleanAttribute = new Attribute("checked", "");
        assertTrue(booleanAttribute.shouldCollapseAttribute(settings));

        Document.OutputSettings settingsWithSyntax = new Document.OutputSettings();
        settingsWithSyntax.syntax(Document.OutputSettings.Syntax.xml);
        Attribute xmlBooleanAttribute = new Attribute("checked", "");
        assertFalse(xmlBooleanAttribute.shouldCollapseAttribute(settingsWithSyntax));
    }

    @org.junit.Test
    public void testIsBooleanAttribute() {
        Attribute attribute = new Attribute("checked", null);
        assertTrue(attribute.isBooleanAttribute());

        Attribute attributeWithoutBooleanValue = new Attribute("key", "value");
        assertFalse(attributeWithoutBooleanValue.isBooleanAttribute());
    }

    @org.junit.Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("key1", "value1");
        Attribute attribute2 = new Attribute("key2", "value2");
        Attribute attribute3 = new Attribute("key1", "value1");

        assertFalse(attribute1.equals(null));
        assertFalse(attribute1.equals(new Object()));
        assertFalse(attribute1.equals(attribute2));
        assertTrue(attribute1.equals(attribute3));
    }

    @org.junit.Test
    public void testHashCode() {
        Attribute attribute1 = new Attribute("key1", "value1");
        Attribute attribute2 = new Attribute("key2", "value2");
        Attribute attribute3 = new Attribute("key1", "value1");

        int hashCode1 = attribute1.hashCode();
        int hashCode2 = attribute2.hashCode();
        int hashCode3 = attribute3.hashCode();

        assertEquals(hashCode1, hashCode3);
        assertNotEquals(hashCode1, hashCode2);
    }

    @org.junit.Test
    public void testClone() throws CloneNotSupportedException {
        Attribute attribute = new Attribute("key", "value");
        Attribute clonedAttribute = attribute.clone();

        assertEquals(attribute.getKey(), clonedAttribute.getKey());
        assertEquals(attribute.getValue(), clonedAttribute.getValue());
        assertNotSame(attribute.getParent(), clonedAttribute.getParent());
    }

    @org.junit.Test
    public void testToString() {
        Attribute attribute = new Attribute("key", "value");
        String html = attribute.html();
        String toString = attribute.toString();

        assertEquals(html, toString);
    }
}