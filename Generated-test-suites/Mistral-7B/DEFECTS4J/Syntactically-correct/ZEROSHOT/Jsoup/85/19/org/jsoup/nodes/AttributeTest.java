package org.jsoup.nodes.test;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes.NotFound;

import java.util.Arrays;

/**
 * Test class for Attribute.java
 */
public class AttributeTest {

    @org.junit.Test
    public void testAttributeCreation() {
        Attribute attribute = new Attribute("key", "value");
        Attribute attributeWithParent = new Attribute("key", "value", new Attributes());

        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
        assertEquals(null, attribute.getParent());

        assertEquals("key", attributeWithParent.getKey());
        assertEquals("value", attributeWithParent.getValue());
        assertNotNull(attributeWithParent.getParent());
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
        attribute.setValue("newValue");

        assertEquals("newValue", attribute.getValue());
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void testSettingNullKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey(null);
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void testSettingNullValue() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setValue(null);
    }

    @org.junit.Test
    public void testHtml() throws Exception {
        Attribute attribute = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        attribute.html(sb, new Document("").outputSettings());

        String expectedHtml = String.format("key=%s", "value");
        assertEquals(expectedHtml, StringUtil.releaseBuilder(sb));
    }

    @org.junit.Test
    public void testCreateFromEncoded() {
        Attribute attribute = Attribute.createFromEncoded("key", "%68%65%61%64%65%64");

        assertEquals("key", attribute.getKey());
        assertEquals("hello", attribute.getValue());
    }

    @org.junit.Test
    public void testIsDataAttribute() {
        Attribute attribute = new Attribute("data-key", "");
        assertTrue(attribute.isDataAttribute());

        Attribute attribute2 = new Attribute("key", "value");
        assertFalse(attribute2.isDataAttribute());
    }

    @org.junit.Test
    public void testShouldCollapseAttribute() {
        assertTrue(new Attribute("checked", "").shouldCollapseAttribute(null));
        assertTrue(new Attribute("checked", "checked").shouldCollapseAttribute(null));
        assertFalse(new Attribute("checked", "checked").shouldCollapseAttribute(new Document.OutputSettings()));
        assertFalse(new Attribute("key", "value").shouldCollapseAttribute(new Document.OutputSettings()));
    }

    @org.junit.Test
    public void testIsBooleanAttribute() {
        Attribute attribute = new Attribute("checked", "");
        assertTrue(attribute.isBooleanAttribute());

        Attribute attribute2 = new Attribute("key", "value");
        assertFalse(attribute2.isBooleanAttribute());
    }

    @org.junit.Test
    public void testIsBooleanAttributeWithParent() {
        Attribute attribute = new Attribute("checked", "");
        Attributes attributes = new Attributes();
        attribute.setParent(attributes);

        assertTrue(attribute.isBooleanAttribute());
        assertFalse(attributes.get(new Attribute("key", "value")).isBooleanAttribute());
    }

    @org.junit.Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("key1", "value1");
        Attribute attribute2 = new Attribute("key1", "value1");
        Attribute attribute3 = new Attribute("key2", "value2");

        assertEquals(attribute1, attribute1);
        assertEquals(attribute1, attribute2);
        assertNotEquals(attribute1, attribute3);
    }

    @org.junit.Test
    public void testHashCode() {
        Attribute attribute1 = new Attribute("key1", "value1");
        Attribute attribute2 = new Attribute("key1", "value1");
        Attribute attribute3 = new Attribute("key2", "value2");

        int hashCode1 = attribute1.hashCode();
        int hashCode2 = attribute2.hashCode();
        int hashCode3 = attribute3.hashCode();

        assertEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
    }

    @org.junit.Test
    public void testClone() {
        Attribute attribute = new Attribute("key", "value");
        Attribute clonedAttribute = attribute.clone();

        assertEquals(attribute, clonedAttribute);
        assertNotSame(attribute, clonedAttribute);
    }

    @org.junit.Test(expected = CloneNotSupportedException.class)
    public void testCloneException() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    @org.junit.Test
    public void testIndexOfKey() {
        Attributes attributes = new Attributes();
        attributes.put(new Attribute("key1", "value1"));
        attributes.put(new Attribute("key2", "value2"));

        assertEquals(0, attributes.indexOfKey("key1"));
        assertEquals(1, attributes.indexOfKey("key2"));
        assertEquals(Attributes.NotFound, attributes.indexOfKey("key3"));
    }
}