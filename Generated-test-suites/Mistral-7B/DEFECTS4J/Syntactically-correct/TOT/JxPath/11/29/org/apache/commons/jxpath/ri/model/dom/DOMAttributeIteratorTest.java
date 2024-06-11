package org.apache.commons.jxpath.ri.model.dom;

import org.apache.commons.jxpath.ri.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeIterator;

/**
 * Tests for {@link DOMAttributeIterator}.
 */
public class DOMAttributeIteratorTest {

    /**
     * Tests constructor when parent is a {@link Element} node and {@code name} is an empty QName.
     */
    @Test
    public void testEmptyQName() {
        Element element = new Element("test");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(element), new QName());
        NodePointer nodePointer = iterator.getNodePointer();
        Assert.assertNull(nodePointer);
        Assert.assertEquals(0, iterator.getPosition());
    }

    /**
     * Tests constructor when parent is a {@link Element} node and {@code name} is a non-empty QName with a prefix and namespace.
     */
    @Test
    public void testWithPrefixAndNamespace() {
        Element element = new Element("root");
        element.setAttributeNS("xmlns:x", "xmlns:x", "http://example.com/x");
        element.setAttributeNS("x:name", "x:name", "value");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(element), new QName("xmlns:x", "name"));
        NodePointer nodePointer = iterator.getNodePointer();
        Assert.assertNotNull(nodePointer);
        Assert.assertEquals("value", nodePointer.getNodeValue());
        Assert.assertEquals(1, iterator.getPosition());

        nodePointer = iterator.getNodePointer();
        Assert.assertNull(nodePointer);
        Assert.assertEquals(1, iterator.getPosition());
    }

    /**
     * Tests constructor when parent is a {@link Element} node and {@code name} is a non-empty QName with no prefix.
     */
    @Test
    public void testWithoutPrefix() {
        Element element = new Element("root");
        element.setAttribute("name", "value");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(element), new QName("name"));
        NodePointer nodePointer = iterator.getNodePointer();
        Assert.assertNotNull(nodePointer);
        Assert.assertEquals("value", nodePointer.getNodeValue());
        Assert.assertEquals(1, iterator.getPosition());

        nodePointer = iterator.getNodePointer();
        Assert.assertNull(nodePointer);
        Assert.assertEquals(1, iterator.getPosition());
    }

    /**
     * Tests constructor when parent is a {@link Element} node and {@code name} is a wildcard QName (*).
     */
    @Test
    public void testWithWildcardQName() {
        Element element = new Element("root");
        element.setAttribute("name1", "value1");
        element.setAttribute("name2", "value2");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(element), new QName("*"));
        NodePointer nodePointer = iterator.getNodePointer();
        Assert.assertNotNull(nodePointer);
        Assert.assertEquals("value1", nodePointer.getNodeValue());
        Assert.assertEquals(1, iterator.getPosition());

        nodePointer = iterator.getNodePointer();
        Assert.assertNotNull(nodePointer);
        Assert.assertEquals("value2", nodePointer.getNodeValue());
        Assert.assertEquals(2, iterator.getPosition());

        nodePointer = iterator.getNodePointer();
        Assert.assertNull(nodePointer);
        Assert.assertEquals(2, iterator.getPosition());
    }

    /**
     * Tests {@link DOMAttributeIterator#getNodePointer()} and {@link DOMAttributeIterator#getPosition()} when {@code parent} is a {@link Element} node and {@code name} is a non-empty QName.
     */
    @Test
    public void testGetNodePointerAndPosition() {
        Element element = new Element("root");
        element.setAttribute("name1", "value1");
        element.setAttribute("name2", "value2");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(element), new QName("name1"));

        NodePointer nodePointer = iterator.getNodePointer();
        Assert.assertNotNull(nodePointer);
        Assert.assertEquals("value1", nodePointer.getNodeValue());
        Assert.assertEquals(1, iterator.getPosition());

        nodePointer = iterator.getNodePointer();
        Assert.assertNotNull(nodePointer);
        Assert.assertEquals("value2", nodePointer.getNodeValue());
        Assert.assertEquals(2, iterator.getPosition());

        nodePointer = iterator.getNodePointer();
        Assert.assertNull(nodePointer);
        Assert.assertEquals(2, iterator.getPosition());
    }

    /**
     * Tests {@link DOMAttributeIterator#getNodePointer()} and {@link DOMAttributeIterator#getPosition()} when {@code parent} is a {@link Element} node and {@code name} is a wildcard QName (*).
     */
    @Test
    public void testGetNodePointerAndPositionWithWildcardQName() {
        Element element = new Element("root");
        element.setAttribute("name1", "value1");
        element.setAttribute("name2", "value2");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(element), new QName("*"));

        NodePointer nodePointer = iterator.getNodePointer();
        Assert.assertNotNull(nodePointer);
        Assert.assertEquals("value1", nodePointer.getNodeValue());
        Assert.assertEquals(1, iterator.getPosition());

        nodePointer = iterator.getNodePointer();
        Assert.assertNotNull(nodePointer);
        Assert.assertEquals("value2", nodePointer.getNodeValue());
        Assert.assertEquals(2, iterator.getPosition());

        nodePointer = iterator.getNodePointer();
        Assert.assertNull(nodePointer);
        Assert.assertEquals(2, iterator.getPosition());
    }

    /**
     * Tests {@link DOMAttributeIterator#getNodePointer()} and {@link DOMAttributeIterator#getPosition()} when {@code parent} is a {@link Element} node and {@code name} is an empty QName.
     */
    @Test
    public void testGetNodePointerAndPositionWithEmptyQName() {
        Element element = new Element("root");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(element), new QName());

        NodePointer nodePointer = iterator.getNodePointer();
        Assert.assertNull(nodePointer);
        Assert.assertEquals(0, iterator.getPosition());

        nodePointer = iterator.getNodePointer();
        Assert.assertNull(nodePointer);
        Assert.assertEquals(0, iterator.getPosition());
    }

    /**
     * Tests {@link DOMAttributeIterator#getNodePointer()} and {@link DOMAttributeIterator#getPosition()} when {@code parent} is a {@code null} node and {@code name} is a non-empty QName.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetNodePointerAndPositionWithNullParent() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(null, new QName("name"));
    }

    /**
     * Tests {@link DOMAttributeIterator#getNodePointer()} and {@link DOMAttributeIterator#getPosition()} when {@code parent} is a {@code null} node and {@code name} is a wildcard QName (*).
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetNodePointerAndPositionWithNullParentAndWildcardQName() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(null, new QName("*"));
    }

    /**
     * Tests {@link DOMAttributeIterator#getNodePointer()} and {@link DOMAttributeIterator#getPosition()} when {@code parent} is a {@link Node} other than an {@link Element} node and {@code name} is a non-empty QName.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetNodePointerAndPositionWithNonElementParent() {
        Node node = new Element("root");
        node.setNodeValue("root");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(node), new QName("name"));
    }

    /**
     * Tests {@link DOMAttributeIterator#getNodePointer()} and {@link DOMAttributeIterator#getPosition()} when {@code parent} is a {@link Node} other than an {@link Element} node and {@code name} is a wildcard QName (*).
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetNodePointerAndPositionWithNonElementParentAndWildcardQName() {
        Node node = new Element("root");
        node.setNodeValue("root");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(node), new QName("*"));
    }

    /**
     * Tests {@link DOMAttributeIterator#getNodeIterator()} when {@code parent} is a {@link Element} node and {@code name} is a non-empty QName.
     */
    @Test
    public void testNodeIteratorWithNonEmptyQName() {
        Element element = new Element("root");
        element.setAttribute("name1", "value1");
        element.setAttribute("name2", "value2");

        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(element), new QName("name1"));
        NodeIterator nodeIterator = iterator.getNodeIterator();
        NodePointer nodePointer = nodeIterator.getNodePointer();
        Assert.assertNotNull(nodePointer);
        Assert.assertEquals("value1", nodePointer.getNodeValue());
        Assert.assertEquals(1, iterator.getPosition());

        nodePointer = nodeIterator.getNodePointer();
        Assert.assertNotNull(nodePointer);
        Assert.assertEquals("value2", nodePointer.getNodeValue());
        Assert.assertEquals(2, iterator.getPosition());

        nodePointer = nodeIterator.getNodePointer();
        Assert.assertNull(nodePointer);
        Assert.assertEquals(2, iterator.getPosition());

        nodeIterator.nextNode();
        nodePointer = nodeIterator.getNodePointer();
        Assert.assertNull(nodePointer);
        Assert.assertEquals(3, iterator.getPosition());

        Assert.assertFalse(nodeIterator.hasNextNode());
    }

    /**
     * Tests {@link DOMAttributeIterator#getNodeIterator()} when {@code parent} is a {@link Element} node and {@code name} is a wildcard QName (*).
     */
    @Test
    public void testNodeIteratorWithWildcardQName() {
        Element element = new Element("root");
        element.setAttribute("name1", "value1");
        element.setAttribute("name2", "value2");

        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(element), new QName("*"));
        NodeIterator nodeIterator = iterator.getNodeIterator();
        NodePointer nodePointer = nodeIterator.getNodePointer();
        Assert.assertNotNull(nodePointer);
        Assert.assertEquals("value1", nodePointer.getNodeValue());
        Assert.assertEquals(1, iterator.getPosition());

        nodePointer = nodeIterator.getNodePointer();
        Assert.assertNotNull(nodePointer);
        Assert.assertEquals("value2", nodePointer.getNodeValue());
        Assert.assertEquals(2, iterator.getPosition());

        nodePointer = nodeIterator.getNodePointer();
        Assert.assertNull(nodePointer);
        Assert.assertEquals(2, iterator.getPosition());

        nodeIterator.nextNode();
        nodePointer = nodeIterator.getNodePointer();
        Assert.assertNotNull(nodePointer);
        Assert.assertEquals("value1", nodePointer.getNodeValue());
        Assert.assertEquals(3, iterator.getPosition());

        Assert.assertFalse(nodeIterator.hasNextNode());
    }

    /**
     * Tests {@link DOMAttributeIterator#getNodeIterator()} when {@code parent} is a {@code null} node and {@code name} is a non-empty QName.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNodeIteratorWithNullParentAndNonEmptyQName() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(null, new QName("name"));
    }

    /**
     * Tests {@link DOMAttributeIterator#getNodeIterator()} when {@code parent} is a {@code null} node and {@code name} is a wildcard QName (*).
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNodeIteratorWithNullParentAndWildcardQName() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(null, new QName("*"));
    }

    /**
     * Tests {@link DOMAttributeIterator#getNodeIterator()} when {@code parent} is a {@link Node} other than an {@link Element} node and {@code name} is a non-empty QName.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNodeIteratorWithNonElementParentAndNonEmptyQName() {
        Node node = new Element("root");
        node.setNodeValue("root");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(node), new QName("name"));
    }

    /**
     * Tests {@link DOMAttributeIterator#getNodeIterator()} when {@code parent} is a {@link Node} other than an {@link Element} node and {@code name} is a wildcard QName (*).
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNodeIteratorWithNonElementParentAndWildcardQName() {
        Node node = new Element("root");
        node.setNodeValue("root");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(node), new QName("*"));
    }
}