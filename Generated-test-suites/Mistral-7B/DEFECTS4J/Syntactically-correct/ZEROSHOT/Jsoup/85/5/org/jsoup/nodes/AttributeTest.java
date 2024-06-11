package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.junit.Test;

/**
 * Test for Attribute class
 */
public class AttributeTest {

    @Test
    public void testAttributeCreation() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());

        Attribute attributeWithParent = new Attribute("key", "value", new Attributes());
        assertEquals("key", attributeWithParent.getKey());
        assertEquals("value", attributeWithParent.getValue());
        assertNull(attributeWithParent.getParent());
    }

    @Test(expected = NullPointerException.class)
    public void testNullKey() {
        new Attribute(null, "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyKey() {
        new Attribute("", "value");
    }

    @Test(expected = NullPointerException.class)
    public void testNullValue() {
        new Attribute("key", null);
    }

    @Test
    public void testKeySetting() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
    }

    @Test
    public void testValueSetting() {
        Attribute attribute = new Attribute("key", "value");
        String newValue = "newValue";
        String oldValue = attribute.setValue(newValue);
        assertEquals(newValue, attribute.getValue());
        assertEquals("value", oldValue);
    }

    @Test
    public void testHtml() throws IOException {
        Attribute attribute = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        attribute.html(sb, new Document("").outputSettings());
        String html = StringUtil.releaseBuilder(sb);
        assertEquals("key=\"value\"", html);
    }

    @Test
    public void testCreateFromEncoded() {
        String encodedKey = "data-my-key";
        String encodedValue = "data-my-value";
        Attribute attribute = Attribute.createFromEncoded(encodedKey.substring(Attributes.dataPrefix.length()), encodedValue);
        assertEquals(encodedKey, attribute.getKey());
        assertEquals(encodedValue, attribute.getValue());
    }

    @Test
    public void testBooleanAttribute() {
        Attribute booleanAttribute = new Attribute("checked", null);
        assertTrue(booleanAttribute.isBooleanAttribute());
        Attribute nonBooleanAttribute = new Attribute("key", "value");
        assertFalse(nonBooleanAttribute.isBooleanAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        Attribute attribute = new Attribute("checked", null);
        assertTrue(attribute.shouldCollapseAttribute(outputSettings));
        Attribute nonCollapsableAttribute = new Attribute("key", "value");
        assertFalse(nonCollapsableAttribute.shouldCollapseAttribute(outputSettings));
    }

    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("key1", "value1");
        Attribute attribute2 = new Attribute("key1", "value1");
        Attribute attribute3 = new Attribute("key2", "value2");
        assertTrue(attribute1.equals(attribute2));
        assertFalse(attribute1.equals(attribute3));
    }

    @Test
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

    @Test
    public void testClone() {
        Attribute attribute = new Attribute("key", "value");
        Attribute clonedAttribute = attribute.clone();
        assertEquals(attribute.getKey(), clonedAttribute.getKey());
        assertEquals(attribute.getValue(), clonedAttribute.getValue());
    }

    @Test(expected = CloneNotSupportedException.class)
    public void testCloneException() {
        Attribute attribute = new Attribute("key", "value");
        attribute.clone();
    }
}