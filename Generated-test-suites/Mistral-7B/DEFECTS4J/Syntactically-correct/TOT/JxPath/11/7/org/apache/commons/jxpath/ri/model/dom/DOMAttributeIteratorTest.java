package org.apache.commons.jxpath.ri.model.dom;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import static org.mockito.Mockito.*;

public class DOMAttributeIteratorTest {

    @Test
    public void testEmptyElement() {
        Element emptyElement = mock(Element.class);
        NodePointer parentPointer = mock(NodePointer.class);
        QName name = new QName("*", null, null);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parentPointer, name);

        Assert.assertNull(iterator.getNodePointer());
    }

    @Test
    public void testSingleAttribute() {
        Element element = mock(Element.class);
        NodePointer parentPointer = mock(NodePointer.class);
        QName name = new QName("attributeName", null, null);
        Attr attribute = mock(Attr.class);
        when(element.getAttributeNodeNS(anyString(), name.getName())).thenReturn(attribute);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parentPointer, name);

        NodePointer attributePointer = iterator.getNodePointer();
        Assert.assertEquals(attribute, attributePointer.getNode());
        Assert.assertEquals(attribute, iterator.getNodePointer().getNode());
        verify(element).getAttributeNodeNS(eq("namespaceURI"), eq(name));
    }

    @Test
    public void testSingleAttributeWithPrefix() {
        Element element = mock(Element.class);
        NodePointer parentPointer = mock(NodePointer.class);
        String namespaceURI = "http://test.com";
        QName name = new QName("prefix:attributeName", namespaceURI, null);
        Attr attribute = mock(Attr.class);
        when(element.getAttributeNodeNS(eq("http://test.com"), eq("prefix:attributeName"))).thenReturn(attribute);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parentPointer, name);

        NodePointer attributePointer = iterator.getNodePointer();
        Assert.assertEquals(attribute, attributePointer.getNode());
        Assert.assertEquals(attribute, iterator.getNodePointer().getNode());
        verify(element).getAttributeNodeNS(eq("http://test.com"), eq("prefix:attributeName"));
    }

    @Test
    public void testMultipleAttributes() {
        Element element = mock(Element.class);
        NodePointer parentPointer = mock(NodePointer.class);
        QName name = new QName("*", null, null);
        Attr attribute1 = mock(Attr.class);
        Attr attribute2 = mock(Attr.class);
        when(element.getAttributes()).thenReturn(mock(NamedNodeMap.class));
        when(element.getAttributes().getLength()).thenReturn(2);
        when(element.getAttributes().item(0)).thenReturn(attribute1);
        when(element.getAttributes().item(1)).thenReturn(attribute2);

        DOMAttributeIterator iterator = new DOMAttributeIterator(parentPointer, name);

        NodePointer attributePointer1 = iterator.getNodePointer();
        Assert.assertEquals(attribute1, attributePointer1.getNode());
        Assert.assertEquals(attribute1, iterator.getNodePointer().getNode());

        NodePointer attributePointer2 = iterator.getNodePointer();
        Assert.assertEquals(attribute2, attributePointer2.getNode());
        Assert.assertEquals(attribute2, iterator.getNodePointer().getNode());

        verify(element, times(2)).getAttributes();
    }

    @Test
    public void testMultipleAttributesWithPrefix() {
        Element element = mock(Element.class);
        NodePointer parentPointer = mock(NodePointer.class);
        String namespaceURI = "http://test.com";
        QName name = new QName("prefix:attributeName", namespaceURI, null);
        Attr attribute1 = mock(Attr.class);
        Attr attribute2 = mock(Attr.class);
        when(element.getAttributes()).thenReturn(mock(NamedNodeMap.class));
        when(element.getAttributes().getLength()).thenReturn(2);
        when(element.getAttributes().item(0)).thenReturn(attribute1);
        when(element.getAttributes().item(1)).thenReturn(attribute2);

        DOMAttributeIterator iterator = new DOMAttributeIterator(parentPointer, name);

        NodePointer attributePointer1 = iterator.getNodePointer();
        Assert.assertEquals(attribute1, attributePointer1.getNode());
        Assert.assertEquals(attribute1, iterator.getNodePointer().getNode());

        NodePointer attributePointer2 = iterator.getNodePointer();
        Assert.assertEquals(attribute2, attributePointer2.getNode());
        Assert.assertEquals(attribute2, iterator.getNodePointer().getNode());

        verify(element, times(2)).getAttributes();
    }

    @Test
    public void testIteratorPosition() {
        Element element = mock(Element.class);
        NodePointer parentPointer = mock(NodePointer.class);
        QName name = new QName("*", null, null);
        Attr attribute1 = mock(Attr.class);
        Attr attribute2 = mock(Attr.class);
        when(element.getAttributes()).thenReturn(mock(NamedNodeMap.class));
        when(element.getAttributes().getLength()).thenReturn(2);
        when(element.getAttributes().item(0)).thenReturn(attribute1);
        when(element.getAttributes().item(1)).thenReturn(attribute2);

        DOMAttributeIterator iterator = new DOMAttributeIterator(parentPointer, name);

        NodePointer attributePointer1 = iterator.getNodePointer();
        NodePointer attributePointer2 = iterator.getNodePointer();

        Assert.assertEquals(attribute1, iterator.getNodePointer().getNode());
        Assert.assertEquals(attribute1, iterator.getNodePointer().getNode());
        Assert.assertTrue(iterator.setPosition(1));
        Assert.assertEquals(attribute2, iterator.getNodePointer().getNode());
        Assert.assertEquals(attribute2, iterator.getNodePointer().getNode());
        Assert.assertFalse(iterator.setPosition(3));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIteratorRemoveAttribute() {
        Element element = mock(Element.class);
        NodePointer parentPointer = mock(NodePointer.class);
        QName name = new QName("*", null, null);
        Attr attribute = mock(Attr.class);
        when(element.getAttributeNodeNS(anyString(), name.getName())).thenReturn(attribute);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parentPointer, name);
        iterator.remove();
    }
}