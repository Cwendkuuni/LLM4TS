package net.sf.xisemele.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.mockito.MockitoAnnotations;

public class OperationsHelperImplTest {

    @Test
    public void testChildren() {
        final Document document = mock(Document.class);
        final Element root = mock(Element.class);
        when(document.getDocumentElement()).thenReturn(root);

        final NodeList nodeList = mock(NodeList.class);
        when(root.getChildNodes()).thenReturn(nodeList);

        final Node node1 = mock(Node.class);
        final Node node2 = mock(Node.class);
        when(nodeList.item(0)).thenReturn(node1);
        when(nodeList.item(1)).thenReturn(node2);

        OperationsHelper operationsHelper = new OperationsHelperImpl();
        List<Node> result = operationsHelper.children(document);

        assertEquals(Arrays.asList(node1, node2), result);
    }

    @Test
    public void testChildrenEmpty() {
        final Document document = mock(Document.class);
        final Element root = mock(Element.class);
        when(document.getDocumentElement()).thenReturn(root);
        final NodeList nodeList = mock(NodeList.class);
        when(root.getChildNodes()).thenReturn(nodeList);
        when(nodeList.getLength()).thenReturn(0);

        OperationsHelper operationsHelper = new OperationsHelperImpl();
        List<Node> result = operationsHelper.children(document);

        assertTrue(result.isEmpty());
    }

    @Test
    public void testChildrenNullDocument() {
        OperationsHelper operationsHelper = new OperationsHelperImpl();
        List<Node> result = operationsHelper.children(null);

        assertNull(result);
    }

    @Test
    public void testFind() {
        final Document document = mock(Document.class);
        final Element root = mock(Element.class);
        when(document.getDocumentElement()).thenReturn(root);
        final Element child1 = mock(Element.class);
        when(child1.getNodeName()).thenReturn("child1");
        final Element child2 = mock(Element.class);
        when(child2.getNodeName()).thenReturn("child2");
        when(root.getChildNodes()).thenReturn(mock(NodeList.class));
        when(document.getDocumentElement().getNodeName()).thenReturn("root");

        OperationsHelper operationsHelper = new OperationsHelperImpl();
        Node result = operationsHelper.find(document, "/root/child1");

        assertEquals(child1, result);
    }

    @Test
    public void testFindNullDocument() {
        OperationsHelper operationsHelper = new OperationsHelperImpl();
        Node result = operationsHelper.find(null, "/root/child1");

        assertNull(result);
    }

    @Test
    public void testFindNullPath() {
        final Document document = mock(Document.class);
        final Element root = mock(Element.class);
        when(document.getDocumentElement()).thenReturn(root);

        OperationsHelper operationsHelper = new OperationsHelperImpl();
        Node result = operationsHelper.find(document, null);

        assertNull(result);
    }

    @Test
    public void testFindNonExistentPath() {
        final Document document = mock(Document.class);
        final Element root = mock(Element.class);
        when(document.getDocumentElement()).thenReturn(root);

        OperationsHelper operationsHelper = new OperationsHelperImpl();
        Node result = operationsHelper.find(document, "/root/nonExistent");

        assertNull(result);
    }

    @Test
    public void testFindExistingPathWithMultipleNodes() {
        final Document document = mock(Document.class);
        final Element root = mock(Element.class);
        when(document.getDocumentElement()).thenReturn(root);
        final Element child1 = mock(Element.class);
        final Element child2 = mock(Element.class);
        when(child1.getNodeName()).thenReturn("child1");
        when(child2.getNodeName()).thenReturn("child1");
        when(root.getChildNodes()).thenReturn(mock(NodeList.class));

        OperationsHelper operationsHelper = new OperationsHelperImpl();
        Node result = operationsHelper.find(document, "/root/child1");

        List<Node> children = Arrays.asList(child1, child2);
        assertEquals(children.get(0), result);
    }

    @Test
    public void testNodeWithName() {
        final List<Node> nodes = Arrays.asList(mock(Node.class), mock(Node.class), mock(Node.class));
        OperationsHelper operationsHelper = new OperationsHelperImpl();
        Node result = operationsHelper.nodeWithName(nodes, "nodeName");

        assertNull(result);
    }

    @Test
    public void testNodeWithNameNullList() {
        OperationsHelper operationsHelper = new OperationsHelperImpl();
        Node result = operationsHelper.nodeWithName(null, "nodeName");

        assertNull(result);
    }

    @Test
    public void testNodeWithNameEmptyList() {
        OperationsHelper operationsHelper = new OperationsHelperImpl();
        Node result = operationsHelper.nodeWithName(Collections.emptyList(), "nodeName");

        assertNull(result);
    }

    @Test
    public void testNodeWithNameSingleMatch() {
        final Node node = mock(Node.class);
        when(node.getNodeName()).thenReturn("nodeName");

        final List<Node> nodes = Arrays.asList(node, mock(Node.class), mock(Node.class));
        OperationsHelper operationsHelper = new OperationsHelperImpl();
        Node result = operationsHelper.nodeWithName(nodes, "nodeName");

        assertEquals(node, result);
    }

    @Test
    public void testNodeWithNameMultipleMatches() {
        final Node node1 = mock(Node.class);
        final Node node2 = mock(Node.class);
        when(node1.getNodeName()).thenReturn("nodeName");
        when(node2.getNodeName()).thenReturn("nodeName");

        final List<Node> nodes = Arrays.asList(node1, node2, mock(Node.class));
        OperationsHelper operationsHelper = new OperationsHelperImpl();
        Node result = operationsHelper.nodeWithName(nodes, "nodeName");

        List<Node> expected = Arrays.asList(node1, node2);
        assertTrue(expected.contains(result));
    }

    @Test
    public void testSublistWithNameEmptyList() {
        OperationsHelper operationsHelper = new OperationsHelperImpl();
        List<Node> result = operationsHelper.sublistWithName(Collections.emptyList(), "name");

        assertTrue(result.isEmpty());
    }

    @Test
    public void testSublistWithNameNullList() {
        OperationsHelper operationsHelper = new OperationsHelperImpl();
        List<Node> result = operationsHelper.sublistWithName(null, "name");

        assertNull(result);
    }

    @Test
    public void testSublistWithNameSingleNode() {
        final Node node = mock(Node.class);
        when(node.getNodeName()).thenReturn("name");

        OperationsHelper operationsHelper = new OperationsHelperImpl();
        List<Node> result = operationsHelper.sublistWithName(Arrays.asList(node), "name");

        assertEquals(Arrays.asList(node), result);
    }

    @Test
    public void testSublistWithNameMultipleNodes() {
        final Node node1 = mock(Node.class);
        final Node node2 = mock(Node.class);
        when(node1.getNodeName()).thenReturn("name");
        when(node2.getNodeName()).thenReturn("name");

        final List<Node> nodes = Arrays.asList(node1, node2, mock(Node.class));
        OperationsHelper operationsHelper = new OperationsHelperImpl();
        List<Node> result = operationsHelper.sublistWithName(nodes, "name");

        List<Node> expected = Arrays.asList(node1, node2);
        assertEquals(expected, result);
    }

    @Test
    public void testSublistWithNameNoMatches() {
        final Node node = mock(Node.class);
        when(node.getNodeName()).thenReturn("otherName");

        final List<Node> nodes = Arrays.asList(node, mock(Node.class), mock(Node.class));
        OperationsHelper operationsHelper = new OperationsHelperImpl();
        List<Node> result = operationsHelper.sublistWithName(nodes, "name");

        assertTrue(result.isEmpty());
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }
}