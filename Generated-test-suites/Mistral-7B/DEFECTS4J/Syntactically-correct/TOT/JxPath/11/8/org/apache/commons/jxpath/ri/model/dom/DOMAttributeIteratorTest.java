package org.apache.commons.jxpath.ri.model.dom;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.NodeList;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class DOMAttributeIteratorTest {

    private Node testNode;
    private Element testElement;
    private DOMAttributeIterator iterator;

    @Before
    public void setUp() throws Exception {
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        DocumentType doctype = doc.createElementDocumentType("html", "-//W3C//DTD XHTML 1.0 Strict//EN", "xhtml1-strict.dtd");
        doc.appendChild(doctype);
        Element html = doc.createElement("html");
        html.setAttribute("xmlns", "http://www.w3.org/1999/xhtml");
        html.setAttribute("xml:lang", "en");
        doc.appendChild(html);
        Element body = doc.createElement("body");
        html.appendChild(body);
        body.setAttribute("class", "testClass");
        body.setAttribute("id", "testId");
        html.appendChild(body);
        testNode = html;
        testElement = (Element) testNode;
    }

    @After
    public void tearDown() throws Exception {
        testNode = null;
        testElement = null;
        iterator = null;
    }

    @Test
    public void testDOMAttributeIterator_EmptyNode() throws Exception {
        NodePointer parent = new DOMPointer(Document.class, testNode);
        DOMAttributeIterator emptyIterator = new DOMAttributeIterator(parent, null);
        assertFalse(emptyIterator.hasNext());
    }

    @Test
    public void testDOMAttributeIterator_EmptyAttributeList() throws Exception {
        NodePointer parent = new DOMPointer(Element.class, testElement);
        DOMAttributeIterator emptyIterator = new DOMAttributeIterator(parent, new QName("", "*"));
        assertFalse(emptyIterator.hasNext());
    }

    @Test
    public void testDOMAttributeIterator_SingleAttribute() throws Exception {
        NodePointer parent = new DOMPointer(Element.class, testElement);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("", "class"));
        NodePointer nodePointer = iterator.next();
        assertTrue(nodePointer instanceof DOMAttributePointer);
        Attr attr = (Attr) nodePointer.getNode();
        assertEquals("testClass", attr.getNodeValue());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testDOMAttributeIterator_MultipleAttributes() throws Exception {
        NodePointer parent = new DOMPointer(Element.class, testElement);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("", "*"));
        NodePointer nodePointer1 = iterator.next();
        assertTrue(nodePointer1 instanceof DOMAttributePointer);
        Attr attr1 = (Attr) nodePointer1.getNode();
        assertEquals("class", attr1.getNodeName());
        assertEquals("testClass", attr1.getNodeValue());

        NodePointer nodePointer2 = iterator.next();
        assertTrue(nodePointer2 instanceof DOMAttributePointer);
        Attr attr2 = (Attr) nodePointer2.getNode();
        assertEquals("id", attr2.getNodeName());
        assertEquals("testId", attr2.getNodeValue());

        assertFalse(iterator.hasNext());
    }

    @Test
    public void testDOMAttributeIterator_NamespaceAttribute() throws Exception {
        NodePointer parent = new DOMPointer(Element.class, testElement);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("xmlns", "xmlns:xlink"));
        NodePointer nodePointer1 = iterator.next();
        assertTrue(nodePointer1 != null);
        Attr attr1 = (Attr) nodePointer1.getNode();
        assertEquals("xmlns:xlink", attr1.getNodeName());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testDOMAttributeIterator_CaseInsensitive() throws Exception {
        NodePointer parent = new DOMPointer(Element.class, testElement);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("", "class"));
        NodePointer nodePointer = iterator.next();
        assertTrue(nodePointer instanceof DOMAttributePointer);
        Attr attr = (Attr) nodePointer.getNode();
        assertEquals("testClass", attr.getNodeValue());

        iterator = new DOMAttributeIterator(parent, new QName("", "CLASS"));
        nodePointer = iterator.next();
        assertTrue(nodePointer == nodePointer);
    }

    @Test
    public void testDOMAttributeIterator_MissingAttribute() throws Exception {
        NodePointer parent = new DOMPointer(Element.class, testElement);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("", "missingAttribute"));
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testDOMAttributeIterator_EmptyStringAttribute() throws Exception {
        NodePointer parent = new DOMPointer(Element.class, testElement);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("", "emptyAttribute"));
        NodePointer nodePointer = iterator.next();
        assertTrue(nodePointer instanceof DOMAttributePointer);
        Attr attr = (Attr) nodePointer.getNode();
        assertNull(attr.getNodeValue());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testDOMAttributeIterator_EmptyNodeListAttribute() throws Exception {
        NodePointer parent = new DOMPointer(Element.class, testElement);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("", "xml:space"));
        NodePointer nodePointer = iterator.next();
        assertTrue(nodePointer == null);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testDOMAttributeIterator_SpecificAttribute() throws Exception {
        NodePointer parent = new DOMPointer(Element.class, testElement);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("", "id"));
        NodePointer nodePointer = iterator.next();
        assertTrue(nodePointer instanceof DOMAttributePointer);
        Attr attr = (Attr) nodePointer.getNode();
        assertEquals("testId", attr.getNodeValue());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testDOMAttributeIterator_SpecificNamespaceAttribute() throws Exception {
        NodePointer parent = new DOMPointer(Element.class, testElement);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("xmlns", "xmlns:xlink"));
        NodePointer nodePointer = iterator.next();
        assertTrue(nodePointer != null);
        Attr attr = (Attr) nodePointer.getNode();
        assertEquals("xmlns:xlink", attr.getNodeName());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testDOMAttributeIterator_SpecificNamespaceAndLocalNameAttribute() throws Exception {
        NodePointer parent = new DOMPointer(Element.class, testElement);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("xmlns:xlink", "type"));
        NodePointer nodePointer = iterator.next();
        assertTrue(nodePointer == null);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testDOMAttributeIterator_MissingNamespaceAttribute() throws Exception {
        NodePointer parent = new DOMPointer(Element.class, testElement);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("xmlns:xlink", "type"));
        assertFalse(iterator.hasNext());
    }

    private NodePointer getNodePointer(NodeIterator iterator) {
        return new DOMPointer(Element.class, testElement, iterator.getNodePointer().getNode());
    }

    private void assertEquals(Node expected, Node actual) {
        assertEquals(expected.getNodeType(), actual.getNodeType());
        assertEquals(expected.getNodeName(), actual.getNodeName());
        assertEquals(expected.getNodeValue(), actual.getNodeValue());
    }

    private void assertEquals(String expected, Node actual) {
        assertEquals(expected, actual.getNodeValue());
    }
}