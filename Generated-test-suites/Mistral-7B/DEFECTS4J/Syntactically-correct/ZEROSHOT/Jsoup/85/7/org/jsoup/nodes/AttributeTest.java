package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes.NotFound;

import java.util.Arrays;

/**
 * Test class for Attribute.java
 */
public class AttributeTest {

    @org.junit.Before
    public void setUp() {
    }

    @org.junit.After
    public void tearDown() {
    }

    /**
     * Test constructor Attribute
     */
    @org.junit.Test
    public void testConstructor() {
        String key = "testKey";
        String val = "testVal";
        Attribute attribute = new Attribute(key, val);
        assertEquals(key, attribute.getKey());
        assertEquals(val, attribute.getValue());
        assertEquals(null, attribute.getParent());
    }

    /**
     * Test constructor Attribute with parent
     */
    @org.junit.Test
    public void testConstructorWithParent() {
        String key = "testKey";
        String val = "testVal";
        Attributes parent = new Attributes();
        Attribute attribute = new Attribute(key, val, parent);
        assertEquals(key, attribute.getKey());
        assertEquals(val, attribute.getValue());
        assertEquals(parent, attribute.getParent());
    }

    /**
     * Test method getKey
     */
    @org.junit.Test
    public void testGetKey() {
        String key = "testKey";
        String val = "testVal";
        Attribute attribute = new Attribute(key, val);
        assertEquals(key, attribute.getKey());
    }

    /**
     * Test method setKey
     */
    @org.junit.Test
    public void testSetKey() {
        String keyOld = "testKeyOld";
        String keyNew = "testKeyNew";
        String val = "testVal";
        Attribute attribute = new Attribute(keyOld, val);
        attribute.setKey(keyNew);
        assertEquals(keyNew, attribute.getKey());
        assertEquals(val, attribute.getValue());
        assertNull(attribute.getParent());
    }

    /**
     * Test method getValue
     */
    @org.junit.Test
    public void testGetValue() {
        String key = "testKey";
        String val = "testVal";
        Attribute attribute = new Attribute(key, val);
        assertEquals(val, attribute.getValue());
    }

    /**
     * Test method setValue
     */
    @org.junit.Test
    public void testSetValue() {
        String key = "testKey";
        String oldVal = "testValOld";
        String newVal = "testValNew";
        Attribute attribute = new Attribute(key, oldVal, null);
        String newValReturned = attribute.setValue(newVal);
        assertEquals(newVal, attribute.getValue());
        assertEquals(oldVal, newValReturned);
        assertNull(attribute.getParent());
    }

    /**
     * Test method html
     */
    @org.junit.Test
    public void testHtml() throws IOException {
        String key = "testKey";
        String val = "testVal";
        Attribute attribute = new Attribute(key, val);
        StringBuilder sb = StringUtil.borrowBuilder();
        attribute.html(sb, null);
        String html = StringUtil.releaseBuilder(sb);
        String expected = key + "=\"testVal\"";
        assertEquals(expected, html);
    }

    /**
     * Test method toString
     */
    @org.junit.Test
    public void testToString() {
        String key = "testKey";
        String val = "testVal";
        Attribute attribute = new Attribute(key, val);
        String toString = attribute.toString();
        String expected = key + "=\"testVal\"";
        assertEquals(expected, toString);
    }

    /**
     * Test method createFromEncoded
     */
    @org.junit.Test
    public void testCreateFromEncoded() {
        String unencodedKey = "testKey";
        String encodedValue = "testKey=\"testVal\"";
        Attribute attribute = Attribute.createFromEncoded(unencodedKey, encodedValue);
        assertEquals(unencodedKey, attribute.getKey());
        assertEquals("testVal", attribute.getValue());
        assertNull(attribute.getParent());
    }

    /**
     * Test method isDataAttribute
     */
    @org.junit.Test
    public void testIsDataAttribute() {
        String key = "testKey";
        boolean expected = false;
        Attribute attribute = new Attribute(key, null);
        boolean result = attribute.isDataAttribute();
        assertEquals(expected, result);

        key = Attributes.dataPrefix + "testKey";
        expected = true;
        attribute = new Attribute(key, null);
        result = attribute.isDataAttribute();
        assertEquals(expected, result);
    }

