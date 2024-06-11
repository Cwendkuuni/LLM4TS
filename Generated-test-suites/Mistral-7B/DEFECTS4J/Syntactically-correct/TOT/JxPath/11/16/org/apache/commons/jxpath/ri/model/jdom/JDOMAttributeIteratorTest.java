import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class JDOMAttributeIteratorTest {

    private NodePointer parentNode;
    private JDOMAttributeIterator attributeIterator;

    @Before
    public void setUp() {
        Element root = new Element("root");
        root.addAttribute("attr1", "value1");
        root.addAttribute("attr2", "value2", "xmlns:x");
        root.addAttribute("attr3", "value3", "xmlns:y");
        root.addNamespace("xml", "http://www.w3.org/XML/1998/namespace");

        parentNode = new NodePointer(root);
    }

    @Test
    public void testIteratorEmpty() {
        JDOMAttributeIterator emptyIterator = new JDOMAttributeIterator(parentNode, new QName(""));
        assertNull(emptyIterator.getNodePointer());
    }

    @Test
    public void testIteratorWithOneAttribute() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentNode, new QName("attr1"));
        NodePointer attributeNode = iterator.getNodePointer();
        assertNotNull(attributeNode);
        assertNotNull(attributeNode.getNode());
        assertEquals("value1", attributeNode.getNode().getValue());
    }

    @Test
    public void testIteratorWithMultipleAttributes() {
        JDOMAttribeIterator iterator = new JDOMAttributeIterator(parentNode, new QName("*"));
        NodePointer attributeNode1 = iterator.getNodePointer();
        NodePointer attributeNode2 = iterator.getNodePointer();
        NodePointer attributeNode3 = iterator.getNodePointer();

        assertNotNull(attributeNode1);
        assertNotNull(attributeNode1.getNode());
        assertEquals("attr1", attributeNode1.getNode().getName());
        assertEquals("value1", attributeNode1.getNode().getValue());

        assertNotNull(attributeNode2);
        assertNotNull(attributeNode2.getNode());
        assertEquals("xmlns:x", attributeNode2.getNode().getNamespaceURI());
        assertEquals("attr2", attributeNode2.getNode().getName());
        assertEquals("value2", attributeNode2.getNode().getValue());

        assertNotNull(attributeNode3);
        assertNotNull(attributeNode3.getNode());
        assertEquals("xmlns:y", attributeNode3.getNode().getNamespaceURI());
        assertEquals("attr3", attributeNode3.getNode().getName());
        assertEquals("value3", attributeNode3.getNode().getValue());
    }

    @Test
    public void testIteratorOutOfBounds() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentNode, new QName("*"));

        for (int i = 0; i < iterator.getSize(); i++) {
            NodePointer attributeNode = iterator.getNodePointer();
            assertNotNull(attributeNode);
            iterator.next();
        }

        NodePointer nextNode = iterator.getNodePointer();
        assertNull(nextNode);
    }

    @Test
    public void testIteratorWithNamespaceAndWildcard() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentNode, new QName("xml", "*"));
        NodePointer attributeNode1 = iterator.getNodePointer();
        NodePointer attributeNode2 = iterator.getNodePointer();

        assertNotNull(attributeNode1);
        assertNotNull(attributeNode1.getNode());
        assertEquals("xmlns:x", attributeNode1.getNode().getNamespaceURI());
        assertEquals("attr2", attributeNode1.getNode().getName());
        assertEquals("value2", attributeNode1.getNode().getValue());

        assertNotNull(attributeNode2);
        assertNotNull(attributeNode2.getNode());
        assertEquals("xmlns:y", attributeNode2.getNode().getNamespaceURI());
        assertEquals("attr3", attributeNode2.getNode().getName());
        assertEquals("value3", attributeNode2.getNode().getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIteratorWithNullParent() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(null, new QName("attr1"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIteratorWithNullName() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentNode, null);
    }
}