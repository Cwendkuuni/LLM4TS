package net.sf.xisemele.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OperationsHelperImplTest {

    private Document document;
    private Node rootElement;

    @Before
    public void setUp() {
        document = mock(Document.class);
        rootElement = mock(Element.class);
        when(document.getDocumentElement()).thenReturn(rootElement);
    }

    @Test
    public void testChildrenEmpty() {
        final Node emptyNode = mock(Node.class);
        when(emptyNode.getChildNodes()).thenReturn(mock(NodeList.class));
        when(emptyNode.getChildNodes().getLength()).thenReturn(0);

        final List<Node> children = new OperationsHelperImpl().children(emptyNode);

        assertEquals(0, children.size());
    }

    @Test
    public void testChildrenSingleElement() {
        final Node elementNode = mock(Element.class);
        when(elementNode.getNodeType()).thenReturn(Node.ELEMENT_NODE);
        when(elementNode.getChildNodes()).thenReturn(mock(NodeList.class));
        when(elementNode.getChildNodes().getLength()).thenReturn(1);

        final List<Node> children = new OperationsHelperImpl().children(elementNode);

        assertEquals(1, children.size());
        assertEquals(elementNode, children.get(0));
    }

    @Test
    public void testChildrenMultipleElements() {
        final Element element1 = mock(Element.class);
        final Element element2 = mock(Element.class);

        when(element1.getNodeType()).thenReturn(Node.ELEMENT_NODE);
        when(element2.getNodeType()).thenReturn(Node.ELEMENT_NODE);

        when(rootElement.getChildNodes()).thenReturn(mock(NodeList.class));
        when(rootElement.getChildNodes().getLength()).thenReturn(2);
        when(rootElement.getChildNodes().item(0)).thenReturn(element1);
        when(rootElement.getChildNodes().item(1)).thenReturn(element2);

        final List<Node> children = new OperationsHelperImpl().children(rootElement);

        assertEquals(2, children.size());
        assertEquals(element1, children.get(0));
        assertEquals(element2, children.get(1));
    }

    @Test
    public void testFindNullDocument() {
        final List<Node> children = new OperationsHelperImpl().children(document);

        assertNull(new OperationsHelperImpl().find(document, ""));
    }

    @Test
    public void testFindRootElement() {
        when(rootElement.getNodeName()).thenReturn("root");

        final Node found = new OperationsHelperImpl().find(document, "root");

        assertEquals(rootElement, found);
    }

    @Test
    public void testFindNullPath() {
        final Node rootNode = mock(Element.class);
        when(rootNode.getNodeName()).thenReturn("root");

        when(document.getDocumentElement()).thenReturn(rootNode);

        assertNull(new OperationsHelperImpl().find(document, null));
    }

    @Test
    public void testFindSingleLevel() {
        when(rootElement.getNodeName()).thenReturn("root");

        final Element childElement = mock(Element.class);
        when(childElement.getNodeName()).thenReturn("child");
        when(rootElement.getElementsByTagName("child").getLength()).thenReturn(1);
        when(rootElement.getElementsByTagName("child").item(0)).thenReturn(childElement);

        final Node found = new OperationsHelperImpl().find(document, "root/child");

        assertEquals(childElement, found);
    }

    @Test
    public void testFindMultiLevel() {
        final Element rootElement = mock(Element.class);
        when(rootElement.getNodeName()).thenReturn("root");

        final Element childElement1 = mock(Element.class);
        when(childElement1.getNodeName()).thenReturn("child1");
        when(rootElement.getElementsByTagName("child1").getLength()).thenReturn(1);
        when(rootElement.getElementsByTagName("child1").item(0)).thenReturn(childElement1);

        final Element childElement2 = mock(Element.class);
        when(childElement2.getNodeName()).thenReturn("child2");
        when(childElement1.getElementsByTagName("child2").getLength()).thenReturn(1);
        when(childElement1.getElementsByTagName("child2").item(0)).thenReturn(childElement2);

        final Node found = new OperationsHelperImpl().find(document, "root/child1/child2");

        assertEquals(childElement2, found);
    }

    @Test
    public void testFindNotFound() {
        when(rootElement.getNodeName()).thenReturn("root");

        final Node found = new OperationsHelperImpl().find(document, "root/non-existent");

        assertNull(found);
    }

    @Test
    public void testNodeWithNameNullList() {
        assertNull(new OperationsHelperImpl().nodeWithName(null, "name"));
    }

    @Test
    public void testNodeWithNameEmptyList() {
        final List<Node> emptyList = new ArrayList<>();

        assertNull(new OperationsHelperImpl().nodeWithName(emptyList, "name"));
    }

    @Test
    public void testNodeWithNameSingleElement() {
        final Element element = mock(Element.class);
        when(element.getNodeName()).thenReturn("name");

        final List<Node> nodes = new ArrayList<>();
        nodes.add(element);

        assertEquals(element, new OperationsHelperImpl().nodeWithName(nodes, "name"));
    }

    @Test
    public void testNodeWithNameMultipleElements() {
        final Element element1 = mock(Element.class);
        when(element1.getNodeName()).thenReturn("name1");

        final Element element2 = mock(Element.class);
        when(element2.getNodeName()).thenReturn("name2");

        final List<Node> nodes = new ArrayList<>();
        nodes.add(element1);
        nodes.add(element2);

        assertNull(new OperationsHelperImpl().nodeWithName(nodes, "name3"));
    }

    @Test
    public void testSublistWithNameEmptyList() {
        final List<Node> emptyList = new ArrayList<>();

        final List<Node> sublist = new OperationsHelperImpl().sublistWithName(emptyList, "name");

        assertEquals(0, sublist.size());
    }

    @Test
    public void testSublistWithNameSingleElement() {
        final Element element = mock(Element.class);
        when(element.getNodeName()).thenReturn("name");

        final List<Node> nodes = Arrays.asList(element);

        assertEquals(1, new OperationsHelperImpl().sublistWithName(nodes, "name").size());
    }

    @Test
    public void testSublistWithNameMultipleElements() {
        final Element element1 = mock(Element.class);
        when(element1.getNodeName()).thenReturn("name1");

        final Element element2 = mock(Element.class);
        when(element2.getNodeName()).thenReturn("name2");

        final List<Node> nodes = Arrays.asList(element1, element2);

        assertEquals(1, new OperationsHelperImpl().sublistWithName(nodes, "name1").size());
        assertEquals(0, new OperationsHelperImpl().sublistWithName(nodes, "name2").size());
    }
}