    /**
     * Test method shouldCollapseAttribute
     */
    @org.junit.Test
    public void testShouldCollapseAttribute() {
        Document.OutputSettings out = null;
        boolean expected = false;
        Attribute attribute = new Attribute("testKey", "");
        boolean result = attribute.shouldCollapseAttribute(out);
        assertEquals(expected, result);

        expected = false;
        attribute = new Attribute("testKey", "testVal");
        result = attribute.shouldCollapseAttribute(out);
        assertEquals(expected, result);

        out = new Document.OutputSettings();
        expected = true;
        attribute = new Attribute("testKey", "");
        result = attribute.shouldCollapseAttribute(out);
        assertEquals(expected, result);

        expected = true;
        attribute = new Attribute("testKey", "testKey");
        result = attribute.shouldCollapseAttribute(out);
        assertEquals(expected, result);

        expected = true;
        attribute = new Attribute("testKey", "testVal");
        out.syntax(Document.OutputSettings.Syntax.html);
        result = attribute.shouldCollapseAttribute(out);
        assertEquals(expected, result);
    }

    /**
     * Test method isBooleanAttribute
     */
    @org.junit.Test
    public void testIsBooleanAttribute() {
        Attribute attribute = new Attribute("testKey", null);
        boolean expected = false;
        boolean result = attribute.isBooleanAttribute();
        assertEquals(expected, result);

        String[] booleanAttributes = Attribute.booleanAttributes;
        String key = booleanAttributes[0];
        expected = true;
        attribute = new Attribute(key, null);
        result = attribute.isBooleanAttribute();
        assertEquals(expected, result);

        key = "testBooleanKey";
        expected = false;
        attribute = new Attribute(key, null);
        result = attribute.isBooleanAttribute();
        assertEquals(expected, result);
    }

    /**
     * Test method equals
     */
    @org.junit.Test
    public void testEquals() {
        String key1 = "testKey1";
        String val1 = "testVal1";
        String key2 = "testKey2";
        String val2 = "testVal2";
        Attribute attribute1 = new Attribute(key1, val1);
        Attribute attribute2 = new Attribute(key2, val2);
        Attribute attribute3 = new Attribute(key1, val1);
        boolean expected = true;
        boolean result1 = attribute1.equals(attribute3);
        assertEquals(expected, result1);
        expected = false;
        result1 = attribute1.equals(attribute2);
        assertEquals(expected, result1);
    }

    /**
     * Test method hashCode
     */
    @org.junit.Test
    public void testHashCode() {
        String key1 = "testKey1";
        String val1 = "testVal1";
        String key2 = "testKey2";
        String val2 = "testVal2";
        Attribute attribute1 = new Attribute(key1, val1);
        Attribute attribute2 = new Attribute(key2, val2);
        int expected1 = key1.hashCode() * 31 + (val1 != null ? val1.hashCode() : 0);
        int result1 = attribute1.hashCode();
        assertEquals(expected1, result1);
        int expected2 = key2.hashCode() * 31 + (val2 != null ? val2.hashCode() : 0);
        int result2 = attribute2.hashCode();
        assertEquals(expected2, result2);
        assertEquals(result1, result2);
    }

    /**
     * Test method clone
     */
    @org.junit.Test
    public void testClone() throws CloneNotSupportedException {
        String key = "testKey";
        String val = "testVal";
        Attribute attribute = new Attribute(key, val);
        Attribute clone = attribute.clone();
        assertEquals(key, clone.getKey());
        assertEquals(val, clone.getValue());
        assertEquals(null, clone.getParent());
    }

    /**
     * Test method indexOfKey
     */
    @org.junit.Test
    public void testTabIndexKey() {
        Attributes attributes = new Attributes();
        String key = "testKey";
        Attribute attribute = new Attribute(key, null);
        int expected = Attributes.NotFound;
        int result = attributes.indexOfKey(key);
        assertEquals(expected, result);
        attributes.put(attribute);
        expected = 0;
        result = attributes.indexOfKey(key);
        assertEquals(expected, result);
        attributes.remove(attribute);
        expected = Attributes.NotFound;
        result = attributes.indexOfKey(key);
        assertEquals(expected, result);
    }
}