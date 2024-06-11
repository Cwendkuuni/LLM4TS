package org.apache.commons.jxpath.ri.model.jdom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;
import org.jdom2.Attribute;
import org.jdom2.Element;
import org.jdom2.Namespace;

public class JDOMAttributeIteratorTest {

    @Test
    public void testJDOMAttributeIteratorWithSingleAttribute() {
        Element root = new Element("root");
        root.addAttribute(new Attribute("attr1", "value1"));

        NodePointer nodePointer = new NodePointer(root);
        QName name = new QName("attr1");

        NodeIterator iterator = new JDOMAttributeIterator(nodePointer, name);

        assertEquals(iterator.getNodePointer(), new JDOMAttributePointer(nodePointer, root.getAttribute("attr1")));
        assertEquals(iterator.getPosition(), 0);

        iterator.setPosition(1);
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testJDOMAttributeIteratorWithMultipleAttributes() {
        Element root = new Element("root");
        root.addAttribute(new Attribute("attr1", "value1", "prefix1"));
        root.addAttribute(new Attribute("attr2", "value2", "prefix2"));

        NodePointer nodePointer = new NodePointer(root);
        QName name = new QName("*");

        NodeIterator iterator = new JDOMAttributeIterator(nodePointer, name);

        assertSame(iterator.getNodePointer(), new JDOMAttributePointer(nodePointer, root.getAttribute("attr1", "prefix1")));
        assertEquals(iterator.getPosition(), 0);

        iterator.setPosition(1);
        assertSame(iterator.getNodePointer(), new JDOMAttributePointer(nodePointer, root.getAttribute("attr2", "prefix2")));
        assertEquals(iterator.getPosition(), 1);

        iterator.setPosition(2);
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testJDOMAttributeIteratorWithEmptyAttributesList() {
        Element root = new Element("root");

        NodePointer nodePointer = new NodePointer(root);
        QName name = new QName("nonExistentAttribute");

        NodeIterator iterator = new JDOMAttributeIterator(nodePointer, name);

        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testJDOMAttributeIteratorWithNullParent() {
        NodePointer nodePointer = null;

        // Test initialization with QName
        QName name = new QName("attr1");
        NodeIterator iterator = new JDOMAttributeIterator(nodePointer, name);
        assertNull(iterator.getNodePointer());

        // Test initialization with wildcard QName
        name = new QName("*");
        iterator = new JDOMAttributeIterator(nodePointer, name);
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testJDOMAttributeIteratorWithNullAttributesList() {
        Element root = new Element("root");
        NodePointer nodePointer = new NodePointer(root);

        QName name = new QName("attr1");
        NodeIterator iterator = new JDOMAttributeIterator(nodePointer, name);

        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testJDOMAttributeIteratorWithWrongParentNodeType() {
        Element root = new Element("root");
        NodePointer nodePointer = new NodePointer(root.getChild("child"));

        QName name = new QName("attr1");
        NodeIterator iterator = new JDOMAttributeIterator(nodePointer, name);

        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testJDOMAttributeIteratorWithEmptyElement() {
        Element root = new Element("root");

        NodePointer nodePointer = new NodePointer(root);
        QName name = new QName("attr1");

        NodeIterator iterator = new JDOMAttributeIterator(nodePointer, name);

        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testJDOMAttributeIteratorWithAttributeWithNullValue() {
        Element root = new Element("root");
        root.addAttribute(new Attribute("attr1", null));

        NodePointer nodePointer = new NodePointer(root);
        QName name = new QName("attr1");

        NodeIterator iterator = new JDOMAttributeIterator(nodePointer, name);

        assertSame(iterator.getNodePointer(), new JDOMAttributePointer(nodePointer, root.getAttribute("attr1")));
        assertEquals(iterator.getPosition(), 0);
    }

    @Test
    public void testJDOMAttributeIteratorWithAttributeWithoutNamespace() {
        Element root = new Element("root");
        root.addAttribute(new Attribute("attr1", "value1"));

        NodePointer nodePointer = new NodePointer(root);
        QName name = new QName("attr1");

        NodeIterator iterator = new JDOMAttributeIterator(nodePointer, name);

        assertEquals(iterator.getNodePointer(), new JDOMAttributePointer(nodePointer, root.getAttribute("attr1")));
        assertEquals(iterator.getPosition(), 0);
    }

    @Test
    public void testJDOMAttributeIteratorWithAttributeWithNamespace() {
        Element root = new Element("root");
        root.addNamespace("prefix", "http://example.com/");
        root.addAttribute(new Attribute("prefix:attr1", "value1"));

        NodePointer nodePointer = new NodePointer(root);
        QName name = new QName("prefix:attr1");

        NodeIterator iterator = new JDOMAttributeIterator(nodePointer, name);

        assertEquals(iterator.getNodePointer(), new JDOMAttributePointer(nodePointer, root.getAttribute("attr1")));
        assertEquals(iterator.getPosition(), 0);
    }

    @Test
    public void testJDOMAttributeIteratorWithAttributeWithEmptyNamespace() {
        Element root = new Element("root");
        root.addNamespace("", "http://example.com/");
        root.addAttribute(new Attribute("", "attr1", "http://example.com/", "value1"));

        NodePointer nodePointer = new NodePointer(root);
        QName name = new QName("", "attr1");

        NodeIterator iterator = new JDOMAttributeIterator(nodePointer, name);

        assertEquals(iterator.getNodePointer(), new JDOMAttributePointer(nodePointer, root.getAttribute("attr1")));
        assertEquals(iterator.getPosition(), 0);
    }

    @Test
    public void testJDOMAttributeIteratorWithMultipleAttributesWithSameName() {
        Element root = new Element("root");
        root.addAttribute(new Attribute("attr1", "value1"));
        root.addAttribute(new Attribute("attr1", "value2"));

        NodePointer nodePointer = new NodePointer(root);
        QName name = new QName("attr1");

        NodeIterator iterator = new JDOMAttributeIterator(nodePointer, name);

        List<NodePointer> nodePointers = new ArrayList<>();
        for (int i = 0; i < iterator.size(); i++) {
            nodePointers.add(iterator.getNodePointer());
            iterator.next();
        }

        assertEquals(nodePointers.size(), 2);
        assertSame(nodePointers.get(0), new JDOMAttributePointer(nodePointer, root.getAttribute("attr1", null)));
        assertSame(nodePointers.get(1), new JDOMAttributePointer(nodePointer, root.getAttribute("attr1", null)));
    }

    @Test
    public void testJDOMAttributeIteratorWithMultipleAttributesWithDifferentNames() {
        Element root = new Element("root");
        root.addAttribute(new Attribute("attr1", "value1"));
        root.addAttribute(new Attribute("attr2", "value2"));

        NodePointer nodePointer = new NodePointer(root);
        QName name1 = new QName("attr1");
        QName name2 = new QName("attr2");

        NodeIterator iterator = new JDOMAttributeIterator(nodePointer, name1);

        assertEquals(iterator.getNodePointer(), new JDOMAttributePointer(nodePointer, root.getAttribute("attr1", null)));
        assertEquals(iterator.getPosition(), 0);

        iterator.setPosition(1);
        assertEquals(iterator.getNodePointer(), new JDOMAttributePointer(nodePointer, root.getAttribute("attr2", null)));
        assertEquals(iterator.getPosition(), 1);

        iterator = new JDOMAttributeIterator(nodePointer, name2);

        assertEquals(iterator.getNodePointer(), new JDOMAttributePointer(nodePointer, root.getAttribute("attr2", null)));
        assertEquals(iterator.getPosition(), 0);
    }
}