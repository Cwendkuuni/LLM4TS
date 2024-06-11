package org.apache.commons.jxpath.ri.model.dom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

/**
 * Test class for DOMAttributeIterator
 */
public class DOMAttributeIteratorTest {

    @org.junit.Before
    public void setUp() {
        // Setup some test data
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<root xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">" +
                "<element xmlns:my=\"http://mynamespace\">" +
                "<attribute1 xsi:type=\"int\" xmlns:x=\"http://xnamespace\">Value1</attribute1>" +
                "<attribute2 xmlns:y=\"http://ysnamespace\">Value2</attribute2>" +
                "<attribute3 xmlns=\"\">Value3</attribute3>" +
                "<another-element>" +
                "<attribute1 xmlns:x=\"http://xnamespace\" xmlns:z=\"http://znamespace\">Value4</attribute1>" +
                "</another-element>" +
                "</element>" +
                "</root>";

        Document doc = javax.xml.parsers.DocumentBuilderFactory.newInstance()
                .newDocumentBuilder()
                .parse(new java.io.StringReader(xml));
        root = (Element) doc.getDocumentElement();
    }

    Element root;

    @org.junit.Test
    public void testEmptyIterator() {
        NodePointer parent = new DOMPointNode(root, 1);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("", "*"));
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }

    @org.junit.Test
    public void testEmptyElement() {
        NodePointer parent = new DOMPointNode(root, 0);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("", ""));
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }

    @org.junit.Test
    public void testAttributeByLocalNameAndNamespace() {
        NodePointer parent = new DOMPointNode(root, 0);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("", "attribute1"));
        assertTrue(iterator.hasNext());
        Attr attr = (Attr) iterator.next();
        assertEquals("attribute1", attr.getLocalName());
        assertEquals("http://www.w3.org/2001/XMLSchema-instance", attr.getNamespaceURI());
        assertEquals("Value1", attr.getValue());
    }

    @org.junit.Test
    public void testAttributeByPrefixAndLocalName() {
        NodePointer parent = new DOMPointNode(root, 0);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("x", "attribute1"));
        assertTrue(iterator.hasNext());
        Attr attr = (Attr) iterator.next();
        assertEquals("attribute1", attr.getLocalName());
        assertEquals("http://www.w3.org/2001/XMLSchema-instance", attr.getNamespaceURI());
        assertEquals("Value1", attr.getValue());
    }

    @org.junit.Test
    public void testAttributeByPrefixAndNamespace() {
        NodePointer parent = new DOMPointNode(root, 0);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("xsi", ""));
        List<Node> nodes = new ArrayList<>();
        while (iterator.hasNext()) {
            nodes.add(iterator.next());
        }
        assertEquals(3, nodes.size());
        for (Node node : nodes) {
            if (node instanceof Attr) {
                Attr attr = (Attr) node;
                assertTrue(attr.getNamespaceURI().equals("http://www.w3.org/2001/XMLSchema-instance"));
            }
        }
    }

    @org.junit.Test
    public void testAttributeByQualifiedName() {
        NodePointer parent = new DOMPointNode(root, 0);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("", "attribute1"));
        assertTrue(iterator.hasNext());
        Attr attr = (Attr) iterator.next();
        assertEquals("attribute1", attr.getLocalName());
        assertEquals("http://www.w3.org/2001/XMLSchema-instance", attr.getNamespaceURI());
        assertEquals("Value1", attr.getValue());

        iterator = new DOMAttributeIterator(parent, new QName("my", "attribute2"));
        assertTrue(iterator.hasNext());
        attr = (Attr) iterator.next();
        assertEquals("attribute2", attr.getLocalName());
        assertEquals("http://mysnamespace", attr.getNamespaceURI());
        assertEquals("Value2", attr.getValue());

        iterator = new DOMAttributeIterator(parent, new QName("", "attribute3"));
        assertTrue(iterator.hasNext());
        attr = (Attr) iterator.next();
        assertEquals("attribute3", attr.getLocalName());
        assertNull(attr.getNamespaceURI());
        assertEquals("Value3", attr.getValue());
    }

    @org.junit.Test
    public void testAttributeByWildcardQualifiedName() {
        NodePointer parent = new DOMPointNode(root, 0);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("", "*"));
        List<Node> nodes = new ArrayList<>();
        while (iterator.hasNext()) {
            nodes.add(iterator.next());
        }
        assertEquals(5, nodes.size());
        for (Node node : nodes) {
            if (node instanceof Attr) {
                Attr attr = (Attr) node;
                if (attr.getLocalName().equals("attribute1")) {
                    assertEquals("http://www.w3.org/2001/XMLSchema-instance", attr.getNamespaceURI());
                    assertEquals("Value1", attr.getValue());
                } else if (attr.getLocalName().equals("attribute2")) {
                    assertEquals("http://ysnamespace", attr.getNamespaceURI());
                    assertEquals("Value2", attr.getValue());
                } else if (attr.getLocalName().equals("attribute3")) {
                    assertNull(attr.getNamespaceURI());
                    assertEquals("Value3", attr.getValue());
                } else if (attr.getLocalName().equals("attribute1")) {
                    assertEquals("http://xnamespace", attr.getNamespaceURI());
                    assertEquals("Value4", attr.getValue());
                }
            }
        }
    }

    @org.junit.Test
    public void testGetNodePointer() {
        NodePointer parent = new DOMPointNode(root, 0);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("", "attribute1"));
        NodePointer attributeNodePointer = iterator.next();
        DOMAttributePointer expected = new DOMAttributePointer(parent, (Attr) iterator.next());
        NodePointer actual = iterator.getNodePointer();
        assertEquals(expected, actual);
    }

    @org.junit.Test
    public void testNextAfterEnd() {
        NodePointer parent = new DOMPointNode(root, 0);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("", "*"));
        List<Node> nodes = new ArrayList<>();
        while (iterator.hasNext()) {
            nodes.add(iterator.next());
        }
        iterator.next(); // advance to the end
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }

    @org.junit.Test
    public void testSetPosition() {
        NodePointer parent = new DOMPointNode(root, 0);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("", "*"));
        List<Node> nodes = new ArrayList<>();
        while (iterator.hasNext()) {
            nodes.add(iterator.next());
        }
        iterator.setPosition(1);
        Attr attribute = (Attr) iterator.next();
        assertEquals("attribute1", attribute.getLocalName());
        assertEquals("http://www.w3.org/2001/XMLSchema-instance", attribute.getNamespaceURI());
        assertEquals("Value1", attribute.getValue());
        iterator.setPosition(0);
        attribute = (Attr) iterator.next();
        assertEquals("attribute1", attribute.getLocalName());
        assertEquals("http://www.w3.org/2001/XMLSchema-instance", attribute.getNamespaceURI());
        assertEquals("Value1", attribute.getValue());
        iterator.setPosition(nodes.size());
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }
}

class DOMAttributePointer extends DOMPointNode {

    private Attr attr;

    public DOMAttributePointer(NodePointer parent, Attr attr) {
        super(parent.getNode(), attr);
        this.attr = attr;
    }
}