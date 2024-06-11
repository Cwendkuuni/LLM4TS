package org.apache.commons.jxpath.ri.model.dom;

import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.Document;
import org.w3c.dom.DOMParser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

/**
 * Test for DOMAttributeIterator class
 */
public class DOMAttributeIteratorTest {
    private NodePointer parentNodePointer;
    private DOMAttributeIterator attributeIterator;

    @Before
    public void setUp() throws Exception {
        Document doc = DOMParser.newDocumentBuilder().newDocument();
        Element root = doc.createElement("root");
        root.setAttribute("testAttr1", "value1");
        root.setAttribute("xmlns:test", "http://test.com");
        root.setAttributeNS("http://test.com", "test:testAttr2", "value2");
        root.setAttribute("attr3", "value3");
        root.setAttribute("attr4", "value4");
        root.setAttribute("attr5", "value5");
        doc.appendChild(root);
        parentNodePointer = new NodePointer(doc, doc);
        attributeIterator = new DOMAttributeIterator(parentNodePointer, new QName("*"));
    }

    @Test
    public void testEmptyIterator() {
        DOMAttributeIterator emptyIterator = new DOMAttributeIterator(parentNodePointer, new QName("nonexistent:nonexistent"));
        assertFalse(emptyIterator.hasNext());
    }

    @Test
    public void testIteratorNext() {
        assertEquals("value1", ((Attr) attributeIterator.next().getNodePointer().getNode().getValue()));
        assertEquals("value3", ((Attr) attributeIterator.next().getNodePointer().getNode().getValue()));
        assertEquals("value4", ((Attr) attributeIterator.next().getNodePointer().getNode().getValue()));
        assertEquals("value5", ((Attr) attributeIterator.next().getNodePointer().getNode().getValue()));
        assertFalse(attributeIterator.hasNext());
    }

    @Test
    public void testIteratorGetPosition() {
        attributeIterator.next();
        attributeIterator.next();
        assertEquals(3, attributeIterator.getPosition());
    }

    @Test
    public void testIteratorSetPosition() {
        attributeIterator.next();
        attributeIterator.next();
        attributeIterator.next();
        attributeIterator.setPosition(1);
        assertEquals("value3", ((Attr) attributeIterator.getNodePointer().getNode().getValue()));
    }

    @Test
    public void testIteratorGetNodePointer() {
        NodePointer nodePointer = attributeIterator.getNodePointer();
        assertSame(nodePointer, attributeIterator.next().getNodePointer());
    }

    @Test
    public void testIteratorNextWithFilter() {
        QName filter = new QName("test", "testAttr2");
        DOMAttributeIterator filterIterator = new DOMAttributeIterator(parentNodePointer, filter);
        assertEquals("value2", ((Attr) filterIterator.next().getNodePointer().getNode().getValue()));
        assertFalse(filterIterator.hasNext());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIteratorNullParentPointer() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(null, new QName("*"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIteratorEmptyParentNode() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(null, null), new QName("*"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIteratorNullQName() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(parentNodePointer, null);
    }
}