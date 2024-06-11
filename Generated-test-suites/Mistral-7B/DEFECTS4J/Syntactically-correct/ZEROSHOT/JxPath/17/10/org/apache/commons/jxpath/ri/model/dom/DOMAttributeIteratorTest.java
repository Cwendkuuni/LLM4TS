package org.apache.commons.jxpath.ri.model.dom;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests for {@link DOMAttributeIterator} class.
 */
public class DOMAttributeIteratorTest {

    @Test
    public void testCreateIteratorForEmptyElement() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(null), null);
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testCreateIteratorForElementWithEmptyAttributeMap() {
        String xml = "<root></root>";
        DOMPointer pointer = new DOMPointer(XMLUtil.stringToNode(xml));
        DOMAttributeIterator iterator = new DOMAttributeIterator(pointer, null);
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testCreateIteratorForElementWithSingleAttribute() {
        String xml = "<root id='123'></root>";
        DOMPointer pointer = new DOMPointer(XMLUtil.stringToNode(xml));
        DOMAttributeIterator iterator = new DOMAttributeIterator(pointer, new QName("*", "id"));
        Assert.assertTrue(iterator.hasNext());
        NodePointer next = iterator.next();
        Assert.assertEquals("123", next.getNodeValue());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testCreateIteratorForElementWithMultipleAttributes() {
        String xml = "<root id='123' class='class1 class2' xml:lang='en'></root>";
        DOMPointer pointer = new DOMPointer(XMLUtil.stringToNode(xml));
        DOMAttributeIterator iterator = new DOMAttributeIterator(pointer, null);
        int expectedPosition = 0;

        List<QName> names = new ArrayList<>();
        names.add(new QName("", "id"));
        names.add(new QName("", "class"));
        names.add(new QName("xml", "lang"));

        while (iterator.hasNext()) {
            NodePointer next = iterator.next();
            QName expectedName = names.get(expectedPosition++);
            Assert.assertEquals(expectedName.getLocalPart(), next.getNode().getNodeName());
            Assert.assertEquals(expectedName.getPrefix(), next.getNode().getPrefix());
            Assert.assertEquals(next.getNode().getNodeValue(), next.getNodeValue());
        }

        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testCreateIteratorForElementWithSingleAttributeWithPrefix() {
        String xml = "<root xml:id='123'></root>";
        DOMPointer pointer = new DOMPointer(XMLUtil.stringToNode(xml));
        DOMAttributeIterator iterator = new DOMAttributeIterator(pointer, new QName("xml", "id"));
        Assert.assertTrue(iterator.hasNext());
        NodePointer next = iterator.next();
        Assert.assertEquals("123", next.getNodeValue());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateIteratorForNullParent() {
        new DOMAttributeIterator(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateIteratorForNullName() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(null), null);
        iterator.next();
        iterator.setPosition(1);
        new DOMAttributeIterator(iterator, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateIteratorForNegativePosition() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(null), null);
        iterator.next();
        iterator.setPosition(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCreateIteratorForPositionGreaterThanSize() {
        String xml = "<root id='123' class='class1'></root>";
        DOMPointer pointer = new DOMPointer(XMLUtil.stringToNode(xml));
        DOMAttributeIterator iterator = new DOMAttributeIterator(pointer, null);
        iterator.next();
        iterator.setPosition(3);
    }

    private static class DOMPointer extends NodePointer {
        public DOMPointer(Node node) {
            super(node);
        }

        public NodeIterator iterator() {
            return new DOMAttributeIterator(this, null);
        }
    }
}