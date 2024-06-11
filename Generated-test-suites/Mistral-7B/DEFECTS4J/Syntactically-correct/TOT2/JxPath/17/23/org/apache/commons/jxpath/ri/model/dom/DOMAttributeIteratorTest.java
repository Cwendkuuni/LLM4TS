package org.apache.commons.jxpath.ri.model.dom;

import org.apache.commons.jxpath.ri.model.DOMAttribute;
import org.apache.commons.jxpath.ri.model.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.DOMPointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.dom.DOMDocumentFactory;
import org.apache.commons.jxpath.ri.model.dom.DOMElement;
import org.apache.commons.jxpath.ri.model.dom.DOMNode;
import org.apache.commons.jxpath.ri.QName;
import org.junit.Assert;
import org.junit.Test;

public class DOMAttributeIteratorTest {

    @Test
    public void testDOMAttributeIteratorConstruction() {
        DOMDocumentFactory factory = new DOMDocumentFactory();
        DOMElement root = factory.createDocument("", "", null);
        NodePointer parent = new DOMPointer(root);
        QName name = new QName("*", "test:prefix", "test:namespace");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        Assert.assertNotNull(iterator);
    }

    @Test
    public void testTestAttr() {
        DOMDocumentFactory factory = new DOMDocumentFactory();
        DOMElement root = factory.createDocument("", "", null);
        NodePointer parent = new DOMPointer(root);
        QName name = new QName("test:prefix", "test:localName", "test:namespace");
        DOMAttribute attr = factory.createAttribute("xmlns:test", "test");
        root.setAttributeNode(attr);

        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        Assert.assertFalse(iterator.testAttr(attr));

        attr = factory.createAttribute("test:localName", "test", null);
        root.setAttributeNodeNS(attr, "test:namespace");

        iterator = new DOMAttributeIterator(parent, name);
        Assert.assertFalse(iterator.testAttr(attr));

        attr = factory.createAttribute("test:prefix:test:localName", "test", null);
        root.setAttributeNodeNS(attr, "test:namespace");

        iterator = new DOMAttributeIterator(parent, name);
        Assert.assertTrue(iterator.testAttr(attr));
    }

    @Test
    public void testPosition() {
        DOMDocumentFactory factory = new DOMDocumentFactory();
        DOMElement root = factory.createDocument("", "", null);
        NodePointer parent = new DOMPointer(root);
        QName name = new QName("*", "test:prefix", "test:namespace");

        // Test initial position
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        Assert.assertNotNull(iterator.getNodePointer());
        Assert.assertEquals(0, iterator.getPosition());
        Assert.assertFalse(iterator.setPosition(1));

        // Test moving to a valid position
        DOMAttribute attr1 = factory.createAttribute("test:localName1", "value1", null);
        root.setAttributeNodeNS(attr1, "test:namespace");
        DOMAttribute attr2 = factory.createAttribute("test:localName2", "value2", null);
        root.setAttributeNodeNS(attr2, "test:namespace");

        iterator = new DOMAttributeIterator(parent, name);
        iterator.next();
        iterator.next();
        Assert.assertEquals(2, iterator.getPosition());
        Assert.assertTrue(iterator.setPosition(1));
        Assert.assertEquals(1, iterator.getPosition());
        Assert.assertNotNull(iterator.getNodePointer());

        // Test moving to an invalid position
        Assert.assertFalse(iterator.setPosition(3));
        Assert.assertEquals(1, iterator.getPosition());
    }
}