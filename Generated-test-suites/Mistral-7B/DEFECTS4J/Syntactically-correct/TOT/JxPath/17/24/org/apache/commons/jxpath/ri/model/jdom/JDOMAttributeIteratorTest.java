package org.apache.commons.jxpath.ri.model.jdom.test;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Test case for {@link JDOMAttributeIterator}.
 */
public class JDOMAttributeIteratorTest {

    @Test
    public void testBasicUsage() {
        // Given
        Element root = new Element("root");
        root.addAttribute("attr1", "value1");
        root.addAttribute("attr2", "value2", "xmlns:ns");

        String namespaceUri = "http://example.com";
        root.addNamespaceDeclaration("ns", namespaceUri);

        NodePointer nodePointer = new NodePointer(root);
        NodeIterator nodeIterator = new JDOMAttributeIterator(nodePointer, new QName("ns:attr1"));

        // When
        NodePointer attributePointer = nodeIterator.next();

        // Then
        Attribute attribute = (Attribute) attributePointer.getNode();
        assertEquals("attr1", attribute.getName());
        assertEquals("value1", attribute.getText());
        assertEquals("ns", attribute.getNamespace().getPrefix());
        assertEquals(namespaceUri, attribute.getNamespace().getURI());

        // Cleanup
        nodeIterator.close();
    }

    @Test
    public void testEmptyIterator() {
        // Given
        Element root = new Element("root");

        NodePointer nodePointer = new NodePointer(root);
        NodeIterator nodeIterator = new JDOMAttributeIterator(nodePointer, new QName(":*"));

        // When
        Attribute attribute = (Attribute) nodeIterator.next().getNode();

        // Then
        assertNull(attribute);

        // Cleanup
        nodeIterator.close();
    }

    @Test
    public void testNamespaceUriResolution() {
        // Given
        Element root = new Element("root");
        String namespaceUri1 = "http://example.com/ns1";
        root.addNamespaceDeclaration("ns1", namespaceUri1);

        root.addAttribute("attr1", "value1", "xmlns:ns2");
        root.addAttribute("attr2", "value2", "ns1:ns");

        NodePointer nodePointer = new NodePointer(root);
        NodeIterator nodeIterator = new JDOMAttributeIterator(nodePointer, new QName("ns1:ns:attr2"));

        // When
        NodePointer attributePointer = nodeIterator.next();

        // Then
        Attribute attribute = (Attribute) attributePointer.getNode();
        assertEquals("attr2", attribute.getName());
        assertEquals("value2", attribute.getText());
        assertEquals("ns", attribute.getNamespace().getPrefix());
        assertEquals(namespaceUri1, attribute.getNamespace().getURI());

        // Cleanup
        nodeIterator.close();
    }

    @Test(expected = IllegalStateException.class)
    public void testIteratingAfterEnd() {
        // Given
        Element root = new Element("root");
        root.addAttribute("attr1", "value1");

        NodePointer nodePointer = new NodePointer(root);
        NodeIterator nodeIterator = new JDOMAttributeIterator(nodePointer, new QName("*:attr1"));

        // When
        nodeIterator.next();
        nodeIterator.next();

        // Then (should throw an IllegalStateException)
        nodeIterator.next();
    }

    @Test
    public void testCloseIterator() {
        // Given
        Element root = new Element("root");
        root.addAttribute("attr1", "value1");

        NodePointer nodePointer = new NodePointer(root);
        NodeIterator nodeIterator = new JDOMAttributeIterator(nodePointer, new QName("*:attr1"));

        // When
        NodePointer attributePointer = nodeIterator.next();

        // Then
        Attribute attribute = (Attribute) attributePointer.getNode();
        assertEquals("attr1", attribute.getName());
        assertEquals("value1", attribute.getText());

        // When closing the iterator
        nodeIterator.close();

        // Then the iterator is closed - next call should return null
        Attribute attributeAfterClose = (Attribute) nodeIterator.next().getNode();
        assertNull(attributeAfterClose);
    }
}