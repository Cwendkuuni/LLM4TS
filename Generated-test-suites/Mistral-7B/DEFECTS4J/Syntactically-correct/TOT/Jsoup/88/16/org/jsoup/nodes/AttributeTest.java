package org.jsoup.nodes.test;

import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Put;
import org.jsoup.nodes.TextNode;

import java.util.Arrays;

import static org.junit.Assert.*;

public class AttributeTest {

    @Test
    public void testAttributeCreation() {
        String key = "class";
        String value = "test";
        Attribute attribute = new Attribute(key, value);

        assertEquals("class", attribute.getKey());
        assertEquals("test", attribute.getValue());
    }

    @Test
    public void testAttributeCreationWithParent() {
        String key = "class";
        String value = "test";
        Attributes parent = new Attributes();
        Attribute attribute = new Attribute(key, value, parent);

        assertEquals("class", attribute.getKey());
        assertEquals("test", attribute.getValue());
        assertEquals(1, parent.size());
        assertTrue(parent.containsKey(key));
        assertEquals("test", parent.get(key));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullKey() {
        String key = null;
        String value = "test";
        Attribute attribute = new Attribute(key, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyKey() {
        String key = "";
        String value = "test";
        Attribute attribute = new Attribute(key, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullValue() {
        String key = "class";
        String value = null;
        Attribute attribute = new Attribute(key, value);
    }

    @Test
    public void testGetAndSetKey() {
        String key = "class";
        String oldKey = "oldClass";
        Attribute attribute = new Attribute(key, "test", null);

        assertEquals("class", attribute.getKey());

        attribute.setKey(oldKey);
        assertEquals("oldClass", attribute.getKey());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyKeyAfterSet() {
        String key = "class";
        String oldKey = "oldClass";
        Attribute attribute = new Attribute(key, "test", null);

        attribute.setKey("");
    }

    @Test
    public void testGetAndSetValue() {
        String key = "class";
        String oldValue = "test";
        Attribute attribute = new Attribute(key, "test", null);

        assertEquals("test", attribute.getValue());

        attribute.setValue("newTest");
        assertEquals("newTest", attribute.getValue());
        assertEquals("test", attribute.getOldValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullValueAfterSet() {
        String key = "class";
        String oldValue = "test";
        Attribute attribute = new Attribute(key, "test", null);

        attribute.setValue(null);
    }

    @Test
    public void testHtml() throws IOException {
        String key = "class";
        String value = "test";
        Attribute attribute = new Attribute(key, value, null);

        StringBuilder sb = StringUtil.borrowBuilder();
        String html = attribute.html();
        String expected = String.format("class=\"test\"", key);

        assertEquals(expected, html);

        Document document = new Document();
        attribute.html(sb, document.outputSettings());
        assertEquals(expected, sb.toString());
    }

    @Test
    public void testHtmlWithParent() throws IOException {
        String key = "class";
        String value = "test";
        Attributes parent = new Attributes();
        Attribute attribute = new Attribute(key, value, parent);

        StringBuilder sb = StringUtil.borrowBuilder();
        attribute.html(sb, new Document().outputSettings());
        String expected = String.format("class=\"test\"", key);

        assertEquals(expected, sb.toString());
        assertEquals(1, parent.size());
        assertTrue(parent.containsKey(key));
        assertEquals("test", parent.get(key));
    }

    @Test
    public void testCreateFromEncoded() {
        String unencodedKey = "class";
        String encodedValue = "class=\"test\"";
        Attribute attribute = Attribute.createFromEncoded(unencodedKey, encodedValue);

        assertEquals("class", attribute.getKey());
        assertEquals("test", attribute.getValue());
    }

    @Test
    public void testIsBooleanAttribute() {
        Attribute attribute = new Attribute("checked", null);
        assertTrue(attribute.isBooleanAttribute());

        attribute = new Attribute("class", "test");
        assertFalse(attribute.isBooleanAttribute());
    }

    @Test
    public void testIsDataAttribute() {
        Attribute attribute = new Attribute("data-test", "test");
        assertTrue(attribute.isDataAttribute());

        attribute = new Attribute("class", "test");
        assertFalse(attribute.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        Document.OutputSettings out = new Document.OutputSettings();
        Attribute attribute = new Attribute("checked", "checked");

        assertTrue(attribute.shouldCollapseAttribute(out));
        assertTrue(attribute.shouldCollapseAttribute());

        attribute = new Attribute("class", "test");
        assertFalse(attribute.shouldCollapseAttribute(out));
        assertFalse(attribute.shouldCollapseAttribute());
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        String key = "class";
        String value = "test";
        Attribute attribute = new Attribute(key, value, null);

        Attribute clonedAttribute = attribute.clone();

        assertEquals("class", clonedAttribute.getKey());
        assertEquals("test", clonedAttribute.getValue());
    }
}