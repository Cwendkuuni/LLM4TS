import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link JDOMAttributeIterator}.
 */
public class JDOMAttributeIteratorTest {

    private Element testElement;

    @Before
    public void setUp() {
        // Create test element
        Namespace xmlNs = Namespace.getNamespace("xml");
        testElement = new Element("root", "root", xmlNs);
        Attribute attr1 = new Attribute("attr1", "value1", xmlNs);
        attr1.setNamespaceBinding(xmlNs);
        testElement.addAttribute(attr1);
        Attribute attr2 = new Attribute("attr2", "value2", null);
        testElement.addAttribute(attr2);
        Attribute attr3 = new Attribute("attr3", "value3", xmlNs);
        attr3.setNamespaceBinding(xmlNs);
        testElement.addAttribute(attr3);
    }

    @Test
    public void testEmptyIterator() {
        NodePointer parent = new NodePointer(testElement, null);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", "*"));
        assertNotNull(iterator);
        assertEquals(0, iterator.getPosition());
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testSingleAttributeIterator() {
        NodePointer parent = new NodePointer(testElement, null);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("xml", "attr1"));
        assertNotNull(iterator);
        assertEquals(1, iterator.getPosition());
        NodePointer nodePointer = iterator.getNodePointer();
        assertNotNull(nodePointer);
        assertEquals(attr1, nodePointer.getNode());
    }

    @Test
    public void testMultipleAttributesIterator() {
        NodePointer parent = new NodePointer(testElement, null);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("xml", "*"));
        assertNotNull(iterator);
        assertEquals(3, iterator.getPosition());
        NodePointer nodePointer = iterator.getNodePointer();
        assertNotNull(nodePointer);
        assertEquals(attr1, nodePointer.getNode());

        iterator.next();
        nodePointer = iterator.getNodePointer();
        assertNotNull(nodePointer);
        assertEquals(attr2, nodePointer.getNode());

        iterator.next();
        nodePointer = iterator.getNodePointer();
        assertNotNull(nodePointer);
        assertEquals(attr3, nodePointer.getNode());

        iterator.next(); // past end of iterator
        nodePointer = iterator.getNodePointer();
        assertNull(nodePointer.getNode());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testNegativePosition() {
        NodePointer parent = new NodePointer(testElement, null);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("xml", "*"));
        iterator.setPosition(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testPositionGreaterThanSize() {
        NodePointer parent = new NodePointer(testElement, null);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("xml", "*"));
        iterator.setPosition(4);
    }
}