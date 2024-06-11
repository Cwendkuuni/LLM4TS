package org.jsoup.nodes;

import org.junit.Test;
import org.junit.Assert.*;

public class AttributeTest {

    @Test
    public void testAttributeConstruction() {
        Attribute attribute = new Attribute("testKey", "testValue");
        assertEquals("testKey", attribute.getKey());
        assertEquals("testValue", attribute.getValue());

        Attribute attributeWithParent = new Attribute("testKey", "testValue", new Attributes());
        assertEquals("testKey", attributeWithParent.getKey());
        assertEquals("testValue", attributeWithParent.getValue());
    }

    @Test
    public void testGetKeyAndSetValue() {
        Attribute attribute = new Attribute("testKey", "testValue");
        assertEquals("testKey", attribute.getKey());

        attribute.setValue("newValue");
        assertEquals("newValue", attribute.getValue());
    }

    @Test
    public void testHtmlAndToString() {
        Attribute attribute = new Attribute("testKey", "testValue");
        String expectedHtml = "testKey=\"testValue\"";
        String expectedToString = "testKey=\"testValue\"";

        String html = attribute.html();
        assertEquals(expectedHtml, html);

        String toString = attribute.toString();
        assertEquals(expectedToString, toString);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructionWithNullKey() {
        new Attribute(null, "testValue");
    }

    @Test(expected = NullPointerException.class)
    public void testConstructionWithNullValue() {
        new Attribute("testKey", null);
    }

    @Test(expected = NullPointerException.class)
    public void testSetValueWithNullValue() {
        Attribute attribute = new Attribute("testKey", "testValue");
        attribute.setValue(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetKeyWithEmptyString() {
        Attribute attribute = new Attribute("testKey", "testValue");
        attribute.setKey("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetKeyWithNullKey() {
        Attribute attribute = new Attribute("testKey", "testValue");
        attribute.setKey(null);
    }
}