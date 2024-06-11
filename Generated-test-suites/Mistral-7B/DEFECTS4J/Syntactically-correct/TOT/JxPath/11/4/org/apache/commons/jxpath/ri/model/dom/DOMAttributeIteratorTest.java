package org.apache.commons.jxpath.ri.model.dom;

import org.apache.commons.jxpath.ri.QName;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class DOMAttributeIteratorTest {

    private Document doc;
    private Element root;

    @Before
    public void setUp() {
        doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        root = doc.createElement("root");
        doc.appendChild(root);
    }

    @Test
    public void testEmptyIterator() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(doc, doc.getDocumentElement()), null);
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }

    @Test
    public void testSingleAttributeIterator() {
        Element child1 = doc.createElement("child");
        child1.setAttribute("id", "1");
        root.appendChild(child1);

        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(doc, root), new QName("*", "id"));
        assertTrue(iterator.hasNext());
        assertEquals("1", ((Attr) iterator.next()).getNodeValue());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testMultipleAttributesIterator() {
        Element child1 = doc.createElement("child");
        child1.setAttribute("id", "1");
        child1.setAttribute("class", "test");
        root.appendChild(child1);

        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(doc, root), new QName("*", "id"));
        assertTrue(iterator.hasNext());
        assertEquals("1", ((Attr) iterator.next()).getNodeValue());
        assertTrue(iterator.hasNext());
        assertFalse(iterator.hasNext());

        iterator = new DOMAttributeIterator(new DOMPoint(doc, root), new QName("*", "class"));
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testPrefixNamespaceIterator() {
        doc.setXmlNamespaceAware(true);
        Element child1 = doc.createElement("{http://test.com}child");
        child1.setAttribute("{http://test.com}id", "1");
        root.appendChild(child1);

        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(doc, root), new QName("{http://test.com}", "id"));
        assertTrue(iterator.hasNext());
        assertEquals("1", ((Attr) iterator.next()).getNodeValue());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testEmptyElementIterator() {
        Element emptyElement = doc.createElement("empty");
        root.appendChild(emptyElement);

        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(doc, emptyElement), null);
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }

    @Test
    public void testNullElementIterator() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(null, null);
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullNameIterator() {
        Element element = doc.createElement("child");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(doc, element), null);
        iterator.next();
        iterator.setPosition(1);
        iterator.next(); // No exception, but should be
        iterator.next(); // Exception should be thrown here
    }

    @Test
    public void testOutOfBoundsIterator() {
        Element child1 = doc.createElement("child");
        child1.setAttribute("id", "1");
        child1.setAttribute("class", "test");
        root.appendChild(child1);

        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(doc, root), new QName("*", "id"));
        assertTrue(iterator.hasNext());
        assertEquals("1", ((Attr) iterator.next()).getNodeValue());
        assertTrue(iterator.hasNext());

        iterator.setPosition(3); // Index out of bounds
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }

    @Test
    public void testMixedNamespaceIterator() {
        doc.setXmlNamespaceAware(true);

        Element element = doc.createElement("{http://test.com}element");
        element.setAttribute("{http://test.com}attr1", "value1");
        element.setAttributeNS("", "xmlns:x", "http://other.com");
        element.setAttributeNS("", "x:attr2", "value2");

        root.appendChild(element);

        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(doc, root), new QName("{http://test.com}", "attr1"));
        assertTrue(iterator.hasNext());
        assertEquals("value1", ((Attr) iterator.next()).getNodeValue());
        assertFalse(iterator.hasNext());

        iterator = new DOMAttributeIterator(new DOMPoint(doc, root), new QName("", "xmlns:x"));
        assertTrue(iterator.hasNext());
        assertEquals("http://other.com", ((Attr) iterator.next()).getNodeValue());
        assertFalse(iterator.hasNext());

        iterator = new DOMAttributeIterator(new DOMPoint(doc, root), new QName("", "x:attr2"));
        assertTrue(iterator.hasNext());
        assertEquals("value2", ((Attr) iterator.next()).getNodeValue());
        assertFalse(iterator.hasNext());
    }
}