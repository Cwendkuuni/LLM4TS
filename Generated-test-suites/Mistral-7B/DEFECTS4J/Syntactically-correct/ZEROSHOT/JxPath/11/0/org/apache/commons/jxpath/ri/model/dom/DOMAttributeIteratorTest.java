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

import static org.apache.commons.jxpath.ri.model.DOMAttributePointer.createDOMAttributePointer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DOMAttributeIteratorTest {

    @Test
    public void testDOMAttributeIterator_Empty() {
        NodePointer parentMock = mock(NodePointer.class);
        QName name = new QName("test");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parentMock, name);

        Assert.assertFalse(iterator.hasNext());
        Assert.assertNull(iterator.next());
    }

    @Test
    public void testDOMAttributeIterator_SingleAttribute_SameNamespace() {
        NodePointer parentMock = mock(NodePointer.class);
        when(parentMock.getNode()).thenReturn(createMockNode("element", "namespaceURI", "prefix"));
        QName name = new QName("test", "namespaceURI", "prefix");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parentMock, name);
        Attr attributeMock = mock(Attr.class);
        when(attributeMock.getNodeName()).thenReturn("test");
        when(attributeMock.getNamespaceURI()).thenReturn("namespaceURI");
        when(attributeMock.getPrefix()).thenReturn("prefix");
        List<Attr> attributes = new ArrayList<>();
        attributes.add(attributeMock);
        when(parentMock.getAttributes()).thenReturn(createMockNamedNodeMap(attributes));

        Assert.assertTrue(iterator.hasNext());
        NodePointer nodePointer = iterator.next();
        Assert.assertEquals(createDOMAttributePointer("namespaceURI", "prefix", "test"), nodePointer);
    }

    @Test
    public void testDOMAttributeIterator_SingleAttribute_DifferentNamespace() {
        NodePointer parentMock = mock(NodePointer.class);
        when(parentMock.getNode()).thenReturn(createMockNode("element", null, null));
        QName name = new QName("test");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parentMock, name);
        Attr attributeMock = mock(Attr.class);
        when(attributeMock.getNodeName()).thenReturn("test");
        when(attributeMock.getNamespaceURI()).thenReturn("namespaceURI");
        List<Attr> attributes = new ArrayList<>();
        attributes.add(attributeMock);
        when(parentMock.getAttributes()).thenReturn(createMockNamedNodeMap(attributes));

        Assert.assertTrue(iterator.hasNext());
        NodePointer nodePointer = iterator.next();
        Assert.assertEquals(createDOMAttributePointer(null, null, "test"), nodePointer);
    }

    @Test
    public void testDOMAttributeIterator_MultipleAttributes_SameNamespace() {
        NodePointer parentMock = mock(NodePointer.class);
        when(parentMock.getNode()).thenReturn(createMockNode("element", "namespaceURI", "prefix"));
        QName name = new QName("test", "namespaceURI", "prefix");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parentMock, name);
        Attr attribute1Mock = mock(Attr.class);
        Attr attribute2Mock = mock(Attr.class);
        when(attribute1Mock.getNodeName()).thenReturn("test1");
        when(attribute1Mock.getNamespaceURI()).thenReturn("namespaceURI");
        when(attribute1Mock.getPrefix()).thenReturn("prefix");
        when(attribute2Mock.getNodeName()).thenReturn("test2");
        when(attribute2Mock.getNamespaceURI()).thenReturn("namespaceURI");
        when(attribute2Mock.getPrefix()).thenReturn("prefix");
        List<Attr> attributes = new ArrayList<>();
        attributes.add(attribute1Mock);
        attributes.add(attribute2Mock);
        when(parentMock.getAttributes()).thenReturn(createMockNamedNodeMap(attributes));

        Assert.assertTrue(iterator.hasNext());
        NodePointer nodePointer1 = iterator.next();
        Assert.assertEquals(createDOMAttributePointer("namespaceURI", "prefix", "test1"), nodePointer1);

        Assert.assertTrue(iterator.hasNext());
        NodePointer nodePointer2 = iterator.next();
        Assert.assertEquals(createDOMAttributePointer("namespaceURI", "prefix", "test2"), nodePointer2);

        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testDOMAttributeIterator_MultipleAttributes_DifferentNamespaces() {
        NodePointer parentMock = mock(NodePointer.class);
        when(parentMock.getNode()).thenReturn(createMockNode("element", null, null));
        QName name1 = new QName("test1");
        QName name2 = new QName("test2");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parentMock, name1);
        Attr attribute1Mock = mock(Attr.class);
        Attr attribute2Mock = mock(Attr.class);
        when(attribute1Mock.getNodeName()).thenReturn("test1");
        when(attribute1Mock.getNamespaceURI()).thenReturn("namespaceURI1");
        when(attribute2Mock.getNodeName()).thenReturn("test2");
        when(attribute2Mock.getNamespaceURI()).thenReturn("namespaceURI2");
        List<Attr> attributes = new ArrayList<>();
        attributes.add(attribute1Mock);
        attributes.add(attribute2Mock);
        when(parentMock.getAttributes()).thenReturn(createMockNamedNodeMap(attributes));

        Assert.assertTrue(iterator.hasNext());
        NodePointer nodePointer1 = iterator.next();
        Assert.assertEquals(createDOMAttributePointer(null, null, "test1"), nodePointer1);

        iterator = new DOMAttributeIterator(parentMock, name2);

        Assert.assertTrue(iterator.hasNext());
        NodePointer nodePointer2 = iterator.next();
        Assert.assertEquals(createDOMAttributePointer(null, null, "test2"), nodePointer2);

        Assert.assertFalse(iterator.hasNext());
    }

    private Node mockNode(String tagName, String namespaceURI, String prefix) {
        Element elementMock = mock(Element.class);
        when(elementMock.getTagName()).thenReturn(tagName);
        when(elementMock.getNamespaceURI()).thenReturn(namespaceURI);
        when(elementMock.getPrefix()).thenReturn(prefix);
        Node nodeMock = mock(Node.class);
        when(nodeMock.getNodeType()).thenReturn(Node.ELEMENT_NODE);
        when(nodeMock.getParentNode()).thenReturn(elementMock);
        return nodeMock;
    }

    private NamedNodeMap createMockNamedNodeMap(List<Attr> attributes) {
        NamedNodeMap mapMock = mock(NamedNodeMap.class);
        for (Attr attr : attributes) {
            when(mapMock.getLength()).thenReturn(attributes.size());
            when(mapMock.item(0)).thenReturn(attr);
        }
        return mapMock;
    }

    private Node createMockNode(String tagName, String namespaceURI, String prefix) {
        Element elementMock = mock(Element.class);
        when(elementMock.getTagName()).thenReturn(tagName);
        when(elementMock.getNamespaceURI()).thenReturn(namespaceURI);
        when(elementMock.getPrefix()).thenReturn(prefix);
        Node nodeMock = mock(Node.class);
        when(nodeMock.getNodeType()).thenReturn(Node.ELEMENT_NODE);
        when(nodeMock.getParentNode()).thenReturn(elementMock);
        when(nodeMock.getAttributes()).thenReturn(createMockNamedNodeMap(new ArrayList<>()));
        return nodeMock;
    }

    private DOMAttributePointer createDOMAttributePointer(String namespaceURI, String prefix, String localName) {
        return new DOMAttributePointer(mock(NodePointer.class), mock(Attr.class)) {
            @Override
            public String getLocalName() {
                return localName;
            }

            @Override
            public String getNamespaceURI() {
                return namespaceURI;
            }

            @Override
            public String getPrefix() {
                return prefix;
            }
        };
    }
}