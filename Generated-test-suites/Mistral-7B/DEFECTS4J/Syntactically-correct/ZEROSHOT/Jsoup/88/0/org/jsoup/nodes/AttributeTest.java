package org.jsoup.nodes;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.helper.Validate;

import org.junit.Test;

/**
 * Test class for Attribute.java
 */
public class AttributeTest {

    /**
     * Test constructor Attribute.
     */
    @Test
    public void testAttribute() {
        String key = "testKey";
        String val = "testVal";
        Attribute attribute = new Attribute(key, val);

        assertEquals(key, attribute.getKey());
        assertEquals(val, attribute.getValue());
        assertNull(attribute.getParent());
    }

    /**
     * Test constructor Attribute with parent.
     */
    @Test
    public void testAttributeWithParent() {
        String key = "testKey";
        String val = "testVal";
        Attributes parent = new Attributes();
        Attribute attribute = new Attribute(key, val, parent);

        assertEquals(key, attribute.getKey());
        assertEquals(val, attribute.getValue());
        assertEquals(parent, attribute.getParent());
    }

    /**
     * Test setKey method.
     */
    @Test
    public void testSetKey() {
        String originalKey = "testKey";
        String newKey = "newTestKey";
        String val = "testVal";
        Attribute attribute = new Attribute(originalKey, val, null);

        attribute.setKey(newKey);
        assertEquals(newKey, attribute.getKey());
    }

    /**
     * Test setValue method.
     */
    @Test
    public void testSetValue() {
        String key = "testKey";
        String originalVal = "testVal";
        String newVal = "newTestVal";
        Attribute attribute = new Attribute(key, originalVal, null);

        String oldVal = attribute.setValue(newVal);
        assertEquals(newVal, attribute.getValue());
        assertEquals(originalVal, oldVal);
    }

    /**
     * Test html method.
     */
    @Test
    public void testHtml() throws IOException {
        String key = "testKey";
        String val = "testVal";
        Attribute attribute = new Attribute(key, val, null);

        StringBuilder expected = new StringBuilder();
        attribute.html(expected, null);
        String result = expected.toString();
        String expectedFull = String.format("%s=\"%s\"", key, val);
        assertEquals(expectedFull, result);
    }

    /**
     * Test createFromEncoded method.
     */
    @Test
    public void testCreateFromEncoded() {
        String unencodedKey = "testKey";
        String encodedValue = "testKey=\"testVal\"";
        Attribute attribute = Attribute.createFromEncoded(unencodedKey, encodedValue);

        assertEquals(unencodedKey, attribute.getKey());
        assertEquals("testVal", attribute.getValue());
    }

    /**
     * Test isDataAttribute method.
     */
    @Test
    public void testIsDataAttribute() {
        String key = "testKey";
        assertTrue(Attribute.isDataAttribute(key));
    }

    /**
     * Test shouldCollapseAttribute method.
     */
    @Test
    public void testShouldCollapseAttribute() throws IOException {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        String key = "testKey";
        String val = "testVal";
        Attribute attribute = new Attribute(key, val, null);

        assertTrue(attribute.shouldCollapseAttribute(outputSettings));
        assertFalse(attribute.shouldCollapseAttribute("", outputSettings));
        assertFalse(attribute.shouldCollapseAttribute(null, outputSettings));
        assertFalse(attribute.shouldCollapseAttribute("newKey", outputSettings));
        assertFalse(attribute.shouldCollapseAttribute("newKey", "newVal", outputSettings));
    }

    /**
     * Test isBooleanAttribute method.
     */
    @Test
    public void testIsBooleanAttribute() {
        String key = "testKey";
        assertTrue(Attribute.isBooleanAttribute(key));
        assertFalse(Attribute.isBooleanAttribute("newKey"));
    }

    /**
     * Test equals method.
     */
    @Test
    public void testEquals() {
        String key1 = "testKey1";
        String val1 = "testVal1";
        String key2 = "testKey2";
        String val2 = "testVal2";
        Attribute attribute1 = new Attribute(key1, val1, null);
        Attribute attribute2 = new Attribute(key2, val2, null);
        Attribute attribute3 = new Attribute(key1, val1, null);

        assertTrue(attribute1.equals(attribute1));
        assertTrue(attribute1.equals(attribute3));
        assertFalse(attribute1.equals(attribute2));
    }

    /**
     * Test hashCode method.
     */
    @Test
    public void testHashCode() {
        String key1 = "testKey1";
        String val1 = "testVal1";
        String key2 = "testKey2";
        String val2 = "testVal2";
        Attribute attribute1 = new Attribute(key1, val1, null);
        Attribute attribute2 = new Attribute(key2, val2, null);

        int hashCode1 = attribute1.hashCode();
        int hashCode2 = attribute2.hashCode();

        assertNotEquals(hashCode1, hashCode2);
    }

    /**
     * Test clone method.
     */
    @Test
    public void testClone() {
        String key = "testKey";
        String val = "testVal";
        Attribute attribute = new Attribute(key, val, null);

        Attribute clonedAttribute = attribute.clone();

        assertEquals(key, clonedAttribute.getKey());
        assertEquals(val, clonedAttribute.getValue());
    }
}