package org.apache.commons.jxpath.ri.model.dom;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test case for DOMAttributeIterator.
 */
public class DOMAttributeIteratorTest {

    private static final String XML_NS = "http://www.w3.org/2000/xmlns/";
    private static final String TEST_NS = "http://test.com/";
    private static final String TEST_PREFIX = "test";
    private static final String TEST_NAME = new QName(TEST_NS, "testAttr");
    private static final String TEST_NAME_NS = new QName(XML_NS, "xmlns:test");
    private static final String TEST_NAME_LOCAL = "testAttr";

    private Element root;

    @Before
    public void setUp() {
        String xml = "<root xmlns:test=\"http://test.com/\">" +
                "<elem testAttr=\"attr1\" xml:id=\"e1\"/>" +
                "<elem testAttr=\"attr2\" xmlns:test=\"http://test.com/\"/>" +
                "</root>";
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        root = builder.parseMemoryDocument(new org.w3c.dom.DocumentBuilderFactory() {
            @Override
            public Document createDocument(String namespaceURI, String qualifiedName, DocumentType doctype) {
                return builder.newDocument();
            }
        }.newDocumentBuilder().parseString(xml));
    }

    @After
    public void tearDown() {
        root = null;
    }

    @Test
    public void testEmptyIterator() {
        NodePointer nodePointer = new DOMPointNodePointer(root, root);
        DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, null);
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }

    @Test
    public void testIteratorWithOneAttribute() {
        NodePointer nodePointer = new DOMPointNodePointer(root, root.getElementByName("elem"));
        DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, TEST_NAME);
        assertTrue(iterator.hasNext());
        Attr attr = iterator.next();
        assertEquals("attr1", attr.getValue());
        assertEquals("attr1", iterator.next().getValue());
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }

    @Test
    public void testIteratorWithNamespacePrefix() {
        NodePointer nodePointer = new DOMPointNodePointer(root, root.getDocumentElement());
        DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, TEST_NAME_NS);
        assertTrue(iterator.hasNext());
        Attr attr = iterator.next();
        assertEquals("test", attr.getNamespaceURI());
        assertEquals("testAttr", attr.getLocalName());
        assertEquals("http://test.com/", attr.getValue());
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }

    @Test
    public void testIteratorWithNamespaceURI() {
        NodePointer nodePointer = new DOMPointNodePointer(root, root.getDocumentElement());
        DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, TEST_NAME);
        assertTrue(iterator.hasNext());
        Attr attr = iterator.next();
        assertNull(attr.getNamespaceURI());
        assertEquals("testAttr", attr.getLocalName());
        assertEquals("testAttr", iterator.next().getValue());
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }

    @Test
    public void testIteratorWithWildcardName() {
        NodePointer nodePointer = new DOMPointNodePointer(root, root.getDocumentElement());
        DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, new QName("*"));
        List<Attr> attributes = new ArrayList<>();
        while (iterator.hasNext()) {
            Attr attr = iterator.next();
            attributes.add(attr);
        }
        assertEquals(5, attributes.size());
        for (Attr attr : attributes) {
            if (attr.getLocalName().equals("testAttr")) {
                assertTrue(attr.hasAttributeNodes());
                List<Attr> attrValue = new ArrayList<>();
                for (Attr a : attr.getAttributeNodes()) {
                    attrValue.add(a);
                }
                assertEquals(2, attrValue.size());
                assertEquals("http://test.com/", attrValue.get(0).getNamespaceURI());
                assertEquals("testAttr", attrValue.get(0).getLocalName());
                assertEquals("attr1", attrValue.get(0).getValue());
                assertEquals("http://test.com/", attrValue.get(1).getNamespaceURI());
                assertEquals("testAttr", attrValue.get(1).getLocalName());
                assertEquals("attr2", attrValue.get(1).getValue());
                break;
            }
        }
    }
}