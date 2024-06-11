package org.apache.commons.jxpath.ri.model.dom;

import static org.apache.commons.jxpath.ri.model.dom.DOMAttributeIterator.equalStrings;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Test class for {@link DOMAttributeIterator}
 */
public class DOMAttributeIteratorTest {

    private Element testElement;

    @Before
    public void setUp() {
        String xml = "<root xmlns:x='http://test' xmlns:y='http://test2'><child xmlns:x='http://test' xmlns:y='http://test2' attr1='value1' attr2='value2' attr3='value3'/></root>";
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        Document doc = null;
        try {
            doc = docFactory.newDocumentBuilder().parse(new ByteArrayInputStream(xml.getBytes()));
            testElement = (Element) doc.getDocumentElement().getElementsByTagName("child").item(0);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testConstructorWithNamespace() {
        QName name = new QName("x:attr1");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(testElement), name);
        assertEquals(1, iterator.getPosition());
        assertEquals("value1", iterator.getNodePointer().getStringValue());
    }

    @Test
    public void testConstructorWithoutNamespace() {
        QName name = new QName("attr1");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(testElement), name);
        assertEquals(1, iterator.getPosition());
        assertEquals("value1", iterator.getNodePointer().getStringValue());
    }

    @Test
    public void testConstructorWithWildcardName() {
        QName name = new QName("*");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(testElement), name);
        for (int i = 0; i < 3; i++) {
            assertEquals(i + 1, iterator.getPosition());
            assertTrue(iterator.getNodePointer().getNode().getNodeName().matches(".*attr[0-9]+.*"));
            iterator.next();
        }
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testConstructorWithNameNotMatchingAnyAttribute() {
        QName name = new QName("z:attrNotExist");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(testElement), name);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testNextAndHasNext() {
        QName name = new QName("attr1");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(testElement), name);
        assertEquals(1, iterator.getPosition());
        assertEquals("value1", iterator.getNodePointer().getStringValue());
        iterator.next();
        assertEquals(2, iterator.getPosition());
        assertEquals("value2", iterator.getNodePointer().getStringValue());
        iterator.next();
        assertEquals(3, iterator.getPosition());
        assertEquals("value3", iterator.getNodePointer().getStringValue());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testGetPosition() {
        QName name = new QName("attr1");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(testElement), name);
        assertEquals(1, iterator.getPosition());
        iterator.next();
        assertEquals(2, iterator.getPosition());
        iterator.next();
        assertEquals(3, iterator.getPosition());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetPositionToNegativeNumber() {
        QName name = new QName("attr1");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(testElement), name);
        iterator.setPosition(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetPositionToNumberGreaterThanSize() {
        QName name = new QName("attr1");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(testElement), name);
        iterator.setPosition(4);
    }

    @Test
    public void testSetPositionToValidNumber() {
        QName name = new QName("attr1");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(testElement), name);
        iterator.next();
        iterator.next();
        iterator.setPosition(1);
        assertEquals(2, iterator.getPosition());
    }

    @Test
    public void testGetNodePointer() {
        QName name = new QName("attr1");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(testElement), name);
        assertEquals("value1", iterator.getNodePointer().getStringValue());
        iterator.next();
        assertEquals("value2", iterator.getNodePointer().getStringValue());
        iterator.next();
        assertEquals("value3", iterator.getNodePointer().getStringValue());
    }

    @Test
    public void testGetNodePointerWithEmptyIterator() {
        QName name = new QName("z:notExist");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(testElement), name);
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testEqualStrings() {
        assertTrue(equalStrings("foo", "foo"));
        assertTrue(equalStrings("foo", "Foo"));
        assertFalse(equalStrings("foo", "bar"));
    }
}

class DOMPointer {

    private Node node;

    public DOMPointer(Node node) {
        this.node = node;
    }

    public Node getNode() {
        return node;
    }

    public String getNamespaceURI(String prefix) {
        return node.getNamespaceURI();
    }
}