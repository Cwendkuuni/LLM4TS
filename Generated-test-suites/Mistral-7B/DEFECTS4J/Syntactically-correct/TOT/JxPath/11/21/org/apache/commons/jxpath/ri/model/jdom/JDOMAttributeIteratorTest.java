package org.apache.commons.jxpath.ri.model.jdom;

import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.StringReader;
import java.util.Collections;
import java.util.List;

public class JDOMAttributeIteratorTest {

    @org.junit.Test
    public void testEmptyAttributeIterator() {
        NodePointer parent = new JDOMPoint(new Element("root"), null);
        NodeIterator iterator = new JDOMAttributeIterator(parent, new QName("", "*"));

        assertEquals(0, iterator.getPosition());
        assertNotNull(iterator.getNodePointer());
        assertTrue(iterator.hasNext());
        assertEquals(0, iterator.getPosition());
        assertNotNull(iterator.getNodePointer());
        assertFalse(iterator.hasNext());
    }

    @org.junit.Test
    public void testAttributeIteratorWithSingleAttribute() throws JDOMException {
        String xml = "<root xmlns:x='http://example.com' myattr='value'/>";
        Document doc = new SAXBuilder().build(new StringReader(xml));
        NodePointer parent = new JDOMPoint(doc.getRootElement(), null);

        NodeIterator iterator = new JDOMAttributeIterator(parent, new QName("x", "myattr"));

        assertEquals(0, iterator.getPosition());
        assertNotNull(iterator.getNodePointer());
        assertEquals("value", iterator.getNodePointer().getNode().getValue());
        assertTrue(iterator.hasNext());
        assertEquals(1, iterator.getPosition());
        assertNull(iterator.getNodePointer());
        assertFalse(iterator.hasNext());
    }

    @org.junit.Test
    public void testAttributeIteratorWithMultipleAttributes() throws JDOMException {
        String xml = "<root xmlns:x='http://example.com' myattr1='value1' myattr2='value2'/>";
        Document doc = new SAXBuilder().build(new StringReader(xml));
        NodePointer parent = new JDOMPoint(doc.getRootElement(), null);

        NodeIterator iterator = new JDOMAttributeIterator(parent, new QName("x", "*"));

        assertEquals(0, iterator.getPosition());
        assertNotNull(iterator.getNodePointer());
        assertEquals("value1", iterator.getNodePointer().getNode().getValue());
        assertTrue(iterator.hasNext());
        assertEquals(1, iterator.getPosition());
        assertNotNull(iterator.getNodePointer());
        assertEquals("value2", iterator.getNodePointer().getNode().getValue());
        assertTrue(iterator.hasNext());
        assertEquals(2, iterator.getPosition());
        assertNull(iterator.getNodePointer());
        assertFalse(iterator.hasNext());
    }

    @org.junit.Test
    public void testAttributeIteratorWithNoMatchingAttributes() throws JDOMException {
        String xml = "<root xmlns:x='http://example.com' myattr1='value1' myattr2='value2'/>";
        Document doc = new SAXBuilder().build(new StringReader(xml));
        NodePointer parent = new JDOMPoint(doc.getRootElement(), null);

        NodeIterator iterator = new JDOMAttributeIterator(parent, new QName("y", "*"));

        assertEquals(0, iterator.getPosition());
        assertNull(iterator.getNodePointer());
        assertFalse(iterator.hasNext());
    }

    @org.junit.Test
    public void testAttributeIteratorWithNullParent() {
        NodeIterator iterator = new JDOMAttributeIterator(null, new QName("", "*"));

        assertEquals(0, iterator.getPosition());
        assertNull(iterator.getNodePointer());
        assertFalse(iterator.hasNext());
    }

    @org.junit.Test
    public void testAttributeIteratorWithNullName() {
        NodePointer parent = new JDOMPoint(new Element("root"), null);
        NodeIterator iterator = new JDOMAttributeIterator(parent, null);

        assertEquals(0, iterator.getPosition());
        assertNull(iterator.getNodePointer());
        assertFalse(iterator.hasNext());
    }

    @org.junit.Test
    public void testAttributeIteratorWithEmptyList() {
        NodePointer parent = new JDOMPoint(new Element("root"), null);
        NodeIterator iterator = new JDOMAttributeIterator(parent, new QName("", ""));

        assertEquals(0, iterator.getPosition());
        assertNull(iterator.getNodePointer());
        assertFalse(iterator.hasNext());
    }
}