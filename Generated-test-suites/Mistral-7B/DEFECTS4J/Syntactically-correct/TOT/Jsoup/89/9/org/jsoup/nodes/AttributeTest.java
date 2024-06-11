package org.jsoup.nodes.test;

import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;

import static org.junit.Assert.*;

/**
 * Testing for Attribute class.
 */
public class AttributeTest {

    /**
     * Testing constructor with unencoded key and value.
     */
    @Test
    public void testAttributeUnencodedKeyValue() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
    }

    /**
     * Testing constructor with unencoded key, value and parent.
     */
    @Test
    public void testAttributeUnencodedKeyValueParent() {
        Attributes attributes = new Attributes();
        Attribute attribute = new Attribute("key", "value", attributes);
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
        assertEquals(attributes, attribute.getParent());
    }

    /**
     * Testing getKey method.
     */
    @Test
    public void testGetKey() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key", attribute.getKey());

        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
    }

    /**
     * Testing setKey method.
     */
    @Test
    public void testSetKey() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key", attribute.getKey());

        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
    }

    /**
     * Testing getValue method.
     */
    @Test
    public void testGetValue() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("value", attribute.getValue());
    }

    /**
     * Testing setValue method.
     */
    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("value", attribute.getValue());

        attribute.setValue("newValue");
        assertEquals("newValue", attribute.getValue());
    }

    /**
     * Testing html method.
     */
    @Test
    public void testHtml() throws IOException {
        Document doc = new Document();
        Attribute attribute = new Attribute("key", "value");
        attribute.html(doc.appendBody().appendElement("p"));
        assertEquals("key=\"value\"", doc.body().toString());
    }

    /**
     * Testing createFromEncoded method.
     */
    @Test
    public void testCreateFromEncoded() {
        Attribute attribute = Attribute.createFromEncoded("key", "key=\"value\"");
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
    }

    /**
     * Testing isDataAttribute method.
     */
    @Test
    public void testIsDataAttribute() {
        Attribute attribute = new Attribute("key", "value");
        assertFalse(attribute.isDataAttribute());

        attribute = new Attribute("data-key", "value");
        assertTrue(attribute.isDataAttribute());
    }

    /**
     * Testing shouldCollapseAttribute method.
     */
    @Test
    public void testShouldCollapseAttribute() {
        Document.OutputSettings out = Document.OutputSettings.htmlParadigm();
        Attribute attribute = new Attribute("key", "value");
        assertFalse(attribute.shouldCollapseAttribute(out));

        attribute = new Attribute("key", "");
        assertTrue(attribute.shouldCollapseAttribute(out));

        attribute = new Attribute("boolean", "");
        assertTrue(attribute.shouldCollapseAttribute(out));
    }

    /**
     * Testing equals method.
     */
    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("key1", "value1");
        Attribute attribute2 = new Attribute("key2", "value2");
        Attribute attribute3 = new Attribute("key1", "value1");

        assertFalse(attribute1.equals(attribute2));
        assertTrue(attribute1.equals(attribute3));
    }

    /**
     * Testing hashCode method.
     */
    @Test
    public void testHashCode() {
        Attribute attribute1 = new Attribute("key1", "value1");
        Attribute attribute2 = new Attribute("key1", "value1");
        Attribute attribute3 = new Attribute("key2", "value2");

        assertEquals(attribute1.hashCode(), attribute1.hashCode());
        assertEquals(attribute1.hashCode(), attribute3.hashCode());
        assertNotEquals(attribute1.hashCode(), attribute2.hashCode());
    }

    /**
     * Testing clone method.
     */
    @Test
    public void testClone() {
        Attribute attribute = new Attribute("key", "value");
        Attribute clonedAttribute = attribute.clone();

        assertEquals(attribute.getKey(), clonedAttribute.getKey());
        assertEquals(attribute.getValue(), clonedAttribute.getValue());
    }
}