package org.apache.commons.jxpath.ri.model.dom;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.NodePointer;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;

import static org.junit.Assert.*;

public class DOMAttributeIteratorTest {

    private Element root;
    private Document doc;

    @Before
    public void setUp() {
        doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        root = doc.createElement("root");
        root.setAttribute("attr1", "value1");
        root.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
        root.setAttribute("attr2", "value2");
        root.setAttribute("xmlns:xml", "http://www.w3.org/XML/1998/namespace");
        root.setAttribute("attr3", "value3");
        root.setAttribute("attr4", "value4");
        root.setAttribute("attr5", "");
        root.setAttribute("attr6", " value6");
        root.setAttribute("attr7", "\n value7");
        root.setAttribute("attr8", " value8\n");
        root.setAttribute("attr9", "\n value9\n");
        root.setAttribute("attr10", " value10\n value11");
        doc.appendChild(root);
    }

    @Test
    public void testSimpleAttribute() {
        NodeIterator iterator = new DOMAttributeIterator(new DOMPointer(doc, root), new QName("attr1"));
        assertNotNull(iterator);
        NodePointer nodePointer = iterator.next();
        assertNotNull(nodePointer);
        Attr attr = (Attr) nodePointer.getNode();
        assertEquals("attr1", attr.getNodeName());
        assertEquals("value1", attr.getValue());
    }

    @Test
    public void testNamespaceAttribute() {
        NodeIterator iterator = new DOMAttributeIterator(new DOMPointer(doc, root), new QName("xmlns:xsi"));
        assertNotNull(iterator);
        NodePointer nodePointer = iterator.next();
        assertNotNull(nodePointer);
        Attr attr = (Attr) nodePointer.getNode();
        assertEquals("xmlns:xsi", attr.getNodeName());
        assertEquals("http://www.w3.org/2001/XMLSchema-instance", attr.getValue());
    }

    @Test
    public void testEmptyAttribute() {
        NodeIterator iterator = new DOMAttributeIterator(new DOMPointer(doc, root), new QName("attr5"));
        assertNotNull(iterator);
        NodePointer nodePointer = iterator.next();
        assertNotNull(nodePointer);
        Attr attr = (Attr) nodePointer.getNode();
        assertEquals("attr5", attr.getNodeName());
        assertEquals("", attr.getValue());
    }

    @Test
    public void testAttributeWithWhiteSpace() {
        NodeIterator iterator = new DOMAttributeIterator(new DOMPointer(doc, root), new QName("attr6"));
        assertNotNull(iterator);
        NodePointer nodePointer = iterator.next();
        assertNotNull(nodePointer);
        Attr attr = (Attr) nodePointer.getNode();
        assertEquals("attr6", attr.getNodeName());
        assertEquals(" value6", attr.getValue());
    }

    @Test
    public void testAttributeWithNewLines() {
        NodeIterator iterator = new DOMAttributeIterator(new DOMPointer(doc, root), new QName("attr8"));
        assertNotNull(iterator);
        NodePointer nodePointer = iterator.next();
        assertNotNull(nodePointer);
        Attr attr = (Attr) nodePointer.getNode();
        assertEquals("attr8", attr.getNodeName());
        assertEquals(" value8\n", attr.getValue());
    }

    @Test
    public void testAttributeWithMultipleValues() {
        NodeIterator iterator = new DOMAttrributeIterator(new DOMPointer(doc, root), new QName("attr10"));
        assertNotNull(iterator);
        NodePointer nodePointer1 = iterator.next();
        NodePointer nodePointer2 = iterator.next();
        assertNotNull(nodePointer1);
        assertNotNull(nodePointer2);
        Attr attr1 = (Attr) nodePointer1.getNode();
        Attr attr2 = (Attr) nodePointer2.getNode();
        assertEquals("attr10", attr1.getNodeName());
        assertEquals(" value10\n value11", attr1.getValue());
        assertEquals("attr10", attr2.getNodeName());
        assertEquals(" value11", attr2.getValue());
    }

    @Test(expected = NoSuchElementException.class)
    public void testGetNextAfterLast() {
        NodeIterator iterator = new DOMAttributeIterator(new DOMPointer(doc, root), new QName("attr1"));
        NodePointer nodePointer = iterator.next();
        nodePointer = iterator.next();
        nodePointer = iterator.next();
        iterator.next(); // consume the last one
        iterator.next(); // should throw an exception
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullQName() {
        new DOMAttributeIterator(new DOMPointer(doc, root), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullNodePointer() {
        new DOMAttributeIterator(null, new QName("attr1"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullParent() {
        new DOMAttributeIterator(new DOMPointer(null, root), new QName("attr1"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullRoot() {
        new DOMAttributeIterator(new DOMPointer(doc, null), new QName("attr1"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidNodeType() {
        NodePointer nodePointer = new DOMPointer(doc, new Text("test"));
        new DOMAttributeIterator(nodePointer, new QName("attr1"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidNodeType2() {
        NodePointer nodePointer = new DOMPointer(doc, new ProcessingInstruction("xml", ""));
        new DOMAttributeIterator(nodePointer, new QName("attr1"));
    }
}